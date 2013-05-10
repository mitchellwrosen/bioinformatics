package suffixtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SuffixTree {
   private Node               root;
   private Collection<String> strings;
   private List<Node>         leaves;

   protected SuffixTree(List<String> strings) {
      root = new Node();
      leaves = new LinkedList<Node>();
      this.strings = strings;
   }
   
   public static SuffixTree create(String string) {
      List<String> strings = new ArrayList<String>(1);
      strings.add(string);
      return create(strings);
   }
   
   public static SuffixTree create(List<String> strings) {
      SuffixTree tree = new SuffixTree(strings);
      tree.fill();
      return tree;
   }
   
   protected void fill() {
      for (String string : strings) {
         for (int i = 0; i < string.length(); ++i) {
            Node leaf = new LeafNode(string, i, string.length());
            root.insertNode(leaf);
            leaves.add(leaf);
         }
      }
      
      for (Node leaf : leaves) {
         Node ptr = leaf;
         while (ptr != null) {
            ptr.count++;
            ptr = ptr.parent;
         }
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

   public String debugString() {
      return root.debugString();
   }
}