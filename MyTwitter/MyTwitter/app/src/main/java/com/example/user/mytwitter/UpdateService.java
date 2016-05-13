package com.example.user.mytwitter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class UpdateService extends Service {

    public static final String TAG="UpdateService";
    //public static final int DELAY=5;
    boolean running=true;
    Twitter twitter;


    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");
        /*twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.newcircle.com/api");*/
        twitter=((MyTwitter)getApplication()).getTwitter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"OnStartCommand");
        new Thread() {
            public void run() {
                while(running) {
                    /*try {
                        List<Twitter.Status> statuses = twitter.getHomeTimeline();
                        for (Twitter.Status status : statuses)
                            Log.d(TAG, status.user.name + ": " + status.text);
                        Thread.sleep(((MyTwitter)getApplication()).delay * 1000);
                    }
                    catch (TwitterException e){
                        Log.d(TAG,e.getMessage());
                    } catch (InterruptedException e) {
                        Log.d(TAG,e.getMessage());
                    }*/
                    try {
                        ((MyTwitter) getApplication()).pullAndStore();
                        Thread.sleep(((MyTwitter) getApplication()).delay * 1000);
                    }
                    catch (InterruptedException e){
                        Log.d(TAG,e.getMessage());
                    }
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        running=false;
    }
}
