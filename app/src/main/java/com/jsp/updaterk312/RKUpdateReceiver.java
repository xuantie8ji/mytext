package com.jsp.updaterk312;

//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//
//public class RKUpdateReceiver
//        extends BroadcastReceiver
//{
//    private static boolean isBootCompleted = false;
//
//    public void onReceive(Context paramContext, Intent paramIntent)
//    {
//        Object localObject = paramIntent.getAction();
//        Log.d("RKUpdateReceiver", "action = " + (String)localObject);
//        if (((String)localObject).equals("android.intent.action.BOOT_COMPLETED"))
//        {
//            Log.d("RKUpdateReceiver", "RKUpdateReceiver recv ACTION_BOOT_COMPLETED.");
//            paramIntent = new Intent(paramContext, RKUpdateService.class);
//            paramIntent.putExtra("command", 1);
//            paramIntent.putExtra("delay", 20000);
//            paramContext.startService(paramIntent);
//            paramIntent = new Intent(paramContext, RKUpdateService.class);
//            paramIntent.putExtra("command", 2);
//            paramIntent.putExtra("delay", 25000);
//            paramContext.startService(paramIntent);
//            isBootCompleted = true;
//        }
//        do
//        {
//            do
//            {
//                boolean bool1;
//                boolean bool2;
//                boolean bool3;
//                do
//                {
////                    return;
//                    if ((((String)localObject).equals("android.intent.action.MEDIA_MOUNTED")) && (isBootCompleted))
//                    {
//                      String  paramIntentString = paramIntent.getData().getPath();
//                        localObject = new Intent(paramContext, RKUpdateService.class);
//                        ((Intent)localObject).putExtra("command", 1);
//                        ((Intent)localObject).putExtra("delay", 5000);
//                        paramContext.startService((Intent)localObject);
//                        Log.d("RKUpdateReceiver", "media is mounted to '" + new String[] { paramIntentString }[0] + "'. To check local update.");
//                        return;
//                    }
//                    if ((!((String)localObject).equals("android.hardware.usb.action.USB_STATE")) || (!isBootCompleted)) {
//                        break;
//                    }
//                    Bundle extras = paramIntent.getExtras();
//                    bool1 = extras.getBoolean("connected");
//                    bool2 = extras.getBoolean("configured");
//                    bool3 = extras.getBoolean("mtp");
//                    extras.getBoolean("ptp");
//                } while ((bool1) || (!bool3) || (bool2));
//                paramIntent = new Intent(paramContext, RKUpdateService.class);
//                paramIntent.putExtra("command", 1);
//                paramIntent.putExtra("delay", 5000);
//                paramContext.startService(paramIntent);
//                return;
//            } while ((!((String)localObject).equals("android.net.conn.CONNECTIVITY_CHANGE")) || (!isBootCompleted));
////            NetworkInfo   paramIntent2 = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
//        } while ((paramIntent == null) || ((paramIntent.getType() != 1+"") && (paramIntent.getType() != 9+"")));
////        paramIntent = new Intent(paramContext, RKUpdateService.class);
////        paramIntent.putExtra("command", 2);
////        paramIntent.putExtra("delay", 5000);
////        paramContext.startService(paramIntent);
//    }
//}





















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
        else if (action.equals("UserRKUpdateService_Brocast")) {
            Log.d("RKUpdateReceiver", "RKUpdateReceiver recv UserRKUpdateService_Brocast.");

boolean flag=intent.getBooleanExtra("verify",true);

            serviceIntent = new Intent(context, RKUpdateService.class);
            serviceIntent.putExtra("command", 1);
            serviceIntent.putExtra("delay", 2000);
            context.startService(serviceIntent);
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
