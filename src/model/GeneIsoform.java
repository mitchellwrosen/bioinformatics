package model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GeneIsoform extends GffFeature {
   protected Gene gene;
   protected List<Exon> exons;

   public GeneIsoform(String chromosome, String source, String feature, int start, int stop,
         String score, boolean reverse, String frame, Map<String, String> attributes) {
      super(chromosome, source, feature, start, stop, score, reverse, frame, attributes);
      
      if (reverse)
         this.start -= 3;
      else
         this.stop += 3;
   }
   
   public Gene getGene() { return gene; }
   public List<Exon> getExons() { return exons; }
   public void setGene(Gene gene) { this.gene = gene; }
   public String getGeneId() { return attributes.get("gene_id"); }
   public String getIsoformName() { return attributes.get("transcript_id"); }
   
   /**
    * Gets the Sequence that this Isoform's coding regions consist of.
    */
   public Sequence getSequence() {
      Sequence s = new Sequence();
      for (Exon e : exons) {
         Sequence exonSequence = gene.getSequence().slice(e.getStart(), e.getStop());
         s = s.concat(exonSequence);
      }
      return s;
   }
   
   public void setExons(List<Exon> exons) { 
      this.exons = exons; 
   }
   
   public int numExons() {
      return exons.size();
   }
   
   public int numIntrons() {
      return exons.size() - 1;
   }

   public int exonSize() {
      int size = 0;
      for (Exon e : exons)
         size += e.size();
      return size;
   }
   
   public int intronSize() {
      // Sort here so as to not create a dependency between this function and, 
      // say, setExons().
      Collections.sort(exons, new Comparator<Exon>() {
         public int compare(Exon e1, Exon e2) {
            return e1.getStart() - e2.getStart();
         }
      });
      
      int size = 0;
      for (int i = 0; i < exons.size()-1; ++i)
         size += exons.get(i+1).getStart() - exons.get(i).getStop();
      return size;
   }
}
