package org.jordon.security.core;

import org.jordon.security.attack.cryptanalysis.DESDifferential;
import org.jordon.security.attack.statistics.DESStatistics;
import org.jordon.security.core.crypto.symmetry.DESCipherService;
import org.jordon.security.util.ArrayUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class DESCipherServiceTest {

    /**
     * DES差分攻击
     */
    @Test
    public void testDESDifferential() {
        DESDifferential desDifferential = new DESDifferential((short) 1);
        desDifferential.analyze();
    }

    /**
     * 简单分析DES明文/密钥变化与密文变化位数的统计学关系
     */
    @Test
    public void testDESStatistics() {
        DESStatistics desStatistics = new DESStatistics(10);
        desStatistics.run();
    }

    @Test
    public void testPadPlaintext() {
        DESCipherService desCipherService = new DESCipherService(false);
//        desCipherService.padPlaintext("password1");
    }

    @Test
    public void testEncryptFile() {
        DESCipherService desCipherService = new DESCipherService(false);
        desCipherService.setWorkingMode("ECB");
        File file = new File("E:\\linfeng\\themes\\hugo-theme-casper" +
                                        "\\layouts\\partials\\twitter_card.html");
        try {
            System.out.println(desCipherService.encryptFile(file, "password"));;
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
