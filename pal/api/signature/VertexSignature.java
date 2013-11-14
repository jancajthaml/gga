package pal.api.signature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pal.api.signature.util.Invariants;
import pal.api.signature.util._Int;
import pal.api.struct.GenericDirectedAcyclicGraph;
import pal.api.struct.GenericGraph;


public class VertexSignature<T>
{

    private static final char START_BRANCH_SYMBOL	= '(';    
    private static final char END_BRANCH_SYMBOL		= ')';
    private static final char START_NODE_SYMBOL		= '[';
    private static final char END_NODE_SYMBOL		= ']';

    private GenericGraph<T>					graph;
    private GenericDirectedAcyclicGraph<T>	dag;
    private Map<Integer, Integer>			vertexMapping;
    private Map<Integer, Integer>			vertexMappingInverse;
    private int								count			= 0;
    
    public VertexSignature( int v, GenericGraph<T> g )
    {
    	this();
        this.graph = g;
        createMaximumHeight( v,g.vsize() );
    }
    
    public VertexSignature( int v, GenericGraph<T> g, int h )
    {
    	this();
        this.graph = g;
        create( v,g.vsize(),h );
    }

    protected int[] getConnected( int v )
    {
        List<Integer> connectedList	= graph.getConnected(v); 
        int[] connected				= new int[connectedList.size()];

        for( int i=0; i<connectedList.size(); i++ )
            connected[i] = connectedList.get(i);

        return connected;
    }

    protected String getEdgeLabel(int vertexIndex, int otherVertexIndex)
    {
        switch( graph.getEdge(vertexIndex, otherVertexIndex).w )
        {
            case 1  : return "-";
            case 2  : return "=";
            default : return "";
        }
    }

    protected int convertEdgeLabelToColor( String label )
    {
        if( label.equals("-") )	return 1;
        if( label.equals("=") )	return 2;
        else					return 0;
    }

    public VertexSignature(  )
    {}
    
    public int getOriginalVertexIndex( int v )
    {
    	try{ return vertexMappingInverse.get(v); }
    	catch(Throwable t){ return -1; }
    }

    public void createMaximumHeight( int root, int size )
    { create( root,size,-1 ); }

    public void create( int root, int size, int height )
    {
    	vertexMappingInverse	= new HashMap<Integer, Integer>();
        vertexMapping			= new HashMap<Integer, Integer>();

        vertexMapping.put(root, 0);
        vertexMappingInverse.put(0, root);

        dag = new GenericDirectedAcyclicGraph<T>(0, size);
        count = 1;
        build(1, dag.getRootLayer(), new ArrayList<GenericDirectedAcyclicGraph<T>.Arc>(), height);
        
        String[] labels = new String[count];

        Arrays.fill(labels, ".");

        dag.init(labels);
    }

    private void build(int layer, List<GenericDirectedAcyclicGraph<T>.Node> previousLayer, List<GenericDirectedAcyclicGraph<T>.Arc> usedArcs, int height)
    {
        if (height == 0) return;

        List<GenericDirectedAcyclicGraph<T>.Node> nextLayer	= new ArrayList<GenericDirectedAcyclicGraph<T>.Node>();
        List<GenericDirectedAcyclicGraph<T>.Arc> layerArcs	= new ArrayList<GenericDirectedAcyclicGraph<T>.Arc>();
        
        for( GenericDirectedAcyclicGraph<T>.Node node : previousLayer )
        {
            int mappedIndex = getOriginalVertexIndex(node.index);
            int[] connected = getConnected(mappedIndex);
            
            Arrays.sort(connected);
            
            for( int v : connected )
            	add( layer,node,v,layerArcs,usedArcs,nextLayer );
        }

        usedArcs.addAll(layerArcs);
        
        if( nextLayer.isEmpty() ) return;

        dag.addLayer(nextLayer);
        build(layer + 1, nextLayer, usedArcs, height - 1);
    }

    private void add(int layer, GenericDirectedAcyclicGraph<T>.Node parent, int vertexIndex, List<GenericDirectedAcyclicGraph<T>.Arc> layerArcs, List<GenericDirectedAcyclicGraph<T>.Arc> usedArcs,  List<GenericDirectedAcyclicGraph<T>.Node> nextLayer)
    {
        int mappedVertexIndex;
        if (vertexMapping.containsKey(vertexIndex))
        {
            mappedVertexIndex = vertexMapping.get(vertexIndex);
        }
        else
        {
        	vertexMappingInverse.put(count, vertexIndex);
            vertexMapping.put(vertexIndex, count);
            mappedVertexIndex = count;
            count++;
        }
        
        GenericDirectedAcyclicGraph<T>.Arc arc = dag.new Arc(parent.index, mappedVertexIndex);

        if( usedArcs.contains(arc) ) return;
        
        GenericDirectedAcyclicGraph<T>.Node existingNode = null;
        
        for( GenericDirectedAcyclicGraph<T>.Node otherNode : nextLayer )
        {
            if (otherNode.index == mappedVertexIndex)
            {
                existingNode = otherNode;
                break;
            }
        }
        
        if( existingNode==null )
        {
            existingNode = dag.makeNode(mappedVertexIndex, layer);
            nextLayer.add(existingNode);
        }
        
        int originalParentIndex	= getOriginalVertexIndex(parent.index);
        String edgeLabel		= getEdgeLabel(originalParentIndex, vertexIndex);
        int edgeColor			= convertEdgeLabelToColor(edgeLabel);

        existingNode.addEdgeColor(parent.index, edgeColor);
        parent.addEdgeColor(mappedVertexIndex, edgeColor);        
        dag.addRelation(existingNode, parent);
        layerArcs.add(arc);
    }

    public String toCanonicalString()
    {
        StringBuffer stringBuffer = new StringBuffer();
        this.canonize(0, stringBuffer);
        return stringBuffer.toString();
    }
    
    public void canonize( int color, StringBuffer canonicalVertexSignature )
    {
        if( count==0 ) return;
        
        this.dag.updateVertexInvariants();
        
        int[] parents		= dag.getParentsInFinalString();
        List<Integer> orbit	= this.dag.createOrbit(parents);

        if( orbit.size()<2 )
        {
            List<_Int> pairs = dag.getInvariantPairs(parents);

            for (_Int pair : pairs)
            {
                this.dag.setColor(pair.index, color);
                color++;
            }
            
            String signature	= this.toString();
            int cmp				= signature.compareTo(canonicalVertexSignature.toString()); 

            if( cmp>0 ) canonicalVertexSignature.replace(0, canonicalVertexSignature.length(), signature);

            return;
        }
        else
        {
            for( int o : orbit )
            {
                this.dag.setColor(o, color);
                Invariants invariantsCopy = this.dag.copyInvariants();
                this.canonize(color + 1, canonicalVertexSignature);
                this.dag.setInvariants(invariantsCopy);
                this.dag.setColor(o, -1);
            }
        }
    }

    private void print(StringBuffer buffer, GenericDirectedAcyclicGraph<T>.Node node, GenericDirectedAcyclicGraph<T>.Node parent, List<GenericDirectedAcyclicGraph<T>.Arc> arcs)
    {
        int vertexIndex = getOriginalVertexIndex(node.index);
        
        if( parent!=null )
            buffer.append(getEdgeLabel(vertexIndex, getOriginalVertexIndex(parent.index)));
        
        buffer.append(VertexSignature.START_NODE_SYMBOL);
        buffer.append(".");
        int color = dag.colorFor(node.index);
        if (color != -1) {
            buffer.append(',').append(color);
        }
        buffer.append(VertexSignature.END_NODE_SYMBOL);
        
        Collections.sort(node.children, dag.nodeComparator);
        
        boolean addedBranchSymbol = false;
        
        for( GenericDirectedAcyclicGraph<T>.Node child : node.children )
        {
            GenericDirectedAcyclicGraph<T>.Arc arc = dag.new Arc(node.index, child.index);
        
            if( arcs.contains(arc) )
                continue;
            
            if( !addedBranchSymbol )
            {
            	buffer.append(VertexSignature.START_BRANCH_SYMBOL);
            	addedBranchSymbol = true;
            }
            
            arcs.add(arc);
            print(buffer, child, node, arcs);
        }
        
        if( addedBranchSymbol )
            buffer.append(VertexSignature.END_BRANCH_SYMBOL);
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        print(buffer, this.dag.getRoot(), null, new ArrayList<GenericDirectedAcyclicGraph<T>.Arc>());
        return buffer.toString();
    }
    
    
}