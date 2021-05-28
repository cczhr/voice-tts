package com.iflytek.msc.util;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    private static String userPath = "";

    public static String getUserPath(Context context) {
        if (!TextUtils.isEmpty(userPath)) {
            return userPath;
        }
        String path = context.getFilesDir().getAbsolutePath();
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        String path2 = path + "msclib/";
        File pf = new File(path2);
        if (!pf.exists()) {
            pf.mkdirs();
        }
        userPath = path2;
        return userPath;
    }

    public static void deleteFile(String file) {
        File f = new File(file);
        if (f.exists()) {
            f.delete();
        }
    }


}
