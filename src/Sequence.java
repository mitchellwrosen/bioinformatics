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
            nucleotides.add(new Nucleotide(line.charAt(i)));
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
   public double[] gcContentHistogram(int windowSize, int shiftLen) {
      if ((nucleotides.size() - windowSize) % shiftLen != 0) {
         throw new IllegalArgumentException(String.format(
               "Window size + n * shift length must equal %d for some n", 
               nucleotides.size()));
      }
      
      int histogramLen = (nucleotides.size() - windowSize) / shiftLen;
      double[] gc = new double[histogramLen];
      for (int i = 0; i < histogramLen; ++i)
         gc[i] = slice(i*shiftLen, i*shiftLen + windowSize).gcContent();
      
      return gc;
   }
   
   /**
    * Gets the GC-content of this Sequence.
    * 
    * @return  the GC-content
    */
   public double gcContent() {
      double numGC = 0;
      for (Nucleotide n : nucleotides) {
         if (n.getType() == Nucleotide.Type.CYTOSINE || 
               n.getType() == Nucleotide.Type.GUANINE) {
            numGC++;
         }
      }
      
      return numGC / nucleotides.size();
   }
}
