package com.minicart.android.baselibrary.support;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by 54506 on 2016/5/5.
 * Toast统一管理类
 */
public class ToastUtil {
    private static Toast toast;
    public static boolean isShow = true;

    private ToastUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(final Context context, CharSequence message, final int duration) {
        if (isShow) {
            if (Looper.getMainLooper().getThread().equals(Thread.currentThread())) {
                toast = Toast.makeText(context, message, duration);
                toast.show();
            } else {
            }
        }
    }


    public static void showError(Context context) {
        showShort(context, "网络访问失败，请重试");
    }

}
