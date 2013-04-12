package model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Sequence of Nucleotides.
 * @author Mitchell Rosen
 */
public class Sequence {
   protected List<Nucleotide> nucleotides;
   
   public Sequence(List<Nucleotide> nucleotides) { this.nucleotides = nucleotides; }
   
   public Sequence(String filename) throws IOException {
      BufferedReader r = new BufferedReader(new FileReader(filename));
      
      nucleotides = new ArrayList<Nucleotide>();
      
      // First line possibly begins with '>'
      String line = r.readLine();
      if (line.startsWith(">"))
         line = r.readLine();
      
      do {
         for (int i = 0; i < line.length(); ++i) 
            nucleotides.add(Nucleotide.fromChar(line.charAt(i)));
      } while ((line = r.readLine()) != null);
      
      r.close();
   }
  
   // Slices this sequence and returns a new sequence of range [from, to)
   public Sequence slice(int from, int to) {
      return new Sequence(nucleotides.subList(from, to));
   }
   
   /**
    *  Gets the GC-content as a histogram, where each entry in the histogram is
    *  the GC-content of a slice of size |windowSize|, shifted by some multiple of
    *  |shiftLen|.
    *  
    *  @param  windowSize  the window size
    *  @param  shiftLen    the shift length
    *  
    *  @return    the GC-content histogram
    */
   public GCContentInfo[] gcContentHistogram(int windowSize, int shiftLen) {
      int histogramLen = (int)Math.ceil((double)nucleotides.size() / shiftLen);
      GCContentInfo[] gc = new GCContentInfo[histogramLen];
      for (int i = 0; i < histogramLen; ++i) {
    	  gc[i].from = i*shiftLen;
    	  gc[i].to = Math.min(nucleotides.size(), i*shiftLen + windowSize);
    	  Sequence slice = slice(gc[i].from, gc[i].to);
          gc[i].min = slice.gcContentMin();
          gc[i].max = slice.gcContentMax();
      }
      
      return gc;
   }
   
   /**
    * Gets the GC-content Min of this Sequence.
    * 
    * @return  the GC-content Min
    */
   public double gcContentMin() {
      double numGC = 0;
      for (Nucleotide n : nucleotides) {
         if (n == Nucleotide.CYTOSINE || 
               n == Nucleotide.GUANINE) {
            numGC++;
         }
      }
      
      return numGC / nucleotides.size();
   }
   
   /**
    * Gets the GC-content Max of this Sequence.
    * 
    * @return  the GC-content Max
    */
   public double gcContentMax() {
      double numGC = 0;
      for (Nucleotide n : nucleotides) {
         if (n == Nucleotide.CYTOSINE || 
               n == Nucleotide.GUANINE ||
               n == Nucleotide.UNKNOWN) {
            numGC++;
         }
      }
      
      return numGC / nucleotides.size();
   }
}
