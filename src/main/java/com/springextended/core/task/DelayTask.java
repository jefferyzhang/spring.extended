package com.springextended.core.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * <p>
 * 延迟任务
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 16 15:33
 */
public interface DelayTask {

   /**
    * 安排任务延迟多久后执行
    * @param clazz
    * @param methodCall
    * @param delay
    * @param <T>
    */
   <T> void schedule(Class<T> clazz, Consumer<T> methodCall, Duration delay);

   /**
    * 安排任务延迟到指定时间执行
    * @param clazz
    * @param methodCall
    * @param delayTo
    * @param <T>
    */
   <T> void schedule(Class<T> clazz, Consumer<T> methodCall, LocalDateTime delayTo);
}
