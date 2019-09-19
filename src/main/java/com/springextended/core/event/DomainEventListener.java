package com.springextended.core.event;

/**
 * <p>
 * 领域事件监听器
 *    用于本地事件监听
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 05 11:57
 */
@FunctionalInterface
public interface DomainEventListener{

    /**
     * 发生一个事件
     * @param domainEvent
     */
    void onEvent(DomainEvent domainEvent);
}
