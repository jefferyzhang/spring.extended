package com.springextended.core.codegenerator;

import com.springextended.core.util.LocalDateTimeUtil;
import com.springextended.core.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 默认Code生成器
 * 保证每0.1秒生成 Math.pow(36,serialNoLength) 个不同的code
 * </p>
 *
 * @author chuanzhang
 * Email chuanzhang@uoko.com
 * created at 2019 - 05 - 05 18:59
 */
@Slf4j
public class DefaultCodeGenerator implements CodeGenerator {
    /**
     * 采用36进制
     */
    private static final int HEX = 36;
    /**
     * 时间戳占用长度 为4,因为一天有24*60*60*10个0.1秒，这个数用36进制来表示为IIO0，它占4位。
     */
    private static final int STAMP_LENGTH = 4;

    private final String machineId;
    private final int serialNoMaxValue;
    private final int serialNoLength;

    /**
     * 序列号，每生成一个code就加1，直到serialNoMaxValue,再循环
     */
    private AtomicReference<TimeNumber> timeNumber = new AtomicReference<>(new TimeNumber());

    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    /**
     * @param machineId      机器唯一标识
     * @param serialNoLength 序列号长度
     */
    public DefaultCodeGenerator(String machineId, int serialNoLength) {
        if (serialNoLength < 1) {
            throw new IllegalArgumentException("serialNoLength is not valid");
        }

        this.machineId = machineId;
        this.serialNoLength = serialNoLength;
        this.serialNoMaxValue = (int) Math.pow(HEX, this.serialNoLength);
    }

    @Override
    public String next(String prefix) {
        int serialNo;
        long timestamp;
        TimeNumber prevTimeNumber, nextTimeNumber;

        while (true) {
            prevTimeNumber = timeNumber.get();

            timestamp = getTheCurrentTimeAccurateToPointOneSeconds();

            validateTimestamp(prevTimeNumber.lastTimestamp, timestamp);

            serialNo = prevTimeNumber.serialNo;

            if (timestamp == prevTimeNumber.lastTimestamp) {
                serialNo++;
                if (serialNo >= serialNoMaxValue) {
                    serialNo = 0;
                    timestamp = tillNextTimeUnit(prevTimeNumber.lastTimestamp);
                }
            } else {
                serialNo = 0;
            }

            nextTimeNumber = new TimeNumber(timestamp, serialNo);
            if (timeNumber.compareAndSet(prevTimeNumber, nextTimeNumber)) {
                break;
            }
        }

        return next(LocalDateTimeUtil.getDateTimeFromStamp(timestamp), serialNo, prefix);
    }

    public String next(LocalDateTime time, int serialNo, String prefix) {
        String date = time.format(DATE_TIME_FORMATTER);

        long durationInMilliseconds = Duration.between(LocalDateTime.of(time.toLocalDate(), LocalTime.MIN), time).toMillis();

        //durationInMilliseconds精确到0.1秒
        int stamp = (int) Math.round(durationInMilliseconds / 100.0);

        String code = prefix + date + machineId
                + StringUtil.padLeft(HexConverter.convert(stamp, HEX), STAMP_LENGTH, '0')
                + StringUtil.padLeft(HexConverter.convert(serialNo, HEX), serialNoLength, '0');

        return code;
    }

    /**
     * 获取精确到0.1秒的当前时间
     * @return
     */
    private static long getTheCurrentTimeAccurateToPointOneSeconds(){
        long time = System.currentTimeMillis();
        return time - time%100;
    }

    /**
     * 获取下一个时间单元
     * @param lastTimestamp
     * @return
     */
    private static long tillNextTimeUnit(final long lastTimestamp){
        if(log.isInfoEnabled()){
            log.info("codes 已用完，等待下一个时间点");
        }
        long timestamp = getTheCurrentTimeAccurateToPointOneSeconds();
        while (timestamp <= lastTimestamp){
            timestamp = getTheCurrentTimeAccurateToPointOneSeconds();
        }
        if(log.isInfoEnabled()){
            log.info("下一个时间的已到");
        }
        return timestamp;
    }

    private static void validateTimestamp(long lastTimestamp,long timestamp){
        if(timestamp< lastTimestamp){
            throw new IllegalStateException("时间回拨，拒绝产生code");
        }
    }

    @Data
    static class TimeNumber {
        private long lastTimestamp = 0;
        private int serialNo = 0;

        public TimeNumber() {
        }

        public TimeNumber(long lastTimestamp, int serialNo) {
            this.lastTimestamp = lastTimestamp;
            this.serialNo = serialNo;
        }
    }
}
