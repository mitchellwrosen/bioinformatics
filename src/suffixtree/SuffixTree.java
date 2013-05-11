package suffixtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SuffixTree {
   private Node               root;
   private Collection<String> strings;
   private List<LeafNode>     leaves;

   public class RepeatEntry {
      private String        string;
      private List<Integer> starts;
      private int           length;

      public List<Integer> getStarts() {
         return starts;
      }

      public int getLength() {
         return length;
      }

      public String toString() {
         return string.substring(starts.get(0), length);
      }
   }

   protected SuffixTree(List<String> strings) {
      root = new Node();
      leaves = new LinkedList<LeafNode>();
      this.strings = strings;
   }

   /**
    * Creates a suffix tree from the given string.
    * 
    * @param string
    *           Must end with a terminal character (i.e. '$')
    * @return
    */
   public static SuffixTree create(String string) {
      List<String> strings = new ArrayList<String>(1);
      strings.add(string);
      return create(strings);
   }

   // TOOD Make SuffixTree generic then this can be public
   protected static SuffixTree create(List<String> strings) {
      SuffixTree tree = new SuffixTree(strings);
      tree.fill();
      return tree;
   }

   protected void fill() {
      for (String string : strings) {
         for (int i = 0; i < string.length(); ++i) {
            LeafNode leaf = new LeafNode(string, i, string.length());
            root.insertNode(leaf);
            leaves.add(leaf);
         }
      }

      for (LeafNode leaf : leaves) {
         Node node = leaf.parent;
         while (node != null) {
            node.addLeaf(leaf);
            node = node.parent;
         }
      }
   }

   public List<LeafNode> getLeaves() {
      return leaves;
   }

   public List<Integer> getOccurrences(String string) {
      int nodeCharIndex = 0;

      Node node = root.getChild(string.charAt(0));
      if (node == null)
         return null;

      for (int i = 0; i < string.length(); ++i, ++nodeCharIndex) {
         if (nodeCharIndex >= node.length()) {
            node = node.getChild(string.charAt(i));
            if (node == null)
               return null;

            nodeCharIndex = 0;
         }

         if (string.charAt(i) != node.charAt(nodeCharIndex))
            return null;
      }

      List<LeafNode> leaves = node.getLeaves();
      List<Integer> retval = new ArrayList<Integer>(leaves.size());
      for (LeafNode leaf : leaves) {
         retval.add(leaf.getStart());
      }

      return retval;
   }

   public List<RepeatEntry> findRepeats(int length) {
      // TODO implement
      return null;
   }

   public String debugString() {
      return root.debugString();
   }
}
