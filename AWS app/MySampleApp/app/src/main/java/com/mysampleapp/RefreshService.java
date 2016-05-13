package com.mysampleapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.mysampleapp.AndroidLocationDO;
import com.mysampleapp.RemoteDatabaseHelper;

import java.sql.Ref;

/**
 * Created by Richard on 5/2/2016.
 */
public class RefreshService extends IntentService {

    SharedPreferences preferences;
    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;


    public static final String TAG="RefreshService";
    @Override
    public void onCreate() {
        super.onCreate();

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();

        preferences= PreferenceManager.getDefaultSharedPreferences(this);


    }

    public RefreshService(){
        super("RefreshService");
    }

    public RefreshService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RemoteDatabaseHelper helper=new RemoteDatabaseHelper();
        helper.RemoteDBretrieveAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
