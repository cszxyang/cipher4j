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



## Details

- DES : [中文文档](https://github.com/jordonyang/cipher4j/tree/master/details/des_Zh.md)
- AES : [中文文档](https://github.com/jordonyang/cipher4j/tree/master/details/aes_Zh.md)

