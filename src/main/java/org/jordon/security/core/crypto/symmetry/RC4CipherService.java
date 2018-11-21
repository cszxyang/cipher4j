package org.jordon.security.core.crypto.symmetry;

import org.jordon.security.core.crypto.CipherService;
import org.jordon.security.util.Base64Util;

public class RC4CipherService implements CipherService {

    @Override
    public String encrypt(String plaintext, String key) {
        String raw = core(plaintext, key);
        return Base64Util.encode(raw);
    }

    @Override
    public String decrypt(String encryptedText, String key) {
        String raw = Base64Util.decode(encryptedText);
        return core(raw, key);
    }

    /**
     * RC4主要逻辑
     *      1. 初始化状态向量
     *      2. 获取流密钥
     *      3. 加/解密
     * @param text 明文或密文
     * @param key 密钥
     * @return 加/解密结果
     */
    private String core(String text, String key) {
        // 状态向量
        int[] state = new int[256];

        // 生成的密钥流
        char[] keySchedule = new char[text.length()];

        StringBuilder cipherText = new StringBuilder();

        scheduleKey(state, key);
        genPseudoRandom(state, keySchedule, text.length());

        // 将密钥与明文/密文异或
        for (int i = 0; i < text.length(); i++) {
            cipherText.append((char) (text.charAt(i) ^ keySchedule[i]));
        }
        return cipherText.toString();
    }

    /**
     * 初始化向量（Key-scheduling algorithm）
     * @param state 状态向量
     * @param key 密钥
     */
    private void scheduleKey(int[] state, String key) {
        for (int i = 0; i < 256; i++) {
            state[i] = i;
        }
        for (int j = 0, i = 0; i < 256; i++) {
            j = (j + state[i] + key.charAt(i % key.length())) % 256;
            swap(state, i, j);
        }
    }

    /**
     * 伪随机生成算法（Pseudo-random generation algorithm）
     * @param state 状态向量
     * @param keySchedule 流密钥
     * @param plaintextLength 明文长度
     */
    private void genPseudoRandom(int[] state, char[] keySchedule, int plaintextLength) {
        for (int i = 0, j = 0, k = 0; k < plaintextLength; k++) {
            i = (i + 1) % 256;
            j = (j + state[i]) % 256;
            swap(state, i, j);
            keySchedule[k] = (char) (state[(state[i] + state[j]) % 256]);
        }
    }

    /**
     * 交换数组s中第i和第j个元素
     * @param s 数组
     * @param i 待处理数索引
     * @param j 待处理数索引
     */
    private void swap(int[] s, int i, int j) {
        int temp = s[i];
        s[i] = s[j];
        s[j] = temp;
    }
}
