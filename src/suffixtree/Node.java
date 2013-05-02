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
   private Node                 parent;
   private Label                label;
   private Map<Character, Node> children;

   Node(Node parent, String string, int begin, int end) {
      this.parent = parent;
      this.parent.addChild(this);
      this.label = new Label(string, begin, end);
      this.children = new HashMap<Character, Node>();
   }

   public Node getParent() {
      return parent;
   }

   public Label getLabel() {
      return label;
   }

   public void addChild(Node child) {
      children.put(child.getLabel().toString().charAt(0), child);
   }

   public Node getChild(Character nextChar) {
      return children.get(nextChar);
   }
}
