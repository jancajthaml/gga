package pal.api.signature.util.invariant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class _List implements Comparable<_List>
{
    
    public List<Integer>	invariants;    
    public int				index;
    
    public _List(int index)
    {
        this.invariants	= new ArrayList<Integer>();
        this.index		= index;
    }
    
    public boolean equals(List<Integer> other)
    {
    	if ( !(this.invariants.size() == other.size()) ) return false;
        for (int i = 0; i < this.invariants.size(); i++)
        {
            if (this.invariants.get(i) == other.get(i)) continue;
            return false;
        }
        return true;
    }
    
    public void add(int i)
    { this.invariants.add(i); }
    
    public void addAll(Collection<Integer> other)
    { this.invariants.addAll(other); }
    
    public boolean equals(Object o)
    { return (o instanceof _List)?this.equals(((_List) o).invariants):false; }

    public int compareTo(_List o)
    {
        int lA = this.invariants.size();
        int lB = o.invariants.size();
    
        if (lA < lB)		return -1;
        else if (lA > lB)	return 1;
        else
        {
            for (int i = 0; i < this.invariants.size(); i++)
            {
                if (this.invariants.get(i) < o.invariants.get(i))		return -1;
                else if (this.invariants.get(i) > o.invariants.get(i))	return 1;
            }
            return 0;
        }
    }
    
    public String toString()
    { return index + " " + invariants; }

}