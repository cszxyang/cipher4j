package org.jordon.security.core.crypto.symmetry;

import org.jordon.security.constant.DESConstants;
import org.jordon.security.core.crypto.CipherService;
import org.jordon.security.util.ArrayUtil;
import org.jordon.security.util.Base64Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DESCipherService implements CipherService {

    private boolean printMsg;
    private List<String> messages;
    private String workingMode;
    private char[] IV;
    private char[][] counters;

    public List<String> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }

    public DESCipherService(boolean printMsg) {
        this.printMsg = printMsg;
        this.messages = new ArrayList<>();
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode;
    }

    /**
     * 明文填充，最后一组的最后8位表示填充长度
     */
    public char[][] padPlaintext(char[] plaintextBytes) {

        System.out.println("填充前的比特串： " + String.valueOf(plaintextBytes));
        int length = plaintextBytes.length;
        int lastBlockBits = length % 64;
        // 现有块数
        int blockCount = length / 64;

        // 待伪随机填充位数
        int randPadCount = 64 - lastBlockBits - 8;
        char[] padded = new char[(blockCount + 1) * 64];
        System.arraycopy(plaintextBytes, 0, padded, 0, length);

        // 伪随机填充
        for (int i = length; i < padded.length - 8; i++) {
            padded[i] = new Random().nextBoolean() ? '1' : '0';
        }
        // 填充最后一个字节比特串
        char[] padCountVal = Integer.toBinaryString(
                (randPadCount & 0xff) + 0x0100).substring(1).toCharArray();
        System.arraycopy(padCountVal, 0, padded, padded.length - 8, 8);

        System.out.println("填充后的比特串： " + String.valueOf(padded));

        // 将填充完比特穿分组
        char[][] blocks = new char[blockCount + 1][64];
        for (int i = 0; i < blockCount + 1; i++) {
            System.arraycopy(padded, i * 64, blocks[i], 0, 64);
        }
        return blocks;
    }


    /**
     * encrypt plaintext with key
     * @param plaintext text to be encrypted
     * @param key key
     * @return encrypted text
     * @throws UnsupportedEncodingException caused by String.getBytes()
     */
    @Override
    public String encrypt(String plaintext, String key) throws UnsupportedEncodingException {
        if (printMsg) {
            messages.add(ArrayUtil.printInfo("plaintext", plaintext, false));
            messages.add(ArrayUtil.printInfo("keyText", key, true));
        }
        char[] keyBytes = ArrayUtil.bytesToChars(key.getBytes("UTF-8"));
        // 获取填充后的所有分组

        char[] plaintextBytes = ArrayUtil.bytesToChars(plaintext.getBytes());
        return encrypt(plaintextBytes, keyBytes);
    }

    /**
     * 对文件进行加密
     * @param file 文件
     * @param key 密钥
     * @return 加密结果
     */
    public String encryptFile(File file, String key) throws UnsupportedEncodingException, FileNotFoundException {
        if (printMsg) {
            messages.add(ArrayUtil.printInfo("fileName", file.getName(), false));
            messages.add(ArrayUtil.printInfo("keyText", key, true));
        }
        char[] keyBytes = ArrayUtil.bytesToChars(key.getBytes("UTF-8"));
        // 获取填充后的所有分组
        char[] plaintextBytes = ArrayUtil.bytesToChars(ArrayUtil.getBytes(file));
        return encrypt(plaintextBytes, keyBytes);
    }

    public String encrypt(char[] plaintextBytes, char[] keyBytes) {
        // 填充、分组
        char[][] blocks = padPlaintext(plaintextBytes);

        int blockCount = blocks.length;
        char[] cipherVal = new char[blocks.length * 64];

        switch (workingMode) {
            case "ECB":
                // 对一块进行加密
                for (int i = 0; i < blockCount; i++) {
                    char[] aCipher = encryptABlock(blocks[i], keyBytes);
                    System.arraycopy(aCipher, 0, cipherVal, i * 64, 64);
                }
                break;

            case "CBC":
                // 初始向量IV
                char[] IV = ranBlock();
                this.IV = IV;
                char[] input;
                for (int i = 0; i < blocks.length; i++) {
                    input = ArrayUtil.xor(IV, blocks[i], 64);
                    char[] aCipher = encryptABlock(input, keyBytes);
                    IV = aCipher;
                    System.arraycopy(aCipher, 0, cipherVal, i * 64, 64);
                }
                break;

            case "CTR":
                // 初始化blockCount个计数器
                counters = new char[blockCount][64];
                for (int i = 0; i < blockCount; i++) {
                    counters[i] = ranBlock();
                }
                // 加密过程
                for (int i = 0; i < blocks.length; i++) {
                    char[] aCipher = ArrayUtil.xor(blocks[i], encryptABlock(counters[i], keyBytes), 64);
                    System.arraycopy(aCipher, 0, cipherVal, i * 64, 64);
                }
                break;
        }
        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("cipher blocks bits", cipherVal));
        }
        ArrayUtil.printBitChars("cipherVal", cipherVal);
        return Base64Util.encode(cipherVal);
    }

    /**
     * 产生随机64位比特串
     * @return 64位比特串
     */
    private char[] ranBlock() {
        char[] result = new char[64];
        for (int i = 0; i < 64; i++) {
            result[i] = new Random().nextBoolean() ? '1' : '0';
        }
        return result;
    }

    /**
     * 单块加密
     * @param plaintextBytes 明文分组（64位）
     * @param keyBytes 密钥串
     * @return 加密结果
     */
    private char[] encryptABlock(char[] plaintextBytes, char[] keyBytes) {
        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("plaintext bits", plaintextBytes));
            messages.add(ArrayUtil.printBitChars("key bits", keyBytes));
        }

        char[][] subKeys = generateSubKeys(keyBytes);
        char[] result = encode(plaintextBytes, subKeys);

        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("encryptedText bits", result));
        }
        return result;
    }

    /**
     * main encryption logic
     * @param plaintextBytes plaintext bits in chars format
     * @param subKeys subKeys bits in chars format
     * @return encryption result bits in chars format
     */
    public char[] encode(char[] plaintextBytes, char[][] subKeys) {
        System.out.println(plaintextBytes.length);
        // initial permutation, get 64 bit disrupted array
        char[] chars = ArrayUtil.disruptArray(plaintextBytes, DESConstants.IP);
        if (printMsg) {
            ArrayUtil.printBitChars("plaintext after ip", chars);
        }

        int length = chars.length;
        String binaryArrayStr = String.valueOf(chars);
        char[] left = binaryArrayStr.substring(0, length / 2).toCharArray();
        char[] right = binaryArrayStr.substring(length / 2).toCharArray();
        char[] coreEncrypted, xorResult;

        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("L0", left));
            messages.add(ArrayUtil.printBitChars("R0", right));
        }

        for (int i = 0; i < 16; i++) {
            if (printMsg) {
                System.out.println();
                messages.add("\n");
            }

            coreEncrypted = coreEncrypt(right, subKeys[i]);

            if (printMsg) {
                messages.add(ArrayUtil.printBitChars("[f] " +
                        "P Replacement", coreEncrypted));
            }
            // get 32-bit array
            xorResult = String.valueOf(ArrayUtil.xor(left, coreEncrypted, 48))
                    .substring(16).toCharArray();

            left = right;
            right = xorResult;

            if (printMsg){
                messages.add(ArrayUtil.printBitChars("["  + (i + 1) +  "] " + "left", left));
                messages.add(ArrayUtil.printBitChars("["  + (i + 1) +  "] " + "right", right));
            }
        }
        if (printMsg) {
            messages.add("\n");
            System.out.println();
        }
        char[] calResult = ArrayUtil.concat(right, left);
        return ArrayUtil.disruptArray(calResult, DESConstants.inverseIP);
    }

    /**
     * decrypt encrypted text with key
     * @param encryptedText encrypted text
     * @param key key
     * @return decrypted origin plaintext
     * @throws UnsupportedEncodingException caused by String.getBytes()
     */
    @Override
    public String decrypt(String encryptedText, String key) throws UnsupportedEncodingException {
        if (printMsg) {
            messages.add(ArrayUtil.printInfo("encryptedText", encryptedText, false));
            messages.add(ArrayUtil.printInfo("key", key, true));
        }

        // 密文串
        char[] encryptedTextBytes = Base64Util.decodeToChars(encryptedText);
        // 密文分组
        char[][] cipherBlocks = new char[encryptedTextBytes.length / 64][64];

        char[] keyBytes = ArrayUtil.bytesToChars(key.getBytes("UTF-8"));
        char[] decryptBytes = new char[cipherBlocks.length * 64];

        // 处理工作模式
        switch (workingMode) {
            case "ECB":
                for (int i = 0; i < cipherBlocks.length; i++) {
                    // 将密文串分组
                    System.arraycopy(encryptedTextBytes, i * 64, cipherBlocks[i], 0, 64);
                    // 单块解密
                    char[] aBlockResult = decryptABlock(cipherBlocks[i], keyBytes);
                    // 解密结果串成一串
                    System.arraycopy(aBlockResult, 0, decryptBytes, i * 64, 64);
                }
                break;

            case "CBC":
                char[] input = IV;
                for (int i = 0; i < cipherBlocks.length; i++) {
                    // 将密文串分组
                    System.arraycopy(encryptedTextBytes, i * 64, cipherBlocks[i], 0, 64);
                    // 单块解密
                    char[] aBlockResult = ArrayUtil.xor(input, decryptABlock(cipherBlocks[i], keyBytes), 64);
                    // 下一个异或输入为当前密文块
                    input = cipherBlocks[i];
                    // 解密结果串成一串
                    System.arraycopy(aBlockResult, 0, decryptBytes, i * 64, 64);
                }
                break;

            case "CTR":
                for (int i = 0; i < cipherBlocks.length; i++) {
                    System.arraycopy(encryptedTextBytes, i * 64, cipherBlocks[i], 0, 64);
                    char[] aBlockResult = ArrayUtil.xor(cipherBlocks[i], encryptABlock(counters[i], keyBytes), 64);
                    // 解密结果串成一串
                    System.arraycopy(aBlockResult, 0, decryptBytes, i * 64, 64);
                }
                break;
        }

        // 处理填充
        // 1. 抽出最后一个字节
        String decryptBytesStr = String.valueOf(decryptBytes);
        int paddedBits = Integer.parseInt(decryptBytesStr.substring(decryptBytes.length - 8), 2);
        System.out.println("pad " + paddedBits);
        char[] origin = decryptBytesStr.substring(0, decryptBytes.length - 8 - paddedBits).toCharArray();
        String plaintext = ArrayUtil.segmentAndPrintChars(origin);
        // 2. 去掉填充信息
        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("plaintext bits", origin));
            messages.add(ArrayUtil.printInfo("plaintext", plaintext, false));
        }
        return plaintext;
    }

    private char[] decryptABlock(char[] encryptedTextBlock, char[] keyBytes) {
        char[][] inverseKeys = inverseSubKeys(generateSubKeys(keyBytes));
        char[] result = encode(encryptedTextBlock, inverseKeys);

        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("encryptedBlock bits", encryptedTextBlock));
            messages.add(ArrayUtil.printBitChars("key bits", keyBytes));
            messages.add(ArrayUtil.printBitChars("decryptedText bits", result));
        }
        return result;
    }

    /**
     * change over the sub keys for reusing the encode function
     * @param subKeys origin subKeys
     * @return  inverse subKeys
     */
    private char[][] inverseSubKeys(char[][] subKeys) {
        char[][] inverseKeys = new char[subKeys.length][];

        for (int i = 0; i < subKeys.length; i++) {
            inverseKeys[i] = subKeys[subKeys.length - 1 - i];
        }
        return inverseKeys;
    }

    /**
     * core function of DES，
     * disruption and confusion most because of
     * substituting and selecting operation
     * @param right the right split of plaintext of xor result of last calculation
     * @param subKey subKey in round i
     * @return result after P Replacement
     */
    private char[] coreEncrypt(char[] right, char[] subKey) {
        // 1. do selection for 32-bit right disrupted info
        //    get 48-bit extended array
        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("[f] 32-bit input", right));
        }
        char[] extendedRight = ArrayUtil.disruptArray(right, DESConstants.E);

        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("[f] Selection", extendedRight));
            messages.add(ArrayUtil.printBitChars("[f] subKey", subKey));
        }

        // 2. xor 48-bit extendedRight and 48-bit subKey
        char[] xorResult = ArrayUtil.xor(extendedRight, subKey, 48);
        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("[f] xor", xorResult));
        }

        // 3. substitute box mixing and confusing
        // (1) format 1x48 matrix into an 8x6 matrix
        char[][] twoDimensionArray = ArrayUtil.segmentDimension(xorResult, 8, 6);
        StringBuilder outputBuilder = new StringBuilder();
        for (int i = 0; i < twoDimensionArray.length; i++) {

            char[] rowBits = {
                    twoDimensionArray[i][0],
                    twoDimensionArray[i][5]
            };
            char[] columnBits = {
                    twoDimensionArray[i][1], twoDimensionArray[i][2],
                    twoDimensionArray[i][3], twoDimensionArray[i][4]
            };

            // (2) obtain the index of output value in SUBSTITUTE_BOX[i]
            int rowIndex = Integer.parseInt(String.valueOf(rowBits), 2);
            int columnIndex = Integer.parseInt(String.valueOf(columnBits), 2);
            // (3) obtain output of Si
            short output = DESConstants.SUBSTITUTE_BOX[i][rowIndex][columnIndex];
            outputBuilder.append(Integer.toBinaryString((output & 0x0f) + 0x10).substring(1));
        }
        char[] substitutedResult = outputBuilder.toString().toCharArray();

        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("[F] SBox", substitutedResult));
        }

        // 4. replacement P, returns 28-bit array
        return ArrayUtil.disruptArray(substitutedResult, DESConstants.P);
    }

    /**
     * generate 16 48-bit sub keys
     * @param keyBytes origin key bits in chars format
     * @return 16-elements subKey array
     */
    public char[][] generateSubKeys(char[] keyBytes) {
        char[][] subKeys = new char[16][48];
        // Replacement and selection 1
        char[] c = ArrayUtil.disruptArray(keyBytes, DESConstants.PERMUTED_CHOICE_1_C0);

        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("Permuted Choice 1 C0", c));
        }

        char[] d = ArrayUtil.disruptArray(keyBytes, DESConstants.PERMUTED_CHOICE_1_D0);

        if (printMsg) {
            messages.add(ArrayUtil.printBitChars("Permuted Choice 1 D0", d));
            messages.add("\nStart to generate sub keys......");
            System.out.println("\nStart to generate sub keys......");
        }

        // loop left shifting
        for (int i = 0; i < 16; i++) {
            c = ArrayUtil.leftShift(c, DESConstants.moveBit[i]);
            d = ArrayUtil.leftShift(d, DESConstants.moveBit[i]);

            if (printMsg) {
                messages.add(ArrayUtil.printBitChars("[" + (i + 1) + "]" + " leftShifting C", c));
                messages.add(ArrayUtil.printBitChars("[" + (i + 1) + "]" + " leftShifting D", d));
            }

            // 56 bit concat
            char[] concatChars = ArrayUtil.concat(c, d);
            if (printMsg) {
                messages.add(ArrayUtil.printBitChars("[" + (i + 1) + "]" + " concatChars", concatChars));
            }

            // Replacement and selection 2, get 48 bit array
            char[] key = ArrayUtil.disruptArray(concatChars, DESConstants.replace2);
            subKeys[i] = key;
            if (printMsg) {
                String prefix = "[" + (i + 1) + "] subKey";
                messages.add(ArrayUtil.printBitChars(prefix, key));
                messages.add("\n");
                System.out.println();
            }
        }
        return subKeys;
    }
}
