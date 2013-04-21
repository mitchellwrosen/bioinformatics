package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.Folder;
import static utils.Functional.*;

/**
 * A single Gene.
 * 
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 */
public class Gene {
   protected String name;
   protected Sequence sequence;
   protected int from; // inclusive
   protected int to; // exclusive
   protected boolean reverse; // Relative to |sequence|
   protected List<GeneIsoform> isoforms;
   
   public Gene() {
      // TODO
   }
   
   // Getters & Setters
   public String            getName()     { return name; }
   public Sequence          getSequence() { return sequence; }
   public int               getFrom()     { return from; }
   public int               getTo()       { return to; }
   public List<GeneIsoform> getIsoforms() { return isoforms; }

   public int numIsoforms() {
      return isoforms.size();
   }
   
   public int numExons() {
      return foldl(new Folder<Integer, GeneIsoform>() {
         public Integer execute(Integer n, GeneIsoform iso) {
            return n + iso.numExons();
         }
      }, 0, isoforms);
   }
   
   public int numIntrons() {
      return foldl(new Folder<Integer, GeneIsoform>() {
         public Integer execute(Integer n, GeneIsoform iso) {
            return n + iso.numIntrons();
         }
      }, 0, isoforms);
   }

   public int size() {
      return to - from;
   }

   // Gets the CDS size of this Gene by including the CDS regions of each
   // isoform, taking care not to double count overlapping regions.
   public int cdsSize() {
      Set<Integer> set = new HashSet<Integer>();
      for (GeneIsoform isoform : isoforms) {
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
      return foldl(new Folder<Integer, GeneIsoform>() {
         public Integer execute(Integer n, GeneIsoform iso) {
            return n + iso.exonSize();
         }
      }, 0, isoforms);
   }
   
   /**
    * Gets the total Intron size of all Isoforms, overlap included.
    */
   public int intronSize() {
      return foldl(new Folder<Integer, GeneIsoform>() {
         public Integer execute(Integer n, GeneIsoform iso) {
            return n + iso.intronSize();
         };
      }, 0, isoforms);
   }
}
