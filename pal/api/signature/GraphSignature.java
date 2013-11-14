package pal.api.signature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pal.api.struct.GenericGraph;

public class GraphSignature<T>
{
    
    private GenericGraph<T>	graph;
    private int				height			= -1;
    private String			graphSignature	= "";

    public GraphSignature( GenericGraph<T> graph )
    { this.graph = graph; }

    public String signatureStringForVertex( int v )
    { return signatureForVertex( v ).toCanonicalString(); }

    public String signatureStringForVertex( int v, int h )
    { return signatureForVertex( v,h ).toCanonicalString(); }

    public VertexSignature<T> signatureForVertex( int v )
    { return new VertexSignature<T>( v,graph ); }
    
    public VertexSignature<T> signatureForVertex( int v, int h )
    { return new VertexSignature<T>( v,graph,h ); }

	public GenericGraph<T> getGraph()
	{ return this.graph; }
	
    public int getHeight()
    { return this.height; }
    
    public String toCanonicalString()
    {
    	try
    	{
    		int n			= graph.vsize();
        	String canonic	= this.signatureStringForVertex(0);
     
        	for( int i=1; i<n; i++ )
        	{
            	String signature = this.signatureStringForVertex(i);

            	if( canonic.compareTo(signature)>0 )
            		canonic = signature; 
        	}

        	return canonic;
    	}
    	catch(Throwable t){ return ""; }
    }

    public String getGraphSignature()
    {
        this.graphSignature = getMaximalSignature();
        return this.graphSignature;
    }
    
    public List<String> getSortedSignatures()
    {
        List<String> vertexSignatures = this.getVertexSignatureStrings();
        Collections.sort(vertexSignatures);
        return vertexSignatures;
    }
    
    public String getMinimalSignature()
    {
        List<String> sortedSignatures = getSortedSignatures();
        return sortedSignatures.get(sortedSignatures.size() - 1);
    }
    
    public String getMaximalSignature()
    { return getSortedSignatures().get(0); }

    public List<String> getVertexSignatureStrings()
    {
        List<String> signatures = new ArrayList<String>();
        int n					= graph.vsize();
        
        for( int i=0; i<n ; i++ )
        	signatures.add( signatureStringForVertex(i) );

        return signatures;
    }

    public List<VertexSignature<T>> getVertexSignatures()
    {
        List<VertexSignature<T>> signatures = new ArrayList<VertexSignature<T>>();
        int n					= graph.vsize();
        
        for( int i=0; i<n ; i++ )
            signatures.add( signatureForVertex(i) );

        return signatures;
    }
 
}