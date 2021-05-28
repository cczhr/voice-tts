package com.iflytek.speechcloud.binder.impl;

import android.content.Context;
import android.os.Environment;

import com.iflytek.msc.util.FileUtil;

import java.util.HashMap;

public class AiConstants {
    public static final String DEFAULT_VOICE_PATH = "aisound";
    public static final String DEFAULT_VOICE_RES = "aisound/common.mp3";
    public static String LINK_PARAM_GET_LIST = "type=json&version=1.0";
    public static String LINK_TTS_LIST_URL = "http://appserver.voicecloud.cn/iflytekWS/Resource/getTTSList?";
    public static String LISTEN_RESOURCE_URL = "http://appserver.voicecloud.cn/iflytekWS/Resource/getListenResource?";
    public static String RES_IS_IN_ASSERT = "resource_position";
    public static final String SDCARD_PATH = "sdcard";

    public static final String TTS_RESOURCE_AUFFIX_IRF = ".irf";
    public static final String TTS_RESOURCE_AUFFIX_MP3 = ".mp3";
    public static final String TTS_RESOURCE_AUFFIX_PCM = ".pcm";
    public static final String TTS_RESOURCE_PATH = "/Speechcloud";




    public static String getPath(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return String.valueOf(Environment.getExternalStorageDirectory().toString()) + TTS_RESOURCE_PATH + "/";
        }
        return FileUtil.getUserPath(context);
    }
}
