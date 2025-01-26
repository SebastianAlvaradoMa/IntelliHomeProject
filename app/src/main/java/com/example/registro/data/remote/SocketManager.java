package com.example.registro.data.remote;

public class SocketManager {
    private static SocketManager instance;
    private SocketClient socketClient;
    private boolean isConnected;

    private SocketManager() {
        //Initialize the SocketClient
        socketClient = new SocketClient("10.0.2.2", 1717);
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void connect(SocketClient.SocketCallback callback) {
        if (!isConnected) {
            socketClient.connect(new SocketClient.SocketCallback() {
                @Override
                public void onResponse(String response) {
                    isConnected = true;
                    callback.onResponse(response);
                }

                @Override
                public void onError(String error) {
                    callback.onError(error);
                }
            });
        } else {
            callback.onResponse("Already connected");
        }
    }

    public void sendMessage(String message, SocketClient.SocketCallback callback) {
        if (isConnected) {
            socketClient.sendMessage(message, callback);
        } else {
            callback.onError("Not connected");
        }
    }

    public void disconnect() {
        if (isConnected) {
            socketClient.disconnect();
            isConnected = false;
        }
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }
}
