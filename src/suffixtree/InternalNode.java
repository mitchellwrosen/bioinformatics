package suffixtree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import suffixtree.LeafNode.NodeInfo;

public class InternalNode extends Node {
   protected String               string      = "";
   protected int                  begin       = 0;
   protected int                  end         = 0;
   protected Map<Character, Node> children    = new HashMap<Character, Node>();
   protected List<LeafNode>       leaves      = new LinkedList<LeafNode>();
   protected Character            leftChar    = null;
   private boolean                leftCharSet = false;

   public InternalNode(String string, int begin, int end) {
      this.string = string;
      this.begin = begin;
      this.end = end;
   }

   public InternalNode() {
      super();
   }

   public boolean isLeaf() {
      return false;
   }

   public void insertNode(int stringIndex, NodeInfo info) {
      int i = 0;
      while (i < length() && info.charAt(i) == this.charAt(i))
         i++;

      info.labelBegin += i;
      if (i < length()) {
         // Split - create an internal node
         InternalNode newNode = new InternalNode(this.string, this.begin,
               this.begin + i);
         this.begin += i;

         parent.addChild(newNode);
         newNode.parent = this.parent;

         newNode.addChild(this);
         this.parent = newNode;

         LeafNode node = new LeafNode(stringIndex, info);
         newNode.addChild(node);
         node.parent = newNode;
      } else {
         // Find the next child node
         Node child = this.getChild(info.charAt(0));

         if (child == null) {
            LeafNode node = new LeafNode(stringIndex, info);
            this.addChild(node);
            node.parent = this;
         } else {
            child.insertNode(stringIndex, info);
         }
      }
   }

   public char charAt(int n) {
      return string.charAt(begin + n);
   }

   void addChild(Node child) {
      children.put(child.charAt(0), child);
   }

   public Node getChild(Character nextChar) {
      return children.get(nextChar);
   }

   public int getCount() {
      return leaves.size();
   }

   public void addLeaf(LeafNode leaf) {
      this.leaves.add(leaf);
   }

   public List<LeafNode> getLeaves() {
      return this.leaves;
   }

   @Override
   public Character getLeftChar() {
      if (!leftCharSet) {
         setLeftChar();
      }
      return leftChar;
   }

   private void setLeftChar() {
      Set<Character> lefts = new HashSet<Character>();
      for (Node child : children.values()) {
         lefts.add(child.getLeftChar());
      }
      if (lefts.size() == 1) {
         leftChar = lefts.iterator().next();
      } else {
         leftChar = null;
      }
      leftCharSet = true;
   }

   public String debugString() {
      StringBuilder sb = new StringBuilder();
      int level = getLevel();
      for (int i = 0; i < level + 1; i++) {
         sb.append('\t');
      }
      String tabs = sb.toString();
      sb = new StringBuilder();
      sb.append("{").append(getLeftChar()).append("} ");
      if (string.equals("")) {
         sb.append("<root>");
      } else {
         sb.append(string.substring(begin, end));
      }
      sb.append(String.format(" (%d)", this.getCount())).append("\n");
      for (Character key : children.keySet()) {
         sb.append(tabs).append("[").append(key).append("]\t")
               .append(children.get(key).debugString());
      }
      return sb.toString();
   }

   @Override
   public List<Node> getLeftDiverseNodes() {
      List<Node> leftDiverseNodes = new LinkedList<Node>();
      if (isLeftDiverse()) {
         leftDiverseNodes.add(this);
         // If a child is left diverse, then all it's parents are left diverse
         for (Node child : this.children.values()) {
            List<Node> childsLeftDiverseNodes = child.getLeftDiverseNodes();
            if (childsLeftDiverseNodes != null) {
               leftDiverseNodes.addAll(childsLeftDiverseNodes);
            }
         }
      }
      return leftDiverseNodes;
   }

   @Override
   public void shiftBegin(int shift) {
      this.begin += shift;
   }

   @Override
   public String getLabel() {
      return string.substring(begin, end);
   }
}
