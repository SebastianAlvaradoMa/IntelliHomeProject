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
    private String amenity1;
    private String amenity2;
    private String amenity3;
    private String amenity4;
    private String mascotasSelection;

    public Property(String name, double price, String contact, int maxPeople, double latitude, double longitude, String mascotasSelection, String amenity1, String amenity2, String amenity3, String amenity4) {
        this.name = name;
        this.price = price;
        this.contact = contact;
        this.maxPeople = maxPeople;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mascotasSelection = mascotasSelection;
        this.amenity1 = amenity1;
        this.amenity2 = amenity2;
        this.amenity3 = amenity3;
        this.amenity4 = amenity4;
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
            json.put("amenidad1", amenity1);
            json.put("amenidad2", amenity2);
            json.put("amenidad3", amenity3);
            json.put("amenidad4", amenity4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}