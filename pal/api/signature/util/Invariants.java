package pal.api.signature.util;

import java.util.Arrays;

public class Invariants implements Cloneable
{

	public int[] colors;
	public int[] nodes;
	public int[] vertices;

    public Invariants(int size_of_vertices, int size_of_nodes)
    {
        this.colors		= new int[size_of_vertices];
        Arrays.fill(colors, -1);
        this.nodes		= new int[size_of_nodes];
        this.vertices	= new int[size_of_vertices];
    }
    
    public int getColor(int vertexIndex)
    { return colors[vertexIndex]; }
    
    public void setColor(int vertexIndex, int color)
    { colors[vertexIndex] = color; }
    
    public int getVertexInvariant(int vertexIndex)
    { return vertices[vertexIndex]; }
    
    public int[] getVertexInvariants()
    { return vertices; }
    
    public void setVertexInvariant(int vertexIndex, int value)
    { vertices[vertexIndex] = value; }
    
    public int getNodeInvariant(int nodeIndex)
    { return nodes[nodeIndex]; }
    
    public void setNodeInvariant(int nodeIndex, int value)
    { nodes[nodeIndex] = value; }
    
    public Object clone()
    {
        Invariants copy	= new Invariants(colors.length, vertices.length);
        copy.colors		= (int[]) colors.clone();
        copy.nodes		= (int[]) nodes.clone();
        copy.vertices	= (int[]) vertices.clone();
        return copy;
    }

}