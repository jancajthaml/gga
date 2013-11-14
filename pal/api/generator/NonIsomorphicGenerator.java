package pal.api.generator;

import java.util.ArrayList;
import pal.api.struct.GenericGraph;

public class NonIsomorphicGenerator<T>
{
	
	private Filter<T> decorator			=  null;

	static Vertex vertices[];
	static int partitions[][];
	static boolean partitionsUsed[][];
	static int mapping[];
	static ArrayList<int[]> mappings = new ArrayList<int[]>();
	static ArrayList<Vertex[]> isomorphisms = new ArrayList<Vertex[]>();
	boolean first=true;

    public NonIsomorphicGenerator( Filter<T> processr )
    { this.decorator = processr; }
    
    public void generate( int ... sequence )
    {
    	first				= true;
    	int n				= sequence.length;
		vertices			= new Vertex[n];
		mapping				= new int[n];
		
		int j	= 0;
		int i	= 0;
		int partition = sequence[0];
		int swaps = 1;

		for(; i < n; i++)
		{
			j = sequence[i];
			
			if(j != partition)
			{
				partition = j;
				swaps++;
			}
			
		}
		
		partitions		= new int[swaps][];
		partitionsUsed	= new boolean[swaps][];
		
		int partitionIndex	= 0;
		int partitionSize = 0;
		
		for( i = 0; i < n; i++)
		{
			vertices[i] = new Vertex(sequence[i], i);
			
			if(i == 0)
				partition = sequence[i];
			
			if(sequence[i] != partition)
			{
				partitions[partitionIndex] = new int[partitionSize];
				partitionsUsed[partitionIndex] = new boolean[partitionSize];
				arrayFill(partitions[partitionIndex], i - partitionSize);
			
				partition = sequence[i];
				partitionSize = 0;
				partitionIndex++;
			}
			
			partitionSize++;
			
		}
		
		//the last partition
		partitions[partitionIndex]		= new int[partitionSize];
		partitionsUsed[partitionIndex]	= new boolean[partitionSize];

		arrayFill(partitions[partitionIndex], i - partitionSize);
		
		//cache all mappings
		constructMappingStep(0, 0, 0);
				
		//generate graphs
		graphConstructStep(0);
    }
	
    private void arrayFill(int array[], int offset)
    {
    	for(int i = 0; i < array.length; i++)
				array[i] = i + offset;
    }
		
    private void graphConstructStep(int index)
    {
    	if(index == vertices.length)
    	{
    		Vertex[] temp = new Vertex[vertices.length];
    		
    		for(int i = 0; i < temp.length; i++)
    		{
    			temp[i] = vertices[i].clone(); //this will probably be a time eater
    		}
				
    		if( first )
    		{
    			first=false;
    			process(temp);
    		}
    		else
    		{
    			boolean iso = false;
    			for(Vertex[] graph : isomorphisms)
    			{
    				iso = isIsomorphic(graph, temp);
    				if( iso ) break;
    			}
    			if( !iso ) process(temp);
    		}
    		return;
    	}
			
    	generateChosenVertices(index, vertices.length, index + 1);
    }
		
    private void process(Vertex ... verticies )
    {
    	isomorphisms.add(verticies);
    	
    	GenericGraph<T> graph = new GenericGraph<T>();
			
    	for(int i=0; i<verticies.length; i++)
    	for(int j=0; j<verticies[i].neighbours.length && i>j; j++)
    		graph.addEdge(verticies[i].index, verticies[i].neighbours[j].index, 1);
		
    	decorator.process(graph);
    }

		private void generateChosenVertices(int index, int top, int count) {
			
			Vertex vertex = vertices[index];
			
			if(vertex.degree - vertex.used > top - count) return;
			
			if(vertex.used == vertex.degree)
			{
				graphConstructStep(index + 1);
				return;
			}
			
			for(int i = count; i < top; i++) {
				if(vertices[i].degree == vertices[i].used)
					continue;
				
				vertex.neighbours[vertex.used++] = vertices[i];
				vertices[i].neighbours[vertices[i].used++] = vertex;
				
				generateChosenVertices(index, top, i+1);
				
				vertex.used--;
				vertices[i].used--;			
				
			}
		}
		
		static void constructMappingStep(int index, int indexInPartition, int absoluteIndex) {
			if(absoluteIndex == mapping.length) {
				//mapping generated
				int temp[] = new int[absoluteIndex];
				System.arraycopy(mapping, 0, temp, 0, mapping.length);
				mappings.add(temp);
				return;
			}
			
			if(indexInPartition == partitions[index].length) {
				//partition finished
				constructMappingStep(index+1, 0, absoluteIndex);
				return;
			}
			
			for(int i = 0; i < partitions[index].length; i++) {
				if(partitionsUsed[index][i])
					continue;
				
				partitionsUsed[index][i] = true;
				mapping[absoluteIndex] = partitions[index][i];
				constructMappingStep(index, indexInPartition+1, absoluteIndex+1);
				partitionsUsed[index][i] = false;
			}
		}
		
		static boolean isIsomorphic(Vertex[] left, Vertex[] right)
		{
			//inspect every mapping until we hit one that fits
			search: for( int[] mapping : mappings )
			{
				//inspect every vertex of both graphs with the current mapping
				for(int i = 0; i < left.length; i++)
				{	
					//the neighbours must lead to the same (mapped) vertices
					for(Vertex neighbour : left[i].neighbours)
					{
						if(!right[mapping[i]].hasNeighbour(mapping[neighbour.index]))
							continue search;
					}
				}
				
				return true;
			}
			
			return false;
		}

		static class Vertex
		{
			int index;
			int degree;
			Vertex neighbours[];
			int used;
			
			Vertex(int degree, int index)
			{
				this.index = index;
				this.degree = degree;
				neighbours = new Vertex[degree];
				used = 0;
			}
			
			public boolean hasNeighbour(int i)
			{
				for(Vertex v : neighbours) if( v.index==i ) return true;
				return false;
			}

			void reset() { used = 0; }
			
			public Vertex clone() {
				Vertex v = new Vertex(degree, index);
				System.arraycopy(neighbours, 0, v.neighbours, 0, neighbours.length);
				return v;
			}
			
		}


}
