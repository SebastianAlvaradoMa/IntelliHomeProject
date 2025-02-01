package com.example.registro.data.remote;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


//LA comunicación va SocketClient -> SocketManager -> AuthService -> UserRepository
public class SocketClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String serverIp;
    private final int serverPort;
    private final Handler mainHandler;
    private boolean isConnected = false;

    public interface SocketCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public SocketClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void connect(SocketCallback callback) {
        new Thread(() -> {
            try {
                socket = new Socket(serverIp, serverPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                isConnected = true;
                mainHandler.post(() -> callback.onResponse("Connected"));
            } catch (Exception e) {
                isConnected = false;
                mainHandler.post(() -> callback.onError("Connection failed: " + e.getMessage()));
            }
        }).start();
    }

    public void sendMessage(String message, SocketCallback callback) {
        if (!isConnected) {
            mainHandler.post(() -> callback.onError("Not connected to server"));
            return;
        }

        new Thread(() -> {
            try {
                //Send the message
                out.println(message);
                out.flush(); //Ensure the message is sent immediately

                //Wait for response with timeout
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                    if (line.contains("}")) {  // Check for end of JSON
                        break;
                    }
                }

                String finalResponse = response.toString();
                if (!finalResponse.isEmpty()) {
                    mainHandler.post(() -> callback.onResponse(finalResponse));
                } else {
                    mainHandler.post(() -> callback.onError("No response from server"));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("Error sending message: " + e.getMessage()));
                disconnect();
                isConnected = false;
            }
        }).start();
    }

    public void disconnect() {
        try {
            isConnected = false;
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected && socket != null && !socket.isClosed();
    }
}