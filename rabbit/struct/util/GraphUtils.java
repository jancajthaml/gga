package rabbit.struct.util;

import static rabbit.logic.Preconditions.*;
import rabbit.logic.predicate.Predicate;
import rabbit.logic.predicate.Predicates;
import rabbit.struct.common.Formatter;
import rabbit.struct.graph.scheme.GraphNode;

import java.util.Collection;
import java.util.HashSet;

public final class GraphUtils
{

    private GraphUtils() {}

    public static boolean hasChildren(GraphNode<?> node)
    { return node != null && !node.get().isEmpty(); }

    public static <T extends GraphNode<T>> T getFirstChild(T node)
    { return hasChildren(node) ? node.get().get(0) : null; }

    public static <T extends GraphNode<T>> T getLastChild(T node)
    { return hasChildren(node) ? node.get().get(node.get().size() - 1) : null; }

    public static <T extends GraphNode<T>> int countAllDistinct(T node)
    { return (node == null)?0:collectAllNodes(node, new HashSet<T>()).size(); }

    public static <T extends GraphNode<T>, C extends Collection<T>> C collectAllNodes(T node, C collection)
    {
    	_not_null_argument(collection, "collection");
    	
        if (node != null && !collection.contains(node))
        {
            collection.add(node);
            for (T child : node.get()) collectAllNodes(child, collection);
        }
        return collection;
    }

    public static <T extends GraphNode<T>> String printTree(T node, Formatter<T> formatter)
    {
        _not_null_argument(formatter, "formatter");
        return printTree(node, formatter, Predicates.<T>_true(), Predicates.<T>_true());
    }

    public static <T extends GraphNode<T>> String printTree(T node, Formatter<T> formatter, Predicate<T> nodeFilter, Predicate<T> subTreeFilter)
    {
    	_not_null_argument(formatter		, "formatter"		);
    	_not_null_argument(nodeFilter		, "nodeFilter"		);
    	_not_null_argument(subTreeFilter	, "subTreeFilter"	);
        return node == null ? "" : printTree(node, formatter, "", new StringBuilder(), nodeFilter, subTreeFilter).toString();
    }

    private static <T extends GraphNode<T>> StringBuilder printTree(T node, Formatter<T> formatter, String indent, StringBuilder sb, Predicate<T> n_filter, Predicate<T> t_filter)
    {
    	if (n_filter.eval(node))
    	{
    		String line = formatter.format(node);
    		
    		if (line != null)
    		{
                sb.append(indent).append(line).append("\n");
                indent += "  ";
            }
        }
        if (t_filter.eval(node)) for (T sub : node.get())
        	printTree(sub, formatter, indent, sb, n_filter, t_filter);

        return sb;
    }

}