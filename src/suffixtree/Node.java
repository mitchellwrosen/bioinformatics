/**
 * 
 */
package suffixtree;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Erik Sandberg &lt;esandber@calpoly.edu&gt;
 * 
 */
public class Node {
   private Node                 parent   = null;
   private Label                label    = null;
   private Map<Character, Node> children = new HashMap<Character, Node>();

   public Node() {
   }

   public Node(String string, int begin, int end) {
      this.label = new Label(string, begin, end);
   }

   public Node(Label label) {
      this.label = label;
   }

   public Node getParent() {
      return parent;
   }

   public Label getLabel() {
      return label;
   }

   public void insertNode(Node node) {
      if (label != null && parent != null) {
         String thisString = this.getLabel().toString();
         String otherString = node.getLabel().toString();
         int i = 0;
         while (i < thisString.length()
               && otherString.charAt(i) == thisString.charAt(i)) {
            i++;
         }
         node.label.shiftBegin(i);
         if (i < thisString.length()) {
            Node newNode = new Node(this.label.split(i));
            this.parent.addChild(newNode);
            newNode.addChild(this);
            newNode.addChild(node);
            return;
         }
      }
      Node child = this.getChild(node.getLabel().get(0));
      if (child == null) {
         this.addChild(node);
      } else {
         child.insertNode(node);
      }
   }

   private void addChild(Node child) {
      child.parent = this;
      children.put(child.getLabel().toString().charAt(0), child);
   }

   public Node getChild(Character nextChar) {
      return children.get(nextChar);
   }

   public boolean isLeaf() {
      return children.size() == 0;
   }

   public int getLevel() {
      if (parent == null) {
         return 0;
      } else {
         return 1 + parent.getLevel();
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      int level = getLevel();
      for (int i = 0; i < level; i++) {
         sb.append('\t');
      }
      String tabs = sb.toString();
      sb = new StringBuilder(tabs);
      if (label == null) {
         sb.append("<root>\n");
      } else {
         sb.append(label.toString()).append("\n");
      }
      for (Character key : children.keySet()) {
         sb.append(tabs).append("[").append(key).append("]\n").append(tabs)
               .append(children.get(key).toString());
      }
      return sb.toString();
   }
}
