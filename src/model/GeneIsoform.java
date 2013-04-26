package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeneIsoform {
   protected String chromosome;
   protected int start;
   protected int stop;
   protected boolean reverse;
   protected String geneId;
   protected String transcriptId;
   
   protected Gene gene;
   protected List<Exon> exons;

   public GeneIsoform(String chromosome, int start, int stop, boolean reverse, String geneId,
         String transcriptId) {
      this.chromosome = chromosome;
      this.start = start;
      this.stop = stop;
      this.reverse = reverse;
      this.geneId = geneId;
      this.transcriptId = transcriptId;
      
      if (reverse)
         this.start -= 3;
      else
         this.stop += 3;
      
      this.exons = new ArrayList<Exon>();
   }
   
   public String getChromosome()   { return chromosome; }
   public int getStart()           { return start; }
   public int getStop()            { return stop; }
   public boolean isReverse()      { return reverse; }
   public String getGeneId()       { return geneId; }
   public String getTranscriptId() { return transcriptId; }
   public Gene getGene()           { return gene; }
   public List<Exon> getExons()    { return exons; }
   public int size()               { return stop-start; }
   
   public void setGene(Gene gene)         { this.gene = gene; }

   public void addExon(Exon exon) {
      exons.add(exon);
   }
   
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
