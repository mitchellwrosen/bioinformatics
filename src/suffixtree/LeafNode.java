package suffixtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafNode extends Node {
   protected Map<Integer, NodeInfo> nodeInfo = new HashMap<Integer, NodeInfo>();

   public LeafNode(int stringIndex, NodeInfo info) {
      this.stringLength = info.getStringLength();
      this.addNodeInfo(stringIndex, info);
   }

   public LeafNode(int stringIndex, String string, int stringBegin, int end) {
      this.stringLength = end - stringBegin;
      this.addNodeInfo(stringIndex, new NodeInfo(string, stringBegin,
            stringBegin, end));
   }

   @Override
   public Character charAt(int n) {
      NodeInfo info = nodeInfo.values().iterator().next();
      if (info != null) {
         return info.charAt(n);
      } else {
         return null;
      }
   }

   @Override
   public String debugString() {
      StringBuilder sb = new StringBuilder();
      int level = getLevel();
      for (int i = 0; i < level; i++) {
         sb.append('\t');
      }
      // String tabs = sb.toString();
      sb = new StringBuilder();
      sb.append("{");
      for (Integer i : nodeInfo.keySet()) {
         sb.append(" (").append(i).append(", ").append(getLeftChar(i)).append(")");
      }
      sb.append(" } ");
      if (getParent() == null) {
         sb.append("<root>\n");
      } else {
         for (NodeInfo info : nodeInfo.values()) {
            sb.append(info.toString()).append(" * ");
         }
         sb.append("\n");
      }
      return sb.toString();
   }

   @Override
   public String getLabel() {
      NodeInfo info = nodeInfo.values().iterator().next();
      if (info != null) {
         return info.toString();
      } else {
         return null;
      }
   }

   @Override
   public Character getLeftChar(int stringIndex) {
      NodeInfo info = nodeInfo.get(stringIndex);
      if (info == null) {
         return null;
      } else {
         return info.getLeftChar();
      }
   }

   @Override
   public List<Node> getFullyDiverseNodes(int stringIndex) {
      return null;
   }

   public Integer getStringBegin(int stringIndex) {
      NodeInfo info = nodeInfo.get(stringIndex);
      if (info == null) {
         return null;
      } else {
         return info.stringBegin;
      }
   }

   @Override
   public LeafNode insertNode(int stringIndex, NodeInfo info) {
      LeafNode leaf;
      
      int i = 0;
      while (i < getLabelLength() && info.charAt(i) == this.charAt(i))
         i++;

      if (i < getLabelLength()) {
         // Split - create an internal node
         InternalNode newNode = new InternalNode(info.getString(), info.labelBegin,
               info.labelBegin + i);
         this.shiftBegin(i);
         info.labelBegin += i;

         getParent().addChild(newNode);
         newNode.setParent(this.getParent());

         newNode.addChild(this);
         this.setParent(newNode);

         leaf = new LeafNode(stringIndex, info);
         newNode.addChild(leaf);
         leaf.setParent(newNode);
      } else {
         this.addNodeInfo(stringIndex, info);
         leaf = this;
      }
      return leaf;
   }
   
   protected void addNodeInfo(int stringIndex, NodeInfo info) {
      this.nodeInfo.put(stringIndex, info);
      this.stringIndicesSeen.add(stringIndex);
   }

   @Override
   public boolean isLeaf() {
      return true;
   }

   @Override
   public boolean isLeftDiverse(int stringIndex) {
      return false;
   }

   @Override
   public int getLabelLength() {
      NodeInfo info = nodeInfo.values().iterator().next();
      if (info != null) {
         return info.getLabelLength();
      } else {
         return 0;
      }
   }

   @Override
   public void shiftBegin(int shift) {
      for (NodeInfo info : nodeInfo.values()) {
         info.labelBegin += shift;
      }
   }

   @Override
   public int getStringLength() {
      return stringLength;
   }
}
