package suffixtree;

public class LeafNode extends Node {
   protected int start;

   public LeafNode(String string, int start, int end) {
      super(string, start, end);
      this.start = start;
   }

   public int getStart() {
      return start;
   }

   public Character getLeftChar() {
      if (start == 0) {
         return null;
      } else {
         return string.charAt(start - 1);
      }
   }

   public String debugString() {
      StringBuilder sb = new StringBuilder();
      int level = getLevel();
      for (int i = 0; i < level; i++) {
         sb.append('\t');
      }
      String tabs = sb.toString();
      sb = new StringBuilder(tabs);
      if (string.equals("")) {
         sb.append("<root>\n");
      } else {
         sb.append(string.substring(begin, end)).append(" * ").append("\n");
      }
      return sb.toString();
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
         // This shouldn't happen.
         // TODO: This might happen when we make this generalized.
         throw new RuntimeException();
      }
   }
}
