package org.jordon.security.core;

import org.jordon.security.constant.AESConstants;
import org.jordon.security.util.ArrayUtil;
import org.jordon.security.util.Base64Util;

import static org.jordon.security.util.ArrayUtil.transferToShorts;

public class AESCipherService implements CipherService {

    /**
     * 对外的解密接口
     * public decryption api
     * @param encryptedText encrypted text
     * @param key key
     * @return decrypted text
     */
    @Override
    public String decrypt(String encryptedText, String key) {
        System.out.println("\n\n#####################  decryption  #####################");
        ArrayUtil.printInfo("encrypted text", encryptedText, false);
        ArrayUtil.printInfo("key text", key, false);

        short[][] initialTextState = transfer(Base64Util.decodeToShorts(encryptedText));
        short[][] initialKeyState = transfer(transferToShorts(key));

        ArrayUtil.printInfo("initial encrypted state", getStateHex(initialTextState), false);
        ArrayUtil.printInfo("initial key state", getStateHex(initialKeyState), true);

        short[][] decryptState = coreDecrypt(initialTextState, initialKeyState);
        String plaintext = getOrigin(decryptState);
        ArrayUtil.printInfo("plaintext", plaintext, false);
        return plaintext;
    }

    /**
     * 将最终解密的short数组还原为字符串
     * transfer state into string
     * @param decryptState decrypt state
     * @return plaintext string
     */
    private String getOrigin(short[][] decryptState) {
        StringBuilder builder = new StringBuilder();
        for (short[] shorts : decryptState) {
            for (short s : shorts) {
                builder.append(String.valueOf((char) s));
            }
        }
        return builder.toString();
    }

    /**
     * 解密逻辑：通过将可逆操作抽取成可逆矩阵, 复用加密核心函数
     * @param encryptedTextState initial encrypted text state
     * @param keyState initial key state
     * @return decrypted state
     */
    private short[][] coreDecrypt(short[][] encryptedTextState, short[][] keyState) {
        // obtain raw round keys
        short[][] rawRoundKeys = generateRoundKeys(keyState);
        System.out.println("RoundKeys");
        printRoundKeys(rawRoundKeys);

        // make it easier to obtain a whole block of round key in a round transformation
        short[][][] roundKeys = transfer(rawRoundKeys);

        for (int i = 1; i < roundKeys.length - 1; i++) {
            roundKeys[i] = mixColumns(roundKeys[i], AESConstants.INVERSE_CX);
        }

        short[][][] inverseRoundKeys = inverseRoundKeys(roundKeys);
        System.out.println("inverse roundKeys");
        printRoundKeys(inverseRoundKeys);
        return coreEncrypt(encryptedTextState, inverseRoundKeys, AESConstants.
                INVERSE_SUBSTITUTE_BOX, AESConstants.INVERSE_CX, AESConstants.INVERSE_SHIFTING_TABLE);
    }

    /**
     * [解密] 将解密扩展密钥数组逆转，方便复用核心加密操作，
     * @param roundKeys 解密扩展密钥数组
     * @return 逆转了的解密扩展密钥数组
     */
    private short[][][] inverseRoundKeys(short[][][] roundKeys) {
        short[][][] result = new short[roundKeys.length][4][4];
        int length = roundKeys.length;
        for (int i = 0; i < roundKeys.length; i++) {
            result[i] = roundKeys[length - 1 - i];
        }
        return result;
    }

    /**
     * 对外的加密接口
     * public encryption api
     * @param plaintext text to be encrypted
     * @param key key
     * @return encryption result
     */
    @Override
    public String encrypt(String plaintext, String key) {
        // transfer plaintext and key from one-dimension matrix
        // to (data.length / 4) x 4 matrix
        System.out.println("#####################  encryption  #####################");
        ArrayUtil.printInfo("plaintext text", plaintext, false);
        ArrayUtil.printInfo("key text", key, false);
        short[][] initialPTState = transfer(transferToShorts(plaintext));
        ArrayUtil.printInfo("initial plaintext state", getStateHex(initialPTState), false);
        short[][] initialKeyState = transfer(transferToShorts(key));
        ArrayUtil.printInfo("initial key state", getStateHex(initialKeyState), true);

        // obtain raw round keys
        short[][] rawRoundKeys = generateRoundKeys(initialKeyState);
        System.out.println("RoundKeys");
        printRoundKeys(rawRoundKeys);

        // make it easier to obtain a whole block of round key in a round transformation
        short[][][] roundKeys = transfer(rawRoundKeys);

        short[][] finalState = coreEncrypt(initialPTState, roundKeys, AESConstants.SUBSTITUTE_BOX,
                                        AESConstants.CX, AESConstants.SHIFTING_TABLE);
        return Base64Util.encode(transfer2Bytes(finalState));
    }

    /**
     * AES核心操作，通过将可逆操作抽取成可逆矩阵作为参数，使该方法能在加/解密操作中复用
     * @param initialPTState    明文或密文的状态数组
     * @param roundKeys     加/解密要用到的轮密钥数组
     * @param substituteTable   加/解密要用到的S盒
     * @param mixColumnTable    列混合中用来取代既约多项式的数组
     * @param shiftingTable    行变换中用来决定字间左移的位数的数组
     * @return 加/解密结果
     */
    private short[][] coreEncrypt(short[][] initialPTState,
                                 short[][][] roundKeys, short[][] substituteTable,
                                 short[][] mixColumnTable, short[][] shiftingTable) {

        // 初始轮密钥加，异或操作
        short[][] state = xor(roundKeys[0], initialPTState);

        // 处理前九轮变换
        for (int i = 0; i < 9; i++) {
            System.out.println("N = " + (i + 1));
            // 将状态数组的字节替换为S盒中相应位置的字节
            state = substituteState(state, substituteTable);
            ArrayUtil.printInfo("SubBytes", getStateHex(state), false);

            // 行移位变换
            state = shiftRows(state, shiftingTable);
            ArrayUtil.printInfo("ShiftRows", getStateHex(state), false);

            // 列混合变换
            state = mixColumns(state, mixColumnTable);
            ArrayUtil.printInfo("MixColumns", getStateHex(state), false);

            // 轮密钥加变换
            ArrayUtil.printInfo("RoundKey", getStateHex(roundKeys[i + 1]), false);
            state = xor(roundKeys[i + 1], state);
            ArrayUtil.printInfo("AddRoundKeys", getStateHex(state), true);
        }

        // 处理最后一轮
        System.out.println("N = 10");
        state = substituteState(state, substituteTable);
        ArrayUtil.printInfo("SubBytes", getStateHex(state), false);

        state = shiftRows(state, shiftingTable);
        ArrayUtil.printInfo("ShiftRows", getStateHex(state), false);

        ArrayUtil.printInfo("RoundKey", getStateHex(roundKeys[roundKeys.length - 1]), false);
        state = xor(roundKeys[roundKeys.length - 1], state);
        ArrayUtil.printInfo("AddRoundKeys", getStateHex(state), false);
        return state;
    }

    /**
     * 将加密后得到的状态数组转成字节数组，便于进行Base64编码
     * @param finalState 加密后得到的状态数组
     * @return 状态数组对应的字节数组
     */
    private byte[] transfer2Bytes(short[][] finalState) {
        byte[] result = new byte[finalState.length * 4];
        for (int i = 0;i < finalState.length; i++) {
            for (int j = 0; j < 4; j++) {
                result[i * 4 + j] = (byte) (finalState[i][j] & 0xff);
            }
        }
        return result;
    }

    /**
     * 列混合变换：状态数组与多项式等价矩阵进行有限域GF(2)上的矩阵乘法
     * @param state 状态数组
     * @param table 多项式等价矩阵
     * @return 列混合变换后的新状态
     */
    private short[][] mixColumns(short[][] state, short[][] table) {
        short[][] result = new short[state.length][4];
        for (int i = 0; i < state.length; i++) {
            result[i] = matrixMultiply(state[i], table);
        }
        return result;
    }

    /**
     * 一个字与多项式等价数组在有限域GF(2)上的乘法操作
     * multiplication between a word of a state and a irreducible
     * polynomial <tt>C(x)=03x^3+01x^2+01^2+01x+02</tt> which is replaced as a
     * constant table <tt>AESConstants.CX</tt>
     * (aes-128: 4x4 x 4x1 = 4x1)
     * @param aWord a word of a state
     * @return multiplication result, a new word
     */
    private short[] matrixMultiply(short[] aWord, short[][] table) {
        short[] result = new short[4];
        for (int i = 0; i < 4; i++) {
            result[i] = wordMultiply(table[i], aWord);
        }
        return result;
    }

    /**
     * 两个字在有限域GF(2)上的乘法操作
     * multiplication between two words
     * @param firstWord first operand
     * @param secondWord second operand
     * @return multiplication result, a byte actually
     */
    private short wordMultiply(short[] firstWord, short[] secondWord) {
        short result = 0;
        for (int i=0; i < 4; i++) {
            result ^= multiply(firstWord[i], secondWord[i]);
        }
        return result;
    }

    /**
     * 有限域GF(2)上的乘法操作，通过分解操作数将之转化成有限域GF(2)上的倍乘操作
     * multiplication in finite field GF(2^8)
     * @param a an operand of this kind of multiplication
     * @param b another operand of this kind of multiplication
     * @return multiplication result
     */
    private short multiply(short a, short b) {
        short temp = 0;
        while (b != 0) {
            if ((b & 0x01) == 1) {
                temp ^= a;
            }
            a <<= 1;
            if ((a & 0x100) > 0) {
                /*
                    judge if a is greater than 0x80, if then subtract a
                    irreducible polynomial which can be substituted by 0x1b
                    cause addition and subtraction are equivalent in this case
                    it's okay to xor 0x1b
                 */
                a ^= 0x1b;
            }
            b >>= 1;
        }
        return (short) (temp & 0xff);
    }

    /**
     * 轮密钥扩展：将1个状态长度的主密钥扩展成<tt>rounds + 1</tt>个状态长度的轮密钥数组
     * generation of round keys
     * @param originalKey original cipher key
     * @return round keys
     */
    private short[][] generateRoundKeys(short[][] originalKey) {
        short[][] roundKeys = new short[44][4];
        int keyWordCount = originalKey.length;
        // 1. copy the original cipher words into the first four words of the roundKeys
        System.arraycopy(originalKey, 0, roundKeys, 0, keyWordCount);
        // 2. extension from previous word
        for (int i = keyWordCount; i < keyWordCount * 11; i++) {
            short[] temp = roundKeys[i - 1];
            if (i % keyWordCount == 0) {
                temp = xor(substituteWord(leftShift(temp)), AESConstants.R_CON[i / keyWordCount]);
            }
            roundKeys[i] = xor(roundKeys[i - keyWordCount], temp);
        }
        return roundKeys;
    }

    /**
     * 状态替代：对状态中的每个字进行字替代
     * substitute value of a state array using byte as unit
     * @param state state array to be substituted
     * @return substitution result, a new state array
     */
    private short[][] substituteState(short[][] state, short[][] substituteTable) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < 4 ; j++) {
                state[i][j] = substituteByte(state[i][j], substituteTable);
            }
        }
        return state;
    }

    /**
     * 字替代：对字中每个字节进行字节替代
     * substitute all bytes in a word through SBox
     * @param aWord a word, aka 4 bytes
     * @return substitution result, a new and disrupted word
     */
    private short[] substituteWord(short[] aWord) {
        for (int i = 0; i < 4; i++) {
            aWord[i] = substituteByte(aWord[i], AESConstants.SUBSTITUTE_BOX);
        }
        return aWord;
    }

    /**
     * 字节替代： 取一个字的高四位和低四位分别作为S盒的行号和列号，
     *          通过行列号取S盒中的字节替代原字节
     * substitute value of a byte through <tt>SBox</tt>
     * @param originalByte byte to be substituted
     * @return substitution result, a new byte
     */
    private short substituteByte(short originalByte, short[][] substituteTable) {
        // low 4 bits in a originByte
        int low4Bits = originalByte & 0x000f;
        // high 4 bits in a originByte
        int high4Bits = (originalByte >> 4) & 0x000f;
        // obtain value in <tt>AESConstants.SUBSTITUTE_BOX</tt>
        return substituteTable[high4Bits][low4Bits];
    }

    /**
     * 行移位变换：对状态的行进行循环左移，左移规则在<tt>shiftingTable</tt>中定义
     * row shifting operation, rotate over N which is defined in
     * <tt>AESConstants.SHIFTING_TABLE</tt> bytes of corresponding rows
     * @param state state array of the original plaintext
     * @return a new state array
     */
    private static short[][] shiftRows(short[][] state, short[][] shiftingTable) {
        short[][] result = new short[state.length][4];
        for (int j = 0; j < 4; j++) {  // local byte in a word
            for (int i = 0; i < state.length; i++) {  // local word
                result[i][j] = state[shiftingTable[i][j]][j];
            }
        }
        return result;
    }

    /**
     * 以字节为单位循环左移1个单位, <tt>LEFT_SHIFT_TABLE</tt> 决定移动的规则
     * loop left shift a byte in a word, LEFT_SHIFT_TABLE decides how to move
     * @param aWord 操作字
     * @return 循环左移操作结果
     */
    private short[] leftShift(short[] aWord) {
        short[] result = new short[4];
        for (int i = 0; i < 4; i++) {
            result[i] = aWord[AESConstants.LEFT_SHIFT_TABLE[i]];
        }
        return result;
    }

    /**
     * 将二维数组转成三维数组，方便在轮变换中通过轮下标获取 Nk x 4 的轮密钥矩阵
     * make it easier to process several specific blocks of the origin arrays
     * @param origin 二维轮密钥数组
     * @return 三维轮密钥数组
     */
    private short[][][] transfer(short[][] origin) {
        short[][][] result = new short[origin.length / 4][4][4];
        for (int i = 0; i < origin.length / 4; i++) {
            short[][] temp = new short[4][4];
            System.arraycopy(origin, i * 4, temp, 0, 4);
            result[i] = temp;
        }
        return result;
    }

    /**
     * 将<tt>1 x modeSize</tt>矩阵转成<tt>Nb x 4</tt>状态矩阵，通常用在获取明文和密钥的初始状态数组
     * transfer short[] to short[][], usually return an initial state
     * @param origin origin array short[]
     * @return transferred array
     */
    private short[][] transfer(short[] origin) {
        short[][] result = new short[origin.length / 4][4];
        for (int i = 0; i < result.length; i++) {
            System.arraycopy(origin, i * 4, result[i], 0, 4);
        }
        return result;
    }

    /**
     * 有限域GF(2)上的加法，<tt>modeSize</tt>位的异或操作
     * xor corresponding byte in two state arrays
     * @param first first operand
     * @param second second operand
     * @return xor result
     */
    private short[][] xor(short[][] first, short[][] second) {
        short[][] result = new short[first.length][4];
        int length = first.length;
        for (short i = 0; i < length; i++) {
            for (short j = 0; j < length; j++) {
                result[i][j] = (short) (first[i][j] ^ second[i][j]);
            }
        }
        return result;
    }

    /**
     * 两个字间的异或操作
     * xor corresponding byte in two words
     * @param first first operand
     * @param second second operand
     * @return xor result
     */
    private short[] xor(short[] first, short[] second) {
        short[] result = new short[4];
        for (short i = 0; i < 4; i++) {
            result[i] = (short) (first[i] ^ second[i]);
        }
        return result;
    }

    /**
     * 状态数组的十六机制串
     * obtain state array
     * @param state a state
     * @return hex string of state
     */
    private String getStateHex(short[][] state) {
        StringBuilder builder = new StringBuilder();
        for (short[] aWord : state) {
            builder.append(toHexString(aWord));
        }
        return builder.toString();
    }

    /**
     * 打印轮密钥数组
     * print round keys
     * @param roundKeys round keys
     */
    private void printRoundKeys(short[][] roundKeys) {
        for (int i = 0, keyOrder = 1; i < roundKeys.length; i += 4, keyOrder++) {
            String infoKValue = getStateHex(new short[][]{
                    roundKeys[i], roundKeys[i + 1],
                    roundKeys[i + 2], roundKeys[i + 3]
            });
            ArrayUtil.printInfo("[RoundKey " + keyOrder + "]", infoKValue, false);
        }
        System.out.println();
    }

    /**
     * 打印三维轮密钥数组
     * @param roundKeys round keys
     */
    private void printRoundKeys(short[][][] roundKeys) {
        for (int i = 0; i < roundKeys.length; i++) {
            String infoKValue = getStateHex(roundKeys[i]);
            ArrayUtil.printInfo("[RoundKey " + (i + 1) + "]", infoKValue, false);
        }
        System.out.println();
    }

    /**
     * 将一个字以字节为单位转换成十六进制形式字符串
     * format a word in hexadecimal string
     * @param aWord a word
     * @return hex string of a word
     */
    private String toHexString(short[] aWord) {
        StringBuilder builder = new StringBuilder();
        for (short aByte : aWord) {
            builder.append(toHexString(aByte));
        }
        return builder.toString();
    }

    /**
     * 获取一个字节的十六进制串
     * @param value a byte
     * @return hex string of a byte
     */
    private String toHexString(short value) {
        String hexString = Integer.toHexString(value);
        if (hexString.toCharArray().length == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
