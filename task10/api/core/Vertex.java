package task10.api.core;

public class Vertex
{
	public boolean  terminal  =  false ;
	public int      index     =  0     ;
	
	public Vertex( int index )
	{ this.index = index; }

	public int hashCode()
	{ return this.index; }

	public String toString()
	{ return terminal?("("+index+")"):(""+index); }
	
	public boolean equals(Object other)
	{ return index==other.hashCode(); }

}