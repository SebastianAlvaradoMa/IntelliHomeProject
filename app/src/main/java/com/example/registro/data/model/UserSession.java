package com.example.registro.data.model;

import android.app.Application;

public class UserSession extends Application {


    private static UserSession instance;
    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }


}


