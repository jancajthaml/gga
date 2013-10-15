package rabbit.struct.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rabbit.struct.graph.scheme.MutableTreeNode;

import static rabbit.struct.util.TreeUtils.unlink;
import static rabbit.logic.Preconditions.*;

public class GenericMutableTreeNode<T extends MutableTreeNode<T>> implements MutableTreeNode<T>
{

    private final  List<T>  children      =  new ArrayList<T>();
    private final  List<T>  childrenView  =  Collections.unmodifiableList(children);
    private        T        parent;

    public T parent()
    { return parent; }

    public List<T> get()
    { return childrenView; }

    public void add( int index , T child )
    {
    	_in_bounds( index, children.size() + 1 );

        if (child != null)
        {
            if (child.parent() == this) return;
            if (child.parent() != null) unlink( child.parent(), child );
        }

        children . add ( index , child );
        parent         ( child , this  );
    }

    public void set( int index , T child )
    {
    	_in_bounds( index, children.size() );

        T old = children . get ( index );
        
        if (old == child) return;
        
        parent(old, null);

        if (child != null && child.parent() != this) unlink( child.parent(), child );

        children . set ( index , child );
        parent         ( child , this  );
    }

    public T remove( int index )
    {
    	_in_bounds ( index, children.size() );
    	
        T removed = children . remove ( index         );
        parent                        ( removed, null );

        return removed;
    }

    @SuppressWarnings("unchecked") private static <T extends MutableTreeNode<T>> void parent(T node, GenericMutableTreeNode<T> parent)
    { if (node != null) ((GenericMutableTreeNode<T>) node).parent = (T) parent; }

}