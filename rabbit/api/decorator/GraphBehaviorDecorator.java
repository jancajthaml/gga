package pal.api.decorator;

import pal.api.struct.GenericGraph;

public interface GraphBehaviorDecorator<T>
{
	
	public void handle(GenericGraph<T> graph);
	public void finish();

}
