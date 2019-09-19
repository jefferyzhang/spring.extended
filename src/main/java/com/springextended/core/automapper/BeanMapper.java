package com.springextended.core.automapper;

import java.util.List;
import java.util.function.BiConsumer;

public interface BeanMapper {

    /**
     * 映射单个对象
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    <T> T map(Object source, Class<T> targetClass);


    /**
     * 映射列表对象
     * @param source
     * @param targetClass
     * @param <TTarget>
     * @param <TSource>
     * @return
     */
    <TTarget,TSource> List<TTarget> map(List<TSource> source, Class<TTarget> targetClass);

    /**
     * 映射列表
     * @param source
     * @param mapAction
     * @param targetClass
     * @param <TTarget>
     * @param <TSource>
     * @return
     */
    <TTarget,TSource> List<TTarget> map(List<TSource> source, BiConsumer<TSource,TTarget> mapAction, Class<TTarget> targetClass);
}
