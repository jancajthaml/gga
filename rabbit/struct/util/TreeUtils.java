package rabbit.struct.util;

import static rabbit.logic.Preconditions.*;
import rabbit.struct.graph.scheme.MutableTreeNode;
import rabbit.struct.graph.scheme.TreeNode;

public final class TreeUtils
{

    private TreeUtils() {}

    public static <T extends TreeNode<T>> T root(T node)
    { return (node == null)?null:((node.parent() != null)?root(node.parent()):node); }

    public static <T extends MutableTreeNode<T>> void addChild(T parent, T child)
    {
    	_not_null_argument(parent, "parent");
        parent.add(parent.get().size(), child);
    }

    public static <T extends MutableTreeNode<T>> void remove(T parent, T child)
    {
    	_not_null_argument(parent, "parent");
    	
        int index = parent.get().indexOf(child);
        
        _in_array(index, parent.get().size());
        
        parent.remove(index);
    }

}
