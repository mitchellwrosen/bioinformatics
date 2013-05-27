package suffixtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SuffixTree {
   private InternalNode              root;
   private List<String>              strings;
   private Map<StartEntry, LeafNode> leaves;

   /**
    * Represents any position in any string.
    * 
    * @author Erik Sandberg &lt;esandber@calpoly.edu&gt;
    */
   public class StartEntry {
      /**
       * The index of the string in SuffixTree.strings
       */
      public int stringIndex;
      /**
       * The start position in the string
       */
      public int start;

      /**
       * Construct a StringEntry
       * 
       * @param stringIndex
       *           The index of the string in SuffixTree.strings
       * @param start
       *           The start position in the string
       */
      public StartEntry(int stringIndex, int start) {
         this.stringIndex = stringIndex;
         this.start = start;
      }

      @Override
      public boolean equals(Object o) {
         if (!(o instanceof StartEntry)) {
            return false;
         } else {
            StartEntry e = (StartEntry) o;
            return this.start == e.start && this.stringIndex == e.stringIndex;
         }
      }

      @Override
      public int hashCode() {
         return start + 4000000 * stringIndex;
      }
   }

   /**
    * Represents a string that is repeated in the SuffixTree.
    * 
    * @author Erik Sandberg &lt;esandber@calpoly.edu&gt;
    */
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

   public class PalindromeEntry {
      private SuffixTree tree;
      private StartEntry forwardStart;
      private StartEntry reverseStart;
      private int        radius;
      private int        gap;
      private StartEntry start;
      private int        length;

      public PalindromeEntry(SuffixTree tree, StartEntry forward,
            StartEntry reverse, int radius, int gap) {
         this.tree = tree;
         this.forwardStart = forward;
         this.reverseStart = reverse;
         this.radius = radius;
         this.gap = gap;
         this.start = new StartEntry(forwardStart.stringIndex,
               forwardStart.start - gap - radius);
         this.length = radius + radius + gap;
      }

      @Override
      public String toString() {
         return tree.strings.get(forwardStart.stringIndex).substring(
               this.start.start, forwardStart.start + radius);
      }

      public StartEntry getStart() {
         return start;
      }

      public int getLength() {
         return length;
      }

      /**
       * @return the tree
       */
      public SuffixTree getTree() {
         return tree;
      }

      /**
       * @return the forwardStart
       */
      public StartEntry getForwardStart() {
         return forwardStart;
      }

      /**
       * @return the reverseStart
       */
      public StartEntry getReverseStart() {
         return reverseStart;
      }

      /**
       * @return the length
       */
      public int getRadius() {
         return radius;
      }

      /**
       * @return the gap
       */
      public int getGap() {
         return gap;
      }
   }

   protected SuffixTree(List<String> strings) {
      this.root = new InternalNode();
      this.leaves = new HashMap<StartEntry, LeafNode>();
      this.strings = new ArrayList<String>(strings.size());
      for (String s : strings) {
         this.strings.add(s + "$");
      }
   }

   /**
    * Creates a suffix tree from the given string.
    * 
    * @return The new suffix tree
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
            leaves.put(new StartEntry(stringIndex, i), leaf);
         }
      }

      for (LeafNode leaf : leaves.values()) {
         InternalNode node = leaf.getParent();
         // Add all leaves to internal nodes
         while (node != null) {
            node.addLeaf(leaf);
            node = node.getParent();
         }
      }
   }

   public List<StartEntry> getOccurrences(String string) {
      int nodeCharIndex = 0;

      Node node = root.getChild(string.charAt(0));
      if (node == null) {
         return new ArrayList<StartEntry>(0);
      }

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

         if (string.charAt(i) != node.charAt(nodeCharIndex)) {
            return new ArrayList<StartEntry>(0);
         }
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
         List<Node> leftDiverseNodes = root.getFullyDiverseNodes(stringIndex);
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

   private int longestCommonExtension(StartEntry forwardStart,
         StartEntry reverseStart) {
      InternalNode forwardNode = null;
      InternalNode reverseNode = null;
      Set<InternalNode> forwardParents = new HashSet<InternalNode>();

      forwardNode = loop1(forwardStart);
      reverseNode = loop1(reverseStart);

      if (forwardNode == null || reverseNode == null) {
         throw new IllegalArgumentException();
      }

      forwardNode = loop2(forwardNode, forwardParents);

      reverseNode = loop3(reverseNode, forwardParents);

      return reverseNode.getStringLength();
   }

   private InternalNode loop3(InternalNode reverseNode,
         Set<InternalNode> forwardParents) {
      while (!forwardParents.contains(reverseNode)) {
         reverseNode = reverseNode.getParent();
      }
      return reverseNode;
   }

   private InternalNode loop2(InternalNode forwardNode,
         Set<InternalNode> forwardParents) {
      // Add all the parents of forward node to a set.
      while (forwardNode != null) {
         forwardParents.add(forwardNode);
         forwardNode = forwardNode.getParent();
      }
      return forwardNode;
   }

   private InternalNode loop1(StartEntry forwardStart) {
      // Find the leaf of the forward start
      return leaves.get(forwardStart).getParent();
   }

   /**
    * Assumes that the string at index 1 is the reverse of the string at index
    * 0.
    * 
    * @param radius
    *           Minimum radius of the palendrome to find.
    * @param gap
    *           Largest allowable gap.
    * @return
    */
   public List<PalindromeEntry> findPalindromes(int radius, int gap) {
      List<PalindromeEntry> entries = new ArrayList<PalindromeEntry>();
      int forwardIdx = 0;
      int reverseIdx = 1;
      String forward = this.strings.get(forwardIdx);
      String reverse = this.strings.get(reverseIdx);
      int n = forward.length();

      if (n != reverse.length()) {
         throw new RuntimeException(
               "Forward and reverse strings are of different lengths");
      }

      for (int g = 0; g <= gap; g++) {
         // Go until q < length - 2 because of $
         for (int q = g; q < forward.length() - 2; q++) {
            StartEntry forwardStart = new StartEntry(forwardIdx, q + 1);
            // length - q - 1 - 1(for $) + gap
            StartEntry reverseStart = new StartEntry(reverseIdx, n - q - 2 + g);
            int lce = longestCommonExtension(forwardStart, reverseStart);
            if (lce >= radius) {
               entries.add(new PalindromeEntry(this, forwardStart,
                     reverseStart, lce, g));
            }
         }
      }

      return entries;
   }

   public String debugString() {
      return root.debugString();
   }
}
