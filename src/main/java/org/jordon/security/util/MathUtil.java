package org.jordon.security.util;

import java.util.concurrent.ThreadLocalRandom;

public class MathUtil {
    // 最大公约数
    public static long gcd(long m, long n) {
        while(true){
            if ((m = m % n) == 0)
                return n;
            if ((n = n % m) == 0)
                return m;
        }
    }

    /**
     * 求解线性同余方程 ax ≡ b(mod m)
     * @param a a
     * @param b b
     * @param m m
     * @return x
     */
    public static long linearCongruence(long a, long b, long m) {
        if(b % gcd(a, m) != 0)
            return - 1;
        /*
            通过扩展欧几里得算法求得x的逆元x'
            x = kx', b = k(a, m)
            所以要求地 x = (b / gcd(a, m)) * x'
         */
        long result = (b / gcd(a, m)) * extendedEuclid(a, m);
        if(result < 0)
            result += m;
        return result;
    }

    /**
     * 扩展欧几里得非递归实现
     * @param a 操作数
     * @param b 操作数
     */
    private static long extendedEuclid(long a, long b) {
        long x = 0, y = 1, lastX = 1, lastY = 0, temp;

        if (a < b) {
            temp = a;
            a = b;
            b = temp;
        }

        while (b != 0) {
            long q = a / b, r = a % b;

            a = b;
            b = r;

            temp = x;
            x = lastX - q * x;
            lastX = temp;

            temp = y;
            y = lastY - q * y;
            lastY = temp;
        }
        return lastY;
    }

    /**
     * 随机生成一个可能是素数的数
     * @param range 随机数产生范围
     * @return 一个可能是素数的数
     */
    public static long probablePrime(int range, int rounds) {
        if (range > 0 && rounds > 0) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            while (true) {
                int num = random.nextInt(range) + 2;
                // 进行rounds轮素性测试
                if (PrimalityTester.isProbablePrime(num, rounds))
                    return num;
            }
        }
        return  -1;
    }

    /**
     * 模幂运算（modular exponentiation）递归实现
     * @param x 底数
     * @param b 指数
     * @param n 模
     * @return x^b mod n
     */
    public static long modExp(long x, long b, long n) {
        if (b == 0) return 1;
        // use long for intermediate computations to eliminate overflow
        long t = modExp(x, b / 2, n);
        long c = (t * t) % n;
        if (b % 2 == 1)
            c = (c * x) % n;
        return c;
    }
    /**
     * 模幂运算（modular exponentiation）递归实现
     * @param x 底数
     * @param b 指数
     * @param n 模
     * @return x^b mod n
     */
    public static int exp(int x, int b, int n) {
        if (b == 1) {
            return x % n;
        }
        if ((b & 1) == 1) { // b是奇数
            return ((x % n) * exp(x, b - 1, n) % n);
        }
        // b是偶数
        long ans = exp(x, b / 2, n);
        return (int) (ans * ans) % n;
    }

    /** Function to calculate (a ^ b) % c **/
    public static long modExpNonRec(long a, long b, long c) {
        long res = 1;
        for (int i = 0; i < b; i++) {
            res *= a;
            res %= c;
        }
        return res % c;
    }
}
