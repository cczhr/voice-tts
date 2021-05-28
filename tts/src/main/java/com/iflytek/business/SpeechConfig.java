package com.iflytek.business;

import android.content.Context;
import android.content.SharedPreferences;

public class SpeechConfig {
    public static final boolean AUTO_UPDATE = true;
    public static String BaseURL = "http://service.voicecloud.cn/speech/get_version.php";
    public static final long HALF_DAY_MIL = 43200000;
    public static final String KEY_AUTOMATIC_SEND = "automatic_send_preference";
    public static final String KEY_AUTOMATIC_UPDATE = "automatic_update_preference";
    public static final String KEY_FEEDBACK_AGE = "feedback_age";
    public static final String KEY_FEEDBACK_GRADE = "feedback_grade";
    public static final String KEY_FEEDBACK_SEX = "feedback_sex";
    public static final String KEY_FEEDBACK_UID = "feedback_uid";
    public static final String KEY_LAST_CONTACT_CHECK_TIME = "last_contact_check_time";
    public static final String KEY_LAST_LOG_CHECK_TIME = "last_log_check_time";
    public static final String KEY_LAST_VERSION_CHECK_TIME = "last_version_check_time";
    public static final String KEY_LAST_WHOISUSING_LOG_CHECK_TIME = "last_whoisusing_log_check_time";
    public static final String KEY_MULTIPLE_CANDIDATE = "multiple_candidate_preference";
    public static final String KEY_NEED_UPDATE_VERSION_CODE = "need_update_version_code";
    public static final String KEY_SELECT_LANG = "speech_select_preference";
    public static final String KEY_SOUND_SET = "sound_set_preference";
    public static final String KEY_SPEAKER_SETTING = "speaker_setting";
    public static final String KEY_UPDATE_CONTACT = "update_contact_preference";
    public static final long ONE_DAY_MIL = 86400000;
    public static final String PACKAGE_NAME = "com.iflytek.speechcloud";
    public static final String PREFER_NAME = "com.iflytek.speechconfig";
    private static SpeechConfig mInstance = null;
    private SharedPreferences mPreferences = null;

    public static SpeechConfig getConfig(Context context) {
        if (mInstance == null) {
            mInstance = new SpeechConfig(context);
        }
        return mInstance;
    }

    private SpeechConfig(Context context) {
        this.mPreferences = context.getSharedPreferences(PREFER_NAME, 0);
    }

    public static long getLong(Context context, String key, long defValue) {
        return getConfig(context).getLong(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        getConfig(context).putLong(key, value);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getConfig(context).getBoolean(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getConfig(context).putBoolean(key, value);
    }

    public static String getString(Context context, String key, String defValue) {
        return getConfig(context).getString(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        getConfig(context).putString(key, value);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor localEditor = this.mPreferences.edit();
        localEditor.putString(key, value);
        localEditor.commit();
    }

    public String getString(String key, String defValue) {
        return this.mPreferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor localEditor = this.mPreferences.edit();
        localEditor.putBoolean(key, value);
        localEditor.commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return this.mPreferences.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor localEditor = this.mPreferences.edit();
        localEditor.putLong(key, value);
        localEditor.commit();
    }

    public long getLong(String key, long defValue) {
        return this.mPreferences.getLong(key, defValue);
    }

    public String getSelectEngine() {
        if ("0".equals(getString(KEY_SELECT_LANG, "0"))) {
            return "sms";
        }
        if ("1".equals(getString(KEY_SELECT_LANG, "0"))) {
            return "cantonese";
        }
        if ("2".equals(getString(KEY_SELECT_LANG, "0"))) {
            return "sms-en";
        }
        return "sms";
    }
}
