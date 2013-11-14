package pal.api.struct;

public class RedBlackTree
{
    
    public BinaryTreeNode root;
    
    public RedBlackTree(int s)
    { root = new BinaryTreeNode(0, s); }
    
    public BinaryTreeNode getLeftmostLeaf(int d)
    { return left(d, root); }
    
	private BinaryTreeNode left(int d, BinaryTreeNode t)
    {
        t.isRed = true;
    
        if( t.isLeaf() ) return t;
        else
        {
            if (d > 0)	return left(d - 1, t.left);
            else		return left(0, t.right);
        }
    }
    
    public String toString()
    { return root.toString(); }
}
