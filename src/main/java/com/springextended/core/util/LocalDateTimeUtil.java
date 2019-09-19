package com.springextended.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 17 9:46
 */
public class LocalDateTimeUtil {

    public static long getUtcEpochMilli(LocalDateTime dateTime){
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDateTime getDateTimeFromStamp(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static final LocalDateTime nullTime=LocalDateTime.of(1900,1,1,0,0);
}
