package org.jordon.security.core;
import java.util.ArrayList;
import java.util.List;
public class MathUtil {
	/**
	 * 十进制整数转化为n位二进制
	 * @param decimal  十进制整数
	 * @param n        二进制位数
	 * @return         n位二进制
	 */
	public static String decimalToBinary(long decimal,int n) {
		long l=(long)Math.pow(2,n);
		String binary=Long.toBinaryString((decimal&(l-1))+l).substring(1);
		return binary;
	}

	/**
	 * 十进制整数转化为n位十六进制
	 * @param decimal  十进制整数
	 * @param n        十六进制位数
	 * @return         n位十六进制
	 */
	public static String decimalToHexadecimal(long decimal,int n) {
		long l=(long)Math.pow(16,n);
		String hexadecimal=Long.toHexString((decimal&(l-1))+l).substring(1);
		return hexadecimal;
	}

	/**
	 * 二进制转化为十进制
	 * @param binary  二进制
	 * @return        十进制
	 */
	public static long binaryToDecimal(String binary) {
		long decimal=0;
		byte[] b=binary.getBytes();
		for(int i=0;i<b.length;i++) b[i]-='0';
		long k=1;
		for(int i=b.length-1;i>=0;i--,k*=2)
			decimal+=b[i]*k;
		return decimal;
	}

	/**
	 * 十六进制转化为十进制
	 * @param hexadecimal  十六进制
	 * @return             十进制
	 */
	public static long hexadecimalToDecimal(String hexadecimal) {
		long decimal=0;
		byte[] b=hexadecimal.getBytes();
		for(int i=0;i<b.length;i++) {
			if(b[i]>='A'&&b[i]<='F') b[i]-='A'-10;
			else if(b[i]>='a'&&b[i]<='f') b[i]-='a'-10;
			else b[i]-='0';
		}
		long k=1;
		for(int i=b.length-1;i>=0;i--,k*=16)
			decimal+=b[i]*k;
		return decimal;
	}


	/**
	 * 欧几里德算法
	 * @param a
	 * @param b
	 * @return   (a,b)
	 */
	public static long gcd(long a,long b) {
		//辗转相除法
		if(a<b){
			long temp=a;
			a=b;
			b=temp;
		}
		if(b==0) return a;
		else return gcd(b,a%b);
	}

	/**
	 * 扩展欧几里德算法
	 * (a,b)=ax+by（其中a>=b）
	 * @param a
	 * @param b
	 * @return  result[0]=x，result[1]=y
	 */
	public static long[] exGcd(long a,long b) {
		if(a<b){
			long temp=a;
			a=b;
			b=temp;
		}
		long[] result=new long[2];
		if(b==0){
			result[0]=1;
			result[1]=0;
			return result;
		}
		long[] temp=exGcd(b,a%b);
		result[0]=temp[1];
		result[1]=temp[0]-a/b*temp[1];
		return result;
	}

	/**
	 * 计算多个数的最大公因子
	 * @param l  数值列表
	 * @return   (l[0],l[1],...,l[n])
	 */
	public static List<Long> gcd(List<Long> l) {
		if(l.size()==1) return l;
		l.set(0,gcd(l.get(0),l.get(1)));
		l.remove(1);
		return gcd(l);
	}

	/**
	 * 判断是否为素数
	 * @param a
	 * @return
	 */
	public static boolean isPrime(long a) {
		if(a<=3) return a>1;   //0和1不是素数也不是复合数
		if(a%2==0||a%3==0) return false;
		for(long i=5;i*i<=a;i+=6)
			if(a%i==0||a%(i+2)==0) return false;
		return true;
	}

	/**
	 * 最小公倍数
	 * @param a
	 * @param b
	 * @return  [a,b]
	 */
	public static long lcm(long a,long b) {
		return a*b/gcd(a,b);   //[a,b]=a*b/(a,b)
	}

	/**
	 * 计算多个数的最小公倍数
	 * @param l  数值列表
	 * @return   [l[0],l[1],...,l[n]]
	 */
	public static List<Long> lcm(List<Long> l) {
		if(l.size()==1) return l;
		l.set(0,lcm(l.get(0),l.get(1)));
		l.remove(1);
		return lcm(l);
	}

	/**
	 * 欧拉函数
	 * @param a
	 * @return  φ(a)
	 */
	public static long Euler(long a) {
		if(isPrime(a)) return a-1;   //如果a是素数，φ(a)=a-1
		//φ(a)=a*(1-1/p1)*(1-1/p2)*...*(1-1/pn)（其中p1,p2,...,pn为a的素因子）
		long phi=a;
		if(a%2==0) phi/=2;
		while(a%2==0) a/=2;
		for(long i=3;i<=a;i+=2)
			if(isPrime(i)&&a%i==0){
				phi-=phi/i;
				while(a%i==0) a/=i;
			}
		return phi;
	}

	/**
	 * 模指运算
	 * @param b
	 * @param n
	 * @param m
	 * @return   b^n(mod m)
	 */
	public static long modExp(long b,long n,long m) {
		if(n < 0)
			return modExp(congruenceEquation(b,1,m), -n, m);
		long result=1;
		b = b % m;
		List<Long> list = new ArrayList<>();
		char[] ns = Long.toBinaryString(n).toCharArray();
		for(int i=0;i<ns.length;i++,b=b*b%m) list.add(b);
		for(int i=0;i<ns.length;i++)
			if(ns[i]=='1')
				result = result * list.get(list.size() - 1 - i) % m;
		return result;
	}

	/**
	 * 解同余方程ax=b(mod m)（其中a < m）
	 * @param a
	 * @param b
	 * @param m
	 * @return  x
	 */
	public static long congruenceEquation(long a, long b, long m) {
		if(b % gcd(a, m) != 0)
			return - 1;
		long result = (b / gcd(a, m)) * exGcd(a, m)[1];
		if(result < 0)
			result += m;
		return result;
	}

	/**
	 * a模m的阶δm(a)
	 * @param a
	 * @param m
	 * @return   δm(a)（-1，(a,m)!=1）
	 */
	public static long order(long a,long m) {
		if(gcd(a,m)!=1) return -1;
		long n=Euler(m);
		for(long i=2;i<n;i++)
			if(n%i==0){
				if((long)Math.pow(a,i)%m==1) return i;
				while(n%i!=0) n/=i;
			}
		return Euler(m);
	}

	/**
	 * 模m所有的原根
	 * @param m
	 * @return  原根列表
	 */
	public static List<Long> primitiveRoot(long m) {
		if(m<2) return null;
		List<Long> l=new ArrayList<Long>();
		if(m==2) l.add((long)1);        //2的原根是1
		else if(m==4) l.add((long)3);   //4的原根是3
		else{
			long temp=m,phi_m=Euler(m);
			//若m形如p^l或2*p^l（p为奇素数），则m有原根
			for(long i=3;i<temp;i+=2)
				if(isPrime(i)&&temp%i==0){
					while(temp%i==0) temp/=i;
					if(temp>2) return null;
				}
			long count=Euler(phi_m);   //若m有原根，则原根个数为φ[φ(m)]
			for(long i=1;i<m;i++){
				//若(i,m)=1且δm(i)=φ(m)，则i是m的一个原根
				if(gcd(i,m)==1&&order(i,m)==phi_m)
					l.add(i);
				if(l.size()==count) break;
			}
		}
		return l;
	}

	/**
	 * Legendre符号(n/p)
	 * @param n
	 * @param p
	 * @return  (n/p)（1，n为模p的二次剩余；-1，n为模p的二次非剩余；0，p|n）
	 */
	public static int Legendre(long n,long p) {
		if(p<3||!isPrime(p)) return -2;
		n=n%p;
		if(n==0) return 0;
		if(n==1) return 1;
		if(n==-1) return pow((p-1)/2,1);
		if(n==2) return pow((p*p-1)/8,1);
		int Legendre=1;
		if(isPrime(n)) Legendre*=Legendre(p,n)*pow((p-1)/2,(n-1)/2);
		else{
			if(n%2==0){
				int a=Legendre(2,p);
				while(n%2==0){
					n/=2;
					Legendre*=a;
				}
			}
			for(int i=3;i<=n;i+=2)
				if(isPrime(i)){
					int a=Legendre(i,p);
					while(n%i==0){
						n/=i;
						Legendre*=a;
					}
				}
		}
		return Legendre;
	}

	public static int pow(long p,long q) {
		if(p%2!=0&&q%2!=0) return -1;
		else return 1;
	}

	/**
	 * 模p所有二次剩余
	 * @param p
	 * @return  二次剩余列表
	 */
	public static List<Long> quadraticResidue(long p) {
		if(p<3||!isPrime(p)) return null;
		List<Long> l=new ArrayList<Long>();
		List<Long> l1=new ArrayList<Long>();
		long t=Legendre(-1,p),t2=0;
		for(long i=1;i<(p+1)/2;i++) {
			if(Math.sqrt(i)==Math.floor(Math.sqrt(i))){
				l.add(i);
				if(t==1) l1.add(p-i);
				continue;
			}
			if((t2=Legendre(i,p))==1){
				l.add(i);
			}
			if(t2*t==1) l1.add(p-i);
			if(l.size()+l1.size()==(p-1)/2) break;
		}
		for(int i=l1.size()-1;i>=0;i--) l.add(l1.get(i));
		return l;
	}
}