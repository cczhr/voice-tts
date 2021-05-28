package com.iflytek.logagent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;


import java.util.HashMap;

import org.json.JSONObject;

public class LogAgent {
    public static final String KEY_SUB_TYPE_DOWNLOAD = "download";
    public static final String KEY_SUB_TYPE_SERVICE = "service";
    public static final String KEY_TYPE_BINDER = "binder";
    public static final String KEY_TYPE_CLICK = "click";
    public static final String KEY_TYPE_ERROR = "error";
    public static final String KEY_TYPE_STATISTIC = "statistic";
    public static final String KEY_TYPE_USERINFO = "userinfo";
    private static final String TAG = "LogAgent";
    private static LogAgent mInstance = null;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public Handler handler;

    /* access modifiers changed from: private */
    public int retryCount = 1;
    /* access modifiers changed from: private */
    public long retryIntervalTime = 600000;

    private LogAgent() {
        delOldVersionLog();
        HandlerThread localHandlerThread = new HandlerThread(TAG);
        localHandlerThread.start();
        this.handler = new Handler(localHandlerThread.getLooper());
    }

    public static LogAgent getLogAgent() {
        if (mInstance == null) {
            mInstance = new LogAgent();
        }
        return mInstance;
    }


    public void onError(Context context2, String error) {
    }

    private void postErrorInfo(Context context2, String error) {
    }

    public void onStatistic(Context context2, String subType, String name) {
    }

    private void postStatisticInfo(Context context2, String subType, String name) {
    }

    public void onUserInfo(Context context2, String name, String info) {
    }

    private void postUserInfo(Context context2, String name, String info) {
    }

    public void onBinder(String binderName, String info, HashMap<String, String> hashMap) {
    }

    private void postBinderInfo(String binder, String info, HashMap<String, String> hashMap) {
    }

    public void onEvent(Context context2, String btnName) {
    }

    private void postEventInfo(Context context2, String event_id) {
    }

    public void postLog(Context context2, String type, String name, String info, HashMap<String, String> hashMap) {
    }

    private void postInfo(Context context2, String type, String name, String info, HashMap<String, String> hashMap) {
    }

    public void saveInfoToFile(String type, JSONObject info) {
    }


    public void uploadLog(Context context2) {
    }

    public boolean isNeedUpload() {
        return false;
    }

    /* access modifiers changed from: private */
    public void uploadAllLog(Context context2) {
    }

    private void delOldVersionLog() {
    }
}
