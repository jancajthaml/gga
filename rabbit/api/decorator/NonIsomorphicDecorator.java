package pal.api.decorator;

import pal.api.signature.GraphSignature;
import pal.api.struct.GenericGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonIsomorphicDecorator<T> implements GraphBehaviorDecorator<T>
{
    
    private Map<String, GenericGraph<T>>	signatures			= null;
    private boolean							ignoreDisconnected	= false;
    private boolean							reset				= false;
    
    public NonIsomorphicDecorator()
    { this(false); }
        
    public NonIsomorphicDecorator(boolean ignoreDisconnected)
    {
        this.signatures			= new HashMap<String, GenericGraph<T>>();
        this.ignoreDisconnected	= ignoreDisconnected;
    }
    
    public void handle(GenericGraph<T> graph)
    {
        if( reset )
        {
            signatures.clear();
            reset = false;
        }
        
        if( ignoreDisconnected && !graph.isConnected() ) return;
        
        String signature = new GraphSignature<T>(graph).toCanonicalString();
        //System.out.println(signature);
        
        if( ! signatures.containsKey(signature) )
        	signatures.put(signature, graph);
        
    }
    
    public Map<String, GenericGraph<T>> getSignatureMap()
    { return signatures; }
    
    public List<GenericGraph<T>> getNonIsomorphicGraphs()
    { return new ArrayList<GenericGraph<T>>(signatures.values()); }
    
    public int getNumberOfGraphs()
    { return signatures.size(); }
    
    public void finish()
    { reset = true; }

}