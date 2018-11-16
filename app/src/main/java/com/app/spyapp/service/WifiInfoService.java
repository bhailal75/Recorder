package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.app.spyapp.model.WifiModel;


public class WifiInfoService extends IntentService {


    public WifiInfoService() {
        super(WifiInfoService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getWifiInfo(WifiInfoService.this);
    }

    public WifiModel getWifiInfo(Context context) {
        WifiModel wifiModel = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {

                String[] str = connectionInfo.toString().split(",");
                if (str != null && str.length > 0) {
                    wifiModel = new WifiModel();
                    wifiModel.setSsID(str[0]);
                    wifiModel.setBSSID(str[1]);
                    wifiModel.setMAC(str[2]);
                    wifiModel.setState(str[3]);
                    wifiModel.setRSSI(str[4]);
                    wifiModel.setLinkSpeed(str[5]);
                    wifiModel.setFrequency(str[6]);
                    wifiModel.setNetID(str[7]);
                    wifiModel.setScore(str[8]);


                    Log.i("Debug", "getWifiInfo: Wifi ="+str[0]+ "\n" +"Speed "+str[5]+ "\n" );
                }


//SSID: Vicky's, BSSID: 0c:d2:b5:38:07:18, MAC: 02:00:00:00:00:00, Supplicant state: COMPLETED, RSSI: -80, Link speed: 26Mbps, Frequency: 2412MHz, Net ID: 22, Metered hint: false, score: 56
            }
        }
        return wifiModel;
    }
}
