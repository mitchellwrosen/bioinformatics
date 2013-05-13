package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * A Sequence of Nucleotides.
 * 
 * @author Mitchell Rosen
 */
public class Sequence {
   protected List<Nucleotide> nucleotides;

   public Sequence() {
      this.nucleotides = new ArrayList<Nucleotide>();
   }
   
   public Sequence(List<Nucleotide> nucleotides) {
      this.nucleotides = nucleotides;
   }

   public Sequence(String filename) throws IOException, IllegalArgumentException {
      this.nucleotides = new ArrayList<Nucleotide>();

      BufferedReader r = new BufferedReader(new FileReader(filename));

      // First line possibly begins with '>'
      String line = r.readLine().trim();
      if (line.startsWith(">"))
         line = r.readLine().trim();

      do {
         line = line.trim();
         for (int i = 0; i < line.length(); ++i)
            nucleotides.add(Nucleotide.fromChar(line.charAt(i)));
      } while ((line = r.readLine()) != null);

      r.close();
   }
   
   /**
    * Get the number of nucleotides (including unknowns) in this sequence.
    * 
    * @return the size of this sequence.
    */
   public int size() {
      return nucleotides.size();
   }
   
   /**
    * Slices this sequence and returns a new sequence of range [from, to)
    */
   public Sequence slice(int from, int to) {
      return new Sequence(nucleotides.subList(from, to));
   }
   
   /**
    * Concatenates a sequence onto this one, returns a new Sequence.
    */
   public Sequence concat(Sequence s) {
      List<Nucleotide> nucleotides = new ArrayList<Nucleotide>(this.nucleotides);
      nucleotides.addAll(s.getNucleotides());
      return new Sequence(nucleotides);
   }

   /**
    * Gets the GC-content as a histogram, where each entry in the histogram is
    * the GC-content of a slice of size |windowSize|, shifted by some multiple
    * of |shiftLen|.
    * 
    * @param windowSize
    *           the window size
    * @param shiftLen
    *           the shift length
    * 
    * @return the GC-content histogram
    */
   public GCContentInfo[] gcContentHistogram(int windowSize, int shiftLen) {
      int histogramLen = (int) Math
            .ceil((double) nucleotides.size() / shiftLen);
      GCContentInfo[] gc = new GCContentInfo[histogramLen];
      for (int i = 0; i < histogramLen; ++i) {
         gc[i] = new GCContentInfo();
         gc[i].setStart(i * shiftLen);
         gc[i].setStop(Math.min(nucleotides.size(), i * shiftLen + windowSize));
         Sequence slice = slice(gc[i].getStart(), gc[i].getStop());
         gc[i].min = slice.gcContentMin();
         gc[i].max = slice.gcContentMax();
      }

      return gc;
   }

   /**
    * Gets the GC-content Min of this Sequence.
    * 
    * @return the GC-content Min
    */
   public double gcContentMin() {
      int numGC = 0;
      for (Nucleotide n : nucleotides) {
         if (n == Nucleotide.CYTOSINE || n == Nucleotide.GUANINE) {
            numGC++;
         }
      }

      return numGC / (double) nucleotides.size();
   }

   /**
    * Gets the GC-content Max of this Sequence.
    * 
    * @return the GC-content Max
    */
   public double gcContentMax() {
      int numGC = 0;
      for (Nucleotide n : nucleotides) {
         if (n == Nucleotide.CYTOSINE || n == Nucleotide.GUANINE
               || n == Nucleotide.UNKNOWN) {
            numGC++;
         }
      }

      return numGC / (double) nucleotides.size();
   }

   /**
    * @return The guanine content of this sequence, unknowns treated as their
    *         own class of acids.
    */
   public double gContent() {
      int gCount = 0;
      for (Nucleotide n : nucleotides) {
         if (n.equals(Nucleotide.GUANINE)) {
            gCount++;
         }
      }
      return gCount / (double) nucleotides.size();
   }

   /**
    * @return The cytosine content of this sequence, unknowns treated as their
    *         own class of acids.
    */
   public double cContent() {
      int cCount = 0;
      for (Nucleotide n : nucleotides) {
         if (n.equals(Nucleotide.CYTOSINE)) {
            cCount++;
         }
      }
      return cCount / (double) nucleotides.size();
   }

   /**
    * @return The adenine content of this sequence, unknowns treated as their
    *         own class of acids.
    */
   public double aContent() {
      int aCount = 0;
      for (Nucleotide n : nucleotides) {
         if (n.equals(Nucleotide.ADENINE)) {
            aCount++;
         }
      }
      return aCount / (double) nucleotides.size();
   }

   /**
    * @return The thymine content of this sequence, unknowns treated as their
    *         own class of acids.
    */
   public double tContent() {
      int tCount = 0;
      for (Nucleotide n : nucleotides) {
         if (n.equals(Nucleotide.THYMINE)) {
            tCount++;
         }
      }
      return tCount / (double) nucleotides.size();
   }

   /**
    * @return The unknown content of this sequence.
    */
   public double unknownContent() {
      int uCount = 0;
      for (Nucleotide n : nucleotides) {
         if (n.equals(Nucleotide.UNKNOWN)) {
            uCount++;
         }
      }
      return uCount / (double) nucleotides.size();
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (Nucleotide n : nucleotides)
         sb.append(n.toChar());
      return sb.toString();
   }
   
   public String toProteinString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < nucleotides.size()-2; i+=3) {
         Sequence codon = slice(i, i+3);
         sb.append(AminoAcid.fromSequence(codon));
      }
      return sb.toString();
   }

   public List<Nucleotide> getNucleotides() {
      return nucleotides;
   }

   public void setNucleotides(List<Nucleotide> nucleotides) {
      this.nucleotides = nucleotides;
   }

   /**
    * @return Returns the reverse complement of this sequence as a sequence.
    */
   public Sequence reverseComplement() {
      List<Nucleotide> reversedList = new ArrayList<Nucleotide>();

      ListIterator<Nucleotide> iter = nucleotides.listIterator(nucleotides
         .size());

      while (iter.hasPrevious()) {
         Nucleotide n = iter.previous();
         reversedList.add(n.complement());
      }
      return new Sequence(reversedList);
   }
}
