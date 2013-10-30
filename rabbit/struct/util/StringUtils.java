package rabbit.struct.util;

import java.util.Arrays;
import java.util.Iterator;

public final class StringUtils
{

	//Library class
    private StringUtils()
    {}

    public static String repeat(char c, int n)
    {
        char[] array = new char[n];

        //FIXME faster Arrays Fill
        Arrays.fill( array, c );

        return String.valueOf(array);
    }

    public static String join( Iterable<?> iterable , String separator )
    { return iterable == null ? null : join(iterable.iterator(), separator); }
    
    public static String join( Iterator<?> iterator , String separator )
    {
        if( iterator == null      )  return null;
        if( !iterator . hasNext() )  return "";
        
        Object first = iterator.next();
        
        if( !iterator.hasNext() )  return Utils.toString( first );

        StringBuilder buf = new StringBuilder( 256 );
        
        if (first != null) buf . append ( first );
        
        while( iterator.hasNext() )
        {
            if( separator                 != null )  buf . append ( separator );
            if( (first = iterator.next()) != null )  buf . append ( first     );
        }
        
        return buf . toString();
    }

    public static String join( Object[] array, String separator )
    { return array == null ? null : join( array, separator, 0, array.length ); }

    public static String join( Object[] array, String separator, int startIndex, int endIndex )
    {
        if( array     == null ) return null;    
        if( separator == null ) separator = "";

        int bufSize = endIndex - startIndex;
        
        if( bufSize <= 0 )      return "";
        
        bufSize           *=  ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());
        StringBuilder buf  =  new StringBuilder( bufSize );

        for( int i = startIndex; i < endIndex; i++ )
        {
            if( i > startIndex   )  buf . append ( separator );
            if( array[i] != null )  buf . append ( array[i]  );
        }
        
        return buf . toString();
    }

    public static boolean isEmpty( String str )
    { return str == null || str.length() == 0; }

    public static boolean isNotEmpty( String str )
    { return !isEmpty( str ); }

    public static int length( String str )
    { return str == null ? 0 : str.length(); }

    public static boolean equalsIgnoreCase( String str1, String str2 )
    { return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2); }

    public static boolean startsWith( String string, String prefix )
    { return string != null && (prefix == null || string.startsWith( prefix )); }

    public static String substring( String str , int start )
    {
        if( str == null )  return null;

        if( start < 0   )  start  =  str.length() + start;
        if( start < 0   )  start  =  0;

        return( start > str.length() ) ? "" : str . substring( start );
    }

    public static String substring( String str , int start , int end )
    {
        if (str == null) return null;

        if( end    <  0            )  end    =  str.length() + end   ;
        if( start  <  0            )  start  =  str.length() + start ;
        if( end    >  str.length() )  end    =  str.length()         ;
        if( start  >  end          )  return ""                      ;
        if( start  <  0            )  start  =  0                    ;
        if( end    <  0            )  end    =  0                    ;

        return str . substring( start , end );
    }

    public static String left( String str, int len )
    { return (str == null) ? null : ((len < 0) ? "" : ((str.length() <= len) ? str : str.substring( 0 , len )));            }

    public static String right( String str, int len )
    { return (str == null) ? null : ((len < 0) ? "" : ((str.length() <= len) ? str : str.substring( str.length() - len ))); }

    public static String mid( String str, int pos, int len )
    {
        if( str == null                   )  return null;
        if( len < 0 || pos > str.length() )  return "";
        if( pos < 0                       )  pos = 0;

        return (str.length() <= (pos + len)) ? str.substring( pos ) : str.substring( pos, pos + len );
    }

}