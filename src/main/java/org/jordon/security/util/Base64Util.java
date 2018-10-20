package org.jordon.security.util;

import java.util.Base64;

/**
 * 通过Base64编码规则将不可见字符转化为可见字符
 * make it easy to print non printable and invisible characters
 * @author Jordon
 */
public class Base64Util {

    /**
     * 对字节数组进行编码
     * encode byte array
     * @param bytes byte array
     * @return encoded string
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 将已经经过Base64编码的字符串解码成short数组
     * decode a encoded string to short array
     * @param encodedText encoded text
     * @return short array
     */
    public static short[] decodeToShorts(String encodedText) {
        return ArrayUtil.byteToShorts(Base64.getDecoder().decode(encodedText));
    }

    /**
     * encode encrypted text bits by Base64 for processing unprintable
     * and invisible character
     * @param chars encrypted text bits
     * @return encoded text
     */
    public static String encode(char[] chars) {
        char[][] segmentedChars = ArrayUtil.segmentDimension(chars, 8, 8);
        byte[] bytes = new byte[0];
        for (char[] segmentedChar : segmentedChars) {
            bytes = ArrayUtil.concat(bytes, ArrayUtil.int2Bytes(
                    Integer.parseInt(String.valueOf(segmentedChar), 2)));
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * decode decoded encrypted text bits in chars format
     * @param encodedText encoded text
     * @return encrypted text bits in chars format
     */
    public static char[] decodeToChars(String encodedText) {
        return ArrayUtil.bytesToChars(Base64.getDecoder().decode(encodedText));
    }
}
