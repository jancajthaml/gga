package rabbit.struct.graph;

import rabbit.struct.common.ImmutableList;
import rabbit.struct.graph.scheme.BinaryTreeNode;

public class GenericImmutableBinaryTreeNode<T extends BinaryTreeNode<T>> extends GenericImmutableTreeNode<T> implements BinaryTreeNode<T>
{
    private  final  T  left  ;
    private  final  T  right ;

    public GenericImmutableBinaryTreeNode( T left , T right )
    {
        super( left == null ? right == null ? ImmutableList.<T>of() : ImmutableList.of(right) : right == null ? ImmutableList.of(left) : ImmutableList.of(left, right) );

        this . left   =  left  ;
        this . right  =  right ;
    }

    public T left  ()  { return left  ; }
    public T right ()  { return right ; }

}