package com.iflytek.speech.engines.processor.aitalk.recognizer.abstracts;

import android.content.Context;

public abstract class AitalkAccessor {
    protected static final String KEY_IS_BUILD = "grammar_is_build";
    protected static final String TAG = "AitalkAccessor";
    protected Context mContext;

    public abstract int SwitchMode(boolean z);

    public abstract int addLexicon(String str, String[] strArr, int i);

    public abstract int appendData(byte[] bArr, int i);

    public abstract boolean buildGrammar(byte[] bArr);

    public abstract boolean creatAitalkEngine(Context context);

    public abstract int deleteLexicon(String str, String[] strArr);

    public abstract void destroy();

    public abstract int endData();

    public abstract int insertLexicon(String str, String[] strArr, int i);

    public abstract boolean loadGrammar(String str);

    public abstract int setDeNoiseEnable(boolean z);

    public abstract int setGrammarPath(String str);

    public abstract int setSampleRate(int i);

    public abstract int startTalk(String str, IAitalkListener iAitalkListener);

    public abstract void stopTalk();

    public abstract int updateGrammar(String str);
}
