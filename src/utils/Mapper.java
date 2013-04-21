package utils;

/**
 * An interface that maps a [A] to a [B]
 * 
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 * 
 * @param <A> Map from
 * @param <B> Map to
 */
public interface Mapper<A,B> {
   B execute(A a);
}
