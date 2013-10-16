package rabbit.struct.common;

public class IntegerStack
{

    public static class UnderflowException extends RuntimeException
    {
		private static final long serialVersionUID = 7393735626683631233L;
		public UnderflowException(String message) { super(message); }
	}

    private  static  final  int    INITIAL_CAPACITY  =  16;
    private                 int[]  array             =  null;
    private                 int    top               =  -1;

    public IntegerStack()
    { array = new int[INITIAL_CAPACITY]; }

    public boolean isEmpty()
    { return top == -1; }

    public int size()
    { return top + 1; }

    public void getElements( int[] destArray, int destStartIndex )
    { System.arraycopy(array, 0, destArray, destStartIndex, size()); }

    public int[] toArray()
    {
        int[] array = new int [ size() ];
        getElements( array, 0 );
        return array;
    }

    public void clear()
    { top = -1; }

    public int peek()
    {
        if (isEmpty()) throw new UnderflowException("IntArrayStack peek");
        return array [ top ];
    }

    public int pop()
    {
        if (isEmpty()) throw new UnderflowException("IntArrayStack pop");
        return array [ top-- ];
    }

    public void push(int x)
    {
        if (top == array.length - 1) expandCapacity();
        array [ ++top ] = x;
    }

    private void expandCapacity()
    {
        int[] arr = new int [ array.length << 1 ];

        System.arraycopy( array, 0, arr, 0, array.length );

        array = arr;
    }

}
