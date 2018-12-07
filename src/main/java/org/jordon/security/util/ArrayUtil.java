package org.jordon.security.util;

import java.io.*;
import java.math.BigInteger;

public class ArrayUtil {

    /**
     * 将字符串转为一维short数组
     * transfer a string to short array
     * @param string target string to be process
     * @return a short array
     */
    public static short[] transferToShorts(String string) {
        byte[] bytes = string.getBytes();
        int length = bytes.length;
        short[] shorts = new short[length];
        for (int i = 0; i < length; i++) {
            shorts[i] = bytes[i];
        }
        return shorts;
    }

    /**
     * print information for tracing the whole process
     *
     * @param key info type
     * @param value info
     * @param nextRow flag deciding whether going to next row
     */
    public static String printInfo(String key, String value, boolean nextRow) {
        String message = String.format("%-30s%-70s", key, value);
        if (nextRow) {
            message += "\n";
        }
        System.out.println(message);
        return message;
    }

    /**
     * concatenate two byte arrays
     * @param before the first array in concat
     * @param after the second array in concat
     * @return concat
     */
    public static byte[] concat(byte[] before, byte[] after) {
        byte[] result = new byte[before.length + after.length];
        System.arraycopy(before, 0, result, 0, before.length);
        System.arraycopy(after, 0, result, before.length, after.length);
        return result;
    }

    /**
     * transfer int value into byte array
     * @param intValue int value
     * @return byte array
     */
    public static byte[] int2Bytes(int intValue) {
        byte[] byteArray = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(byteOut);
            dataOut.writeByte(intValue);
            byteArray = byteOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    /**
     * segment one-dimension array into an 8x8 array
     * transfer every byte unit into int
     * transfer int into ascii type  and print it
     * @param chars char array to be processed
     */
    public static String segmentAndPrintChars(char[] chars) {
        char[][] chars1 = ArrayUtil.segmentDimension(chars, chars.length / 8 ,8);
        StringBuilder builder = new StringBuilder();
        for (char[] aChars1 : chars1) {
            builder.append((char) Integer.parseInt(String.valueOf(aChars1), 2));
        }
        return builder.toString();
    }

    /**
     * transfer byte array into char array
     * @param bytes byte array to be transferred
     * @return bits in chars format
     */
    public static char[] bytesToChars(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            String aByteStr = Integer.toBinaryString(
                    (aByte & 0xff) + 0x0100).substring(1);
            builder.append(aByteStr);
        }
        return builder.toString().toCharArray();
    }

    public static short[] byteToShorts(byte[] bytes) {
        short[] result = new short[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (short) (bytes[i] & 0xff);
        }
        return result;
    }

    /**
     * disrupt the order of an array by reindexing it
     * @param chars original array to be disrupted
     * @param newIndexes index array the disruption is according to
     * @return disrupted sub array of chars
     */
    public static char[] disruptArray(char[] chars, short[] newIndexes) {
        char[] resultChars = new char[newIndexes.length];
        for (int i = 0; i < newIndexes.length; i++) {
            resultChars[i] = chars[newIndexes[i] - 1];
        }
        return resultChars;
    }

    /**
     * byte array printing
     * @param bytes byte array
     */
    public static void printByteArray(byte[] bytes){
        int length = bytes.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            String symbol = (i % 8 == 0) ? " " : "";
            builder.append((int) bytes[i - 1]).append(symbol);
        }
        System.out.println(builder.toString());
    }

    /**
     * print bits in chars format and what it stands for
     * @param prefix info
     * @param chars bits in chars format to be printed
     */
    public static String printBitChars(String prefix, char[] chars) {
        int length = chars.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            String symbol = (i % 8 == 0 || i == length)
                    ? " " : "";
            builder.append(chars[i - 1]).append(symbol);
        }
        String message = String.format("%-30s%-30s", prefix, builder.toString());
        System.out.println(message);
        return message;
    }

    /**
     * char array printing
     * @param chars char array to be printed
     */
    public static void printArray(char[] chars) {
        int length = chars.length;
        for (int i = 1; i <= length; i++) {
            String symbol = (i % 8 == 0 || i == length)
                    ? "\n" : " ";
            System.out.print(chars[i - 1] + symbol);
        }
    }

    /**
     * left shifting based on String operation
     * @param src bits to be shifted in chars format
     * @param length shifting length
     * @return shifted bit sequences in chars format
     */
    public static char[] leftShift(char[] src, int length) {
        String byteString = String.valueOf(src);
        return (byteString.substring(length) +
                byteString.substring(0, length)).toCharArray();
    }

    /**
     * xor operation
     * @param aChars operating bits in chars format
     * @param bChars operating bits in chars format
     * @return result of operation, formatting in 48 or 64 bits
     */
    public static char[] xor(char[] aChars, char[] bChars, int bits) {
        BigInteger a = new BigInteger(String.valueOf(aChars), 2);
        BigInteger b = new BigInteger(String.valueOf(bChars), 2);

        BigInteger xorResult = a.xor(b);

        String xorStr = xorResult.toString(2);
        StringBuilder builder = new StringBuilder();

        int sub = bits - xorStr.length();
        if (sub > 0) {
            for (int i = 0; i < sub; i++) {
                builder.append("0");
            }
            xorStr = builder.toString() + xorStr;
        }
        return xorStr.toCharArray();
    }

    /**
     * segment one-dimension array into a two-dimension array
     * which holds data in rows x columns format
     * @param chars one-dimension array
     * @param rows rows of result
     * @param columns columns of result
     * @return a two-dimension array
     *          which holds data in rows x columns format
     */
    public static char[][] segmentDimension(char[] chars, int rows, int columns) {
        char[][] result = new char[rows][columns];
        for (int i = 0; i < result.length; i++) {
            System.arraycopy(chars, i * columns, result[i], 0, result[i].length);
        }
        return result;
    }

    /**
     * concatenate two arrays after left shifting in loop
     * @param before the first array in concat
     * @param after  the second array in concat
     * @return concat
     */
    public static char[] concat(char[] before, char[] after) {
        return (String.valueOf(before) + String.valueOf(after))
                .toCharArray();
    }

    /**
     * 获取文件字节数组
     * @param file 文件
     * @return 字节数组
     */
    public static byte[] getBytes(File file){
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return buffer;
    }
}