package com.springextended.core.automapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 对Spring的BeanUtils进行包装，以便支持枚举与整型值之间的映射
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 08 - 07 18:17
 */
public class SpringBeanUtilsWrapper {

    /**
     * 拷贝属性列表
     * @param source
     * @param target
     * @param ignoreProperties
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target ,
                                      @Nullable String... ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null ) {
                        if(ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())){
                            copyProperty(source, target, targetPd, writeMethod, readMethod);
                        }else{
                            mapBetweenIntAndEnum(source, target, targetPd, writeMethod, readMethod);
                        }
                    }
                }
            }
        }
    }

    /**
     * 拷贝属性
     * @param source
     * @param target
     * @param targetPd
     * @param writeMethod
     * @param readMethod
     */
    private static void copyProperty(Object source, Object target, PropertyDescriptor targetPd, Method writeMethod, Method readMethod) {
        try {
            ensurePropertyAccessible(writeMethod,readMethod);

            Object value = readMethod.invoke(source);
            writeMethod.invoke(target, value);
        }
        catch (Throwable ex) {
            throw new FatalBeanException(
                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
        }
    }

    /**
     * 在整型枚举值 与  整型值 之间映射
     * @param source
     * @param target
     * @param targetPd
     * @param writeMethod
     * @param readMethod
     */
    private static void mapBetweenIntAndEnum(Object source, Object target, PropertyDescriptor targetPd, Method writeMethod, Method readMethod)  {
        Class<?> writeMethodParameterType = writeMethod.getParameterTypes()[0];
        try{
            if(fromEnumToInt(readMethod.getReturnType(),writeMethodParameterType)){

                ensurePropertyAccessible(readMethod,writeMethod);

                //处理枚举到整型值的映射
                Object value = readMethod.invoke(source);
                if(value!=null){
                    value=  ((IntEnum)value).getValue();
                    writeMethod.invoke(target, value);
                }
            }else if(fromEnumToInt(writeMethodParameterType,readMethod.getReturnType())){

                ensurePropertyAccessible(readMethod,writeMethod);

                //处理整型值到枚举的映射
                Object value = readMethod.invoke(source);
                if(value!=null){

                    Object[] enumConstants = writeMethodParameterType.getEnumConstants();
                    for (Object enumConstant :
                            enumConstants) {
                        if(((IntEnum)enumConstant).getValue().equals(value)){
                            writeMethod.invoke(target,enumConstant);
                            break;
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            throw new FatalBeanException(
                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
        }
    }

    /**
     * 是否第一个参数是整型值枚举，第二个参数是整型值
     * @param fromType
     * @param toType
     * @return
     */
    private static boolean fromEnumToInt(Class<?> fromType,Class<?> toType){
        return fromType.isEnum()
                && ClassUtils.isAssignable(IntEnum.class,fromType)
                && toType.equals(Integer.class);
    }

    /**
     * 确保属性可以访问
     * @param writeMethod
     * @param readMethod
     */
    private static void ensurePropertyAccessible(Method writeMethod, Method readMethod) {

        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
            readMethod.setAccessible(true);
        }

        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
            writeMethod.setAccessible(true);
        }
    }
}
