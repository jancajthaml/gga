package pal.api.core;

public class Node
{

    public int				id					= 0;
    public int				r					= 0;
    public Node				parent				= null;
    public Node				left				= null;
    public Node				right				= null;
    public boolean			isLeft				= false;
    public boolean			isRed				= false;

    public Node( int r, int s )
    { this( null, r, s, false, 0 ); }
    
    public Node(Node parent, int r, int s, boolean isLeftChild, int id)
    {
        this.id				= id;
        this.r				= r;
        this.parent			= parent;
        this.isLeft			= isLeftChild;
    
        if( r<s )
        {
            this.left   =  new Node( this , r+1 , s , true  , id+1              );
            this.right  =  new Node( this , r+1 , s , false , left.right().id+1 );
        }
    }
    
    public Node right()
    { return isLeaf() ? this : right.right(); }
    
    public boolean isLeaf()
    { return this.left==null; }
    
    public String toString()
    { return isLeaf()?"[" + id + "]" : ("[" + id + "](" + left + "," + right + ")"); }

}