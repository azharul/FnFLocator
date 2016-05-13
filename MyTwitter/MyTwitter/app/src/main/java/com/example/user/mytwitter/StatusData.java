package com.example.user.mytwitter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by kkx358 on 3/29/2016.
 */
public class StatusData {

    public static final String TAG = "StatusData";

    public static final String DB_NAME = "timeline.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "status";

    public static final String C_ID = "_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_USER = "user_name";
    public static final String C_TEXT = "status_text";

    Context context;
    DbHelper dbHelper;
    SQLiteDatabase db;


    public StatusData(Context context){
        this.context=context;
        dbHelper = new DbHelper();
    }

    public void insert(Twitter.Status status){

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(C_ID,status.id);
        values.put(C_CREATED_AT,status.createdAt.getTime());
        values.put(C_USER,status.user.name);
        values.put(C_TEXT, status.text);

        //db.insert(TABLE,null,values);
        db.insertWithOnConflict(TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
        context.sendBroadcast(new Intent(MyTwitter.STATUS_CHANGE_ALERT));
    }

    public Cursor query(){
        db = dbHelper.getReadableDatabase();
        return db.query(TABLE,null,null,null,null,null,C_CREATED_AT+ " DESC",null);
    }

    class DbHelper extends SQLiteOpenHelper{

        public DbHelper() {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG,"onCreate");
            String sql = String.format("create table %s (%s int primary key, %s long, %s text, %s text)",
                    TABLE, C_ID, C_CREATED_AT, C_USER, C_TEXT);
            Log.d(TAG,sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG,"onUpgrade");
            db.execSQL("drop table if exists " + TABLE);
            onCreate(db);
        }
    }
}
