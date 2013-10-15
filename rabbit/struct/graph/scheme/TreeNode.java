package rabbit.struct.graph.scheme;


public interface TreeNode<T extends TreeNode<T>> extends GraphNode<T>
{

    T  parent  ();  //get parent node

}
