package utils;

/**
 * An interface that knows how to fold a B into an A.
 *  
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 *
 * @param <A>  The accumulator
 * @param <B>  The object
 */
public interface Folder<A, B> {
   A execute(A a, B b);
}
