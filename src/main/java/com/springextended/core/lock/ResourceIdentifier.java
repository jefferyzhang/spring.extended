package com.springextended.core.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ResourceIdentifier {
    /**
     * Key前缀
     * 尽量每个业务有自己的前缀，以避免 分布式锁的key冲突；也有利于通过key识别业务
     * @return
     */
    String keyPrefix() default "NotSpecify";
}
