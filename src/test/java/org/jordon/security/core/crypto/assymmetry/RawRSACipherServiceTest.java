package org.jordon.security.core.crypto.assymmetry;

import org.jordon.security.util.ArrayUtil;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RawRSACipherServiceTest {
    private Random random = ThreadLocalRandom.current();

    @Test
    public void test() {
        RawRSACipherService service = new RawRSACipherService();
        for (int i = 0; i < 30; i++) { // 进行30次测试
            String example = genPlaintext();
            ArrayUtil.printInfo("example", example, false);
            String cipher = service.encrypt(example);
            ArrayUtil.printInfo("cipher", cipher, false);
            String plaintext = service.decrypt(cipher);
            ArrayUtil.printInfo("plaintext", plaintext, true);
        }
    }

    // 随机生成明文样例
    private String genPlaintext() {
        // 随机生成含有[1, 50]个可打印字符的字符串
        int count = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            // 在可打印字符范围内随机获取
            int val = random.nextInt(126 -33) + 33;
            builder.append((char) val);
        }
        return builder.toString();
    }
}