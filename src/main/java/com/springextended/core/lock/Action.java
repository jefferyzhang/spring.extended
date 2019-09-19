package com.springextended.core.lock;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 14:57
 */
@FunctionalInterface
public interface Action {
    /**
     * 执行
     */
    void perform();
}
