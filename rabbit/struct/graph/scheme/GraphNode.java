package rabbit.struct.graph.scheme;

public interface GraphNode<T extends GraphNode<T>>
{

	java.util.List<T>  get  (); //get all subnodes

}
