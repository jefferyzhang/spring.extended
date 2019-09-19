package com.springextended.core.util;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 04 - 24 21:02
 */
public class IntegerUtil {
    /**
     * 向上整除  比如 5向上整除6= 2
     * @param value
     * @param divisor
     * @return
     */
    public static Integer ceilingDivide(Integer value,Integer divisor){
        return (value+ divisor-1)/divisor;
    }
}
