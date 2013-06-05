package suffixtree;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import model.Sequence;

import org.junit.Test;

import suffixtree.SuffixTree.PalindromeEntry;
import suffixtree.SuffixTree.RepeatEntry;
import suffixtree.SuffixTree.StartEntry;
import controller.Controller;

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
       * Repeats: [ 0, BA] [ 0, CBA] [ 1, ABC] [ 2, AB] [ 2, ABC] [ 3, CBA]
       */
      assertEquals(6, repeats.size());
   }

   @Test
   public void testPalendromes() {
      List<String> strings = new ArrayList<String>(2);

/*      String string = "ABCABCDCBAABC";
      List<PalindromeEntry> palindromes =
            SuffixTreeUtils.findPalindromes(string, 2, 1);
      for (int i = 0; i < palindromes.size(); i++) {
         System.out.println(palindromes.get(i).toString());
      }
      assertEquals(2, palindromes.size());

      strings.add("CATTGATCAACGA");
      strings.add("TCGTTGATCAATG");
      palindromes = SuffixTreeUtils.findPalindromes(strings, 3, 1);
      for (int i = 0; i < palindromes.size(); i++) {
         System.out.println(palindromes.get(i).toString());
      }
      assertEquals(1, palindromes.size());*/

      try {
         Sequence seq = new Sequence("test/files/paltest.txt");
//       Sequence seq = new Sequence("test/files/fosmid10.fna");
         strings = new ArrayList<String>(2);
         strings.add(seq.toString());
         strings.add(seq.reverseComplement().toString());
         System.out.println(strings);

/*         for (int i = 0; i < 20; i++) {
            System.out.println("Find Palindromes: " + i);
            long startTime = System.nanoTime();
            SuffixTreeUtils.findPalindromes(strings, 20, 0, 0);
            long endTime = System.nanoTime();

            long duration = endTime - startTime;
            System.out.println("Duration: "
                  + ((double) duration / (double) 1000000000));
         }
*/
         System.out.println("Find Palindromes");
         long startTime = System.nanoTime();
         List<PalindromeEntry> pals =
               SuffixTreeUtils.findPalindromes(strings, 2, 0, 20);
         long endTime = System.nanoTime();

         long duration = endTime - startTime;
         System.out.println("Duration: "
               + ((double) duration / (double) 1000000000));
         for (PalindromeEntry p : pals) {
            System.out.println(p.toString());
         }
      } catch (IllegalArgumentException | IOException e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   @Test
   public void testFindMRNA() {
      try {
         Sequence seq = new Sequence("test/files/paltest.txt");
//         Sequence seq = new Sequence("test/files/fosmid10.fna");
         for (int i = 0; i < 5; i++) {
            System.out.println("Find mRNA: " + i);
            long timeMethodStartTime = System.nanoTime();
            String mrna = Controller.findMRNA(i, seq);
            long timeMethodEndTime = System.nanoTime();
            long timeMethodDuration = timeMethodEndTime - timeMethodStartTime;
            System.out.println("Duration: "
                  + ((double) timeMethodDuration / (double) 1000000000));
            System.out.println(mrna);
         }
      } catch (IllegalArgumentException | IOException e) {
         Assert.fail();
         e.printStackTrace();
      }
   }
}
