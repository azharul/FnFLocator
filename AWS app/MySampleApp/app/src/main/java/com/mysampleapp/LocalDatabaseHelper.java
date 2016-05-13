package com.mysampleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Richard on 4/26/2016.
 */
public class LocalDatabaseHelper extends SQLiteOpenHelper{

    //Log TAG
    public final static String TAG = "LocalDatabase";

    //Database Name
    public final static String DB_NAME = "localdatabase.db";

    //Database version
    public final static int DB_version = 1;


    //Table names
    public final static String TABLE_USERS = "users";
    public final static String TABLE_CLOCKS = "clocks";

    //column names
    //user table
    public final static String C_ID = "_id";
    public final static String C_USERNAME = "_username";
    public final static String C_LOCATION = "_location";

    //clock table
    public final static String C_CLOCKNAME = "_clockname";
    public final static String C_USERONE = "_userone";
    public final static String C_USERTWO = "_usertwo";
    public final static String C_USERTHREE = "_userthree";
    public final static String C_USERFOUR = "_userfour";

    //Table Create Statements
    public final static String CREATE_TABLE_USERS = "CREATE TABLE "+TABLE_USERS+"("+C_ID+" INTEGER PRIMARY KEY,"
            +C_USERNAME+" TEXT,"+C_LOCATION+" TEXT"+")";
    public final static String CREATE_TABLE_CLOCKS = "CREATE TABLE "+TABLE_CLOCKS+"("+C_ID+" INTEGER PRIMARY KEY,"
            +C_CLOCKNAME+" TEXT,"+C_USERONE+" TEXT,"+C_USERTWO+" TEXT,"+C_USERTHREE+" TEXT,"+C_USERFOUR+" TEXT"+")";

    public LocalDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the required tables
        Log.d(TAG, "onCreate");
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CLOCKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        db.execSQL("drop if exists " + CREATE_TABLE_USERS);
        db.execSQL("drop if exists " + CREATE_TABLE_CLOCKS);
        onCreate(db);
    }

    //--------------------------Table Methods-----------------------------------------------------//
    //Create Clock
    public boolean createClock(int id,String clockname,String userone,String usertwo,String userthree, String userfour){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_ID,id);
        contentValues.put(C_CLOCKNAME,clockname);
        contentValues.put(C_USERONE,userone);
        contentValues.put(C_USERTWO, usertwo);
        contentValues.put(C_USERTHREE, userthree);
        contentValues.put(C_USERFOUR, userfour);
        db.insertWithOnConflict(TABLE_CLOCKS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "CreateClock");
        return true;
    }

    public boolean createUser(int id,String username,String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_ID,id);
        contentValues.put(C_USERNAME,username);
        contentValues.put(C_LOCATION,location);
        db.insertWithOnConflict(TABLE_USERS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "CreateUser");
        return true;
    }


    public boolean updateUserLocation(String username, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        AndroidLocationDO user=new AndroidLocationDO();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(C_USERNAME,username);
        contentValues.put(C_LOCATION,location);
        db.update(TABLE_USERS, contentValues, C_USERNAME + " = ?", new String[] {username});
        Log.d(TAG, "Updated User= "+username);
        return true;
    }

    public Cursor queryUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery="SELECT * FROM "+TABLE_USERS+" WHERE "+C_USERNAME+" = '"+username+"'";
        Log.d(TAG,"queryClock "+username);
        return db.rawQuery(selectQuery,null);
    }

    public  Cursor queryAllUsers(){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+TABLE_USERS;
        return db.rawQuery(selectQuery,null);
    }

    public Cursor queryClock(String clockname){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery="SELECT * FROM "+TABLE_CLOCKS+" WHERE "+C_CLOCKNAME+" = '"+clockname+"'";
        Log.d(TAG,"queryClock "+clockname);
        return db.rawQuery(selectQuery,null);
    }

    public  Cursor queryAllClocks(){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+TABLE_CLOCKS;
        return db.rawQuery(selectQuery,null);
    }

    public boolean queryUserexists(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery="SELECT * FROM "+TABLE_USERS+" WHERE "+C_USERNAME+" = '"+user+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void deleteAllUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
//         db.delete(TABLE_USERS,null,null);
        db.execSQL("DELETE * FROM"+ TABLE_USERS);
//        db.execSQL("TRUNCATE table" + TABLE_NAME);
        db.close();
    }

    public int queryUserNumber(){
        SQLiteDatabase db=this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}
