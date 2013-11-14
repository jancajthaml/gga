package pal.api.signature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pal.api.signature.util.Symetry;
import pal.api.struct.GenericGraph;

public class GraphSignature<T>
{
    
    private GenericGraph<T> graph;
	private final String separator	= "+";
    private int height				= -1;
    private String graphSignature	= "";

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
        String canonicalString = null;
        int n = graph.vsize();
        
        for( int i=0; i<n; i++ )
        {
            String signatureString = this.signatureStringForVertex(i);

            if( canonicalString==null || canonicalString.compareTo(signatureString)>0 )
                canonicalString = signatureString; 
        }

        return ( canonicalString==null ) ? "" : canonicalString;
    }

    public List<Symetry> getSymmetryClasses()
    { return getSymmetryClasses( -1 ); }
        
    public List<Symetry> getSymmetryClasses( int height )
    {
        List<Symetry> symmetryClasses = new ArrayList<Symetry>();
        int n = graph.vsize();

        for( int i=0; i<n; i++ )
        {
            String signatureString	= this.signatureStringForVertex( i,height );
            Symetry foundClass		= null;

            for( Symetry s : symmetryClasses )
            {
                if( s.hasSignature(signatureString) )
                {
                    foundClass = s;
                    break;
                }
            }
            if (foundClass == null)
            {
                foundClass = new Symetry(signatureString);
                symmetryClasses.add(foundClass);
            } 
            foundClass.addIndex(i);
        }
        return symmetryClasses;
    }

    public String toFullString()
    {
        Map<String, Integer> sigmap = new HashMap<String, Integer>();
        int n = graph.vsize();
        
        for( int i=0; i<n; i++ )
        {
            String signatureString = this.signatureStringForVertex(i);
            sigmap.put(signatureString, (sigmap.containsKey(signatureString)) ? (sigmap.get(signatureString) + 1) : 1);
        }

        List<String> keyList = new ArrayList<String>();
        keyList.addAll(sigmap.keySet());
        Collections.sort(keyList);
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < keyList.size() - 1; i++)
        {
            String signature	= keyList.get(i);
            int count			= sigmap.get(signature);

            buffer.append(count).append(signature).append(this.separator);
        }

        String finalSignature = keyList.get(keyList.size() - 1);

        buffer.append( sigmap.get(finalSignature) ).append(finalSignature);
        return buffer.toString();
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