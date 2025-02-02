package com.example.registro.data.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Property {
    private String userId;
    private String name;
    private double price;
    private String contact;
    private int maxPeople;
    private double latitude;
    private double longitude;
    private String amenidadesElegidas;
    private String mascotasSelection;

    public Property( String userId, String name, double price, String contact, int maxPeople, double latitude, double longitude, String mascotasSelection, String amenidadesElegidas) {
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.contact = contact;
        this.maxPeople = maxPeople;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mascotasSelection = mascotasSelection;
        this.amenidadesElegidas = amenidadesElegidas;
    }

    public Property() {

    }

    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
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


    public String getDescription() {
        // You can add a description field or generate it from existing fields
        return "Price: " + price + ", Max People: " + maxPeople;
    }

    // Getters
    public String getUserId() { return userId; }

    public String getName() { return name; }
    public String getPetsAllowed() { return mascotasSelection; }
    public double getPrice() { return price; }
    public String getContact() { return contact; }
    public int getMaxPeople() { return maxPeople; }


    // Setters
    public void setUserId(String userId) { this.userId = userId; }

    public void setName(String name) { this.name = name; }
    public void setPetsAllowed(String petsAllowed) { mascotasSelection= petsAllowed; }
    public void setPrice(double price) { this.price = price; }
    public void setContact(String contact) { this.contact = contact; }
    public void setMaxPeople(int maxPeople) { this.maxPeople = maxPeople; }

    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

}