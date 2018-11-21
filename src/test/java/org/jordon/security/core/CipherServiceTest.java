package org.jordon.security.core;

import org.jordon.security.core.crypto.CipherService;
import org.jordon.security.core.crypto.symmetry.AESCipherService;
import org.jordon.security.core.crypto.symmetry.DESCipherService;
import org.jordon.security.util.ArrayUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

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
        CipherService desService = new DESCipherService(false);
        String encryptedText = desService.encrypt(plaintext, key);

        desService.decrypt(encryptedText,key);
    }

    @Test
    public void t() {
//        extendedEuclid(3337, 547);
        extendedEuclid(new BigInteger("3337"), new BigInteger("547"));
    }

    private void extendedEuclid(long a, long b) {
        long x = 0, y = 1, lastX = 1, lastY = 0, temp;
        while (b != 0)
        {
            long q = a / b;
            long r = a % b;

            a = b;
            b = r;

            temp = x;
            x = lastX - q * x;
            lastX = temp;

            temp = y;
            y = lastY - q * y;
            lastY = temp;
        }
        System.out.println("Roots  x : " + lastX + " y :" + lastY);
    }

    /**
     * 扩展欧几里得非递归实现
     * @param a 操作数
     * @param b 操作数
     */
    private void extendedEuclid(BigInteger a, BigInteger b) {
        BigInteger x = BigInteger.ZERO, y = BigInteger.ONE,
                lastX = BigInteger.ONE, lastY = BigInteger.ZERO, temp;

        if (a.compareTo(b) < 0) {
            temp = a;
            a = b;
            b = temp;
        }

        while (!b.equals(BigInteger.ZERO)) {
            BigInteger q = a.divide(b);
            BigInteger r = a.mod(b);

            a = b;
            b = r;

            temp = x;
            x = lastX.subtract(q.multiply(x));
            lastX = temp;

            temp = y;
            y = lastY.subtract(q.multiply(y));
            lastY = temp;
        }
        System.out.println("Roots  x : " + lastX.toString(10) + " y :" +
                                                    lastY.toString(10));
    }
}