package com.springextended.core.task;

import com.springextended.core.task.impl.MethodCall;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 延迟任务存储
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 10:22
 */
public interface DelayTaskStorage {

    /**
     * 添加方法调用
     * @param methodCall
     * @param delayTo
     */
    void add(MethodCall methodCall, LocalDateTime delayTo);

    /**
     * 获取到期的方法调用列表
     * @param now
     * @param topCount
     * @return
     */
    List<MethodCall> get(LocalDateTime now,int topCount);

    /**
     * 删除方法调用
     * @param methodCall
     */
    void delete(MethodCall methodCall);
}
