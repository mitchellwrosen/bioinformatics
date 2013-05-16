/**
 * 
 */
package suffixtree;

import java.util.List;

import suffixtree.LeafNode.NodeInfo;

/**
 * @author Erik Sandberg &lt;esandber@calpoly.edu&gt;
 * 
 */
public abstract class Node {
   protected int          labelSize = -1;
   protected InternalNode parent    = null;

   public Node() {
   }

   public abstract boolean isLeaf();

   public boolean isLeftDiverse() {
      return getLeftChar() == null;
   }

   /** If leftChar == null, then node is left diverse. */
   public abstract Character getLeftChar();

   public abstract List<Node> getLeftDiverseNodes();

   public abstract void shiftBegin(int shift);

   public int length() {
      return end - begin;
   }

   public abstract char charAt(int n);

   public int getLevel() {
      if (parent == null) {
         return 0;
      } else {
         return 1 + parent.getLevel();
      }
   }

   public abstract String getLabel();

   public String toString() {
      if (parent != null) {
         return parent.toString() + getLabel();
      } else {
         return getLabel();
      }
   }

   public int getLabelSize() {
      if (labelSize == -1) {
         labelSize = toString().length();
      }
      return labelSize;
   }

   public abstract String debugString();

   public abstract void insertNode(int stringIndex, NodeInfo info);
}
