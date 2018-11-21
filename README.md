## Introduction

Pure Implementations for encryption algorithms including DES, RSA, AES



## Usage

Take AES Implementation for example

```java
String plaintext = "passwordTextCase", key = "simpleKeyCase123";
CipherService aesService = new AESCipherService();

String encryptedText = aesService.encrypt(plaintext, key);
aesService.decrypt(encryptedText, key);
```



## Output

### DES

For tracing the whole process of DES encryption and decryption

```
plaintext                     01234567                      
keyText                       12345678                      

plaintext bits                00110000 00110001 00110010 00110011 00110100 00110101 00110110 00110111 
key bits                      00110001 00110010 00110011 00110100 00110101 00110110 00110111 00111000 
Permuted Choice 1 C0          00000000 00000000 11111111 1111 
Permuted Choice 1 D0          01100110 01111000 10000000 1111 

Start to generate sub keys......
[1] leftShifting C            00000000 00000001 11111111 1110 
[1] leftShifting D            11001100 11110001 00000001 1110 
[1] concatChars               00000000 00000001 11111111 11101100 11001111 00010000 00011110 
[1] subKey                    01010000 00101100 10101100 01010111 00101010 11000010 

[2] leftShifting C            00000000 00000011 11111111 1100 
[2] leftShifting D            10011001 11100010 00000011 1101 
[2] concatChars               00000000 00000011 11111111 11001001 10011110 00100000 00111101 
[2] subKey                    01010000 10101100 10100100 01010000 10100011 01000111 

[3] leftShifting C            00000000 00001111 11111111 0000 
[3] leftShifting D            01100111 10001000 00001111 0110 
[3] concatChars               00000000 00001111 11111111 00000110 01111000 10000000 11110110 
[3] subKey                    11010000 10101100 00100110 11110110 10000100 10001100 

[4] leftShifting C            00000000 00111111 11111100 0000 
[4] leftShifting D            10011110 00100000 00111101 1001 
[4] concatChars               00000000 00111111 11111100 00001001 11100010 00000011 11011001 
[4] subKey                    11100000 10100110 00100110 01001000 00110111 11001011 

[5] leftShifting C            00000000 11111111 11110000 0000 
[5] leftShifting D            01111000 10000000 11110110 0110 
[5] concatChars               00000000 11111111 11110000 00000111 10001000 00001111 01100110 
[5] subKey                    11100000 10010110 00100110 00111110 11110000 00101001 

[6] leftShifting C            00000011 11111111 11000000 0000 
[6] leftShifting D            11100010 00000011 11011001 1001 
[6] concatChars               00000011 11111111 11000000 00001110 00100000 00111101 10011001 
[6] subKey                    11100000 10010010 01110010 01100010 01011101 01100010 

[7] leftShifting C            00001111 11111111 00000000 0000 
[7] leftShifting D            10001000 00001111 01100110 0111 
[7] concatChars               00001111 11111111 00000000 00001000 10000000 11110110 01100111 
[7] subKey                    10100100 11010010 01110010 10001100 10101001 00111010 

[8] leftShifting C            00111111 11111100 00000000 0000 
[8] leftShifting D            00100000 00111101 10011001 1110 
[8] concatChars               00111111 11111100 00000000 00000010 00000011 11011001 10011110 
[8] subKey                    10100110 01010011 01010010 11100101 01011110 01010000 

[9] leftShifting C            01111111 11111000 00000000 0000 
[9] leftShifting D            01000000 01111011 00110011 1100 
[9] concatChars               01111111 11111000 00000000 00000100 00000111 10110011 00111100 
[9] subKey                    00100110 01010011 01010011 11001011 10011010 01000000 

[10] leftShifting C           11111111 11100000 00000000 0001 
[10] leftShifting D           00000001 11101100 11001111 0001 
[10] concatChars              11111111 11100000 00000000 00010000 00011110 11001100 11110001 
[10] subKey                   00101111 01010001 01010001 11010000 11000111 00111100 

[11] leftShifting C           11111111 10000000 00000000 0111 
[11] leftShifting D           00000111 10110011 00111100 0100 
[11] concatChars              11111111 10000000 00000000 01110000 01111011 00110011 11000100 
[11] subKey                   00001111 01000001 11011001 00011001 00011110 10001100 

[12] leftShifting C           11111110 00000000 00000001 1111 
[12] leftShifting D           00011110 11001100 11110001 0000 
[12] concatChars              11111110 00000000 00000001 11110001 11101100 11001111 00010000 
[12] subKey                   00011111 01000001 10011001 11011000 01110000 10110001 

[13] leftShifting C           11111000 00000000 00000111 1111 
[13] leftShifting D           01111011 00110011 11000100 0000 
[13] concatChars              11111000 00000000 00000111 11110111 10110011 00111100 01000000 
[13] subKey                   00011111 00001001 10001001 00100011 01101010 00101101 

[14] leftShifting C           11100000 00000000 00011111 1111 
[14] leftShifting D           11101100 11001111 00010000 0001 
[14] concatChars              11100000 00000000 00011111 11111110 11001100 11110001 00000001 
[14] subKey                   00011011 00101000 10001101 10110010 00111001 10010010 

[15] leftShifting C           10000000 00000000 01111111 1111 
[15] leftShifting D           10110011 00111100 01000000 0111 
[15] concatChars              10000000 00000000 01111111 11111011 00110011 11000100 00000111 
[15] subKey                   00011001 00101100 10001100 10100101 00000011 00110111 

[16] leftShifting C           00000000 00000000 11111111 1111 
[16] leftShifting D           01100110 01111000 10000000 1111 
[16] concatChars              00000000 00000000 11111111 11110110 01100111 10001000 00001111 
[16] subKey                   01010001 00101100 10001100 10100111 01000011 11000000 

plaintext after ip            00000000 11111111 11110000 10101010 00000000 11111111 00000000 11001100 
L0                            00000000 11111111 11110000 10101010 
R0                            00000000 11111111 00000000 11001100 

[f] 32-bit input              00000000 11111111 00000000 11001100 
[f] Selection                 00000000 00010111 11111110 10000000 00010110 01011000 
[f] subKey                    01010000 00101100 10101100 01010111 00101010 11000010 
[f] xor                       01010000 00111011 01010010 11010111 00111100 10011010 
[F] SBox                      01101101 10000010 00001110 11110000 
[f] P Replacement             00010010 01111000 11000111 00011001 
[1] left                      00000000 11111111 00000000 11001100 
[1] right                     00010010 10000111 00110111 10110011 

[f] 32-bit input              00010010 10000111 00110111 10110011 
[f] Selection                 10001010 01010100 00001110 10011010 11111101 10100110 
[f] subKey                    01010000 10101100 10100100 01010000 10100011 01000111 
[f] xor                       11011010 11111000 10101010 11001010 01011110 11100001 
[F] SBox                      01110010 01101011 10010010 00100010 
[f] P Replacement             11100001 01100011 10000110 01000110 
[2] left                      00010010 10000111 00110111 10110011 
[2] right                     11100001 10011100 10000110 10001010 

[f] 32-bit input              11100001 10011100 10000110 10001010 
[f] Selection                 01110000 00111100 11111001 01000000 11010100 01010101 
[f] subKey                    11010000 10101100 00100110 11110110 10000100 10001100 
[f] xor                       10100000 10010000 11011111 10110110 01010000 11011001 
[F] SBox                      11011111 01111001 00100010 00000000 
[f] P Replacement             11000100 10101001 11000000 11010110 
[3] left                      11100001 10011100 10000110 10001010 
[3] right                     11010110 00101110 11110111 01100101 

[f] 32-bit input              11010110 00101110 11110111 01100101 
[f] Selection                 11101010 11000001 01011101 01111010 11101011 00001011 
[f] subKey                    11100000 10100110 00100110 01001000 00110111 11001011 
[f] xor                       00001010 01100111 01111011 00110010 11011100 11000000 
[F] SBox                      01001011 11110111 10111111 01011101 
[f] P Replacement             11111111 01111001 11111001 10101100 
[4] left                      11010110 00101110 11110111 01100101 
[4] right                     00011110 11100101 01111111 00100110 

[f] 32-bit input              00011110 11100101 01111111 00100110 
[f] Selection                 00001111 11010111 00001010 10111111 11101001 00001100 
[f] subKey                    11100000 10010110 00100110 00111110 11110000 00101001 
[f] xor                       11101111 01000001 00101100 10000001 00011001 00100101 
[F] SBox                      00001100 10010111 01000110 10111110 
[f] P Replacement             10001110 01101110 00010101 00111001 
[5] left                      00011110 11100101 01111111 00100110 
[5] right                     01011000 01000000 11100010 01011100 

[f] 32-bit input              01011000 01000000 11100010 01011100 
[f] Selection                 00101111 00000010 00000001 01110000 01000010 11111000 
[f] subKey                    11100000 10010010 01110010 01100010 01011101 01100010 
[f] xor                       11001111 10010000 01110011 00010010 00011111 10011010 
[F] SBox                      10110000 11010100 01000100 00100000 
[f] P Replacement             00000100 10000101 00010111 00001010 
[6] left                      01011000 01000000 11100010 01011100 
[6] right                     00011010 01100000 01101000 00101100 

[f] 32-bit input              00011010 01100000 01101000 00101100 
[f] Selection                 00001111 01000011 00000000 00110101 00000001 01011000 
[f] subKey                    10100100 11010010 01110010 10001100 10101001 00111010 
[f] xor                       10101011 10010001 01110010 10111001 10101000 01100010 
[F] SBox                      01100000 00000001 10000111 01101011 
[f] P Replacement             10001001 00110010 10101110 00001000 
[7] left                      00011010 01100000 01101000 00101100 
[7] right                     11010001 01110010 01001100 01010100 

[f] 32-bit input              11010001 01110010 01001100 01010100 
[f] Selection                 01101010 00101011 10100100 00100101 10000010 10101001 
[f] subKey                    10100110 01010011 01010010 11100101 01011110 01010000 
[f] xor                       11001100 01111000 11110110 11000000 11011100 11111001 
[F] SBox                      10110111 10101110 11111001 01010011 
[f] P Replacement             01110011 11010110 01111011 11010110 
[8] left                      11010001 01110010 01001100 01010100 
[8] right                     01101001 10110110 00010011 11111010 

[f] 32-bit input              01101001 10110110 00010011 11111010 
[f] Selection                 00110101 00111101 10101100 00001010 01111111 11110100 
[f] subKey                    00100110 01010011 01010011 11001011 10011010 01000000 
[f] xor                       00010011 01101110 11111111 11000001 11100101 10110100 
[F] SBox                      11010110 01011110 11111011 01111010 
[f] P Replacement             01111111 11110111 10110100 11010010 
[9] left                      01101001 10110110 00010011 11111010 
[9] right                     10101110 10000101 11111000 10000110 

[f] 32-bit input              10101110 10000101 11111000 10000110 
[f] Selection                 01010101 11010100 00001011 11111111 00010100 00001101 
[f] subKey                    00101111 01010001 01010001 11010000 11000111 00111100 
[f] xor                       01111010 10000101 01011010 00101111 11010011 00110001 
[F] SBox                      01111010 01011100 01111000 10001111 
[f] P Replacement             01111100 00001111 10011010 11100011 
[10] left                     10101110 10000101 11111000 10000110 
[10] right                    00010101 10111001 10001001 00011001 

[f] 32-bit input              00010101 10111001 10001001 00011001 
[f] Selection                 10001010 10111101 11110011 11000101 00101000 11110010 
[f] subKey                    00001111 01000001 11011001 00011001 00011110 10001100 
[f] xor                       10000101 11111100 00101010 11011100 00110110 01111110 
[F] SBox                      11110101 10111011 10011111 00101000 
[f] P Replacement             10111101 11100000 11100111 01011110 
[11] left                     00010101 10111001 10001001 00011001 
[11] right                    00010011 01100101 00011111 11011000 

[f] 32-bit input              00010011 01100101 00011111 11011000 
[f] Selection                 00001010 01101011 00001010 10001111 11111110 11110000 
[f] subKey                    00011111 01000001 10011001 11011000 01110000 10110001 
[f] xor                       00010101 00101010 10010011 01010111 10001110 01000001 
[F] SBox                      01110111 11110111 11110001 11100001 
[f] P Replacement             11100101 01010101 11111111 10010111 
[12] left                     00010011 01100101 00011111 11011000 
[12] right                    11110000 11101100 01110110 10001110 

[f] 32-bit input              11110000 11101100 01110110 10001110 
[f] Selection                 01111010 00010111 01011000 00111010 11010100 01011101 
[f] subKey                    00011111 00001001 10001001 00100011 01101010 00101101 
[f] xor                       01100101 00011110 11010001 00011001 10111110 01110000 
[F] SBox                      10011100 01010100 00011011 11100000 
[f] P Replacement             00110100 10111001 00110100 00010011 
[13] left                     11110000 11101100 01110110 10001110 
[13] right                    00100111 11011100 00101011 11001011 

[f] 32-bit input              00100111 11011100 00101011 11001011 
[f] Selection                 10010000 11111110 11111000 00010101 01111110 01010110 
[f] subKey                    00011011 00101000 10001101 10110010 00111001 10010010 
[f] xor                       10001011 11010110 01110101 10100111 01000111 11000100 
[F] SBox                      00011110 11000101 00010100 01101000 
[f] P Replacement             11101000 00011001 00010101 00011010 
[14] left                     00100111 11011100 00101011 11001011 
[14] right                    00011000 11110101 01100011 10010100 

[f] 32-bit input              00011000 11110101 01100011 10010100 
[f] Selection                 00001111 00010111 10101010 10110000 01111100 10101000 
[f] subKey                    00011001 00101100 10001100 10100101 00000011 00110111 
[f] xor                       00010110 00111011 00100110 00010101 01111111 10011111 
[F] SBox                      01111000 00110000 00101110 00100010 
[f] P Replacement             00010100 00101010 10000110 10001110 
[15] left                     00011000 11110101 01100011 10010100 
[15] right                    00110011 11110110 10101101 01000101 

[f] 32-bit input              00110011 11110110 10101101 01000101 
[f] Selection                 10011010 01111111 10101101 01010101 10101010 00001010 
[f] subKey                    01010001 00101100 10001100 10100111 01000011 11000000 
[f] xor                       11001011 01010011 00100001 11110010 11101001 11001010 
[F] SBox                      11000111 11110011 00000011 10001111 
[f] P Replacement             11001100 11100011 11101001 00110101 
[16] left                     00110011 11110110 10101101 01000101 
[16] right                    11010100 00010110 10001010 10100001 

encryptedText bits            10001011 10110100 01111010 00001100 11110000 10101001 01100010 01101101 
encryptedText                 i7R6DPCpYm0=                  
```

convenient and fun offering with GUI

​                                (![img](https://raw.githubusercontent.com/jordonyang/jordonyang.github.io/master/images/sec/des//desView5.png))

 ![img](https://raw.githubusercontent.com/jordonyang/jordonyang.github.io/master/images/sec/des//desView4.png)





### AES

The output of AES encryption and decryption process

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



### DES Differential Cryptanalysis

```java
package org.jordon.security.attack.cryptanalysis;

import org.jordon.security.constant.DESConstants;
import org.jordon.security.util.ArrayUtil;

public class DESDifferential {
	// 输入差分
    private short inputDiff;

    public DESDifferential(short inputDiff) {
        this.inputDiff = inputDiff;
    }

    /**
     * differential cryptanalysis
     */
    public void analyze() {
        // 选定输入查分为000001
        short[][] diffPairs = genDiffInputPairs(inputDiff);
        if (diffPairs != null) {
            printPairs(diffPairs);
            // 经过S盒子
            System.out.println("\n输出差分");

            StringBuilder[] builders = new StringBuilder[16];
            for (int i = 0; i < builders.length; i++) {
                builders[i] = new StringBuilder();
            }
            for (int i = 1; i <= diffPairs.length; i++) {
                short s = (short) (sub(getBinStr(diffPairs[i - 1][0])) ^ sub(getBinStr(diffPairs[i - 1][1])));
                String symbol = i % 8 == 0 ? "\n" : " ";
                System.out.print(getOutput(s) + symbol);

                builders[s].append(getOutput(s)).append(" ");
            }
            System.out.println("\n差分输出分布");
            for (int i = 0; i < builders.length; i++){
                ArrayUtil.printInfo(getOutput((short) i), builders[i].toString(), false);
            }
        }
    }
    
	// 格式化获取输出差分
    private String getOutput(short output) {
        if (output == 0) {
            return "0000";
        }else if (output == 1) {
            return "0001";
        }
        return Integer.toBinaryString((output & 0b1111) + 0b10000).substring(1, 5);
    }
    
	// S盒替代
    private short sub(String val) {
        char[] chars = val.toCharArray();
        char[] rowBits = {
                chars[0],
                chars[5]
        };
        char[] columnBits = {
                chars[1], chars[2],
                chars[3], chars[4]
        };

        int rowIndex = Integer.parseInt(String.valueOf(rowBits), 2);
        int columnIndex = Integer.parseInt(String.valueOf(columnBits), 2);
        // (3) obtain output of Si
        return DESConstants.SUBSTITUTE_BOX[0][rowIndex][columnIndex];
    }

    /**
     * 根据指定输入差分，生成所有明文输入对
     */
    private short[][] genDiffInputPairs(short inputDiff) {
        // 2^6
        System.out.println("输入差分：" + getBinStr(inputDiff));
        if (inputDiff >= 0x00 && inputDiff <= 0x0f) {
            short[] pair = {0, inputDiff};
            short[][] pairs = new short[64][2];
            pairs[0] = pair;

            for (short i = 1; i < 64; i++) {
                pairs[i] = new short[]{i, (short) (i ^ inputDiff)};
            }
            return pairs;
        }
        return null;
    }
	
    // 打印输入对
    private void printPairs(short[][] pairs) {
        System.out.println("\n输入对");
        for (int i = 1; i <= pairs.length; i++) {
            String symbol = i % 8 == 0 ? "\n" : " ";
            System.out.print("(" + getBinStr(pairs[i - 1][0]) +
                    "," + getBinStr(pairs[i - 1][1]) + ")" + symbol);
        }
    }
	
    // 格式化S盒输入
    private String getBinStr(short val) {
        if (val == 0) {
            return "000000";
        }else if (val == 1) {
            return "000001";
        }
        return Integer.toBinaryString((val & 0b111111) + 0b1000000).substring(1, 7);
    }
}
```

outputing all the input pairs of the differential input and corresponding differential distribution

```
输入差分：000001

输入对
(000000,000001) (000001,000000) (000010,000011) (000011,000010) (000100,000101) (000101,000100) (000110,000111) (000111,000110)
(001000,001001) (001001,001000) (001010,001011) (001011,001010) (001100,001101) (001101,001100) (001110,001111) (001111,001110)
(010000,010001) (010001,010000) (010010,010011) (010011,010010) (010100,010101) (010101,010100) (010110,010111) (010111,010110)
(011000,011001) (011001,011000) (011010,011011) (011011,011010) (011100,011101) (011101,011100) (011110,011111) (011111,011110)
(100000,100001) (100001,100000) (100010,100011) (100011,100010) (100100,100101) (100101,100100) (100110,100111) (100111,100110)
(101000,101001) (101001,101000) (101010,101011) (101011,101010) (101100,101101) (101101,101100) (101110,101111) (101111,101110)
(110000,110001) (110001,110000) (110010,110011) (110011,110010) (110100,110101) (110101,110100) (110110,110111) (110111,110110)
(111000,111001) (111001,111000) (111010,111011) (111011,111010) (111100,111101) (111101,111100) (111110,111111) (111111,111110)

输出差分
1110 1110 1011 1011 1010 1010 0101 0101
1100 1100 1101 1101 0110 0110 1001 1001
1001 1001 1100 1100 1010 1010 0111 0111
1100 1100 1100 1100 0011 0011 1111 1111
1011 1011 1101 1101 0110 0110 1010 1010
1001 1001 1111 1111 0011 0011 1100 1100
1010 1010 0111 0111 1010 1010 1001 1001
1001 1001 1010 1010 0011 0011 1101 1101

差分输出分布
0000                                                                                               
0001                                                                                               
0010                                                                                               
0011                          0011 0011 0011 0011 0011 0011                                         
0100                                                                                               
0101                          0101 0101                                                             
0110                          0110 0110 0110 0110                                                   
0111                          0111 0111 0111 0111                                                   
1000                                                                                               
1001                          1001 1001 1001 1001 1001 1001 1001 1001 1001 1001                     
1010                          1010 1010 1010 1010 1010 1010 1010 1010 1010 1010 1010 1010           
1011                          1011 1011 1011 1011                                                   
1100                          1100 1100 1100 1100 1100 1100 1100 1100 1100 1100                     
1101                          1101 1101 1101 1101 1101 1101                                         
1110                          1110 1110                                                             
1111                          1111 1111 1111 1111  
```



## Details

- DES : [中文文档](http://geekeye.cc/post/impl-of-des/)
- AES : [中文文档](http://geekeye.cc/post/impl-of-aes/)

