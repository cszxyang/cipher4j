package org.jordon.security.core;

import org.jordon.security.constant.AESConstants;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class CipherServiceTest {

    @Test
    public void testAES() {
        short[] ptBytes = {
                0x00, 0x01, 0x00, 0x01, 0x01, 0xa1, 0x98, 0xaf,
                0xda, 0x78, 0x17, 0x34, 0x86, 0x15, 0x35, 0x66
        };

        short[] keyBytes = {
                0x00, 0x01, 0x20, 0x01, 0x71, 0x01, 0x98, 0xae,
                0xda, 0x79, 0x17, 0x14, 0x60, 0x15, 0x35, 0x94
        };
        AESCipherService service = new AESCipherService();
        service.coreOperation(ptBytes, keyBytes);
    }

    @Test
    public void testDESService() throws UnsupportedEncodingException {
        String plaintext = "01234567", key = "12345678";
        CipherService desService = new DESCipherService();
        String encryptedText = desService.encrypt(plaintext, key);
        desService.decrypt(encryptedText,key);
    }

    @Test
    public void testAESService() throws UnsupportedEncodingException {
        String p = "00000000000000010000000000000001000000011010000110011000101011111110000000000000000000000000000000000000000000000000000000000000";
        char[] pChars = p.toCharArray();
        System.out.println(pChars.length);
        System.out.println(Arrays.toString(pChars));

        String key = "00000000000000010010000000000001011100010000000110011000101011101110000000000000000000000000000000000000000000000000000000000000";
        char[] kChars = key.toCharArray();
        System.out.println(kChars.length);
        System.out.println(Arrays.toString(kChars));
//        String plaintext = "01234567", key = "12345678";
        CipherService aesService = new AESCipherService();

        short[] pBytes = {
                0x00, 0x01, 0x00, 0x01, 0x01, 0xa1, 0x98, 0xaf,
                0xda, 0x78, 0x17, 0x14, 0x60, 0x15, 0x35, 0x94
        };

        short[] shorts = new short[10];


        String str = "";

        char[] chars = str.toCharArray();
        for (char c : chars) {
            shorts[1] = (short) c;
        }

        int nb = pChars.length / 32;
        char[][][] state = new char[nb][][];

//        String encryptedText = desService.encrypt(plaintext, key);
//        desService.decrypt(encryptedText,key);
    }

    @Test
    public void test() {
        short[][] shorts = {
            {0x12, 0x25, 0xb2, 0x75},
            {0x12, 0x25, 0xb2, 0x75},
            {0x46, 0x25, 0xb2, 0x75},
            {0x73, 0x25, 0xb2, 0x75},

            {0x22, 0x25, 0xb2, 0x75},
            {0x23, 0x25, 0xb2, 0x75},
            {0x24, 0x25, 0xb2, 0x75},
            {0x25, 0x25, 0xb2, 0x75},
        };
        System.out.println(Arrays.deepToString(transfer(shorts)));;
    }

    private short[][][] transfer(short[][] shorts) {
        short[][][] result = new short[shorts.length / 4][4][4];
        for (int i = 0; i < shorts.length / 4; i++) {
            short[][] temp = new short[4][4];
            System.arraycopy(shorts, i * 4, temp, 0, 4);
            result[i] = temp;
        }
        return result;
    }

    @Test
    public void testMul() {
        short[] b = {0xd4, 0xbf, 0x5d, 0x30};
        AESCipherService service = new AESCipherService();

        for (int i = 0; i < 4; i++) {
//            System.out.println(service.matrixMultiply(AESConstants.CX[i], b));
        }
    }
}