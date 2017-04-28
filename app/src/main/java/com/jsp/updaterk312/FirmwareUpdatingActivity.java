package com.jsp.updaterk312;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.Formatter;
import java.util.Locale;

public class FirmwareUpdatingActivity extends Activity {
    private static StringBuilder sFormatBuilder;
    private static Formatter sFormatter;
    private Context mContext;
    private String mCurrentVersion;
    private String mImageFilePath;
    private String mImageVersion;
    private BroadcastReceiver mReceiver;

    private boolean flag=true; //update dialog verify

    /* renamed from: android.rockchip.update.service.FirmwareUpdatingActivity.1 */
    class C00001 extends BroadcastReceiver {
        C00001() {
        }

        public void onReceive(Context context, Intent intent) {
            FirmwareUpdatingActivity.LOG("mReceiver.onReceive() : 'action' =" + intent.getAction());
            if (intent.getAction() == "android.intent.action.MEDIA_UNMOUNTED") {
                String path = intent.getData().getPath();
                FirmwareUpdatingActivity.LOG("mReceiver.onReceive() : original mount point : " + path + "; image file path : " + FirmwareUpdatingActivity.this.mImageFilePath);
                if (FirmwareUpdatingActivity.this.mImageFilePath != null && FirmwareUpdatingActivity.this.mImageFilePath.contains(path)) {
                    FirmwareUpdatingActivity.LOG("mReceiver.onReceive() : Media that img file live in is unmounted, to finish this activity.");
                    FirmwareUpdatingActivity.this.finish();
                }
            }
        }
    }

    /* renamed from: android.rockchip.update.service.FirmwareUpdatingActivity.2 */
    class C00012 implements OnClickListener {
        C00012() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(FirmwareUpdatingActivity.this.mContext, UpdateAndRebootActivity.class);
            //Intent.FLAG_RECEIVER_FOREGROUND=268435456
            intent.addFlags(268435456);
            intent.putExtra("android.rockchip.update.extra.IMAGE_PATH", FirmwareUpdatingActivity.this.mImageFilePath);
            FirmwareUpdatingActivity.this.startActivity(intent);
            FirmwareUpdatingActivity.this.finish();
        }
    }

    /* renamed from: android.rockchip.update.service.FirmwareUpdatingActivity.3 */
    class C00023 implements OnClickListener {
        C00023() {
        }

        public void onClick(View v) {
            FirmwareUpdatingActivity.this.finish();
        }
    }

    public FirmwareUpdatingActivity() {
        this.mReceiver = new C00001();
    }

    private static void LOG(String msg) {
        Log.d("FirmwareUpdatingActivity",msg);
    }

    static {
        sFormatBuilder = new StringBuilder();
        sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
    }

    protected void onCreate(Bundle savedInstanceState) {
        LOG("onCreate() : Entered.");
        super.onCreate(savedInstanceState);
        this.mContext = this;
        requestWindowFeature(3);
        setContentView(R.layout.notify_dialog);
        setFinishOnTouchOutside(false);
        getWindow().setFeatureDrawableResource(3, 17301543);
        setTitle(getString(R.string.updating_title));
        Bundle extr = getIntent().getExtras();
        this.mImageFilePath = extr.getString("android.rockchip.update.extra.IMAGE_PATH");
        this.mImageVersion = extr.getString("android.rockchip.update.extra.IMAGE_VERSION");
        this.mCurrentVersion = extr.getString("android.rockchip.update.extra.CURRENT_VERSION");


        this.flag = extr.getBoolean("verify");

        if (!flag){
            Intent intent = new Intent(FirmwareUpdatingActivity.this.mContext, UpdateAndRebootActivity.class);
            //Intent.FLAG_RECEIVER_FOREGROUND=268435456
            intent.addFlags(268435456);
            intent.putExtra("android.rockchip.update.extra.IMAGE_PATH", FirmwareUpdatingActivity.this.mImageFilePath);
            FirmwareUpdatingActivity.this.startActivity(intent);
            FirmwareUpdatingActivity.this.finish();
        }else{
            String messageFormat = getString(R.string.updating_message_formate);
            sFormatBuilder.setLength(0);
            sFormatter.format(messageFormat, new Object[]{this.mImageFilePath});
            ((TextView) findViewById(R.id.notify)).setText(sFormatBuilder.toString());
            Button btn_ok = (Button) findViewById(R.id.button_ok);
            Button btn_cancel = (Button) findViewById(R.id.button_cancel);
            btn_ok.setText(getString(R.string.updating_button_install));
            btn_cancel.setText(getString(R.string.updating_button_cancel));
            btn_ok.setOnClickListener(new C00012());
            btn_cancel.setOnClickListener(new C00023());

        }

//        LOG("FirmwareUpdatingActivity"+this.mImageFilePath+"======"+this.mImageVersion+"======"+this.mCurrentVersion);
//        String messageFormat = getString(R.string.updating_message_formate);
//        sFormatBuilder.setLength(0);
//        sFormatter.format(messageFormat, new Object[]{this.mImageFilePath});
//        ((TextView) findViewById(R.id.notify)).setText(sFormatBuilder.toString());
//        Button btn_ok = (Button) findViewById(R.id.button_ok);
//        Button btn_cancel = (Button) findViewById(R.id.button_cancel);
//        btn_ok.setText(getString(R.string.updating_button_install));
//        btn_cancel.setText(getString(R.string.updating_button_cancel));
//        btn_ok.setOnClickListener(new C00012());
//        btn_cancel.setOnClickListener(new C00023());
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addDataScheme("file");
        registerReceiver(this.mReceiver, filter);
    }

    protected void onPause() {
        super.onPause();
        LOG("onPause() : Entered.");
    }

    protected void onDestroy() {
        super.onDestroy();
        LOG("onDestroy() : Entered.");
        unregisterReceiver(this.mReceiver);
    }
}
