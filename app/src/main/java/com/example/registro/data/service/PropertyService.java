package com.example.registro.data.service;

import com.example.registro.data.model.Property;
import com.example.registro.data.model.ServerResponse;
import com.example.registro.data.repository.PropertyRepository;

public class PropertyService {
    private final PropertyRepository propertyRepository;

    public interface PropertyCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public PropertyService() {
        this.propertyRepository = new PropertyRepository();
    }

    public void registerProperty(String name, double price, String contact, int maxPeople,
                                 double latitude, double longitude, String mascotasSelection,
                                 String amenity1, String amenity2, String amenity3, String amenity4,
                                 PropertyCallback callback) {
        // Create Property object from parameters
        Property property = new Property(name, price, contact, maxPeople,
                latitude, longitude, mascotasSelection,
                amenity1, amenity2, amenity3, amenity4);

        // Use repository to handle the registration
        propertyRepository.registerProperty(property, new PropertyRepository.PropertyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    ServerResponse serverResponse = new ServerResponse(response);
                    if ("error".equals(serverResponse.getStatus())) {
                        callback.onError(serverResponse.getMessage());
                    } else {
                        callback.onSuccess(serverResponse.getMessage());
                    }
                } catch (Exception e) {
                    callback.onError("Error processing server response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError("Network error: " + error);
            }
        });
    }
}
