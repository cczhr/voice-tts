package com.iflytek.aisound;

import android.util.Log;

import com.iflytek.speechcloud.tts.filelog.TtsFileLog;


public class Aisound {
    private static final int ERROR_NO_LIBRARY = -1;
    private static final String LIB_NAME = "ttsaisound";
    private static final String TAG = "TTS_Aisound5";
    private static IAisoundCallback mAisoundListener;
    private static boolean mIsJniLoaded;

    public interface IAisoundCallback {
        void onOutPutCallBack(int i, byte[] bArr);

        void onProgressCallBack(int i, int i2);

        void onReadResCallBack(int i, int i2, int i3);

        void onWatchCallBack(int i);
    }

    public static native int JniCreate();

    public static native int JniDestory();

    public static native int JniGetParam(int i);

    public static native int JniGetVersion();

    private static native int JniInputTextCopy(String str);

    public static native void JniReadResCopy(byte[] bArr, int i);

    public static native int JniSetParam(int i, int i2);

    public static native int JniSpeak(String str, int i, int i2, int i3, int i4, int i5, int i6);

    public static native int JniStop();

    public static native int JniStreamSpeak();

    static {
        mIsJniLoaded = false;
        try {
            System.loadLibrary(LIB_NAME);
            mIsJniLoaded = true;
        } catch (Exception e) {
            Log.e(TAG, "loadLibrary failed");
        }
    }

    public static boolean isJniLoaded() {
        return mIsJniLoaded;
    }

    public static void setAisoundCallbak(IAisoundCallback listener) {
        mAisoundListener = listener;
    }

    public static int setParam(int id, int value) {
        if (mIsJniLoaded) {
            return JniSetParam(id, value);
        }
        return -1;
    }

    public static int create(String resFileName) {
        if (mIsJniLoaded) {
            return JniCreate();
        }
        return -1;
    }

    public static int destory() {
        if (mIsJniLoaded) {
            return JniDestory();
        }
        return -1;
    }

    public static int speak(String text, int roleCn, int roleEn, int effect, int speed, int tone, int volume) {
        if (mIsJniLoaded) {
            return JniSpeak(text, roleCn, roleEn, effect, speed, tone, volume);
        }
        return -1;
    }

    public static int stop() {
        if (mIsJniLoaded) {
            return JniStop();
        }
        return -1;
    }

    public static int getParam(int id) {
        if (mIsJniLoaded) {
            return JniGetParam(id);
        }
        return -1;
    }

    public static void onJniOutputCB(int length, byte[] data) {
        if (mAisoundListener != null) {
            TtsFileLog.writeTtsPcmData(data);
            mAisoundListener.onOutPutCallBack(length, data);
        }
    }

    public static void onJniWatchCB(int type) {
        if (mAisoundListener != null) {
            mAisoundListener.onWatchCallBack(type);
        }
    }

    public static void onJniProgressCB(int pos, int len) {
        if (mAisoundListener != null) {
            mAisoundListener.onProgressCallBack(pos, len);
        }
    }

    public static void onJniReadResCB(int offset, int length, int resIndex) {
        if (mAisoundListener != null) {
            mAisoundListener.onReadResCallBack(offset, length, resIndex);
        }
    }

    public static void onJniInputCB(int offset, int length) {
    }
}
