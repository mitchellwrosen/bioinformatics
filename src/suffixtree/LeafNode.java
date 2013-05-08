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
}
