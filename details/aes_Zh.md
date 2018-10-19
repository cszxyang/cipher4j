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

// todo



## 测试

```java
  	@Test
    public void testAES() {
        short[] ptBytes = {
                0x00, 0x01, 0x00, 0x01, 0x01, 0xa1, 0x98, 0xaf,
                0xda, 0x78, 0x17, 0x34, 0x86, 0x15, 0x35, 0x66
        };

        short[] keyBytes = {
                0x00, 0x01, 0x20, 0x01, 0x71, 0x01, 0x98, 0xae,
                0xda, 0x79, 0x17, 0x14, 0x60, 0x15, 0x35, 0x94
        };
        AESCipherService service = new AESCipherService();
        service.coreOperation(ptBytes, keyBytes);
    }
```

结果

```
roundKeys: 
00012001710198aeda79171460153594
589702d129969a7ff3ef8d6b93fab8ff
77fb140d5e6d8e72ad8203193e78bbe6
cf119abf917c14cd3cfe17d40286ac32
8380b9c812fcad052e02bad12c8416e3
ccc7a8b9de3b05bcf039bf6ddcbda98e
9614b13f482fb483b8160bee64aba260
b42e617cfc01d5ff4417de1120bc7c71
513ec2cbad3f1734e928c925c994b554
68ebe216c5d4f5222cfc3c07e5688953
1b4c0fcfde98faedf264c6ea170c4fb9
0000200070a0000100010020e60000f2
N = 1
6363b76351e0637c637c63b78e636389
63e06389517c63636363b77c8e6363b7
1794c52f266f4e2aa81bf189765ae9fc
4f03c7fe0ff9d4555bf47ce2e5a05103
N = 2
847bc6bb769948fc39bf1098d9e0d17b
8499107b76bfd1bb39e0c6fcd97b4898
c8e6b0e85cc0a699734f518ef46f8168
bf1da4e502ad28ebdecd5297ca173a8e
N = 3
08a449d9779534e91dbd008874f08019
0895001977bd80d91df049e974a43488
ad20b6bf6b54a10d91d45f57a3f33b07
62312c00fa28b5c0ad2a4883a1759735
N = 4
aac771632d34d5ba95e552ec329d8896
aa3452962de58863959d71ba32c7d5ec
d7a29bb4851c66dc469d3f270f2f6b87
5422227c97e0cbd9689f85f623ab7d64
N = 5
2093931088e11f3545db97422662ff43
20e1974388dbff104562933526931f42
ac183190922f86878a1a4554bf784d62
60df99294c14833b7a23fa3963c5e4ec
N = 6
d09eeea529faece2da262d12fba669ce
d0fa2dce292669a5daa6eee2fb9eec12
4d86393bf47b2965524686e2aae19040
db928804bc549de6ea508d0cce4a3220
N = 7
b94fc4f265205e8e87535dfe8bd623b7
b9205db7655323f287d6c48e8b4f5efe
e3a9e1d8ee547d203ee94b877c096170
578780a41255a8df7afe95965cb51d01
N = 8
5b17cd49c9fcc29edabb2a904ad5a47c
5bfc2a7cc9bba449dad5cd9e4a17c290
ffba77c3b21afacd98b9374affa96930
ae84b5081f25edf97191fe6f363ddc64
N = 9
e45fd530c03f5599a381bba805278643
e43fbb43c0818630a327d599055f55a8
6a0f7335b578063c7810852516ec134e
02e4912370acf31e54ecb922f3849a1d
N = 10
7769812651910d7220ce56930d5fb8a4
779156a451ceb826205f81720d690d93
6cdd596b8f5642cbd23b47981a65422a
```



参考

- [*FIPS* *197* -- Advanced Encryption Standard](https://www.baidu.com/link?url=QhLf4SboJVjBsfe1JGiOB6nzvzmo0xZ0QU_Mxoft1fB6bJK_H8o2ffPXMY4-cv-W&wd=&eqid=bea9a25000020bc8000000065bc6115b)