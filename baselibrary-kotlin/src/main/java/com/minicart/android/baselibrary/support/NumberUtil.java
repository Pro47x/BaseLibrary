package com.minicart.android.baselibrary.support;

import java.util.Locale;

/**
 * Created by 54506 on 2016/5/24.
 */
public class NumberUtil {

    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("\\d+");
    }

    /**
     * 精确到小数点后面两位
     *
     * @param price
     * @return
     */
    public static String precise2(double price) {
        return String.format(Locale.CHINA, "%.2f", price);
    }

}
