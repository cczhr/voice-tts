package com.cczhr.voicetts;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.speech.tts.SynthesisCallback;
import android.speech.tts.SynthesisRequest;
import android.view.View;

import com.cczhr.TTS;
import com.cczhr.TTSConstants;
import com.iflytek.aisound.Aisound;
import com.iflytek.business.SpeechConfig;
import com.iflytek.speechcloud.TtsService;
import com.iflytek.speechcloud.tts.impl.AisoundEngine;

public class MainActivity extends AppCompatActivity {
    private TTS tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tts=TTS.getInstance();
        tts.init(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.release();
    }

    public void speek(View view) {
        tts.speakText("这是一条测试语音12345678999hello");

    }

    public void stopAndSpeakText(View view) {
        tts.stopAndSpeakText("哈哈哈哈哈");
    }

    public void stop(View view) {
        tts.stop();

    }
}