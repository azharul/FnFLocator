package com.mysampleapp;

/**
 * Created by User on 5/5/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import com.mysampleapp.LocalDatabaseHelper;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mysampleapp.R;
import com.mysampleapp.AndroidLocationDO;
import com.mysampleapp.demo.nosql.DemoNoSQLResult;
import com.mysampleapp.demo.nosql.DemoNoSQLTableAndroidLocation;
import com.mysampleapp.demo.nosql.DemoNoSQLTableBase;
import com.mysampleapp.demo.nosql.DemoSampleDataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RemoteDatabaseHelper{

    //Log TAG
    public final static String TAG = "RemoteDatabaseHelper";

    //Database Name
    public final static String DB_NAME = "localdatabase.db";

    //Database version
    public final static int DB_version = 1;

    LocalDatabaseHelper db;
    Context context;
    SimpleCursorAdapter adapter;
    Cursor cursor;

    AndroidLocationDO newUser = new AndroidLocationDO();

    Exception exception;

    // getting all users from remote DB
    public String RemoteDBretrieveAll() {
        try {
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
            PaginatedScanList<AndroidLocationDO> allUsers=null;
            allUsers = mapper.scan(AndroidLocationDO.class, scanExpression);
//            db.deleteAllUsers();
            for (AndroidLocationDO item : allUsers) {
                int id = cursor.getCount() + 1;
                db.createUser(id, item.getUserName(), item.getStatus());
                Log.d("username= ", item.getUserName());
                Log.d("password= ", item.getPassword());
                Log.d("status= ", item.getStatus());
                Log.d("id= ", item.getUserId());
                }
            return "Got users!";
            } catch(Exception e){
                this.exception=e;
                return "Failed to retrieve users";
            }
        }

    //inserting a user in remote db
    public String RemoteDBinsertUser(String[] userDetails){
        Log.d(TAG,"inserting user");
        final AndroidLocationDO newUser=new AndroidLocationDO();
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        newUser.setUserName(userDetails[0]);
        newUser.setPassword(userDetails[1]);
        newUser.setStatus(userDetails[2]);
        Cursor cursor=db.queryAllUsers();
        int num=cursor.getCount() + 1;
        newUser.setUserId(Integer.toString(num));
        AmazonClientException lastException = null;

        try {
            mapper.save(newUser);
            return "success!";
        }catch (final AmazonClientException ex){
                lastException=ex;
            return "Failure!";
        }

    }

    //updating a user in remote db
    public String RemoteDBupdateUser(String[] userDetails){
        AndroidLocationDO newUser=new AndroidLocationDO();
        newUser.setUserName(userDetails[0]);
        newUser.setStatus(userDetails[1]);
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        mapper.save(newUser);
        return "Updated in Database!";
    }

    //verifying a user in remote db, for login purpose, when it returns true, that means user exists in db
    public String RemoteDBverifyUser(String[] userDetails){
        AndroidLocationDO verifyUser=new AndroidLocationDO();
        String userName,password;
        DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        verifyUser=mapper.load(AndroidLocationDO.class, userDetails[0]);
        userName=verifyUser.getUserName();
        password=verifyUser.getPassword();
        if (userName.equals(userDetails[0])&& password.equals(userDetails[1])){
            return "True";
        } else{
            return "False";
        }
    }

}
