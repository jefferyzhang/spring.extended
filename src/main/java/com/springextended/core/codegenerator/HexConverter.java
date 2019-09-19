package com.springextended.core.codegenerator;

/**
 * <p>
 * 进制转换器
 * 支持转化为2到36进制
 * 比如36进制 那么就由 1，2，3，4，5，6，7，8，9，A,B,C,D,...,Z 组成，A代表10，B代表11....
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 06 - 03 10:30
 */
public class HexConverter {
    /**
     * 最小进制
     * 2进制为最小进制
     */
    private static final int MIN_HEX=2;
    /**
     * 最大进制
     * 字母+数字一共有36个，所以支持的最大进制为36
     */
    private static final int MAX_HEX=36;

    /**
     * 最大的一位数
     */
    private static final int MAX_SINGLE_DIGIT=9;

    /**
     * 所有的数字数量
     * 0-9，即10个
     */
    private static final int ALL_DIGIT_COUNT=10;


    public static String convert(int number, int hex)
    {
        if (hex < MIN_HEX || hex > MAX_HEX) {
            throw new IllegalArgumentException("hex is not valid!");
        }

        String result = "";

        while (number >= hex)
        {
            int x = number % hex;
            result = convertToNumberAndAlphabet(x) + result;
            number = number / hex;
        }
        result = convertToNumberAndAlphabet(number) + result;

        return result;
    }

    private static String convertToNumberAndAlphabet(int i)
    {
        if (i <= MAX_SINGLE_DIGIT) {
            return String.valueOf(i);
        }

        return String.valueOf((char)(i - ALL_DIGIT_COUNT + 'A'));
    }
}
