package suffixtree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InternalNode extends Node {
   protected int                     begin    = 0;
   protected Map<Character, Node>    children = new HashMap<Character, Node>();
   protected int                     end      = 0;
   protected List<LeafNode>          leaves   = new LinkedList<LeafNode>();
   protected Map<Integer, Character> leftChar = new HashMap<Integer, Character>();
   protected String                  string   = "";

   public InternalNode() {
      super();
   }

   public InternalNode(String string, int begin, int end) {
      this.string = string;
      this.begin = begin;
      this.end = end;
   }

   void addChild(Node child) {
      children.put(child.charAt(0), child);
   }

   public void addLeaf(LeafNode leaf) {
      this.stringIndicesSeen.addAll(leaf.stringIndicesSeen);
      this.leaves.add(leaf);
   }

   @Override
   public Character charAt(int n) {
      return string.charAt(begin + n);
   }

   @Override
   public String debugString() {
      StringBuilder sb = new StringBuilder();
      int level = getLevel();
      for (int i = 0; i < level + 1; i++) {
         sb.append('\t');
      }
      String tabs = sb.toString();
      sb = new StringBuilder();
      sb.append("{");
      for (Integer i : stringIndicesSeen) {
         sb.append(" (").append(i).append(", ").append(getLeftChar(i))
               .append(")");
      }
      sb.append(" } ");
      if (string.equals("")) {
         sb.append("<root>");
      } else {
         sb.append(string.substring(begin, end));
      }
      sb.append(String.format(" (%d)", this.getLeafCount())).append("\n");
      for (Character key : children.keySet()) {
         sb.append(tabs).append("[").append(key).append("]\t")
               .append(children.get(key).debugString());
      }
      return sb.toString();
   }

   public Node getChild(Character nextChar) {
      return children.get(nextChar);
   }

   @Override
   public String getLabel() {
      return string.substring(begin, end);
   }

   @Override
   public int getLabelLength() {
      return end - begin;
   }

   public int getLeafCount() {
      return leaves.size();
   }

   public List<LeafNode> getLeaves() {
      return this.leaves;
   }

   @Override
   public Character getLeftChar(int stringIndex) {
      if (!leftChar.containsKey(stringIndex)) {
         setLeftChar(stringIndex);
      }
      return leftChar.get(stringIndex);
   }

   public boolean isRightDiverse(int stringIndex) {
      // This node is right diverse only if two or more children have seen
      // the stringIndex.
      int childrenThatHaveSeenStringIndex = 0;
      for (Node child : children.values()) {
         if (childrenThatHaveSeenStringIndex < 2
               && child.stringIndicesSeen.contains(stringIndex)) {
            childrenThatHaveSeenStringIndex++;
            if (childrenThatHaveSeenStringIndex == 2) {
               // At least two children have seen this stringIndex so this is
               // right diverse.
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public List<Node> getFullyDiverseNodes(int stringIndex) {
      List<Node> fullyDiverseNodes = new LinkedList<Node>();

      if (stringIndicesSeen.contains(stringIndex) && isLeftDiverse(stringIndex)) {
         // Check for right diversity.
         // In a gereralized suffix tree, some internal node may not be right
         // diverse.
         if (isRightDiverse(stringIndex)) {
            fullyDiverseNodes.add(this);
         }

         // Since this is left diverse, some of the children may be as well.
         for (Node child : this.children.values()) {
            List<Node> childsFullyDiverseNodes = child
                  .getFullyDiverseNodes(stringIndex);
            if (childsFullyDiverseNodes != null) {
               fullyDiverseNodes.addAll(childsFullyDiverseNodes);
            }
         }
      }

      return fullyDiverseNodes;
   }

   @Override
   public int getStringLength() {
      return this.stringLength;
   }

   @Override
   public LeafNode insertNode(int stringIndex, NodeInfo info) {
      LeafNode leaf;

      int i = 0;
      while (i < getLabelLength() && info.charAt(i) == this.charAt(i))
         i++;

      info.labelBegin += i;
      if (i < getLabelLength()) {
         // Split - create an internal node
         InternalNode newNode = new InternalNode(this.string, this.begin,
               this.begin + i);
         this.shiftBegin(i);

         getParent().addChild(newNode);
         newNode.setParent(this.getParent());

         newNode.addChild(this);
         this.setParent(newNode);

         leaf = new LeafNode(stringIndex, info);
         newNode.addChild(leaf);
         leaf.setParent(newNode);
      } else {
         // Find the next child node
         Node child = this.getChild(info.charAt(0));

         if (child == null) {
            leaf = new LeafNode(stringIndex, info);
            this.addChild(leaf);
            leaf.setParent(this);
         } else {
            leaf = child.insertNode(stringIndex, info);
         }
      }
      return leaf;
   }

   @Override
   public boolean isLeaf() {
      return false;
   }

   @Override
   public boolean isLeftDiverse(int stringIndex) {
      return getLeftChar(stringIndex) == null;
   }

   private void setLeftChar(int stringIndex) {
      Set<Character> lefts = new HashSet<Character>();
      for (Node child : children.values()) {
         if (child.stringIndicesSeen.contains(stringIndex)) {
            lefts.add(child.getLeftChar(stringIndex));
         }
      }
      if (lefts.size() == 1) {
         leftChar.put(stringIndex, lefts.iterator().next());
      } else {
         leftChar.put(stringIndex, null);
      }
   }

   @Override
   protected void setParent(InternalNode parent) {
      super.setParent(parent);
      this.stringLength = getParent().stringLength + (end - begin);
   }

   @Override
   public void shiftBegin(int shift) {
      this.begin += shift;
   }
}
