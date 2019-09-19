package com.springextended.core.task.impl;

import com.springextended.core.dategenerator.TimeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * 下一次调用任务时间计算器
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 30 15:02
 */
@Component
@AllArgsConstructor
public class NextInvokeTaskTimeCalculator {
    private TimeGenerator timeGenerator;

    /**
     * 计算下一次调用时间
     * @param invokeFailTimes
     * @return
     */
    public LocalDateTime calculate(int invokeFailTimes) {
        //30秒后进行第一次重试
        if (invokeFailTimes <= RETRY_TIMES_PER_30_SECONDS) {
            return timeGenerator.now().plusSeconds(30);
        }
        invokeFailTimes-=RETRY_TIMES_PER_30_SECONDS;

        //然后第一个小时内 ，每5分钟重试一次
        if (invokeFailTimes <= RETRY_TIMES_PER_5_MINUTES) {
            return timeGenerator.now().plusMinutes(5);
        }
        invokeFailTimes-= RETRY_TIMES_PER_5_MINUTES;

        //然后23个小时内每1个小时重试一次
        if (invokeFailTimes <= RETRY_TIMES_PER_HOUR) {
            return timeGenerator.now().plusHours(1);
        }
        invokeFailTimes-= RETRY_TIMES_PER_HOUR;

        //然后14天内每一天重试一次
        if (invokeFailTimes <= RETRY_TIMES_PER_DAY) {
            return timeGenerator.now().plusDays(1);
        }
        return null;
    }

    private static final int RETRY_TIMES_PER_30_SECONDS=1;
    private static final int RETRY_TIMES_PER_5_MINUTES=11;
    private static final int RETRY_TIMES_PER_HOUR=23;
    private static final int RETRY_TIMES_PER_DAY=14;
}
