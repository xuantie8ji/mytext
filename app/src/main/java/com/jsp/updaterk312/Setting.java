package com.jsp.updaterk312;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import android.widget.TextView;

public class Setting extends Activity {
    private SharedPreferences mAutoCheckSet;
    private ImageButton mBtn_CheckNow;
    private Context mContext;
    private TextView mTxtProduct;
    private TextView mTxtVersion;

    /* renamed from: android.rockchip.update.service.Setting.1 */
    class C00151 implements OnClickListener {
        C00151() {
        }

        public void onClick(View v) {
            Intent serviceIntent = new Intent("android.rockchip.update.service");
            serviceIntent.setPackage("com.jsp.updaterk312");
            serviceIntent.putExtra("command", 3);
            Setting.this.mContext.startService(serviceIntent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(1);
        setContentView(R.layout.setting);
        this.mContext = this;
        this.mBtn_CheckNow = (ImageButton) findViewById(R.id.btn_check_now);
        this.mTxtProduct = (TextView) findViewById(R.id.txt_product);
        this.mTxtVersion = (TextView) findViewById(R.id.txt_version);
        this.mTxtProduct.setText(RKUpdateService.getOtaProductName());
        this.mTxtVersion.setText(RKUpdateService.getSystemVersion());
        this.mAutoCheckSet = getSharedPreferences("auto_check", 0);
        this.mBtn_CheckNow.setOnClickListener(new C00151());

    }
}
