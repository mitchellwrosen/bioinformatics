package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Gene.Isoform.Exon;

/**
 * A single Gene.
 * 
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 */
public class Gene {
   protected class Isoform {
      protected class Exon {
         public int from; // inclusive
         public int to; // exculsive

         public int size() {
            return to - from;
         }
      }
      
      protected String name;
      protected List<Exon> exons;
      
      public int numExons() {
         return exons.size();
      }
      
      public int numIntrons() {
         return exons.size() - 1;
      }

      public int exonSize() {
         int size = 0;
         for (Exon exon : exons)
            size += exon.size();
         return size;
      }
      
      public int intronSize() {
         int size = 0;
         for (int i = 0; i < exons.size()-1; ++i)
            size += exons.get(i+1).from - exons.get(i).to;
         return size;
      }
   }

   protected String name;
   protected Sequence sequence;
   protected int from; // inclusive
   protected int to; // exclusive
   protected boolean reverse; // Relative to |sequence|
   protected List<Isoform> isoforms;
   
   public Gene() {
      // TODO
   }
   
   // Getters & Setters
   public String   getName()     { return name; }
   public Sequence getSequence() { return sequence; }
   public int      getFrom()     { return from; }
   public int      getTo()       { return to; }

   public int numIsoforms() {
      return isoforms.size();
   }
   
   public int numExons() {
      int size = 0;
      for (Isoform i : isoforms)
         size += i.exonSize();
      return size;
   }
   
   public int numIntrons() {
      int size = 0;
      for (Isoform i : isoforms)
         size += i.intronSize();
      return size;
   }

   public int size() {
      return to - from;
   }

   // Gets the CDS size of this Gene by including the CDS regions of each
   // isoform, taking care not to double count overlapping regions.
   public int cdsSize() {
      Set<Integer> set = new HashSet<Integer>();
      for (Isoform isoform : isoforms) {
         for (Exon exon : isoform.exons) {
            for (int i = exon.from; i <= exon.to; ++i)
               set.add(i);
         }
      }
      return set.size();
   }

   /**
    * Gets the total Exon size of all Isoforms, overlap included.
    */
   public int exonSize() {
      int size = 0;
      for (Isoform isoform : isoforms)
         size += isoform.exonSize();
      return size;
   }
   
   /**
    * Gets the total Intron size of all Isoforms, overlap included.
    */
   public int intronSize() {
      int size = 0;
      for (Isoform isoform : isoforms)
         size += isoform.intronSize();
      return size;
   }
}
