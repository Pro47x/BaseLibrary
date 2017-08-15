package com.minicart.android.baselibrary.support;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @类名：NetUtil
 * @描述：
 * @创建人：54506
 * @创建时间：2016/8/20 17:13
 * @版本：
 */
public class NetUtil {

    public static String getIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(ip & 0xFF)).append(".");
        sb.append(String.valueOf((ip & 0xFFFF) >>> 8)).append(".");
        sb.append(String.valueOf((ip & 0xFFFFFF) >>> 16)).append(".");
        sb.append(String.valueOf(ip >>> 24));
        return sb.toString();
    }
}
