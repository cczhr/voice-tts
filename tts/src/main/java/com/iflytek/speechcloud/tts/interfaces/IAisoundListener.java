package com.iflytek.speechcloud.tts.interfaces;

public interface IAisoundListener {
    void onOutPutCallBack(byte[] bArr, int i);

    void onProgressCallBack(int i, int i2);
}
