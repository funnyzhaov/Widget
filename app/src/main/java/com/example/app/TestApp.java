package com.example.app;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


public class TestApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
