package com.springextended.core.codegenerator;

import java.time.LocalDateTime;

/**
 * <p>
 * Code生成器
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 05 18:58
 */
public interface CodeGenerator {

    /**
     * 生成下一个code
     * @param prefix
     * @return
     */
    String next(String prefix);

    /**
     * 指定时间生成下一个code
     * @param time
     * @param prefix
     * @return
     */
    String next(LocalDateTime time, String prefix);
}
