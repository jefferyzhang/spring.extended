package com.springextended.core.util;

/**
 * <p>
 *  位运算工具
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 04 - 26 13:45
 */
public class BitwiseUtil {
    /**
     * 添加flag
     * @param value
     * @param flag
     * @return
     */
    public static int and(int value,int flag){
        return value | flag;
    }

    /**
     * 去掉flag
     * @param value
     * @param flag
     * @return
     */
    public static int remove(int value,int flag){
        return value & (~flag);
    }

    /**
     * 是否包含flag
     * @param value
     * @param flag
     * @return
     */
    public static boolean has(int value,int flag){
        return (value & flag) == flag;
    }
}
