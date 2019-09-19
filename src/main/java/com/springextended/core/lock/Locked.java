package com.springextended.core.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明式分布式锁
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Locked {
    /**
     * 资源锁定时间，单位 秒
     * @return
     */
    int duration() default 10;

    /**
     * 锁定失败是否抛出异常
     * @return
     */
    boolean throwIfLockFail() default false;
}
