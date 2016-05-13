package com.mysampleapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mysampleapp.LocalDatabaseHelper;

/**
 * Created by Richard on 5/2/2016.
 */
public class MyClock extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{

    public final static String TAG="MyClock";

    String mode,username,location;

    SharedPreferences preferences;

    LocalDatabaseHelper localData;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        Log.d(TAG,"Application Created");
        preferences.registerOnSharedPreferenceChangeListener(this);

        localData = new LocalDatabaseHelper(this);
    }

    public String getUsername(){
        return preferences.getString("username", "");
    }

    public String getMode(){
        return preferences.getString("mode","manual");
    }

    public String getLocation(){
        return preferences.getString("location","home");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG,key);
        username=preferences.getString("username","");
        mode=preferences.getString("mode","manual");
        location=preferences.getString("location","home");
    }
    public void updateDatabase(){
        localData.queryAllUsers();
    }
}
