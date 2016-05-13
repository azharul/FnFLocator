package com.example.user.mytwitter;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    public static final String TAG="BootReceiver";

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        //context.startService(new Intent(context,UpdateService.class));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Intent pIntent = new Intent(context,RefreshService.class);
        PendingIntent operation = PendingIntent.getService(context,0,pIntent,0);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),10000,operation);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,10000,AlarmManager.INTERVAL_FIFTEEN_MINUTES,operation);

    }
}
