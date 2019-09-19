package com.springextended.core.event.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.springextended.core.event.DomainEvent;
import com.springextended.core.event.EventType;
import com.springextended.core.util.LocalDateTimeUtil;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 29 18:27
 */
@Data
@TableName("rental_event")
public class DomainEventRecord {
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
     * 参数对象
     */
    private String args;

    /**
     * 事件发生时间
     */
    private LocalDateTime occurredOn;

    /**
     * 是否已经转发
     */
    private boolean forwarded;

    /**
     * 转发时间
     */
    private LocalDateTime forwardTime;

    /**
     * 标签
     */
    private Integer flags;

    public void copyFrom(DomainEvent domainEvent,String args){
        id= domainEvent.getId();
        roomId = domainEvent.getRoomId();
        eventType = domainEvent.getEventType().getValue();
        orderId = domainEvent.getOrderId();
        occurredOn = domainEvent.getOccurredOn();

        forwarded = domainEvent.isForwarded();
        forwardTime = domainEvent.getForwardTime();

        if(forwarded==false){
            forwardTime = LocalDateTimeUtil.nullTime;
        }

        flags = domainEvent.getFlags();

        this.args= args;
    }

    public static DomainEventRecord of(DomainEvent domainEvent,String args){
        DomainEventRecord domainEventRecord = new DomainEventRecord();

        domainEventRecord.copyFrom(domainEvent,args);

        return domainEventRecord;
    }

    public void fillTo(DomainEvent domainEvent){
        domainEvent.setId(id);
        domainEvent.setRoomId(roomId);
        domainEvent.setEventType(EventType.valueOf(eventType));
        domainEvent.setOrderId(orderId);
        domainEvent.setOccurredOn(occurredOn);

        domainEvent.setForwarded(forwarded);

        if(forwarded){
            domainEvent.setForwardTime(forwardTime);
        }
        domainEvent.setFlags(flags);

        domainEvent.setArgs(args);
    }
}
