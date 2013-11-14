package pal.api.generator;

import java.util.LinkedList;
import pal.api.struct.GenericGraph;

public class Filter<T>
{
	private LinkedList<GenericGraph<T>> result	= new LinkedList<GenericGraph<T>>();

    public Filter()
    {}

	public void process(final GenericGraph<T> graph )
    {
		result.add(graph);
	}

    public int getNumberOfGraphs()
    {
    	return result.size();
    }

}