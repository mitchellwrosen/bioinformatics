package suffixtree;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import suffixtree.SuffixTree.RepeatEntry;
import suffixtree.SuffixTree.StartEntry;

public class SuffixTreeTest {

   private static void printOccurrences(List<StartEntry> o) {
      System.out.print("{");
      for (StartEntry start : o) {
         System.out.print(" [" + start.stringIndex + ", " + start.start + "]");
      }
      System.out.println(" }");
   }

   @Test
   public void testAdd() {
      String[] strings = { "CBABADCBA", "ABCABC", "ABCDABABC", "CBACBA" };
      SuffixTree tree = SuffixTree.create(Arrays.asList(strings));
      System.out.println(tree.getLeaves());
      System.out.println(tree.debugString());
      List<StartEntry> o = tree.getOccurrences("B");
      printOccurrences(o);
      o = tree.getOccurrences("AB");
      printOccurrences(o);
      List<RepeatEntry> repeats = tree.findRepeats(2);
      System.out.println("Repeats:");
      for (RepeatEntry r : repeats) {
         System.out.println(r.toString());
      }
      assertEquals(2, repeats.size());
   }
}
