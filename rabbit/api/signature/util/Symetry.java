package pal.api.signature.util;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Symetry implements Comparable<Symetry>, Iterable<Integer>
{

	private final String				signatureString;
    private final SortedSet<Integer>	vertexIndices;

    public Symetry(String signatureString)
    {
       this.signatureString	= signatureString;
       this.vertexIndices	= new TreeSet<Integer>();
    }
    
    public Iterator<Integer> iterator()
    { return this.vertexIndices.iterator(); }

    public int size()
    { return vertexIndices.size(); }

    public String getSignatureString()
    { return this.signatureString; }
    
    public boolean hasSignature(String otherSignatureString)
    { return this.signatureString.equals(otherSignatureString); }
    
    public void addIndex(int vertexIndex)
    { this.vertexIndices.add(vertexIndex); }
    
    public int getMinimal( int v, List<Integer> used )
    {
        int min = -1;
        
        for( int i : this.vertexIndices )
        {
            if( i==v ) return ( min==-1 ) ? v : min;
            else
            {
                if( used.contains(i) ) continue;
                min = i;
            }
        }
        return -1;
    }

    public int compareTo( Symetry o )
    { return this.signatureString.compareTo(o.signatureString); }
    
    public String toString()
    { return this.signatureString + " " + this.vertexIndices; }
    
}
