package com.tc.tar;

/**
 * Created by aarontang on 2017/3/30.
 */

public class TARNativeInterface {
    public static final String TAG = TARNativeInterface.class.getSimpleName();

    public static native void nativeInit();
    public static native void nativeDestroy();
    public static native void nativeInitGL();
    public static native void nativeResize(int w, int h);
    public static native void nativeRender();
    public static native void nativeKey(int keycode);
}
