## 一、 AES的概况 

1、历史时间：

- 1997年美国政府向社会公开征集高级数据加密标 准（AES）； 
- 第一轮： 1998年8月20日从应征的21个算法中选出 15个。 
- 第二轮： 1999年8月又选中其中5个算法。 
- 第三轮： 2000年10月2日再选出1个算法。 
- 2001年11月26日接受其作为标准。 
- 2001年12月4日正式公布： FIPS－197。    



2、AES产生的背景 

- 1984年12月里根总统下令由国家保密局研制新密 码标准，以取代DES。 
- 1991年新密码开始试用并征求意见。 民众要求公开算法，并去掉法律监督。
- 1994年颁布新密码标准（EES）。
- 1995年5月贝尔实验室的博士生M.Blaze在PC 机 上用45分钟攻击法律监督字段获得成功。
- 1995年7月美国政府放弃用EES加密数据。 
- 1997年美国政府向社会公开征AES。    

## 加密过程

### 生成轮密钥

```java
/**
 * 轮密钥扩展：将1个状态长度的主密钥扩展成<tt>rounds + 1</tt>个状态长度的轮密钥数组
 * generation of round keys
 * @param originalKey original cipher key
 * @return round keys
 */
private short[][] generateRoundKeys(short[][] originalKey) {
    short[][] roundKeys = new short[44][4];
    int keyWordCount = originalKey.length;
    // 1. copy the original cipher words into the first four words of the roundKeys
    System.arraycopy(originalKey, 0, roundKeys, 0, keyWordCount);
    // 2. extension from previous word
    for (int i = keyWordCount; i < keyWordCount * 11; i++) {
        short[] temp = roundKeys[i - 1];
        if (i % keyWordCount == 0) {
            temp = xor(substituteWord(leftShift(temp)), AESConstants.R_CON[i / keyWordCount]);
        }
        roundKeys[i] = xor(roundKeys[i - keyWordCount], temp);
    }
    return roundKeys;
}
```



### 对外加密接口

```java
public String encrypt(String plaintext, String key) {
    // transfer plaintext and key from one-dimension matrix
    // to (data.length / 4) x 4 matrix
    short[][] initialPTState = transfer(transferToShorts(plaintext));
    short[][] initialKeyState = transfer(transferToShorts(key));

    // obtain raw round keys
    short[][] rawRoundKeys = generateRoundKeys(initialKeyState);

    // make it easier to obtain a whole block of round key in a round transformation
    short[][][] roundKeys = transfer(rawRoundKeys);
    
    short[][] finalState = coreEncrypt(initialPTState, roundKeys, AESConstants.SUBSTITUTE_BOX,
                                       AESConstants.CX, AESConstants.SHIFTING_TABLE);
    return Base64Util.encode(transfer2Bytes(finalState));
}
```



### 核心加密逻辑

```java
/**
 * AES核心操作，通过将可逆操作抽取成可逆矩阵作为参数，使该方法能在加/解密操作中复用
 * @param initialPTState    明文或密文的状态数组
 * @param roundKeys     加/解密要用到的轮密钥数组
 * @param substituteTable   加/解密要用到的S盒
 * @param mixColumnTable    列混合中用来取代既约多项式的数组
 * @param shiftingTable    行变换中用来决定字间左移的位数的数组
 * @return 加/解密结果
 */
private short[][] coreEncrypt(short[][] initialPTState,
                              short[][][] roundKeys, short[][] substituteTable,
                              short[][] mixColumnTable, short[][] shiftingTable) {

    // 初始轮密钥加，异或操作
    short[][] state = xor(roundKeys[0], initialPTState);

    // 处理前九轮变换
    for (int i = 0; i < 9; i++) {
        // 将状态数组的字节替换为S盒中相应位置的字节
        state = substituteState(state, substituteTable);
        // 行移位变换
        state = shiftRows(state, shiftingTable);
        // 列混合变换
        state = mixColumns(state, mixColumnTable);
        // 轮密钥加变换
        state = xor(roundKeys[i + 1], state);
    }

    // 处理最后一轮
    state = substituteState(state, substituteTable);
    state = shiftRows(state, shiftingTable);
    state = xor(roundKeys[roundKeys.length - 1], state);
    return state;
}
```



#### 有限域GF(2)上的加法

```java
/**
 * 有限域GF(2)上的加法，<tt>modeSize</tt>位的异或操作
 * xor corresponding byte in two state arrays
 * @param first first operand
 * @param second second operand
 * @return xor result
 */
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
```



#### 字节替代

|      | 00   | 01   | 02   | 03   | 04   | 05   | 06   | 07   | 08   | 09   | 0a   | 0b   | 0c   | 0d   | 0e   | 0f   |
| ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
| 00   | 63   | 7c   | 77   | 7b   | f2   | 6b   | 6f   | c5   | 30   | 01   | 67   | 2b   | fe   | d7   | ab   | 76   |
| 10   | ca   | 82   | c9   | 7d   | fa   | 59   | 47   | f0   | ad   | d4   | a2   | af   | 9c   | a4   | 72   | c0   |
| 20   | b7   | fd   | 93   | 26   | 36   | 3f   | f7   | cc   | 34   | a5   | e5   | f1   | 71   | d8   | 31   | 15   |
| 30   | 04   | c7   | 23   | c3   | 18   | 96   | 05   | 9a   | 07   | 12   | 80   | e2   | eb   | 27   | b2   | 75   |
| 40   | 09   | 83   | 2c   | 1a   | 1b   | 6e   | 5a   | a0   | 52   | 3b   | d6   | b3   | 29   | e3   | 2f   | 84   |
| 50   | 53   | d1   | 00   | ed   | 20   | fc   | b1   | 5b   | 6a   | cb   | be   | 39   | 4a   | 4c   | 58   | cf   |
| 60   | d0   | ef   | aa   | fb   | 43   | 4d   | 33   | 85   | 45   | f9   | 02   | 7f   | 50   | 3c   | 9f   | a8   |
| 70   | 51   | a3   | 40   | 8f   | 92   | 9d   | 38   | f5   | bc   | b6   | da   | 21   | 10   | ff   | f3   | d2   |
| 80   | cd   | 0c   | 13   | ec   | 5f   | 97   | 44   | 17   | c4   | a7   | 7e   | 3d   | 64   | 5d   | 19   | 73   |
| 90   | 60   | 81   | 4f   | dc   | 22   | 2a   | 90   | 88   | 46   | ee   | b8   | 14   | de   | 5e   | 0b   | db   |
| a0   | e0   | 32   | 3a   | 0a   | 49   | 06   | 24   | 5c   | c2   | d3   | ac   | 62   | 91   | 95   | e4   | 79   |
| b0   | e7   | c8   | 37   | 6d   | 8d   | d5   | 4e   | a9   | 6c   | 56   | f4   | ea   | 65   | 7a   | ae   | 08   |
| c0   | ba   | 78   | 25   | 2e   | 1c   | a6   | b4   | c6   | e8   | dd   | 74   | 1f   | 4b   | bd   | 8b   | 8a   |
| d0   | 70   | 3e   | b5   | 66   | 48   | 03   | f6   | 0e   | 61   | 35   | 57   | b9   | 86   | c1   | 1d   | 9e   |
| e0   | e1   | f8   | 98   | 11   | 69   | d9   | 8e   | 94   | 9b   | 1e   | 87   | e9   | ce   | 55   | 28   | df   |
| f0   | 8c   | a1   | 89   | 0d   | bf   | e6   | 42   | 68   | 41   | 99   | 2d   | 0f   | b0   | 54   | bb   | 16   |



```java
/**
 * 状态替代：对状态中的每个字进行字替代
 * substitute value of a state array using byte as unit
 * @param state state array to be substituted
 * @return substitution result, a new state array
 */
private short[][] substituteState(short[][] state, short[][] substituteTable) {
    for (int i = 0; i < state.length; i++) {
        for (int j = 0; j < 4 ; j++) {
            state[i][j] = substituteByte(state[i][j], substituteTable);
        }
    }
    return state;
}

/**
 * 字替代：对字中每个字节进行字节替代
 * substitute all bytes in a word through SBox
 * @param aWord a word, aka 4 bytes
 * @return substitution result, a new and disrupted word
 */
private short[] substituteWord(short[] aWord) {
    for (int i = 0; i < 4; i++) {
        aWord[i] = substituteByte(aWord[i], AESConstants.SUBSTITUTE_BOX);
    }
    return aWord;
}

/**
 * 字节替代： 取一个字的高四位和低四位分别作为S盒的行号和列号，
 *          通过行列号取S盒中的字节替代原字节
 * substitute value of a byte through <tt>SBox</tt>
 * @param originalByte byte to be substituted
 * @return substitution result, a new byte
 */
private short substituteByte(short originalByte, short[][] substituteTable) {
    // low 4 bits in a originByte
    int low4Bits = originalByte & 0x000f;
    // high 4 bits in a originByte
    int high4Bits = (originalByte >> 4) & 0x000f;
    // obtain value in <tt>AESConstants.SUBSTITUTE_BOX</tt>
    return substituteTable[high4Bits][low4Bits];
}
```



#### 行移位变换

```java
/**
 * 行移位变换：对状态的行进行循环左移，左移规则在<tt>shiftingTable</tt>中定义
 * row shifting operation, rotate over N which is defined in
 * <tt>AESConstants.SHIFTING_TABLE</tt> bytes of corresponding rows
 * @param state state array of the original plaintext
 * @return a new state array
 */
private static short[][] shiftRows(short[][] state, short[][] shiftingTable) {
    short[][] result = new short[state.length][4];
    for (int j = 0; j < 4; j++) {  // local byte in a word
        for (int i = 0; i < state.length; i++) {  // local word
            result[i][j] = state[shiftingTable[i][j]][j];
        }
    }
    return result;
}
```



#### 列混合变换

```java
/**
 * 列混合变换：状态数组与多项式等价矩阵进行有限域GF(2)上的矩阵乘法
 * @param state 状态数组
 * @param table 多项式等价矩阵
 * @return 列混合变换后的新状态
 */
private short[][] mixColumns(short[][] state, short[][] table) {
    short[][] result = new short[state.length][4];
    for (int i = 0; i < state.length; i++) {
        result[i] = matrixMultiply(state[i], table);
    }
    return result;
}

/**
 * 一个字与多项式等价数组在有限域GF(2)上的乘法操作
 * multiplication between a word of a state and a irreducible
 * polynomial <tt>C(x)=03x^3+01x^2+01^2+01x+02</tt> which is replaced as a
 * constant table <tt>AESConstants.CX</tt>
 * (aes-128: 4x4 x 4x1 = 4x1)
 * @param aWord a word of a state
 * @return multiplication result, a new word
 */
private short[] matrixMultiply(short[] aWord, short[][] table) {
    short[] result = new short[4];
    for (int i = 0; i < 4; i++) {
        result[i] = wordMultiply(table[i], aWord);
    }
    return result;
}

/**
 * 两个字在有限域GF(2)上的乘法操作
 * multiplication between two words
 * @param firstWord first operand
 * @param secondWord second operand
 * @return multiplication result, a byte actually
 */
private short wordMultiply(short[] firstWord, short[] secondWord) {
    short result = 0;
    for (int i=0; i < 4; i++) {
        result ^= multiply(firstWord[i], secondWord[i]);
    }
    return result;
}

/**
 * 有限域GF(2)上的乘法操作，通过分解操作数将之转化成有限域GF(2)上的倍乘操作
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
```





## 解密过程

### 对外的解密接口

```java
public String decrypt(String encryptedText, String key) {
    short[][] initialTextState = transfer(Base64Util.decodeToShorts(encryptedText));
    short[][] initialKeyState = transfer(transferToShorts(key));

    short[][] decryptState = coreDecrypt(initialTextState, initialKeyState);
    return getOrigin(decryptState);
}
```



### 核心解密逻辑

获取加密轮密钥逆变换数组，复用核心加密函数即可

```java
/**
 * 解密逻辑：通过将可逆操作抽取成可逆矩阵, 复用加密核心函数
 * @param encryptedTextState initial encrypted text state
 * @param keyState initial key state
 * @return decrypted state
 */
private short[][] coreDecrypt(short[][] encryptedTextState, short[][] keyState) {
    // obtain raw round keys
    short[][] rawRoundKeys = generateRoundKeys(keyState);

    // make it easier to obtain a whole block of round key in a round transformation
    short[][][] roundKeys = transfer(rawRoundKeys);

    // 对中间9个密钥进行列混合变换
    for (int i = 1; i < roundKeys.length - 1; i++) {
        roundKeys[i] = mixColumns(roundKeys[i], AESConstants.INVERSE_CX);
    }

    short[][][] inverseRoundKeys = inverseRoundKeys(roundKeys);
    return coreEncrypt(encryptedTextState, inverseRoundKeys, AESConstants.
                       INVERSE_SUBSTITUTE_BOX, AESConstants.INVERSE_CX, AESConstants.INVERSE_SHIFTING_TABLE);
}

/**
 * [解密] 将解密扩展密钥数组逆转，方便复用核心加密操作，
 * @param roundKeys 解密扩展密钥数组
 * @return 逆转了的解密扩展密钥数组
 */
private short[][][] inverseRoundKeys(short[][][] roundKeys) {
    short[][][] result = new short[roundKeys.length][4][4];
    int length = roundKeys.length;
    for (int i = 0; i < roundKeys.length; i++) {
        result[i] = roundKeys[length - 1 - i];
    }
    return result;
}
```



## 测试

```java
@Test
public void testAES() throws UnsupportedEncodingException {
    String plaintext = "passwordTextCase", key = "simpleKeyCase123";
    CipherService aesService = new AESCipherService();
    String encryptedText = aesService.encrypt(plaintext, key);

    ArrayUtil.printInfo("encrypted text", encryptedText, false);
    aesService.decrypt(encryptedText, key);
}
```

### 加密结果

```
#####################  encryption  #####################
plaintext text                passwordTextCase              
key text                      simpleKeyCase123              
initial plaintext state       70617373776f72645465787443617365
initial key state             73696d706c654b657943617365313233

RoundKeys
[RoundKey 1]                  73696d706c654b657943617365313233
[RoundKey 2]                  b54aae3dd92fe558a06c842bc55db618
[RoundKey 3]                  fb04039b222be6c3824762e8471ad4f0
[RoundKey 4]                  5d4c8f3b7f6769f8fd200b10ba3adfe0
[RoundKey 5]                  d5d26ecfaab5073757950c27edafd3c7
[RoundKey 6]                  bcb4a89a1601afad4194a38aac3b704d
[RoundKey 7]                  7ee54b0b68e4e4a62970472c854b3761
[RoundKey 8]                  8d7fa49ce59b403acceb071649a03077
[RoundKey 9]                  ed7b51a708e0119dc40b168b8dab26fc
[RoundKey 10]                 948ce1fa9c6cf0675867e6ecd5ccc010
[RoundKey 11]                 e9362bf9755adb9e2d3d3d72f8f1fd62

N = 1
SubBytes                      7b30727baf67127cd8f7d4c5f75383b1
ShiftRows                     7b67d4b1aff7837bd853727cf73012c5
MixColumns                    3a636747bfbfc8685094ebaa7264b7b1
RoundKey                      b54aae3dd92fe558a06c842bc55db618
AddRoundKeys                  8f29c97a66902d30f0f86f81b73901a9

N = 2
SubBytes                      73a5ddda3360d8048c41a80ca9127cd3
ShiftRows                     7360a8d333417cda8c12dd04a9a5d80c
MixColumns                    3d8336e003efffc7ecd033486987b385
RoundKey                      fb04039b222be6c3824762e8471ad4f0
AddRoundKeys                  c687357b21c419046e9751a02e9d6775

N = 3
SubBytes                      b4179621fd1cd4f29f88d1e0315e859d
ShiftRows                     b41cd19dfd8885219f5e96f23117d4e0
MixColumns                    1b79ad2bc6430753a370fb8d6f98ae4b
RoundKey                      5d4c8f3b7f6769f8fd200b10ba3adfe0
AddRoundKeys                  46352210b9246eab5e50f09dd5a271ab

N = 4
SubBytes                      5a9693ca56369f6258538c5e033aa362
ShiftRows                     5a368c625653a3ca583a936203969f5e
MixColumns                    00dbc99030c41d850fe0f98566d052b0
RoundKey                      d5d26ecfaab5073757950c27edafd3c7
AddRoundKeys                  d509a75f9a711ab25875f5a28b7f8177

N = 5
SubBytes                      03015ccfb8a3a2376a9de63a3dd20cf5
ShiftRows                     03a3e6f5b89d0ccf6ad25c373d01a23a
MixColumns                    eb9a73b1144277c7d206595ee1f82d90
RoundKey                      bcb4a89a1601afad4194a38aac3b704d
AddRoundKeys                  572edb2b0243d86a9392fad44dc35ddd

N = 6
SubBytes                      5b31b9f1771a6102dc4f2d48e32e4cc1
ShiftRows                     5b1a2dc1774f4cf1dc2eb902e3316148
MixColumns                    74d9434382cca8636a529deca76ac8fe
RoundKey                      7ee54b0b68e4e4a62970472c854b3761
AddRoundKeys                  0a3c0848ea284cc54322dac02221ff9f

N = 7
SubBytes                      67eb3052873429a61a9357ba93fd16db
ShiftRows                     673457db879316521afd30a693eb29ba
MixColumns                    1e2d8b67ffd2ceb3be0d76b4889fff03
RoundKey                      8d7fa49ce59b403acceb071649a03077
AddRoundKeys                  93522ffb1a498e8972e671a2c13fcf74

N = 8
SubBytes                      dc00150fa23b19a7408ea33a78758a92
ShiftRows                     dc3ba392a28e8a0f407515a77800193a
MixColumns                    dfc617d8532f32e7ad32edf5d36904e5
RoundKey                      ed7b51a708e0119dc40b168b8dab26fc
AddRoundKeys                  32bd467f5bcf237a6939fb7e5ec22219

N = 9
SubBytes                      237a5ad2398a26daf9120ff3582593d4
ShiftRows                     238a0fd4391293d2f9255ada587a26f3
MixColumns                    18e9d05305617b7506871dc0eb356049
RoundKey                      948ce1fa9c6cf0675867e6ecd5ccc010
AddRoundKeys                  8c6531a9990d8b125ee0fb2c3ef9a059

N = 10
SubBytes                      644dc7d3eed73dc958e10f71b299e0cb
ShiftRows                     64d70fcbeee1e0d35899c7c9b24d3d71
RoundKey                      e9362bf9755adb9e2d3d3d72f8f1fd62
AddRoundKeys                  8de124329bbb3b4d75a4fabb4abcc013
encrypted text                jeEkMpu7O011pPq7SrzAEw==      
```

### 解密结果

```
#####################  decryption  #####################
encrypted text                jeEkMpu7O011pPq7SrzAEw==      
key text                      simpleKeyCase123              
initial encrypted state       8de124329bbb3b4d75a4fabb4abcc013
initial key state             73696d706c654b657943617365313233

RoundKeys
[RoundKey 1]                  73696d706c654b657943617365313233
[RoundKey 2]                  b54aae3dd92fe558a06c842bc55db618
[RoundKey 3]                  fb04039b222be6c3824762e8471ad4f0
[RoundKey 4]                  5d4c8f3b7f6769f8fd200b10ba3adfe0
[RoundKey 5]                  d5d26ecfaab5073757950c27edafd3c7
[RoundKey 6]                  bcb4a89a1601afad4194a38aac3b704d
[RoundKey 7]                  7ee54b0b68e4e4a62970472c854b3761
[RoundKey 8]                  8d7fa49ce59b403acceb071649a03077
[RoundKey 9]                  ed7b51a708e0119dc40b168b8dab26fc
[RoundKey 10]                 948ce1fa9c6cf0675867e6ecd5ccc010
[RoundKey 11]                 e9362bf9755adb9e2d3d3d72f8f1fd62

inverse roundKeys
[RoundKey 1]                  e9362bf9755adb9e2d3d3d72f8f1fd62
[RoundKey 2]                  708e03febe111cd46ee2ba036852f407
[RoundKey 3]                  513f2d23ce9f1f2ad0f3a6d706b04e04
[RoundKey 4]                  16b3b0df9fa032091e6cb9fdd643e8d3
[RoundKey 5]                  de6e462d891382d681cc8bf4c82f512e
[RoundKey 6]                  0dc56d9f577dc4fb08df092249e3dada
[RoundKey 7]                  e0240c6e5ab8a9645fa2cdd9413cd3f8
[RoundKey 8]                  e0ec63caba9ca50a051a64bd1e9e1e21
[RoundKey 9]                  a13297635a70c6c0bf86c1b71b847a9c
[RoundKey 10]                 1d7fded0fb4251a3e5f60777a402bb2b
[RoundKey 11]                 73696d706c654b657943617365313233

N = 1
SubBytes                      8c0dfb5999e0a0a95ef931123e658b2c
ShiftRows                     8c6531a9990d8b125ee0fb2c3ef9a059
MixColumns                    53040c2a87038f0697c7e0d93028d2f4
RoundKey                      708e03febe111cd46ee2ba036852f407
AddRoundKeys                  238a0fd4391293d2f9255ada587a26f3

N = 2
SubBytes                      32cffb195b39227f69c2467a5ebd237e
ShiftRows                     32bd467f5bcf237a6939fb7e5ec22219
MixColumns                    8d048eb16c1195259086b3707eb0573e
RoundKey                      513f2d23ce9f1f2ad0f3a6d706b04e04
AddRoundKeys                  dc3ba392a28e8a0f407515a77800193a

N = 3
SubBytes                      934971741ae6cffb723f2f89c1528ea2
ShiftRows                     93522ffb1a498e8972e671a2c13fcf74
MixColumns                    7187e7041833245b0491895b45a8c169
RoundKey                      16b3b0df9fa032091e6cb9fdd643e8d3
AddRoundKeys                  673457db879316521afd30a693eb29ba

N = 4
SubBytes                      0a28da9fea22ff48432108c5223c4cc0
ShiftRows                     0a3c0848ea284cc54322dac02221ff9f
MixColumns                    85746becfe5cce275de232f62b1e3066
RoundKey                      de6e462d891382d681cc8bf4c82f512e
AddRoundKeys                  5b1a2dc1774f4cf1dc2eb902e3316148

N = 5
SubBytes                      5743fadd02925d2b93c3db6a4d2ed8d4
ShiftRows                     572edb2b0243d86a9392fad44dc35ddd
MixColumns                    0e668b6aefe0c834620d551574e278e0
RoundKey                      0dc56d9f577dc4fb08df092249e3dada
AddRoundKeys                  03a3e6f5b89d0ccf6ad25c373d01a23a

N = 6
SubBytes                      d571f5779a75815f587fa7b28b091aa2
ShiftRows                     d509a75f9a711ab25875f5a28b7f8177
MixColumns                    ba12800c0ceb0aae07985ebb42aa4ca6
RoundKey                      e0240c6e5ab8a9645fa2cdd9413cd3f8
AddRoundKeys                  5a368c625653a3ca583a936203969f5e

N = 7
SubBytes                      4624f0abb95071105ea222abd5356e9d
ShiftRows                     46352210b9246eab5e50f09dd5a271ab
MixColumns                    54f0b2574714202b9a44f24f2f89cac1
RoundKey                      e0ec63caba9ca50a051a64bd1e9e1e21
AddRoundKeys                  b41cd19dfd8885219f5e96f23117d4e0

N = 8
SubBytes                      c6c451752197677b6e9d35042e8719a0
ShiftRows                     c687357b21c419046e9751a02e9d6775
MixColumns                    d2523fb06931ba1a33941cb3b221a290
RoundKey                      a13297635a70c6c0bf86c1b71b847a9c
AddRoundKeys                  7360a8d333417cda8c12dd04a9a5d80c

N = 9
SubBytes                      8f906fa966f8017af039c930b7292d81
ShiftRows                     8f29c97a66902d30f0f86f81b73901a9
MixColumns                    66180a6154b5d2d83da5750b5332a9ee
RoundKey                      1d7fded0fb4251a3e5f60777a402bb2b
AddRoundKeys                  7b67d4b1aff7837bd853727cf73012c5

N = 10
SubBytes                      030a19561b2641032d501e0126083907
ShiftRows                     03081e031b0a39012d26190726504156
RoundKey                      73696d706c654b657943617365313233
AddRoundKeys                  70617373776f72645465787443617365
plaintext                     passwordTextCase  
```



参考

- [*FIPS* *197* -- Advanced Encryption Standard](https://www.baidu.com/link?url=QhLf4SboJVjBsfe1JGiOB6nzvzmo0xZ0QU_Mxoft1fB6bJK_H8o2ffPXMY4-cv-W&wd=&eqid=bea9a25000020bc8000000065bc6115b)