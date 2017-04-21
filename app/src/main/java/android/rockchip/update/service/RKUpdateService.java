package android.rockchip.update.service;


public class RKUpdateService {

    static {
        System.loadLibrary("rockchip_update_jni");

    }

    public static native String getImageProductName(String str);

    public static native String getImageVersion(String str);







}
