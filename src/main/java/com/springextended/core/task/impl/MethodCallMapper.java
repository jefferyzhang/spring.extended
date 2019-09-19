package com.springextended.core.task.impl;

import com.springextended.core.json.JsonConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 10:42
 */
@Component
public class MethodCallMapper {

    @Autowired
    private JsonConvert jsonConvert;

    public  MethodCallInfo convertTo(MethodCall methodCall){
        Class<?>[] parameterTypes = methodCall.getMethod().getParameterTypes();

        String[] argTypes = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            argTypes[i]= parameterTypes[i].getName();
        }

        Object[] objArgs = methodCall.getArgs();
        String[] args = new String[objArgs.length];
        for (int i = 0; i < objArgs.length; i++) {
            args[i]= jsonConvert.serializeObject(objArgs[i]);
        }

        return new MethodCallInfo()
                .setTargetInterface(methodCall.getInterfaceClazz().getName())
                .setMethod(methodCall.getMethod().getName())
                .setArgTypes(argTypes)
                .setArgs(args)
                .setId(methodCall.getId())
                .setFailInvokeTimes(methodCall.getFailInvokeTimes());
    }

    public  MethodCall convertFrom( MethodCallInfo methodCallInfo){
        try{
            Class clazz = getClass(methodCallInfo);
            Class[] argTypes = getArgTypes(methodCallInfo);
            Method method = getMethod(clazz, methodCallInfo,argTypes);
            Object[] args = getArgs(methodCallInfo, argTypes);
            return new MethodCall(clazz,method,args)
                    .withId(methodCallInfo.getId())
                    .withFailInvokeTimes(methodCallInfo.getFailInvokeTimes());

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Class getClass(MethodCallInfo methodCallInfo)  throws Exception {
        return Class.forName(methodCallInfo.getTargetInterface());
    }

    private Method getMethod(Class clazz, MethodCallInfo methodCallInfo,Class[] argTypes)  throws Exception{
        return clazz.getMethod(methodCallInfo.getMethod(),argTypes);
    }

    private Class[] getArgTypes(MethodCallInfo methodCallInfo)  throws Exception {
        String[] strArgTypes = methodCallInfo.getArgTypes();
        Class[] argTypes= new Class[strArgTypes.length];

        for (int i = 0; i < strArgTypes.length; i++) {
            argTypes[i]= Class.forName(strArgTypes[i]);
        }

        return argTypes;
    }

    private Object[] getArgs(MethodCallInfo methodCallInfo,Class[] argTypes){
        String[] strArgs = methodCallInfo.getArgs();
        Object[] args= new Object[strArgs.length];

        for (int i = 0; i < strArgs.length; i++) {
            args[i]= jsonConvert.deserializeObject(strArgs[i],argTypes[i]);
        }

        return args;
    }
}
