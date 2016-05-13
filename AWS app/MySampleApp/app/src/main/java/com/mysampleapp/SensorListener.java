package com.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.content.BroadcastReceiver;

import android.os.Bundle;


/**
 * Created by User on 5/5/2016.
 */
public class SensorListener extends BroadcastReceiver{

    final public static String TAG="SensorListener";

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
        }
    }

    public SensorListener(){
    }

    public String getWifiName(Context context) {
        String bssid = "none";

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        ssid=wifiInfo.getBSSID();
        if (WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED) {
            bssid = wifiInfo.getSSID();
        }
        return bssid;
    }

}
