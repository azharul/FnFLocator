package com.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.mysampleapp.R;
import com.mysampleapp.MyClock;
import com.mysampleapp.AndroidLocationDO;
import com.mysampleapp.RemoteDatabaseHelper;


import java.util.Random;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenu";
    public static final String EXTRA_CLOCKNAME = "com.mysampleapp.demo.EXTRA_CLOCKNAME";

    TextView welcomeText, modeText;
    String username, mode;
    Intent intent;
    LocalDatabaseHelper dbHelper;
    SharedPreferences preferences;
    int id;

    Context context;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    ListView listView;
    String FROM[] = {LocalDatabaseHelper.C_ID, LocalDatabaseHelper.C_CLOCKNAME, LocalDatabaseHelper.C_USERONE, LocalDatabaseHelper.C_USERTWO, LocalDatabaseHelper.C_USERTHREE, LocalDatabaseHelper.C_USERFOUR};
    int TO[] = {R.id.clock_id, R.id.clock_name, R.id.clock_user1, R.id.clock_user2, R.id.clock_user3, R.id.clock_user4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new LocalDatabaseHelper(getApplicationContext());

        cursor = dbHelper.queryAllClocks();
        id = cursor.getCount();
        if (id == 0) {
            dbHelper.createClock(1, "Futurama", "Fry", "Leela", "Zoidberg", "Bender");
            cursor = dbHelper.queryAllUsers();
            id = cursor.getCount() + 1;
            dbHelper.createUser(id, "Fry", "home");
            cursor = dbHelper.queryAllUsers();
            id = cursor.getCount() + 1;
            dbHelper.createUser(id, "Leela", "school");
            cursor = dbHelper.queryAllUsers();
            id = cursor.getCount() + 1;
            dbHelper.createUser(id, "Zoidberg", "work");
            cursor = dbHelper.queryAllUsers();
            id = cursor.getCount() + 1;
            dbHelper.createUser(id, "Bender", "traveling");
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        listView = (ListView) findViewById(R.id.clock_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, listView.getItemAtPosition(position) + "");

                Cursor cursor3 = (Cursor) listView.getItemAtPosition(position);
                String clockName = cursor3.getString(cursor3.getColumnIndex(LocalDatabaseHelper.C_CLOCKNAME));
                Intent intent = new Intent(MainMenuActivity.this, DisplayClockActivity.class);
                intent.putExtra("Username",username);
                intent.putExtra(EXTRA_CLOCKNAME, clockName);
                startActivity(intent);
            }
        });

        final Button homeButton;
        homeButton = (Button) findViewById(R.id.home_button);
        final Button workButton;
        workButton = (Button) findViewById(R.id.work_button);
        final Button travelButton;
        travelButton = (Button) findViewById(R.id.traveling_button);
        final Button schoolButton;
        schoolButton = (Button) findViewById(R.id.school_button);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INSERT CODE TO UPDATE REMOTE SERVER WITH LOCATION "home"
                String[] userDetails={username,"home"};
                new LocationUpdateTask().execute(userDetails);
                preferences.edit().putString("location", "home");
                dbHelper.updateUserLocation(username, "home");
                homeButton.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                workButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                travelButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                schoolButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                Toast.makeText(MainMenuActivity.this, R.string.location_updated, Toast.LENGTH_SHORT).show();
            }
        });

        workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INSERT CODE TO UPDATE REMOTE SERVER WITH LOCATION "work"
                String[] userDetails={username,"work"};
                new LocationUpdateTask().execute(userDetails);
                preferences.edit().putString("location", "work");
                dbHelper.updateUserLocation(username, "work");
                workButton.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                homeButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                travelButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                schoolButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                Toast.makeText(MainMenuActivity.this, R.string.location_updated, Toast.LENGTH_SHORT).show();
            }
        });

        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INSERT CODE TO UPDATE REMOTE SERVER WITH LOCATION "traveling"
                String[] userDetails={username,"traveling"};
                new LocationUpdateTask().execute(userDetails);
                preferences.edit().putString("location", "traveling");
                dbHelper.updateUserLocation(username, "traveling");
                travelButton.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                workButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                homeButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                schoolButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                Toast.makeText(MainMenuActivity.this, R.string.location_updated, Toast.LENGTH_SHORT).show();
            }
        });

        schoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INSERT CODE TO UPDATE REMOTE SERVER WITH LOCATION "school"
                String[] userDetails={username,"school"};
                new LocationUpdateTask().execute(userDetails);
                preferences.edit().putString("location", "school");
                dbHelper.updateUserLocation(username, "school");
                schoolButton.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                workButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                travelButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                homeButton.setBackgroundColor(getResources().getColor(R.color.buttonNotSelected));
                Toast.makeText(MainMenuActivity.this, R.string.location_updated, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PreferenceActivity.class));
        } else if (id == R.id.service_refresh) {
            Log.d(TAG, "Refresh Service");
            SensorListener listener=new SensorListener();

            new UserTask().execute();
//            startService(new Intent(this,RefreshService.class));
        } else if (id == R.id.create_clock) {
            startActivity(new Intent(this, CreateClockActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        welcomeText = (TextView) findViewById(R.id.welcome_text);
        intent = getIntent();
        username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME);
        welcomeText.setText("Welcome: " + username);
        preferences.edit().putString("username", username);
        modeText = (TextView) findViewById(R.id.mode_text);
        mode = ((MyClock) getApplication()).getMode();
        modeText.setText("Mode: " + mode);

        cursor = dbHelper.queryAllClocks();
        cursor.moveToFirst();
        Log.d(TAG, cursor.getCount() + "");
        listView = (ListView) findViewById(R.id.clock_list);
        adapter = new SimpleCursorAdapter(MainMenuActivity.this, R.layout.row, cursor, FROM, TO, 0);
        listView.setAdapter(adapter);
    }

    // retrieve all users from remote db
    public class UserTask extends AsyncTask<String, Void, String> {

        protected Exception exception;

        @Override
        protected String doInBackground(String... user) {
            try {
//                startService(new Intent(MainMenuActivity.this,RefreshService.class));
//                RemoteDatabaseHelper remoteHelper = new RemoteDatabaseHelper();
//                remoteHelper.RemoteDBretrieveAll();

                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                PaginatedScanList<AndroidLocationDO> allUsers = null;
                allUsers = mapper.scan(AndroidLocationDO.class, scanExpression);

                for (AndroidLocationDO item : allUsers) {
                    cursor = dbHelper.queryAllUsers();
                    String username=item.getUserName();
                    String status=item.getStatus();
                    if (dbHelper.queryUserexists(username)) {
                        dbHelper.updateUserLocation(username, status);
                    } else {
                    int id = cursor.getCount() + 1;
                    dbHelper.createUser(id, username, status);
//                    Log.d("username= ", username);
//                    Log.d("password= ", item.getPassword());
//                    Log.d("status= ", status);
//                    Log.d("id= ", item.getUserId());
                    Log.d("total user= ", Integer.toString(dbHelper.queryUserNumber()));
                    }
                }
                Log.d(TAG,"Retrieved users from remote DB");
                return "Retrieved users from remote DB";

            } catch (Exception e) {
                this.exception = e;
                Log.d(TAG,"Failed to retrieve!");
                return "Failed to retrieve!";
            }
        }
        protected void onPostExecute(String result) {
            Toast.makeText(MainMenuActivity.this,result,Toast.LENGTH_SHORT).show();
        }
    }


    // update location of user in remote db
    public class LocationUpdateTask extends AsyncTask<String, Void, String> {

        protected Exception exception;

        @Override
        protected String doInBackground(String... user) {
            try {

                /////////// when I define it in here then it works, but when I call it from remoteDBhelper class it doesn't work!///////////
//                RemoteDatabaseHelper helper=new RemoteDatabaseHelper();
//                helper.RemoteDBinsertUser(user);

                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                AndroidLocationDO newUser=mapper.load(AndroidLocationDO.class,user[0]);

                newUser.setStatus(user[1]);
                mapper.save(newUser);
                return "Location updated!";
            } catch (Exception e) {
                this.exception = e;
                return "Failed to update location!";
            }
        }

        protected void onPostExecute(String result) {
            Toast.makeText(MainMenuActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }


}