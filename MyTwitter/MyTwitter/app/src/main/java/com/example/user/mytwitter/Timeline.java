package com.example.user.mytwitter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Timeline extends AppCompatActivity {

    public static final String TAG = "Timeline";

    SimpleCursorAdapter adapter;
    Cursor cursor;
    ListView list;
    String FROM[] = {StatusData.C_USER,StatusData.C_CREATED_AT,StatusData.C_TEXT};
    int TO[] = {R.id.text_user,R.id.text_createdat,R.id.text_status};

    TimelineReceiver receiver=null;

    //TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
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

        cursor = ((MyTwitter) getApplication()).statusData.query();

        list = (ListView) findViewById(R.id.list);

        adapter = new SimpleCursorAdapter(this,R.layout.row,cursor,FROM,TO,0);
        adapter.setViewBinder(VIEW_BINDER);
        list.setAdapter(adapter);


/*        tv=(TextView)findViewById(R.id.text_timeline);
        Cursor cursor = ((MyTwitter) getApplication()).statusData.query();

        while (cursor.moveToNext()){
            //String id = cursor.getString(cursor.getColumnIndex(StatusData.C_ID));
            String user = cursor.getString(cursor.getColumnIndex(StatusData.C_USER));
            String status_text = cursor.getString(cursor.getColumnIndex(StatusData.C_TEXT));
            tv.append(user+"\n" +status_text+"\n");
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver==null)
            receiver = new TimelineReceiver();

        registerReceiver(receiver,new IntentFilter(MyTwitter.STATUS_CHANGE_ALERT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(this,UpdateService.class);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,PrefsActivity.class));
        } else if (id == R.id.service_start) {
            Log.d(TAG, "Start Service");
            startService(intent);
            return true;
        } else if (id == R.id.service_stop){
            Log.d(TAG, "Stop Service");
            stopService(intent);
            return true;
        } else if (id==R.id.service_refresh){
            Log.d(TAG,"Refresh Service");
            startService(new Intent(this,RefreshService.class));
        } else if (id==R.id.activity_status){
            startActivity(new Intent(this,StatusActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

            if (view.getId()!=R.id.text_createdat)
                return false;

            long timeInMilli = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timeInMilli);
            ((TextView)view).setText(relativeTime);
            return true;

        }
    };

    class TimelineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            cursor = ((MyTwitter) getApplication()).statusData.query();
            adapter.changeCursor(cursor);

        }
    }

}
