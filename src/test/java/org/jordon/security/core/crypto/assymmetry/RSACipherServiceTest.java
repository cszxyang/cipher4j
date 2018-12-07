package org.jordon.security.core.crypto.assymmetry;

import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSACipherServiceTest {
    private RSACipherService service = new RSACipherService(2 << 10);
    private final static SecureRandom random = new SecureRandom();

    @Test
    public void test() {
        int N = 1024;
        RSACipherService key = new RSACipherService(N);
        System.out.println(key);

        // create random message, encrypt and decrypt
        BigInteger message = new BigInteger(N - 1, random);

        BigInteger encrypt = key.encrypt(message);
        BigInteger decrypt = key.decrypt(encrypt);
        System.out.println("message   = " + message);
        System.out.println("encrypted = " + encrypt);
        System.out.println("decrypted = " + decrypt);
    }
}