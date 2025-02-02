package com.example.registro.data.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Property {
    private String name;
    private double price;
    private String contact;
    private int maxPeople;
    private double latitude;
    private double longitude;
    private String amenidadesElegidas;
    private String mascotasSelection;

    public Property(String name, double price, String contact, int maxPeople, double latitude, double longitude, String mascotasSelection, String amenidadesElegidas) {
        this.name = name;
        this.price = price;
        this.contact = contact;
        this.maxPeople = maxPeople;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mascotasSelection = mascotasSelection;
        this.amenidadesElegidas = amenidadesElegidas;
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("nombrePropiedad", name);
            json.put("mascotas", mascotasSelection);
            json.put("precio", price);
            json.put("contacto", contact);
            json.put("maxPersonas", maxPeople);
            json.put("latitud", latitude);
            json.put("longitud", longitude);
            json.put("amenidades", amenidadesElegidas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}