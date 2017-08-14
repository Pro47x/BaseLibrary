package com.minicart.android.baselibrary.net.http.secret;

public interface ISecret {

    /**
     * 加密字符串
     *
     * @param str
     * @return
     */
    String encryptString(String str);

    /**
     * 解密字符串
     *
     * @param str
     * @return
     */
    String decryptString(String str);
}
