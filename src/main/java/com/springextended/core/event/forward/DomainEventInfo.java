package com.springextended.core.event.forward;

import com.springextended.core.event.DomainEvent;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 领域事件信息
 *    转发到外部系统的事件体
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 19 15:26
 */
@Data
@Accessors(chain = true)
public class DomainEventInfo{
    /**
     * 事件Id
     */
    private String id;
    /**
     * 房间Id
     */
    private String roomId;
    /**
     * 事件类型
     */
    private Integer eventType;
    /**
     * 订单Id
     */
    private String orderId;
    /**
     * 事件发生时间
     */
    private LocalDateTime occurredOn;

    /**
     * 参数
     */
    private String args;

    public static DomainEventInfo of(DomainEvent domainEvent){
        return new DomainEventInfo()
                .setId(domainEvent.getId())
                .setRoomId(domainEvent.getRoomId())
                .setEventType(domainEvent.getEventType().getValue())
                .setOrderId(domainEvent.getOrderId())
                .setOccurredOn(domainEvent.getOccurredOn())
                .setArgs(domainEvent.getArgs());
    }
}
