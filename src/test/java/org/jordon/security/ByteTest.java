package org.jordon.security;
import org.jordon.security.util.ArrayUtil;
import org.junit.Test;
import java.util.Arrays;

public class ByteTest {

    @Test
    public void testXor() {
        char[] a = {'0', '0', '1', '1', '1', '1', '0', '0', '0', '0', '1', '1', '1', '1', '0', '0',
                    '0', '0', '1', '1', '1', '1', '0', '0', '0', '0', '1', '1', '1', '1', '0', '0',
                    '0', '0', '1', '1', '1', '1', '0', '0', '0', '0', '1', '1', '1', '1', '0', '0'};

        char[] b = {'1', '1', '0', '0', '1', '1',
                    '0', '0', '0', '0', '1', '1',
                    '1', '1', '0', '0', '0', '0',
                    '1', '1', '1', '1', '0', '0',
                    '0', '0', '1', '1', '1', '1',
                    '0', '0', '0', '0', '1', '1',
                    '1', '1', '0', '0', '0', '0',
                    '1', '1', '1', '0', '1', '1'};

        char[] xorResult = ArrayUtil.xor(a, b, 48);
        ArrayUtil.printArray(xorResult);
        System.out.println(Arrays.deepToString(ArrayUtil.segmentDimension(b, 8, 6)));
    }
}
