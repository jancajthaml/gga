package rabbit.struct.graph.scheme;

public interface MutableBinaryTreeNode<T extends MutableBinaryTreeNode<T>> extends BinaryTreeNode<T>, MutableTreeNode<T>
{

	void left   ( T node );  //set left child
    void right  ( T node );  //set right child

}