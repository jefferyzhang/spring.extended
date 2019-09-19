package com.springextended.core.task.impl;

import java.lang.reflect.Method;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 10:04
 */
public class MethodCall {
    public MethodCall withId(String id) {
        this.id = id;
        return this;
    }

    public MethodCall withFailInvokeTimes(Integer invokeFailTimes) {
        this.failInvokeTimes= invokeFailTimes;
        return this;
    }

    private String id;
    private Class interfaceClazz;
    private Method method;
    private Object[] args;
    private Integer failInvokeTimes;

    public MethodCall(Class interfaceClazz, Method method, Object[] args) {
        this.interfaceClazz = interfaceClazz;
        this.method = method;
        this.args = args;
    }

    public Class getInterfaceClazz() {
        return interfaceClazz;
    }

    public Method getMethod() {
        return method;
    }
    public String getId() {
        return id;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object invoke(Object target) {
        try {
            return method.invoke(target,args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void increaseFailInvokeTimes() {

        if (failInvokeTimes == null) {
            failInvokeTimes = 0;
        }
        failInvokeTimes++;
    }

    public int getFailInvokeTimes() {
        return failInvokeTimes == null ? 0 : failInvokeTimes;
    }
}
