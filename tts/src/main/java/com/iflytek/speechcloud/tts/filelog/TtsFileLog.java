package com.iflytek.speechcloud.tts.filelog;

import android.util.Log;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TtsFileLog {
    public static final short FORMAT_ALAW = 6;
    public static final short FORMAT_PCM = 1;
    public static final short FORMAT_ULAW = 7;
    private static final String TAG = "SPEECH_TtsFileLog";
    private static short mBitsPerSample = 16;
    private static boolean mIsTtsLogOpen = false;
    private static short mNumChannels = 1;
    private static int mSampleRate = 16000;
    private static int mTtsPcmBytes = 0;
    private static RandomAccessFile mTtsPcmFile = null;
    private static int nHeadLen = 44;

    public static void setTtsLogOpen(boolean flag) {
        mIsTtsLogOpen = flag;
    }

    public static void ttsOpenFile(int sampleRate, String file) {
        String logFile;
        if (mIsTtsLogOpen) {
            mTtsPcmBytes = 0;
            mSampleRate = sampleRate;
            if (file == null) {
                logFile = "/sdcard/tts" + getDateTime() + ".wav";
            } else {
                logFile = "/sdcard/" + file + ".wav";
            }
            Log.i(TAG, " tts file = " + logFile);
            try {
                File tmpFile = new File(logFile);
                if (tmpFile.isFile() && tmpFile.exists()) {
                    tmpFile.delete();
                }
                mTtsPcmFile = new RandomAccessFile(logFile, "rw");
                mTtsPcmFile.write(new byte[nHeadLen], 0, nHeadLen);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.toString());
            } catch (IOException e2) {
                Log.e(TAG, e2.toString());
            }
        }
    }

    public static void writeTtsPcmData(byte[] data) {
        if (mIsTtsLogOpen) {
            if (mTtsPcmFile == null || data == null) {
                Log.e(TAG, " mTtsPcmFile file is null");
                return;
            }
            int len = data.length;
            try {
                mTtsPcmFile.write(data, 0, len);
                mTtsPcmBytes += len;
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public static void ttsCloseFile() {
        if (mIsTtsLogOpen && mTtsPcmFile != null) {
            try {
                mTtsPcmFile.seek(0);
                writeHeader(mTtsPcmFile, mTtsPcmBytes);
                mTtsPcmFile.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            } finally {
                mTtsPcmFile = null;
            }
        }
    }

    private static String getDateTime() {
        return new SimpleDateFormat("_yyyyMMdd_HHmmss").format(new Date());
    }

    private static void writeHeader(RandomAccessFile out, int len) throws IOException {
        writeId(out, "RIFF");
        writeInt(out, len + 36);
        writeId(out, "WAVE");
        writeId(out, "fmt ");
        writeInt(out, 16);
        writeShort(out, (short) 1);
        writeShort(out, mNumChannels);
        writeInt(out, mSampleRate);
        writeInt(out, ((mNumChannels * mSampleRate) * mBitsPerSample) / 8);
        writeShort(out, (short) ((mNumChannels * mBitsPerSample) / 8));
        writeShort(out, mBitsPerSample);
        writeId(out, "data");
        writeInt(out, len);
    }

    private static void writeId(RandomAccessFile out, String id) throws IOException {
        for (int i = 0; i < id.length(); i++) {
            out.write(id.charAt(i));
        }
    }

    private static void writeInt(RandomAccessFile out, int val) throws IOException {
        out.write(val >> 0);
        out.write(val >> 8);
        out.write(val >> 16);
        out.write(val >> 24);
    }

    private static void writeShort(RandomAccessFile out, short val) throws IOException {
        out.write(val >> 0);
        out.write(val >> 8);
    }
}
