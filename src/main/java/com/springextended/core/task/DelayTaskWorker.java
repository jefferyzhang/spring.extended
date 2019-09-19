package com.springextended.core.task;

import java.time.LocalDateTime;

/**
 * <p>
 *  延迟任务执行器
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 9:59
 */
public interface DelayTaskWorker {

   /**
    * 处理延迟任务
    * @param now
    * @param batchSize
    */
   void process(LocalDateTime now, int batchSize);
}
