package suffixtree;

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

   public int getLabelLength() {
      return end - labelBegin;
   }

   public int getStringLength() {
      return end - stringBegin;
   }

   public char charAt(int n) {
      return string.charAt(labelBegin + n);
   }
   
   public Character getLeftChar() {
      if (stringBegin == 0) {
         return null;
      } else {
         return string.charAt(stringBegin - 1);
      }
   }
   
   @Override
   public String toString() {
      return string.substring(labelBegin, end);
   }
   
   public String getString() {
      return this.string;
   }
}