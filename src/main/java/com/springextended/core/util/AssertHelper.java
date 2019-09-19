package com.springextended.core.util;


/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 04 - 18 17:06
 */

public class AssertHelper {
    public static void throwIfNull(Object object, String message){
        if(object==null){
            throw new RuntimeException(message);
        }
    }
}
