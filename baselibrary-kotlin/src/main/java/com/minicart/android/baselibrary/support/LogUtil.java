package com.minicart.android.baselibrary.support;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Log统一管理类
 */
public class LogUtil {

    private static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static String TAG = "LogUtil";

    private LogUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void json(String json) {
        json(null, json);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug) {
            Logger.i(noNull(tag), noNull(msg));
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(noNull(tag), noNull(msg));
//            FileUtil.writeLog(tag,msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(noNull(tag), noNull(msg));
        }

    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(noNull(tag), noNull(msg));
        }
    }

    public static void json(String tag, String json) {
        if (isDebug) {
            Logger.init().hideThreadInfo().methodCount(0);
            Logger.json(json);
        }
    }

    public static void simple(String tag, String msg) {
        if (isDebug) {
            Log.d(noNull(tag), noNull(msg));
//            FileUtil.writeLog(tag,msg);
        }
    }

//    private static Settings init(String tag) {
//        return Logger.init(tag).methodOffset(1).methodCount(2);
//    }

    private static String noNull(String content) {
        return content == null ? "" : content;
    }


}