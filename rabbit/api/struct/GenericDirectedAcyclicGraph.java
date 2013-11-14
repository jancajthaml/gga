package pal.api.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pal.api.signature.util.invariant.Invariants;
import pal.api.signature.util.invariant._Array;
import pal.api.signature.util.invariant._Int;
import pal.api.signature.util.invariant._List;
import pal.api.signature.util.invariant._String;

public class GenericDirectedAcyclicGraph<T> implements Iterable<List<GenericDirectedAcyclicGraph<T>.Node>>
{
	public Comparator<Node>								nodeComparator;
	private List<List<Node>>							layers;
	private int[]										parentCounts;
    private int[]										childCounts;
	private Invariants									invariants;
	private List<GenericDirectedAcyclicGraph<T>.Node>	nodes;
	private int											vertexCount;
	
    public class Node
    {
		public final int					index;
		public final List<Node>				parents;
		public final List<Node>				children;
		public final int					layer;
		public final Map<Integer, Integer>	edgeColors;
		public int							invariant;

		public Node( int v, int layer )
		{
			this.index			= v;
			this.layer			= layer;
			this.parents		= new ArrayList<Node>();
			this.children		= new ArrayList<Node>();
			this.edgeColors		= new HashMap<Integer, Integer>();
		}
	
        public void addParent(Node node)
        { this.parents.add(node); }
		
		public void addChild(Node node)
		{ this.children.add(node); }
		
		public void addEdgeColor(int partnerIndex, int edgeColor)
		{ this.edgeColors.put(partnerIndex, edgeColor); }
		
	}
	
	public class Arc
	{
		public final int from;
		public final int to;
		
		public Arc( int _from, int _to )
		{
			this.from  =  _from;
			this.to    =  _to;
		}
		
		@SuppressWarnings("unchecked")
		public boolean equals(Object other)
		{
			if(!(other instanceof GenericDirectedAcyclicGraph.Arc)) return false;
			Arc o = (Arc) other;
			return ( this.from==o.from && this.to==o.to ) || ( this.from==o.to && this.to==o.from );
		}
	}

	public class NodeStringLabelComparator implements Comparator<Node>
	{
	    public String[] vertexLabels;
	    
	    public NodeStringLabelComparator( String ... vertexLabels )
	    { this.vertexLabels = vertexLabels; }

        public int compare( Node o1, Node o2 )
        {
            String o1s = this.vertexLabels[o1.index];
            String o2s = this.vertexLabels[o2.index];
            int c = o1s.compareTo(o2s);
            
            if( c==0 )
            {
                if( o1.invariant<o2.invariant )			return -1;
                else if ( o1.invariant>o2.invariant )	return 1;
                else									return 0;
            }

            return c;
        }
	}
	
	public GenericDirectedAcyclicGraph(int rootVertexIndex, int graphVertexCount)
	{
		this.layers				= new ArrayList<List<Node>>();
		this.nodes				= new ArrayList<Node>();
		List<Node> rootLayer	= new ArrayList<Node>();
		Node rootNode			= new Node(rootVertexIndex, 0);
		rootLayer.add(rootNode);
		this.layers.add(rootLayer);
		this.nodes.add(rootNode);
		
		this.vertexCount	= 1;
		this.parentCounts	= new int[graphVertexCount];
		this.childCounts	= new int[graphVertexCount];
	}
	
	public Iterator<List<Node>> iterator()
	{ return layers.iterator(); }
	
	public List<GenericDirectedAcyclicGraph<T>.Node> getRootLayer()
	{ return this.layers.get(0); }
	
	public GenericDirectedAcyclicGraph<T>.Node getRoot()
	{ return getRootLayer().get(0); }
	
	public Invariants copyInvariants()
	{ return (Invariants) this.invariants.clone(); }
	
	public void init(String ... vertexLabels)
	{
	    vertexCount = vertexLabels.length;
	    this.invariants = new Invariants(vertexCount, nodes.size());
	    
        List<_String> pairs = 
            new ArrayList<_String>();
        for (int i = 0; i < vertexCount; i++) {
            String l = vertexLabels[i];
            int p = parentCounts[i];
            pairs.add(new _String(l, p, i));
        }
        Collections.sort(pairs);
        
        if (pairs.size() == 0) return;
        
        nodeComparator = new NodeStringLabelComparator(vertexLabels);
        int order = 1;
        _String first = pairs.get(0);
        invariants.setVertexInvariant(first.index, order);
        for (int i = 1; i < pairs.size(); i++) {
            _String a = pairs.get(i - 1);
            _String b = pairs.get(i);
            if (!a.equals(b)) {
                order++;
            }
            invariants.setVertexInvariant(b.index, order);
        }
    }
	
    public void setColor( int v, int color )
    { this.invariants.setColor( v,color ); }
	
	public int occurences(int v)
	{
	    int count = 0;

	    for (Node node : nodes) if( node.index==v ) count++;

	    return count;
	}
	
	public void setInvariants( Invariants invariants )
	{
	    this.invariants.colors		= invariants.colors.clone();
	    this.invariants.nodes		= invariants.nodes.clone();
	    this.invariants.vertices	= invariants.vertices.clone();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericDirectedAcyclicGraph<T>.Node makeNode(int v, int layer)
	{
        GenericDirectedAcyclicGraph<T>.Node node = new GenericDirectedAcyclicGraph.Node(v, layer);
        this.nodes.add(node);
        return node;
    }

	public GenericDirectedAcyclicGraph<T>.Node makeNodeInLayer(int vertexIndex, int layer)
	{
        GenericDirectedAcyclicGraph<T>.Node node = this.makeNode(vertexIndex, layer);

        if( layers.size()<=layer )
          this.layers.add(new ArrayList<GenericDirectedAcyclicGraph<T>.Node>());

        this.layers.get(layer).add(node);

        return node;
    }
	
	public void addRelation(GenericDirectedAcyclicGraph<T>.Node childNode, GenericDirectedAcyclicGraph<T>.Node parentNode)
	{
	    childNode.parents.add(parentNode);
	    
	    parentCounts [ childNode.index  ]++;
	    childCounts  [ parentNode.index ]++;
	    
	    parentNode.children.add(childNode);
	}
	
	public int[] getParentsInFinalString()
	{
	    int[] counts = new int[vertexCount];
	    getParentsInFinalString(counts, getRoot(), null,  new ArrayList<GenericDirectedAcyclicGraph<T>.Arc>());
	    return counts;
	}
	
	private void getParentsInFinalString(int[] counts, GenericDirectedAcyclicGraph<T>.Node node, GenericDirectedAcyclicGraph<T>.Node parent, List<GenericDirectedAcyclicGraph<T>.Arc> arcs)
	{
	    if( parent!=null ) counts[node.index]++;

	    Collections.sort(node.children, nodeComparator);

	    for( GenericDirectedAcyclicGraph<T>.Node child : node.children )
	    {
            GenericDirectedAcyclicGraph<T>.Arc arc = new Arc(node.index, child.index);
            
            if (arcs.contains(arc)) continue;

            arcs.add(arc);
            getParentsInFinalString(counts, child, node, arcs);
        }	          
	}

    public int[] getOccurrences()
    {
        int[] occurences = new int[vertexCount];
        getOccurences(occurences, getRoot(), null, new ArrayList<GenericDirectedAcyclicGraph<T>.Arc>());
        return occurences;
    }
    
    private void getOccurences( int[] occurences, GenericDirectedAcyclicGraph<T>.Node node, GenericDirectedAcyclicGraph<T>.Node parent, List<GenericDirectedAcyclicGraph<T>.Arc> arcs )
    {
        occurences[node.index]++;
        Collections.sort(node.children, nodeComparator);

        for( GenericDirectedAcyclicGraph<T>.Node child : node.children )
        {
            GenericDirectedAcyclicGraph<T>.Arc arc = new Arc(node.index, child.index);
            
            if( arcs.contains(arc) ) continue;

            arcs.add(arc);
            getOccurences(occurences, child, node, arcs);
        }
    }
	
	public List<_Int> getInvariantPairs( int ... parents )
	{
	    List<_Int> pairs = new ArrayList<_Int>();
	    
	    for( int i = 0; i < this.vertexCount; i++ ) if( invariants.getColor(i)==-1 && parents[i]>=2 )
	    	pairs.add( new _Int(invariants.getVertexInvariant(i), i) );

	    Collections.sort(pairs);

	    return pairs;
	}
	
	public int colorFor( int v )
	{ return this.invariants.getColor(v); }

	public void addLayer( List<Node> layer )
	{ this.layers.add(layer); }
	
	public List<Integer> createOrbit( int ... parents )
	{
	    Map<Integer, List<Integer>> orbits = new HashMap<Integer, List<Integer>>();

	    for( int j=0; j<vertexCount; j++ )
	    {
	        if( parents[j]>=2 )
	        {
	            int invariant = invariants.getVertexInvariant(j);
	            List<Integer> orbit;
	            
	            if (orbits.containsKey(invariant)) orbit = orbits.get(invariant);
	            else
	            {
	                orbit = new ArrayList<Integer>();
	                orbits.put(invariant, orbit);
	            }
	            orbit.add(j);
	        }
	    }

	    if( orbits.isEmpty() ) return new ArrayList<Integer>();

	    List<Integer> maxOrbit = null;
	    List<Integer> invariants = new ArrayList<Integer>(orbits.keySet());
	    
	    Collections.sort(invariants);
	        
	    for( int invariant : invariants )
	    {
	    	List<Integer> orbit = orbits.get(invariant);
	    	
	    	if( maxOrbit == null || orbit.size() > maxOrbit.size() ) 
	    		maxOrbit = orbit;
	    }

	    return maxOrbit;
	}
	
	public void computeVertexInvariants()
	{
	    Map<Integer, int[]> layerInvariants = new HashMap<Integer, int[]>();

	    for( int i = 0; i < this.nodes.size(); i++ )
	    {
	        GenericDirectedAcyclicGraph<T>.Node node = this.nodes.get(i);
	        int j = node.index;
	        int[] layerInvariantsJ;

	        if( layerInvariants.containsKey(j) ) layerInvariantsJ = layerInvariants.get(j);
	        else
	        {
	            layerInvariantsJ = new int[this.layers.size()];
	            layerInvariants.put(j, layerInvariantsJ);
	        }

	        layerInvariantsJ[node.layer] = invariants.getNodeInvariant(i); 
	    }
	    
	    List<_Array> invariantLists = new ArrayList<_Array>();

	    for( int i : layerInvariants.keySet() )
	        invariantLists.add( new _Array(layerInvariants.get(i), i) );

	    Collections.sort(invariantLists);
	    
	    int order	= 1;
	    int first	= invariantLists.get(0).index;

	    invariants.setVertexInvariant(first, 1);

	    for( int i=1; i<invariantLists.size(); i++ )
	    {
	        _Array a = invariantLists.get(i - 1);
	        _Array b = invariantLists.get(i);

	        if( !a.equals(b) ) order++;
	        
	        invariants.setVertexInvariant(b.index, order);
	    }
	}
	
	public void updateVertexInvariants()
	{
	    int[] oldInvariants = new int[vertexCount];
	    boolean invariantSame = true;

	    while( invariantSame )
	    {
	        oldInvariants = invariants.vertices.clone();
	        
	        updateNodeInvariants(Direction.UP);
	        computeVertexInvariants(); 
	        updateNodeInvariants(Direction.DOWN);
	        computeVertexInvariants();
	        invariantSame = checkInvariantChange( oldInvariants, invariants.getVertexInvariants() );
	    }

	    int size	= this.nodes.size();
	    int i		= 0;
	    
	    while( i<size )
	    	this.nodes.get(i).invariant = invariants.getNodeInvariant(i++);
	}
	
	public boolean checkInvariantChange( int[] a, int[] b )
	{
	    for( int i=0; i<vertexCount; i++ ) if( a[i]!=b[i] )
	    	return true;
	    	return false;
	}
	
	public void updateNodeInvariants( GenericDirectedAcyclicGraph.Direction direction )
	{
	    int start, end, increment;
	    
	    switch(direction)
	    {
	    	case UP :
	    	{
	    		start		= this.layers.size() - 1;
		        end			= -1; 
		        increment	= -1;
	    	}
	    	break;

	    	default :
	    	{
	    		start		= 0;
		        end			= this.layers.size();
		        increment	= 1;
	    	}
	    }
	    
	    while( start!=end )
	    {
	    	this.updateLayer(this.layers.get(start), direction);
	    	start+=increment;
	    }

	}
	
	public void updateLayer( List<GenericDirectedAcyclicGraph<T>.Node> layer, GenericDirectedAcyclicGraph.Direction direction )
	{
	    List<_List> nodeInvariantList =  new ArrayList<_List>();

        for( int i = 0; i < layer.size(); i++ )
        {
            GenericDirectedAcyclicGraph<T>.Node layerNode = layer.get(i);
            int x = layerNode.index;
            _List nodeInvariant = new _List(nodes.indexOf(layerNode));
            
            nodeInvariant.add(this.invariants.getColor(x));
            nodeInvariant.add(this.invariants.getVertexInvariant(x));
            
            List<Integer> relativeInvariants = new ArrayList<Integer>();

            List<GenericDirectedAcyclicGraph<T>.Node> relatives = (direction == Direction.UP) ? layerNode.children : layerNode.parents;
            
            for( Node relative : relatives )
            {
                relativeInvariants.add( this.invariants.getNodeInvariant(this.nodes.indexOf(relative)) );
                relativeInvariants.add( vertexCount + 1 + (( direction==Direction.UP ) ? (relative.edgeColors.get(layerNode.index)) : (layerNode.edgeColors.get(relative.index))) );
            }

            Collections.sort(relativeInvariants);
            nodeInvariant.addAll(relativeInvariants);
            nodeInvariantList.add(nodeInvariant);
        }
        
        Collections.sort(nodeInvariantList);
        
        int order = 1;
        int first = nodeInvariantList.get(0).index;
        
        this.invariants.setNodeInvariant(first, order);
        
        for( int i=1; i<nodeInvariantList.size(); i++ )
        {
            _List a = nodeInvariantList.get(i - 1);
            _List b = nodeInvariantList.get(i);
            
            if( !a.equals(b) ) order++;
            
            this.invariants.setNodeInvariant(b.index, order);
        }
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		
		for( List<Node> layer : this )
		{
			buffer.append(layer);
			buffer.append('\n');
		}

		return buffer.toString();
	}

    public enum Direction { UP, DOWN };

}