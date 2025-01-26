package com.example.registro.data.model;
import org.json.JSONException;
import org.json.JSONObject;

//Clase de usuario
public class User {
    private String nombre;
    private String apellidos;
    private String username;
    private String email;

    private String password;
    private String fechaNacimiento;

    private String gender;

    private String nacionalidad;
    private String pasatiempos;
    private String photoPath;  //Path(??)

    public User(String nombre, String apellidos, String username, String email, String password, String fechaNacimiento, String gender,
                String nacionalidad, String pasatiempos, String photoPath) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.gender=gender;
        this.nacionalidad = nacionalidad;
        this.pasatiempos = pasatiempos;
        this.photoPath = photoPath;
    }

    //Convert user data to json format for transmission
    public String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject payloadObject = new JSONObject();

            // Add fields to the payload
            payloadObject.put("nombre", nombre);
            payloadObject.put("apellidos", apellidos);
            payloadObject.put("username", username);
            payloadObject.put("email", email);
            payloadObject.put("password", password);
            payloadObject.put("fechaNacimiento", fechaNacimiento);
            payloadObject.put("gender", gender);
            payloadObject.put("nacionalidad", nacionalidad);
            payloadObject.put("pasatiempos", pasatiempos);
            payloadObject.put("photoPath", photoPath);

            // Wrap in the client request format
            jsonObject.put("action", "REGISTER");
            jsonObject.put("payload", payloadObject);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}"; // Return an empty JSON object in case of an error
        }
}


    //Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public String getPasatiempos() { return pasatiempos; }
    public String getPhotoPath() { return photoPath; }
}
