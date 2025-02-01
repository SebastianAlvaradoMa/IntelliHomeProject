package com.example.registro.data.service;

import com.example.registro.data.model.Message;
import com.example.registro.data.model.ServerResponse;
import com.example.registro.data.remote.SocketClient;
import com.example.registro.data.remote.SocketManager;

public class ChangePasswordValidationService {
    private final SocketManager socketManager;
    public interface ChangePasswordValidationCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public ChangePasswordValidationService() {
        this.socketManager = SocketManager.getInstance();
    }

    public void changePasswordValidation(String email, String pregunta1, String pregunta2, String pregunta3, ChangePasswordValidationCallback callback) {

        Message changePasswordMessage = Message.createChangePasswordValidationMessage(email, pregunta1, pregunta2, pregunta3);
        if (changePasswordMessage == null) {
            callback.onError("Error creating ChangePassword request");
            return;
        }

        socketManager.sendMessage(changePasswordMessage.toJson(), new SocketClient.SocketCallback() {
            @Override
            public void onResponse(String response) {
                // Empty response means success according to your server implementation
                if (response.isEmpty()) {
                    callback.onSuccess("ChangePassword request sent succesfully.");
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
