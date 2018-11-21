package org.jordon.security.core.crypto;

import java.io.UnsupportedEncodingException;

public interface CipherService {
    /**
     * encrypt plaintext with key
     * @param plaintext text to be encrypted
     * @param key key
     * @return encrypted text
     */
    String encrypt(String plaintext, String key) throws UnsupportedEncodingException;

    /**
     * decrypt encrypted text with key
     * @param encryptedText encrypted text
     * @param key key
     * @return origin plaintext
     */
    String decrypt(String encryptedText, String key) throws UnsupportedEncodingException;
}
