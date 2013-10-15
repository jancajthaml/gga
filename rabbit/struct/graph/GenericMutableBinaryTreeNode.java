package rabbit.struct.graph;

import rabbit.struct.graph.scheme.BinaryTreeNode;
import rabbit.struct.graph.scheme.MutableBinaryTreeNode;


public class GenericMutableBinaryTreeNode<T extends MutableBinaryTreeNode<T>> extends GenericMutableTreeNode<T> implements BinaryTreeNode<T>
{

    public GenericMutableBinaryTreeNode()
    {
        super . add ( 0 , null );
        super . add ( 1 , null );
    }

    public T left  ()                { return get() . get ( 0 ); }
    public T right ()                { return get() . get ( 1 ); }
    
    public void setLeft  ( T node )  { set( 0 , node ); }
    public void setRight ( T node )  { set( 1 , node ); }

    
    //------------------------------------------------------------------------------------------------//
    
    @Override public void add ( int index , T child )  { throw new UnsupportedOperationException(); }
    @Override public T remove ( int index           )  { throw new UnsupportedOperationException(); }

}