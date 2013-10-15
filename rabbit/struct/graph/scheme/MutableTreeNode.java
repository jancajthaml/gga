package rabbit.struct.graph.scheme;

public interface MutableTreeNode<T extends MutableTreeNode<T>> extends TreeNode<T>
{

	void add	( int index, T child );
    void set	( int index, T child );
    T remove	( int index          );

}