package rabbit.struct.util;

import static rabbit.logic.Preconditions.*;
import rabbit.struct.graph.scheme.MutableBinaryTreeNode;
import rabbit.struct.graph.scheme.MutableTreeNode;
import rabbit.struct.graph.scheme.TreeNode;

public final class TreeUtils
{

    private TreeUtils() {}

    public static <T extends TreeNode<T>> T root( T node )
    { return (node == null) ? null : ((node.parent() != null) ? root( node.parent() ) : node); }

    public static <T extends MutableTreeNode<T>> void addChild( T parent, T child )
    {
    	_not_null_argument( parent, "parent" );

        parent . add( parent.get().size() , child );
    }

    public static <T extends MutableTreeNode<T>> void unlink( T parent, T child )
    {
    	_not_null_argument( parent, "parent" );
    	
        int index = parent . get() . indexOf( child );
        
        _in_array( index, parent.get().size() );
        
        parent . remove( index );
    }
    

    public static <N extends MutableBinaryTreeNode<N>> N toLeftAssociativity( N node )
    {
    	_not_null_argument( node, "node" );
    	
        N right = node . right();
        
        if (right == null) return node;

        node  . right ( right . left() );
        right . left  ( node           );

        return right;
    }

}