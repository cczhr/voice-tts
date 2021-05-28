package com.iflytek.aitalk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iflytek.business.speech.SpeechError;
import com.iflytek.speech.engines.processor.aitalk.recognizer.abstracts.AitalkContent;
import com.iflytek.speech.engines.processor.aitalk.recognizer.abstracts.IAitalkListener;
 
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Aitalk5 {
    private static final String APPDATA = "/data/data/";
    private static final String DESCFILES = "resource";
    private static final String DEVIDER = "/";
    public static final int MAX_RESULT_COUNT = 3;
    public static final int MSG_COMMITNETWORK = 1544;
    public static final int MSG_COPYGRMRES = 1543;
    public static final int MSG_DESTROYWAITING = 1539;
    public static final int MSG_DIALOG_CLOSE = 2305;
    public static final int MSG_END_BY_USER = 1042;
    public static final int MSG_ENGINE_STATE = 1541;
    public static final int MSG_GRMDESTROYWAITING = 1540;
    public static final int MSG_GRMNETWORKBUILT = 1542;
    public static final int MSG_HAVE_RESULT = 1280;
    public static final int MSG_RESPONSE_TIMEOUT = 1040;
    public static final int MSG_SPEECH_END = 1026;
    public static final int MSG_SPEECH_FLUSH_END = 1027;
    public static final int MSG_SPEECH_NO_DETECT = 1039;
    public static final int MSG_SPEECH_START = 1025;
    public static final int MSG_SPEECH_TIMEOUT = 1041;
    public static final int MSG_START_RECORD = 784;
    public static final int MSG_STOP_RECORD = 785;
    public static final int PARAM_AUDIODISCARD = 5;
    public static final int PARAM_DENOSIE = 11;
    public static final int PARAM_ES_INPUT = 15;
    public static final int PARAM_ES_PITCH = 16;
    public static final int PARAM_ES_SAMPLERATE = 101;
    public static final int PARAM_GS = 10;
    public static final int PARAM_RESPONSETIMEOUT = 2;
    public static final int PARAM_SENSITIVITY = 1;
    public static final int PARAM_SPEECHNOTIFY = 4;
    public static final int PARAM_SPEECHTIMEOUT = 3;
    public static final int PARAM_VAD_ENABLE = 103;
    private static final int REFUSE_CONFIDENCE = 0;
    private static final String[] RESOURCES_V2 = {"grm.mp3", "normModel.mp3", "ivCMNParam.mp3", "ivVadModels.mp3", "ivCMScore_8K.mp3", "ivDTree_8K.mp3", "ivDTree_16K.mp3", "ivFiller_8K.mp3", "ivHmmRes_8K.mp3", "ivHmmRes_16K.mp3", "ivModel_8K.mp3", "ivModel_16K.mp3", "ivTMRes_8K.mp3", "ivTMRes_16K.mp3", "sms_8K.mp3", "sms_16K.mp3", "poi_16K.mp3"};
    public static final int SAMPLERATE_16K = 16000;
    public static final int SAMPLERATE_8K = 8000;
    private static final String SERVICE_DIR = "speechservice";
    private static final int SPEECH_OUT_TIME = 20000;
    private static final String SRCFILES = "aitalk5";
    private static final String TAG = "SPEECH_Aitalk5";
    private static final int TIMEOUT_WAIT_LOCK = 40000;
    private static final int TIMEOUT_WAIT_QUEUE = 2000;
    public static final int VALUE_ES_INPUT_FEATURE = 1;
    public static final int VALUE_ES_INPUT_WAV = 0;
    public static final int VALUE_GS_OFF = 1;
    public static final int VALUE_GS_ON = 1;
    private static final int WAIT_OUT_TIME = 4000;
    private static final ReentrantLock asrRunLock = new ReentrantLock();
    private static AitalkStatus mAitalkStatus = AitalkStatus.Idle;
    private static IAitalkListener mCallback = null;
    private static String mClient;
    /* access modifiers changed from: private */
    public static int mErrorCode = 0;
    private static String mGrammarPath;
    private static boolean mIsJniLoaded;
    private static Handler mMsgHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Aitalk5.MSG_START_RECORD /*784*/:
                    Log.i(Aitalk5.TAG, "MSG_START_RECORD");
                    return;
                case Aitalk5.MSG_STOP_RECORD /*785*/:
                    Log.i(Aitalk5.TAG, "MSG_STOP_RECORD");
                    return;
                case Aitalk5.MSG_SPEECH_START /*1025*/:
                    Log.i(Aitalk5.TAG, "MSG_SPEECH_START");
                    return;
                case Aitalk5.MSG_SPEECH_END /*1026*/:
                    Log.i(Aitalk5.TAG, "MSG_SPEECH_END");
                    return;
                case Aitalk5.MSG_SPEECH_FLUSH_END /*1027*/:
                    Log.i(Aitalk5.TAG, "MSG_SPEECH_FLUSH_END");
                    return;
                case Aitalk5.MSG_SPEECH_NO_DETECT /*1039*/:
                    Log.i(Aitalk5.TAG, "MSG_SPEECH_NO_DETECT");
                    return;
                case Aitalk5.MSG_RESPONSE_TIMEOUT /*1040*/:
                    Log.i(Aitalk5.TAG, "MSG_RESPONSE_TIMEOUT");
                    Aitalk5.errorCallback(SpeechError.ERROR_RESPONSE_TIMEOUT);
                    return;
                case Aitalk5.MSG_SPEECH_TIMEOUT /*1041*/:
                    Log.i(Aitalk5.TAG, "MSG_SPEECH_TIMEOUT");
                    Aitalk5.errorCallback(SpeechError.ERROR_SPEECH_TIMEOUT);
                    return;
                case Aitalk5.MSG_END_BY_USER /*1042*/:
                    Log.i(Aitalk5.TAG, "MSG_END_BY_USER");
                    return;
                case Aitalk5.MSG_HAVE_RESULT /*1280*/:
                    Log.i(Aitalk5.TAG, "MSG_HAVE_RESULT FROM MSGHANDER");
                    Aitalk5.resultCallback();
                    return;
                case Aitalk5.MSG_ENGINE_STATE /*1541*/:
                    int state = Aitalk5.JniGetEngineState();
                    Log.i(Aitalk5.TAG, "MSG_ENGINE_STATE state=" + state);
                    if (state == 0 || 29 == state || 31 == state) {
                        Log.i(Aitalk5.TAG, "MSG_ENGINE_STATE Start OK");
                        return;
                    } else if (state == 30 || state == 32) {
                        Log.i(Aitalk5.TAG, "MSG_ENGINE_STATE Stop OK");
                        return;
                    } else {
                        Log.e(Aitalk5.TAG, "MSG_ENGINE_STATE error,will stop." + state);
                        Aitalk5.mErrorCode = state;
                        Aitalk5.stop();
                        Aitalk5.errorCallback(state);
                        return;
                    }
                case Aitalk5.MSG_COMMITNETWORK /*1544*/:
                    Log.i(Aitalk5.TAG, "MSG_COMMITNETWORK");
                    return;
                default:
                    Log.i(Aitalk5.TAG, "unkown  message: " + msg.what);
                    return;
            }
        }
    };
    private static String[] mReources = RESOURCES_V2;
    private static final List<AitalkContent> mResult = new ArrayList();
    private static int mSampleRate;
    private static String mScene;

    private enum AitalkStatus {
        Idle,
        Ready,
        Running
    }

    private static native int JniAddLexiconItem(String str, String str2, int i);

    private static native int JniAppendData(byte[] bArr, int i);

    private static native int JniBuildGrammar(byte[] bArr, int i);

    private static native int JniCreate(String str, FileDescriptor fileDescriptor, long[] jArr);

    private static native int JniCreateLexicon(String str);

    private static native int JniDeleteLexiconItem(String str, String str2);

    private static native int JniDestroy();

    private static native int JniEndData();

    private static native int JniEndLexicon(String str);

    private static native int JniExit();

    private static native int JniGetConfidence(int i);

    /* access modifiers changed from: private */
    public static native int JniGetEngineState();

    private static native int JniGetItemId(int i, int i2, int i3);

    private static native int JniGetItemNumber(int i, int i2);

    private static native String JniGetItemText(int i, int i2, int i3);

    private static native int JniGetResCount();

    private static native int JniGetSlotConfidence(int i, int i2);

    private static native String JniGetSlotName(int i, int i2);

    private static native int JniGetSlotNumber(int i);

    private static native int JniGetSlotType(int i, int i2);

    private static native int JniLoadNetWork(String str);

    private static native int JniReset(int i);

    /* access modifiers changed from: private */
    public static native int JniRunTask();

    private static native int JniSetGrammarPath(String str);

    private static native int JniSetParam(int i, int i2);

    private static native int JniStart(String str);

    private static native int JniStop();

    private static native int JniUnloadLexicon(String str);

    private static native int JniUpdateGrammar(String str);

    static {
        mIsJniLoaded = false;
        try {
            System.loadLibrary("em_aitalk5");
            mIsJniLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            Log.i(TAG, "loadLibrary failed");
        }
    }

    public static boolean isJniLoaded() {
        return mIsJniLoaded;
    }

    private static void startRunThread() {
        new Thread(new Runnable() {
            int nRet = 0;

            public void run() {
                try {
                    Log.i(Aitalk5.TAG, "AsrRunThread to start");
                    Aitalk5.mErrorCode = 0;
                    this.nRet = Aitalk5.JniRunTask();
                    if (this.nRet != 0) {
                        Log.i(Aitalk5.TAG, "AsrRunThread Start Error! nRet = " + this.nRet);
                        Aitalk5.errorCallback(SpeechError.ERROR_AITALK);
                    }
                    Log.i(Aitalk5.TAG, "AsrRunThread run ok ");
                    if (Aitalk5.mErrorCode > 0) {
                        Aitalk5.errorCallback(SpeechError.ERROR_AITALK);
                    } else {
                        Aitalk5.resultCallback();
                    }
                    Log.i(Aitalk5.TAG, "AsrRunThread run End!");
                } catch (Exception e) {
                    Log.e(Aitalk5.TAG, "AsrRunThread Exception");
                    e.printStackTrace();
                    if (Aitalk5.mErrorCode > 0) {
                        Aitalk5.errorCallback(SpeechError.ERROR_AITALK);
                    } else {
                        Aitalk5.resultCallback();
                    }
                    Log.i(Aitalk5.TAG, "AsrRunThread run End!");
                } catch (Throwable th) {
                    if (Aitalk5.mErrorCode > 0) {
                        Aitalk5.errorCallback(SpeechError.ERROR_AITALK);
                    } else {
                        Aitalk5.resultCallback();
                    }
                    Log.i(Aitalk5.TAG, "AsrRunThread run End!");
                    throw th;
                }
            }
        }).start();
    }

    public static boolean creatAitalk(Context context) throws IOException {
        if (!isJniLoaded()) {
            return false;
        }
        Log.i(TAG, "creatAitalk");
        int ret = 0;
        mGrammarPath = APPDATA + context.getPackageName() + DEVIDER;
        long[] offsets = new long[mReources.length];
        int i = 0;
        while (i < mReources.length) {
            try {
                offsets[i] = getResourceOffset(context, "aitalk5/" + mReources[i]);
                Log.i(TAG, "creatAitalk init res(" + i + "):" + offsets[i]);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ret = JniCreate(mGrammarPath, context.getResources().getAssets().openFd("aitalk5/" + mReources[0]).getFileDescriptor(), offsets);
        Log.i(TAG, "ASR Create = " + ret);
        if (ret == 0) {
            setThreshold(0);
            setResponseTimeout(4000);
            setSpeechTimeout(SPEECH_OUT_TIME);
            Log.i(TAG, "ASR set vadRet = " + setVAD(0));
            startRunThread();
        }
        if (ret == 0) {
            return true;
        }
        return false;
    }

    private static long getResourceOffset(Context ctx, String resName) {
        try {
            return ctx.getResources().getAssets().openFd(resName).getStartOffset();
        } catch (IOException e) {
            Log.e(TAG, "getResourceOffset:" + e.toString());
            return 0;
        }
    }

    public static void destory() {
        if (isJniLoaded()) {
            stop();
            exit();
            Log.i(TAG, " ASR engine destoryed! ret = " + JniDestroy());
        }
    }

    public static int exit() {
        if (!isJniLoaded()) {
            return 0;
        }
        return JniExit();
    }

    public static void stop() {
        if (isJniLoaded()) {
            boolean locked = false;
            Log.i(TAG, " ASR stop with locked = " + asrRunLock.isLocked());
            try {
                locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (locked) {
                if (mAitalkStatus == AitalkStatus.Running) {
                    Log.i(TAG, " ASR stop begin!");
                    JniStop();
                    mAitalkStatus = AitalkStatus.Idle;
                    Log.i(TAG, " ASR stop end!");
                }
                asrRunLock.unlock();
            }
        }
    }

    public static boolean isRuning() {
        return asrRunLock.isLocked();
    }

    public static int start(IAitalkListener listener) {
        if (!isJniLoaded()) {
            return 0;
        }
        int ret = -1;
        boolean locked = false;
        try {
            locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!locked) {
            return -1;
        }
        if (mAitalkStatus == AitalkStatus.Idle) {
            mAitalkStatus = AitalkStatus.Running;
            mCallback = listener;
            mResult.clear();
            Log.i(TAG, "jnistart Scene = " + mScene);
            ret = JniStart(mScene);
        }
        asrRunLock.unlock();
        return ret;
    }

    public static void setScene(String sceneName) {
        if (sceneName == null || sceneName.length() == 0) {
            sceneName = "NONE";
        }
        mScene = sceneName;
    }

    public static int onCallMessage(int msgType) {
        Log.i(TAG, "onCallMessage");
        mMsgHandler.sendMessageDelayed(mMsgHandler.obtainMessage(msgType), 0);
        return 0;
    }

    public static int onCallResult() {
        mResult.clear();
        int iResCount = JniGetResCount();
        Log.i(TAG, "onCallResult: result count: " + iResCount);
        for (int iRes = 0; iRes < iResCount; iRes++) {
            int iSlotCount = JniGetSlotNumber(iRes);
            int confidence = JniGetConfidence(iRes);
            Log.i(TAG, "onCallResult res:" + (iRes + 1) + " sentenceId:" + 0 + "  confidence:" + confidence + " SlotCount:" + iSlotCount);
            AitalkContent rs = new AitalkContent(0, confidence, iSlotCount);
            for (int iSlot = 0; iSlot < iSlotCount; iSlot++) {
                int resultCount = Math.min(JniGetItemNumber(iRes, iSlot), 3);
                if (resultCount <= 0) {
                    Log.e(TAG, "Error iItemCount < 0");
                } else {
                    int[] itemIds = new int[resultCount];
                    String[] itemTexts = new String[resultCount];
                    LinkedList<String> itemTextList = new LinkedList<>();
                    String slotName = JniGetSlotName(iRes, iSlot);
                    int slotType = JniGetSlotType(iRes, iSlot);
                    int slotConfidence = JniGetSlotConfidence(iRes, iSlot);
                    Log.i(TAG, "onCallResult slot:" + (iSlot + 1) + " iItemCount:" + resultCount + " slotConfidence:" + slotConfidence + " slotName:" + slotName + " slotType" + slotType);
                    for (int iItem = 0; iItem < resultCount; iItem++) {
                        itemIds[iItem] = JniGetItemId(iRes, iSlot, iItem);
                        itemTexts[iItem] = JniGetItemText(iRes, iSlot, iItem);
                        itemTextList.add(JniGetItemText(iRes, iSlot, iItem));
                        if (itemTexts[iItem] == null) {
                            itemTexts[iItem] = "";
                        }
                        Log.i(TAG, "onCallResult slot item:" + (iItem + 1) + " itemTexts:" + itemTexts[iItem] + " itemIds " + itemIds[iItem]);
                    }
                    rs.AddSlot(slotName, slotType, resultCount, itemIds, itemTexts, itemTextList, slotConfidence);
                }
            }
            mResult.add(rs);
        }
        mMsgHandler.sendMessageDelayed(mMsgHandler.obtainMessage(MSG_HAVE_RESULT), 0);
        Log.i(TAG, "MSG_HAVE_RESULT");
        return 0;
    }

    public static void readyCallback() {
        Log.i(TAG, "grammar is ok");
        mAitalkStatus = AitalkStatus.Ready;
        if (mCallback == null) {
            Log.e(TAG, "IRecognitionListener cb is null");
        }
    }

    public static void errorCallback(int errorId) {
        if (mCallback == null) {
            Log.e(TAG, "IRecognitionListener cb is null");
            return;
        }
        try {
            mCallback.onError(errorId);
        } catch (Exception e) {
        }
        mCallback = null;
        Log.e(TAG, "IRecognitionListener : hava error");
    }

    public static void resultCallback() {
        if (mCallback == null) {
            Log.e(TAG, "IRecognitionListener cb is null");
            return;
        }
        try {
            mCallback.onResults(mResult);
        } catch (Exception e) {
        }
        mCallback = null;
        Log.e(TAG, "IRecognitionListener : have result");
    }

    public static int appendData(byte[] buff, int length) {
        if (isJniLoaded() && mAitalkStatus == AitalkStatus.Running) {
            return JniAppendData(buff, length);
        }
        return 0;
    }

    public static int endData() {
        if (!isJniLoaded()) {
            return 0;
        }
        int ret = JniEndData();
        Log.i(TAG, "JniEndData ret = " + ret);
        return ret;
    }

    public static int addLexiconItem(String lexiconName, String[] words, int baseId, boolean isDeleteOld) {
        if (!isJniLoaded()) {
            return 0;
        }
        if (lexiconName == null) {
            Log.e(TAG, "addLexiconItem lexiconName null.");
            return 0;
        } else if (words == null || words.length == 0) {
            Log.e(TAG, "addLexiconItem words size 0.");
            return 0;
        } else {
            int ret_count = 0;
            boolean locked = false;
            try {
                locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!locked) {
                return 0;
            }
            if (isDeleteOld) {
                Log.i(TAG, "addLexiconItem __1.1__ JniUnloadLexicon old ret=" + JniUnloadLexicon(lexiconName));
                String file = String.valueOf(getGrammarPath()) + lexiconName.replace("<", "").replace(">", "") + "_";
                Log.i(TAG, "addLexiconItem __1.2__DELETE  " + file + " ret=" + new File(file).delete());
                Log.i(TAG, "addLexiconItem __1.3_ JniCreateLexicon new ret=" + JniCreateLexicon(String.valueOf(lexiconName) + 0));
            }
      
            Log.i(TAG, "start Addlexicon  Name = " + lexiconName);
            for (String word : words) {
                int ret = JniAddLexiconItem(lexiconName, word, baseId);
                Log.i(TAG, "AddlexiconItem " + word + ",ret :" + ret);
                if (ret == 0) {
                    ret_count++;
                } else {
                    Log.i(TAG, "addLexiconItem JniAddLexiconItem ERROR ret=" + ret);
                }
                baseId++;
            }
            int ret2 = JniEndLexicon(lexiconName);
            Log.i(TAG, "end Addlexicon  Name = " + lexiconName);
            Log.i(TAG, "addLexiconItem __1.4 JniEndLexicon ret=" + ret2);
            asrRunLock.unlock();
            return ret_count;
        }
    }

    public static int deleteLexiconItem(String lexiconName, String[] words) {
        if (!isJniLoaded()) {
            return 0;
        }
        int ret_count = 0;
        if (words == null || words.length == 0) {
            return 0;
        }
        boolean locked = false;
        try {
            locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!locked) {
            return 0;
        }
        int length = words.length;
        for (int i = 0; i < length; i++) {
            if (JniDeleteLexiconItem(String.valueOf(lexiconName) + 0, String.valueOf(words[i]) + 0) == 0) {
                ret_count++;
            }
        }
        int ret = JniEndLexicon(String.valueOf(lexiconName) + 0);
        asrRunLock.unlock();
        return ret_count;
    }

    public static String getGrammarPath() {
        return String.valueOf(mGrammarPath) + mClient;
    }

    public static int setGrammarPath(String name) {
        int ret = -1;
        boolean locked = false;
        try {
            locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (locked) {
            mClient = name;
            if (mClient == null || mClient.length() == 0) {
                mClient = SERVICE_DIR;
            }
            if (isJniLoaded()) {
                ret = JniSetGrammarPath(mClient);
            }
            Log.i(TAG, "JniSetGrammarPath to " + mClient + " ,ret=" + ret);
            asrRunLock.unlock();
        }
        return ret;
    }

    public static int updateGrammar(String name) {
        int ret = -1;
        if (!isJniLoaded()) {
            return -1;
        }
        boolean locked = false;
        try {
            locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (locked) {
            ret = JniUpdateGrammar(name);
            Log.i(TAG, "JniUpdateGrammar name =" + name + ", ret = " + ret);
            asrRunLock.unlock();
        }
        return ret;
    }

    public static int buildGrammar(byte[] xmlText, int length) {
        int ret = -1;
        if (!isJniLoaded()) {
            return -1;
        }
        boolean locked = false;
        try {
            locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (locked) {
            ret = JniBuildGrammar(xmlText, length);
            asrRunLock.unlock();
        }
        Log.e(TAG, "JniBuildGrammar ret = " + ret);
        return ret;
    }

    public static int loadGrammar(String client) {
        return 0;
    }

    public static int setSampleRate(int samplerate) {
        int ret = -1;
        boolean locked = false;
        try {
            locked = asrRunLock.tryLock(40000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (locked) {
            mSampleRate = samplerate;
            if (mSampleRate == 16000 || mSampleRate == 8000) {
                Log.e(TAG, "set SampleRate to " + mSampleRate);
                if (isJniLoaded()) {
                    ret = JniSetParam(101, mSampleRate);
                }
                Log.e(TAG, "set SampleRate ret is " + ret);
                asrRunLock.unlock();
            } else {
                Log.i(TAG, "unsupported samplerate " + mSampleRate);
                asrRunLock.unlock();
                return -1;
            }
        }
        return ret;
    }

    public static int switchMode(boolean ret) {
        if (isJniLoaded()) {
            Log.e(TAG, "switcgMode JniStop ret = " + JniStop());
            Log.e(TAG, "switcgMode JniDestroy ret = " + JniDestroy());
            if (ret) {
                JniReset(8000);
            } else {
                JniReset(16000);
            }
        }
        return 0;
    }

    public static List<AitalkContent> getRecognitionResults(long key) {
        return mResult;
    }

    public static int setThreshold(int threshold) {
        if (!isJniLoaded()) {
            return 0;
        }
        return JniSetParam(1, threshold);
    }

    public static int setResponseTimeout(int time) {
        if (!isJniLoaded()) {
            return 0;
        }
        return JniSetParam(2, time);
    }

    public static int setSpeechTimeout(int time) {
        if (!isJniLoaded()) {
            return 0;
        }
        return JniSetParam(3, time);
    }

    public static int setVAD(int value) {
        if (!isJniLoaded()) {
            return 0;
        }
        return JniSetParam(PARAM_VAD_ENABLE, value);
    }

    public static int setDeNoiseEnable(boolean enable) {
        if (!isJniLoaded()) {
            return 0;
        }
        if (enable) {
            return JniSetParam(11, 1);
        }
        return JniSetParam(11, 0);
    }

    public static int setInputVav() {
        if (!isJniLoaded()) {
            return 0;
        }
        return JniSetParam(15, 0);
    }

    public static int setInputFeature() {
        if (!isJniLoaded()) {
            return 0;
        }
        return JniSetParam(15, 1);
    }

    public static int setPitchEnable(boolean isEnable) {
        if (!isJniLoaded()) {
            return 0;
        }
        if (isEnable) {
            return JniSetParam(16, 1);
        }
        return JniSetParam(16, 0);
    }
}
