package rabbit.util;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;


public class PermutationGenerator<E extends Comparable<E>> implements Iterator<E[]>, Iterable<E[]>
{

    private  final  E[]      helper          ;
    private  final  E[]      it              ;
    private         E[]      high   =  null  ;
    private         boolean  ready  =  false ;
    
    
	public PermutationGenerator()
    {
		throw new IllegalArgumentException("Cannot generate a permutation from a empty set");	
    }
    
    public PermutationGenerator(final E ... LOW)
    {
    	this.it		= toArray(LOW)	;
    	this.high   = reverse(LOW)	;
    	this.helper	= LOW			;
    	this.ready  = false			;
    }
    
    private E[] reverse(E ... arr)
    {
		E[] mirror = toArray(arr);
    	
    	int size = arr.length-1;
    	for(int i=0; i<=size; i++)
    		mirror[size-i]=arr[i];
    		
		return mirror;
	}

	public PermutationGenerator( final E[] LOW, final E[] HIGH )
    {
        this . high    =  HIGH;
        this . helper  =  toArray(LOW);
        this . it      =  toArray(LOW).clone();
        this . ready   =  false                          ;
    }

	@Override public Iterator<E[]> iterator()
	{ return this; }

	@Override public boolean hasNext()
	{
        boolean end    =  ready;

        for( int i = 0 ; i < helper.length ; i++ )
            if( !helper[i].equals(high[i]) ) end = false;
        
        return !end;
	}

	@Override public E[] next()
	{
        if( !ready )
        {
            ready = true;
            return expand();
        }

        int j = helper.length - 2;
        int k = helper.length - 1;

        for( ; helper[ j ].compareTo(helper[ j+1 ])>0 ; j-- ) ;
        for( ; helper[ j ].compareTo(helper[ k   ])>0 ; k-- ) ;

        swap(k, j);
        
        int r = helper.length - 1;
        int s = j + 1;
	 
        while( r > s )
        {
        	swap(r, s);
             
             s++;
             r--;
        }
            
        return expand();
	}
	
	private void swap(int i, int j)
	{
		E temp = helper[i];
		helper[i] = helper[j];
		helper[j] = temp;
	}
	
	private E[] expand( )
	{
		System.arraycopy( helper , 0 , it , 0 , helper.length );
		return it;
	}

	@SuppressWarnings("unchecked")
	public E[ ] toArray(E ... arr)  
	{  
		Class<?> clazz	= arr.getClass();
		E[ ] result		= (E[]) clazz.cast(Array.newInstance(clazz.getComponentType( ), arr.length));  
		int index		= 0;  
		for(E k : arr)  
			result[index++] = k;  
		return result;  
	}

	@Override public void remove()
	{ throw new ConcurrentModificationException(); }
	
	/////////////////////////////////////////////////////////////////////
	
	public static void main(String ... args)
	{
		System.out.println("####Test1\n");
		PermutationGenerator<Integer> simple = new PermutationGenerator<Integer>(1,2,3);
		
		for(Integer[] i : simple)
		{
			for(int j : i)
				System.out.print(j+" ");
			System.out.println();
		}
		
		System.out.println("\n####Test2\n");
		
		Integer[] LOW	= {2,1,3};
		Integer[] HIGH	= {3,1,2};
		PermutationGenerator<Integer> bounded = new PermutationGenerator<Integer>(LOW,HIGH);
		
		for(Integer[] i : bounded)
		{
			for(int j : i)
				System.out.print(j+" ");
			System.out.println();
		}
/*
		System.out.println("\n####Test3\n");
		
		try{ PermutationGenerator<Integer> illegal = new PermutationGenerator<Integer>(); }
		catch(Throwable t){ System.out.println("ERROR : "+t.getMessage()); }
		
		System.out.println("\n####Test4\n");
		
		//This should be a infinite loop <-- bug
		char[] f = "aacca".toCharArray();
		Character[] d = new Character[f.length];
		for(int i=0; i<d.length; i++)
			d[i]=f[i];
		
		PermutationGenerator<Character> word = new PermutationGenerator<Character>(d);
		
		for(Character[] i : word)
		{
			char[] c = new char[i.length];
			for(int j=0; j<c.length;j++)
				c[j]=i[j];
			System.out.print(new String(c));
			System.out.println();
		}
		*/
	}
	
}