package rabbit.struct.util;

import java.lang.reflect.*;

import static rabbit.logic.Preconditions.*;

public final class Utils
{

    public static final Character []  EMPTY_CHARACTER_OBJECT_ARRAY  =  new Character [ 0 ];
    public static final Integer   []  EMPTY_INTEGER_OBJECT_ARRAY    =  new Integer   [ 0 ];
    public static final Long      []  EMPTY_LONG_OBJECT_ARRAY       =  new Long      [ 0 ];
    public static final Short     []  EMPTY_SHORT_OBJECT_ARRAY      =  new Short     [ 0 ];
    public static final Byte      []  EMPTY_BYTE_OBJECT_ARRAY       =  new Byte      [ 0 ];
    public static final Float     []  EMPTY_FLOAT_OBJECT_ARRAY      =  new Float     [ 0 ];
    public static final Double    []  EMPTY_DOUBLE_OBJECT_ARRAY     =  new Double    [ 0 ];
    public static final Boolean   []  EMPTY_BOOLEAN_OBJECT_ARRAY    =  new Boolean   [ 0 ];

    private Utils() {}

    public static Character[] toObjectArray( char ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_CHARACTER_OBJECT_ARRAY;
        
        Character[] result = new Character[array.length];
        
        for( int i = 0; i < array.length; i++ ) result [ i ] = array [ i ];
        
        return result;
    }

    public static Integer[] toObjectArray( int ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_INTEGER_OBJECT_ARRAY;
        
        Integer[] result = new Integer[array.length];
        
        for( int i = 0; i < array.length; i++ ) result [ i ] = array [ i ];
        
        return result;
    }

    public static Long[] toObjectArray( long ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_LONG_OBJECT_ARRAY;
        
        Long[] result = new Long[array.length];
        
        for( int i = 0; i < array.length; i++ ) result[i] = array[i];
        
        return result;
    }

    public static Short[] toObjectArray( short ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_SHORT_OBJECT_ARRAY;
        
        Short[] result = new Short[array.length];
        
        for( int i = 0; i < array.length; i++ ) result [ i ] = array [ i ];
        
        return result;
    }

    public static Byte[] toObjectArray( byte ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_BYTE_OBJECT_ARRAY;
        
        Byte[] result = new Byte[array.length];
        
        for( int i = 0; i < array.length; i++ ) result [ i ] = array [ i ];
        
        return result;
    }

    public static Float[] toObjectArray( float ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_FLOAT_OBJECT_ARRAY;
        
        Float[] result = new Float[array.length];
        
        for( int i = 0; i < array.length; i++ ) result [ i ] = array [ i ];
        
        return result;
    }

    public static Double[] toObjectArray( double ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_DOUBLE_OBJECT_ARRAY;
        
        Double[] result = new Double [ array.length ];
        
        for( int i = 0; i < array.length; i++ ) result [ i ] = array [ i ];
        
        return result;
    }

    public static Boolean[] toObjectArray( boolean ... array )
    {
        if( array == null     )  return null;
        if( array.length == 0 )  return EMPTY_BOOLEAN_OBJECT_ARRAY;
        
        Boolean[] result = new Boolean[array.length];
        
        for( int i = 0; i < array.length; i++ ) result [ i ] = array [ i ];
        
        return result;
    }

    @SuppressWarnings({"unchecked"}) public static <T> T[] arrayOf( T firstElement, T ... moreElements )
    {
        _not_null_argument( moreElements, "moreElements" );
        
        Class<?> elementType  =  moreElements.getClass().getComponentType();
        T[] array             =  (T[]) Array.newInstance(elementType, moreElements.length + 1);
        array [ 0 ]           =  firstElement;
        
        System.arraycopy( moreElements, 0, array, 1, moreElements.length );
        
        return array;
    }

    @SuppressWarnings({"unchecked"}) public static <T> T[] arrayOf( T firstElement, T secondElement, T ... moreElements )
    {
    	_not_null_argument( moreElements, "moreElements" );
    	
        Class<?> elementType  =  moreElements.getClass().getComponentType();
        T[] array             =  (T[]) Array.newInstance(elementType, moreElements.length + 2);
        array [ 0 ]           =  firstElement;
        array [ 1 ]           =  secondElement;
        
        System.arraycopy(moreElements, 0, array, 2, moreElements.length);
        
        return array;
    }

    @SuppressWarnings({"unchecked"}) public static <T> T[] arrayOf( T[] firstElements, T lastElement )
    {
    	_not_null_argument( firstElements, "firstElements" );
    	
        Class<?>  elementType  =  firstElements.getClass().getComponentType();
        T[]       array        =  (T[]) Array.newInstance(elementType, firstElements.length + 1);
        
        System.arraycopy( firstElements, 0, array, 0, firstElements.length );
        
        array [ firstElements . length ] = lastElement;
        return array;
    }

    public static String toString(Object obj)
    { return obj == null ? "" : obj.toString(); }

    public static <T> boolean equal(T a, T b)
    { return a != null ? a.equals(b) : b == null; }

    public static Class<?> getClass(Type type)
    {
        if (type instanceof Class)
            return (Class<?>) type;
        else if (type instanceof ParameterizedType)
            return getClass(((ParameterizedType) type).getRawType());
        else if (type instanceof GenericArrayType)
        {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            }
        }
        return null;
    }


    public static String humanize(long value)
    {
        if (value < 0)							return '-' + humanize(-value);
        else if (value > 1000000000000000000L)	return Double.toString((value + 500000000000000L) / 1000000000000000L * 1000000000000000L / 1000000000000000000.0) + 'E';
        else if (value > 100000000000000000L)	return Double.toString((value + 50000000000000L) / 100000000000000L * 100000000000000L / 1000000000000000.0) + 'P';
        else if (value > 10000000000000000L)	return Double.toString((value + 5000000000000L) / 10000000000000L * 10000000000000L / 1000000000000000.0) + 'P';
        else if (value > 1000000000000000L)		return Double.toString((value + 500000000000L) / 1000000000000L * 1000000000000L / 1000000000000000.0) + 'P';
        else if (value > 100000000000000L)		return Double.toString((value + 50000000000L) / 100000000000L * 100000000000L / 1000000000000.0) + 'T';
        else if (value > 10000000000000L)		return Double.toString((value + 5000000000L) / 10000000000L * 10000000000L / 1000000000000.0) + 'T';
        else if (value > 1000000000000L)		return Double.toString((value + 500000000) / 1000000000 * 1000000000 / 1000000000000.0) + 'T';
        else if (value > 100000000000L)			return Double.toString((value + 50000000) / 100000000 * 100000000 / 1000000000.0) + 'G';
        else if (value > 10000000000L)			return Double.toString((value + 5000000) / 10000000 * 10000000 / 1000000000.0) + 'G';
        else if (value > 1000000000)			return Double.toString((value + 500000) / 1000000 * 1000000 / 1000000000.0) + 'G';
        else if (value > 100000000)				return Double.toString((value + 50000) / 100000 * 100000 / 1000000.0) + 'M';
        else if (value > 10000000)				return Double.toString((value + 5000) / 10000 * 10000 / 1000000.0) + 'M';
        else if (value > 1000000)				return Double.toString((value + 500) / 1000 * 1000 / 1000000.0) + 'M';
        else if (value > 100000)				return Double.toString((value + 50) / 100 * 100 / 1000.0) + 'K';
        else if (value > 10000)					return Double.toString((value + 5) / 10 * 10 / 1000.0) + 'K';
        else if (value > 1000)					return Double.toString(value / 1000.0) + 'K';
        else									return Long.toString(value) + ' ';
    }

}
