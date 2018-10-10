package org.jordon.security;

import static org.junit.Assert.assertTrue;

import org.jordon.security.core.CipherService;
import org.jordon.security.core.DesService;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
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

    @Test
    public void test1() {
        System.out.println(test2(403, 1.375f));
    }

    private double R = 8.314f, a = 9387900, b = 90.460f;

    private double test2(int T, float P) {
        for (int V = 0 ; V < 500000 ; V ++) {
            int p = statusCal(T, V);
            System.out.println(p);
            if ((int)P == p)
                return V;
        }
        return 0;
    }

    private int statusCal(int T, int V) {
        return (int) (R * T / (V - b) -
                                a / (V * (V + b) + b * (V - b)));
    }

    private double test(double T, double P) {
        double V, Z = 1, V1, Z1, fv, _fv;

        for ( ; ; ) {
            V = Z * R * T / P;
            fv = fv(P, T, V);
            _fv = _fv(T, V);

            V1 = V - fv / _fv;
            Z1 = P * V1 / (R * T);
            if (Math.abs(Z1 - Z) < 0.0001) {
                return Z * R * T / P;
            }else {
                Z = Z1;
            }
        }
    }

    private double fv(double P, double T, double V) {
        return  P + a / (Math.sqrt(T) * V * (V + b)) - R * T / (V - b);
    }

    private double _fv(double T, double V) {
        return R * T / ((V - b) * (V - b)) - a * (2 * V + b)
                / (Math.sqrt(T) * V * V * (V + b) * (V + b));
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
    public void testService() {
        CipherService cipherService = new DesService();
        try {
            cipherService.encrypt("01234567", "12345678");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
