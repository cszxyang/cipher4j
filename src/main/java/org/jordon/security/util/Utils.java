package org.jordon.security.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author steven
 */
public class Utils {

    private static Random ran = null;

    static {
        ran = new SecureRandom();
    }

    /**
     * 计算 base^exp % n
     *
     * @param base
     * @param exp
     * @param n
     * @return
     */
    public static BigInteger expmod(int base, BigInteger exp, BigInteger n) {
        if (exp.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }

        if (!exp.testBit(0)) {//如果为偶数
            return expmod(base, exp.divide(BigInteger.valueOf(2)), n).pow(2).remainder(n);
        } else {
            return (expmod(base, exp.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)), n).pow(2).multiply(BigInteger.valueOf(base))).remainder(n);
        }
    }

    /**
     * 费马测试, 如果返回false, 则n肯定为合数, 如果为true, 则n有一半以上的概率为素数
     *
     * @param n
     * @return
     */
    public static boolean fermatTest(BigInteger n) {
        int base = 0;
        if (n.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) < 0) {
            base = ran.nextInt(n.intValue() - 1) + 1;
        } else {
            base = ran.nextInt(Integer.MAX_VALUE - 1) + 1;
        }
        if (expmod(base, n, n).equals(BigInteger.valueOf(base))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Miller-Rabin测试
     *
     * @param n
     * @return
     */
    public static boolean passesMillerRabin(BigInteger n) {
        int base = 0;
        if (n.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) < 0) {
            base = ran.nextInt(n.intValue() - 1) + 1;
        } else {
            base = ran.nextInt(Integer.MAX_VALUE - 1) + 1;
        }

        BigInteger thisMinusOne = n.subtract(BigInteger.ONE);
        BigInteger m = thisMinusOne;
        while (!m.testBit(0)) {
            m = m.shiftRight(1);
            BigInteger z = expmod(base, m, n);
            if (z.equals(thisMinusOne)) {
                break;
            } else if (z.equals(BigInteger.ONE)) {

            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean isPrime(BigInteger n) {
        //copy自jdk源码, n的bit数越多, 需要的检测次数就越少
        //注释说是根据标准 ANSI X9.80, "PRIME NUMBER GENERATION, PRIMALITY TESTING, AND PRIMALITY CERTIFICATES".
        //我不知道为什么
        int sizeInBits = n.bitLength();
        int tryTime = 0;
        if (sizeInBits < 100) {
            tryTime = 50;
        }

        if (sizeInBits < 256) {
            tryTime = 27;
        } else if (sizeInBits < 512) {
            tryTime = 15;
        } else if (sizeInBits < 768) {
            tryTime = 8;
        } else if (sizeInBits < 1024) {
            tryTime = 4;
        } else {
            tryTime = 2;
        }
        return isPrime(n, tryTime);
    }

    /**
     * 多次调用素数测试, 判定输入的n是否为质数
     *
     * @param n
     * @param tryTime
     * @return
     */
    public static boolean isPrime(BigInteger n, int tryTime) {
        for (int i = 0; i < tryTime; i++) {
            if (!passesMillerRabin(n)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 产生一个n bit的素数
     *
     * @param bitCount
     * @return
     */
    public static BigInteger getPrime(int bitCount) {
        //随机生成一个n bit的大整数
        BigInteger init = new BigInteger(bitCount, ran);
        //如果n为偶数, 则加一变为奇数
        if (!init.testBit(0)) {
            init = init.setBit(0);
        }
        int i = 0;
        //基于素数定理, 平均只需要不到n次搜索, 就能找到一个素数
        while (!isPrime(init)) {
            i++;
            init = init.add(BigInteger.valueOf(2));
        }
        //System.out.println(String.format("try %d\ttimes", i));
        return init;
    }
}
