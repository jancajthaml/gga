package rabbit.logic;

public final class Preconditions
{
    private Preconditions() {}

    public static void ensure(boolean condition, String errorMessageFormat, Object ... errorMessageArgs)
    { if (!condition) throw new IllegalStateException(String.format(errorMessageFormat, errorMessageArgs)); }

    public static void ensure(boolean condition, String errorMessage)
    { if (!condition) throw new IllegalArgumentException(errorMessage); }

    public static void _is_true_argument(boolean expression)
    { if (!expression) throw new IllegalArgumentException(); }

    public static void _is_true_argument(boolean expression, Object errorMessage)
    { if (!expression) throw new IllegalArgumentException(String.valueOf(errorMessage)); }

    public static void _is_true_argument(boolean expression, String errorMessageTemplate, Object ... errorMessageArgs)
    { if (!expression) throw new IllegalArgumentException($(errorMessageTemplate, errorMessageArgs)); }

    public static void _is_true_state(boolean expression)
    { if (!expression) throw new IllegalStateException(); }

    public static void _is_true_state(boolean expression, Object errorMessage)
    { if (!expression) throw new IllegalStateException(String.valueOf(errorMessage)); }

    public static void _is_true_state(boolean expression, String errorMessageTemplate, Object ... errorMessageArgs)
    { if (!expression) throw new IllegalStateException($(errorMessageTemplate, errorMessageArgs)); }

    public static <T> T _not_null(T reference)
    {
    	if (reference == null) throw new NullPointerException();
        return reference;
    }

    public static <T> T _not_null(T reference, Object errorMessage)
    {
        if (reference == null) throw new NullPointerException(String.valueOf(errorMessage));
        return reference;
    }

    public static <T> T _not_null_argument(T reference, String name)
    {
        if (reference == null) throw new NullPointerException($("Argument '%s' must not be null", name));
        return reference;
    }

    public static <T> T _not_null(T reference, String errorMessageTemplate, Object... errorMessageArgs)
    {
        if (reference == null) throw new NullPointerException($(errorMessageTemplate, errorMessageArgs));
        return reference;
    }

    public static int _in_bounds(int index, int size)
    { return _in_bounds(index, size, "index"); }

    public static int _in_bounds(int index, int size, String desc)
    {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(_in_bounds_msg(index, size, desc));
        return index;
    }

    public static int _in_array(int index, int size)
    { return _in_array(index, size, "index"); }

    public static int _in_array(int index, int size, String desc)
    {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException(_in_array_msg(index, size, desc));
        return index;
    }

    public static void in_array(int start, int end, int size)
    { if (start < 0 || end < start || end > size) throw new IndexOutOfBoundsException(in_array_msg(start, end, size)); }

    private static String in_array_msg(int start, int end, int size)
    {
        if (start < 0 || start > size)	return _in_array_msg(start, size, "start index");
        if (end < 0 || end > size)		return _in_array_msg(end, size, "end index");

        return $("end index (%s) must not be less than start index (%s)", end, start);
    }

    private static String _in_array_msg(int index, int size, String desc)
    {
        if (index < 0)		return $("%s (%s) must not be negative", desc, index);
        else if (size < 0)	throw new IllegalArgumentException("negative size: " + size);
        else				return $("%s (%s) must not be greater than size (%s)", desc, index, size);
    }
    
    private static String _in_bounds_msg(int index, int size, String desc)
    {
        if (index < 0)		return $("%s (%s) must not be negative", desc, index);
        else if (size < 0)	throw new IllegalArgumentException("negative size: " + size);
        else				return $("%s (%s) must be less than size (%s)", desc, index, size);
    }

    
    private static final String $(String template, Object ... args)
    {
        StringBuilder builder	= new StringBuilder(template.length() + (args.length << 4));
        int templateStart		= 0;
        int i					= 0;
        
        while (i < args.length)
        {
            int placeholderStart = template.indexOf("%s", templateStart);
            
            if (placeholderStart == -1) break;
            
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        if (i < args.length)
        {
            builder.append(" [");
            builder.append(args[i++]);
            
            while (i < args.length)
            {
                builder.append(", ");
                builder.append(args[i++]);
            }
            
            builder.append("]");
        }

        return builder.toString();
    }

}