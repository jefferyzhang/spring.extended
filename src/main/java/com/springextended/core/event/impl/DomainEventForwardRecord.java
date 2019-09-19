package com.springextended.core.event.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.springextended.core.event.DomainEvent;
import com.springextended.core.event.forward.DomainEventForward;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 16 13:58
 */
@Data
@TableName("rental_event_queue")
public class DomainEventForwardRecord {

    private int id;

    private String eventId;

    private LocalDateTime needForwardTime;

    private int forwardTimes;

    public static DomainEventForwardRecord of(DomainEvent domainEvent){
        DomainEventForwardRecord forwardQueueRecord = new DomainEventForwardRecord();
        forwardQueueRecord.setEventId(domainEvent.getId());
        forwardQueueRecord.setNeedForwardTime(domainEvent.getOccurredOn());
        forwardQueueRecord.setForwardTimes(0);

        return forwardQueueRecord;
    }

    public DomainEventForward toEntity(Map<String, List<DomainEventRecord>> domainEvents){
        DomainEventForward domainEventForward = new DomainEventForward()
                .setId(id)
                .setNeedForwardTime(needForwardTime)
                .setForwardTimes(forwardTimes);

        List<DomainEventRecord> domainEventRecords = domainEvents.get(eventId);
        if(domainEventRecords!=null){
            DomainEvent domainEvent = new DomainEvent();
            domainEventRecords.get(0).fillTo(domainEvent);

            domainEventForward.setEvent(domainEvent);
        }
        return domainEventForward;
    }

    public void copyFrom(DomainEventForward forward){
         id = forward.getId();
         eventId = forward.getEvent().getId();
         needForwardTime = forward.getNeedForwardTime();
         forwardTimes = forward.getForwardTimes();
    }
}
