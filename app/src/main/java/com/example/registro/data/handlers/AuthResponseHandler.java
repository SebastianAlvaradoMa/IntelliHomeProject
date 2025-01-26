package com.example.registro.data.handlers;

import com.example.registro.data.model.ServerResponse;

public class AuthResponseHandler implements ResponseHandler<Boolean> {
    @Override
    public Boolean handleResponse(ServerResponse response) {
        if (response == null) {
            return false;
        }
        // Add logging to track the response
        System.out.println("Auth Response - Status: " + response.getStatus() +
                ", Message: " + response.getMessage());
        return response.isSuccess();
    }
}