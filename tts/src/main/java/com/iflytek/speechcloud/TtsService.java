package com.iflytek.speechcloud;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.SynthesisCallback;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.business.SpeechConfig;


import com.iflytek.logagent.LogAgent;
import com.iflytek.speech.engines.processor.aitalk.result.AitalkResult;
import com.iflytek.speechcloud.binder.impl.LocalTtsPlayer;
import com.iflytek.speechcloud.binder.impl.AiConstants;
import com.iflytek.speechcloud.tts.impl.AisoundEngine;
import com.iflytek.speechcloud.tts.interfaces.IAisoundListener;

import java.io.File;

@TargetApi(14)
public class TtsService  {
    private static String[] SUPORT_LANG = {"zho-CHN", "eng-USA"};
    private static final String TAG = "TTS_TtsService";
    private static final int ivTTS_SPEED_MAX = 32767;
    private static final int ivTTS_SPEED_MIN = -32768;
    private static final int ivTTS_SPEED_NORMAL = 0;
    private AisoundEngine mAisound;
    private IAisoundListener mAisoundListener = new IAisoundListener() {
        public void onOutPutCallBack(byte[] data, int watch) {
            if (TtsService.this.mCallback != null) {
                TtsService.this.mCallback.audioAvailable(data, 0, data.length);
            }
        }

        public void onProgressCallBack(int proBegin, int procLen) {

        }
    };
    /* access modifiers changed from: private */
    public SynthesisCallback mCallback;
    private String mSavedRole = null;

    private Context context;

    public void onCreate(Context context) {
        this.context=context.getApplicationContext();
        String name = SpeechConfig.getString(context, SpeechConfig.KEY_SPEAKER_SETTING, (String) null);
        if (TextUtils.isEmpty(name)) {
            name = "xiaomei";
        }
        this.mSavedRole = name;
        String tempPath = String.valueOf(AiConstants.getPath(context)) + name + AiConstants.TTS_RESOURCE_AUFFIX_IRF;
        String resPath = new File(tempPath).exists() ? tempPath : "aisound/" + name + AiConstants.TTS_RESOURCE_AUFFIX_MP3;
        Intent intent = new Intent();
        intent.putExtra(LocalTtsPlayer.STANDERD_VOICE_NAME, resPath);
        AisoundEngine.reset();
        this.mAisound = AisoundEngine.getEngine(context, LocalTtsPlayer.ResMode.STANDARD, intent);
    }

    public void onDestroy() {

        this.mAisound.destory();
    }

    /* access modifiers changed from: protected */
    public String[] onGetLanguage() {
        return SUPORT_LANG;
    }

    /* access modifiers changed from: protected */
    public int onIsLanguageAvailable(String lang, String country, String variant) {
        Log.e(TAG, "onIsLanguageAvailable LANG=" + lang + " country=" + country + " variant=" + variant);
        String[] strArr = SUPORT_LANG;
        int length = strArr.length;
        int i = 0;
        while (i < length && !lang.equals(strArr[i])) {
            i++;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public synchronized int onLoadLanguage(String lang, String country, String variant) {
        Log.e(TAG, "onLoadLanguage LANG=" + lang + " country=" + country + " variant=" + variant);
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mAisound.stop();
    }


    private int convertToLocalSpeecd(int speechrate) {
        switch (speechrate) {
            case AitalkResult.RESULT_CONFIDENCE:
                return ivTTS_SPEED_MIN;
            case 80:
                return -16384;
            case 100:
                return 0;
            case 150:
                return 16383;
            case 200:
                return ivTTS_SPEED_MAX;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: protected */


    //Gets the speech rate to use. The normal rate is 100.
    @SuppressLint("WrongConstant")
    public synchronized void onSynthesizeText(String text,int rate, SynthesisCallback callback) {
        String resPath;
        String newRole = SpeechConfig.getString(context, SpeechConfig.KEY_SPEAKER_SETTING, "xiaoyan");
        String tempPath = String.valueOf(AiConstants.getPath(context)) + newRole + AiConstants.TTS_RESOURCE_AUFFIX_IRF;
        if (new File(tempPath).exists()) {
            resPath = tempPath;
        } else {
            resPath = "aisound/" + newRole + AiConstants.TTS_RESOURCE_AUFFIX_MP3;
        }
        Intent intent = new Intent();
        intent.putExtra(LocalTtsPlayer.STANDERD_VOICE_NAME, resPath);
        AisoundEngine.reset();
        this.mAisound = AisoundEngine.getEngine(context, LocalTtsPlayer.ResMode.STANDARD, intent);
        this.mSavedRole = newRole;
        this.mCallback = callback;
        Log.e(TAG, "onSynthesizeText---3--");
        callback.start(16000, 2, 1);
        LogAgent.getLogAgent().onStatistic((Context) null, LogAgent.KEY_SUB_TYPE_SERVICE, LogConstants.LOG_BINDER_SUB_TYPE_TTS_LOCAL);
        this.mAisound.speak(text, 0, 0, 0, convertToLocalSpeecd(rate), 0, 0, this.mAisoundListener);// convertToLocalSpeecd(rate)
        Log.e(TAG, "onSynthesizeText---4--");
        callback.done();

    }
}
