package pal.api.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import pal.api.core.Edge;
import pal.api.decorator.GraphBehaviorDecorator;
import pal.api.decorator.StdOutDecorator;
import pal.api.struct.GenericGraph;
import pal.api.struct.RedBlackTree;
import pal.api.struct.BinaryTreeNode;

public class KiralyGenerator<T>
{

    private GraphBehaviorDecorator<T> decorator  =  null;
    
    public KiralyGenerator()
    { this(new StdOutDecorator<T>()); }
    
    public KiralyGenerator( GraphBehaviorDecorator<T> handler )
    { this.decorator = handler; }
    
    public void generate( int ... sequence )
    {
        generate( sequence,sequence.length,new GenericGraph<T>() );
        decorator.finish();
    }
    
    public void generate( int[] sequence, int n, GenericGraph<T> g )
    {
        if (n == 3)
        {
        	int i = sequence[0]*100 + sequence[1]*10 + sequence[2];
     
        	switch(i)
        	{
        		case 0		: decorator.handle(g); break;
        		case 11		:
        		{
        			g.addEdge(1, 2);
        			decorator.handle(g);
        		} break;
        		
        		case 101	:
        		{
        			g.addEdge(0, 2);
        			decorator.handle(g);
        		} break;
        		
        		case 110	:
        		{
        			g.addEdge(0, 1);
        			decorator.handle(g);
        		} break;
        		
        		case 112	:
        		{
        			g.addEdge(0, 2);
                    g.addEdge(1, 2);
                    decorator.handle(g);
        		} break;
        		
        		case 121	:
        		{
        			g.addEdge(0, 1);
                    g.addEdge(1, 2);
                    decorator.handle(g);
        		} break;
        		
        		case 211	:
        		{
        			g.addEdge(0, 1);
                    g.addEdge(0, 2);
                    decorator.handle(g);
        		} break;
        		
        		case 222	:
        		{
        			g.addEdge(0, 1);
                    g.addEdge(0, 2);
                    g.addEdge(1, 2);
                    decorator.handle(g);
        		} break;
        	}
            return;
        }

        traverse( sequence,new RedBlackTree( n-1 ),g,n,sequence[n-1] );
    }
    
    private void setEdges( GenericGraph<T> g,BitSet chiP,int n )
    {
        for( int i = chiP.nextSetBit(0); i >= 0; i = chiP.nextSetBit(i+1) )
        {
            if( g.hasEdge( i,n ) )  continue;
            g.addEdge(i, n);
        }
    }
    
    private void handleLeaf(int[] degreeSequence, RedBlackTree tree, BinaryTreeNode current, BitSet chiP, GenericGraph<T> g, int n, int degree)
    {
        if (chiP.cardinality() == degree)
            generate(reduce(degreeSequence, chiP, n - 1), n - 1, new GenericGraph<T>(g));

        if (current.isLeftChild)	upFromLeft(degreeSequence, tree, current.parent, chiP, g, n, degree);
        else						upFromRight(degreeSequence, tree, current.parent, chiP, g, n, degree);
    }
    
    private void traverse(int[] degreeSequence, RedBlackTree tree, GenericGraph<T> g, int n, int degree)
    {
        BinaryTreeNode startLeaf	= tree.getLeftmostLeaf(degree);
        BitSet chiP			= new BitSet();
        int i				= 0;
        
        while( i<degree ) chiP.set(i++);

        setEdges(g, chiP, n - 1);
        handleLeaf(degreeSequence, tree, startLeaf, chiP, g, n, degree);
    }

    private void traverseTreeDown(int[] degreeSequence, RedBlackTree tree, BinaryTreeNode current, BitSet chiP, GenericGraph<T> g, int n, int degree)
    {
        if( current.isLeaf() ) handleLeaf(degreeSequence, tree, current, chiP, g, n, degree);
        else
        {
            if( chiP.cardinality() < degree && isGraphical(degreeSequence, chiP, n - 1) )
            {
            	g.addEdge(current.r, n - 1);
            	chiP.set(current.r);
            	traverseTreeDown(degreeSequence, tree, current.left, chiP, g, n, degree);
            }
            else traverseTreeDown(degreeSequence, tree, current.right, chiP, g, n, degree);
        }
    }

    private void upFromLeft(int[] degreeSequence, RedBlackTree tree, BinaryTreeNode current, BitSet chiP, GenericGraph<T> g, int n, int degree)
    {
        chiP.clear(current.r);
        removeLast(g);

        if (chiP.cardinality() < degree)
            traverseTreeDown(degreeSequence, tree, current.right, chiP, g, n, degree);
        else
        {
            if (current.isLeftChild)	upFromLeft(degreeSequence, tree, current.parent, chiP, g, n, degree);
            else						upFromRight(degreeSequence, tree, current.parent, chiP, g, n, degree);
        }
    }
    
    private void removeLast( GenericGraph<?> g )
    {
        List<Edge>  edges  =  new ArrayList<Edge>();
        int         size   =  g.esize()-1;
        int         i      =  0;
        
        while( i<size ) edges.add( g.edges.get(i++) );
        
        g.edges = edges;
    }
    
    private void upFromRight(int[] degreeSequence, RedBlackTree tree, BinaryTreeNode current, BitSet chiP, GenericGraph<T> g, int n, int degree)
    {
        if (current.parent == null) return;
        
        else
        {
            if (current.isLeftChild)	upFromLeft(degreeSequence, tree, current.parent, chiP, g, n, degree);
            else						upFromRight(degreeSequence, tree, current.parent, chiP, g, n, degree);
        }
    }
    
    private int[] reduce( int[] degreeSequence, BitSet chiP, int n )
    {
        int[] reducedDegreeSeq = new int[degreeSequence.length];
        
        for (int i = 0; i < degreeSequence.length; i++)
        {
            if( chiP.get(i) )	reducedDegreeSeq[i] = degreeSequence[i] - 1;
            else if( i==n )		reducedDegreeSeq[i] = degreeSequence[i] - chiP.cardinality();
            else				reducedDegreeSeq[i] = degreeSequence[i];
        }

        return reducedDegreeSeq;
    }
    
   
    
    private boolean isGraphical( int[] degreeSequence , BitSet chiP , int n )
    {
    	int[] sequence	= reduce( degreeSequence,chiP,n );
    	int sum			= 0;
    	n				= sequence.length;
    	
    	Arrays.sort(sequence);
    	
    	for( int k = 1; k < n + 1; k++ )
    	{
    		sum += sequence[k - 1];
    		int stubCount		= k * (k - 1);
    		int remainingDegree	= 0;
    	              
    		for( int i = k; i < n; i++ )
    			remainingDegree += Math.min(k, sequence[i - 1]);

    		if( sum<=stubCount+remainingDegree ) continue;
    		return false;
    	}

    	return ( sum%2==0 );
    }
    
}
