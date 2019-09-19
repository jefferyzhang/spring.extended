package com.springextended.core.util;

import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 04 - 25 10:28
 */
public class LocalDateUtil {
    /**
     * 返回更小日期
     * @param date1
     * @param date2
     * @return
     */
    public static LocalDate getMinDate(LocalDate date1,LocalDate date2){
       return date1.isBefore(date2)?date1:date2;
    }

    public static final LocalDate nullDate=LocalDate.of(1900,1,1);

    public static int calcDayDiff(LocalDate from,LocalDate to){
        return (int)to.toEpochDay()-(int)from.toEpochDay();
    }
}
