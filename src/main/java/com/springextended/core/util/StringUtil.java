package com.springextended.core.util;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 05 19:14
 */
public class StringUtil {
    /**
     * 左对齐
     * @param src
     * @param len
     * @param ch
     * @return
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] padCharArray = new char[len];
        System.arraycopy(src.toCharArray(), 0, padCharArray, diff, src.length());
        for (int i = 0; i < diff; i++) {
            padCharArray[i] = ch;
        }
        return new String(padCharArray);
    }
}
