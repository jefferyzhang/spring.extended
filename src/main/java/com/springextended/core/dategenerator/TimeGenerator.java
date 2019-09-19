package com.springextended.core.dategenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 时间产生器
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 15 16:01
 */
public interface TimeGenerator {
    /**
     * 今天
     * @return
     */
    LocalDate today();

    /**
     * 当前时间
     * @return
     */
    LocalDateTime now();
}


