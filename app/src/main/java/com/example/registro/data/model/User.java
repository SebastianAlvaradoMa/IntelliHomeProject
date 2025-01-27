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
    private String photoPath;

    //Datos Tarjeta
    private String expiracionM;
    private String expiracionA;
    private String cuentaIban;
    private  String tarjeta;
    private String Pin;
    private String hospital;
    private String lugarFavorito;
    private String mascota;
    //Path(??)

    public User(String nombre, String apellidos, String username, String email, String password, String fechaNacimiento, String gender,
                String nacionalidad, String pasatiempos, String photoPath, String expiracionM, String expiracionA, String cuentaIban, String tarjeta, String Pin, String hospital, String lugarFavorito, String mascota) {
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
        this.expiracionM = expiracionM;
        this.expiracionA = expiracionA;
        this.cuentaIban = cuentaIban;
        this.tarjeta = tarjeta;
        this.Pin = Pin;
        this.hospital = hospital;
        this.lugarFavorito = lugarFavorito;
        this.mascota = mascota;
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
            payloadObject.put("expiracionM", expiracionM);
            payloadObject.put("expiracionA", expiracionA);
            payloadObject.put("tarjeta", tarjeta);
            payloadObject.put("iban", cuentaIban);
            payloadObject.put("pin", Pin);
            payloadObject.put("hospital", hospital);
            payloadObject.put("lugarFavorito", lugarFavorito);
            payloadObject.put("mascota", mascota);

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
    public String getExpiracionM() { return expiracionM; }
    public String getExpiracionA() { return expiracionA; }
    public String getTarjeta() { return tarjeta; }
    public String getPin() { return Pin; }
    public String getHospital() { return hospital; }
    public String getLugarFavorito() { return lugarFavorito; }
    public String getMascota() { return mascota; }
}
