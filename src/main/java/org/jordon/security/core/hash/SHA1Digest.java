package org.jordon.security.core.hash;

import org.jordon.security.util.Base64Util;

public class SHA1Digest implements IHash {

    @Override
    public byte[] hash(byte[] data) {
        return new byte[0];
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.encode(encode(new byte[32])));
    }

    /*
     *	Encode ASCII formatted string using secure hash algorithm
     *	@params data byte array of string to be encoded
     */
    private static byte[] encode(byte[] data) {
        // pad byte array, assume ASCII encoding
        // 获取原始信息字节数
        int origMsgLen = data.length;	// length in bytes
        // 获取原始信息总位数
        long origBitCount = origMsgLen * 8;	// length in bits

        // 末尾填充1
        byte[] appendOneMsg = new byte[origMsgLen + 1];
        // 复制数据
        System.arraycopy(data, 0, appendOneMsg, 0, origMsgLen);
        // Java没法操作，所以追加1操作是先追加字节，将字节最高位设为1
        appendOneMsg[appendOneMsg.length - 1] = (byte) 0x80;	//append 1
        int apdOneMsgLen = appendOneMsg.length * 8;		//get new length in bits

        // find length multiple of 512
        // 空出填充信息最后一块的最后64位
        while (apdOneMsgLen % 512 != 448) {
            apdOneMsgLen += 8;
        }

        // size of block with appended zeros
        // 填充0
        byte[] appendZerosMsg = new byte[apdOneMsgLen / 8];
        System.arraycopy(appendOneMsg, 0 , appendZerosMsg, 0, appendOneMsg.length);

        // add 64 bits for original length
        // 根据消息初始长度追加64位
        byte[] output = new byte[appendZerosMsg.length + 8];

        for (int i = 0; i < 8; i++) {
            output[output.length - 1 - i] = (byte)((origBitCount >>> (8 * i)) & 0xFF);
        }
        System.arraycopy(appendZerosMsg, 0 , output, 0, appendZerosMsg.length);

        int size = output.length;
        int num_chunks = size * 8 / 512;

        int h0 = 0x67452301;
        int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;

        // hash each successive 512 chunk
        for (int i = 0; i < num_chunks; i++) {
            int[] w = new int[80];
            // divide into 16 32 bit words
            // 将512位的明文分组划分为16个子明文分组，每个子明文分组为32位
            for (int j = 0; j < 16; j++) {
                w[j] =  ((output[i * 512 / 8 + 4*j] << 24) & 0xFF000000) |
                            ((output[i*512/8 + 4*j+1] << 16) & 0x00FF0000);
                w[j] |= ((output[i * 512 / 8 + 4 * j + 2] << 8) & 0xFF00) |
                                    (output[i*512/8 + 4*j+3] & 0xFF);
            }

            // extend 16 words into 80 words
            // 16份子明文分组扩展为80份。
            for (int j = 16; j < 80; j++) {
                w[j] = left_rotate(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);
            }

            // initialize initial values
            // 申请5个32位的链接变量，记为A、B、C、D、E。
            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            int f = 0;
            int k = 0;

            // main loop
            // 80份子明文分组进行4轮运算。
            for (int j = 0; j < 80; j++) {
                if (0 <= j && j <= 19) {
                    f = (b & c) | ((~b) & d);
                    k = 0x5A827999;
                }
                else if(20 <= j && j <= 39) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                }
                else if(40 <= j && j <= 59) {
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                }
                else if(60 <= j && j <= 79) {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }

                int temp = left_rotate(a, 5) + f + e + k + w[j];
                e = d;
                d = c;
                c = left_rotate(b, 30);
                b = a;
                a = temp;
            }

            // add chunk's hash to result
            // 链接变量与初始链接变量进行求和运算。
            // 链接变量作为下一个明文分组的输入重复进行以上操作。
            h0 = h0 + a;
            h1 = h1 + b;
            h2 = h2 + c;
            h3 = h3 + d;
            h4 = h4 + e;
        }

        // 最后，5个链接变量里面的数据就是SHA1摘要。
        byte[] hash = new byte[20];

        for (int j = 0; j < 4; j++) {
            hash[j] = (byte) ((h0 >>> 24 - j * 8) & 0xFF);
        }

        for (int j = 0; j < 4; j++) {
            hash[j+4] = (byte) ((h1 >>> 24 - j * 8) & 0xFF);
        }

        for (int j = 0; j < 4; j++) {
            hash[j+8] = (byte) ((h2 >>> 24 - j * 8) & 0xFF);
        }

        for (int j = 0; j < 4; j++) {
            hash[j+12] = (byte) ((h3 >>> 24 - j * 8) & 0xFF);
        }

        for (int j = 0; j < 4; j++) {
            hash[j+16] = (byte) ((h4 >>> 24 - j * 8) & 0xFF);
        }

        return hash;
    }

    private static int left_rotate(int n, int d) {
        return (n << d) | (n >>> (32 - d));
    }

    private int[] h = {
            0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0
    };
}
