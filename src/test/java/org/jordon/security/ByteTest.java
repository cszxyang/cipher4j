package org.jordon.security;
import org.jordon.security.util.ArrayUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

public class ByteTest {

    @Test
    public void testStrToByteArr() throws UnsupportedEncodingException {
        byte[] bytes = plaintext.getBytes();
        byte[] bytes1 = plaintext.getBytes("UTF-8");

        System.out.println();
    }

    @Test
    public void print() throws UnsupportedEncodingException {
//        byte[] bytes = "password".getBytes("UTF-8");
//        for (byte b : bytes) {
//            System.out.println(Integer.toBinaryString(b) + "\t" +
//                    Integer.toBinaryString(b & 255) + "\t" + Integer.toBinaryString(b & 255 + 256));
////            System.out.println(b & 0xffff);
        System.out.println(Integer.toBinaryString(4 & 255 + 256));
        System.out.println(Integer.toBinaryString((4 & 0xff) + 0x0100).substring(1));
        System.out.println(Integer.toBinaryString(-2));
        System.out.println(Integer.toBinaryString(2));
        System.out.println(0x0100);
    }

    private String plaintext = "password11";
    @Test
    public void test() {
        byte[] bytes = plaintext.getBytes();
        System.out.println(bytes.length);
    }

    @Test
    public void testXor() {
        char[] a = {'0', '0', '1', '1', '1', '1', '0', '0', '0', '0', '1', '1', '1', '1', '0', '0',
                    '0', '0', '1', '1', '1', '1', '0', '0', '0', '0', '1', '1', '1', '1', '0', '0',
                    '0', '0', '1', '1', '1', '1', '0', '0', '0', '0', '1', '1', '1', '1', '0', '0'};

        char[] b = {'1', '1', '0', '0', '1', '1',
                    '0', '0', '0', '0', '1', '1',
                    '1', '1', '0', '0', '0', '0',
                    '1', '1', '1', '1', '0', '0',
                    '0', '0', '1', '1', '1', '1',
                    '0', '0', '0', '0', '1', '1',
                    '1', '1', '0', '0', '0', '0',
                    '1', '1', '1', '0', '1', '1'};

//        char[] xorResult = ArrayUtil.xor(a, b);
//        ArrayUtil.printArray(xorResult);
        System.out.println(Arrays.deepToString(ArrayUtil.segmentDimension(b, 8, 6)));
    }

    @Test
    public void testDimension() {
//        char[][] result = new char[8][6];
//        System.out.println(result.length);
        System.out.println(Long.parseLong("ffffffffffff", 16));
    }

    @Test
    public void modTwoAdd() {
        Random random = new Random();
        for (int i=0; i < 100; i++) {
            int a = random.nextInt(100) + 1;
            int b = random.nextInt(100) + 1;
            System.out.print(((a + b) % 2) + "  " + (a ^ b));
            System.out.println();
        }
    }
}
