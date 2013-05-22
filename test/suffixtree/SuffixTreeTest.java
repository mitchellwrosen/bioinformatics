package suffixtree;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import suffixtree.SuffixTree.PalindromeEntry;
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
      String[] strings = { "CBABADCBA", "XABCABC", "ABCDABABC", "CBACBA" };
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
      /*
       * Repeats:
       * [ 0, BA]
       * [ 0, CBA]
       * [ 1, ABC]
       * [ 2, AB]
       * [ 2, ABC]
       * [ 3, CBA]
       */
      assertEquals(6, repeats.size());
   }

   @Test
   public void testPalendromes() {
      String string = "ABCABCDCBAABC";
      List<PalindromeEntry> palindromes = SuffixTreeUtils.findPalindromes(
            string, 2, 1);
      for (int i = 0; i < palindromes.size(); i++) {
         System.out.println(palindromes.get(i).toString());
      }
      assertEquals(2, palindromes.size());

      List<String> strings = new ArrayList<String>(2);
      strings.add("CATTGATCAACGA");
      strings.add("TCGTTGATCAATG");
      palindromes = SuffixTreeUtils.findPalindromes(strings, 3, 1);
      for (int i = 0; i < palindromes.size(); i++) {
         System.out.println(palindromes.get(i).toString());
      }
      assertEquals(1, palindromes.size());
   }
}
