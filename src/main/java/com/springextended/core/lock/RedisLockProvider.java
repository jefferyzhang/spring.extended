package com.springextended.core.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * <p>
 * 基于Redis的分布式锁实现
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 14:39
 */

@Component
@Slf4j
public class RedisLockProvider implements LockProvider {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String LOCK_VALUE = "lockValue";
    private static final String LOCK_PREFIX ="lock:";
    private static final int LOCK_TIMEOUT_OFFSET=1;

    @Override
    public void lock(String resourceIdentifier, Duration duration, Action action,boolean throwIfLockFail) {
        String lockIdentifier = LOCK_PREFIX +resourceIdentifier;

        if (tryLock(lockIdentifier,duration))
        {
            try
            {
                action.perform();
            }
            finally
            {
                if (releaseLock(lockIdentifier)==false)
                {
                    log.error("尝试解锁资源失败,lockIdentifier:{}",lockIdentifier);
                }
            }
        }
        else
        {
            logLockFail(throwIfLockFail, lockIdentifier);
        }
    }

    @Override
    public void lock(String resourceIdentifier, Duration duration, Consumer<LockTimer> action,boolean throwIfLockFail) {
        String lockIdentifier = LOCK_PREFIX +resourceIdentifier;

        if (tryLock(lockIdentifier,duration))
        {
            LockTimer lockTimer = new LockTimer(duration.minusMinutes(LOCK_TIMEOUT_OFFSET));
            lockTimer.start();

            try
            {
                action.accept(lockTimer);
            }
            finally
            {
                if (releaseLock(lockIdentifier)==false)
                {
                    log.error("尝试解锁资源失败,lockIdentifier:{}",lockIdentifier);
                }
            }
        }
        else
        {
            logLockFail(throwIfLockFail, lockIdentifier);
        }
    }

    public Boolean tryLock(String lockIdentifier, Duration duration){
        log.debug("尝试锁定资源："+lockIdentifier);

        byte[] key = redisTemplate.getKeySerializer().serialize(lockIdentifier);

        RedisCallback<Boolean> lockCallback= connection -> {
            RedisStringCommands cmd = connection.stringCommands();

            return cmd.set(key
                    , LOCK_VALUE.getBytes()
                    , Expiration.from(duration)
                    , RedisStringCommands.SetOption.SET_IF_ABSENT);
        };
        return (Boolean) redisTemplate.execute(lockCallback);
    }

    public Boolean releaseLock(String lockIdentifier){
        log.debug("尝试解锁资源："+lockIdentifier);

        return redisTemplate.delete(lockIdentifier);
    }

    private void logLockFail(boolean throwIfLockFail, String lockIdentifier) {
        if(throwIfLockFail){
            log.error("尝试锁定资源失败,lockIdentifier:{}",lockIdentifier);
            throw new LockFailException();
        }else{
            log.warn("尝试锁定资源失败,lockIdentifier:{}",lockIdentifier);
        }
    }
}

