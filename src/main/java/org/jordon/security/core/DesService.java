package org.jordon.security.core;

import org.jordon.security.util.ArrayUtil;

import java.io.UnsupportedEncodingException;

public class DesService implements CipherService {

    /**
     * encrypt plaintext with key
     * @param plaintext text to be encrypted
     * @param key key
     * @return encrypted text
     * @throws UnsupportedEncodingException coursed by String.getBytes()
     */
    @Override
    public String encrypt(String plaintext, String key) throws UnsupportedEncodingException {
        char[] plaintextBytes = ArrayUtil.bytesToChars(
                plaintext.getBytes("UTF-8"));
        char[] keyBytes = ArrayUtil.bytesToChars(
                key.getBytes("UTF-8"));
        char[] result = encode(plaintextBytes, keyBytes);

        ArrayUtil.printBitChars("plaintext", plaintextBytes);
        ArrayUtil.printBitChars("key", keyBytes);
        ArrayUtil.printBitChars("encrypted text bits", result);
        ArrayUtil.segmentAndPrintChars("encrypted text", result);
        return null;
    }

    /**
     * main encryption logic
     * @param plaintextBytes plaintext bits in chars format
     * @param keyBytes key bits in chars format
     * @return encryption result bits in chars format
     */
    private char[] encode(char[] plaintextBytes, char[] keyBytes) {
        char[][] subKeys = generateSubKeys(keyBytes);

        // initial permutation, get 64 bit disrupted array
        char[] chars = ArrayUtil.disruptArray(plaintextBytes, Constants.IP);
        ArrayUtil.printBitChars("plaintext after ip", chars);

        int length = chars.length;
        String binaryArrayStr = String.valueOf(chars);
        char[] left = binaryArrayStr.substring(0, length / 2).toCharArray();
        char[] right = binaryArrayStr.substring(length / 2).toCharArray();
        char[] coreEncrypted, xorResult;

        ArrayUtil.printBitChars("L0", left);
        ArrayUtil.printBitChars("R0", right);

        for (int i = 0; i < 16; i++) {
            System.out.println("N = " + (i + 1));

            coreEncrypted = coreEncrypt(right, subKeys[i]);
            ArrayUtil.printBitChars("P Replacement", coreEncrypted);

            // get 32-bit array
            xorResult = String.valueOf(ArrayUtil.xor(left, coreEncrypted))
                    .substring(16).toCharArray();

            left = right;
            right = xorResult;

            ArrayUtil.printBitChars("left", left);
            ArrayUtil.printBitChars("right", right);
            System.out.println();
        }

        char[] calResult = ArrayUtil.concat(right, left);
        return ArrayUtil.disruptArray(calResult, Constants.inverseIP);
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
        System.out.println("coreEncrypting");
        ArrayUtil.printBitChars("32-bit input", right);
        char[] extendedRight = ArrayUtil.disruptArray(right, Constants.E);
        ArrayUtil.printBitChars("Selection", extendedRight);
        ArrayUtil.printBitChars("subKey", subKey);

        // 2. xor 48-bit extendedRight and 48-bit subKey
        char[] xorResult = ArrayUtil.xor(extendedRight, subKey);
        ArrayUtil.printBitChars("xor", xorResult);

        // 3. substitute box mixing and confusing
        // (1) format 48-bit one-dimension array into an 8x6 matrix
        char[][] twoDimensionArray = ArrayUtil.segmentDimension(xorResult, 8, 6);
        StringBuilder outputBuilder = new StringBuilder();
        for (int i = 0; i < twoDimensionArray.length; i++) {
            char[] rowBits = {twoDimensionArray[i][0], twoDimensionArray[i][5]};
            char[] columnBits =
                    {
                            twoDimensionArray[i][1], twoDimensionArray[i][2],
                            twoDimensionArray[i][3], twoDimensionArray[i][4]
                    };

            // obtain the index of output value in SUBSTITUTE_BOX[i]
            int rowIndex = Integer.parseInt(String.valueOf(rowBits), 2);
            int columnIndex = Integer.parseInt(String.valueOf(columnBits), 2);

            short output = Constants.SUBSTITUTE_BOX[i][rowIndex][columnIndex];
            outputBuilder.append(Integer.toBinaryString((output & 0x0f) + 0x10).substring(1));
        }
        char[] substitutedResult = outputBuilder.toString().toCharArray();
        ArrayUtil.printBitChars("SBox", substitutedResult);
        // 4. replacement through P array, returns 28-bit array
        return ArrayUtil.disruptArray(substitutedResult, Constants.P);
    }

    /**
     * generate 16 46-bit sub keys
     * @param keyBytes origin key bits in chars format
     * @return 16-elements subKey array
     */
    private char[][] generateSubKeys(char[] keyBytes) {
        char[][] subKeys = new char[16][48];
        // Replacement and selection 1
        char[] c = ArrayUtil.disruptArray(keyBytes, Constants.replace1C);
        ArrayUtil.printBitChars("Replacement 1 C", c);
        char[] d = ArrayUtil.disruptArray(keyBytes, Constants.replace1D);
        ArrayUtil.printBitChars("Replacement 1 D", d);

        // loop left shifting
        for (int i = 0; i < 16; i++) {
            c = ArrayUtil.leftShift(c, Constants.moveBit[i]);
            ArrayUtil.printBitChars((i + 1) + " leftShifting C", c);
            d = ArrayUtil.leftShift(d, Constants.moveBit[i]);
            ArrayUtil.printBitChars((i + 1) + " leftShifting D", d);

            // 56 bit concat
            char[] concatChars = ArrayUtil.concat(c, d);
            ArrayUtil.printBitChars((i + 1) + " concatChars", concatChars);

            // Replacement and selection 2, get 48 bit array
            char[] key = ArrayUtil.disruptArray(concatChars, Constants.replace2);
            subKeys[i] = key;
            String prefix = "subKey[" + (i + 1) + "]";
            ArrayUtil.printBitChars(prefix, key);
            System.out.println();
        }
        return subKeys;
    }

    @Override
    public String decrypt(String encryptedText, String key) {
        return null;
    }
}
