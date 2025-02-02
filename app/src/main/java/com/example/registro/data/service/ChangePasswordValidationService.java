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
                try {
                    // Parse the server response
                    ServerResponse serverResponse = new ServerResponse(response);

                    // Check if the response is successful and contains a valid userId
                    if ("success".equals(serverResponse.getStatus())) {
                        String userId = serverResponse.getPayload().getString("userId");
                        if (!userId.isEmpty()) {
                            // Pass the raw JSON response to the onSuccess callback
                            callback.onSuccess(response); // Pass the raw JSON response
                        } else {
                            // userId is missing or invalid
                            callback.onError("Invalid user ID in server response");
                        }
                    } else {
                        // Server returned an error
                        callback.onError(serverResponse.getMessage());
                    }
                } catch (Exception e) {
                    // Error parsing the server response
                    callback.onError("Error processing server response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError("Network error: " + error);
            }
        });
    }

    public void changePassword(String userID, String newPassword, ChangePasswordValidationCallback callback) {

        Message changePasswordMessage = Message.createChangePasswordMessage(userID, newPassword);
        if (changePasswordMessage == null) {
            callback.onError("Error sending new password");
            return;
        }

        socketManager.sendMessage(changePasswordMessage.toJson(), new SocketClient.SocketCallback() {
            @Override
            public void onResponse(String response) {
                // Empty response means success according to your server implementation
                if (response.isEmpty()) {
                    callback.onSuccess("New password sent succesfully.");
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
