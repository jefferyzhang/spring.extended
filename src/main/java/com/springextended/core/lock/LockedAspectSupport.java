package com.springextended.core.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;

/**
 * <p>
 * 启用锁定的切面处理
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 09 - 18 16:01
 */
@Aspect
@Component
@Order(1)
public class LockedAspectSupport {

    @Autowired
    private LockProvider lockProvider;

    @Pointcut("@annotation(com.springextended.core.lock.Locked)")
    public void lockedMethod() {
    }

    /**
     * 锁定后调用方法
     * @param point
     * @return
     * @throws Throwable
     */
    @Around(value = "lockedMethod()")
    public Object invokeWithinLock(ProceedingJoinPoint point) throws Throwable {
        Method method = getMethodFromJointPoint(point);

        Locked locked = method.getAnnotation(Locked.class);

        String resourceIdentifier = getResourceIdentifier(point, method);

        return invokeWithinLockCore(point, locked, resourceIdentifier);
    }

    /**
     * 锁定后调用方法
     * @param point
     * @param locked
     * @param resourceIdentifier
     * @return
     */
    private Object invokeWithinLockCore(ProceedingJoinPoint point, Locked locked, String resourceIdentifier) {
        final RetValWrapper retValWrapper = new RetValWrapper();

        lockProvider.lock(resourceIdentifier, Duration.ofSeconds(locked.duration()), () -> {

            try {
                Object retVal = point.proceed();
                retValWrapper.setVal(retVal);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

        }, locked.throwIfLockFail());

        return retValWrapper.getVal();
    }

    /**
     * 获取资源标识 用于锁定
     * @param point
     * @param method
     * @return
     */
    private String getResourceIdentifier(ProceedingJoinPoint point, Method method) {
        String resourceIdentifier = null;
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            ResourceIdentifier annotation = parameters[i].getAnnotation(ResourceIdentifier.class);
            if (annotation != null) {
                resourceIdentifier = annotation.keyPrefix() + ":" + (point.getArgs()[i]).toString();
                break;
            }
        }
        if (resourceIdentifier == null) {
            throw new RuntimeException("未找到资源标识符");
        }
        return resourceIdentifier;
    }

    /**
     * 通过Jointpoint获取方法
     * @param point
     * @return
     */
    private Method getMethodFromJointPoint(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        return methodSignature.getMethod();
    }
}
