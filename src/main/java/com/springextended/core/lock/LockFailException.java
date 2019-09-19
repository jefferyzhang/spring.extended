package com.springextended.core.lock;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 14:42
 */
public class LockFailException extends RuntimeException {
    public LockFailException(){
        super("请求的资源处于繁忙中，请稍后重试。");
    }
}
