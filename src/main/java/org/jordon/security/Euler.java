package org.jordon.security;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Euler {

    /**
     * 获取2到num范围内的所有素数
     * @param num 范围
     * @return 2到num范围内的所有素数
     */
    private static List<Integer> getAllPrime(int num){
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 2; i < num; i++){
            if (isPrime(i)) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 判断一个数是否素数
     * @param num 待处理数
     * @return 是否素数
     */
    private static boolean isPrime(int num){
        if(num < 2) {
            return false;
        }
        for(int i = 2; i <= Math.sqrt(num); i++) {
            if(num % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean canBeDivided(int num, int i){
        return num != 1 && (num % i == 0);
    }

    private static int repeatDivide(int num, int i){
        int result;
        if (canBeDivided(num,i)){
            result = repeatDivide(num / i, i);
        }else{
            return num;
        }
        return result;
    }

    private static int Phi(int num) {
        int a = num;
        double eulerAnswer;
        ArrayList<Integer> eulerList = new ArrayList<>();
        if (isPrime(num)){
            eulerAnswer = num - 1;
        }else{
            List<Integer> allPrime = getAllPrime(num);
            for(int i : allPrime){
                int temp = num;
                num = repeatDivide(num,i);
                if (temp != num){
                    eulerList.add(i);
                }
            }
            eulerAnswer = a;
            for (int j : eulerList){
                eulerAnswer = eulerAnswer * (1 - (double)1 / j);
            }
        }
        return (int) Math.round(eulerAnswer);
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        System.out.println(Phi(num));
    }
}