package suffixtree;

import java.util.List;

import org.junit.Test;

public class SuffixTreeTest {

   @Test
   public void testAdd() {
      String string = "AAAAAAAAAAAAA$";
      SuffixTree tree = SuffixTree.create(string);
      System.out.print(tree.getLeaves());
      System.out.print(tree.debugString());
      List<Integer> o = tree.getOccurrences("A");
      System.out.println(o);
      o = tree.getOccurrences("AA");
      System.out.println(o);
   }
}
