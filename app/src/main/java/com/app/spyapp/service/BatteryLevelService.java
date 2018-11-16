package com.app.spyapp.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.app.spyapp.common.Utils;


public class BatteryLevelService extends IntentService {

    public BatteryLevelService() {
        super(BatteryLevelService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "BATTERY STUTES");
        batteryLevel();
    }

    /**
     * Computes the battery level by registering a receiver to the intent triggered
     * by a battery status/level change.
     */
    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
//                int rawlevel = intent.getIntExtra("level", -1);
//                int scale = intent.getIntExtra("scale", -1);
//                int level = -1;
//                if (rawlevel >= 0 && scale > 0) {
//                    level = (rawlevel * 100) / scale;
//                }

                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
                int icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
                boolean present = intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

                Toast.makeText(getApplicationContext(), "Health: " + health + "\n" +
                        "Icon Small:" + icon_small + "\n" +
                        "Level: " + level + "\n" +
                        "Plugged: " + plugged + "\n" +
                        "Present: " + present + "\n" +
                        "Scale: " + scale + "\n" +
                        "Status: " + status + "\n" +
                        "Technology: " + technology + "\n" +
                        "Temperature: " + temperature + "\n" +
                        "Voltage: " + voltage + "\n", Toast.LENGTH_LONG).show();

                Log.i("Debug","Health: " + health + "\n" +
                        "Icon Small:" + icon_small + "\n" +
                        "Level: " + level + "\n" +
                        "Plugged: " + plugged + "\n" +
                        "Present: " + present + "\n" +
                        "Scale: " + scale + "\n" +
                        "Status: " + status + "\n" +
                        "Technology: " + technology + "\n" +
                        "Temperature: " + temperature + "\n" +
                        "Voltage: " + voltage + "\n");
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
}
