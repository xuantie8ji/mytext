package com.jsp.updaterk312;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class RKUpdateReceiver extends BroadcastReceiver {
    private static boolean isBootCompleted;

    static {
        isBootCompleted = false;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("RKUpdateReceiver", "action = " + action);
        Intent serviceIntent;
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("RKUpdateReceiver", "RKUpdateReceiver recv ACTION_BOOT_COMPLETED.");
            serviceIntent = new Intent(context, RKUpdateService.class);
            serviceIntent.putExtra("command", 1);
            serviceIntent.putExtra("delay", 20000);
            context.startService(serviceIntent);
            serviceIntent = new Intent(context, RKUpdateService.class);
            serviceIntent.putExtra("command", 2);
            serviceIntent.putExtra("delay", 25000);
            context.startService(serviceIntent);
            isBootCompleted = true;
        } else if (action.equals("android.intent.action.MEDIA_MOUNTED") && isBootCompleted) {
            String[] path = new String[]{intent.getData().getPath()};
            serviceIntent = new Intent(context, RKUpdateService.class);
            serviceIntent.putExtra("command", 1);
            serviceIntent.putExtra("delay", 5000);
            context.startService(serviceIntent);
            Log.d("RKUpdateReceiver", "media is mounted to '" + path[0] + "'. To check local update.");
        }
//        else if (action.equals("android.hardware.usb.action.USB_STATE") && isBootCompleted) {
//            Bundle extras = intent.getExtras();
//            boolean connected = extras.getBoolean("connected");
//            boolean configured = extras.getBoolean("configured");
//            boolean mtpEnabled = extras.getBoolean("mtp");
//            boolean ptpEnabled = extras.getBoolean("ptp");
//            if (!connected && mtpEnabled && !configured) {
//                serviceIntent = new Intent(context, RKUpdateService.class);
//                serviceIntent.putExtra("command", 1);
//                serviceIntent.putExtra("delay", 5000);
//                context.startService(serviceIntent);
//            }
//        }
    }
}
