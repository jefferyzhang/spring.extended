package com.springextended.core.event.forward;

import com.springextended.core.event.DomainEvent;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <p>
 * 领域事件转发排队记录
 *    用于对需要进行转发的事件进行排队
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 17 10:44
 */
@Data
@Accessors(chain = true)
public class DomainEventForward {
    /**
     * 排队id
     */
    private int id;

    /**
     * 应当转发时间
     *
     */
    private LocalDateTime needForwardTime;

    /**
     * 转发次数
     */
    private int forwardTimes;

    /**
     * 领域事件
     */
    private DomainEvent event;

    /**
     * 转发失败
     * @param forwardInterval
     */
    public void forwardFailed(Duration forwardInterval) {
        forwardTimes++;
        needForwardTime = needForwardTime.plus(forwardInterval);
    }
}
