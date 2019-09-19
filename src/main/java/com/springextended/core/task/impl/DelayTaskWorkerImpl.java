package com.springextended.core.task.impl;

import com.springextended.core.lock.LockProvider;
import com.springextended.core.task.DelayTaskStorage;
import com.springextended.core.task.DelayTaskWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
 * created at 2019 - 05 - 17 11:07
 */
@Component
@Slf4j
public class DelayTaskWorkerImpl implements DelayTaskWorker {
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private DelayTaskStorage delayTaskStorage;
    @Autowired
    private LockProvider lockProvider;
    @Autowired
    private MethodCallMapper methodCallMapper;
    @Autowired
    private NextInvokeTaskTimeCalculator nextInvokeTaskTimeCalculator;

    private static final String DELAY_TASK_LOCK="delay_task";
    private static final Duration DELAY_TASK_LOCK_DURATION=Duration.ofMinutes(15);

    @Override
    public void process(LocalDateTime now, int batchSize) {

        log.debug("开始执行延迟任务>>>");

        lockProvider.lock(DELAY_TASK_LOCK,DELAY_TASK_LOCK_DURATION,(lockTimer)->{

            while (true){
                List<MethodCall> methodCalls = delayTaskStorage.get(now, batchSize);
                log.debug("获取到延迟任务：{}",methodCalls.size());

                if(methodCalls.size()<=0){
                    break;
                }

                for (MethodCall methodCall :
                        methodCalls) {
                    handleDelayTask(methodCall);

                    //如果处理时间超过了锁定超时时间，则立即退出，防止多服务器的并发操作
                    if(lockTimer.isTimeout()){
                        return;
                    }
                }
            }
        },false);
    }

    private void handleDelayTask(MethodCall methodCall) {
         boolean methodInvokeSuccess=false;
         try {
             Object target = ctx.getBean(methodCall.getInterfaceClazz());
             methodCall.invoke(target);

             log.info("处理任务成功：{}.{}",methodCall.getInterfaceClazz().getName(),methodCall.getMethod().getName());

             methodInvokeSuccess =true;
         }
         catch (Exception e){
             log.error("处理延迟任务失败:{}",methodCallMapper.convertTo(methodCall).toString(),e);
         }
         finally {
             log.debug("从redis删除任务");
             delayTaskStorage.delete(methodCall);
         }

         if(methodInvokeSuccess==false){
             log.warn("处理失败的任务，重新进入redis排队");

             methodCall.increaseFailInvokeTimes();
             LocalDateTime nextInvokeTaskTime = nextInvokeTaskTimeCalculator.calculate(methodCall.getFailInvokeTimes());
             if(nextInvokeTaskTime!=null){
                 delayTaskStorage.add(methodCall,nextInvokeTaskTime);
             }
         }
    }
}
