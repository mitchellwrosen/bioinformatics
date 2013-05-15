package suffixtree;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import suffixtree.SuffixTree.RepeatEntry;

public class SuffixTreeTest {

   @Test
   public void testAdd() {
      String string = "ABCDABABC";
      SuffixTree tree = SuffixTree.create(string);
      System.out.println(tree.getLeaves());
      System.out.println(tree.debugString());
      List<Integer> o = tree.getOccurrences("B");
      System.out.println(o);
      o = tree.getOccurrences("AB");
      System.out.println(o);
      List<RepeatEntry> repeats = tree.findRepeats(2);
      assertEquals(2, repeats.size());
      System.out.println("Repeats:");
      for (RepeatEntry r : repeats) {
         System.out.println(r.toString());
      }
   }
}
