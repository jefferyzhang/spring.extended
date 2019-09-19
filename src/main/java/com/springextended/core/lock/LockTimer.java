package com.springextended.core.lock;

import org.springframework.util.StopWatch;

import java.time.Duration;

/**
 * <p>
 * 锁计时器
 *   用于判断是否锁超时
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 19 15:08
 */
public class LockTimer {
    private StopWatch sw = new StopWatch();
    private Duration duration;

    public LockTimer(Duration duration) {
        this.duration = duration;
    }

    public void start(){
        sw.start();
    }

    /**
     * 是否超时
     * @return
     */
    public boolean isTimeout(){
        sw.stop();

        boolean isTimeout = sw.getTotalTimeSeconds() > duration.getSeconds();

        sw.start();

        return isTimeout;
    }

    public void stop(){
        sw.stop();
    }
}
