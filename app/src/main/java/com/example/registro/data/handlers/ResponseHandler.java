package com.example.registro.data.handlers;

import com.example.registro.data.model.ServerResponse;

import org.json.JSONException;

public interface ResponseHandler<T> {
    T handleResponse(ServerResponse response) throws JSONException;
}

