package suffixtree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InternalNode extends Node {
   protected Map<Character, Node> children    = new HashMap<Character, Node>();
   protected List<LeafNode>       leaves      = new LinkedList<LeafNode>();
   protected Character            leftChar    = null;
   private boolean                leftCharSet = false;

   public InternalNode(String string, int begin, int end) {
      super(string, begin, end);
   }

   public InternalNode() {
      super();
   }

   public void insertNode(LeafNode node) {
      int i = 0;
      while (i < length() && node.charAt(i) == charAt(i))
         i++;

      node.begin += i;
      if (i < length()) {
         InternalNode newNode = new InternalNode(this.string, this.begin,
               this.begin + i);
         this.begin += i;

         parent.addChild(newNode);
         newNode.parent = this.parent;

         newNode.addChild(this);
         this.parent = newNode;

         newNode.addChild(node);
         node.parent = newNode;
      } else {
         Node child = this.getChild(node.charAt(0));

         if (child == null) {
            this.addChild(node);
            node.parent = this;
         } else {
            child.insertNode(node);
         }
      }
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
         for (Node child : children.values()) {
            if (!leftCharSet) {
               leftChar = child.getLeftChar();
               leftCharSet = true;
            } else if (child.getLeftChar() != leftChar) {
               leftChar = null;
            }

         }
      }
      return leftChar;
   }

   public void setLeftChar(Character c) {
      this.leftChar = c;
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
}
