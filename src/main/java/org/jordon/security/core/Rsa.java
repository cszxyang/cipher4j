package org.jordon.security.core;
import java.util.Random;
public class Rsa {
	private int p,q,phi_n,d;
	public int n,e;
	/**
	 * 无参构造函数
	 */
	public Rsa() {
		Random r=new Random();
		//随机地选择两个大素数p和q，而且保密
		do{
			p=Math.abs(r.nextInt((int)Math.pow(2,7))+1);
		}while(!MathUtil.isPrime((long)p));
		do{
			q=Math.abs(r.nextInt((int)Math.pow(2,7))+1);
		}while(!MathUtil.isPrime((long)q));
		//计算n=pq，将n公开
		n=p*q;
		System.out.println("n="+n);
		//计算φ(n)=(p-1)(q-1)，对φ(n)保密
		phi_n=(p-1)*(q-1);
		//随机地取一个正整数e，1<e<φ(n)且(e,φ(n))=1，将e公开
		do{
			e=Math.abs(r.nextInt((int)phi_n));
		}while(e <= 1 ||e >= phi_n || MathUtil.gcd(e,phi_n) != 1);
		System.out.println("e="+e);
		//根据ed = 1(mod φ(n))，求出d，并对d保密
		d = (int)MathUtil.congruenceEquation(e,1, phi_n);
	}

	/**
	 * 带参构造函数，用于测试
	 * @param p
	 * @param q
	 * @param e
	 */
	public Rsa(int p,int q,int e) {
		//选择两个大素数p和q，而且保密
		this.p=p;
		this.q=q;
		//计算n=pq，将n公开
		n=p*q;
		System.out.println("n="+n);
		//计算φ(n)=(p-1)(q-1)，对φ(n)保密
		phi_n=(p-1)*(q-1);
		//取一个正整数e，1<e<φ(n)且(e,φ(n))=1，将e公开
		this.e=e;
		System.out.println("e="+e);
		//根据ed=1(mod φ(n))，求出d，并对d保密
		d=(int)MathUtil.congruenceEquation(e,1,phi_n);
	}

	/**
	 * 加密
	 * @return   密文C=M^e(mod n)
	 */
	public String encrypt(String plainText) throws Exception {
		byte[] M=plainText.getBytes("UTF-8");
		char[] C=new char[M.length];
		for(int i=0;i<M.length;i++) C[i]=(char)MathUtil.modExp(M[i],e,n);
		String cipherText=new String(C);
		return cipherText;
	}

	/**
	 * 解密
	 * @return   明文M=C^d(mod n)
	 */
	public String decrypt(String cipherText) throws Exception {
		char[] C=cipherText.toCharArray();
		byte[] M=new byte[C.length];
		for(int i=0;i<C.length;i++) M[i]=(byte)MathUtil.modExp(C[i],d,n);
		String plainText=new String(M,"UTF-8");
		return plainText;
	}
}
