package rabbit.struct.graph.scheme;

public interface BinaryTreeNode<T extends BinaryTreeNode<T>> extends TreeNode<T>
{

    T left  ();  // get left child
    T right ();  // get right child

}