package pal.api.decorator;

import pal.api.struct.GenericGraph;

public class StdOutDecorator<T> implements GraphBehaviorDecorator<T>
{
	
	private int		count				= 0;
	private int		size				= -1;
	private boolean	rejectDisconnected	= false;
	
	public StdOutDecorator()
	{}
	
	public StdOutDecorator( int size, boolean rejectDisconnected )
	{
		this.size                =  size;
		this.rejectDisconnected	 =  rejectDisconnected;
	}
	
	public int getNumberOfGraphs()
	{ return count; }

	public void handle( GenericGraph<T> graph )
	{
		if( size != -1 && graph.vsize() != size ) return;
		
		if( !rejectDisconnected || graph.isConnected() )
		{
			System.out.println("G"+(count+1) + "\t" + graph);
			count++;
		}
	}

	public void finish() {}

}