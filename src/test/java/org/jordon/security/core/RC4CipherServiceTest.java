package org.jordon.security.core;

import org.jordon.security.core.crypto.symmetry.RC4CipherService;
import org.junit.Test;


public class RC4CipherServiceTest {

    @Test
    public void test() {
        RC4CipherService rc4CipherService = new RC4CipherService();
        String encryptedText = rc4CipherService.encrypt("password", "jason");
        System.out.println(encryptedText);

        String decryptedText = rc4CipherService.decrypt(encryptedText, "jason");
        System.out.println(decryptedText);
    }
}