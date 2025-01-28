package com.example.registro.data.service;

import com.example.registro.data.model.Message;
import com.example.registro.data.model.ServerResponse;
import com.example.registro.data.remote.SocketClient;
import com.example.registro.data.remote.SocketManager;

public class LightService {
    private final SocketManager socketManager;
    public interface LightCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public LightService() {
        this.socketManager = SocketManager.getInstance();
    }

    public void controlLight(String room, boolean turnOn, LightCallback callback) {
        // Create the command in the format expected by the server
        String command = (turnOn ? "On" : "Off") + room;

        Message lightMessage = Message.createLightsControlMessage(command);
        if (lightMessage == null) {
            callback.onError("Error creating light control message");
            return;
        }

        socketManager.sendMessage(lightMessage.toJson(), new SocketClient.SocketCallback() {
            @Override
            public void onResponse(String response) {
                // Empty response means success according to your server implementation
                if (response.isEmpty()) {
                    callback.onSuccess("Light command executed successfully");
                    return;
                }

                try {
                    ServerResponse serverResponse = new ServerResponse(response);
                    if ("error".equals(serverResponse.getStatus())) {
                        callback.onError(serverResponse.getMessage());
                    } else {
                        callback.onSuccess(serverResponse.getMessage());
                    }
                } catch (Exception e) {
                    callback.onError("Error processing server response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError("Network error: " + error);
            }
        });
    }
}