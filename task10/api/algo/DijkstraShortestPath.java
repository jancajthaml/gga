package task10.api.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import task10.api.core.Edge;
import task10.api.struct.GenericGraph;

public class DijkstraShortestPath<T>
{
	private final GenericGraph<T> graph;
	private Set<Integer> settledNodes;
	private Set<Integer> unSettledNodes;
	private Map<Integer, Integer> predecessors;
	private Map<Integer, Integer> distance;

	public DijkstraShortestPath(GenericGraph<T> graph)
	{ this.graph=graph; }

	public void execute(int source)
	{
		//System.out.println("moving root to "+source);
		this.settledNodes	= new HashSet<Integer>();
		this.unSettledNodes	= new HashSet<Integer>();
		this.distance		= new HashMap<Integer, Integer>();
		this.predecessors	= new HashMap<Integer, Integer>();
		this.distance.put(source, 0);
		this.unSettledNodes.add(source);

		while( this.unSettledNodes.size()>0 )
		{
			Integer node = getMinimum(unSettledNodes);
			this.settledNodes.add(node);
			this.unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(int node)
	{
		List<Integer> adjacentNodes = getNeighbors(node);

		for( int target : adjacentNodes )
		if( getShortestDistance(target)>(getShortestDistance(node)+getDistance(node, target)) )
		{
			this.distance.put(target, getShortestDistance(node) + getDistance(node, target));
			this.predecessors.put(target, node);
			this.unSettledNodes.add(target);
		}
	}

	private int getDistance(int node, int target)
	{
		Edge e = graph.getEdge(node, target);
		if(e!=null) return e.w;
		throw new RuntimeException("Should not happen");
	}

	private List<Integer> getNeighbors(int node)
	{
		List<Integer> neighbors = new ArrayList<Integer>();
		
		for( Edge edge : graph.getNeighbours(node) ) if( !isSettled(edge.to) )
			neighbors.add(edge.to);
	
		return neighbors;
	}

	private int getMinimum(Set<Integer> vertexes)
	{
		int minimum = 0;
		for( int vertex : vertexes )
		{
			if( minimum==0 )
				minimum = vertex;
			else if( getShortestDistance(vertex)<getShortestDistance(minimum) )
				minimum = vertex;
		}
		return minimum;
	}

	private boolean isSettled(int vertex)
	{ return this.settledNodes.contains(vertex); }

	private int getShortestDistance(int destination)
	{
		Integer d = distance.get(destination);
		return ( d==null ) ? Integer.MAX_VALUE : d;
	}

	public LinkedList<Integer> getPathTo(int target)
	{
		LinkedList<Integer> path	= new LinkedList<Integer>();
		int step					= target;

		if( predecessors.get(step)==null ) return null;
		
		path.addFirst(step);

		while( predecessors.get(step)!=null )
		{
			step = predecessors.get(step);
			path.addFirst(step);
		}

		return path;
	}

	public LinkedList<Integer> getPathVisiting(int ... target)
	{
		LinkedList<Integer> path	= new LinkedList<Integer>();

		for(int i=1; i<target.length; i++)
		{
			execute(target[i-1]);
			LinkedList<Integer> p = getPathTo(target[i]);
			//System.out.println("from:"+target[i-1]+" to:"+target[i]+"   "+p);
			if(p!=null)
			{
				if(i!=target.length-1) p.removeFirst();
				path.addAll(p);	
			}
		}

		return path;
	}

} 
