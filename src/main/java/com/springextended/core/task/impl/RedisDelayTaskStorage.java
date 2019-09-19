package com.springextended.core.task.impl;

import com.springextended.core.json.JsonConvert;
import com.springextended.core.task.DelayTaskStorage;
import com.springextended.core.util.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 基于Redis的延迟任务存储
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 16 20:19
 */
@Component
public class RedisDelayTaskStorage implements DelayTaskStorage {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JsonConvert jsonConvert;
    @Autowired
    private MethodCallMapper methodCallMapper;

    private static final String DELAY_TASK="task:delaytaskqueue";

    @Override
    public void add(MethodCall methodCall, LocalDateTime delayTo){

        ZSetOperations ops = redisTemplate.opsForZSet();

        String jsonMethodCallInfo = jsonConvert.serializeObject(methodCallMapper.convertTo(methodCall));

        ops.add(DELAY_TASK,jsonMethodCallInfo,LocalDateTimeUtil.getUtcEpochMilli(delayTo));
    }

    @Override
    public List<MethodCall> get(LocalDateTime now, int topCount){
        ZSetOperations ops = redisTemplate.opsForZSet();

        Set tasks = ops.rangeByScore(DELAY_TASK, 0,LocalDateTimeUtil.getUtcEpochMilli(now),0,topCount);

        List<MethodCall> methodCalls = new ArrayList<>();

        for (Object task :
                tasks) {
            MethodCallInfo methodCallInfo = jsonConvert.deserializeObject((String) task, MethodCallInfo.class);
            MethodCall methodCall = methodCallMapper.convertFrom(methodCallInfo);
            methodCalls.add(methodCall);
        }

        return methodCalls;
    }

    @Override
    public void delete(MethodCall methodCall){
        ZSetOperations ops = redisTemplate.opsForZSet();

        String jsonMethodCallInfo = jsonConvert.serializeObject(methodCallMapper.convertTo(methodCall));

        ops.remove(DELAY_TASK,jsonMethodCallInfo);
    }
}
