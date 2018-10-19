## Introduction

Pure Implementations for encryption algorithms including DES, RSA, AES



## Usage

Take DES Implementation for example

```java
String plaintext = "01234567", key = "12345678";
CipherService desService = new DESCipherService();
String encryptedText = desService.encrypt(plaintext, key);
desService.decrypt(encryptedText,key);
```



## Details

- DES : [中文文档](https://github.com/jordonyang/cipher4j/tree/master/details/des_Zh.md)
- AES : [中文文档](https://github.com/jordonyang/cipher4j/tree/master/details/aes_Zh.md)

