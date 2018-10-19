package org.jordon.security.core;

import org.jordon.security.constant.AESConstants;
import org.jordon.security.util.AESUtil;

import java.io.UnsupportedEncodingException;

public class AESCipherService implements CipherService {

    @Override
    public String encrypt(String plaintext, String key){
        short[] ptBytes = {
                0x00, 0x01, 0x00, 0x01, 0x01, 0xa1, 0x98, 0xaf,
                0xda, 0x78, 0x17, 0x34, 0x86, 0x15, 0x35, 0x66
        };

        short[] keyBytes = {
                0x00, 0x01, 0x20, 0x01, 0x71, 0x01, 0x98, 0xae,
                0xda, 0x79, 0x17, 0x14, 0x60, 0x15, 0x35, 0x94
        };
        coreOperation(ptBytes, keyBytes);
        return null;
    }

    /**
     * core encryption logic
     * @param plaintext origin plaintext in number form
     * @param key origin key in number form
     */
    public void coreOperation(short[] plaintext, short[] key) {
        // transfer plaintext and key from one-dimension matrix
        // to (data.length / 4) x 4 matrix
        short[][] initialPTState = segmentDimension(plaintext);
        short[][] initialKeyState = segmentDimension(key);

        // obtain raw round keys
        short[][] rawRoundKeys = generateRoundKeys(initialKeyState);
        printRoundKeys(rawRoundKeys);

        // make it easier to obtain a whole block of round key in a round transformation
        short[][][] roundKeys = transfer(generateRoundKeys(initialKeyState));

        // initial round key addition (xor actually)
        short[][] state = xor(roundKeys[0], initialPTState);
        printMatrix(state);

        // process the first nine round
        for (int i = 0; i < 9; i++) {
            System.out.println("N = " + (i + 1));
            // substitute bytes in state
            state = substituteState(state);
            printMatrix(state);

            // shift rows in state
            state = shiftRow(state);
            printMatrix(state);

            // mix columns in state
            state = mixColumn(state);
            printMatrix(state);

            // addition (xor actually) between round key and state
            state = xor(roundKeys[i + 1], state);
            printMatrix(state);
        }
        // process final round
        System.out.println("N = 10");
        state = substituteState(state);
        printMatrix(state);

        state = shiftRow(state);
        printMatrix(state);

        state = xor(roundKeys[roundKeys.length - 1], state);
        printMatrix(state);
    }

    /**
     * mix row operation
     * @param state a state array of the original plaintext
     * @return mixColumn result, a new state array
     */
    private short[][] mixColumn(short[][] state) {
        short[][] result = new short[state.length][4];
        for (int i = 0; i < state.length; i++) {
            result[i] = wordMultiply(state[i]);
        }
        return result;
    }

    /**
     * multiplication between a word of a state and a irreducible
     * polynomial <tt>C(x)=03x^3+01x^2+01^2+01x+02</tt> which is replaced as a
     * constant table <tt>AESConstants.CX</tt>
     * (aes-128: 4x4 x 4x1 = 4x1)
     * @param aWord a word of a state
     * @return multiplication result, a new word
     */
    private short[] wordMultiply(short[] aWord) {
        short[] result = new short[4];
        for (int i = 0; i < 4; i++) {
            result[i] = matrixMultiply(AESConstants.CX[i], aWord);
        }
        return result;
    }

    /**
     * multiplication between two words
     * @param firstWord first operand
     * @param secondWord second operand
     * @return multiplication result, a byte actually
     */
    private short matrixMultiply(short[] firstWord, short[] secondWord) {
        short result = 0;
        for (int i=0; i < 4; i++) {
            result ^= multiply(firstWord[i], secondWord[i]);
        }
        return result;
    }

    /**
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
     * generation of round keys
     * @param originalKey original cipher key
     * @return round keys
     */
    private short[][] generateRoundKeys(short[][] originalKey) {
        short[][] roundKeys = new short[44][4];
        int keyWordCount = originalKey.length;

        // copy the original cipher words into the first four words of the roundKeys
        System.arraycopy(originalKey, 0, roundKeys, 0, keyWordCount);

        for (int i = keyWordCount; i < keyWordCount * 11; i++) {
            short[] temp = roundKeys[i - 1];
            if (i % keyWordCount == 0) {
                temp = xor(substituteWord(leftShift(temp)),
                                AESConstants.R_CON[i / keyWordCount]);
            }
            roundKeys[i] = xor(roundKeys[i - keyWordCount], temp);
        }
        return roundKeys;
    }

    /**
     * substitute value of a state array using byte as unit
     * @param state state array to be substituted
     * @return substitution result, a new state array
     */
    private short[][] substituteState(short[][] state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < 4 ; j++) {
                state[i][j] = substituteByte(state[i][j]);
            }
        }
        return state;
    }

    /**
     * substitute value of a byte through <tt>SBox</tt>
     * @param originalByte byte to be substituted
     * @return substitution result, a new byte
     */
    private short substituteByte(short originalByte) {
        // low 4 bits in a originByte
        int low4Bits = originalByte & 0x000f;
        // high 4 bits in a originByte
        int high4Bits = (originalByte >> 4) & 0x000f;
        // obtain value in <tt>AESConstants.SUBSTITUTE_BOX</tt>
        return AESConstants.SUBSTITUTE_BOX[high4Bits][low4Bits];
    }

    /**
     * substitute all bytes in a word through SBox
     * @param aWord a word, aka 4 bytes
     * @return substitution result, a new and disrupted word
     */
    private short[] substituteWord(short[] aWord) {
        for (int i = 0; i < 4; i++) {
            aWord[i] = substituteByte(aWord[i]);
        }
        return aWord;
    }

    /**
     * row shifting operation, rotate over N which is defined in
     * <tt>AESConstants.SHIFTING_TABLE</tt> bytes of corresponding rows
     * @param state state array of the original plaintext
     * @return a new state array
     */
    private static short[][] shiftRow(short[][] state) {
        short[][] result = new short[state.length][4];
        for (int j = 0; j < 4; j++) {  // local byte in a word
            for (int i = 0; i < state.length; i++) {  // local word
                result[i][j] = state[AESConstants.SHIFTING_TABLE[i][j]][j];
            }
        }
        return result;
    }

    private short[] leftShift(short[] aWord) {
        short[] result = new short[4];
        for (int i = 0; i < 4; i++) {
            result[i] = aWord[AESConstants.LEFT_SHIFT_TABLE[i]];
        }
        return result;
    }

    private short[][][] transfer(short[][] shorts) {
        short[][][] result = new short[shorts.length / 4][4][4];
        for (int i = 0; i < shorts.length / 4; i++) {
            short[][] temp = new short[4][4];
            System.arraycopy(shorts, i * 4, temp, 0, 4);
            result[i] = temp;
        }
        return result;
    }

    private short[][] segmentDimension(short[] data) {
        short[][] result = new short[data.length / 4][4];
        for (int i = 0; i < result.length; i++) {
            System.arraycopy(data, i * 4, result[i], 0, 4);
        }
        return result;
    }

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

    private short[] xor(short[] first, short[] second) {
        short[] result = new short[4];
        for (short i = 0; i < 4; i++) {
            result[i] = (short) (first[i] ^ second[i]);
        }
        return result;
    }

    private void printMatrix(short[][] data) {
        for (short[] aData : data) {
            toHexString(aData);
        }
        System.out.println();
    }

    private void printRoundKeys(short[][] roundKeys) {
        System.out.println("roundKeys: ");
        for (int i = 0; i < roundKeys.length; i += 4) {
            toHexString(roundKeys[i]);
            toHexString(roundKeys[i + 1]);
            toHexString(roundKeys[i + 2]);
            toHexString(roundKeys[i + 3]);
            System.out.println();
        }
    }

    private void toHexString(short[] data) {
        for (short s : data) {
            String hexString = Integer.toHexString(s);
            if (hexString.toCharArray().length == 1) {
                hexString = "0" + hexString;
            }
            System.out.print(hexString);
        }
    }

    @Override
    public String decrypt(String encryptedText, String key) throws UnsupportedEncodingException {
        return null;
    }
}
