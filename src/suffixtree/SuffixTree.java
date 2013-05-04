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
   }

   @Override
   public String toString() {
      return root.toString();
   }

   private void insertSuffix(String string, int begin) {
      Node leaf = new Node(string, begin, string.length());
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
}
