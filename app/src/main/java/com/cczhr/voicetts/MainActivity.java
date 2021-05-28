package com.cczhr.voicetts;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;

import com.cczhr.TTS;


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