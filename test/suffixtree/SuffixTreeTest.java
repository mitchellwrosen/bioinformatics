package suffixtree;

import java.util.List;

import org.junit.Test;

public class SuffixTreeTest {

   @Test
   public void testAdd() {
      String string = "ABCDABAB$";
      SuffixTree tree = SuffixTree.create(string);
      System.out.println(tree.getLeaves());
      System.out.println(tree.debugString());
      List<Integer> o = tree.getOccurrences("B");
      System.out.println(o);
      o = tree.getOccurrences("AB");
      System.out.println(o);
   }
}
