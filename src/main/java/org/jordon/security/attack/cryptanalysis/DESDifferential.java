package org.jordon.security.attack.cryptanalysis;

import org.jordon.security.constant.DESConstants;
import org.jordon.security.util.ArrayUtil;

public class DESDifferential {

    private short inputDiff;

    public DESDifferential(short inputDiff) {
        this.inputDiff = inputDiff;
    }

    /**
     * differential cryptanalysis
     */
    public void analyze() {
        // 选定输入查分为000001
        short[][] diffPairs = genDiffInputPairs(inputDiff);
        if (diffPairs != null) {
            printPairs(diffPairs);
            // 经过S盒子
            System.out.println("\n输出差分");

            StringBuilder[] builders = new StringBuilder[16];
            for (int i = 0; i < builders.length; i++) {
                builders[i] = new StringBuilder();
            }
            for (int i = 1; i <= diffPairs.length; i++) {
                short s = (short) (sub(getBinStr(diffPairs[i - 1][0])) ^ sub(getBinStr(diffPairs[i - 1][1])));
                String symbol = i % 8 == 0 ? "\n" : " ";
                System.out.print(getOutput(s) + symbol);

                builders[s].append(getOutput(s)).append(" ");
            }
            System.out.println("\n差分输出分布");
            for (int i = 0; i < builders.length; i++){
                ArrayUtil.printInfo(getOutput((short) i), builders[i].toString(), false);
            }
        }
    }

    private String getOutput(short output) {
        if (output == 0) {
            return "0000";
        }else if (output == 1) {
            return "0001";
        }
        return Integer.toBinaryString((output & 0b1111) + 0b10000).substring(1, 5);
    }

    private short sub(String val) {
        char[] chars = val.toCharArray();
        char[] rowBits = {
                chars[0],
                chars[5]
        };
        char[] columnBits = {
                chars[1], chars[2],
                chars[3], chars[4]
        };

        int rowIndex = Integer.parseInt(String.valueOf(rowBits), 2);
        int columnIndex = Integer.parseInt(String.valueOf(columnBits), 2);
        // (3) obtain output of Si
        return DESConstants.SUBSTITUTE_BOX[0][rowIndex][columnIndex];
    }

    /**
     * 根据指定输入差分，生成所有明文输入对
     */
    private short[][] genDiffInputPairs(short inputDiff) {
        // 2^6
        System.out.println("输入差分：" + getBinStr(inputDiff));
        if (inputDiff >= 0x00 && inputDiff <= 0x0f) {
            short[] pair = {0, inputDiff};
            short[][] pairs = new short[64][2];
            pairs[0] = pair;

            for (short i = 1; i < 64; i++) {
                pairs[i] = new short[]{i, (short) (i ^ inputDiff)};
            }
            return pairs;
        }
        return null;
    }

    private void printPairs(short[][] pairs) {
        System.out.println("\n输入对");
        for (int i = 1; i <= pairs.length; i++) {
            String symbol = i % 8 == 0 ? "\n" : " ";
            System.out.print("(" + getBinStr(pairs[i - 1][0]) +
                    "," + getBinStr(pairs[i - 1][1]) + ")" + symbol);
        }
    }

    private String getBinStr(short val) {
        if (val == 0) {
            return "000000";
        }else if (val == 1) {
            return "000001";
        }
        return Integer.toBinaryString((val & 0b111111) + 0b1000000).substring(1, 7);
    }
}
