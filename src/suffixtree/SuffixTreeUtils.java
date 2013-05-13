package suffixtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Gene;
import model.GeneIsoform;
import model.Nucleotide;
import model.Sequence;

/**
 * Class with several calculations needed for processing the information stored
 * in the suffix tree.
 * 
 * @author Cameron Stearns
 * 
 */
public class SuffixTreeUtils {
   private Sequence sequence;

   private double aContent;
   private double tContent;
   private double cContent;
   private double gContent;
   private double unknownContent;

   // List of positions of mRNA strands.
   private Integer[] mRNAStartsPositive;
   private Integer[] mRNAStartsNegative;
   
   public SuffixTreeUtils(Sequence sequence, List<Gene> genes) {
      this.sequence = sequence;
      this.aContent = sequence.aContent();
      this.tContent = sequence.tContent();
      this.cContent = sequence.cContent();
      this.gContent = sequence.gContent();
      this.unknownContent = sequence.unknownContent();
      
      List<Integer> positiveStarts = new ArrayList<Integer>();
      List<Integer> negativeStarts = new ArrayList<Integer>();
      for(Gene gene : genes) {
         for(GeneIsoform isoform : gene.getIsoforms()) {
            if(isoform.isReverse()) {
               negativeStarts.add(isoform.getStop());
            } else {
               positiveStarts.add(isoform.getStart());
            }
         }
      }
      this.mRNAStartsNegative = negativeStarts.toArray(new Integer[]{});
      this.mRNAStartsPositive = positiveStarts.toArray(new Integer[]{});

      Arrays.sort(this.mRNAStartsNegative);
      Arrays.sort(this.mRNAStartsPositive);
   }

   /**
    * Calculates the expected fold expression for a given string in the entire
    * sequence.
    */
   public double findExpectedFoldExpression(String searchString) {
      double foldExpression = 1;
      for (int i = 0; i < searchString.length(); i++) {
         char nextChar = searchString.charAt(i);
         if (nextChar == Nucleotide.ADENINE.toChar()) {
            foldExpression *= aContent;
         } else if (nextChar == Nucleotide.CYTOSINE.toChar()) {
            foldExpression *= cContent;
         } else if (nextChar == Nucleotide.GUANINE.toChar()) {
            foldExpression *= gContent;
         } else if (nextChar == Nucleotide.THYMINE.toChar()) {
            foldExpression *= tContent;
         } else {
            foldExpression *= unknownContent;
         }
      }
      return foldExpression * sequence.size();
   }

   /**
    * Calculates the average distance to the next positive mRNA strand.
    * 
    * There are opportunities for optimization here, if interested. I doubt it will be
    * the most time consuming portion of the program.
    * @param indices
    *           a list of indices to compare to the start indices of the strand.
    * @return Returns a single average for distances from each of the given
    *         indices to the next index in the list of (positive) mRNA starts.
    */
   public double averageDistanceToNextPositiveMRNA(List<Integer> indices) {
      double average = 0;
      for(Integer index : indices) {
         int distance = -1;
         for(Integer mRNAStart : mRNAStartsPositive) { 
            if(index < mRNAStart) {
               distance = mRNAStart - index;
               break;
            }
         }
         // If no "next" mRNA is found, count from end of strand.
         if(distance == -1) {
            distance = sequence.size() - index;
         }
         average += distance;
      }
      return average / indices.size();
   }

   /**
    * Calculates the average distance to the next negative mRNA strand.
    * 
    * There are opportunities for optimization here, if interested. I doubt it will be
    * the most time consuming portion of the program.
    * @param indices
    *           a list of indices to compare to the start indices of the strand.
    * @return Returns a single average for distances from each of the given
    *         indices to the next index in the list of (negative) mRNA starts.
    */
   public double averageDistanceToNextNegativeMRNA(List<Integer> indices) {
      double average = 0;
      for(Integer index : indices) {
         int distance = -1;
         for(int i = mRNAStartsNegative.length - 1; i >= 0; i--) { 
            if(index > mRNAStartsNegative[i]) {
               distance = index - mRNAStartsNegative[i];
               break;
            }
         }
         // If no "next" mRNA is found, count from end of strand.
         if(distance == -1) {
            distance = index;
         }
         average += distance;
      }
      return average / indices.size();
   }

   /**
    * Reverse complements a String, rather than a sequence.
    * 
    * @param forwardStrand
    *           The String to reverse complement.
    * @return The reverse complement of the given string.
    */
   public static String reverseComplement(String forwardStrand) {
      String reverseComplementString = "";
      for (int i = forwardStrand.length() - 1; i >= 0; i--) {
         char nextChar = forwardStrand.charAt(i);
         if (nextChar == Nucleotide.ADENINE.toChar()) {
            reverseComplementString += Nucleotide.ADENINE.complement().toChar();
         } else if (nextChar == Nucleotide.CYTOSINE.toChar()) {
            reverseComplementString += Nucleotide.CYTOSINE.complement().toChar();
         } else if (nextChar == Nucleotide.GUANINE.toChar()) {
            reverseComplementString += Nucleotide.GUANINE.complement().toChar();
         } else if (nextChar == Nucleotide.THYMINE.toChar()) {
            reverseComplementString += Nucleotide.THYMINE.complement().toChar();
         } else {
            // Directly just copy the current character if we do not know a
            // reverse complement.
            reverseComplementString += nextChar;
         }
      }
      return reverseComplementString;
   }
}
