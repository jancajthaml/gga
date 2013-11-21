package task10;

import task10.api.core.Edge;

public class AlphabeticalEdge extends Edge
{

	public char character = 0;
	
	public AlphabeticalEdge(int from, int to, char character)
	{
		super(from,to,1);
		this.character = character;
	}

	public String toString()
	{ return String.valueOf(this.character); }

}
