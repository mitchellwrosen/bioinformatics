package suffixtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import model.Gene;
import model.GeneIsoform;
import model.Nucleotide;
import model.Sequence;
import suffixtree.SuffixTree.PalindromeEntry;
import suffixtree.SuffixTree.StartEntry;

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

   // List of positions of mRNA strands. Includes EOF as a start.
   private Integer[] mRNAStartsPositive;
   /*
    * List of positions of mRNA strands on reverse strand. Includes EOF as
    * a start.
    */
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
      for (Gene gene : genes) {
         for (GeneIsoform isoform : gene.getIsoforms()) {
            if (isoform.isReverse()) {
               negativeStarts.add(isoform.getStop());
            } else {
               positiveStarts.add(isoform.getStart());
            }
         }
      }
      // Add the beginning and end of the file as start and end positions.
      negativeStarts.add(0);
      positiveStarts.add(sequence.size() - 1);
      this.mRNAStartsNegative = negativeStarts.toArray(new Integer[] {});
      this.mRNAStartsPositive = positiveStarts.toArray(new Integer[] {});

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
    * There are opportunities for optimization here, if interested. I doubt it
    * will be
    * the most time consuming portion of the program.
    * 
    * @param starts
    *           a list of indices to compare to the start indices of the strand.
    * @return Returns a single average for distances from each of the given
    *         indices to the next index in the list of (positive) mRNA starts.
    */
   public double averageDistanceToNextPositiveMRNA(List<StartEntry> starts) {
      double average = 0;
      for (StartEntry entry : starts) {
         int distance = -1;
         for (Integer mRNAStart : mRNAStartsPositive) {
            if (entry.start < mRNAStart) {
               distance = mRNAStart - entry.start;
               break;
            }
         }
         // If no "next" mRNA is found, count from end of strand.
         if (distance == -1) {
            distance = sequence.size() - entry.start;
         }
         average += distance;
      }
      return average / starts.size();
   }

   /**
    * Calculates the average distance to the next negative mRNA strand.
    * 
    * There are opportunities for optimization here, if interested. I doubt it
    * will be
    * the most time consuming portion of the program.
    * 
    * @param startEntries
    *           a list of start entries to compare to the start indices of the
    *           strand.
    * @return Returns a single average for distances from each of the given
    *         indices to the next index in the list of (negative) mRNA starts.
    */
   public double
         averageDistanceToNextNegativeMRNA(List<StartEntry> startEntries) {
      double average = 0;
      for (StartEntry entry : startEntries) {
         int distance = -1;
         for (int i = mRNAStartsNegative.length - 1; i >= 0; i--) {
            if (entry.start > mRNAStartsNegative[i]) {
               distance = entry.start - mRNAStartsNegative[i];
               break;
            }
         }
         // If no "next" mRNA is found, count from end of strand.
         if (distance == -1) {
            distance = entry.start;
         }
         average += distance;
      }
      return average / startEntries.size();
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
            reverseComplementString +=
                  Nucleotide.CYTOSINE.complement().toChar();
         } else if (nextChar == Nucleotide.GUANINE.toChar()) {
            reverseComplementString += Nucleotide.GUANINE.complement().toChar();
         } else if (nextChar == Nucleotide.THYMINE.toChar()) {
            reverseComplementString += Nucleotide.THYMINE.complement().toChar();
         } else {
            // Directly just copy the current character if we do not know a
            // reverse complement for this character.
            reverseComplementString += nextChar;
         }
      }
      return reverseComplementString;
   }

   /**
    * Complements a String, and replaces T's with U's.
    * 
    * @param forwardStrand
    *           The DNA to convert.
    * @return The MRNA produced by this DNA strand
    */
   public static String toMRNAString(String forwardStrand) {
      String mRNA = "";
      for (int i = 0; i < forwardStrand.length(); i++) {
         char nextChar = forwardStrand.charAt(i);
         if (nextChar == Nucleotide.ADENINE.toChar()) {
            mRNA += 'U';
         } else if (nextChar == Nucleotide.CYTOSINE.toChar()) {
            mRNA += Nucleotide.CYTOSINE.complement().toChar();
         } else if (nextChar == Nucleotide.GUANINE.toChar()) {
            mRNA += Nucleotide.GUANINE.complement().toChar();
         } else if (nextChar == Nucleotide.THYMINE.toChar()) {
            mRNA += Nucleotide.THYMINE.complement().toChar();
         } else {
            // Directly just copy the current character if we do not know a
            // reverse complement for this character.
            mRNA += nextChar;
         }
      }
      return mRNA;
   }

   /**
    * reverseComplements an mrna
    * 
    * @param forwardStrand
    *           The mRNA to convert.
    * @return The MRNA reverse complement, for palindrome detection.
    */
   public static String reverseComplementMRNAString(String forwardStrand) {
      String mRNA = "";
      for (int i = forwardStrand.length() - 1; i >= 0; i--) {
         char nextChar = forwardStrand.charAt(i);
         if (nextChar == Nucleotide.ADENINE.toChar()) {
            mRNA += 'U';
         } else if (nextChar == Nucleotide.CYTOSINE.toChar()) {
            mRNA += Nucleotide.CYTOSINE.complement().toChar();
         } else if (nextChar == Nucleotide.GUANINE.toChar()) {
            mRNA += Nucleotide.GUANINE.complement().toChar();
         } else if (nextChar == 'U') {
            mRNA += Nucleotide.THYMINE.complement().toChar();
         } else {
            // Directly just copy the current character if we do not know a
            // reverse complement for this character.
            mRNA += nextChar;
         }
      }
      return mRNA;
   }

   /**
    * Removes all indices that are outside of the acceptable maxDistance from an
    * mRNA start. If an index is within maxDistance from a positive start, or
    * maxDistance + the string's length from a negative mRNA start, it is kept.
    * Otherwise, it is discarded.
    * 
    * @param maxDistance
    *           The maximum distance from an mRNA start.
    * @param startEntries
    *           A list of start entries for a given string.
    * @param lengthOfMatchString
    *           The length of the string, which is used when calculating whether
    *           a given index is within the range of a reverse mRNA strand.
    */
   public void stripStartsOutsideRange(int maxDistance,
         List<StartEntry> startEntries, int lengthOfMatchString) {
      List<StartEntry> toRemove = new ArrayList<StartEntry>();
      for (StartEntry entry : startEntries) {
         boolean keepThisIndex = false;

         for (Integer positiveStart : mRNAStartsPositive) {
            if (entry.start + maxDistance >= positiveStart
                  && entry.start + lengthOfMatchString <= positiveStart) {
               keepThisIndex = true;
               break;
            }
         }
         for (Integer negativeStart : mRNAStartsNegative) {
            if (lengthOfMatchString + entry.start - maxDistance <= negativeStart
                  && entry.start >= negativeStart) {
               keepThisIndex = true;
               break;
            }
         }
         // If we are not keeping this index, put it in the list to remove.
         if (!keepThisIndex) {
            toRemove.add(entry);
         }
      }
      // Remove indices
      startEntries.removeAll(toRemove);
   }

   public static List<PalindromeEntry> findPalindromes(String string,
         int minRadius, int maxGap) {
      List<String> strings = new ArrayList<String>(2);
      strings.add(string);
      strings.add(new StringBuilder(string).reverse().toString());
      return findPalindromes(strings, minRadius, 0, maxGap);
   }

   /**
    * 
    * @param strings
    *           Exactly two strings which are reverse of eachother.
    * @param minRadius
    * @param minGap Smallest gap size to try (inclusive)
    * @param maxGap Largest gap size to try (inclusive)
    * @return
    */
   public static List<PalindromeEntry> findPalindromes(List<String> strings,
         int minRadius, int minGap, int maxGap) {
      if (strings.size() != 2) {
         throw new IllegalArgumentException("Must give exactly two strings");
      }
      if (strings.get(0).length() != strings.get(1).length()) {
         throw new IllegalArgumentException(
               "Both strings must be the same length");
      }

      SuffixTree tree = SuffixTree.create(strings);
      List<PalindromeEntry> pals = new LinkedList<PalindromeEntry>();
      for (int g = minGap; g <= maxGap; g++) {
         pals.addAll(tree.findPalindromes(minRadius, g));
      }
      return pals;
   }
}
