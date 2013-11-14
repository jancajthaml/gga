package pal.api.signature.util;

public class _Int implements Comparable<_Int>
{
    
    public int invariant;
    public int index;
    
    public _Int(int invariant, int index)
    {
        this.invariant = invariant;
        this.index = index;
    }

    public int compareTo(_Int o)
    {
        if (this.invariant < o.invariant)		return -1;
        else if (this.invariant > o.invariant)	return 1;
        else									return 0;
    }
    
    public String toString()
    { return invariant + "/" + index; }

}