package com.app.spyapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.app.spyapp.database.DatabaseHelper;


public class SpyApp extends Application {
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private String outgoingnumber;

    public String getOutgoingnumber() {
        return outgoingnumber;
    }

    public void setOutgoingnumber(String outgoingnumber) {
        this.outgoingnumber = outgoingnumber;
    }




    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.openDataBase();
     //   MultiDex.install(this);
    }


    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
