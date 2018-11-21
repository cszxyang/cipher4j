package org.jordon.security.attack.statistics;

import org.jordon.security.core.crypto.symmetry.DESCipherService;

import java.security.SecureRandom;

public class DESStatistics {

    private DESCipherService desService;

    private int testCount;

    public DESStatistics(int testCount) {
        this.desService = new DESCipherService(false);
        this.testCount = testCount;
    }

    /**
     * 顺序统计明文/密钥改变n位，对应密文改变位数
     */
    public void run() {
        countRounds(testCount);
    }

    /**
     * 进行n次测试统计，取平均值（明文&密钥变化）
     * @param testCount 测试次数
     */
    private void countRounds(int testCount) {
        double[][] keyChangeResult = new double[testCount][64];
        double[][] plaintextChangeResult = new double[testCount][64];

        for (int i = 0; i < testCount; i++) {
            keyChangeResult[i] = countOneRound(true);
            plaintextChangeResult[i] = countOneRound(false);
        }

        double[] keyAvg = new double[64];
        double[] plaintextAvg = new double[64];

        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < testCount; j++) {
                keyAvg[i] += keyChangeResult[j][i];
                plaintextAvg[i] += plaintextChangeResult[j][i];
            }
            keyAvg[i] /= testCount;
            plaintextAvg[i] /= testCount;
        }

        for (int i = 0 ; i < testCount; i++) {
            double avg4Key = 0;
            System.out.println("第" + (i + 1) + "次测试");
            System.out.println(String.format("%-25s%-30s", "密钥改变位数", "密文改变位数"));
            for (int j = 0; j < 64; j++) {
                System.out.println(String.format("%-30s%-30s", (j + 1),
                        keyChangeResult[i][j]));
                avg4Key += keyChangeResult[i][j];
            }
            System.out.println("均值：" + (avg4Key / 64) + "\n");
        }

        System.out.println(testCount + "次密钥改变， 密文位数改变均值");
        System.out.println(String.format("%-25s%-30s", "密钥改变位数", "密文平均改变位数"));
        for (int j = 0; j < 64; j++) {
            System.out.println(String.format("%-30s%-30s", (j + 1), keyAvg[j]));
        }


        for (int i = 0 ; i < testCount; i++) {
            double plaintAvg = 0;
            System.out.println("第" + (i + 1) + "次测试");
            System.out.println(String.format("%-25s%-30s", "明文改变位数", "密文改变位数"));
            for (int j = 0; j < 64; j++) {
                System.out.println(String.format("%-30s%-30s", (j + 1),
                        plaintextChangeResult[i][j]));
                plaintAvg += plaintextChangeResult[i][j];
            }
            System.out.println("均值：" + (plaintAvg / 64) + "\n");
        }

        System.out.println("统计" + testCount + "次明文改变， 密文位数改变均值");
        System.out.println(String.format("%-25s%-30s", "明文改变位数", "密文平均改变位数"));
        for (int j =0; j < 64; j++) {
            System.out.println(String.format("%-30s%-30s", (j + 1), plaintextAvg[j]));
        }
    }

    /**
     * 统计密钥/明文改变1, 2,..., 64位，密文相应改变的位数
     * @param processKeyChange 为了复用该方法， 标记位改变的是明文还是密钥
     * @return  密钥/明文改变64次，密文相应改变的位数
     */
    private double[] countOneRound(boolean processKeyChange) {
        char[] plaintext = ran64Bits();
        char[] originKey = ran64Bits();
        char[] firstResult = desService.encode(plaintext, desService.generateSubKeys(originKey));

        // 用来存放每次位数变化，相应数据（明文或密钥）变化的位数
        double[] changedBits = new double[64];
        for (int i = 1; i <= 64; i++) {
            char[] negation, result;
            if (processKeyChange) { // 处理密钥位变化
                negation = bitNegation(i, originKey);
                result = desService.encode(plaintext, desService.generateSubKeys(negation));
            }else { // 处理明文位变化
                negation = bitNegation(i, plaintext);
                result = desService.encode(negation, desService.generateSubKeys(originKey));
            }
            changedBits[i - 1] = compare(firstResult, result);
        }
        return changedBits;
    }

    /**
     * 对比第一次加密得到的密文和明文/密钥改变n位得到的密文， 统计差异位数
     * @param firstResult 第一次加密得到的密文
     * @param changedResult 明文/密钥改变n位得到的密文
     * @return 两者之间差异的位数
     */
    private int compare(char[] firstResult, char[] changedResult) {
        int counter = 0;
        for (int i = 0; i < 64; i++) {
            if (firstResult[i] != changedResult[i]) {
                counter ++;
            }
        }
        return counter;
    }

    /**
     * 将原比特字符数组的相应位数”逻辑取反“
     * @param bitCount 从高位数起要取反的位数
     * @param origin 比特字符数组（明文或密钥）
     * @return 取反结果
     */
    private char[] bitNegation(int bitCount, char[] origin) {
        char[] result = new char[64];
        for (int i = 0; i < bitCount; i++) {
            result[i] = bitNegation(origin[i]);
        }
        System.arraycopy(origin, bitCount, result, bitCount, 64 - bitCount);
        return result;
    }

    /**
     * 随机产生64位数据
     * @return 64位数据组
     */
    private char[] ran64Bits() {
        char[] result = new char[64];
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 64; i++) {
            result[i] = secureRandom.nextBoolean() ? '1' :'0';
        }
        return result;
    }

    /**
     * 比特位取反
     * @param bit char类型的比特数据
     * @return 取反后的比特数据
     */
    private char bitNegation(char bit) {
        return bit == '0' ? '1' : '0';
    }
}
