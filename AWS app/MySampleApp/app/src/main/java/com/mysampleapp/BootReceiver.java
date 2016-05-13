package com.mysampleapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by User on 5/5/2016.
 */
public class BootReceiver extends BroadcastReceiver{

    final public static String TAG="LoginActivity";

    public BootReceiver(){

    }

    public void onReceive(Context context, Intent intent){
        // Alarm manager object
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        // Targets the intended service
        PendingIntent operation= PendingIntent.getService(context,0,new Intent(context,RefreshService.class),0);
        // set the repeating interval
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,10000,AlarmManager.INTERVAL_FIFTEEN_MINUTES,operation);

    }

}
