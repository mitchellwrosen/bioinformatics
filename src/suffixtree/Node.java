/**
 * 
 */
package suffixtree;

import java.util.List;

/**
 * @author Erik Sandberg &lt;esandber@calpoly.edu&gt;
 * 
 */
public abstract class Node {
   protected String       string;
   protected int          begin;
   protected int          end;
   protected int          labelSize = -1;
   protected InternalNode parent    = null;

   public Node() {
      this.string = "";
      this.begin = 0;
      this.end = 0;
   }

   public Node(String string, int begin, int end) {
      this.string = string;
      this.begin = begin;
      this.end = end;
   }

   public abstract boolean isLeaf();

   public boolean isLeftDiverse() {
      return getLeftChar() == null;
   }

   /** If leftChar == null, then node is left diverse. */
   public abstract Character getLeftChar();

   public abstract List<Node> getLeftDiverseNodes();

   public int length() {
      return end - begin;
   }

   public char charAt(int n) {
      return string.charAt(begin + n);
   }

   public int getLevel() {
      if (parent == null) {
         return 0;
      } else {
         return 1 + parent.getLevel();
      }
   }

   public String toString() {
      if (parent != null) {
         return parent.toString() + string.substring(begin, end);
      } else {
         return string.substring(begin, end);
      }
   }

   public int getLabelSize() {
      if (labelSize == -1) {
         labelSize = toString().length();
      }
      return labelSize;
   }

   public abstract String debugString();

   public abstract void insertNode(LeafNode node);
}
