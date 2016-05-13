package com.mysampleapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.mysampleapp.R;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

import com.mysampleapp.R;
import com.mysampleapp.AndroidLocationDO;
import com.mysampleapp.demo.nosql.DemoNoSQLResult;
import com.mysampleapp.demo.nosql.DemoNoSQLTableAndroidLocation;
import com.mysampleapp.demo.nosql.DemoSampleDataGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ViewActivity extends AppCompatActivity {

    public static final String TAG = "ViewActivity";
    private final AndroidLocationDO result = new AndroidLocationDO();

    private ProgressDialog progressBar;
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    TextView textView;
    private EditText userName,password,status,id;
    Button enterButton;

//    AndroidLocationDO sampleUser=new AndroidLocationDO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button enterButton=(Button) findViewById(R.id.enterDbButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Log.d(TAG, "user pressed search");
                userName=(EditText) findViewById(R.id.userName);
                password=(EditText) findViewById(R.id.password);
                status=(EditText) findViewById(R.id.status);
                String[] userDetails={userName.getText().toString(),password.getText().toString(),status.getText().toString()};
                new UserTask().execute(userDetails);
                Toast.makeText(ViewActivity.this, "Added to database!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public class UserTask extends AsyncTask<String, Void, String> {

        protected Exception exception;
        @Override
        protected String doInBackground(String... user) {
            try {
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
//                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//                PaginatedScanList<AndroidLocationDO> allUsers = mapper.scan(AndroidLocationDO.class, scanExpression);

//                for (AndroidLocationDO item : allUsers) {
//
//                    Log.d("username= ", item.getUserName());
//                    Log.d("password= ",item.getPassword());
//                    Log.d("status= ",item.getStatus());
//                    Log.d("id= ",item.getUserId());
//                }

                AndroidLocationDO newUser=new AndroidLocationDO();
                Random rand=new Random();
                int num=rand.nextInt(500)+1;
                newUser.setUserName(user[0]);
                newUser.setPassword(user[1]);
                newUser.setStatus(user[2]);
                newUser.setUserId(Integer.toString(num));
                mapper.save(newUser);
                return "Added to database!";
            } catch (Exception e) {
                this.exception = e;
                return "Failed to Add!";
            }
        }
    }

}