package org.jordon.security.core.crypto.assymmetry;

import lombok.Getter;
import org.jordon.security.util.MathUtil;

import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 原生RSA加解密算法实现
 * @author Jrodon
 * @since 2018/10
 */
@Getter
public class RawRSACipherService {
    // 密钥属性组
    private long n, d, e, eulerVal, p, q;
    // 素数的选择范围
    private static final int RANGE = 100;
    // 加密完得到的
    private static final int SPLIT_POINT = 2 << 8;
    // 素性测试轮数
    private static final int PRIMALITY_TESTING_ROUNDS = 4;
    // Base64编码工具
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public RawRSACipherService() {
        Random random = ThreadLocalRandom.current();
        // 随机选择两个大素数
        long p = MathUtil.probablePrime(RANGE, PRIMALITY_TESTING_ROUNDS), q;
        do {
            q = MathUtil.probablePrime(RANGE, PRIMALITY_TESTING_ROUNDS);
        } while (p == q);

        long eulerVal = (p - 1) * (q - 1);

        // 随机地取一个正整数e，1<e<φ(n)且(e,φ(n))=1，将e公开
        do {
            e = Math.abs(random.nextLong()) % eulerVal + 1;
        }while (MathUtil.gcd(e, eulerVal) != 1);

        // 根据ed = 1 (mod φ(n))，求出d，并对d保密
        d = (MathUtil.linearCongruence(e, 1, eulerVal)) % eulerVal;

        this.p = p;
        this.q = q;
        this.n = p * q;
        this.eulerVal = eulerVal;
    }

    /**
     * 加密函数，当数值val超过2 << 16时，将val转换为char类型会发生溢出问题
     *         所以将模逆结果值分割成两个字符表示,在解密时再进行还原
     * @param plaintext 明文
     * @return 密文
     */
    public String encrypt(String plaintext) {
        int[] plaintextBytes = changeToInts(plaintext.toCharArray());
        StringBuilder builder = new StringBuilder();
        for (int plaintextByte : plaintextBytes) {
            int modExpResult = (int) MathUtil.modExpNonRec(plaintextByte, e, n);
            builder.append((char) (modExpResult / SPLIT_POINT)).
                    append((char) (modExpResult % SPLIT_POINT));
        }
        return encoder.encodeToString(builder.toString().getBytes());
    }

    /**
     * 解密函数
     * @param cipher 密文
     * @return 明文
     */
    public String decrypt(String cipher) {
        // 获取解码后的字符串的字符数组
        char[] cipherChars = new String(decoder.decode(cipher)).toCharArray();

        // 将相邻两个字符合并，用一个整型数表示，得到原加密结果数组
        int[] cipherInts = new int[cipherChars.length / 2];
        for(int i = 0; i < cipherInts.length; i++)
            cipherInts[i] = (int)cipherChars[i * 2] * SPLIT_POINT
                    + (int)cipherChars[i * 2 + 1];
        // 解密
        int[] plaintextInts = new int[cipherInts.length];
        for(int i = 0; i < cipherInts.length; i++)
            plaintextInts[i] = (int)MathUtil.modExpNonRec(cipherInts[i], d, n);

        StringBuilder plainText = new StringBuilder();
        for (int plaintextInt : plaintextInts)
            plainText.append((char) plaintextInt);
        return plainText.toString();
    }

    /**
     * 考虑到Java的字符编码问题和扩展性，将字符数组转化为int数组
     * 而不用字节表示
     * @param chars 字符数组
     * @return int数组
     */
    private int[] changeToInts(char[] chars) {
        if (chars != null && chars.length > 0) {
            int[] result = new int[chars.length];
            for (int i = 0; i < chars.length; i++) {
                result[i] = chars[i];
            }
            return result;
        }
        return null;
    }
}