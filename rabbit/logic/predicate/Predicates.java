package rabbit.logic.predicate;

import static rabbit.logic.Preconditions.*;
import static rabbit.struct.util.StringUtils.join;

import java.util.Collection;

import rabbit.struct.common.ImmutableList;
import rabbit.struct.util.StringUtils;

public final class Predicates
{

	private static final Predicate<?> NULL     =  new Null();
	private static final Predicate<?> NOTNULL  =  new NotNull();
	private static final Predicate<?> TRUE     =  new True();
	private static final Predicate<?> FALSE    =  new False();

    private Predicates() {}

    @SuppressWarnings("unchecked") public static <T> Predicate<T> _true     ()  { return (Predicate<T>) TRUE;     }
    @SuppressWarnings("unchecked") public static <T> Predicate<T> _false    ()  { return (Predicate<T>) FALSE;    }
    @SuppressWarnings("unchecked") public static <T> Predicate<T> _null     ()  { return (Predicate<T>) NULL;     }
    @SuppressWarnings("unchecked") public static <T> Predicate<T> _not_null ()  { return (Predicate<T>) NOTNULL;  }

    public static <T> Predicate<T> _not(Predicate<T> predicate)
    { return new Not<T>(predicate); }

    public static <T> Predicate<T> _and(Collection<? extends Predicate<? super T>> set)
    { return new And<T>(set); }

    public static <T> Predicate<T> _and(Predicate<? super T> ... set)
    { return new And<T>(ImmutableList.of(set)); }

    public static <T> Predicate<T> _and(Predicate<? super T> left, Predicate<? super T> right)
    {
    	_not_null_argument( left  , "left"  );
    	_not_null_argument( right , "right" );
    	
        return new And<T>(ImmutableList.<Predicate<? super T>>of(left, right));
    }

    public static <T> Predicate<T> _or(Collection<? extends Predicate<? super T>> set)
    { return new Or<T>(set); }

    public static <T> Predicate<T> _or(Predicate<? super T> ... set)
    { return new Or<T>(ImmutableList.of(set)); }

    public static <T> Predicate<T> _or(Predicate<? super T> left, Predicate<? super T> right)
    {
    	_not_null_argument( left  , "left"  );
    	_not_null_argument( right , "right" );
        
        return new Or<T>(ImmutableList.<Predicate<? super T>>of(left, right));
    }

    public static <T> Predicate<T> _equal( T other )
    { return (other == null) ? Predicates.<T>_null() : new EqualTo<T>(other); }

    public static Predicate<Object> _instance(Class<?> clazz)
    { return new InstanceOf(clazz); }

    public static <T> Predicate<T> _in(Collection<? extends T> target)
    { return new In<T>(target); }

    //--- [ nested ] ---------------------------------------------
    
    private static class True implements Predicate<Object>
    {
        public boolean eval               ( Object o )  { return true;      }
        @Override public String toString  (          )  { return "True";    }
    }

    private static class False implements Predicate<Object>
    {
        public boolean eval               ( Object o )  { return false;     }
        @Override public String toString  (          )  { return "False";   }
    }

    private static class Null implements Predicate<Object>
    {
        public boolean eval               ( Object o )  { return o == null; }
        @Override public String toString  (          )  { return "Null";    }
    }

    private static class NotNull implements Predicate<Object>
    {
        public boolean eval               ( Object o )  { return o != null; }
        @Override public String toString  (          )  { return "NotNull"; }
    }

    private static class Not<T> implements Predicate<T>
    {
        private final Predicate<T> predicate;

        private Not(Predicate<T> predicate)
        {
        	_not_null_argument(predicate, "predicate");
        	
            this.predicate = predicate;
        }

        public boolean eval(T t)
        { return !predicate.eval(t); }

        public String toString()
        { return "Not(" + predicate.toString() + ")"; }
    }

    private static class And<T> implements Predicate<T>
    {
        private final Collection<? extends Predicate<? super T>> components;

        private And(Collection<? extends Predicate<? super T>> components)
        { this.components = components; }

        public boolean eval(T t)
        {
            for (Predicate<? super T> predicate : components) if (!predicate.eval(t)) return false;
            return true;
        }

        @Override public String toString()
        { return "And(" + join(components, ", ") + ")"; }
    }

    private static class Or<T> implements Predicate<T>
    {
        private final Collection<? extends Predicate<? super T>> components;

        private Or(Collection<? extends Predicate<? super T>> components)
        { this.components = components; }

        public boolean eval(T t)
        {
            for (Predicate<? super T> predicate : components) if (predicate.eval(t)) return true;
            return false;
        }

        @Override public String toString()
        { return "Or(" + StringUtils.join(components, ", ") + ")"; }
    }

    private static class EqualTo<T> implements Predicate<T>
    {
        private final T target;

        private EqualTo                   ( T target )  { this.target = target;               }
        public boolean eval               ( T t      )  { return target.equals(t);            }
        @Override public String toString  (          )  { return "IsEqualTo(" + target + ")"; }
    }

    private static class InstanceOf implements Predicate<Object>
    {
        private final Class<?> clazz;

        private InstanceOf(Class<?> clazz)
        {
        	_not_null_argument(clazz, "clazz");
        	
            this.clazz = clazz;
        }

        public boolean eval(Object o)
        { return clazz.isInstance(o); }

        @Override public String toString()
        { return "InstanceOf(" + clazz.getName() + ")"; }
    }

    private static class In<T> implements Predicate<T>
    {
        private final Collection<?> target;

        private In(Collection<?> target)
        {
        	_not_null_argument(target, "target");
        	
            this.target = target;
        }

        public boolean eval(T t)
        {
            try { return target.contains(t); }
            catch (NullPointerException e) { return false; }
            catch (ClassCastException e) { return false; }
        }

        @Override public String toString()
        { return "In(" + target + ")"; }
    }

}