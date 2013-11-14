package pal.api.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pal.api.core.Edge;
import pal.api.core.Permutation;
import pal.api.decorator.NonIsomorphicDecorator;
import pal.api.generator.KiralyGenerator;
import pal.api.util.SmartMath;
import rabbit.struct.common.Matrix;

public class GenericGraph<T>
{

    public List<Edge>					edges			= new ArrayList<Edge>();    
    public int							index			= -1;
    public List<Integer>				colors			= new ArrayList<Integer>();
    private Map<Integer, List<Integer>>	connections		= null;
    
    public GenericGraph()
    {}

    public GenericGraph(GenericGraph<T> other)
    {
        for( Edge edge : other.edges )
            this.addEdge( edge.from, edge.to, edge.w );

        this.index = other.index;
    }
    
    public GenericGraph(int vertices)
    { index = vertices - 1; }
    
    @SuppressWarnings("unchecked")
	public boolean equals(Object o)
    { return (o instanceof GenericGraph) ? this.getSortedEdgeString().equals(((GenericGraph<T>) o).getSortedEdgeString()) : false; }
    
    public int hashCode()
    { return getSortedEdgeString().hashCode(); }
    
    public void makeVertex()
    { this.index++; }
    
    public GenericGraph<T> getPermutedGraph( int ... permutation )
    {
    	GenericGraph<T> graph = new GenericGraph<T>();

        for( Edge e : this.edges )
            graph.addEdge( permutation[e.from], permutation[e.to], e.w );

        return graph;
    }
    
    public int getColor(int index)
    { return (index >= colors.size()) ? 0 : colors.get(index); }
    
    public void setColors(int ... colors)
    {
        for (int color : colors)
            this.colors.add(color);
    }
    
    public List<Integer> getPermutedColorString(Permutation p)
    {
        List<Integer> permutedColors = new ArrayList<Integer>();
        
        for (int i = 0; i < this.colors.size(); i++)
            permutedColors.add(this.colors.get(p.get(i)));

        return permutedColors;
    }
    
    private void updateMaxVertexIndex(int a, int b)
    {
        if( a>index )  index = a;
        if( b>index )  index = b;
    }
    
    public Edge getEdge(int i, int j)
    {
    	for( Edge e : this.edges ) if (( e.from==i && e.to==j ) || ( e.from==j && e.to==i ))
    		return e;
    	return null;
    }
    
    public void makeIsolatedVertex()
    { this.index++; }
    
    public void addEdge(int a, int b)
    {
        Edge e = getEdge(a, b);
        
        if( e == null )
        {
        	if( a < b )	this.edges.add(new Edge(a, b));
        	else 		this.edges.add(new Edge(b, a));
        }
        else e.w++;
        
        this.updateMaxVertexIndex(a, b);
    }
    
    public void addEdge(int a, int b, int c)
    {
        this.edges.add(new Edge(a, b, c));
        this.updateMaxVertexIndex(a, b);
    }
    
    public List<Integer> getConnected(int vertexIndex)
    { return calculateConnectionTable().get(vertexIndex); }
    
    public List<Integer> getSameColorConnected(int vertexIndex)
    {
        int color = getColor(vertexIndex);
        List<Integer> connected = new ArrayList<Integer>();

        for (Edge edge : this.edges)
        {
            if ( edge.from==vertexIndex && getColor(edge.to)==color )       connected.add(  edge.to  );
            else if ( edge.to==vertexIndex && getColor(edge.from)==color )  connected.add( edge.from );
            else continue;
        }
        
        return connected;
    }
    
    public boolean isConnected(int i, int j)
    {
        for (Edge e : this.edges) if (( e.from==i && e.to==j ) || ( e.from==j && e.to==i ))
        	return true;
        return false;
    }
    
    public boolean isConnected()
    {
    	return this.getNumberOfSpanningTrees() >0;
    }
    
    public List<List<Integer>> getComponents()
    {
    	Map<Integer, List<Integer>> connectionTable	= this.getConnectionTable();
    	BitSet visited								= new BitSet();
    	List<List<Integer>> components				= new ArrayList<List<Integer>>();

    	for (int i = 0; i < vsize(); i++)
    	{
        	if (visited.get(i)) continue;
        	else
        	{
        		List<Integer> component = new ArrayList<Integer>();
        		getComponent(i, connectionTable, visited, component);
        		components.add(component);
        	}
        }
        return components;
    }
    
    private void getComponent(int vertex, Map<Integer, List<Integer>> connectionTable, BitSet visited, List<Integer> component)
    {
    	component.add(vertex);
    	visited.set(vertex);

    	for( int neighbour : connectionTable.get(vertex) )
    	{
    		if( visited.get(neighbour) ) continue;
    		getComponent(neighbour, connectionTable, visited, component);
    	}
    }
    
    private Map<Integer, List<Integer>> calculateConnectionTable()
    {
        Map<Integer, List<Integer>> connectionTable =  new HashMap<Integer, List<Integer>>();
        
        for( Edge e : this.edges )
        {
        	if( connectionTable.containsKey(e.from) ) connectionTable.get(e.from).add(e.to);
            else
            {
                List<Integer> connected = new ArrayList<Integer>();
                connected.add(e.to);
                connectionTable.put(e.from, connected);
            }
            if( connectionTable.containsKey(e.to) )
            {
                connectionTable.get(e.to).add(e.from);
            }
            else
            {
                List<Integer> connected = new ArrayList<Integer>();
                connected.add(e.from);
                connectionTable.put(e.to, connected);
            }
        }
        
        for( int i = 0; i <= index; i++ )
        {
            if (connectionTable.containsKey(i)) continue;
            else								connectionTable.put(i, new ArrayList<Integer>());
        }

        return connectionTable;
    }
    
    public Map<Integer, List<Integer>> getConnectionTable()
    {
        if( connections==null )
        	connections = calculateConnectionTable();

        return connections;
    }
    
    public void bfs(int vertex, List<Integer> visited, Map<Integer, List<Integer>> table)
    {
    	
    }
    
    public void dfs(int vertex, List<Integer> visited, Map<Integer, List<Integer>> table)
    {
        visited.add(vertex);

        try
        {
            for( int connected : table.get(vertex) )
            {
                if( visited.contains(connected) )	continue;
                else								dfs(connected, visited, table);
            }
        }
        catch (NullPointerException e)
        {}
    }
    
    public String getSortedPermutedColoredOnlyEdgeString(int ... p)
    {
        List<String> edgeStrings = new ArrayList<String>();
        
        for (Edge e : this.edges)
            edgeStrings.add(e.getSortedPermutedColorOnlyString(p, colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedPermutedColoredEdgeString(int ... p)
    {
        List<String> edgeStrings = new ArrayList<String>();
        
        for (Edge e : this.edges)
            edgeStrings.add(e.getSortedPermutedColoredString(p, colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedColorOnlyEdgeString()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges)
            edgeStrings.add(e.toSortedColorOnlyString(colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getColorOnlyEdgeString()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges)
            edgeStrings.add(e.toSortedColorOnlyString(colors));

        return edgeStrings.toString();
    }
    
    public String getSortedColoredEdgeString()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges)
            edgeStrings.add(e.toSortedColoredString(colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedPermutedEdgeString(int ... p)
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges)
            edgeStrings.add(e.getSortedPermutedString(p));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedEdgeStringWithEdgeOrder()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges)
            edgeStrings.add(e.toSortedStringWithEdgeOrder());

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedEdgeString()
    {
        List<Edge> sortedEdges = new ArrayList<Edge>(edges);
        Collections.sort(sortedEdges);
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : sortedEdges)
            edgeStrings.add(e.toSortedString());

        return edgeStrings.toString();
    }
    
    public List<Edge> getSortedEdges()
    {
        List<Edge> sortedEdges = new ArrayList<Edge>(edges);
        Collections.sort(sortedEdges);
        return sortedEdges;
    }
    
    public String getPermutedEdgeString( int ... p )
    {
        List<String> edgeStrings = new ArrayList<String>();

        for( Edge e : this.edges )
            edgeStrings.add( e.getSortedPermutedString(p) );

        return edgeStrings.toString();
    }
    
    public boolean edgesInOrder()
    {
        if (this.edges.size() == 0) return true;

        String edgeString = this.edges.get(0).toString();

        for (int i = 1; i < this.edges.size(); i++)
        {
            String current = this.edges.get(i).toString();

            if (current.compareTo(edgeString) < 0) return false;
            
            edgeString = current; 
        }
        return true;
    }
    
    public boolean colorsInOrder()
    {
        int prev = -1;

        for( int color : colors )
        {
            if( color<prev ) return false;
            prev = color;
        }

        return true;
    }
    
    public String toStringIncludingSize()
    { return edges.toString() + " " + index; }

    public String toString()
    { return edges.toString(); }
    
    public int[] degreeSequence( boolean sorted )
    {
    	int[] degSeq	= new int[vsize()];
    	int n			= vsize();
    	
    	for( int i = 0; i < n; i++ )
    		degSeq[i] = degree(i);
    	
    	if( sorted )
    	{
    		Arrays.sort(degSeq);
    		int[] reversed = new int[vsize()];
    		
    		for (int i = vsize() - 1; i >= 0; i--)
    			reversed[i] = degSeq[vsize() - i - 1];
    		
    		degSeq = reversed;
    	}
    	return degSeq;
    }
    
    public Matrix getSparseLaplacianMatrix()
    {
    	int n			= vsize();
    	Matrix matrix	= new Matrix(n);

    	for( Edge edge: this.edges )
    	{
    		int w		= edge.w;            	
    		int from	= edge.from;
    		int to		= edge.to;

    		matrix.set(from, to, -w);
    		matrix.set(to, from, -w);
    		
    		matrix.set(from, from, matrix.get(from, from)+w);
    		matrix.set(to, to, matrix.get(to, to)+w);
    	}
            
    	return matrix;
    }

	public int getNumberOfSpanningTrees()
	{ return getSparseLaplacianMatrix().determinant( vsize()-1 ); }

    public int degree( int i )
    {
        int degree = 0;

        for( Edge edge : edges ) if ( edge.from==i || edge.to==i )
        	degree++;

        return degree;
    }
    
    public GenericGraph<T> removeLastEdge()
    {
    	List<String> edgeStrings = new ArrayList<String>();

    	for (Edge edge : edges) 
    		edgeStrings.add(edge.toSortedString());

    	Collections.sort(edgeStrings);

    	int l				= edgeStrings.size();
    	int n				= vsize();
    	GenericGraph<T> g	= new GenericGraph<T>();
    	BitSet connected	= new BitSet(n);

    	for( int i=0; i<l-1; i++ )
    	{
    		String[] bits	= edgeStrings.get(i).split(":");
    		int a			= Integer.parseInt(bits[0].trim());
    		int b			= Integer.parseInt(bits[1].trim());

    		if( a<b )  g.addEdge(a, b);
    		else       g.addEdge(b, a);
    		
    		connected.set(a);
    		connected.set(b);
    	}

    	if( connected.cardinality()==n ) return g;

    	else
    	{
    		int[] map	= new int[n];
    		int offset	= 0;
    		
    		for( int i=0; i<n; i++ )
    		{
    			if( connected.get(i) ) map[i] = i - offset;
    			else
    			{
    				map[i] = -1;
    				offset++;
    			}
    		}
    		for( Edge e : g.edges )
    		{
    			e.from  =  map[ e.from ];
    			e.to    =  map[  e.to  ];
    		}
    		
    		g.index = connected.cardinality() - 1;
    		
    		return g;
    	}
    }

    public GenericGraph<T> remove( Edge e )
    {
    	GenericGraph<T> copy = new GenericGraph<T>();

        for( Edge edge : this.edges )
        {
            if( edge==e ) continue;
            copy.addEdge( edge.from, edge.to );
        }

        return copy;
    }

	public GenericGraph<T> getPermutedGraph(Permutation permutation)
	{ return getPermutedGraph(permutation.getValues()); }

	public boolean hasEdge(int i, int j)
	{ return getEdge(i, j) != null; }

    public int getEdgeIndex(int a, int b)
    {
        for( int index = 0; index < edges.size(); index++ )
        {
            Edge e = edges.get(index); 
            if (( e.from==a && e.to==b ) || ( e.to==a && e.from==b )) return index;
        }
        return -1;
    }

    public int esize()
    { return edges.size(); }

    public int vsize()
    { return this.index + 1; }

	public String getEdgeStringWithEdgeOrder()
	{
		String s = "[";
		int counter = edges.size();
		
		for (Edge e : edges)
		{
			s += e.toSortedStringWithEdgeOrder();
			
			if( counter>1 ) s += ", ";
			
			counter--;
		}

		return s+"]";
	}

	public GenericGraph<T> minus( int i )
	{
		GenericGraph<T> h = new GenericGraph<T>();

		for( Edge e : edges )
		{
			if( e.from==i || e.to==i ) continue;
			h.addEdge( (e.from < i) ? e.from : e.from - 1,(e.to < i) ? e.to : e.to - 1 );
		}

		return h;
	}

	public GenericGraph<T> makeAll(List<Integer> list, int j)
	{
		GenericGraph<T> h = new GenericGraph<T>(this);
		
		for( int i : list )
			h.addEdge(i, j);
		
		return h;
	}

	public int getNumberOfBijections()
	{ return (int) SmartMath.factorial(vsize()); }

	public int[] toDegrees()
	{
		int[] vertices = new int[ vsize() ];
		
		for( Edge e : this.edges )
		{
			vertices[ e.from ]  +=  e.w;
			vertices[  e.to  ]  +=  e.w;
		}

		return vertices;
	}

	public static<T> void generateGraphsFromDegree(int[] sequence, NonIsomorphicDecorator<T> decorator)
	{ new KiralyGenerator<T>(decorator).generate(sequence); }
    
}