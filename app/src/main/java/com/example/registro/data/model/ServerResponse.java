package com.example.registro.data.model;

import org.json.JSONException;
import org.json.JSONObject;
public class ServerResponse {
    private String status;
    private String message;
    private JSONObject data;

    public ServerResponse(String jsonResponse) throws JSONException {
        JSONObject response = new JSONObject(jsonResponse);
        this.status = response.optString("status", "");
        this.message = response.optString("message", "");
        this.data = response.optJSONObject("data");
    }

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public JSONObject getData() { return data; }
}

