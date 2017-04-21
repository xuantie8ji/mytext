package com.jsp.updaterk312;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
//import android.rockchip.update.service.RKUpdateService.LocalBinder;
//import android.rockchip.update.util.FTPRequestInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.HttpClient;

public class PackageDownloadActivity extends Activity {
//    private static WakeLock mWakeLock;
//    private String TAG;
//    private String WAKELOCK_KEY;
//    private ResolveInfo homeInfo;
//    private LocalBinder mBinder;
//    private Button mBtnCancel;
//    private Button mBtnControl;
//    private TextView mCompletedTV;
//    private ServiceConnection mConnection;
//    private Context mContext;
//    private int mDownloadProtocol;
//    private TextView mDownloadRateTV;
//    private FTPRequestInfo mFTPRequest;
//    private String mFileName;
//    private FTPFileDownloadTask mFtpTask;
//    private HttpClient mHttpClient;
//    private HTTPdownloadHandler mHttpDownloadHandler;
//    private HTTPFileDownloadTask mHttpTask;
//    private URI mHttpUri;
//    private volatile boolean mIsCancelDownload;
//    private Notification mNotify;
//    private NotificationManager mNotifyManager;
//    private ProgressBar mProgressBar;
//    private TextView mRemainTimeTV;
//    private int mState;
//    private int notification_id;
//
//    /* renamed from: android.rockchip.update.service.PackageDownloadActivity.1 */
//    class C00101 implements ServiceConnection {
//        C00101() {
//        }
//
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            PackageDownloadActivity.this.mBinder = (LocalBinder) service;
//            PackageDownloadActivity.this.mBinder.LockWorkHandler();
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            PackageDownloadActivity.this.mBinder = null;
//        }
//    }
//
//    /* renamed from: android.rockchip.update.service.PackageDownloadActivity.2 */
//    class C00112 implements OnClickListener {
//        C00112() {
//        }
//
//        public void onClick(View v) {
//            if (PackageDownloadActivity.this.mState == 0 || PackageDownloadActivity.this.mState == 4) {
//                if (PackageDownloadActivity.this.mDownloadProtocol == 0) {
//                    PackageDownloadActivity.this.mHttpTask = new HTTPFileDownloadTask(PackageDownloadActivity.this.mHttpClient, PackageDownloadActivity.this.mHttpUri, RKUpdateService.FLASH_ROOT, PackageDownloadActivity.this.mFileName, 1);
//                    PackageDownloadActivity.this.mHttpTask.setProgressHandler(PackageDownloadActivity.this.mHttpDownloadHandler);
//                    PackageDownloadActivity.this.mHttpTask.start();
//                } else {
//                    PackageDownloadActivity.this.mFtpTask = new FTPFileDownloadTask(PackageDownloadActivity.this.mFTPRequest, RKUpdateService.FLASH_ROOT, PackageDownloadActivity.this.mFileName);
//                    PackageDownloadActivity.this.mFtpTask.setProgressHandler(PackageDownloadActivity.this.mHttpDownloadHandler);
//                    PackageDownloadActivity.this.mFtpTask.start();
//                }
//                PackageDownloadActivity.this.mBtnControl.setText(PackageDownloadActivity.this.getString(R.string.starting));
//                PackageDownloadActivity.this.mBtnControl.setClickable(false);
//                PackageDownloadActivity.this.mBtnControl.setFocusable(false);
//                PackageDownloadActivity.this.mBtnCancel.setClickable(false);
//                PackageDownloadActivity.this.mBtnCancel.setFocusable(false);
//            } else if (PackageDownloadActivity.this.mState == 2) {
//                if (PackageDownloadActivity.this.mDownloadProtocol == 0) {
//                    PackageDownloadActivity.this.mHttpTask.stopDownload();
//                } else {
//                    PackageDownloadActivity.this.mFtpTask.stopDownload();
//                }
//                PackageDownloadActivity.this.mBtnControl.setText(PackageDownloadActivity.this.getString(R.string.stoping));
//                PackageDownloadActivity.this.mBtnControl.setClickable(false);
//                PackageDownloadActivity.this.mBtnControl.setFocusable(false);
//                PackageDownloadActivity.this.mBtnCancel.setClickable(false);
//                PackageDownloadActivity.this.mBtnCancel.setFocusable(false);
//            }
//        }
//    }
//
//    /* renamed from: android.rockchip.update.service.PackageDownloadActivity.3 */
//    class C00123 implements OnClickListener {
//        C00123() {
//        }
//
//        public void onClick(View v) {
//            if (PackageDownloadActivity.this.mState == 0 || PackageDownloadActivity.this.mState == 4) {
//                PackageDownloadActivity.this.finish();
//            } else if (PackageDownloadActivity.this.mDownloadProtocol == 0) {
//                if (PackageDownloadActivity.this.mHttpTask != null) {
//                    PackageDownloadActivity.this.mHttpTask.stopDownload();
//                    PackageDownloadActivity.this.mIsCancelDownload = true;
//                    return;
//                }
//                PackageDownloadActivity.this.finish();
//            } else if (PackageDownloadActivity.this.mFtpTask != null) {
//                PackageDownloadActivity.this.mFtpTask.stopDownload();
//                PackageDownloadActivity.this.mIsCancelDownload = true;
//            } else {
//                PackageDownloadActivity.this.finish();
//            }
//        }
//    }
//
//    private class HTTPdownloadHandler extends Handler {
//        private HTTPdownloadHandler() {
//        }
//
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    Bundle b = msg.getData();
//                    long receivedCount = b.getLong("ReceivedCount", 0);
//                    long contentLength = b.getLong("ContentLength", 0);
//                    long receivedPerSecond = b.getLong("ReceivedPerSecond", 0);
//                    int percent = (int) ((100 * receivedCount) / contentLength);
//                    Log.d(PackageDownloadActivity.this.TAG, "percent = " + percent);
//                    PackageDownloadActivity.this.setDownloadInfoViews(contentLength, receivedCount, receivedPerSecond);
//                    PackageDownloadActivity.this.mProgressBar.setProgress(percent);
//                    PackageDownloadActivity.this.setNotificationProgress(percent);
//                    PackageDownloadActivity.this.showNotification();
//                case 2:
//                    int errCode = msg.getData().getInt("err", 0);
//                    if (errCode == 1) {
//                        Toast.makeText(PackageDownloadActivity.this.getApplicationContext(), PackageDownloadActivity.this.getString(R.string.error_display), 1).show();
//                    } else if (errCode != 2) {
//                        if (errCode == 4) {
//                            Toast.makeText(PackageDownloadActivity.this.getApplicationContext(), PackageDownloadActivity.this.getString(R.string.error_display), 1).show();
//                        } else if (errCode != 3 && errCode == 5) {
//                            Toast.makeText(PackageDownloadActivity.this.getApplicationContext(), PackageDownloadActivity.this.getString(R.string.error_display), 1).show();
//                        }
//                    }
//                    PackageDownloadActivity.this.mState = 4;
//                    PackageDownloadActivity.this.mRemainTimeTV.setText("");
//                    PackageDownloadActivity.this.mDownloadRateTV.setText("");
//                    PackageDownloadActivity.this.mBtnControl.setText(PackageDownloadActivity.this.getString(R.string.retry));
//                    PackageDownloadActivity.this.mBtnControl.setClickable(true);
//                    PackageDownloadActivity.this.mBtnControl.setFocusable(true);
//                    PackageDownloadActivity.this.mBtnCancel.setClickable(true);
//                    PackageDownloadActivity.this.mBtnCancel.setFocusable(true);
//                    PackageDownloadActivity.this.setNotificationPause();
//                    PackageDownloadActivity.this.showNotification();
//                    if (PackageDownloadActivity.mWakeLock.isHeld()) {
//                        PackageDownloadActivity.mWakeLock.release();
//                    }
//                    if (PackageDownloadActivity.this.mIsCancelDownload) {
//                        PackageDownloadActivity.this.finish();
//                    }
//                case 3:
//                    PackageDownloadActivity.this.mState = 2;
//                    PackageDownloadActivity.this.mBtnControl.setText(PackageDownloadActivity.this.getString(R.string.pause));
//                    PackageDownloadActivity.this.mBtnControl.setClickable(true);
//                    PackageDownloadActivity.this.mBtnControl.setFocusable(true);
//                    PackageDownloadActivity.this.mBtnCancel.setClickable(true);
//                    PackageDownloadActivity.this.mBtnCancel.setFocusable(true);
//                    PackageDownloadActivity.this.setNotificationStrat();
//                    PackageDownloadActivity.this.showNotification();
//                    PackageDownloadActivity.mWakeLock.acquire();
//                case 4:
//                    PackageDownloadActivity.this.mState = 0;
//                    PackageDownloadActivity.this.mBtnControl.setText(PackageDownloadActivity.this.getString(R.string.start));
//                    PackageDownloadActivity.this.mBtnControl.setClickable(true);
//                    PackageDownloadActivity.this.mBtnControl.setFocusable(true);
//                    PackageDownloadActivity.this.mBtnCancel.setClickable(true);
//                    PackageDownloadActivity.this.mBtnCancel.setFocusable(true);
//                    Intent intent = new Intent();
//                    intent.setClass(PackageDownloadActivity.this.mContext, UpdateAndRebootActivity.class);
//                    intent.addFlags(268435456);
//                    intent.putExtra("android.rockchip.update.extra.IMAGE_PATH", RKUpdateService.FLASH_ROOT + "/" + PackageDownloadActivity.this.mFileName);
//                    PackageDownloadActivity.this.startActivity(intent);
//                    PackageDownloadActivity.this.finish();
//                default:
//            }
//        }
//    }
//
//    public PackageDownloadActivity() {
//        this.TAG = "PackageDownloadActivity";
//        this.WAKELOCK_KEY = "myDownload";
//        this.mState = 0;
//        this.notification_id = 20110921;
//        this.mDownloadProtocol = 0;
//        this.mIsCancelDownload = false;
//        this.mConnection = new C00101();
//    }
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.mContext = this;
//        this.mHttpUri = null;
//        Intent intent = getIntent();
//        String uriStr = intent.getStringExtra("uri");
//        if (uriStr != null) {
//            setContentView(R.layout.package_download);
//            setFinishOnTouchOutside(false);
//            if (uriStr.startsWith("ftp://")) {
//                this.mDownloadProtocol = 1;
//                this.mFTPRequest = parseFtpUri(uriStr);
//            } else {
//                this.mDownloadProtocol = 0;
//                try {
//                    this.mHttpUri = new URI(uriStr);
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//            }
//            this.mContext.bindService(new Intent(this.mContext, RKUpdateService.class), this.mConnection, 1);
//            this.mFileName = intent.getStringExtra("OtaPackageName");
//            this.homeInfo = getPackageManager().resolveActivity(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 0);
//            this.mNotifyManager = (NotificationManager) getSystemService("notification");
//            this.mNotify = new Notification(R.drawable.ota_update, getString(R.string.app_name), System.currentTimeMillis());
//            this.mNotify.contentView = new RemoteViews(getPackageName(), R.layout.download_notify);
//            this.mNotify.contentView.setProgressBar(R.id.pb_download, 100, 0, false);
//            this.mNotify.contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, PackageDownloadActivity.class), 0);
//            mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, this.WAKELOCK_KEY);
//            this.mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
//            this.mBtnControl = (Button) findViewById(R.id.btn_control);
//            this.mBtnCancel = (Button) findViewById(R.id.button_cancel);
//            this.mRemainTimeTV = (TextView) findViewById(R.id.download_info_remaining);
//            this.mDownloadRateTV = (TextView) findViewById(R.id.download_info_rate);
//            this.mCompletedTV = (TextView) findViewById(R.id.progress_completed);
//            this.mBtnControl.setOnClickListener(new C00112());
//            this.mBtnCancel.setOnClickListener(new C00123());
//            this.mProgressBar.setIndeterminate(false);
//            this.mProgressBar.setProgress(0);
//            this.mHttpDownloadHandler = new HTTPdownloadHandler();
//            this.mHttpClient = CustomerHttpClient.getHttpClient();
//            if (this.mDownloadProtocol == 0) {
//                this.mHttpTask = new HTTPFileDownloadTask(this.mHttpClient, this.mHttpUri, RKUpdateService.FLASH_ROOT, this.mFileName, 1);
//                this.mHttpTask.setProgressHandler(this.mHttpDownloadHandler);
//                this.mHttpTask.start();
//            } else {
//                this.mFtpTask = new FTPFileDownloadTask(this.mFTPRequest, RKUpdateService.FLASH_ROOT, this.mFileName);
//                this.mFtpTask.setProgressHandler(this.mHttpDownloadHandler);
//                this.mFtpTask.start();
//            }
//            this.mBtnControl.setText(getString(R.string.starting));
//            this.mBtnControl.setClickable(false);
//            this.mBtnControl.setFocusable(false);
//        }
//    }
//
//    private void showNotification() {
//        if (this.mNotifyManager != null) {
//            this.mNotifyManager.notify(this.notification_id, this.mNotify);
//            Log.d(this.TAG, "show notification " + this.notification_id);
//        }
//    }
//
//    private void clearNotification() {
//        if (this.mNotifyManager != null) {
//            this.mNotifyManager.cancel(this.notification_id);
//            Log.d(this.TAG, "clearNotification " + this.notification_id);
//        }
//    }
//
//    private void setNotificationProgress(int percent) {
//        if (this.mNotify != null) {
//            this.mNotify.contentView.setProgressBar(R.id.pb_download, 100, percent, false);
//            this.mNotify.contentView.setTextViewText(R.id.pb_percent, String.valueOf(percent) + "%");
//        }
//    }
//
//    private void setNotificationPause() {
//        if (this.mNotify != null) {
//            this.mNotify.contentView.setTextViewText(R.id.pb_title, this.mContext.getString(R.string.pb_title_pause));
//            this.mNotify.contentView.setViewVisibility(R.id.image_pause, 0);
//        }
//    }
//
//    private void setNotificationStrat() {
//        if (this.mNotify != null) {
//            this.mNotify.contentView.setTextViewText(R.id.pb_title, this.mContext.getString(R.string.pb_title_downloading));
//            this.mNotify.contentView.setViewVisibility(R.id.image_pause, 8);
//        }
//    }
//
//    private void setDownloadInfoViews(long contentLength, long receivedCount, long receivedPerSecond) {
//        this.mCompletedTV.setText(String.valueOf((int) ((100 * receivedCount) / contentLength)) + "%");
//        String rate = "";
//        if (receivedPerSecond < 1024) {
//            rate = String.valueOf(receivedPerSecond) + "B/S";
//        } else if (receivedPerSecond / 1024 > 0 && (receivedPerSecond / 1024) / 1024 == 0) {
//            rate = String.valueOf(receivedPerSecond / 1024) + "KB/S";
//        } else if ((receivedPerSecond / 1024) / 1024 > 0) {
//            rate = String.valueOf((receivedPerSecond / 1024) / 1024) + "MB/S";
//        }
//        this.mDownloadRateTV.setText(rate);
//        int remainSecond = receivedPerSecond == 0 ? 0 : (int) ((contentLength - receivedCount) / receivedPerSecond);
//        String remainSecondString = "";
//        if (remainSecond < 60) {
//            remainSecondString = String.valueOf(remainSecond) + "s";
//        } else if (remainSecond / 60 > 0 && (remainSecond / 60) / 60 == 0) {
//            remainSecondString = String.valueOf(remainSecond / 60) + "min";
//        } else if ((remainSecond / 60) / 60 > 0) {
//            remainSecondString = String.valueOf((remainSecond / 60) / 60) + "h";
//        }
//        this.mRemainTimeTV.setText(this.mContext.getString(R.string.remain_time) + " " + remainSecondString);
//    }
//
//    protected void onDestroy() {
//        Log.d(this.TAG, "ondestroy");
//        if (mWakeLock != null && mWakeLock.isHeld()) {
//            mWakeLock.release();
//        }
//        clearNotification();
//        this.mNotifyManager = null;
//        if (this.mBinder != null) {
//            this.mBinder.unLockWorkHandler();
//        }
//        this.mContext.unbindService(this.mConnection);
//        super.onDestroy();
//    }
//
//    protected void onPause() {
//        Log.d(this.TAG, "onPause");
//        super.onPause();
//    }
//
//    protected void onRestart() {
//        Log.d(this.TAG, "onRestart");
//        super.onRestart();
//    }
//
//    protected void onStart() {
//        Log.d(this.TAG, "onStart");
//        super.onStart();
//    }
//
//    protected void onStop() {
//        Log.d(this.TAG, "onStop");
//        super.onStop();
//    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode != 4) {
//            return super.onKeyDown(keyCode, event);
//        }
//        ActivityInfo ai = this.homeInfo.activityInfo;
//        Intent startIntent = new Intent("android.intent.action.MAIN");
//        startIntent.addCategory("android.intent.category.LAUNCHER");
//        startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
//        startActivitySafely(startIntent);
//        return true;
//    }
//
//    void startActivitySafely(Intent intent) {
//        intent.addFlags(268435456);
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//        } catch (SecurityException e2) {
//        }
//    }
//
//    private FTPRequestInfo parseFtpUri(String uri) {
//        FTPRequestInfo info = new FTPRequestInfo();
//        try {
//            String[] s = uri.split("//");
//            if (s[1].contains("@")) {
//                String[] s2 = s[1].split("@", 2);
//                String[] s3 = s2[0].split(":", 2);
//                info.setUsername(s3[0]);
//                info.setPassword(s3[1]);
//                String[] s4 = s2[1].split(":", 2);
//                if (s4.length > 1) {
//                    info.setHost(s4[0]);
//                    info.setPort(Integer.valueOf(s4[1].substring(0, s4[1].indexOf("/"))).intValue());
//                    info.setRequestPath(s4[1].substring(s4[1].indexOf("/")));
//                } else {
//                    info.setHost(s4[0].substring(0, s4[0].indexOf("/")));
//                    info.setRequestPath(s4[0].substring(s4[0].indexOf("/")));
//                }
//                info.dump();
//                return info;
//            }
//            String[] str = s[1].split(":", 2);
//            if (str.length > 1) {
//                info.setHost(str[0]);
//                info.setPort(Integer.valueOf(str[1].substring(0, str[1].indexOf("/"))).intValue());
//                info.setRequestPath(str[1].substring(str[1].indexOf("/")));
//            } else {
//                info.setHost(str[0].substring(0, str[0].indexOf("/")));
//                info.setRequestPath(str[0].substring(str[0].indexOf("/")));
//            }
//            info.dump();
//            return info;
//        } catch (Exception e) {
//            Log.e(this.TAG, "parseFtpUri error....!");
//        }
//    }
}
