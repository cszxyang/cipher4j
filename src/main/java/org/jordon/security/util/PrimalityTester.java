package org.jordon.security.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 素性测试工具
 */
public class PrimalityTester {
    /**
     * 判断一个数是否素数，使用米勒-拉宾素性测试
     * @param n 待测奇数，大于3
     * @param rounds 测试轮数
     * @return 判断结果
     */
    public static boolean isProbablePrime(long n, int rounds) {
        // 2是素数
        if (n == 2)
            return true;

        // n为偶数
        if ((n & 1) == 0)
            return false;

        // 把n-1写成(2^s)*d的形式
        long s = 0, d = n - 1, quotient, remainder;
        for (;;) {
            quotient = d / 2;
            remainder = d % 2;
            if (remainder == 1)
                break;
            s++;
            d = quotient;
        }

        Random rnd = ThreadLocalRandom.current();
        // 进行k次米勒-拉宾测试
        for (int i = 0; i < rounds; i++) {
            // n肯定是合数，结束，不然n可能是素数，继续测试
            long a = Math.abs(rnd.nextLong()) % (n - 1) + 1;
            long b = MathUtil.modExpNonRec(a, d , n);

            if (b == 1 || b == n - 1) {
                continue;
            }
            int j = 0;
            for (; j < s; j++) {
                b = MathUtil.modExpNonRec(b, 2, n);
                if (b == 1) {
                    return false;
                }
                if (b == n - 1)
                    break;
            }
            if (j == s) // None of the steps mad x equals n-1.
                return false;
        }
        return true;
    }
}
