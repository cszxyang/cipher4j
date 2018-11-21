package org.jordon.security.core.hash;

import org.jordon.security.util.ArrayUtil;

public class SHA1 {

    public void core() {

    }

    // 处理512位
    private void processABlock(byte[] data) {
        // 获取原始信息字节数
        int origMsgLen = data.length;
        // 获取原始信息总位数
        long origBitCount = origMsgLen * 8;

        // 末尾填充1
        byte[] appendOneMsg = new byte[origMsgLen + 1];
        // 复制数据
        System.arraycopy(data, 0, appendOneMsg, 0, origMsgLen);
        // Java没法定义位，所以追加1操作是先追加字节，将字节最高位设为1
        appendOneMsg[appendOneMsg.length - 1] = (byte) 0x80;	//append 1
        int apdOneMsgLen = appendOneMsg.length * 8;		//get new length in bits

        // 确定需要添加0的位数，空出填充信息最后一块的最后64位
        while (apdOneMsgLen % 512 != 448) {
            apdOneMsgLen += 8;
        }
        // 填充0
        byte[] appendZerosMsg = new byte[apdOneMsgLen / 8];
        System.arraycopy(appendOneMsg, 0 , appendZerosMsg, 0, appendOneMsg.length);

        // add 64 bits for original length
        // 根据消息初始长度追加64位
        byte[] output = new byte[appendZerosMsg.length + 8];
        for (int i = 0; i < 8; i++) {
            output[output.length - 1 - i] = (byte)((origBitCount >>> (8 * i)) & 0xFF);
        }
    }

    /**
     * 将字节数组转成字数组（字长可变）
     * @param bytes 字节数组
     * @param wordLength 字长
     * @return 字数组
     */
    private byte[][] bytes2Words(byte[] bytes, int wordLength) {
        if (wordLength > 0) {
            byte[][] words = new byte[bytes.length / wordLength][wordLength];
            for (int i = 0, j = 0; i < bytes.length; i += wordLength, j++) {
                System.arraycopy(bytes, i, words[j], 0, wordLength);
            }
            return words;
        }
        return null;
    }

    /**
     * 字间异或
     * @param words 1,...,n个字
     * @return 异或结果
     */
    private byte[] xor(byte[]... words) {
        byte[] result = new byte[4];
        for (byte[] aWord : words) {
            for (int i = 0; i < 4; i++) {
                result[i] ^= aWord[i];
            }
        }
        return result;
    }

    /**
     * 字节数组中比特循环左移
     * @param bytes 字节数组
     * @param bits 循环左移位数
     * @return 循环左移结果
     */
    private byte[] leftRotate(byte[] bytes, int bits) {
        if (bits > 0) {
            StringBuilder builder = new StringBuilder();
            for (byte aByte : bytes) {
                builder.append(Integer.toBinaryString(aByte & 0xff));
            }
            String bitsStr = builder.toString();
            String rotatedStr = bitsStr.substring(bits, bitsStr.length()) +
                                            bitsStr.substring(0, bits);
            return ArrayUtil.int2Bytes(Integer.parseInt(rotatedStr, 2));
        }
        return null;
    }
}
