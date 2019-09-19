package com.springextended.core.lock;

import lombok.Data;

/**
 * <p>
 * 返回值包装器
 * 主要用于lambda表达式中不能修改上下文变量的情况
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 09 - 18 16:27
 */
@Data
public class RetValWrapper {
    private Object val;
}
