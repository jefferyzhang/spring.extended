package com.springextended.core.lock;


import java.time.Duration;
import java.util.function.Consumer;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 13:41
 */
public interface LockProvider {

    /**
     * 锁住资源，并在操作执行完或者过期时间后释放锁
     * @param resourceIdentifier
     * @param duration
     * @param action
     * @param throwIfLockFail
     */
    void lock(String resourceIdentifier, Duration duration, Action action,boolean throwIfLockFail);

    /**
     * 锁住资源，并在操作执行完或者过期时间后释放锁
     * @param resourceIdentifier
     * @param duration
     * @param action
     * @param throwIfLockFail
     */
    void lock(String resourceIdentifier, Duration duration, Consumer<LockTimer> action,boolean throwIfLockFail);
}
