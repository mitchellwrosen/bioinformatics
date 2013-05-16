package suffixtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafNode extends Node {
   public class NodeInfo {
      private String string;
      public int     stringBegin;
      public int     labelBegin;
      public int     end;

      public NodeInfo(String string, int stringBegin, int labelBegin, int end) {
         this.string = string;
         this.stringBegin = stringBegin;
         this.labelBegin = labelBegin;
         this.end = end;
      }

      public int length() {
         return end - labelBegin;
      }

      public char charAt(int n) {
         return string.charAt(labelBegin + n);
      }

      public String toString() {
         return string.substring(labelBegin, end);
      }
   }

   protected Map<Integer, NodeInfo> nodeInfo = new HashMap<Integer, NodeInfo>();

   public LeafNode(int stringIndex, NodeInfo info) {
      this.nodeInfo.put(stringIndex, info);
   }

   public LeafNode(int stringIndex, String string, int stringBegin, int end) {
      this.nodeInfo.put(stringIndex, new NodeInfo(string, stringBegin,
            stringBegin, end));
   }

   public boolean isLeftDiverse() {
      return false;
   }

   public boolean isLeaf() {
      return true;
   }

   public Integer getStringBegin(int stringIndex) {
      NodeInfo info = nodeInfo.get(stringIndex);
      if (info == null) {
         return null;
      } else {
         return info.stringBegin;
      }
   }

   public Character getLeftChar(int stringIndex) {
      Integer stringBegin = getStringBegin(stringIndex);
      if (stringBegin == 0 || stringBegin == null) {
         return null;
      } else {
         // nodeInfo.get(stringIndex) will not be null or getStringBegin() would
         // have been null.
         return nodeInfo.get(stringIndex).charAt(stringBegin - 1);
      }
   }

   public void insertNode(int stringIndex, NodeInfo info) {
      this.nodeInfo.put(stringIndex, info);
   }

   /*
    * public void insertNode(LeafNode node) {
    * int i = 0;
    * while (i < length() && node.charAt(i) == charAt(i))
    * i++;
    * 
    * node.shiftBegin(i);
    * if (i < length()) {
    * InternalNode newNode = new InternalNode(this.string, this.begin,
    * this.begin + i);
    * this.begin += i;
    * 
    * parent.addChild(newNode);
    * newNode.parent = this.parent;
    * 
    * newNode.addChild(this);
    * this.parent = newNode;
    * 
    * newNode.addChild(node);
    * node.parent = newNode;
    * } else {
    * // This shouldn't happen.
    * // TODO: This might happen when we make this generalized.
    * throw new RuntimeException();
    * }
    * }
    */

   public String debugString() {
      StringBuilder sb = new StringBuilder();
      int level = getLevel();
      for (int i = 0; i < level; i++) {
         sb.append('\t');
      }
      // String tabs = sb.toString();
      sb = new StringBuilder();
      sb.append("{").append(getLeftChar()).append("} ");
      if (string.equals("")) {
         sb.append("<root>\n");
      } else {
         sb.append(string.substring(begin, end)).append(" * ").append("\n");
      }
      return sb.toString();
   }

   @Override
   public List<Node> getLeftDiverseNodes() {
      return null;
   }

   @Override
   public void shiftBegin(int shift) {
      // TODO Auto-generated method stub

   }

   @Override
   public char charAt(int n) {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public String getLabel() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Character getLeftChar() {
      // TODO Auto-generated method stub
      return null;
   }
}
