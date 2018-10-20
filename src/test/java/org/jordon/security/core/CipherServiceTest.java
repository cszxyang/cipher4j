package org.jordon.security.core;

import org.jordon.security.util.ArrayUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class CipherServiceTest {

    @Test
    public void testAES() throws UnsupportedEncodingException {
        String plaintext = "passwordTextCase", key = "simpleKeyCase123";
        CipherService aesService = new AESCipherService();
        String encryptedText = aesService.encrypt(plaintext, key);

        ArrayUtil.printInfo("encrypted text", encryptedText, false);
        aesService.decrypt(encryptedText, key);
    }

    @Test
    public void testDESService() throws UnsupportedEncodingException {
        String plaintext = "01234567", key = "12345678";
        CipherService desService = new DESCipherService();
        String encryptedText = desService.encrypt(plaintext, key);

        desService.decrypt(encryptedText,key);
    }
}