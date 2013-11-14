package pal.api.util;

public class SmartMath {

	static double N;
	static double log2 = 1.0/Math.log(2);
	
	//"Poor Man's" factorial algorithm
	public static double factorial(int n)
	{
		if( n>170 ) return Double.POSITIVE_INFINITY;
		if( n<2 ) return 1;
	
		double p	= 1;
		double r	= 1;
		N			= 1;
		int h		= 0;
		int shift	= 0;
		int high	= 1;		
		int log2n	= (int) Math.floor(Math.log(n) * log2);

		while( h!=n )
		{
			shift	+= h;
			h		 = n >> log2n--;
			int len  = high;
			high	 = ( h&1 ) == 1 ? h : h - 1;
			len		 = (high - len) >> 1;

			if( len>0 ) r *= p *= product(len);
		}

		r *= Math.pow(2, shift);
		return r;
	}

	private static double product(int n)
	{
		int m = n >> 1;
		if( m==0 )  return N += 2;
		if( n==2 )  return (N += 2) * (N += 2);
		return product( n-m ) * product(m);
	}
	
	public static void main(String ... args)
	{
		double time = System.nanoTime();

		for(int i=1; i<170; i++)
			System.out.println(i+"! = "+factorial(i));
		
		System.out.println((int)((System.nanoTime()-time)/1000000)+"ms");
	}
}
