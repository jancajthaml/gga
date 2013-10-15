package rabbit.struct.graph.scheme;

public interface MutableTreeNode<T extends MutableTreeNode<T>> extends TreeNode<T>
{

	void  add     ( int index , T child );  // add node to index
    void  set     ( int index , T child );  // set node at index
    T     remove  ( int index           );  // remove node at index

}