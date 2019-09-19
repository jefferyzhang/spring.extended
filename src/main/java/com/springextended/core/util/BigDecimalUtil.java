package com.springextended.core.util;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 12 13:46
 */
public class BigDecimalUtil {
    public static BigDecimal securityAdd(BigDecimal first,BigDecimal second){
        if(first==null){
            if(second==null){
                return null;
            }else{
                return second;
            }
        } else {
            if (second == null) {
                return first;
            } else {
                return first.add(second);
            }
        }
    }

    /**
     * 是否存在非零值
     * @param value
     * @return
     */
    public static boolean hasNonZeroValue(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) != 0;
    }
}
