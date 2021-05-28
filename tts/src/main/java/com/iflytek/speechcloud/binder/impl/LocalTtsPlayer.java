package com.iflytek.speechcloud.binder.impl;

import android.content.Context;
import android.content.Intent;


import com.iflytek.speechcloud.tts.impl.AisoundEngine;

import java.util.ArrayList;

public class LocalTtsPlayer  {
    private static final int MSG_TTS = 0;
    public static final String STANDERD_VOICE_NAME = "standard_voice_name";
    private static final String TAG = "LocalTtsPlayer";
    private static final int ivTTS_PARAM_MAX = 32767;
    private static final int ivTTS_PARAM_MIN = -32768;
    private static LocalTtsPlayer mInstance = null;
    /* access modifiers changed from: private */
    public AisoundEngine mAisound;
    /* access modifiers changed from: private */
    public boolean mAudioFocus = true;
    /* access modifiers changed from: private */

    /* access modifiers changed from: private */
    public Context mContext = null;
    /* access modifiers changed from: private */
    public ArrayList<byte[]> mDatas = null;
    /* access modifiers changed from: private */

    /* access modifiers changed from: private */
    public String mParams = null;
    /* access modifiers changed from: private */
    public int mPitch = 0;
    /* access modifiers changed from: private */
    public boolean mPlayStarted = false;
    /* access modifiers changed from: private */


    private Intent mResPathOrName = null;
    private String mRole_cn = "";
    /* access modifiers changed from: private */
    public int mSpeed = 0;
    /* access modifiers changed from: private */

    /* access modifiers changed from: private */
    public int mUnicodeIndex = 0;
    /* access modifiers changed from: private */
    public int mUnicodeLength = 0;
    private boolean mVoiceChanged = false;
    private String mVoiceName = "";
    /* access modifiers changed from: private */
    public int mVolume = 0;

    public enum ResMode {
        STANDARD,
        YUDIAN
    }

}
