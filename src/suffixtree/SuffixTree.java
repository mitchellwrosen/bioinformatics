package suffixtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SuffixTree {
   private Node               root;
   private Collection<String> strings = new ArrayList<String>();
   private List<Node>         leaves  = new LinkedList<Node>();

   public SuffixTree() {
      this.root = new Node();
   }

   public void add(String string) {
      this.strings.add(string);
      insertSuffix(string, 0);

      for (Node leaf : leaves) {
         Node ptr = leaf;
         while (ptr != null) {
            ptr.count++;
            ptr = ptr.parent;
         }
      }
   }

   public String debugString() {
      return root.debugString();
   }

   private void insertSuffix(String string, int begin) {
      Node leaf = new LeafNode(string, begin, string.length());
      root.insertNode(leaf);
      leaves.add(leaf);

      begin++;
      if (begin < string.length()) {
         insertSuffix(string, begin);
      }
   }

   public List<Node> getLeaves() {
      return leaves;
   }

   public int numOccurrences(String string) {
      int nodeCharIndex = 0;

      Node node = root.getChild(string.charAt(0));
      if (node == null)
         return 0;

      for (int i = 0; i < string.length(); ++i, ++nodeCharIndex) {
         if (nodeCharIndex >= node.length()) {
            node = node.getChild(string.charAt(i));
            if (node == null)
               return 0;

            nodeCharIndex = 0;
         }

         if (string.charAt(i) != node.charAt(nodeCharIndex))
            return 0;
      }

      return node.getCount();
   }
}
