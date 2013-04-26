package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A single Gene, represented by one or more isoforms.
 * 
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 */
public class Gene {
   protected List<GeneIsoform> isoforms;
   protected Sequence sequence;

   /**
    * Genes should be constructed with the create method.
    */
   protected Gene() {
      isoforms = new ArrayList<GeneIsoform>();
   }
   
   public static Gene create(GeneIsoform isoform) {
      Gene gene = new Gene();
      gene.addIsoform(isoform);
      return gene;
   }
   
   public void addIsoform(GeneIsoform isoform) {
      isoform.setGene(this);
      isoforms.add(isoform);
   }

   public String getId() {
      return isoforms.get(0).getGeneId();
   }

   public int getStart() {
      return isoforms.get(0).getStart();
   }

   public int getStop() {
      return isoforms.get(0).getStop();
   }

   public int numIsoforms() {
      return isoforms.size();
   }

   public int numExons() {
      int size = 0;
      for (GeneIsoform iso : isoforms)
         size += iso.numExons();
      return size;
   }

   public int numIntrons() {
      int size = 0;
      for (GeneIsoform iso : isoforms)
         size += iso.numIntrons();
      return size;
   }

   /**
    * A Gene spans the extremes of its isoforms.
    */
   public int size() {
      int min = isoforms.get(0).getStart();
      int max = isoforms.get(0).getStop();
      
      for (int i = 1; i < isoforms.size(); ++i) {
         min = Math.min(min, isoforms.get(i).getStart());
         max = Math.max(max,  isoforms.get(i).getStop());
      }
      
      return max - min;
   }

   /**
    * Gets the CDS size of this Gene by including the CDS regions of each
    * isoform, taking care not to double count overlapping regions.
    */
   public int cdsSize() {
      Set<Integer> set = new HashSet<Integer>();
      for (GeneIsoform isoform : isoforms) {
         for (Exon exon : isoform.exons) {
            for (int i = exon.getStart(); i <= exon.getStop(); ++i)
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
      for (GeneIsoform iso : isoforms)
         size += iso.exonSize();
      return size;
   }

   /**
    * Gets the total Intron size of all Isoforms, overlap included.
    */
   public int intronSize() {
      int size = 0;
      for (GeneIsoform iso : isoforms)
         size += iso.intronSize();
      return size;
   }

   public void setSequence(Sequence sequence) {
      this.sequence = sequence;
   }

   public Sequence getSequence() {
      return sequence;
   }

   public void setIsoforms(List<GeneIsoform> isoforms) {
      this.isoforms = isoforms;
   }

   public List<GeneIsoform> getIsoforms() {
      return isoforms;
   }
}
