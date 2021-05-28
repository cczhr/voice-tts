package com.iflytek.speechcloud.tts.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.business.speech.SpeechIntent;
import com.iflytek.speechcloud.binder.impl.LocalTtsPlayer;
import com.iflytek.speechcloud.binder.impl.AiConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class TtsResource {
    private final String FILE_MODE = "r";
    private final String PROVIDER_URI_PREFIX = "content://";
    private InputStream mAssetsInputStream;
    private Context mContext;
    private RandomAccessFile mFileInputStream;
    private boolean mIsInAssert = false;
    private LocalTtsPlayer.ResMode mMode = LocalTtsPlayer.ResMode.STANDARD;
    private String mProviderName;
    private String mResFile;
    private int mResType = 257;
    private Uri mUri = null;

    public TtsResource(Context context, LocalTtsPlayer.ResMode rm, Intent resIntent) {
        this.mContext = context;
        this.mMode = rm;
        if (rm == LocalTtsPlayer.ResMode.STANDARD) {
            this.mResFile = resIntent.getStringExtra(LocalTtsPlayer.STANDERD_VOICE_NAME);
            Log.e("地址1",this.mResFile );
            if (this.mResFile.startsWith(AiConstants.DEFAULT_VOICE_PATH)) {
                this.mIsInAssert = true;
            } else {
                this.mIsInAssert = resIntent.getBooleanExtra(AiConstants.RES_IS_IN_ASSERT, false);
            }
        } else {
            if (resIntent != null) {
                this.mResType = resIntent.getIntExtra("engine_res_type", 257);
                this.mResFile = resIntent.getStringExtra(SpeechIntent.ARG_RES_FILE);
                this.mProviderName = resIntent.getStringExtra("content_provider_name");
            }
            if (resIntent == null || TextUtils.isEmpty(this.mResFile)) {
                this.mResType = 257;
                this.mResFile = AiConstants.DEFAULT_VOICE_RES;
                this.mProviderName = null;
            }
        }
    }

    public byte[] readResource(int offset, int length) {
        closeUnusedStream();
        if (this.mMode != LocalTtsPlayer.ResMode.STANDARD) {
            switch (this.mResType) {
                case 257:
                    return readResFromAssets(this.mResFile, offset, length);
                case 258:
                    return readResFromPath(this.mResFile, offset, length);
                case 259:
                    return readResFromClient(this.mResFile, offset, length, this.mProviderName);
                default:
                    return null;
            }
        } else if (AiConstants.DEFAULT_VOICE_RES.equals(this.mResFile)) {
            return readResFromAssets(this.mResFile, offset, length);
        } else {
            if (this.mIsInAssert) {
                return readResFromAssets(this.mResFile, offset, length);
            }
            return readResFromPath(this.mResFile, offset, length);
        }
    }

    private byte[] readResFromAssets(String file, int offset, int length) {
        int len = length;
        try {
            if (this.mAssetsInputStream == null) {
                this.mAssetsInputStream = this.mContext.getResources().getAssets().open(file);
            }
            this.mAssetsInputStream.reset();
            if (length == -1) {
                len = this.mAssetsInputStream.available();
            } else {
                this.mAssetsInputStream.skip((long) offset);
            }
            byte[] buff = new byte[len];
            this.mAssetsInputStream.read(buff, 0, len);
            return buff;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] readResFromPath(String file, int offset, int length) {
        int len = length;
        try {
            if (this.mFileInputStream == null) {
                this.mFileInputStream = new RandomAccessFile(file, "r");
            }
            if (length == -1) {
                len = (int) this.mFileInputStream.length();
            } else {
                this.mFileInputStream.seek((long) offset);
            }
            byte[] buff = new byte[len];
            this.mFileInputStream.read(buff, 0, len);
            return buff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] readResFromClient(String file, int offset, int length, String providerName) {
        if (TextUtils.isEmpty(providerName)) {
            return null;
        }
        int len = length;
        try {
            if (this.mUri == null) {
                this.mUri = Uri.parse("content://" + providerName + File.separator + file);
            }
            InputStream is = this.mContext.getContentResolver().openInputStream(this.mUri);
            if (length == -1) {
                len = is.available();
            } else {
                is.skip((long) offset);
            }
            byte[] buff = new byte[len];
            is.read(buff, 0, len);
            return buff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void closeUnusedStream() {
        try {
            switch (this.mResType) {
                case 257:
                    if (this.mFileInputStream != null) {
                        this.mFileInputStream.close();
                        this.mFileInputStream = null;
                        return;
                    }
                    return;
                case 258:
                    if (this.mAssetsInputStream != null) {
                        this.mAssetsInputStream.close();
                        this.mAssetsInputStream = null;
                        return;
                    }
                    return;
                case 259:
                    if (this.mAssetsInputStream != null) {
                        this.mAssetsInputStream.close();
                        this.mAssetsInputStream = null;
                    }
                    if (this.mFileInputStream != null) {
                        this.mFileInputStream.close();
                        this.mFileInputStream = null;
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        try {
            if (this.mAssetsInputStream != null) {
                this.mAssetsInputStream.close();
                this.mAssetsInputStream = null;
            }
            if (this.mFileInputStream != null) {
                this.mFileInputStream.close();
                this.mFileInputStream = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
