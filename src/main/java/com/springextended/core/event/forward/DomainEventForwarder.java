package com.springextended.core.event.forward;

import java.time.LocalDateTime;

/**
 * <p>
 * 领域事件转发器
 *    将 排队在 领域事件转发队列 里面的事件进行转发
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 15 17:33
 */
public interface DomainEventForwarder {

    /**
     * 处理转发
     * @param now
     * @param batchSize
     */
    void process(LocalDateTime now, int batchSize);

    /**
     * 处理转发结果
     * @param domainEventForward
     * @param now
     * @param forwardResult
     */
    void handleDomainEventForwardResult(DomainEventForward domainEventForward, LocalDateTime now, boolean forwardResult);
}
