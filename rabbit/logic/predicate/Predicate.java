package rabbit.logic.predicate;

public interface Predicate<T>
{

	boolean eval(T input);

}
