package task10.api.core;

import java.util.List;

public class Edge implements Comparable<Edge>
{
	
    public int from	= 0;
    public int to	= 0;
    public int w	= 1;
    
    public Edge( int _from, int _to )
    {
        this.from  =  _from;
        this.to    =  _to;
    }
    
    public Edge( int _from, int _to, int _cost )
    {
        this(_from, _to);
        this.w = _cost;
    }
    
    public Edge( Edge e )
    {
        this.from  =  e.from;
        this.to    =  e.to;
    }
    
    public String getSortedPermutedColorOnlyString( int[] p, List<Integer> colors )
    {
        int pa  =  p[ this.from ];
        int pb  =  p[  this.to  ];
        int ca  =  colors.get(pa);
        int cb  =  colors.get(pb);
        
        return ( ca<cb ) ? (ca + ":" + cb) : (cb + ":" + ca);
    }
    
    public String getSortedPermutedColoredString( int[] p, List<Integer> colors )
    {
        int pa = p[ this.from ];
        int pb = p[  this.to  ];
        
        return ( pa<pb ) ? (pa + "(" + colors.get(from) + ")-" + pb + "(" + colors.get(to) + ")") : (pb + "(" + colors.get(to) + ")-" + pa + "(" + colors.get(from) + ")");
    }
    
    public String getSortedPermutedString( int ... p )
    {
        int pa = p[ this.from ];
        int pb = p[  this.to  ];

        return ( pa<pb ) ? (pa + "-" + pb) : (pb + "-" + pa);
    }
    
    public String getPermutedString( int ... p )
    { return p[this.from] + "-" + p[this.to]; }
    
    public String toSortedColorOnlyString( List<Integer> colors )
    {
        int ca = colors.get(from);
        int cb = colors.get(to);
        
        return ( ca<cb ) ? (ca + "-" + cb) : (cb + "-" + ca);
    }
    
    public String toSortedColoredString( List<Integer> colors )
    { return ( from<to ) ? (from + "(" + colors.get(from) + ")-" + to + "(" + colors.get(to) + ")") : (to + "(" + colors.get(to) + ")-" + from + "(" + colors.get(from) + ")"); }
    
    public String toSortedString()
    { return ( from<to ) ? (from + "-" + to) : (to + "-" + from); }
    
    public String toSortedStringWithEdgeOrder()
    { return ( ( from<to ) ? (from + "-" + to) : (to + "-" + from) ) + "(" + w + ")"; }

    public String toString()
    { return this.from + "-" + this.to; }

    public int compareTo(Edge other)
    {
    	int tMin = to;
    	int tMax = from;
    	int oMin = other.to;
    	int oMax = other.from;

    	if( from<to )
    	{
    		tMin = from;
            tMax = to;	
    	}
        
        if( other.from<other.to )
        {
            oMin = other.from;
            oMax = other.to;
        }

        return ( tMin<oMin || ( tMin==oMin && tMax<oMax ) ) ? -1 : ( ( tMin==oMin && tMax==oMax ) ? 0 : 1 );
    }
    
    public int hashCode()
    { return ( from+1 )*31 + ( to+1 ); }
    
    public boolean equals(Object obj)
    {
    	if(!(obj instanceof Edge)) return false;
    	
    	Edge other = (Edge) obj;
    	return ( from==other.from && to==other.to ) ;
    }

	public int getGovernor()
	{ return to; }

	public int getDependent()
	{ return from; }

}