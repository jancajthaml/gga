package task10.api.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import task10.api.algo.Tarjan;
import task10.api.core.Edge;
import task10.api.core.Matrix;
import task10.api.core.Queue;

public class GenericGraph<T>
{

    public int							index			= -1;
    public List<Integer>				colors			= new ArrayList<Integer>();
    private Map<Integer, List<Integer>>	connections		= null;
    private Map<Integer, List<Integer>>	spans			= null;
    private ArrayList<T>				verticies		= new ArrayList<T>();
    private boolean						vertices_maped	= false;
	private boolean						naive			= false;
	private HashMap<Integer,Edge>		edges			= new HashMap<Integer,Edge>();
	private HashMap<Integer,List<Edge>>	edges_from_map	= new HashMap<Integer,List<Edge>>();
	private HashMap<Integer,List<Edge>>	edges_to_map	= new HashMap<Integer,List<Edge>>();

    public GenericGraph()
    {}

    public GenericGraph( GenericGraph<T> other )
    {
        for( Edge edge : other.edges.values() )
            this.addEdge( edge );

        this.index = other.index;
    }
    
    public GenericGraph(int vertices)
    { index = vertices - 1; }
    
    public int hashCode()
    { return getSortedEdgeString().hashCode(); }
    
    public void makeVertex()
    { this.index++; }
    
    private void saveEdge(Edge e)
    {
    	edges.put(e.hashCode(), e);

    	if(!edges_from_map.containsKey(e.from))	edges_from_map.put(e.from, new LinkedList<Edge>());
    	if(!edges_to_map.containsKey(e.to))		edges_to_map.put(e.to, new LinkedList<Edge>());

    	edges_from_map.get(e.from).add(e);
    	edges_to_map.get(e.to).add(e);
    }

    public GenericGraph<T> getPermutedGraph( int ... permutation )
    {
    	GenericGraph<T> graph = new GenericGraph<T>();

        for( Edge e : this.edges.values() )
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

    private void updateMaxVertexIndex(int a, int b)
    {
        if( a>index )  index = a;
        if( b>index )  index = b;
    }
    
    public Edge getEdge(int i, int j)
    { return this.edges.get(new Edge(i,j).hashCode()); }
    
    public void makeIsolatedVertex()
    { this.index++; }
    
    public void addEdge(int a, int b)
    {
    	Edge e = getEdge(a, b);
        
        if( e == null )
        {
        	if( a < b )	e = new Edge(a, b);
        	else 		e = new Edge(b, a);
        	saveEdge(e);
        }
        else e.w++;
        
        this.updateMaxVertexIndex(a, b);
    }
    
    public void addEdge(int a, int b, int c)
    { addEdge(new Edge(a, b, c)); }

	public void addEdge(Edge e)
	{
		if( this.edges.containsKey(e.hashCode()) ) return;
		
		saveEdge(e);
    	
		if( naive ) return;
		
        this.updateMaxVertexIndex(e.from, e.to);
	}
    
    public List<Integer> getConnected(int vertexIndex)
    { return this.calculateConnectionTable().get(vertexIndex); }
    
    public List<Integer> getSameColorConnected(int vertexIndex)
    {
        int color = getColor(vertexIndex);
        List<Integer> connected = new ArrayList<Integer>();

        for (Edge edge : this.edges.values())
        {
            if ( edge.from==vertexIndex && getColor(edge.to)==color )       connected.add(  edge.to  );
            else if ( edge.to==vertexIndex && getColor(edge.from)==color )  connected.add( edge.from );
            else continue;
        }
        
        return connected;
    }
    
    public boolean isConnected(int i, int j)
    {
        for (Edge e : this.edges.values()) if (( e.from==i && e.to==j ) || ( e.from==j && e.to==i ))
        	return true;
        return false;
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
        
        for( Edge e : this.edges.values() )
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
        
        for (Edge e : this.edges.values())
            edgeStrings.add(e.getSortedPermutedColorOnlyString(p, colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedPermutedColoredEdgeString(int ... p)
    {
        List<String> edgeStrings = new ArrayList<String>();
        
        for (Edge e : this.edges.values())
            edgeStrings.add(e.getSortedPermutedColoredString(p, colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedColorOnlyEdgeString()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges.values())
            edgeStrings.add(e.toSortedColorOnlyString(colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getColorOnlyEdgeString()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges.values())
            edgeStrings.add(e.toSortedColorOnlyString(colors));

        return edgeStrings.toString();
    }
    
    public String getSortedColoredEdgeString()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges.values())
            edgeStrings.add(e.toSortedColoredString(colors));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedPermutedEdgeString(int ... p)
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges.values())
            edgeStrings.add(e.getSortedPermutedString(p));

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedEdgeStringWithEdgeOrder()
    {
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : this.edges.values())
            edgeStrings.add(e.toSortedStringWithEdgeOrder());

        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedEdgeString()
    {
        List<Edge> sortedEdges = new ArrayList<Edge>(edges.values());
        Collections.sort(sortedEdges);
        List<String> edgeStrings = new ArrayList<String>();

        for (Edge e : sortedEdges)
            edgeStrings.add(e.toSortedString());

        return edgeStrings.toString();
    }
    
    public List<Edge> getSortedEdges()
    {
        List<Edge> sortedEdges = new ArrayList<Edge>(edges.values());
        Collections.sort(sortedEdges);
        return sortedEdges;
    }
    
    public String getPermutedEdgeString( int ... p )
    {
        List<String> edgeStrings = new ArrayList<String>();

        for( Edge e : this.edges.values() )
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
    {
    	if(this.vertices_maped)
    	{
    		StringBuilder sb = new StringBuilder();
    		sb.append("[");
    		int i=0;
    		for(Edge e : edges.values())
    		{
    			sb.append(this.getVertex(e.from));
    			sb.append("-");
    			sb.append(this.getVertex(e.to));
    			if(i!=edges.size()-1) sb.append(", ");
    			i++;
    		}
    		sb.append("]");
    		
    		return sb.toString();
    	}
    	else return edges.toString();
    }
    
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
    		
    		for( int i = vsize() - 1; i >= 0; i--)
    			reversed[i] = degSeq[vsize() - i - 1];
    		
    		degSeq = reversed;
    	}
    	return degSeq;
    }

    public int degree( int i )
    {
        int degree = 0;


        for( Edge edge : edges.values() ) if ( edge.from==i || edge.to==i )
        	degree++;

        return degree;
    }
    
    public int inDegree( int i )
    {
        int degree = 0;

        //FIXME
        for( Edge edge : edges.values() ) if( edge.to==i )
        	degree++;

        return degree;
    }
    
    public int outDegree( int i )
    {
        int degree = 0;

        //FIXME
        for( Edge edge : edges.values() ) if( edge.from==i )
        	degree++;

        return degree;
    }
    
    public GenericGraph<T> removeLastEdge()
    {
    	List<String> edgeStrings = new ArrayList<String>();

    	for (Edge edge : edges.values()) 
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
    		for( Edge e : g.edges.values() )
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

        for( Edge edge : this.edges.values() )
        {
            if( edge==e ) continue;
            copy.addEdge( edge.from, edge.to );
        }

        return copy;
    }

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
		
		for (Edge e : edges.values())
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

		for( Edge e : edges.values() )
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

	public int[] toDegrees()
	{
		int[] vertices = new int[ vsize() ];
		
		for( Edge e : this.edges.values() )
		{
			vertices[ e.from ]  +=  e.w;
			vertices[  e.to  ]  +=  e.w;
		}

		return vertices;
	}


    public Matrix<Integer> getSparseLaplacianMatrix()
    {
    	int n					= vsize();
    	Matrix<Integer> matrix	= new Matrix<Integer>(n);

    	for( Edge edge: this.edges.values() )
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

    public int[][] getAdjacencyList()
    {
    	Matrix<Boolean> matrix	= adjacencyMatrix();
    	int[][] result	= new int[matrix.n][];
    	
    	int n	= matrix.n;
    	
		for (int i = 0; i < n; i++) {
			Vector<Integer> v = new Vector<Integer>();
			for (int j = 0; j < n; j++) {
				if (matrix.get(i, j) == 1) {
					v.add(new Integer(j));
				}
			}

			result[i] = new int[v.size()];
			for (int j = 0; j < v.size(); j++) {
				Integer in = (Integer) v.get(j);
				result[i][j] = in.intValue();
			}
		}

    	return result;
    }

    public Matrix<Boolean> adjacencyMatrix()
    {
    	int n			= vsize();
    	Matrix<Boolean> matrix	= new Matrix<Boolean>(n);
    	
    	try
    	{
    		for( Edge edge : this.edges.values() )
    		{	
    			int from	= edge.from;
    			int to		= edge.to;
    			matrix.set(from, to, 1);
    		}
    	}
    	catch(Throwable t){}
            
    	return matrix;
    }

	public int getNumberOfSpanningTrees()
	{ return getSparseLaplacianMatrix().determinant( vsize()-1 ); }

	public boolean pathExists(LinkedList<Integer> path)
	{
		Integer current = path.get(0);
		Integer last	= current;
		
		for(int i=1; i<path.size(); i++)
		{
			current=path.get(i);
			if(!this.hasEdge(last, current)) return false;
			last=current;
		}
		return true;
	}

	public Iterable<Integer> adj(int v)
	{
        if (v < 0 || v >= vsize()) throw new IndexOutOfBoundsException();
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        for(Edge e : this.getNeighbours(v))
        		list.add(e.to);
        return list;
    }

	public Set<Integer> findStartLeafs() {

		HashSet<Integer> e = new HashSet<Integer>();
		
		for(Edge edge : edges.values())
			e.add(edge.from);
			
		for(Edge edge : edges.values())
			e.remove(edge.to);
		
		if(e.isEmpty()) return Collections.emptySet();
		return e;
	}

	public Set<Integer> findEndLeafs()
	{
		HashSet<Integer> e = new HashSet<Integer>();
		
		for(Edge edge : edges.values())
			e.add(edge.to);
			
		for(Edge edge : edges.values())
			e.remove(edge.from);
		
		if(e.isEmpty()) return Collections.emptySet();
		
		return e;
	}

	public Map<Integer, List<Integer>> getSpanTable() {
        if( spans==null )
        	spans = calculateSpanTable();

        return spans;
	}
	
	 private Map<Integer, List<Integer>> calculateSpanTable()
	    {
	        Map<Integer, List<Integer>> connectionTable =  new HashMap<Integer, List<Integer>>();
	        
	        for( Edge e : this.edges.values() )
	        {
	        	if( connectionTable.containsKey(e.from) ) connectionTable.get(e.from).add(e.to);
	            else
	            {
	                List<Integer> connected = new ArrayList<Integer>();
	                connected.add(e.to);
	                connectionTable.put(e.from, connected);
	            }
	        }
	        
	        return connectionTable;
	    }

	public GenericGraph<T> copy()
	{ return new GenericGraph<T>(this); }

	public Set<Integer> getVerticies()
	{
		HashSet<Integer> set = new HashSet<Integer>();
		
    	for( Edge edge: this.edges.values() )
    	{
    		set.add(edge.from);
    		set.add(edge.to);
    	}
    	

		return set;
	}

	public boolean isSuccessor(int i, int j)
	{
		for( Edge e : this.edges.values() ) if( e.from==i && e.to==j )
    		return true;

    	return false;
	}
	
	public boolean isPredecessor(int i, int j)
	{
		for( Edge e : this.edges.values() ) if (e.to==i && e.from==j)
    		return true;

    	return false;
	}

	public List<Edge> getOutEdges(int i)
	{
		List<Edge> ed = new ArrayList<Edge>();
		for( Edge e : this.edges.values() ) if (e.from==i) ed.add(e);
		return ed;
	}

	public List<Edge> getInEdges(int i)
	{
		List<Edge> ed = new ArrayList<Edge>();
		for( Edge e : this.edges.values() ) if (e.to==i) ed.add(e);
		return ed;
	}

	public Set<Integer> successors(int i)
	{
		HashSet<Integer> ed = new HashSet<Integer>();
		for( Edge e : this.edges.values() ) if (e.from==i) ed.add(e.to);
		return ed;	
	}

	public Set<Integer> predecessors(int i)
	{
		HashSet<Integer> ed = new HashSet<Integer>();
		for( Edge e : this.edges.values() ) if (e.to==i) ed.add(e.from);
		return ed;
	}

	public void addVertex( T v )
	{
		vertices_maped = true;
		this.verticies.add(v.hashCode(),v);
	}
	
	public T getVertex(int index)
	{
		if( naive ) return this.verticies.get(index);
		
		if( !this.vertices_maped ) throw new IllegalStateException("Verticies are VIRTUAL");
		if( index<=this.index ) return this.verticies.get(index);
		
		throw new IllegalArgumentException(index+" is not in "+this.verticies);
	}

	public void ignoreConditions()
	{ naive = true; }

	public void listenToConditions()
	{ naive = false; }
	
	public void setNumberOfVerticies(int length)
	{ this.index=length-1; }

	public List<Edge> getNeighbours(int node)
	{
		List<Edge> n = this.edges_from_map.get(node);
		if(n==null) return Collections.emptyList();
		return n;
	}

	
////

	    public LinkedList<Iterable<Integer>> getSelfLoops()
	    {
	    	LinkedList<Iterable<Integer>> cycles = new LinkedList<Iterable<Integer>>(); 
	        hasSelfLoop(cycles);
	        return cycles;
	    }

	    private void hasSelfLoop(LinkedList<Iterable<Integer>> list)
	    {
	        for( int v = 0; v < vsize(); v++ )
	        {
	            for( int w : adj(v) )
	            {
	                if( v==w )
	                {
	                	Stack<Integer> cycle = new Stack<Integer>();
	                    cycle.push(v);
	                    cycle.push(v);
	                    list.add(cycle);
	                }
	            }
	        }
	    }

		public Queue[] getStronglyConectedComponents()
		{
			Tarjan<T> scc	= new Tarjan<T>(this);
			int M			= scc.count();
	        
	        Queue[] components = new Queue[M];
	        for( int i = 0; i < M; i++ )	components[i] = new Queue();
	        for( int v = 0; v < vsize(); v++ )	components[scc.id(v)].enqueue(v);
	        
			return components;
		}
		
}
