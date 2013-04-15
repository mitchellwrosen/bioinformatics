package model;

public class GCContentInfo {
   // Note that from is indexed in array format. Must convert any time a user
   // sees the value.
   public int from;
   // to is exclusively indexed in array format, which is fine because it is the
   // same as inclusive when indexing from 1.
   public int to;
   public double min;
   public double max;

   /**
    * Gives a 4-tuple description of this {@link GCContentInfo} as a String.
    * 
    * @return the description of this {@link GCContentInfo}.
    */
   @Override
   public String toString() {
      return String.format("%d,%d,%.2f%%,%.2f%%", from + 1, to, min * 100, max * 100);
   }
}
