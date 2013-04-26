package model;

public class Exon {
   public int start;
   public int stop;
   
   public Exon(int start, int stop) {
      this.start = start;
      this.stop = stop;
   }
   
   public int getStart() { return start; }
   public int getStop()  { return stop; }
   public int size()     { return stop-start; }
}
