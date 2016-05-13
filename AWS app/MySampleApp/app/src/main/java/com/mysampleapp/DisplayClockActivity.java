package com.mysampleapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.mysampleapp.R;

public class DisplayClockActivity extends AppCompatActivity {

    private static final String TAG = "DisplayClock" ;
    Intent intent;
    String clockName, userName;
    LocalDatabaseHelper db;
    Cursor cursor,cursor2;

    String user1,user2,user3,user4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_clock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        userName=intent.getStringExtra("Username");
        clockName = intent.getStringExtra(MainMenuActivity.EXTRA_CLOCKNAME);
        Log.d(TAG,clockName);
        db = new LocalDatabaseHelper(getApplicationContext());
        cursor = db.queryClock(clockName);
        cursor.moveToFirst();

        user1 = cursor.getString(cursor.getColumnIndex(LocalDatabaseHelper.C_USERONE));
        user2 = cursor.getString(cursor.getColumnIndex(LocalDatabaseHelper.C_USERTWO));
        user3 = cursor.getString(cursor.getColumnIndex(LocalDatabaseHelper.C_USERTHREE));
        user4 = cursor.getString(cursor.getColumnIndex(LocalDatabaseHelper.C_USERFOUR));

        setUserLocation(user1);
        setUserLocation(user2);
        setUserLocation(user3);
        setUserLocation(user4);

    }

    public void setUserLocation(String username){
        cursor2 = db.queryUser(username);
        cursor2.moveToFirst();
        String location = cursor2.getString(cursor2.getColumnIndex(LocalDatabaseHelper.C_LOCATION));
        if (location.equals("work")){
            TextView workText = (TextView) findViewById(R.id.textView2);
            workText.append(username+"\n");
        } else if (location.equals("traveling")){
            TextView travelText = (TextView) findViewById(R.id.textView5);
            travelText.append(username+"\n");
        } else if (location.equals("school")){
            TextView schoolText = (TextView) findViewById(R.id.textView4);
            schoolText.append(username+"\n");
        } else {
            TextView homeText = (TextView) findViewById(R.id.textView3);
            homeText.append(username+"\n");
        }
        String[] UpdateUserDetails={username,location};
//        new LocationUpdateTask().execute(UpdateUserDetails);
    }

//    public class LocationUpdateTask extends AsyncTask<String, Void, String> {
//
//        protected Exception exception;
//
//        void processFinish(String output){
//            //Here you will receive the result fired from async class
//            //of onPostExecute(result) method.
//        }
//
//        @Override
//        protected String doInBackground(String... user) {
//            try {
//
//                /////////// when I define it in here then it works, but when I call it from remoteDBhelper class it doesn't work!///////////
////                RemoteDatabaseHelper helper=new RemoteDatabaseHelper();
////                helper.RemoteDBinsertUser(user);
//
//                AndroidLocationDO newUser=new AndroidLocationDO();
//                newUser.setUserName(user[0]);
//                newUser.setStatus(user[2]);
//                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
//                mapper.save(newUser);
//                return "Location updated!";
//            } catch (Exception e) {
//                this.exception = e;
//                return "Failed to update location!";
//            }
//        }
//
//        protected void onPostExecute(String result) {
//            Toast.makeText(DisplayClockActivity.this, result, Toast.LENGTH_SHORT).show();
//        }
//    }
}
