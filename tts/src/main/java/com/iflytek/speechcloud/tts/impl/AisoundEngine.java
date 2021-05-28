package com.iflytek.speechcloud.tts.impl;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iflytek.aisound.Aisound;
import com.iflytek.business.speech.SpeechError;
import com.iflytek.speechcloud.binder.impl.LocalTtsPlayer;
import com.iflytek.speechcloud.binder.impl.AiConstants;
import com.iflytek.speechcloud.tts.interfaces.IAisoundListener;
 
import java.util.concurrent.locks.ReentrantLock;

public class AisoundEngine implements Aisound.IAisoundCallback {
    private static final String TAG = "AisoundEngine";
    private static AisoundEngine mInstance = null;
    private int initCode = -1;
    private IAisoundListener mAisoundListener;
    private TtsResource mCommResource = null;
    private Context mContext = null;
    private LocalTtsPlayer.ResMode mResMode = LocalTtsPlayer.ResMode.STANDARD;
    private Intent mResPathOrName = null;
    private TtsResource mSpeekerResource = null;
    private ReentrantLock mTtsLock = new ReentrantLock();
    private int mWatchType;
    private Object synObj = new Object();

    public static synchronized AisoundEngine getEngine(Context ctx, LocalTtsPlayer.ResMode rm, Intent respath) {
        AisoundEngine aisoundEngine;
        synchronized (AisoundEngine.class) {
            if (mInstance == null) {
                mInstance = new AisoundEngine(ctx, rm, respath);
            }
            aisoundEngine = mInstance;
        }
        return aisoundEngine;
    }

    private AisoundEngine(Context ctx, LocalTtsPlayer.ResMode rm, Intent respath) {
        synchronized (this.synObj) {
            this.mContext = ctx;
            this.mResMode = rm;
            this.mResPathOrName = respath;
            Aisound.setAisoundCallbak(this);
            init();
        }
    }

    private synchronized int init() {
        Log.e(TAG, "AisoundEngine.init res dir ====== " + this.mResPathOrName);
        if (this.mCommResource == null) {
            Intent intent = new Intent();
            intent.putExtra(LocalTtsPlayer.STANDERD_VOICE_NAME, AiConstants.DEFAULT_VOICE_RES);
            this.mCommResource = new TtsResource(this.mContext, this.mResMode, intent);
        }
        if (this.mSpeekerResource == null) {
            this.mSpeekerResource = new TtsResource(this.mContext, this.mResMode, this.mResPathOrName);
            Log.e(TAG, "init res dir = " + this.mResPathOrName);
        }
        this.initCode = Aisound.create("");
        return this.initCode;
    }

    public boolean isBusy() {
        return this.mTtsLock.isLocked();
    }

    public void onReadResCallBack(int offset, int length, int resIndex) {
        byte[] buff;
        byte[] buff2;
        if (resIndex == 0) {
            if (this.mCommResource != null && (buff2 = this.mCommResource.readResource(offset, length)) != null) {
                Aisound.JniReadResCopy(buff2, buff2.length);
            }
        } else if (this.mSpeekerResource != null && (buff = this.mSpeekerResource.readResource(offset, length)) != null) {
            Aisound.JniReadResCopy(buff, buff.length);
        }
    }

    public void onOutPutCallBack(int length, byte[] data) {
        synchronized (this.synObj) {
            if (this.mAisoundListener != null) {
                this.mAisoundListener.onOutPutCallBack(data, this.mWatchType);
            }
        }
    }

    public void onProgressCallBack(int procBegin, int procLen) {
        synchronized (this.synObj) {
            if (this.mAisoundListener != null) {
                this.mAisoundListener.onProgressCallBack(procBegin, procLen);
            }
        }
    }

    public void onWatchCallBack(int type) {
    }

    public synchronized int speak(String text, int roleCn, int roleEn, int effect, int speed, int pitch, int volume, IAisoundListener listener) {
        int ret;
        if (this.initCode != 0) {
            init();
            if (this.initCode != 0) {
                ret = this.initCode;
            }
        }
        if (!this.mTtsLock.tryLock()) {
            ret = SpeechError.ivTTS_ERR_STATE_REFUSE;
        } else {
            this.mWatchType = 0;
            this.mAisoundListener = listener;
            ret = Aisound.speak(text, roleCn, roleEn, effect, speed, pitch, volume);
            this.mTtsLock.unlock();
        }
        return ret;
    }

    public int setParam(int id, int value) {
        return Aisound.setParam(id, value);
    }

    public static void reset() {
        if (mInstance != null) {
            mInstance.destory();
        }
        mInstance = null;
    }

    public synchronized void destory() {
        synchronized (this.synObj) {
            if (this.mCommResource != null) {
                this.mCommResource.close();
            }
            this.mCommResource = null;
            if (this.mSpeekerResource != null) {
                this.mSpeekerResource.close();
            }
            this.mSpeekerResource = null;
            Aisound.destory();
            this.initCode = -1;
        }
    }

    public int stop() {
        int stop;
        synchronized (this.synObj) {
            this.mAisoundListener = null;
            stop = Aisound.stop();
        }
        return stop;
    }

    public static boolean isLibraryLoaded() {
        return Aisound.isJniLoaded();
    }
}
