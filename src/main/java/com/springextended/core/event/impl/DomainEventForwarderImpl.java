package com.springextended.core.event.impl;

import com.springextended.core.event.*;
import com.springextended.core.event.forward.DomainEventForward;
import com.springextended.core.event.forward.DomainEventForwarder;
import com.springextended.core.event.forward.DomainEventInfo;
import com.springextended.core.event.forward.DomainEventSource;
import com.springextended.core.json.JsonConvert;
import com.springextended.core.lock.LockProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 15 17:36
 */
@Component
@Slf4j
@EnableBinding(DomainEventSource.class)
public class DomainEventForwarderImpl implements DomainEventForwarder {
    @Autowired
    private DomainEventStorage domainEventStorage;
    @Autowired
    private LockProvider lockProvider;
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private DomainEventSource eventSource;
    @Autowired
    private JsonConvert jsonConvert;

    private static final String EVENT_FORWARD_LOCK="event_forward";
    private static final int MAX_FORWARD_TIMES=50;
    private static final Duration EVENT_FORWARD_LOCK_DURATION=Duration.ofMinutes(15);
    private static final Duration RETRY_DELAY_EVENT_FORWARD_INTERVAL= Duration.ofMinutes(3);
    private static final long SEND_MESSAGE_TIMEOUT_IN_MILLIS = 3000;

    @Override
    public void process(LocalDateTime now, int batchSize) {

        log.debug("开始执行领域事件转发任务>>>");

        lockProvider.lock(EVENT_FORWARD_LOCK,EVENT_FORWARD_LOCK_DURATION,(lockTimer)->{

            while (true){
                List<DomainEventForward> domainEventForwards = domainEventStorage
                        .queryWaitingForwardEvents(now, batchSize,MAX_FORWARD_TIMES);
                log.debug("获取到待转发事件：{}",domainEventForwards.size());

                if(domainEventForwards.size()<=0){
                    break;
                }

                for (DomainEventForward domainEventForward :
                        domainEventForwards) {
                    boolean forwardResult = forwardDomainEvent(domainEventForward.getEvent());

                    //通过接口调用服务方法保证事务处理
                    DomainEventForwarder forwarder = ctx.getBean(DomainEventForwarder.class);
                    forwarder.handleDomainEventForwardResult(domainEventForward,now, forwardResult);

                    //如果处理时间超过了锁定超时时间，则立即退出，防止多服务器的并发操作
                    if(lockTimer.isTimeout()){
                        return;
                    }
                }
            }
        },false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handleDomainEventForwardResult(DomainEventForward domainEventForward, LocalDateTime now, boolean forwardResult) {
        DomainEvent event = domainEventForward.getEvent();

        if(forwardResult){
            event.forwardSuccess(now);

            domainEventStorage.save(event);
            domainEventStorage.deleteForward(domainEventForward);
        }else{
            domainEventForward.forwardFailed(RETRY_DELAY_EVENT_FORWARD_INTERVAL);

            domainEventStorage.saveForward(domainEventForward);
        }
    }

    public boolean forwardDomainEvent(DomainEvent event) {
        DomainEventInfo domainEventInfo = DomainEventInfo.of(event);
        try{
            Message<String> message = MessageBuilder
                    .withPayload(jsonConvert.serializeObject(domainEventInfo)).build();

            boolean forwardResult = eventSource.channel().send(message, SEND_MESSAGE_TIMEOUT_IN_MILLIS);

            log.info("事件:{},转发结果:{}",event.getId(),forwardResult);

            return forwardResult;

        }catch (Exception e){
            log.error("转发事件失败",e);
            return false;
        }
    }
}
