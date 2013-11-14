package pal.api.generator;

import java.util.ArrayList;
import java.util.HashSet;

import pal.api.core.Edge;
import pal.api.struct.GenericGraph;

public class NaiveGenerator<T>
{
	private Filter<T> decorator			=  null;
    
    public NaiveGenerator( Filter<T> processr )
    { this.decorator = processr; }
    
    public void generate( int ... sequence )
    { generate(new ArrayList<Edge>(), 0,sequence ); }
	
	private void generate(ArrayList<Edge> edges, int depth, int ... sequence)
	{
        int k = sequence[0];

        if( k<0 ) return;

        if( k==0 )
        {
            if( sequence.length>1 )
            {
            	int n		= sequence.length - 1;
                int[] sub	= new int[n];
                System.arraycopy(sequence, 1, sub, 0, n);

                generate( new ArrayList<Edge>(edges) , depth+1 , sub );
            }
            else
            {
            	GenericGraph<T> g = new GenericGraph<T>();
            	
                for( Edge e : edges ) g.addEdge( e );

                decorator.process(g);
                
                return;
            }
        }
        else
        {
        	int n		= sequence.length-1;
            int[] sub	= new int[n];
            System.arraycopy(sequence, 1, sub, 0, n);
            subtree( new HashSet<Integer>() , k , 0 , 0 , depth , new ArrayList<Edge>(edges) , sub );
        }
    }

    private void subtree(HashSet<Integer> permutation, int k, int depth, int start, int offset, ArrayList<Edge> tree, int ... sequence)
    {
    	int n = sequence.length;
        if( depth==k )
        {
            int[] sub = new int[n];
            System.arraycopy( sequence,0,sub,0,n );
            tree = new ArrayList<Edge>(tree);

            for( int in : permutation )
            {
            	sub[in]--;
            	tree.add(new Edge( offset , offset+in+1 ));
            }
            
            generate( tree, offset+1, sub );

            return;
        }
        
        while( start<n )
        {
        	permutation.add(start);
            subtree( permutation , k , depth+1 , start+1 , offset , tree , sequence );
            permutation.remove(start++);
        }
    }

}
