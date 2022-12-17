package com.example.geometry.utils;

import android.app.Application;

import com.example.common_utils.repository.MongoDb;

public class GeometryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MongoDb.connectDB(this);
    }
}
