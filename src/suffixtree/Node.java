/**
 * 
 */
package suffixtree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Erik Sandberg &lt;esandber@calpoly.edu&gt;
 * 
 */
public abstract class Node {
   protected int          labelSize         = -1;
   private InternalNode   parent            = null;
   protected int          stringLength      = 0;
   protected Set<Integer> stringIndicesSeen = new HashSet<Integer>();

   public Node() {
   }

   public abstract Character charAt(int n);

   public abstract String debugString();

   public abstract String getLabel();

   public abstract int getLabelLength();

   public int getLabelSize() {
      if (labelSize == -1) {
         labelSize = toString().length();
      }
      return labelSize;
   }

   /** If leftChar == null, then node is left diverse. */
   public abstract Character getLeftChar(int stringIndex);

   public abstract List<Node> getFullyDiverseNodes(int stringIndex);

   public int getLevel() {
      if (getParent() == null) {
         return 0;
      } else {
         return 1 + getParent().getLevel();
      }
   }

   /**
    * @return the parent
    */
   protected InternalNode getParent() {
      return parent;
   }

   public abstract int getStringLength();

   public abstract LeafNode insertNode(int stringIndex, NodeInfo info);

   public abstract boolean isLeaf();

   public abstract boolean isLeftDiverse(int stringIndex);

   /**
    * @param parent
    *           the parent to set
    */
   protected void setParent(InternalNode parent) {
      this.parent = parent;
   }

   public abstract void shiftBegin(int shift);

   @Override
   public String toString() {
      if (getParent() != null) {
         return getParent().toString() + getLabel();
      } else {
         return getLabel();
      }
   }
}
