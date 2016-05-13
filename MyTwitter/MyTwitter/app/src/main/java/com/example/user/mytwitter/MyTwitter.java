package com.example.user.mytwitter;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

/**
 * Created by kkx358 on 3/22/2016.
 */
public class MyTwitter extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    public final static String TAG="MyTwitter";

    public final static String STATUS_CHANGE_ALERT = "com.example.user.mytwitter.STATUS_CHANGED";

    private Twitter twitter;
    private String username,password,server;
    int delay;
    SharedPreferences prefs;
    StatusData statusData;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        /*username=prefs.getString("username", "");
        password=prefs.getString("password","");
        server=prefs.getString("server","");

        twitter=new Twitter(username,password);
        twitter.setAPIRootUrl(server);*/
        Log.d(TAG, "Application created");

        prefs.registerOnSharedPreferenceChangeListener(this);

        statusData = new StatusData(this);
    }

    public Twitter getTwitter(){

        if (twitter==null){
            username=prefs.getString("username", "");
            password=prefs.getString("password","");
            server=prefs.getString("server","");
            String sync=prefs.getString("delay","100");
            delay=Integer.parseInt(sync);
            Log.d(TAG,""+delay);

            twitter=new Twitter(username,password);
            twitter.setAPIRootUrl(server);

        }


        return twitter;

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG,key);
        twitter=null;
        username=prefs.getString("username","");
        password=prefs.getString("password","");
        server=prefs.getString("server","");

        twitter=new Twitter(username,password);
        twitter.setAPIRootUrl(server);
    }

    public void pullAndStore(){
        List<Twitter.Status> statuses= null;
        try {
            statuses = getTwitter().getHomeTimeline();
            for (Twitter.Status status:statuses) {
                Log.d(TAG, String.format("User: %s, Status: %s", status.user.name, status.text));
                statusData.insert(status);
            }
        } catch (TwitterException e) {
            //e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }
    }
}
