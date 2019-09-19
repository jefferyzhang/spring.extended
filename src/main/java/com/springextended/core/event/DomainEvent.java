package com.springextended.core.event;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 领域事件参数
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 05 11:59
 */

@Data
@Accessors(chain = true)
public class DomainEvent {
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
    private EventType eventType;
    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 参数对象
     */
    private Object argObj;

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

    private String args;

    public static DomainEvent of(String roomId,EventType eventType,String orderId){
        return new DomainEvent()
                .setRoomId(roomId)
                .setEventType(eventType)
                .setOrderId(orderId)
                .setOccurredOn(LocalDateTime.now())
                .setArgObj(new Object());
    }

    /**
     * 是否是空参数
     * @return
     */
    public boolean isEmptyArgs(){
        return argObj==null || argObj.getClass() == Object.class;
    }

    public void forwardSuccess(LocalDateTime now) {
        forwarded = true;
        forwardTime = now;
    }
}
