package com.springextended.core.task.impl;

import com.springextended.core.task.DelayTask;
import com.springextended.core.task.DelayTaskStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 16 18:59
 */
@Component
public class DelayTaskImpl implements DelayTask {

    @Autowired
    private DelayTaskStorage delayTaskStorage;

    @Override
    public <T> void schedule(Class<T> clazz, Consumer<T> methodCall,Duration delay) {
        LocalDateTime delayTo = LocalDateTime.now().plus(delay);

        scheduleCore(clazz,methodCall,delayTo);
    }

    @Override
    public <T> void schedule(Class<T> clazz, Consumer<T> methodCall, LocalDateTime delayTo) {

        scheduleCore(clazz,methodCall,delayTo);
    }

    private  <T> void scheduleCore(Class<T> clazz, Consumer<T> methodCall,LocalDateTime delayTo){
        if(clazz==null) {
            throw new IllegalArgumentException("clazz不能为空");
        }
        if(methodCall==null){
            throw new IllegalArgumentException("methodCall不能为空");
        }
        if(delayTo==null){
            throw new IllegalArgumentException("延迟时间不能为空");
        }

        MethodCallProxy methodCallProxy = new MethodCallProxy();

        T target = methodCallProxy.bind(clazz);

        methodCall.accept(target);

        delayTaskStorage.add(methodCallProxy.methodCall().withId(UUID.randomUUID().toString()),delayTo);
    }
}









