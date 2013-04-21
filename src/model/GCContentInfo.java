package model;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple 4-tuple representing a frame of GC content information.
 * 
 * @author Mitchell Rosen
 * @version 21-Apr-2013
 */
public class GCContentInfo {
   @Getter @Setter protected int start; // inclusive
   @Getter @Setter protected int stop;  // exclusive
   @Getter protected double min;
   @Getter protected double max;

   @Override
   public String toString() {
      return String.format("%d,%d,%.2f%%,%.2f%%", start+1, stop, min*100, max*100);
   }
}
