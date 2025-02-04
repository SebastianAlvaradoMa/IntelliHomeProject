package com.example.registro.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Property implements Parcelable {
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

    protected Property(Parcel in) {
        userId = in.readString();
        name = in.readString();
        price = in.readDouble();
        contact = in.readString();
        maxPeople = in.readInt();
        mascotasSelection = in.readString();
        amenidadesElegidas = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
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


    // Parcelable Creator
    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(contact);
        dest.writeInt(maxPeople);
        dest.writeString(mascotasSelection);
        dest.writeString(amenidadesElegidas);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }


    public String getDescription() {
        // You can add a description field or generate it from existing fields
        return "Price: " + price + ", Max People: " + maxPeople;
    }

    // Getters
    public String getUserId() { return userId; }

    public String getName() { return name; }
    public String getAmenidadesElegidas() { return amenidadesElegidas; }
    public String getPetsAllowed() { return mascotasSelection; }
    public double getPrice() { return price; }
    public String getContact() { return contact; }
    public int getMaxPeople() { return maxPeople; }


    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setAmenidadesElegidas(String amenidadesElegidas) { this.amenidadesElegidas = amenidadesElegidas; }

    public void setName(String name) { this.name = name; }
    public void setPetsAllowed(String petsAllowed) { mascotasSelection= petsAllowed; }
    public void setPrice(double price) { this.price = price; }
    public void setContact(String contact) { this.contact = contact; }
    public void setMaxPeople(int maxPeople) { this.maxPeople = maxPeople; }

    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

}