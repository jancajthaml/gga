package task10.api.algo;

import java.util.Stack;

import task10.api.struct.GenericGraph;

public class Tarjan<T>
{

    private boolean[] marked;        // marked[v] = has v been visited?
    private int[] id;                // id[v] = id of strong component containing v
    private int[] low;               // low[v] = low number of v
    private int pre;                 // preorder number counter
    private int count;               // number of strongly-connected components
    private Stack<Integer> stack;

    public Tarjan(GenericGraph<T> G)
    {
        marked = new boolean[G.vsize()];
        stack = new Stack<Integer>();
        id = new int[G.vsize()]; 
        low = new int[G.vsize()];
        for (int v = 0; v < G.vsize(); v++) {
            if (!marked[v]) dfs(G, v);
        }

    }

    private void dfs(GenericGraph<T> G, int v) { 
        marked[v] = true;
        low[v] = pre++;
        int min = low[v];
        stack.push(v);
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
            if (low[w] < min) min = low[w];
        }
        if (min < low[v]) { low[v] = min; return; }
        int w;
        do {
            w = stack.pop();
            id[w] = count;
            low[w] = G.vsize();
        } while (w != v);
        count++;
    }

    public int count()
    { return count; }

    public boolean stronglyConnected(int v, int w)
    { return id[v] == id[w]; }

    public int id(int v)
    { return id[v]; }


}