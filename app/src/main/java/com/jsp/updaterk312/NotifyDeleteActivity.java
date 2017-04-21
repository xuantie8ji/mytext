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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NotifyDeleteActivity extends Activity {
    private static String TAG;
    private LocalBinder mBinder;
    private ServiceConnection mConnection;
    private Context mContext;
    private String mPath;

    /* renamed from: android.rockchip.update.service.NotifyDeleteActivity.1 */
    class C00041 implements ServiceConnection {
        C00041() {
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            NotifyDeleteActivity.this.mBinder = (LocalBinder) service;
            Log.d(NotifyDeleteActivity.TAG, "bind rkupdateservice completed!");
        }

        public void onServiceDisconnected(ComponentName className) {
            NotifyDeleteActivity.this.mBinder = null;
        }
    }

    /* renamed from: android.rockchip.update.service.NotifyDeleteActivity.2 */
    class C00052 implements OnClickListener {
        C00052() {
        }

        public void onClick(View v) {
            if (NotifyDeleteActivity.this.mBinder != null) {
                Log.d(NotifyDeleteActivity.TAG, "click ok!");
                NotifyDeleteActivity.this.mBinder.deletePackage(NotifyDeleteActivity.this.mPath);
                NotifyDeleteActivity.this.finish();
            }
        }
    }

    /* renamed from: android.rockchip.update.service.NotifyDeleteActivity.3 */
    class C00063 implements OnClickListener {
        C00063() {
        }

        public void onClick(View v) {
            if (NotifyDeleteActivity.this.mBinder != null) {
                NotifyDeleteActivity.this.finish();
            }
        }
    }

    public NotifyDeleteActivity() {
        this.mBinder = null;
        this.mConnection = new C00041();
    }

    static {
        TAG = "NotifyDeleteActivity";
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestory.........");
        if (this.mBinder != null) {
            this.mBinder.unLockWorkHandler();
            this.mContext.unbindService(this.mConnection);
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown...........");
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        requestWindowFeature(3);
        setContentView(R.layout.notify_dialog);
        getWindow().addFlags(1003);
        getWindow().setFeatureDrawableResource(3, 17301543);
        setFinishOnTouchOutside(false);
        Intent startIntent = getIntent();
        TextView text = (TextView) findViewById(R.id.notify);
        int flag = startIntent.getIntExtra("flag", 0);
        this.mPath = startIntent.getStringExtra("path");
        if (flag == 1) {
            text.setText(getString(R.string.update_success) + getString(R.string.ask_delete_package));
        } else if (flag == 2) {
            text.setText(getString(R.string.update_failed) + getString(R.string.ask_delete_package));
        }
        this.mContext.bindService(new Intent(this.mContext, RKUpdateService.class), this.mConnection, 1);
        Button btn_ok = (Button) findViewById(R.id.button_ok);
        Button btn_cancel = (Button) findViewById(R.id.button_cancel);
        btn_ok.setFocusable(false);
        btn_ok.setClickable(false);
        btn_cancel.setFocusable(false);
        btn_cancel.setClickable(false);
        btn_ok.setOnClickListener(new C00052());
        btn_cancel.setOnClickListener(new C00063());
    }
}
