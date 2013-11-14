package pal.api.struct;

import java.util.BitSet;

public class BinaryTreeNode
{

    public int				id					= 0;
    public int				r					= 0;
    public BinaryTreeNode	parent				= null;
    public BinaryTreeNode	left				= null;
    public BinaryTreeNode	right				= null;
    public int[]			currentDegSeq		= null;
    public boolean			hasRedLeftChild		= false;
    public boolean			hasRedRightChild	= false;
    public boolean			isLeftChild			= false;
    public BitSet			chiP				= null;
    public boolean			isRed				= false;

    public BinaryTreeNode( int r, int s )
    { this( null, r, s, false, 0 ); }
    
    public BinaryTreeNode(BinaryTreeNode parent, int r, int s, boolean isLeftChild, int id)
    {
        this.id				= id;
        this.r				= r;
        this.parent			= parent;
        this.isLeftChild	= isLeftChild;
    
        if( r<s )
        {
            this.left   =  new BinaryTreeNode(this, r + 1, s, true, id + 1);
            this.right  =  new BinaryTreeNode(this, r + 1, s, false, left.rightmostLeaf().id + 1);
        }
    }
    
    public int getA()
    { return chiP.cardinality(); }
    
    public BinaryTreeNode rightmostLeaf()
    { return isLeaf() ? this : right.rightmostLeaf(); }
    
    public boolean isLeaf()
    { return this.left==null; }
    
    public String toString()
    { return isLeaf()?"[" + id + "]" : ("[" + id + "](" + left + "," + right + ")"); }

}