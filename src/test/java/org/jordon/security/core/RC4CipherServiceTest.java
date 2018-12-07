package org.jordon.security.core;

import org.jordon.security.core.crypto.symmetry.RC4CipherService;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;


public class RC4CipherServiceTest {

    @Test
    public void test() {
        RC4CipherService rc4CipherService = new RC4CipherService();
        String encryptedText = rc4CipherService.encrypt("password", "jason");
        System.out.println(encryptedText);

        String decryptedText = rc4CipherService.decrypt(encryptedText, "jason");
        System.out.println(decryptedText);
    }

    @Test
    public void testRan() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(4 - 2) + 2);;
        }
    }

    @Test
    public void test1() {
        String s = "测试测试";
        char[] chars = s.toCharArray();
        System.out.println(Arrays.toString(chars));
        for (char c : chars) {
            System.out.println((int)c);
        }
    }

}