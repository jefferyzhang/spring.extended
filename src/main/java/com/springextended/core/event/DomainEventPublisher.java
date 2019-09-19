package com.springextended.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *   领域事件发布器
 *   一个轻量级的实现，将事件发布到ThreadLocal里面
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 04 - 30 15:48
 */
@Slf4j
public class DomainEventPublisher {
    /**
     * 每一个线程 都保存一个 订阅者 列表
     */
    private static final ThreadLocal<List<DomainEventListener>> subscribers =
            new NamedThreadLocal<>("Current thread domain event listeners");

    /**
     * 每一个线程 都保存一个 是否在发布中的 标签
     */
    private static final ThreadLocal<Boolean> publishing = ThreadLocal.withInitial(() -> Boolean.FALSE);

    public static DomainEventPublisher instance(){
        return new DomainEventPublisher();
    }

    /**
     * 发布事件
     * @param domainEvent
     */
    public  void publish(DomainEvent domainEvent){
        if(publishing.get()){
            return;
        }
        try {
            log.debug("publishing event :{}", domainEvent);

            publishing.set(true);

            List<DomainEventListener> domainEventListeners = subscribers.get();

            if(domainEventListeners!=null){
                for (DomainEventListener listener :
                        domainEventListeners) {
                    listener.onEvent(domainEvent);
                }
            }
        }finally {
            publishing.set(false);
        }
    }

    /**
     * 订阅事件
     * @param listener
     */
    public void subscribe(DomainEventListener listener){
        if(publishing.get()){
            return;
        }

        List<DomainEventListener> domainEventListeners = subscribers.get();
        if(domainEventListeners==null){
            domainEventListeners = new ArrayList<>();
            subscribers.set(domainEventListeners);
        }
        domainEventListeners.add(listener);
    }


    /**
     * 重置
     * @return
     */
    public DomainEventPublisher reset(){
        if(!publishing.get()){
            log.debug("reset event subscribers.");
            subscribers.set(null);
        }
        return this;
    }
}






