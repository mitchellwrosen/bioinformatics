package model;

import model.GFFParser.Feature;

public class Exon {
   public int start;
   public int stop;
   protected Feature feature;
   
   public String toGff() {
      return feature.toGff();
   }

   public Exon(int start, int stop, Feature feature) {
      this.start = start;
      this.stop = stop;
   }
   
   public int getStart() { return start; }
   public int getStop()  { return stop; }
   public int size()     { return stop-start; }
}
