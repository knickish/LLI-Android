package com.pwny.sauruk.preptracker.m_JSON;

import android.app.Application;
import android.content.Context;

public class ContextChecker extends Application {
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}

