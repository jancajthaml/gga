package rabbit.struct.graph;

import java.util.List;

import rabbit.struct.graph.scheme.TreeNode;

public class GenericImmutableTreeNode<T extends TreeNode<T>> extends GenericImmutableGraphNode<T> implements TreeNode<T>
{
    private T parent;

    public GenericImmutableTreeNode()
    {}

    public GenericImmutableTreeNode( List<T> children )
    {
        super   ( children );
        acquire (          );
    }

    public T parent()
    { return parent; }

    @SuppressWarnings({"unchecked"})
    protected void acquire()
    {
        List<T>  children  =  get();
        int      size      =  children . size();

        for (int i = 0; i < size; i++)
            ((GenericImmutableTreeNode<T>) children . get( i )) . parent = (T) this;
    }

}