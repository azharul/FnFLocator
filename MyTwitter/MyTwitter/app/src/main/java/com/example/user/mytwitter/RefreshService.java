package com.example.user.mytwitter;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class RefreshService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String TAG="RefreshService";
    public static final String ACTION_FOO = "com.example.user.mytwitter.action.FOO";
    public static final String ACTION_BAZ = "com.example.user.mytwitter.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.user.mytwitter.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.user.mytwitter.extra.PARAM2";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*Twitter twitter=new Twitter("student","password");
        twitter.setAPIRootUrl("http://yamba.newcircle.com/api");*/

        /*List<Twitter.Status> statuses= null;
        try {
            statuses = ((MyTwitter)getApplication()).getTwitter().getHomeTimeline();
            for (Twitter.Status status:statuses) {
                Log.d(TAG, String.format("User: %s, Status: %s", status.user.name, status.text));
                ((MyTwitter)getApplication()).statusData.insert(status);
            }
        } catch (TwitterException e) {
            //e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }*/
        ((MyTwitter) getApplication()).pullAndStore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
