package pal.api.algo;

import java.util.BitSet;
import java.util.HashSet;

import pal.api.signature.GraphSignature;
import pal.api.struct.GenericGraph;

public class Ullman
{

	private static int n;
	private static boolean areIsomorph;
	private static HashSet<Pair> F = null;
	
	public static<T> boolean areIsomorph(GenericGraph<T> g, GenericGraph<T> gi)
	{ return areIsomorph(g, gi, false); }
	
	public static<T> boolean areIsomorph(GenericGraph<T> g, GenericGraph<T> gi, boolean details)
	{
		if( ((n = g.vsize())!=gi.vsize() ) || ( g.esize()!=gi.esize() ))	return false;
		//n++;
		areIsomorph	= false;
		BitSet p	= new BitSet();
		p.set(0, n*n, false);

//		System.out.println(g.vsize()+" "+gi.vsize());

		if(details)
		{
			System.out.println("\t n           : "+n);
			System.out.println("\t areIsomorph : "+areIsomorph);
			System.out.println("\t p           : "+p);
			System.out.println("\t -------------");
		}
		
		if(details)
		{
			System.out.println("\t -------------");
		}

		for( int i = 0; i < n; i++ )
		for( int j = 0; j < n; j++ )
		{
			if( (g.outDegree(i) == gi.outDegree(j)) && (g.inDegree(i) == gi.inDegree(j)) )
			{
				//p.set(i*n+j, !(gi.successors(j).contains(j) ^ g.successors(i).contains(i)));
				p.set(i*n+j, !(gi.hasEdge(j,j) ^ g.hasEdge(i,i)));
				//p.set(i*n+j, true);
			}
		}

		F = new HashSet<Pair>();
		backtrack(p, 0);
		
		if(details)
		{
			System.out.println("\t -------------");
			System.out.println("\t iso         : "+areIsomorph);
			
		}

		if(areIsomorph)
		{
		
		}
		else
		{
			/*
			for(int i=0; i<n; i++)
			{
				for(int j=0; j<n; j++)
					System.out.print((p.get(i*n+j)?"x":".")+" ");
				System.out.println();
			}
			System.out.println();
			*/
			//System.out.println(g);
		}

		//if(areIsomorph){
		//	return new GraphSignature(g).toCanonicalString().equals(new GraphSignature(gi).toCanonicalString());
		//}
		return areIsomorph;
	}

	private static void backtrack(BitSet p, int i)
	{
		//if(!forward_checking(p)) return;
		
		if( areIsomorph ) return;
		
		if( i>n-1 )
		{
			if( F.size()==n ) areIsomorph = true;
			return;
		}

		for( int j=0; j<n; j++ )
		{
			if( p.get(i*n+j) )
			{
				Pair pair = new Pair(i,j);

				F.add(pair);
				BitSet p_prime = (BitSet) p.clone();

				for( int k = i + 1; k < n; k++ )
					p_prime.set(k*n+j,false);
				
				if( forward_checking(p_prime) )
					backtrack(p_prime, i + 1);
				
				F.remove(pair);
			}
		}
	}

	static class Pair{
		public int i =0;
		public int j = 0;
		
		public Pair(int i, int j)
		{
			this.i=i;
			this.j=j;
		}
		public int hashCode()
		{ return 37 * (37 * 17 + i) + j; }
	}

	private static boolean forward_checking(BitSet p)
	{
		for( int k=0; k<n; k++ ) if( fail(p,k) ) return false;
		return true;
	}
	
	private static boolean fail(BitSet p, int k)
	{ return p.get(k*=n, k+n).isEmpty(); }
	
}