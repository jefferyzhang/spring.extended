package com.springextended.core.automapper;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 通过Properties一一映射到目标类，并最终返回目标类的对象
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created by 2019/4/12 0012
 */
@Component
public class DefaultBeanMapper implements BeanMapper {
    /**
     * 映射单对象
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    @Override
    public  <T> T map(Object source, Class<T> targetClass){
        if(source==null){
            return null;
        }

        T instance;
        try {
            instance = targetClass.newInstance();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        SpringBeanUtilsWrapper.copyProperties(source,instance);
        return instance;
    }

    /**
     * 映射列表
     * @param source
     * @param targetClass
     * @param <TTarget>
     * @param <TSource>
     * @return
     */
    @Override
    public  <TTarget,TSource> List<TTarget> map(List<TSource> source, Class<TTarget> targetClass){
        if(source==null){
            return new ArrayList<>();
        }

        return source.stream()
                .map(element->map(element,targetClass))
                .collect(Collectors.toList());
    }

    @Override
    public <TTarget, TSource> List<TTarget> map(List<TSource> source, BiConsumer<TSource, TTarget> mapAction, Class<TTarget> targetClass) {
        if(source==null){
            return new ArrayList<>();
        }

        return source.stream()
                .map(element->{
                    TTarget target = map(element, targetClass);
                    mapAction.accept(element,target);
                    return target;
                })
                .collect(Collectors.toList());
    }
}