package com.springextended.core.codegenerator;



import com.springextended.core.util.StringUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *  默认Code生成器
 *  保证每0.1秒生成 Math.pow(36,serialNoLength) 个不同的code
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 05 18:59
 */
public class DefaultCodeGenerator implements CodeGenerator {
    /**
     * 采用36进制
     */
    private static final int HEX = 36;
    /**
     * 时间戳占用长度 为4,因为一天有24*60*60*10个0.1秒，这个数用36进制来表示为IIO0，它占4位。
     */
    private static final int STAMP_LENGTH=4;

    private final String machineId;
    private final int serialNoMaxValue;
    private final int serialNoLength;

    /**
     * 序列号，每生成一个code就加1，直到serialNoMaxValue,再循环
     */
    private final AtomicInteger serialNo = new AtomicInteger(-1);

    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    /**
     *
     * @param machineId 机器唯一标识
     * @param serialNoLength 序列号长度
     */
    public DefaultCodeGenerator(String machineId, int serialNoLength)
    {
        if (serialNoLength < 1) {
            throw new IllegalArgumentException("serialNoLength is not valid");
        }

        this.machineId = machineId;
        this.serialNoLength = serialNoLength;
        this.serialNoMaxValue = (int)Math.pow(HEX, this.serialNoLength);
    }

    @Override
    public String next(String prefix)
    {
        return next(LocalDateTime.now(), prefix);
    }

    @Override
    public String next(LocalDateTime time, String prefix)
    {
        String date = time.format(DATE_TIME_FORMATTER);

        long durationInMilliseconds = Duration.between(LocalDateTime.of(time.toLocalDate(),LocalTime.MIN),time).toMillis();

        //durationInMilliseconds精确到0.1秒
        int stamp = (int)Math.round(durationInMilliseconds/100.0);

        int nextSerialNo = serialNo.updateAndGet(this::nextSerialNo);

        String code = prefix + date + machineId
                + StringUtil.padLeft(HexConverter.convert(stamp, HEX),STAMP_LENGTH,'0')
                + StringUtil.padLeft(HexConverter.convert(nextSerialNo, HEX), serialNoLength,'0');

        return code;
    }

    private int nextSerialNo(int prev) {
        int next = prev + 1;
        if (next >= serialNoMaxValue) {
            next = 0;
        }
        return next;
    }
}
