package utils;

import java.util.Collection;

public class Functional {
   public static <A, B> Collection<B> map(Mapper<A, B> f, Collection<A> as)
         throws InstantiationException, IllegalAccessException {
      Collection<B> bs = as.getClass().newInstance();
      for (A a : as)
         bs.add(f.execute(a));
      return bs;
   }

   public static <A, B> A foldl(Folder<A, B> f, A a, Iterable<B> xs) {
      A acc = a;
      for (B x : xs)
         acc = f.execute(acc, x);
      return acc;
   }
}
