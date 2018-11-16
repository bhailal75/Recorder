package com.app.spyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

import com.app.spyapp.common.WriteLog;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static final int DONE = 1;
    public static final int NEXT = 2;
    public static final int PERIOD = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Camera camera;
    private int cameraId = 0;
    private Timer timer;
    private boolean safeToTakePicture = false;
    SurfaceView view;
    private static final String TAG = "MainActivity";
    ArrayList<String> applist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        boolean isbroswerInstall=isPackageInstalled("com.android.browser",this);
//        boolean ischromeInstall=isPackageInstalled("com.android.chrome",this);
        //Callling service for gettting broadcast for smsSent
//        Intent screenshotIntent1 = new Intent(MainActivity.this, BookmarkService.class);
//        startService(screenshotIntent1);
//        //get the list of SMS & store in database
//        Intent smsIntent = new Intent(MainActivity.this, BookmarkHistoryChangeService.class);
//        startService(smsIntent);
        Log.i(TAG, "onCreate");
    }

    private class MyContentObserver extends ContentObserver {

        public MyContentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

    }

    //com.android.browser
    //com.android.chrome
    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}