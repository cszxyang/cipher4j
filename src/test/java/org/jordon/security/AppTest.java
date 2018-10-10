package org.jordon.security;

import static org.junit.Assert.assertTrue;

import org.jordon.security.core.CipherService;
import org.jordon.security.core.DesService;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testService() {
        CipherService cipherService = new DesService();
        try {
            String encryptedText = cipherService.encrypt("01234567", "12345678");
            cipherService.decrypt(encryptedText,"12345678");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    public void testGCD() {
        Random random = new Random();
        int[] ranges = {10, 100, 1000, 10000, 100000, Integer.MAX_VALUE};
        for (int range : ranges) {
            for (int i = 0; i < 1000; i++) {
                int m = random.nextInt(range);
                int n = random.nextInt(range);
                if (m == 0 && n == 0)
                    continue;
                System.out.println(String.format("%-50s%-50s%-50s", m, n, gcd(m, n)));
            }
        }
    }

    private static int gcd(int m, int n) {
        if (m == 0 || n == 0)
            return 0;
        while(true){
            if ((m = m % n) == 0)
                return n;
            if ((n = n % m) == 0)
                return m;
        }
    }

    @Test
    public void testIsPrime() {
        Random random = new Random();
        int[] ranges = {10, 100, 1000, 10000, 100000, Integer.MAX_VALUE};
        for (int range : ranges) {
            for (int i = 0; i < 1000; i++) {
                int m = random.nextInt(range);
                if (m < 2)
                    continue;
                System.out.println(String.format("%-50s%-50s", m, isPrime(m)));
            }
        }
    }

    private boolean isPrime(int a) {
        boolean isPrime = true;
        for(int i = 2 ; i <= Math.sqrt(a) ; i++){
            //Math.sqrt 是调用Math类中的sqrt方法，求一个数的平方根
            if(a % i == 0){
                isPrime = false;
                break;
            }
        }
        return isPrime;
    }

    private static long[] ex_gcd(long a, long b) {
        long ans;
        long[] result = new long[3];
        if(b == 0) {
            result[0] = a;
            result[1] = 1;
            result[2] = 0;
            return result;
        }
        long [] temp = ex_gcd(b,a % b);
        ans = temp[0];
        result[0] = ans;
        result[1] = temp[2];
        result[2] = temp[1] - (a / b) * temp[2];
        return result;
    }


    private int ModExp(int b, int n, int m){
        int exponent = 1;
        for (int i = 0; i < n ;i++) {
            exponent *= b;
        }
        return exponent % m;
    }

    @Test
    public void test() {
        char[] chars = new char[3];
        chars[0] = '1';
        chars[2] = 'i';
        System.out.println(Arrays.toString(chars));
    }
}
