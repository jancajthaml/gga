package rabbit.struct.graph;

import rabbit.struct.common.ImmutableLinkedList;
import rabbit.struct.common.ImmutableList;
import rabbit.struct.graph.scheme.GraphNode;

import java.util.List;

public class GenericImmutableGraphNode<T extends GraphNode<T>> implements GraphNode<T>
{
    private final List<T> children;

    public GenericImmutableGraphNode()
    { this.children = ImmutableList.<T>of(); }

    public GenericImmutableGraphNode(List<T> children)
    { this.children = children == null ? ImmutableList.<T>of() : children instanceof ImmutableList ? children : children instanceof ImmutableLinkedList ? children : ImmutableList.copy(children); }

    public List<T> get()
    { return children; }

}