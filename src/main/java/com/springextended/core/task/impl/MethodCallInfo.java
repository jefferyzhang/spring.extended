package com.springextended.core.task.impl;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 16 20:20
 */
@Data
@Accessors(chain = true)
public class MethodCallInfo{
    private String id;
    private String targetInterface;
    private String method;
    private String[] argTypes;
    private String[] args;
    private Integer failInvokeTimes;
}
