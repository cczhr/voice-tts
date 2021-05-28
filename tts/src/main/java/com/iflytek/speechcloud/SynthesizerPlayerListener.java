package com.iflytek.speechcloud;

import com.iflytek.business.speech.SpeechError;

public interface SynthesizerPlayerListener {
    void onBufferPercent(int i, int i2, int i3, String str);

    void onEnd(SpeechError speechError);

    void onPlayBegin();

    void onPlayPaused();

    void onPlayPercent(int i, int i2, int i3);

    void onPlayResumed();
}
