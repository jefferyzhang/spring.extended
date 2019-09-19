package com.springextended.core.event;

import com.springextended.core.event.forward.DomainEventForward;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  领域事件存储
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 29 17:50
 */
public interface DomainEventStorage   {

     /**
      * 添加领域事件
      * @param domainEvent
      */
     void add(DomainEvent domainEvent);

     /**
      * 查询待转发的领域事件
      * @param now
      * @param topCount
      * @param maxForwardTimes
      * @return
      */
     List<DomainEventForward> queryWaitingForwardEvents(LocalDateTime now, int topCount, int maxForwardTimes);

     /**
      * 保存领域事件
      * @param event
      */
     void save(DomainEvent event);

     /**
      * 删除转发记录
      * @param domainEventForward
      */
     void deleteForward(DomainEventForward domainEventForward);

     /**
      * 保存转发记录
      * @param domainEventForward
      */
     void saveForward(DomainEventForward domainEventForward);
}
