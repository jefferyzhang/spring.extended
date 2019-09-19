package com.springextended.core.event.forward;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <p>
 * 领域事件源
 *   用于通过消息队列对外部系统发布领域事件
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 19 13:53
 */
public interface DomainEventSource {

    String OUTPUT = "publish_domain_event";

    /**
     * 发布事件通道
     * @return
     */
    @Output(OUTPUT)
    MessageChannel channel();
}
