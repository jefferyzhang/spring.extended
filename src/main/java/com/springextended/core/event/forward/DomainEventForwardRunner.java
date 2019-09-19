package com.springextended.core.event.forward;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  领域事件转发Runner
 *     负责定时 触发 转发器
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 15 17:39
 */
@Slf4j
@Component
public class DomainEventForwardRunner implements ApplicationRunner,Runnable {

    @Autowired
    private DomainEventForwarder domainEventForwarder;

    private static final int DOMAIN_EVENT_FORWARD_INTERVAL=7;

    @Value("${app.domainevent.forward.enabled:true}")
    private boolean domainEventForwardEnabled =true;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(domainEventForwardEnabled){
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("domain-event-forward-schedule-pool-%d").daemon(true).build());

            executorService.scheduleAtFixedRate(this, 15, DOMAIN_EVENT_FORWARD_INTERVAL, TimeUnit.SECONDS);

            log.info("领域事件转发任务Job已启动");
        }
    }

    @Override
    public void run() {
        try {
            if(domainEventForwardEnabled){
                domainEventForwarder.process(LocalDateTime.now(),5);
            }
        }catch (Exception e){
            log.error("领域事件转发失败",e);
        }
    }
}
