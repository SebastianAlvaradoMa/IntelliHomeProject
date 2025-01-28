package com.example.registro.data.model;
import com.example.registro.data.model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class Message {
    public static final String ACTION_LOGIN = "LOGIN";
    public static final String ACTION_REGISTER = "REGISTER";
    public static final String ACTION_LUCES = "LUCES";
    private String action;
    private JSONObject payload;
    private String status;
    private String message;

    public Message(String action, JSONObject payload) {
        this.action = action;
        this.payload = payload;
        this.status = "";
        this.message = "";
    }

    // Constructor that takes a JSON string
    public Message(String jsonStr) throws JSONException {
        JSONObject json = new JSONObject(jsonStr);
        this.action = json.getString("action");
        this.payload = json.getJSONObject("payload");
        this.status = json.optString("status", "");
        this.message = json.optString("message", "");
    }


    public static Message createLoginMessage(String usernameOrEmail, String password) {
        JSONObject payload = new JSONObject();
        try {
            // Determine if input is an email (contains @) or username
            if (usernameOrEmail.contains("@")) {
                payload.put("email", usernameOrEmail);
            } else {
                payload.put("username", usernameOrEmail);
            }
            payload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Message(ACTION_LOGIN, payload);
    }

    //Metodo que crea un mensaje de control de luces
    public static Message createLightsControlMessage(String command) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("command", command);
            return new Message(ACTION_LUCES, payload);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Metodo que crea un mensaje de registrar propiedad
//    public static Message createRegisterPropiedadMessage(Propiedad propiedad) {
//        try {
//            JSONObject propiedadJson = new JSONObject(propiedad.toJson());
//            return new Message(ACTION_REGISTER_PROPIEDAD, propiedadJson.getJSONObject("payload"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    public static Message createRegistrationMessage(User user) {
        try {
            JSONObject userJson = new JSONObject(user.toJson());
            return new Message(ACTION_REGISTER, userJson.getJSONObject("payload"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Message createLucesMessage(User user) {
        try {
            JSONObject userJson = new JSONObject(user.toJson());
            return new Message(ACTION_REGISTER, userJson.getJSONObject("payload"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", action);
            json.put("payload", payload);
            if (!status.isEmpty()) json.put("status", status);
            if (!message.isEmpty()) json.put("message", message);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    // Getters and setters
    public String getAction() { return action; }
    public JSONObject getPayload() { return payload; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }

    public void setStatus(String status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
}