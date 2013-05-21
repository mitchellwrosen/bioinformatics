package suffixtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SuffixTree {
   private InternalNode   root;
   private List<String>   strings;
   private List<LeafNode> leaves;

   public class StartEntry {
      public int stringIndex;
      public int start;

      public StartEntry(int stringIndex, int start) {
         this.stringIndex = stringIndex;
         this.start = start;
      }
   }

   public class RepeatEntry {
      private SuffixTree       tree;
      private List<StartEntry> starts;
      private int              length;

      public RepeatEntry(SuffixTree tree, List<StartEntry> starts, int length) {
         this.tree = tree;
         this.starts = starts;
         this.length = length;
      }

      public List<StartEntry> getStarts() {
         return starts;
      }

      public int getLength() {
         return length;
      }

      @Override
      public String toString() {
         StartEntry start = starts.get(0);
         return "[ "
               + start.stringIndex
               + ", "
               + tree.strings.get(start.stringIndex).substring(start.start,
                     start.start + length) + "]";
      }
   }

   protected SuffixTree(List<String> strings) {
      this.root = new InternalNode();
      this.leaves = new LinkedList<LeafNode>();
      this.strings = new ArrayList<String>(strings.size());
      for (String s : strings) {
         this.strings.add(s + "$");
      }
   }

   /**
    * Creates a suffix tree from the given string.
    * 
    * @return
    */
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

   public String getString(int stringIndex) {
      return strings.get(stringIndex);
   }

   protected void fill() {

      for (int stringIndex = 0; stringIndex < strings.size(); stringIndex++) {
         String string = strings.get(stringIndex);
         for (int i = 0; i < string.length(); ++i) {
            NodeInfo info = new NodeInfo(string, i, i, string.length());
            LeafNode leaf = root.insertNode(stringIndex, info);
            leaves.add(leaf);
         }
      }

      for (LeafNode leaf : leaves) {
         InternalNode node = leaf.getParent();
         // Add all leaves to internal nodes
         while (node != null) {
            node.addLeaf(leaf);
            node = node.getParent();
         }
      }
   }

   public List<LeafNode> getLeaves() {
      return leaves;
   }

   public List<StartEntry> getOccurrences(String string) {
      int nodeCharIndex = 0;

      Node node = root.getChild(string.charAt(0));
      if (node == null)
         return new ArrayList<StartEntry>(0);

      for (int i = 0; i < string.length(); ++i, ++nodeCharIndex) {
         if (nodeCharIndex >= node.getLabelLength()) {
            if (node instanceof InternalNode) {
               node = ((InternalNode) node).getChild(string.charAt(i));
            } else {
               node = null;
            }
            if (node == null) {
               return new ArrayList<StartEntry>(0);
            }

            nodeCharIndex = 0;
         }

         if (string.charAt(i) != node.charAt(nodeCharIndex))
            return new ArrayList<StartEntry>(0);
      }

      List<StartEntry> retval = null;

      if (node instanceof LeafNode) {
         // This is a leaf so there is only one occurrence.
         retval = new ArrayList<StartEntry>(1);
         for (int stringIndex = 0; stringIndex < strings.size(); stringIndex++) {
            Integer start = ((LeafNode) node).getStringBegin(stringIndex);
            if (start != null) {
               retval.add(new StartEntry(stringIndex, start));
            }
         }
      } else if (node instanceof InternalNode) {
         // This is an internal node so there are multiple occurrences.
         List<LeafNode> leaves = ((InternalNode) node).getLeaves();
         retval = new ArrayList<StartEntry>();
         for (LeafNode leaf : leaves) {
            for (int stringIndex = 0; stringIndex < strings.size(); stringIndex++) {
               Integer start = leaf.getStringBegin(stringIndex);
               if (start != null) {
                  retval.add(new StartEntry(stringIndex, start));
               }
            }
         }
      } else {
         // This shouldn't happen
         throw new RuntimeException();
      }

      return retval;
   }

   public List<RepeatEntry> findRepeats(int length) {
      List<RepeatEntry> repeats = new ArrayList<RepeatEntry>();
      for (int stringIndex = 0; stringIndex < strings.size(); stringIndex++) {
         List<Node> leftDiverseNodes = root.getLeftDiverseNodes(stringIndex);
         for (Node node : leftDiverseNodes) {
            if (node.getStringLength() >= length) {
               Collection<LeafNode> leaves = ((InternalNode) node).getLeaves();
               List<StartEntry> starts = new ArrayList<StartEntry>(
                     leaves.size());
               for (LeafNode leaf : leaves) {
                  Integer start = leaf.getStringBegin(stringIndex);
                  if (start != null) {
                     starts.add(new StartEntry(stringIndex, start));
                  }
               }
               repeats
                     .add(new RepeatEntry(this, starts, node.getStringLength()));
            }
         }
      }
      return repeats;
   }

   public String debugString() {
      return root.debugString();
   }
}
