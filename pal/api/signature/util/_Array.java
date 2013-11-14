package pal.api.signature.util;

import java.util.Arrays;

public class _Array implements Comparable<_Array>
{
    
    public final int[]	invariants;    
    public final int	index;
    
    public _Array(int[] invariants, int index)
    {
        this.invariants	= invariants;
        this.index		= index;
    }
    
    public boolean equals(int ... other)
    {
        if (this.invariants == null || other == null) return false;

        for( int i=0; i<this.invariants.length; i++ )
        {
            if (this.invariants[i] == other[i])
                continue;

            return false;
        }
        return true;

    }
    
    public boolean equals(Object o)
    {
        if(!(o instanceof _Array)) return false;
        return this.equals(((_Array) o).invariants);
    }

    public int compareTo(_Array o)
    {
        if (this.invariants == null || o.invariants == null) return 0;

        for( int i=0; i<this.invariants.length; i++ )
        {
            if( this.invariants[i]<o.invariants[i] )		return -1;
            else if( this.invariants[i]>o.invariants[i] )	return 1;
        }
        return 0;
    }
    
    public String toString()
    { return Arrays.toString(this.invariants) + ":" + this.index; }

}
