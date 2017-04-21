package com.jsp.updaterk312;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.jsp.updaterk312.RKUpdateService.LocalBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OtaUpdateNotifyActivity extends Activity {
    private boolean IsChoseOK;
    private String TAG;
    private LocalBinder mBinder;
    private ServiceConnection mConnection;
    private Context mContext;
    private String mDescription;
    private String mOtaPackageLength;
    private String mOtaPackageName;
    private String mOtaPackageVersion;
    private String mRemoteURI;
    private String mSystemVersion;

    /* renamed from: android.rockchip.update.service.OtaUpdateNotifyActivity.1 */
    class C00071 implements ServiceConnection {
        C00071() {
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            OtaUpdateNotifyActivity.this.mBinder = (LocalBinder) service;
            OtaUpdateNotifyActivity.this.mBinder.LockWorkHandler();
        }

        public void onServiceDisconnected(ComponentName className) {
            OtaUpdateNotifyActivity.this.mBinder = null;
        }
    }

    /* renamed from: android.rockchip.update.service.OtaUpdateNotifyActivity.2 */
    class C00082 implements OnClickListener {
        C00082() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(OtaUpdateNotifyActivity.this.mContext, PackageDownloadActivity.class);
            intent.addFlags(268435456);
            intent.putExtra("uri", OtaUpdateNotifyActivity.this.mRemoteURI);
            intent.putExtra("OtaPackageLength", OtaUpdateNotifyActivity.this.mOtaPackageLength);
            intent.putExtra("OtaPackageName", OtaUpdateNotifyActivity.this.mOtaPackageName);
            intent.putExtra("OtaPackageVersion", OtaUpdateNotifyActivity.this.mOtaPackageVersion);
            intent.putExtra("SystemVersion", OtaUpdateNotifyActivity.this.mSystemVersion);
            OtaUpdateNotifyActivity.this.mContext.startActivity(intent);
            OtaUpdateNotifyActivity.this.IsChoseOK = true;
            OtaUpdateNotifyActivity.this.finish();
        }
    }

    /* renamed from: android.rockchip.update.service.OtaUpdateNotifyActivity.3 */
    class C00093 implements OnClickListener {
        C00093() {
        }

        public void onClick(View v) {
            OtaUpdateNotifyActivity.this.finish();
        }
    }

    public OtaUpdateNotifyActivity() {
        this.TAG = "OtaUpdateNotifyActivity";
        this.mRemoteURI = null;
        this.mOtaPackageVersion = null;
        this.mSystemVersion = null;
        this.mOtaPackageName = null;
        this.mOtaPackageLength = null;
        this.mDescription = null;
        this.IsChoseOK = false;
        this.mConnection = new C00071();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.mContext.bindService(new Intent(this.mContext, RKUpdateService.class), this.mConnection, 1);
        requestWindowFeature(3);
        setContentView(R.layout.notify_dialog);
        setFinishOnTouchOutside(false);
        getWindow().setFeatureDrawableResource(3, 17301543);
        Intent startIntent = getIntent();
        this.mRemoteURI = startIntent.getStringExtra("uri");
        this.mOtaPackageVersion = startIntent.getStringExtra("OtaPackageVersion");
        this.mSystemVersion = startIntent.getStringExtra("SystemVersion");
        this.mOtaPackageName = startIntent.getStringExtra("OtaPackageName");
        this.mOtaPackageLength = startIntent.getStringExtra("OtaPackageLength");
        this.mDescription = startIntent.getStringExtra("description");
        TextView txt = (TextView) findViewById(R.id.notify);
        if (this.mOtaPackageLength != null) {
            long packageSize = Long.valueOf(this.mOtaPackageLength).longValue();
            String packageSize_string = null;
            if (packageSize < 1024) {
                packageSize_string = String.valueOf(packageSize) + "B";
            } else if (packageSize / 1024 > 0 && (packageSize / 1024) / 1024 == 0) {
                packageSize_string = String.valueOf(packageSize / 1024) + "KB";
            } else if ((packageSize / 1024) / 1024 > 0) {
                packageSize_string = String.valueOf((packageSize / 1024) / 1024) + "MB";
            }
            txt.setText(getString(R.string.ota_update) + getString(R.string.ota_package_size) + packageSize_string);
        } else {
            txt.setText(getString(R.string.ota_update));
        }
        TextView descriptView = (TextView) findViewById(R.id.description);
        descriptView.setMinLines(5);
        descriptView.setMaxLines(20);
        if (this.mDescription != null) {
            descriptView.setText(this.mDescription.replace("@#", "\n"));
            Log.d(this.TAG, "description: " + this.mDescription);
        }
        Button btn_cancel = (Button) findViewById(R.id.button_cancel);
        ((Button) findViewById(R.id.button_ok)).setOnClickListener(new C00082());
        btn_cancel.setOnClickListener(new C00093());
    }

    protected void onStop() {
        finish();
        super.onStop();
    }

    protected void onDestroy() {
        if (!(this.mBinder == null || this.IsChoseOK)) {
            this.mBinder.unLockWorkHandler();
        }
        this.mContext.unbindService(this.mConnection);
        super.onDestroy();
    }
}
