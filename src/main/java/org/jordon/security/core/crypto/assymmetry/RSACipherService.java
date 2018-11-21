package org.jordon.security.core.crypto.assymmetry;

import org.jordon.security.core.crypto.CipherService;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSACipherService implements CipherService {

    @Override
    public String encrypt(String plaintext, String key) throws UnsupportedEncodingException {
        // 随机选取两个大素数p和q，并且保密
        BigInteger p = getProbablePrime();
        BigInteger q = getProbablePrime();

        // 计算n = pq，将n公开
        BigInteger n = p.multiply(q);

        // 计算φ(n) = (p - 1) (q -1)，对φ(n)保密
        BigInteger euler = p.subtract(BigInteger.ONE).multiply(p.subtract(BigInteger.ONE));

        // 随机选取一个正整数e，使得1<e<φ(n)且(e, φ(n)) = 1， 将e作为公钥公开
        BigInteger publicKey = BigInteger.ONE;
        while (publicKey.compareTo(BigInteger.ONE) <= 0 || publicKey.compareTo(euler) > 0 ||
                publicKey.gcd(euler).compareTo(BigInteger.ONE) != 0) {
            publicKey = new BigInteger(String.valueOf(new Random().nextInt(Integer.MAX_VALUE / 2)
                    + Integer.MAX_VALUE / 2));
        }

        BigInteger privateKey = congruenceEquation(publicKey, euler);

        return null;
    }

    /**
     * 解同余方程 ax = 1 (mod m)（其中a < m）
     */
    private BigInteger congruenceEquation(BigInteger a, BigInteger m) {
        if(!BigInteger.ONE.mod(a.gcd(m)).equals(BigInteger.ZERO))
            return new BigInteger("-1");

        BigInteger result = BigInteger.ONE.divide(a.gcd(m)).multiply(extendedEuclid(a, m));

        if(result.compareTo(BigInteger.ZERO) < 0)
            result = result.add(m);
        return result;
    }

    /**
     * 扩展欧几里得非递归实现
     * @param a 操作数
     * @param b 操作数
     */
    private BigInteger extendedEuclid(BigInteger a, BigInteger b) {
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
        return lastY;
    }

    @Override
    public String decrypt(String encryptedText, String key) throws UnsupportedEncodingException {
        return null;
    }

    /**
     * 在Integer.MAX_VALUE / 2 到 Integer.MAX_VALUE 之间
     * 获取一个（可能是）素数的数
     * @return 概率性素数
     */
    private BigInteger getProbablePrime() {
        SecureRandom random = new SecureRandom();
        int half = Integer.MAX_VALUE / 2;
        return BigInteger.probablePrime(random.nextInt(half) + half, new Random());
    }
}
