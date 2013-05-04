package suffixtree;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class SuffixTreeTest {

   @Test
   public void testAdd() {
      SuffixTree tree = new SuffixTree();
      //tree.add("CATCAT$");
      tree.add("AABABC$");
      System.out.print(tree.getLeaves());
      System.out.print(tree.toString());
      assertEquals("", tree.toString());
   }
}
