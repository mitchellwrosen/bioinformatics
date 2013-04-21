package model;

import java.util.List;

public class GeneIsoform {
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
