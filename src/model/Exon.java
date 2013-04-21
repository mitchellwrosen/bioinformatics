package model;

public class Exon {
   public int from; // inclusive
   public int to; // exculsive

   public int size() {
      return to - from;
   }
}
