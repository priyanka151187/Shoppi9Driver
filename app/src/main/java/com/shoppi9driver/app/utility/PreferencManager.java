package com.shoppi9driver.app.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class PreferencManager {

    Set<String> strings;
    private Context _context;

    public PreferencManager(Context context) {
        this._context = context;

    }
    public Context getContext() {
        return _context;
    }

    public void setContext(Context context) {
        this._context = context;
    }

    protected SharedPreferences getSharedPreferences(String key) {
        return PreferenceManager.getDefaultSharedPreferences(_context);
    }

    public String getString(String key, String def) {
        SharedPreferences prefs = getSharedPreferences(key);
        return prefs.getString(key, def);
    }

    public void setString(String key, String val) {
        SharedPreferences prefs = getSharedPreferences(key);
        SharedPreferences.Editor e = prefs.edit();
        e.putString(key, val);
        e.apply();
    }
    public void setBoolean(String key, Boolean val) {
        SharedPreferences prefs = getSharedPreferences(key);
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean(key, val);
        e.apply();
    }
    public Boolean getBoolean(String key, Boolean def) {
        SharedPreferences prefs = getSharedPreferences(key);
        return prefs.getBoolean(key, def);
    }
    public String getToken(String key, String def) {
        SharedPreferences prefs = getSharedPreferences(key);
        String b = prefs.getString(key, def);
        return b;
    }


    public void setToken(String key, String val) {
        SharedPreferences prefs = getSharedPreferences(key);
        SharedPreferences.Editor e = prefs.edit();
        e.putString(key, val);
        e.apply();
    }
}
