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

- DES : [中文文档](http://geekeye.cc/post/impl-of-des/)
- AES : [中文文档](http://geekeye.cc/post/impl-of-aes/)

