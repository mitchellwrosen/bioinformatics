package model;

/**
 * A simple 4-tuple representing a frame of GC content information.
 * 
 * @author Mitchell Rosen
 * @version 21-Apr-2013
 */
public class GCContentInfo {
   protected int start; // inclusive

   protected int stop; // exclusive
   protected double min;
   protected double max;

   @Override
   public String toString() {
      return String.format("%d,%d,%.2f%%,%.2f%%", start + 1, stop, min * 100,
            max * 100);
   }

   public int getStart() {
      return start;
   }

   public void setStart(int start) {
      this.start = start;
   }

   public int getStop() {
      return stop;
   }

   public void setStop(int stop) {
      this.stop = stop;
   }

   public double getMin() {
      return min;
   }

   public void setMin(double min) {
      this.min = min;
   }

   public double getMax() {
      return max;
   }

   public void setMax(double max) {
      this.max = max;
   }
}
