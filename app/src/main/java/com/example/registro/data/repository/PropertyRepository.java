package com.example.registro.data.repository;

import com.example.registro.data.model.Message;
import com.example.registro.data.model.Property;
import com.example.registro.data.remote.SocketClient;
import com.example.registro.data.remote.SocketManager;

public class PropertyRepository {
    private final SocketManager socketManager;

    public PropertyRepository() {
        this.socketManager = SocketManager.getInstance();
    }

    public void registerProperty(Property property, PropertyCallback callback) {
        Message propertyMessage = Message.createRegisterPropertyMessage(property);
        if (propertyMessage == null) {
            callback.onError("Error creating property registration message");
            return;
        }

        socketManager.sendMessage(propertyMessage.toJson(), new SocketClient.SocketCallback() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public interface PropertyCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}