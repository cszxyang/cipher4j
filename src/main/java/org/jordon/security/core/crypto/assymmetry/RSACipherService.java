package org.jordon.security.core.crypto.assymmetry;

import lombok.Getter;

import java.math.BigInteger;
import java.security.SecureRandom;

@Getter
public class RSACipherService {
    private final static BigInteger one      = new BigInteger("1");
    private final static SecureRandom random = new SecureRandom();

    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger modulus;

    // generate an N-bit (roughly) public and private key
    public RSACipherService(int N) {
        // 随机选取选取素数p，q
        BigInteger p = BigInteger.probablePrime(N / 2, random);
        BigInteger q = BigInteger.probablePrime(N / 2, random);
        // 计算欧拉函数值
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

        modulus    = p.multiply(q);
        // 在实际应用中公钥通常为2^16 + 1
        publicKey  = new BigInteger("65537");
        // 模逆运算
        privateKey = publicKey.modInverse(phi);
    }


    public BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    public BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    public String toString() {
        String s = "";
        s += "public  = " + publicKey  + "\n";
        s += "private = " + privateKey + "\n";
        s += "modulus = " + modulus;
        return s;
    }
}
