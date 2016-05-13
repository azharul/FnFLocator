package com.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.mysampleapp.R;
import com.mysampleapp.AndroidLocationDO;
import com.mysampleapp.RemoteDatabaseHelper;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.mysampleapp.demo.DemoConfiguration;
import com.mysampleapp.demo.HomeDemoFragment;
import com.mysampleapp.navigation.NavigationDrawer;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    final public static String TAG="LoginActivity";
    public static final String EXTRA_USERNAME = "com.mysampleapp.demo.EXTRA_USERNAME";
    public String loginStatus="False";
    SharedPreferences preferences;
    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;
    Context context;
    LocalDatabaseHelper dbHelper;
    public String userLoggedIn="Test User";

    ////// login status does not get enough time to change, so it remains false, and the login button always shows wrong login//////
    private String setData(String data){
//        loginStatus=data;
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();

        preferences= PreferenceManager.getDefaultSharedPreferences(this);

        Button loginButton;
        loginButton=(Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "user pressed Login!");
                String tempUsername, tempPassword;

                tempUsername = ((EditText) findViewById(R.id.login_user_name)).getText().toString();
                tempPassword = ((EditText) findViewById(R.id.login_password)).getText().toString();
                userLoggedIn=tempUsername;
                //checks the remote server to see if username and password match
                String[] userDetails={tempUsername,tempPassword};
                new verifyUserTask().execute(userDetails);
//                Log.d(TAG,"This is the status: "+loginStatus);
//                if (loginStatus.equals("user verified")) {
////                    preferences.edit().putString("username", tempUsername);
//                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
//                    intent.putExtra(EXTRA_USERNAME, tempUsername);
//                    Log.d(TAG,"Starting next activity in a moment....");
//                    }
//                else{
//                    Log.d(TAG, "user not verified, input correct credentials");
//
//                }


                //tempLoginCheck=RemoteServerDatabaseHelper.login(tempUsername,tempPassword);
//                if (tempLoginCheck) {//True - sets the preference to the username and starts the MainMenuActivity
//                    preferences.edit().putString("username", tempUsername);
//                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
//                    intent.putExtra(EXTRA_USERNAME, tempUsername);
//
//                }
//                else { //False - toast to the user that the username/password combination is incorrect
//                    Toast.makeText(LoginActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
//                }
            }
        });

        //inserting a user in remote database
        Button createUserButton;
        createUserButton=(Button)findViewById(R.id.create_user_button);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "user pressed Create!");
                String tempUsername, tempPassword,tempLocation;
                tempLocation="home";
                tempUsername = ((EditText) findViewById(R.id.login_user_name)).getText().toString();
                tempPassword = ((EditText) findViewById(R.id.login_password)).getText().toString();
                //Creates the user in the remote database, sets the preference to the username and starts MainMenuActivity
                String[] newUserdetails={tempUsername,tempPassword,tempLocation};
                new createUserTask().execute(newUserdetails);
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                intent.putExtra(EXTRA_USERNAME, tempUsername);
                startActivity(intent);
            }
        });

//        Button bypassButton;
//        bypassButton=(Button)findViewById(R.id.bypass_button);
//        bypassButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "user pressed Bypass!!");
//                Intent intent=new Intent(LoginActivity.this,MainMenuActivity.class);
//                intent.putExtra(EXTRA_USERNAME, userLoggedIn);
//                //preferences.edit().putString("username","testUser");
//                startActivity(intent);
//            }
//        });

    }


    public class createUserTask extends AsyncTask<String, Void, String> {

        protected Exception exception;

        void processFinish(String output){
            //Here you will receive the result fired from async class
            //of onPostExecute(result) method.
        }

        @Override
        protected String doInBackground(String... user) {
            try {

                /////////// when I define it in here then it works, but when I call it from remoteDBhelper class it doesn't work!///////////
//                RemoteDatabaseHelper helper=new RemoteDatabaseHelper();
//                helper.RemoteDBinsertUser(user);

//                Cursor cursor=dbHelper.queryAllUsers();
                AndroidLocationDO newUser=new AndroidLocationDO();
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                newUser.setUserName(user[0]);
                newUser.setPassword(user[1]);
                newUser.setStatus(user[2]);
                int id = 1;
//                int id = dbHelper.queryUserNumber() + 1;
                newUser.setUserId(Integer.toString(id));
                mapper.save(newUser);
//                cursor.close();
                return "Added user to remote database!";
            } catch (Exception e) {
                this.exception = e;
                return "Failed to Add!";
            }
        }

        protected void onPostExecute(String result) {
            Toast.makeText(LoginActivity.this,result,Toast.LENGTH_SHORT).show();
        }
    }

    public class verifyUserTask extends AsyncTask<String, Void, String> {

        public AsyncResponse delegate = null;
        protected Exception exception;

        @Override
        protected String doInBackground(String... user) {
            try {
                AndroidLocationDO verifyUser = new AndroidLocationDO();
                String userName = " ";
                String password = " ";
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                verifyUser = mapper.load(AndroidLocationDO.class, user[0]);
                userName = verifyUser.getUserName();
                password = verifyUser.getPassword();
                if (userName.equals(user[0]))
                    if (userName.equals(user[0]) && password.equals(user[1])) {
                        Log.d(TAG, "user verified");
                        return "user verified";
                    }
            } catch (Exception e) {
                this.exception = e;
                Log.d(TAG, "user not verified");
                return "user not verified";
            }
            return " ";
        }


        @Override
        protected void onPostExecute(String result) {
            loginStatus = result;
            Context c;
            Log.d(TAG, "this is the result= " + result);
//            Toast.makeText(LoginActivity.this,"starting next activity...",Toast.LENGTH_SHORT).show();
            if (loginStatus.equals("user verified")) {
//                    preferences.edit().putString("username", tempUsername);
                Log.d(TAG, "Starting next activity in a moment....");
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                intent.putExtra(EXTRA_USERNAME, userLoggedIn);
                startActivity(intent);
            } else {
                Log.d(TAG, "user not verified, input correct credentials");
            }
        }
    }

}

