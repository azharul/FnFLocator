package com.mysampleapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mysampleapp.R;

public class CreateClockActivity extends AppCompatActivity {

    EditText clockname,user1,user2,user3,user4;
    String clockName,userOne,userTwo,userThree,userFour;
    LocalDatabaseHelper db;
    Cursor cursor;
    int id;
    String location = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_clock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button createClockButton;
        createClockButton=(Button)findViewById(R.id.create_clock_button);
        createClockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clockname = (EditText) findViewById(R.id.create_clock_name);
                clockName = clockname.getText().toString();
                user1 = (EditText) findViewById(R.id.create_clock_userOne);
                userOne = user1.getText().toString();
                user2 = (EditText) findViewById(R.id.create_clock_userTwo);
                userTwo = user2.getText().toString();
                user3 = (EditText) findViewById(R.id.create_clock_userThree);
                userThree = user3.getText().toString();
                user4 = (EditText) findViewById(R.id.create_clock_userFour);
                userFour = user4.getText().toString();

                db = new LocalDatabaseHelper(getApplicationContext());
                cursor = db.queryAllClocks();
                id = cursor.getCount()+1;
                db.createClock(id, clockName, userOne, userTwo, userThree, userFour);

                cursor = db.queryAllUsers();
                id = cursor.getCount()+1;
                db.createUser(id, userOne, location);
                cursor = db.queryAllUsers();
                id = cursor.getCount()+1;
                db.createUser(id,userTwo,location);
                cursor = db.queryAllUsers();
                id = cursor.getCount()+1;
                db.createUser(id,userThree,location);
                cursor = db.queryAllUsers();
                id = cursor.getCount()+1;
                db.createUser(id,userFour,location);

                Toast.makeText(CreateClockActivity.this, R.string.clock_created, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
