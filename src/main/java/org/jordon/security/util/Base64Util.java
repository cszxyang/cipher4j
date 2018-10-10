package org.jordon.security.util;

import java.util.Base64;

/**
 * make it easy to print non printable and invisible characters
 * @author Jordon
 */
public class Base64Util {

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
    public static char[] decode(String encodedText) {
        return ArrayUtil.bytesToChars(Base64.getDecoder().decode(encodedText));
    }
}
