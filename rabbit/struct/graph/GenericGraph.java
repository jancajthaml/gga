package rabbit.struct.graph;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import rabbit.struct.common.Matrix;

@SuppressWarnings("unchecked")
public class GenericGraph<T>
{
	public static enum Status
	{
		VISIT_COLOR_WHITE,
		VISIT_COLOR_GREY,
		VISIT_COLOR_BLACK;
	}
	
	private List<Vertex<T>> verticies;
	private List<Edge<T>> edges;
	private Vertex<T> rootVertex;
	
	public GenericGraph()
	{
		verticies	= new ArrayList<Vertex<T>>();
		edges		= new ArrayList<Edge<T>>();
	}

	public boolean isEmpty()
	{ return verticies.size() == 0; }

	public boolean addVertex(Vertex<T> v)
	{
		boolean added = false;
		
		if (verticies.contains(v) == false)
			added = verticies.add(v);
    
		return added;
	}

	public int size()
	{ return verticies.size(); }

	public Vertex<T> getRootVertex()
	{ return rootVertex; }

	public void setRootVertex(Vertex<T> root)
	{
		this.rootVertex = root;
		
		if (verticies.contains(root) == false)
			this.addVertex(root);
	}

	public Vertex<T> getVertex(int n)
	{ return verticies.get(n); }

	public List<Vertex<T>> getVerticies()
	{ return this.verticies; }

	public boolean addEdge(Vertex<T> from, Vertex<T> to, int cost) throws IllegalArgumentException
	{
		if (verticies.contains(from) == false)	throw new IllegalArgumentException("from is not in GenericGraph");
		if (verticies.contains(to) == false)	throw new IllegalArgumentException("to is not in GenericGraph");

		Edge<T> e = new Edge<T>(from, to, cost);
		
		if (from.findEdge(to) != null) return false;
		else
		{
			from.addEdge(e);
			to.addEdge(e);
			edges.add(e);
			return true;
		}
	}

	public boolean insertBiEdge(Vertex<T> from, Vertex<T> to, int cost) throws IllegalArgumentException
	{ return addEdge(from, to, cost) && addEdge(to, from, cost); }

	public List<Edge<T>> getEdges()
	{ return this.edges; }

	public boolean removeVertex(Vertex<T> v)
	{
		if (!verticies.contains(v)) return false;

		verticies.remove(v);
		
		if (v == rootVertex)
			rootVertex = null;

		for (int n = 0; n < v.getOutgoingEdgeCount(); n++)
		{
			Edge<T> e = v.getOutgoingEdge(n);
			v.remove(e);
			Vertex<T> to = e.getTo();
			to.remove(e);
			edges.remove(e);
		}
		
		for (int n = 0; n < v.getIncomingEdgeCount(); n++)
		{
			Edge<T> e = v.getIncomingEdge(n);
			v.remove(e);
			Vertex<T> predecessor = e.getFrom();
			predecessor.remove(e);
		}
		return true;
	}

	public boolean removeEdge(Vertex<T> from, Vertex<T> to)
	{
		Edge<T> e = from.findEdge(to);
		
		if (e == null) return false;
		else
		{
			from.remove(e);
			to.remove(e);
			edges.remove(e);
			return true;
		}
	}

	public void clearMark()
	{ for (Vertex<T> w : verticies) w.clearMark(); }

	public void clearEdges()
	{ for (Edge<T> e : edges) e.clearMark(); }

	public void depthFirstSearch(Vertex<T> v, final Visitor<T> visitor)
	{
		VisitorEX<T, RuntimeException> wrapper = new VisitorEX<T, RuntimeException>()
		{
			public void visit(GenericGraph<T> g, Vertex<T> v) throws RuntimeException
			{ if (visitor != null) visitor.visit(g, v); }
		};
		this.depthFirstSearch(v, wrapper);
	}

	public <E extends Exception> void depthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E
	{
		if (visitor != null) visitor.visit(this, v);
		
		v.visit();

		for (int i = 0; i < v.getOutgoingEdgeCount(); i++)
		{
			Edge<T> e = v.getOutgoingEdge(i);

			if (!e.getTo().visited()) depthFirstSearch(e.getTo(), visitor);
		}
	}

	public void breadthFirstSearch(Vertex<T> v, final Visitor<T> visitor)
	{
		VisitorEX<T, RuntimeException> wrapper = new VisitorEX<T, RuntimeException>()
		{
			public void visit(GenericGraph<T> g, Vertex<T> v) throws RuntimeException
			{
				if (visitor != null) visitor.visit(g, v);
			}
		};
		this.breadthFirstSearch(v, wrapper);
	}

	public <E extends Exception> void breadthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E
	{
		LinkedList<Vertex<T>> q = new LinkedList<Vertex<T>>();
		q.add(v);

		if (visitor != null) visitor.visit(this, v);

		v.visit();

		while (q.isEmpty() == false)
		{
			v = q.removeFirst();

			for (int i = 0; i < v.getOutgoingEdgeCount(); i++)
			{
				Edge<T> e = v.getOutgoingEdge(i);
				Vertex<T> to = e.getTo();

				if (!to.visited())
				{
					q.add(to);
					
					if (visitor != null) visitor.visit(this, to);

					to.visit();
				}
			}
		}
	}

	public void dfsSpanningTree(Vertex<T> v, DFSVisitor<T> visitor)
	{
		v.visit();

		if (visitor != null) visitor.visit(this, v);
		
		for (int i = 0; i < v.getOutgoingEdgeCount(); i++)
		{
			Edge<T> e = v.getOutgoingEdge(i);

			if (!e.getTo().visited())
			{
				if (visitor != null) visitor.visit(this, v, e);

				e.mark();
				dfsSpanningTree(e.getTo(), visitor);
			}
		}
	}

	public Vertex<T> findVertexById(int name)
	{
		Vertex<T> match = null;

		for (Vertex<T> v : verticies)
		{
			if (name == v.getName())
			{
				match = v;
				break;
			}
		}
		return match;
	}

	public Vertex<T> findVertexByData(T data, Comparator<T> compare)
	{
		Vertex<T> match = null;

		for (Vertex<T> v : verticies)
		{
			if (compare.compare(data, v.getData()) == 0)
			{
				match = v;
				break;
			}
		}

		return match;
	}

	public Edge<T>[] findCycles()
	{
		ArrayList<Edge<T>> cycleEdges = new ArrayList<Edge<T>>();

		for (int n = 0; n < verticies.size(); n++)
			getVertex(n).setMarkGenericGraph(Status.VISIT_COLOR_WHITE);
		
		for (int n = 0; n < verticies.size(); n++)
			visit(getVertex(n), cycleEdges);

		Edge<T>[] cycles = new Edge[cycleEdges.size()];
		cycleEdges.toArray(cycles);
		return cycles;
	}

	private void visit(Vertex<T> v, ArrayList<Edge<T>> cycleEdges)
	{
		v.setMarkGenericGraph(Status.VISIT_COLOR_GREY);
		
		int count = v.getOutgoingEdgeCount();

		for (int n = 0; n < count; n++)
		{
			Edge<T> e = v.getOutgoingEdge(n);
			Vertex<T> u = e.getTo();

			switch(u.getMarkGenericGraph())
			{
				case VISIT_COLOR_GREY	: cycleEdges.add(e)		; break;
				case VISIT_COLOR_WHITE	: visit(u, cycleEdges)	; break;
				default : break;
			}
		}

		v.setMarkGenericGraph(Status.VISIT_COLOR_BLACK);
	}

	public String toString()
	{
		StringBuffer tmp = new StringBuffer("GenericGraph [");

		for (Vertex<T> v : verticies)
			tmp.append(v);

		tmp.append(']');
		return tmp.toString();
	}

	public Matrix getSparseLaplacianMatrix()
    {
    	int n			= getVerticies().size();
    	Matrix matrix	= new Matrix(n);

    	for( Edge<T> edge: getEdges() )
    	{
    		Vertex<T> i	= edge.getFrom();
    		Vertex<T> j	= edge.getTo();

    		int w		= edge.getCost();            	
    		int I		= i.getName();
    		int J		= j.getName();

    		matrix.set(I, J, -w);
    		matrix.set(J, I, -w);
    		
    		matrix.set(I, I, matrix.get(I, I)+w);
    		matrix.set(J, J, matrix.get(J, J)+w);

    	}
            
    	return matrix;
    }

	public int getNumberOfSpanningTrees()
	{ return getSparseLaplacianMatrix().determinant( this.getVerticies().size()-1 ); }

}

class Edge<T>
{
	private Vertex<T> from;
	private Vertex<T> to;
	private int cost;
	private boolean mark;

	public Edge(Vertex<T> from, Vertex<T> to)
	{ this(from, to, 0); }

	public Edge(Vertex<T> from, Vertex<T> to, int cost)
	{
		this.from = from;
		this.to = to;
		this.cost = cost;
		mark = false;
	}

	public Vertex<T> getTo()
	{
		return to;
	}

	public Vertex<T> getFrom()
	{ return from; }

	public int getCost()
	{ return cost; }

	public void mark()
	{ mark = true; }

	public void clearMark()
	{ mark = false; }

	public boolean isMarked()
	{ return mark; }

	public String toString()
	{
		StringBuffer tmp = new StringBuffer("Edge[from: ");
		tmp.append(from.getName());
		tmp.append(",to: ");
		tmp.append(to.getName());
		tmp.append(", cost: ");
		tmp.append(cost);
		tmp.append("]");
		
		return tmp.toString();
	}
}

class Vertex<T>
{
	private List<Edge<T>> incomingEdges;
	private List<Edge<T>> outgoingEdges;
	private int name;
	private boolean mark;
	private GenericGraph.Status markGenericGraph;
	private T data;

	public Vertex()
	{ this(0, null); }

	public Vertex(int id)
	{ this(id, null); }

	public Vertex(int id, T data)
	{
		incomingEdges = new ArrayList<Edge<T>>();
		outgoingEdges = new ArrayList<Edge<T>>();
		name = id;
		mark = false;
		this.data = data;
	}

	public int getName()
	{ return name; }

	public T getData()
	{ return this.data; }

	public void setData(T data)
	{ this.data = data; }

	public boolean addEdge(Edge<T> e)
	{
		if (e.getFrom() == this)	outgoingEdges.add(e);
		else if (e.getTo() == this)	incomingEdges.add(e);
		else						return false;
		return true;
	}

	public void addOutgoingEdge(Vertex<T> to, int cost)
	{ outgoingEdges.add( new Edge<T>(this, to, cost) ); }

	public void addIncomingEdge(Vertex<T> from, int cost)
	{ incomingEdges.add( new Edge<T>(this, from, cost) ); }

	public boolean hasEdge(Edge<T> e)
	{
		if (e.getFrom() == this)	return incomingEdges.contains(e);
		else if (e.getTo() == this)	return outgoingEdges.contains(e);
		else						return false;
	}

	public boolean remove(Edge<T> e)
	{
		if (e.getFrom() == this)	incomingEdges.remove(e);
		else if (e.getTo() == this)	outgoingEdges.remove(e);
		else						return false;
		return true;
	}

	public int getIncomingEdgeCount()
	{ return incomingEdges.size(); }

	public Edge<T> getIncomingEdge(int i)
	{ return incomingEdges.get(i); }

	public List<Edge<T>> getIncomingEdges()
	{ return this.incomingEdges; }

	public int getOutgoingEdgeCount()
	{ return outgoingEdges.size(); }

	public Edge<T> getOutgoingEdge(int i)
	{ return outgoingEdges.get(i); }

	public List<Edge<T>> getOutgoingEdges()
	{ return this.outgoingEdges; }

	public Edge<T> findEdge(Vertex<T> dest)
	{
		for (Edge<T> e : outgoingEdges)
			if (e.getTo() == dest) return e;
		return null;
	}

	public Edge<T> findEdge(Edge<T> e)
	{ return (outgoingEdges.contains(e)) ? e : null; }

	public int cost(Vertex<T> dest)
	{
		if (dest == this) return 0;

		Edge<T> e = findEdge(dest);
		int cost = Integer.MAX_VALUE;
		if (e != null)	cost = e.getCost();
		return cost;
	}

	public boolean hasEdge(Vertex<T> dest)
	{ return (findEdge(dest) != null); }

	public boolean visited()
	{ return mark; }

	public void mark()
	{ mark = true; }

	public void setMarkGenericGraph(GenericGraph.Status GenericGraph)
	{ markGenericGraph = GenericGraph; }

	public GenericGraph.Status getMarkGenericGraph()
	{ return markGenericGraph; }

	public void visit()
	{ mark(); }

	public void clearMark()
	{ mark = false; }

	public String toString()
	{
		StringBuffer tmp = new StringBuffer("Vertex(");
		
		tmp.append(name);
		tmp.append(", data=");
		tmp.append(data);
		tmp.append("), in:[");

		for (int i = 0; i < incomingEdges.size(); i++)
		{
			Edge<T> e = incomingEdges.get(i);
			
			if (i > 0) tmp.append(',');

			tmp.append('{');
			tmp.append(e.getFrom().name);
			tmp.append(',');
			tmp.append(e.getCost());
			tmp.append('}');
		}
		
		tmp.append("], out:[");

		for (int i = 0; i < outgoingEdges.size(); i++)
		{
			Edge<T> e = outgoingEdges.get(i);

			if (i > 0) tmp.append(',');

			tmp.append('{');
			tmp.append(e.getTo().name);
			tmp.append(',');
			tmp.append(e.getCost());
			tmp.append('}');
		}

		tmp.append(']');

		return tmp.toString();
	}
}

interface Visitor<T>
{
	public void visit(GenericGraph<T> g, Vertex<T> v);
}

interface VisitorEX<T, E extends Exception>
{
	public void visit(GenericGraph<T> g, Vertex<T> v) throws E;
}

interface DFSVisitor<T>
{
	public void visit(GenericGraph<T> g, Vertex<T> v);
	public void visit(GenericGraph<T> g, Vertex<T> v, Edge<T> e);
}

   