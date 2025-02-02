package com.example.registro.data.service;

import com.example.registro.data.handlers.AuthResponseHandler;
import com.example.registro.data.model.Message;
import com.example.registro.data.model.ServerResponse;
import com.example.registro.data.model.User;
import com.example.registro.data.remote.SocketClient;

public class AuthService {
    private final SocketClient socketClient;
    private final AuthResponseHandler authResponseHandler;
    public interface AuthCallback {
        void onSuccess(String userId);
        void onError(String error);
    }

    public AuthService(SocketClient socketClient) {
        this.socketClient = socketClient;
        this.authResponseHandler = new AuthResponseHandler();
    }

    public void register(User user, AuthCallback callback) {
        Message registrationMessage = Message.createRegistrationMessage(user);
        if (registrationMessage != null) {
            sendAuthMessage(registrationMessage, callback);
        } else {
            callback.onError("Error creating registration message");
        }
    }

    public void login(String username, String password, AuthCallback callback) {
        Message loginMessage = Message.createLoginMessage(username, password);
        sendAuthMessage(loginMessage, callback);
    }

    private void sendAuthMessage(Message message, AuthCallback callback) {
        socketClient.sendMessage(message.toJson(), new SocketClient.SocketCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    ServerResponse serverResponse = new ServerResponse(response);
                    boolean success = authResponseHandler.handleResponse(serverResponse);

                    if (success) {
                        String userId = serverResponse.getPayload().getString("userId");
                        callback.onSuccess(userId); //Pass the user ID back
                    } else {
                        callback.onError(serverResponse.getMessage());
                    }
                } catch (Exception e) {
                    callback.onError("Error processing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError("Network error: " + error);
            }
        });
    }
}