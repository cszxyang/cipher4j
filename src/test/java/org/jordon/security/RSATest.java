package org.jordon.security;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

public class RSATest {

    @Test
    public void test() {
        long begin = System.currentTimeMillis();
        BigInteger bigInteger = BigInteger.probablePrime(1024, new Random());
        long end = System.currentTimeMillis();
        System.out.println((bigInteger.add(new BigInteger("-1"))).isProbablePrime(Integer.MAX_VALUE));
        System.out.println((end - begin) + " milliseconds spent");
        System.out.println(bigInteger.toString(2));
    }

    @Test
    public void testM() {
        BigInteger a = new BigInteger("4").pow(2).mod(new BigInteger("6"));

        System.out.println(a.toString(10));
    }

    @Test
    public void tes() {

        int[] as = {3337, 688, 232, 687, 966, 668};
        for (int i : as) {
            System.out.println(Integer.toBinaryString(i).length());
        }
    }
}
