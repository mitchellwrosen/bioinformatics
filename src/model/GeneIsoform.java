package model;

import java.util.List;

import utils.Folder;
import static utils.Functional.*;

public class GeneIsoform {
   protected Gene gene;
   protected String name;
   protected List<Exon> exons;
   
   public Gene   getGene() { return gene; }
   public String getName() { return name; }
   
   /**
    * Gets the Sequence that this Isoform's coding regions consist of.
    */
   public Sequence getSequence() {
      return foldl(new Folder<Sequence, Exon>() {
         public Sequence execute(Sequence s, Exon e) {
            Sequence exonSequence = gene.getSequence().slice(e.from, e.to);
            return s.concat(exonSequence);
         }
      }, new Sequence(), exons);
   }
   
   public int numExons() {
      return exons.size();
   }
   
   public int numIntrons() {
      return exons.size() - 1;
   }

   public int exonSize() {
      return foldl(new Folder<Integer, Exon>() {
         public Integer execute(Integer n, Exon e) {
            return n + e.size();
         }
      }, 0, exons);
   }
   
   public int intronSize() {
      int size = 0;
      for (int i = 0; i < exons.size()-1; ++i)
         size += exons.get(i+1).from - exons.get(i).to;
      return size;
   }
}
