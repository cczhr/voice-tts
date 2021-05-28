package com.iflytek.speechcloud;

import com.iflytek.business.speech.SpeechError;

import java.util.ArrayList;

public interface SynthesizerListener {
    void onBufferReceived(ArrayList<byte[]> arrayList, int i, int i2, int i3, String str);

    void onCancel();

    void onConnected();

    void onEnd(SpeechError speechError);
}
