package com.jsp.updaterk312;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.jsp.updaterk312.RKUpdateService.LocalBinder;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdateAndRebootActivity extends Activity {
    private static WakeLock mWakeLock;
    private String WAKELOCK_KEY;
    private LocalBinder mBinder;
    private ServiceConnection mConnection;
    private Context mContext;
    private String mImageFilePath;
    private UiHandler mUiHandler;
    private WorkHandler mWorkHandler;

    /* renamed from: android.rockchip.update.service.UpdateAndRebootActivity.1 */
    class C00161 implements ServiceConnection {
        C00161() {
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            UpdateAndRebootActivity.this.mBinder = (LocalBinder) service;
            UpdateAndRebootActivity.this.mWorkHandler.sendEmptyMessageDelayed(1, 3000);
        }

        public void onServiceDisconnected(ComponentName className) {
            UpdateAndRebootActivity.this.mBinder = null;
        }
    }

    /* renamed from: android.rockchip.update.service.UpdateAndRebootActivity.2 */
    class C00172 implements OnClickListener {
        C00172() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            UpdateAndRebootActivity.this.finish();
        }
    }

    private class UiHandler extends Handler {
        private UiHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    UpdateAndRebootActivity.this.dialog();
                default:
            }
        }
    }

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    UpdateAndRebootActivity.LOG("WorkHandler::handleMessage() : To perform 'COMMAND_START_UPDATING'.");
                    if (UpdateAndRebootActivity.this.mBinder == null) {
                        Log.d("UpdateAndRebootActivity", "service have not connected!");
                    } else if (UpdateAndRebootActivity.this.mImageFilePath.endsWith("img")) {
                        UpdateAndRebootActivity.this.mBinder.updateFirmware(UpdateAndRebootActivity.this.mImageFilePath, 1);
                    } else if (UpdateAndRebootActivity.this.mBinder.doesOtaPackageMatchProduct(UpdateAndRebootActivity.this.mImageFilePath)) {
                        UpdateAndRebootActivity.mWakeLock.acquire();
                        UpdateAndRebootActivity.this.mBinder.updateFirmware(UpdateAndRebootActivity.this.mImageFilePath, 2);
                    } else {
                        UpdateAndRebootActivity.mWakeLock.acquire();
                        UpdateAndRebootActivity.this.mUiHandler.sendEmptyMessage(1);
                    }
                default:
            }
        }
    }

    public UpdateAndRebootActivity() {
        this.WAKELOCK_KEY = "UpdateAndReboot";
        this.mConnection = new C00161();
    }

    private static void LOG(String msg) {
        Log.d("UpdateAndRebootActivity", msg);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        requestWindowFeature(3);
        setContentView(R.layout.notify_dialog);
        setFinishOnTouchOutside(false);
        getWindow().setFeatureDrawableResource(3, 17301543);
        setTitle(getString(R.string.updating_title));


        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(805306374, this.WAKELOCK_KEY);



        TextView txt = (TextView) findViewById(R.id.notify);
        Button btn_ok = (Button) findViewById(R.id.button_ok);
        Button btn_cancel = (Button) findViewById(R.id.button_cancel);
        this.mImageFilePath = getIntent().getExtras().getString("android.rockchip.update.extra.IMAGE_PATH");
        Log.d("UpdateAndRebootActivity","---------------0start");
        String msg = getString(R.string.updating_prompt);
        if (this.mImageFilePath.contains(RKUpdateService.SDCARD_ROOT)) {
            msg = msg + getString(R.string.updating_prompt_sdcard);
        }
        txt.setText(msg);
        btn_ok.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        LOG("onCreate() : start 'work thread'.");
        HandlerThread workThread = new HandlerThread("UpdateAndRebootActivity : work thread");
        workThread.start();
        this.mWorkHandler = new WorkHandler(workThread.getLooper());
        this.mUiHandler = new UiHandler();
        this.mContext.bindService(new Intent(this.mContext, RKUpdateService.class), this.mConnection, 1);
    }

    protected void onDestroy() {
        LOG("onDestroy()");
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
        LOG("onPause() : Entered.");
    }

    protected void dialog() {
        Builder builder = new Builder(this.mContext);
        builder.setMessage("not a valid update package !");
        builder.setTitle("error");
        builder.setPositiveButton("OK", new C00172());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
}
