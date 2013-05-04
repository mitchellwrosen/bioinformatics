package suffixtree;

public class Label {
   private String string;
   private int    begin;
   private int    end;

   public Label(String string, int begin, int end) {
      this.string = string;
      setBegin(begin);
      setEnd(end);
   }

   public char get(int index) {
      return this.string.charAt(begin + index);
   }

   public int getBegin() {
      return begin;
   }

   public void setBegin(int begin) {
      this.begin = begin;
   }

   public void shiftBegin(int shift) {
      this.begin += shift;
   }

   public int getEnd() {
      return end;
   }

   public void setEnd(int end) {
      this.end = end;
   }

   public int getLength() {
      return end - begin;
   }

   public Label split(int at) {
      Label newLabel = new Label(this.string, this.begin, this.begin + at);
      this.begin = at;
      return newLabel;
   }

   @Override
   public String toString() {
      return string.substring(begin, end);
   }

   /**
    * Gets the character to the left.
    * 
    * @return The left character or null if begin is 0.
    */
   public Character getLeftChar() {
      if (begin > 0) {
         return string.charAt(begin - 1);
      } else {
         return null;
      }
   }
}