package model.suffixtree;

import java.util.HashSet;
import java.util.Set;

public class Node {
   public Set<Node> children = new HashSet<Node>();
   public Node parent;
   public String incomingSuffix = "";
   public int id;
   public static int c;
   int nodeDepth = 0;
   public Node(int id) {
      this.id = id;
   }

   public Node(Node parent, String incomingSuffix, int id, int nodeDepth) {
      this.parent = parent;
      this.incomingSuffix = incomingSuffix;
      this.id = id;
      this.nodeDepth = nodeDepth;
   }
}