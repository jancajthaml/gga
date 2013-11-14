package pal.api.signature.util.invariant;

public class _String implements Comparable<_String>
{

    public String string;
    public int value;
    public int index;

    public _String(String string, int value, int index)
    {
        this.string	= string;
        this.value	= value;
        this.index	= index;
    }
    
    public boolean equals(String string, int value)
    { return this.value == value && this.string.equals(string); }
    
    public boolean equals(_String o)
    { return (this.string == null || o.string == null)?false:(this.value == o.value && this.string.equals(o.string)); }

    public int compareTo(_String o)
    {
        if (this.string == null || o.string == null) return 0;
        int c = this.string.compareTo(o.string);
        if (c == 0)
        {
            if (this.value < o.value)		return -1;
            else if (this.value > o.value)	return 1;
            else							return 0;    
        }
        else return c;
    }

    public String toString()
    { return this.string + "|" + this.value + "|" + this.index; }
}
