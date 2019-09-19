package com.springextended.core.task.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>
 *  目前只支持拦截接口方法的的调用
 *  下一步考虑使用cglib以支持对类的方法拦截
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 10:00
 */
public class MethodCallProxy implements InvocationHandler {

    private Class interfaceClazz;
    private Method method;
    private Object[] args;

    public <T> T bind(Class<T> interfaceClazz){
        this.interfaceClazz = interfaceClazz;

        Class[] interfaces = new Class[1];
        interfaces[0]= interfaceClazz;

        return (T)Proxy.newProxyInstance(interfaceClazz.getClassLoader(),interfaces,this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        return null;
    }

    public MethodCall methodCall(){
        return new MethodCall(interfaceClazz,this.method,this.args);
    }
}
