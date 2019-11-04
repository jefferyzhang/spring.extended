package com.springextended.core.codegenerator;

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
}
