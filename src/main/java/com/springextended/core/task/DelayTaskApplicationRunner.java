package com.springextended.core.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 延迟任务处理者 入口
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 18:16
 */
@Component
@Slf4j
class DelayTaskApplicationRunner implements ApplicationRunner,Runnable {

    @Autowired
    private DelayTaskWorker delayTaskWorker;

    private static final int DELAY_TASK_BATCH_SIZE=5;
    private static final int DELAY_TASK_HANDLE_INTERVAL=45;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("delay-task-schedule-pool-%d").daemon(true).build());

        executorService.scheduleAtFixedRate(this, 15, DELAY_TASK_HANDLE_INTERVAL, TimeUnit.SECONDS);

        log.info("延迟任务Job已启动");
    }

    @Override
    public void run() {
        try {
            delayTaskWorker.process(LocalDateTime.now(),DELAY_TASK_BATCH_SIZE);
        }catch (Exception e){
            log.error("延迟任务执行失败",e);
        }
    }
}
