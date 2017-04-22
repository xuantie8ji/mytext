package com.jsp.updaterk312;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpHead;

public class RKUpdateService extends Service {
    public static String CACHE_ROOT;
    public static String DATA_ROOT;
    public static String FLASH_ROOT;
    private static final String[] IMAGE_FILE_DIRS;
    public static String OTA_PACKAGE_FILE;
    public static String RKIMAGE_FILE;
    public static String SDCARD_ROOT;
    public static String USB_ROOT;
    private static volatile boolean mIsNeedDeletePackage;
    public static URI mRemoteURI;
    public static URI mRemoteURIBackup;
    private static volatile boolean mWorkHandleLocked;
    private SharedPreferences mAutoCheckSet;
    private final LocalBinder mBinder;
    private Context mContext;
    private String mDescription;
    private volatile boolean mIsFirstStartUp;
    private volatile boolean mIsOtaCheckByHand;
    private String mLastUpdatePath;
    private Handler mMainHandler;
    private String mOtaPackageLength;
    private String mOtaPackageName;
    private String mOtaPackageVersion;
    private String mSystemVersion;
    private String mTargetURI;
    private boolean mUseBackupHost;
    private WorkHandler mWorkHandler;
    private static Context context;



    public static Context getAppContext() {
        return RKUpdateService.context;
    }
    /* renamed from: android.rockchip.update.service.RKUpdateService.1 */
    class C00131 implements FileFilter {
        C00131() {
        }

        public boolean accept(File arg0) {
            RKUpdateService.LOG("scan usb files: " + arg0.getAbsolutePath());
            if (arg0.isDirectory()) {
                return false;
            }
            if (arg0.getName().equals(RKUpdateService.RKIMAGE_FILE) || arg0.getName().equals(RKUpdateService.OTA_PACKAGE_FILE)) {
                return true;
            }
            return false;
        }
    }

    /* renamed from: android.rockchip.update.service.RKUpdateService.2 */
    class C00142 implements Runnable {
        final /* synthetic */ CharSequence val$msg;

        C00142(CharSequence charSequence) {
            this.val$msg = charSequence;
        }

        public void run() {
            Toast.makeText(RKUpdateService.this.getApplicationContext(), this.val$msg, 1).show();
        }
    }


    //---------------------------------------

    public class LocalBinder extends Binder {
        public void updateFirmware(String imagePath, int mode) {
            RKUpdateService.LOG("updateFirmware(): imagePath = " + imagePath);
            try {
                RKUpdateService.mWorkHandleLocked = true;
                if (mode == 2) {
                    RecoverySystem.installPackage(RKUpdateService.this.mContext, new File(imagePath));
                } else if (mode == 1) {
                    RecoverySystem.installRKimage(RKUpdateService.this.mContext, imagePath);
                }
            } catch (IOException e) {
                Log.e("RKUpdateService", "updateFirmware() : Reboot for updateFirmware() failed", e);
            }
        }

        public boolean doesOtaPackageMatchProduct(String imagePath) {
            RKUpdateService.LOG("doesImageMatchProduct(): start verify package , imagePath = " + imagePath);
            try {
                RecoverySystem.verifyPackage(new File(imagePath), null, null);
                return true;
            } catch (GeneralSecurityException e) {
                RKUpdateService.LOG("doesImageMatchProduct(): verifaPackage faild!");
                return false;
            } catch (IOException e2) {
                RKUpdateService.LOG("doesImageMatchProduct(): verifaPackage faild!");
                return false;
            }
        }

        public void deletePackage(String path) {
            RKUpdateService.LOG("try to deletePackage...");
            File f = new File(path);
            if (f.exists()) {
                f.delete();
                RKUpdateService.LOG("delete complete! path=" + path);
                return;
            }
            RKUpdateService.LOG("path=" + path + " ,file not exists!");
        }

        public void unLockWorkHandler() {
            RKUpdateService.LOG("unLockWorkHandler...");
            RKUpdateService.mWorkHandleLocked = false;
        }

        public void LockWorkHandler() {
            RKUpdateService.mWorkHandleLocked = true;
            RKUpdateService.LOG("LockWorkHandler...!");
        }
    }


    //---------------------------------------




    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    RKUpdateService.LOG("WorkHandler::handleMessage() : To perform 'COMMAND_CHECK_LOCAL_UPDATING'.");
                    if (RKUpdateService.mWorkHandleLocked) {
                        RKUpdateService.LOG("WorkHandler::handleMessage() : locked !!!");
                        return;
                    }
                    String[] searchResult = RKUpdateService.this.getValidFirmwareImageFile(RKUpdateService.IMAGE_FILE_DIRS);
                    if (searchResult == null) {
                        return;
                    }
                    if (1 == searchResult.length) {
                        String path = searchResult[0];
                        String imageFileVersion = null;
                        String currentVersion = null;
                        if (path.endsWith("img")) {
                            if (RKUpdateService.this.checkRKimage(path)) {
                                imageFileVersion = android.rockchip.update.service.RKUpdateService.getImageVersion(path);
                                RKUpdateService.LOG("WorkHandler::handleMessage() : Find a VALID image file : '" + path + "'. imageFileVersion is '" + imageFileVersion);
                                currentVersion = RKUpdateService.this.getCurrentFirmwareVersion();
                                RKUpdateService.LOG("WorkHandler::handleMessage() : Current system firmware version : '" + currentVersion + "'.");
                            } else {
                                RKUpdateService.LOG("WorkHandler::handleMessage() : not a valid rkimage !!");
                                return;
                            }
                        }
                        RKUpdateService.this.startProposingActivity(path, imageFileVersion, currentVersion);
                        return;
                    }
                    RKUpdateService.LOG("find more than two package files, so it is invalid!");
                case 2:
//                    if (RKUpdateService.mWorkHandleLocked) {
//                        RKUpdateService.LOG("WorkHandler::handleMessage() : locked !!!");
//                        return;
//                    }
//                    int i = 0;
//                    while (i < 2) {
//                        boolean result = false;
//                        if (i == 0) {
//                            try {
//                                RKUpdateService.this.mUseBackupHost = false;
////                                result = RKUpdateService.this.requestRemoteServerForUpdate(RKUpdateService.mRemoteURI);
//                            } catch (Exception e) {
//                                RKUpdateService.LOG("request remote server error...");
//                                RKUpdateService.this.myMakeToast(RKUpdateService.this.mContext.getString(R.string.current_new));
//                                try {
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException e2) {
//                                    e2.printStackTrace();
//                                }
//                                i++;
//                            }
//                        }
//                        else {
//                            RKUpdateService.this.mUseBackupHost = true;
//                            try {
//                                result = RKUpdateService.this.requestRemoteServerForUpdate(RKUpdateService.mRemoteURIBackup);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (result) {
//                            RKUpdateService.LOG("find a remote update package, now start PackageDownloadActivity...");
//                            RKUpdateService.this.startNotifyActivity();
//                            return;
//                        }
                        RKUpdateService.LOG("no find remote update package...");
                        RKUpdateService.this.myMakeToast(RKUpdateService.this.mContext.getString(R.string.current_new));
                        return;
//                    }
                case 4:
                    if (RKUpdateService.mIsNeedDeletePackage) {
                        RKUpdateService.LOG("execute COMMAND_DELETE_UPDATEPACKAGE...");
                        File f = new File(RKUpdateService.this.mLastUpdatePath);
                        if (f.exists()) {
                            f.delete();
                            RKUpdateService.LOG("delete complete! path=" + RKUpdateService.this.mLastUpdatePath);
                        } else {
                            RKUpdateService.LOG("path=" + RKUpdateService.this.mLastUpdatePath + " ,file not exists!");
                        }
                        RKUpdateService.mIsNeedDeletePackage = false;
                        RKUpdateService.mWorkHandleLocked = false;
                    }
                default:
            }
        }
    }

//    private static native String getImageProductName(String str);
//
//    private static native String getImageVersion(String str);

    public RKUpdateService() {
        this.mIsFirstStartUp = true;
        this.mTargetURI = null;
        this.mUseBackupHost = false;
        this.mOtaPackageVersion = null;
        this.mSystemVersion = null;
        this.mOtaPackageName = null;
        this.mOtaPackageLength = null;
        this.mDescription = null;
        this.mIsOtaCheckByHand = false;
        this.mBinder = new LocalBinder();
    }

    private static void LOG(String msg) {
        Log.d("RKUpdateService", msg);
    }

    static {
//        System.loadLibrary("rockchip_update_jni");
        android.rockchip.update.service.RKUpdateService.loadLibary();
        OTA_PACKAGE_FILE = "update.zip";
        RKIMAGE_FILE = "update.img";
        mWorkHandleLocked = false;
        mIsNeedDeletePackage = false;
        DATA_ROOT = "/data/media/0";
        FLASH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
        SDCARD_ROOT = "/mnt/external_sd";
        USB_ROOT = "/mnt/usb_storage";
        CACHE_ROOT = Environment.getDownloadCacheDirectory().getAbsolutePath();
        IMAGE_FILE_DIRS = new String[]{DATA_ROOT + "/", FLASH_ROOT + "/", SDCARD_ROOT + "/", USB_ROOT + "/"};
        mRemoteURI = null;
        mRemoteURIBackup = null;
    }

    public IBinder onBind(Intent arg0) {
        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        RKUpdateService.context = getApplicationContext();
        this.mContext = this;
        LOG("starting RKUpdateService, version is 1.8.0");
        if (getMultiUserState()) {
            FLASH_ROOT = DATA_ROOT;
        }
        String ota_packagename = getOtaPackageFileName();
        if (ota_packagename != null) {
            OTA_PACKAGE_FILE = ota_packagename;
            LOG("get ota package name private is " + OTA_PACKAGE_FILE);
        }
        String rk_imagename = getRKimageFileName();
        if (rk_imagename != null) {
            RKIMAGE_FILE = rk_imagename;
            LOG("get rkimage name private is " + RKIMAGE_FILE);
        }
        try {
            mRemoteURI = new URI(getRemoteUri());
            mRemoteURIBackup = new URI(getRemoteUriBackup());
            LOG("remote uri is " + mRemoteURI.toString());
            LOG("remote uri backup is " + mRemoteURIBackup.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.mAutoCheckSet = getSharedPreferences("auto_check", 0);
        this.mMainHandler = new Handler(Looper.getMainLooper());
        HandlerThread workThread = new HandlerThread("UpdateService : work thread");
        workThread.start();
        this.mWorkHandler = new WorkHandler(workThread.getLooper());
        if (this.mIsFirstStartUp) {
            LOG("first startup!!!");
            this.mIsFirstStartUp = false;
            String command = RecoverySystem.readFlagCommand();
            if (command != null) {
                LOG("command = " + command);
                if (command.contains("$path")) {
                    String path = command.substring(command.indexOf(61) + 1);
                    LOG("last_flag: path = " + path);
                    Intent intent;
                    if (command.startsWith("success")) {
                        LOG("now try to start notifydialog activity!");
                        intent = new Intent(this.mContext, NotifyDeleteActivity.class);
                        //Intent.FLAG_ACTIVITY_NEW_TASK=268435456
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("flag", 1);
                        intent.putExtra("path", path);
                        startActivity(intent);
                        mWorkHandleLocked = true;
                    } else if (command.startsWith("updating")) {
                        intent = new Intent(this.mContext, NotifyDeleteActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("flag", 2);
                        intent.putExtra("path", path);
                        startActivity(intent);
                        mWorkHandleLocked = true;
                    }
                }
            }
        }
    }

    public void onDestroy() {
        LOG("onDestroy.......");
        super.onDestroy();
    }

    public void onStart(Intent intent, int startId) {
        LOG("onStart.......");
        super.onStart(intent, startId);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LOG("onStartCommand.......");
        if (intent == null) {
            return 2;
        }
        int command = intent.getIntExtra("command", 0);
        int delayTime = intent.getIntExtra("delay", 1000);
        LOG("command = " + command + " delaytime = " + delayTime);
        if (command == 0) {
            return 2;
        }
        if (command == 2) {
            this.mIsOtaCheckByHand = false;
            if (!this.mAutoCheckSet.getBoolean("auto_check", true)) {
                LOG("user set not auto check!");
                return 2;
            }

        }
        if (command == 3) {
            this.mIsOtaCheckByHand = true;
            command = 2;
        }
        if (mIsNeedDeletePackage) {
            command = 4;
            delayTime = 20000;
            mWorkHandleLocked = true;
        }
        Message msg = new Message();
        msg.what = command;
        msg.arg1 = 0;
        this.mWorkHandler.sendMessageDelayed(msg, (long) delayTime);
        return 3;
    }

    private String[] getValidFirmwareImageFile(String[] searchPaths) {
        for (String dir_path : searchPaths) {
            String filePath = dir_path + OTA_PACKAGE_FILE;
            LOG("getValidFirmwareImageFile() : Target image file path : " + filePath);
            if (new File(filePath).exists()) {
                return new String[]{filePath};
            }
        }
        for (String dir_path2 : searchPaths) {
            if (new File(dir_path2 + RKIMAGE_FILE).exists()) {
                return new String[]{dir_path2 + RKIMAGE_FILE};
            }
        }
        File usbRoot = new File(USB_ROOT);
        if (usbRoot.listFiles() == null) {
            return null;
        }
        for (File tmp : usbRoot.listFiles()) {
            if (tmp.isDirectory()) {
                File[] files = tmp.listFiles(new C00131());
                if (files != null && files.length > 0) {
                    return new String[]{files[0].getAbsolutePath()};
                }
            }
        }
        return null;
    }

    private void startProposingActivity(String path, String imageVersion, String currentVersion) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.jsp.updaterk312", "com.jsp.updaterk312.FirmwareUpdatingActivity"));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("android.rockchip.update.extra.IMAGE_PATH", path);
        intent.putExtra("android.rockchip.update.extra.IMAGE_VERSION", imageVersion);
        intent.putExtra("android.rockchip.update.extra.CURRENT_VERSION", currentVersion);
        this.mContext.startActivity(intent);
    }

    private boolean checkRKimage(String path) {
        String imageProductName = android.rockchip.update.service.RKUpdateService.getImageProductName(path);
        LOG("checkRKimage() : imageProductName = " + imageProductName);
        if (imageProductName != null && imageProductName.trim().equals(getProductName())) {
            return true;
        }
        return false;
    }


    private String getOtaPackageFileName() {
//        String str = SystemPropertiesProxy.get(this,"ro.ota.packagename");
        String str =SystemProperties.get("ro.ota.packagename");
        if (str == null || str.length() == 0) {
            return null;
        }
        if (str.endsWith(".zip")) {
            return str;
        }
        return str + ".zip";
    }

    private String getRKimageFileName() {
        String str = SystemProperties.get("ro.rkimage.name");
//        String str =SystemPropertiesProxy.get(this,"ro.rkimage.name");
        if (str == null || str.length() == 0) {
            return null;
        }
        if (str.endsWith(".img")) {
            return str;
        }
        return str + ".img";
    }

    private String getCurrentFirmwareVersion() {
//        return SystemPropertiesProxy.get(this,"ro.firmware.version");
        return SystemProperties.get("ro.firmware.version");
    }

    private static String getProductName() {
//        return SystemPropertiesProxy.get(context,"ro.product.model");
        return SystemProperties.get("ro.product.model");
    }

    private void makeToast(CharSequence msg) {
        this.mMainHandler.post(new C00142(msg));
    }

    public static String getRemoteUri() {
        return "http://" + getRemoteHost() + "/OtaUpdater/android?product=" + getOtaProductName() + "&version=" + getSystemVersion() + "&sn=" + getProductSN() + "&country=" + getCountry() + "&language=" + getLanguage();
    }

    public static String getRemoteUriBackup() {
        return "http://" + getRemoteHostBackup() + "/OtaUpdater/android?product=" + getOtaProductName() + "&version=" + getSystemVersion() + "&sn=" + getProductSN() + "&country=" + getCountry() + "&language=" + getLanguage();
    }

    public static String getRemoteHost() {
//        String remoteHost = SystemPropertiesProxy.get(context,"ro.product.ota.host");
        String remoteHost = SystemProperties.get("ro.product.ota.host");
        if (remoteHost == null || remoteHost.length() == 0) {
            return "192.168.1.143:2300";
        }
        return remoteHost;
    }

    public static String getRemoteHostBackup() {
//        String remoteHost = SystemPropertiesProxy.get(context,"ro.product.ota.host2");
        String remoteHost = SystemProperties.get("ro.product.ota.host2");
        if (remoteHost == null || remoteHost.length() == 0) {

            return "192.168.1.143:2300";
        }
        return remoteHost;
    }

    public static String getOtaProductName() {
//        String productName = SystemPropertiesProxy.get(context,"ro.product.model");
        String productName = SystemProperties.get("ro.product.model");
        if (productName.contains(" ")) {
            return productName.replaceAll(" ", "");
        }
        return productName;
    }

    public static boolean getMultiUserState() {
//        String multiUser = SystemPropertiesProxy.get(context,"ro.factory.hasUMS");
        String multiUser = SystemProperties.get("ro.factory.hasUMS");
        if (multiUser == null || multiUser.length() <= 0) {
//            multiUser = SystemPropertiesProxy.get(context,"ro.factory.storage_policy");
            multiUser = SystemProperties.get("ro.factory.storage_policy");
            if (multiUser == null || multiUser.length() <= 0) {
                return false;
            }
            return multiUser.equals("1");
        } else if (multiUser.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    private void startNotifyActivity() {
        Intent intent = new Intent(this.mContext, OtaUpdateNotifyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("uri", this.mTargetURI);
        intent.putExtra("OtaPackageLength", this.mOtaPackageLength);
        intent.putExtra("OtaPackageName", this.mOtaPackageName);
        intent.putExtra("OtaPackageVersion", this.mOtaPackageVersion);
        intent.putExtra("SystemVersion", this.mSystemVersion);
        intent.putExtra("description", this.mDescription);
        this.mContext.startActivity(intent);
        mWorkHandleLocked = true;
    }

    private void myMakeToast(CharSequence msg) {
        if (this.mIsOtaCheckByHand) {
            makeToast(msg);
        }
    }

    private boolean requestRemoteServerForUpdate(URI remote) throws IOException, ClientProtocolException {
        if (remote == null) {
            return false;
        }
        HttpResponse response = CustomerHttpClient.getHttpClient().execute(new HttpHead(remote));
        if (response.getStatusLine().getStatusCode() != 200) {
            return false;
        }
        for (Header header : response.getAllHeaders()) {
            LOG(header.getName() + ":" + header.getValue());
        }
        Header[] headLength = response.getHeaders("OtaPackageLength");
        if (headLength != null && headLength.length > 0) {
            this.mOtaPackageLength = headLength[0].getValue();
        }
        Header[] headName = response.getHeaders("OtaPackageName");
        if (headName == null) {
            return false;
        }
        if (headName.length > 0) {
            this.mOtaPackageName = headName[0].getValue();
        }
        Header[] headVersion = response.getHeaders("OtaPackageVersion");
        if (headVersion != null && headVersion.length > 0) {
            this.mOtaPackageVersion = headVersion[0].getValue();
        }
        Header[] headTargetURI = response.getHeaders("OtaPackageUri");
        if (headTargetURI == null) {
            return false;
        }
        if (headTargetURI.length > 0) {
            this.mTargetURI = headTargetURI[0].getValue();
        }
        if (this.mOtaPackageName == null || this.mTargetURI == null) {
            LOG("server response format error!");
            return false;
        }
        Header[] headDescription = response.getHeaders("description");
        if (headDescription != null && headDescription.length > 0) {
            this.mDescription = new String(headDescription[0].getValue().getBytes("ISO8859_1"), "UTF-8");
        }
        if (!(this.mTargetURI.startsWith("http://") || this.mTargetURI.startsWith("https://") || this.mTargetURI.startsWith("ftp://"))) {
            String str;
            StringBuilder append = new StringBuilder().append("http://").append(this.mUseBackupHost ? getRemoteHostBackup() : getRemoteHost());
            if (this.mTargetURI.startsWith("/")) {
                str = this.mTargetURI;
            } else {
                str = "/" + this.mTargetURI;
            }
            this.mTargetURI = append.append(str).toString();
        }
        this.mSystemVersion = getSystemVersion();
        LOG("OtaPackageName = " + this.mOtaPackageName + " OtaPackageVersion = " + this.mOtaPackageVersion + " OtaPackageLength = " + this.mOtaPackageLength + " SystemVersion = " + this.mSystemVersion + "OtaPackageUri = " + this.mTargetURI);
        return true;
    }

    public static String getSystemVersion() {
//        String version = SystemPropertiesProxy.get(context,"ro.product.version");
        String version = SystemProperties.get("ro.product.version");
        if (version == null || version.length() == 0) {
            return "1.0.0";
        }
        return version;
    }

    public static String getProductSN() {
//        String sn = SystemPropertiesProxy.get(context,"ro.serialno");
        String sn = SystemProperties.get("ro.serialno");
        if (sn == null || sn.length() == 0) {
            return "unknown";
        }
        return sn;
    }

    public static String getCountry() {
        return Locale.getDefault().getCountry();
    }

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }
}
