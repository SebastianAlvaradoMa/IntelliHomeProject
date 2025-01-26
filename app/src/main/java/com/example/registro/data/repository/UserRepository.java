package com.example.registro.data.repository;

import com.example.registro.data.model.User;
import com.example.registro.data.remote.SocketClient;
import com.example.registro.data.remote.SocketManager;
import com.example.registro.data.service.AuthService;

public class UserRepository {
    private final AuthService authService;

    public UserRepository() {
        // Retrieve the SocketClient from SocketManager
        SocketClient socketClient = SocketManager.getInstance().getSocketClient();
        this.authService = new AuthService(socketClient);
    }

    public void register(User user, RegistrationCallback callback) {
        authService.register(user, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                callback.onSuccess(message);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void login(String username, String password, LoginCallback callback) {
        authService.login(username, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                callback.onSuccess(message);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public interface RegistrationCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
