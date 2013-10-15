package rabbit.struct.graph.scheme;



public interface BinaryTreeNode<T extends BinaryTreeNode<T>> extends TreeNode<T>
{

    T left();
    T right();

}