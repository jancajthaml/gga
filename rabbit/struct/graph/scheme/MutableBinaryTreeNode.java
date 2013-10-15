package rabbit.struct.graph.scheme;


public interface MutableBinaryTreeNode<T extends MutableBinaryTreeNode<T>> extends BinaryTreeNode<T>, MutableTreeNode<T>
{

	void setLeft(T node);
    void setRight(T node);

}