package com.shoppi9driver.app.utility;

import android.app.Application;

public class MyApplication extends Application {

    private PreferencManager preferences;;
    @Override
    public void onCreate() {
        super.onCreate();

        doInit();
    }

    private void doInit() {
        this.preferences = new PreferencManager(this);
    }

    public synchronized PreferencManager getPreferences() {
        return preferences;
    }

}
