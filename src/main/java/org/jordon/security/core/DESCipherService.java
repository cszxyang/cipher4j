package org.jordon.security.core;

import org.jordon.security.constant.DESConstants;
import org.jordon.security.util.ArrayUtil;
import org.jordon.security.util.Base64Util;
import java.io.UnsupportedEncodingException;

public class DESCipherService implements CipherService {

    /**
     * encrypt plaintext with key
     * @param plaintext text to be encrypted
     * @param key key
     * @return encrypted text
     * @throws UnsupportedEncodingException caused by String.getBytes()
     */
    @Override
    public String encrypt(String plaintext, String key) throws UnsupportedEncodingException {
        ArrayUtil.printInfo("plaintext", plaintext, false);
        ArrayUtil.printInfo("keyText", key, true);

        char[] plaintextBytes = ArrayUtil.bytesToChars(
                                    plaintext.getBytes("UTF-8"));
        char[] keyBytes = ArrayUtil.bytesToChars(
                            key.getBytes("UTF-8"));

        ArrayUtil.printBitChars("plaintext bits", plaintextBytes);
        ArrayUtil.printBitChars("key bits", keyBytes);

        char[][] subKeys = generateSubKeys(keyBytes);
        char[] result = encode(plaintextBytes, subKeys);
        String encryptedText = Base64Util.encode(result);

        ArrayUtil.printBitChars("encryptedText bits", result);
        ArrayUtil.printInfo("encryptedText", encryptedText, true);
        return encryptedText;
    }

    /**
     * main encryption logic
     * @param plaintextBytes plaintext bits in chars format
     * @param subKeys subKeys bits in chars format
     * @return encryption result bits in chars format
     */
    private char[] encode(char[] plaintextBytes, char[][] subKeys) {
        // initial permutation, get 64 bit disrupted array
        char[] chars = ArrayUtil.disruptArray(plaintextBytes, DESConstants.IP);
        ArrayUtil.printBitChars("plaintext after ip", chars);

        int length = chars.length;
        String binaryArrayStr = String.valueOf(chars);
        char[] left = binaryArrayStr.substring(0, length / 2).toCharArray();
        char[] right = binaryArrayStr.substring(length / 2).toCharArray();
        char[] coreEncrypted, xorResult;

        ArrayUtil.printBitChars("L0", left);
        ArrayUtil.printBitChars("R0", right);

        for (int i = 0; i < 16; i++) {
            System.out.println();

            coreEncrypted = coreEncrypt(right, subKeys[i]);
            ArrayUtil.printBitChars("[f] " +
                                "P Replacement", coreEncrypted);

            // get 32-bit array
            xorResult = String.valueOf(ArrayUtil.xor(left, coreEncrypted))
                                .substring(16).toCharArray();

            left = right;
            right = xorResult;

            ArrayUtil.printBitChars("["  + (i + 1) +  "] " + "left", left);
            ArrayUtil.printBitChars("["  + (i + 1) +  "] " + "right", right);
        }
        System.out.println();
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
        ArrayUtil.printInfo("encryptedText", encryptedText, false);
        ArrayUtil.printInfo("key", key, true);

        char[] encryptedTextBytes = Base64Util.decodeToChars(encryptedText);
        char[] keyBytes = ArrayUtil.bytesToChars(
                            key.getBytes("UTF-8"));

        char[][] inverseKeys = inverseSubKeys(generateSubKeys(keyBytes));
        char[] result = encode(encryptedTextBytes, inverseKeys);

        ArrayUtil.printBitChars("encryptedText bits", encryptedTextBytes);
        ArrayUtil.printBitChars("key bits", keyBytes);
        ArrayUtil.printBitChars("decryptedText bits", result);
        return ArrayUtil.segmentAndPrintChars("decrypt plaintext text", result);
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
        ArrayUtil.printBitChars("[f] 32-bit input", right);
        char[] extendedRight = ArrayUtil.disruptArray(right, DESConstants.E);
        ArrayUtil.printBitChars("[f] Selection", extendedRight);
        ArrayUtil.printBitChars("[f] subKey", subKey);

        // 2. xor 48-bit extendedRight and 48-bit subKey
        char[] xorResult = ArrayUtil.xor(extendedRight, subKey);
        ArrayUtil.printBitChars("[f] xor", xorResult);

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
        ArrayUtil.printBitChars("[F] SBox", substitutedResult);
        // 4. replacement P, returns 28-bit array
        return ArrayUtil.disruptArray(substitutedResult, DESConstants.P);
    }

    /**
     * generate 16 48-bit sub keys
     * @param keyBytes origin key bits in chars format
     * @return 16-elements subKey array
     */
    private char[][] generateSubKeys(char[] keyBytes) {
        char[][] subKeys = new char[16][48];
        // Replacement and selection 1
        char[] c = ArrayUtil.disruptArray(keyBytes, DESConstants.PERMUTED_CHOICE_1_C0);
        ArrayUtil.printBitChars("Permuted Choice 1 C0", c);
        char[] d = ArrayUtil.disruptArray(keyBytes, DESConstants.PERMUTED_CHOICE_1_D0);
        ArrayUtil.printBitChars("Permuted Choice 1 D0", d);
        System.out.println("\nStart to generate sub keys......");

        // loop left shifting
        for (int i = 0; i < 16; i++) {
            c = ArrayUtil.leftShift(c, DESConstants.moveBit[i]);
            ArrayUtil.printBitChars("[" + (i + 1) + "]" + " leftShifting C", c);
            d = ArrayUtil.leftShift(d, DESConstants.moveBit[i]);
            ArrayUtil.printBitChars("[" + (i + 1) + "]" + " leftShifting D", d);

            // 56 bit concat
            char[] concatChars = ArrayUtil.concat(c, d);
            ArrayUtil.printBitChars("[" + (i + 1) + "]" + " concatChars", concatChars);

            // Replacement and selection 2, get 48 bit array
            char[] key = ArrayUtil.disruptArray(concatChars, DESConstants.replace2);
            subKeys[i] = key;
            String prefix = "[" + (i + 1) + "] subKey";
            ArrayUtil.printBitChars(prefix, key);
            System.out.println();
        }
        return subKeys;
    }
}
