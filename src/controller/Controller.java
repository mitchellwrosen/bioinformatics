package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import model.GCContentInfo;
import model.GFFParser;
import model.GFFParser.ParseException;
import model.Gene;
import model.GeneIsoform;
import model.GeneUtils;
import model.Sequence;
import suffixtree.SuffixTree;
import suffixtree.SuffixTree.PalindromeEntry;
import suffixtree.SuffixTree.RepeatEntry;
import suffixtree.SuffixTree.StartEntry;
import suffixtree.SuffixTreeUtils;

/**
 * The Controller
 * 
 * @author MitchellRosen
 * @version 21-Apr-2013
 */
public class Controller {
   
   private List<StringBuilder> mSuperFastaFiles = new ArrayList<StringBuilder>();
   private List<List<Gene>> mSuperGffFiles = new ArrayList<List<Gene>>();

   private static final Pattern mSequenceNumberPattern = Pattern
         .compile("\\D+(\\d+)\\.\\d+\\.fna");
   private static final Pattern mGffNumberPattern = Pattern
         .compile("\\D+(\\d+)\\.\\d+\\.gff");

   protected String mSequenceFile;
   protected String mGffFile;

   protected Sequence mSequence;
   protected List<Gene> mGenes;

   public Controller() {
      mSequenceFile = new String();
      mGffFile = new String();
   }

   public void useSequenceFile(String filename) throws IOException,
         IllegalArgumentException {
      if (!mSequenceFile.equals(filename)) {
         mSequenceFile = filename;
         mSequence = new Sequence(mSequenceFile);
         if (mGenes != null) {
            for (Gene gene : mGenes) {
               gene.setSequence(mSequence);
            }
         }
      }
   }

   public void useGffFile(String filename) throws IOException, ParseException {
      if (!mGffFile.equals(filename)) {
         mGffFile = filename;

         GFFParser parser = new GFFParser();
         mGenes = parser.parse(filename);
         if (mSequence != null) {
            for (Gene gene : mGenes) {
               gene.setSequence(mSequence);
            }
         }
      }

   }

   /**
    * Called when "run" is pressed in the GC content tab. Assumes the file has
    * already been set.
    * 
    * @param startPos
    *           The start position in the file to process. Indexed inclusively
    *           from 1. If the string is empty, we index from the start of file.
    * @param endPos
    *           The end position in the file to process. Indexed inclusively
    *           from 1. (Or exclusively from 0). If empty, we set to end of
    *           file.
    * @param useSlidingWindow
    *           True if we are using sliding window logic.
    * @param winSize
    *           A string containing the size of individual windows to use.
    * @param shiftIncr
    *           The amount to shift the window in our sliding window protocol.
    * @return A string representing information about a nucleotide sequence, or
    *         an appropriate error message detailing why one could not be
    *         provided.
    */
   public String getGcContent(String startPos, String endPos,
         boolean useSlidingWindow, String winSize, String shiftIncr) {
      StringBuilder sb = new StringBuilder();
      sb.append("Start, stop, min %, max %\n");

      if (startPos.isEmpty()) {
         startPos = "1";
      }

      if (endPos.isEmpty()) {
         endPos = Integer.toString(mSequence.size());
      }

      mSequence =
            mSequence.slice(Integer.parseInt(startPos) - 1,
                  Integer.parseInt(endPos));

      if (useSlidingWindow) {
         GCContentInfo[] gcs =
               mSequence.gcContentHistogram(Integer.parseInt(winSize),
                     Integer.parseInt(shiftIncr));

         for (GCContentInfo gc : gcs) {
            sb.append(gc + "\n");
         }
      } else {
         sb.append(String.format("%5d, %5d, %5.2f%%, %5.2f%%",
               Integer.parseInt(startPos), Integer.parseInt(endPos),
               mSequence.gcContentMin() * 100, mSequence.gcContentMax() * 100));
      }

      return sb.toString();
   }

   public String getNucleotides() {
      return String.format("%d", mSequence.size());
   }

   public String getGenes() {
      return String.format("%d", mGenes.size());
   }

   public String getIsoforms() {
      return String.format("%d", GeneUtils.numIsoforms(mGenes));
   }

   public String avgGeneSize() {
      return String.format("%.2f", GeneUtils.avgGeneSize(mGenes));
   }

   public String avgCdsSize() {
      return String.format("%.2f", GeneUtils.avgCdsSize(mGenes));
   }

   public String avgExonSize() {
      return String.format("%.2f", GeneUtils.avgExonSize(mGenes));
   }

   public String avgIntronSize() {
      return String.format("%.2f", GeneUtils.avgIntronSize(mGenes));
   }

   public String avgIntergenicRegionSize() {
      return String.format("%.2f", GeneUtils.avgIntergenicRegionSize(mGenes));
   }

   public String geneDensity() {
      return String.format("%.2f", GeneUtils.geneDensity(mGenes));
   }

   public String cdsDensity() {
      return String.format("%.2f", GeneUtils.cdsDensity(mGenes));
   }

   public String genesPerKilobase() {
      return String.format("%.2f", GeneUtils.genesPerKilobase(mGenes));
   }

   public String kilobasesPerGene() {
      return String.format("%.2f", GeneUtils.kilobasesPerGene(mGenes));
   }

   public String getProteins() {
      StringBuilder sb =
            new StringBuilder("gene name, isoform name, protein\n");

      for (Gene g : mGenes) {
         String geneName = g.getId();
         for (GeneIsoform iso : g.getIsoforms()) {
            sb.append(geneName + ", ");
            sb.append(iso.getTranscriptId() + ", ");
            if (iso.isReverse()) {
               sb.append(iso.getSequence().reverseComplement()
                     .toProteinString()
                     + "\n");
            } else {
               sb.append(iso.getSequence().toProteinString() + "\n");
            }
         }
      }

      return sb.toString();
   }

   /**
    * Find all repeats.
    * 
    * @param minimumRepeatLength
    *           The minimum length of a repeat.
    * @param maxDistanceFromMRNAStart
    *           The maximum distance from the next mRNA start. If negative, all
    *           occurrences will be included regardless of distance from mRNA
    *           start.
    * @return Returns formatted output representing all repeated strings within
    *         the sequence.
    */
   public String getRepeats(int minimumRepeatLength,
         int maxDistanceFromMRNAStart) {
      StringBuilder matchInfo = new StringBuilder();
      SuffixTreeUtils treeUtil = new SuffixTreeUtils(mSequence, mGenes);

      SuffixTree tree = SuffixTree.create(mSequence.toString());
      List<RepeatEntry> repeats = tree.findRepeats(minimumRepeatLength);
      matchInfo.append("Repeated sequence,Frequency,Fold Expression,Average"
            + " Distance From (+) mRNA Start,Average Distance From (-)"
            + " mRNA Start,Coordinates\n");
      for (RepeatEntry repeat : repeats) {
         List<StartEntry> occurences = repeat.getStarts();
         if (maxDistanceFromMRNAStart >= 0) {
            treeUtil.stripStartsOutsideRange(maxDistanceFromMRNAStart,
                  occurences, repeat.toString().length());
         }

         matchInfo.append(repeat + ",");
         matchInfo.append(repeat.getStarts().size() + ",");
         matchInfo.append(repeat.getStarts().size()
               / treeUtil.findExpectedFoldExpression(repeat.toString()) + ",");
         matchInfo.append(treeUtil.averageDistanceToNextPositiveMRNA(repeat
               .getStarts()) + ",");
         matchInfo.append(treeUtil.averageDistanceToNextNegativeMRNA(repeat
               .getStarts()) + ",");

         for (StartEntry occurence : occurences) {
            // Represent as 1-indexed for ease of bio students.
            matchInfo.append(" " + (occurence.start + 1));
         }
         matchInfo.append("\n");
      }
      return matchInfo.toString();
   }

   /**
    * Find all occurrences of the given search string.
    * 
    * @param searchString
    *           The string to be searched for.
    * @param maxDistanceFromMRNAStart
    *           The maximum distance from the next mRNA start. If negative, all
    *           occurrences will be included regardless of distance from mRNA
    *           start. <Currently excluded from calculations!>
    * @return Returns formatted output representing all repeated strings within
    *         the sequence.
    */
   public String matchString(String searchString, int maxDistanceFromMRNAStart) {
      SuffixTreeUtils treeUtil = new SuffixTreeUtils(mSequence, mGenes);

      String reverseSearchString =
            SuffixTreeUtils.reverseComplement(searchString);

      SuffixTree tree = SuffixTree.create(mSequence.toString());

      List<StartEntry> occurences = tree.getOccurrences(searchString);
      List<StartEntry> revOccurences = tree.getOccurrences(reverseSearchString);

      if (maxDistanceFromMRNAStart >= 0) {
         treeUtil.stripStartsOutsideRange(maxDistanceFromMRNAStart, occurences,
               searchString.length());
         treeUtil.stripStartsOutsideRange(maxDistanceFromMRNAStart,
               revOccurences, searchString.length());
      }

      StringBuilder matchInfo = new StringBuilder();

      int absoluteFrequency = occurences.size();
      int reverseAbsoluteFrequency = revOccurences.size();

      double averageDistance =
            treeUtil.averageDistanceToNextPositiveMRNA(occurences);
      double averageDistanceNegative =
            treeUtil.averageDistanceToNextNegativeMRNA(occurences);

      double reverseAverageDistance =
            treeUtil.averageDistanceToNextPositiveMRNA(revOccurences);
      double reverseAverageDistanceNegative =
            treeUtil.averageDistanceToNextNegativeMRNA(revOccurences);

      double expectedFoldExpression =
            treeUtil.findExpectedFoldExpression(searchString);
      double reverseExpectedFoldExpression =
            treeUtil.findExpectedFoldExpression(reverseSearchString);

      double relativeFoldExpression =
            absoluteFrequency / expectedFoldExpression;
      double reverseRelativeFoldExpression =
            reverseAbsoluteFrequency / reverseExpectedFoldExpression;
      matchInfo.append("Repeated sequence,Frequency,Fold Expression,Average"
            + " Distance From (+) mRNA Start,Average Distance From (-)"
            + " mRNA Start,Coordinates\n");

      matchInfo.append(searchString + "," + absoluteFrequency + ","
            + relativeFoldExpression + "," + averageDistance + ","
            + averageDistanceNegative + ",");
      for (StartEntry occurance : occurences) {
         // Represent as 1-indexed for ease of bio students.
         matchInfo.append(" " + (occurance.start + 1));
      }

      matchInfo.append("\n" + reverseSearchString + ","
            + reverseAbsoluteFrequency + "," + reverseRelativeFoldExpression
            + "," + reverseAverageDistance + ","
            + reverseAverageDistanceNegative + ",");
      for (StartEntry occurance : revOccurences) {
         // Represent as 1-indexed for ease of bio students.
         matchInfo.append(" " + (occurance.start + 1));
      }
      return matchInfo.toString();
   }

   /**
    * Converts the DNA String to microRNA, then finds palindromes and returns
    * them.
    * 
    * @param nucleotideGap
    *           The maximum distance between and external and internal
    *           palindrome when including imperfect plaindromes.
    * @return A formatted string representing microRNA strands within the
    *         current FASTA file.
    */
   public String findMRNA(int nucleotideGap) {
      return findMRNA(nucleotideGap, mSequence);
   }

   public static String findMRNA(int nucleotideGap, Sequence inputSequence) {
      List<String> strings = new ArrayList<String>();
      strings.add(SuffixTreeUtils.toMRNAString(inputSequence.toString()));
      strings.add(SuffixTreeUtils.reverseComplementMRNAString(SuffixTreeUtils
            .toMRNAString(inputSequence.toString())));

      // Minimum needs to be quite low. maximum = sizeof(entire pri mrna)
      List<PalindromeEntry> entries =
            SuffixTreeUtils.findPalindromes(strings, 2, 0, 20);

      StringBuilder returnVal =
            new StringBuilder("start, stop, miRNA length, sequence\n");

      for (PalindromeEntry palindrome : entries) {
         int start = palindrome.getStart().start + 1;
         int length = palindrome.getRadius();
         int stop = start + length;

         String sequence = strings.get(0).substring(start - 1, stop - 1);
         sequence += "[";
         sequence +=
               strings.get(0).substring(stop - 1,
                     stop - 1 + palindrome.getGap());
         sequence += "]";
         sequence +=
               strings.get(0).substring(stop - 1 + palindrome.getGap(),
                     stop - 1 + palindrome.getGap() + palindrome.getRadius());
         if (length >= 21 && length <= 23) {
            returnVal.append(start + "," + stop + "," + length + "," + sequence
                  + "\n");
         } else if (length < 21) {
            for (PalindromeEntry secondPalindrome : entries) {
               int secondStart = secondPalindrome.getStart().start + 1;
               int secondLength = secondPalindrome.getRadius();
               int secondStop = secondStart + secondLength;

               /*
                * If new palindrome is inside the first palindrome AND the
                * second start is exactly nucleotidesGap from the first stop AND
                * the sum of the palindrome lengths is between 21 and 23, add
                * it.
                * 
                * This code is largely untested. I created a local test, but was
                * unable to run it because the CORRECT string was not recognized
                * as a real palindrome.
                */
               if (secondStart > stop
                     && secondStop + 2 * secondLength
                           + secondPalindrome.getGap() < start + length
                           + palindrome.getGap()
                     && secondStart - stop == nucleotideGap
                     && (secondLength + length >= 21 && secondLength + length <= 23)) {
                  String updatedSequence =
                        strings.get(0).substring(start - 1, stop - 1);
                  updatedSequence += "[";
                  updatedSequence +=
                        strings.get(0).substring(stop - 1, secondStart - 1);
                  updatedSequence += "]";
                  updatedSequence +=
                        strings.get(0).substring(secondStart - 1, secondStop);
                  updatedSequence += "[";
                  updatedSequence +=
                        strings.get(0).substring(secondStop,
                              secondStop + secondPalindrome.getGap());
                  updatedSequence += "]";
                  updatedSequence +=
                        strings.get(0).substring(
                              secondStop + secondPalindrome.getGap(),
                              secondStop + secondPalindrome.getGap()
                                    + secondLength);
                  updatedSequence += "[";
                  updatedSequence +=
                        strings.get(0).substring(
                              secondStop + secondPalindrome.getGap()
                                    + secondLength,
                              stop - 1 + palindrome.getGap());
                  updatedSequence += "]";
                  sequence +=
                        strings.get(0).substring(
                              stop - 1 + palindrome.getGap(),
                              stop - 1 + palindrome.getGap()
                                    + palindrome.getRadius());
                  returnVal.append(start + "," + stop + "," + length + ","
                        + updatedSequence);
               }
            }
         }
      }
      return returnVal.toString();
   }

   /**
    * Merges the given fasta files and returns a list of the offset for each file in a parallel list.
    * @param sb An empty StringBuilder that will be filled with the merged fasta
    * @param fastaFileNames
    * @return
    * @throws IllegalArgumentException
    * @throws IOException
    */
   public static List<Integer> mergeFasta(StringBuilder sb,
         List<Sequence> sequences) throws IllegalArgumentException, IOException {
      String prev = null;
      Integer prevOffset = 0;
      List<Integer> offsets = new ArrayList<Integer>(sequences.size());
      for (Sequence seq : sequences) {
         String s = seq.toString();
         Integer offset = 0;
         if (prev != null) {
            offset = SuffixTreeUtils.findOverlap(prev, s);
         }
         prev = s;
         sb.append(s.substring(offset));
         offsets.add(prevOffset += offset);
      }
      return offsets;
   }

   public static List<Gene> convertAndMergeGFF(List<Integer> offsets,
         List<List<Gene>> geneLists) throws IOException, ParseException {
      /* TODO
      GFFParser parser = new GFFParser();

      List<Gene> genes = new LinkedList<Gene>();
      for (String filename : gffFilenames) {
         genes.addAll(parser.parse(filename));
      }

      Collections.sort(genes, new Comparator<Gene>() {
         @Override
         public int compare(Gene g1, Gene g2) {
            return g1.getStart() - g2.getStart();
         }
      });

      // Report overlapping genes and mark duplicate genes (to remove on a second pass).
      List<Gene> genesToRemove = new ArrayList<Gene>();
      for (int i = 0; i < genes.size() - 1; ++i) {
         Gene thisGene = genes.get(i);
         Gene nextGene = genes.get(i + 1);
         if (thisGene.getStop() > nextGene.getStart()) {
            ; // report overlap
         } else if (thisGene.getStart() == nextGene.getStart()
               && thisGene.getStop() == nextGene.getStop()) {
            genesToRemove.add(thisGene);
         }
      }

      for (Gene gene : genesToRemove) {
         genes.remove(gene); // Ignoring return value, perhaps bad style.
      }

      return genes;
      */
      return null;
   }

   /**
    * Read in two zip files, report any errors when trying to merge the files together.
    * @param sequenceZipPath A path to the sequence zip.
    * @param gffZipPath A path to the gff zip.
    * @return The locations of conflicts between exons in the two GFF files.
    * @throws IOException 
    * @throws ParseException 
    */
   public String runCreateSuperContigs(String sequenceZipPath, String gffZipPath) throws IOException, ParseException {
      GFFParser parser = new GFFParser();
      // Return list of occurences of mistaken GFF files.
      ZipFile sequenceZip = new ZipFile(sequenceZipPath);
      ZipFile gffZip = new ZipFile(gffZipPath);

      Enumeration<? extends ZipEntry> sequenceFiles = sequenceZip.entries();
      Enumeration<? extends ZipEntry> gffFiles = gffZip.entries();


      SortedMap<Integer, ZipEntry> fastas = new TreeMap<Integer, ZipEntry>();
      SortedMap<Integer, ZipEntry> gffs = new TreeMap<Integer, ZipEntry>();

      while (sequenceFiles.hasMoreElements()) {
         ZipEntry sequenceFile = sequenceFiles.nextElement();
         if (sequenceFile.getName().endsWith(".fna")) {
            Matcher matcher = mSequenceNumberPattern.matcher(sequenceFile.getName());
            if(matcher.find()) {
               String version = matcher.group(1);
               fastas.put(Integer.parseInt(version), sequenceFile);
            }
         }
      }

      while (gffFiles.hasMoreElements()) {
         System.out.println("File.");
         ZipEntry gffFile = gffFiles.nextElement();
         if (gffFile.getName().endsWith(".gff")) {
            System.out.println("...ends with gff");
            Matcher matcher = mGffNumberPattern.matcher(gffFile.getName());
            if(matcher.find()) {
               System.out.println("......matched");
               String version = matcher.group(1);
               gffs.put(Integer.parseInt(version), gffFile);
            }
         }
      }
      
      int i = 0;
      int count = 0;
      boolean lastMatched = false;
      
      System.out.println(fastas.keySet());
      System.out.println(gffs.keySet());
      List<Sequence> sequences = new ArrayList<Sequence>();
      List<List<Gene>> genes = new ArrayList<List<Gene>>();

      mSuperFastaFiles = new ArrayList<StringBuilder>();
      mSuperGffFiles = new ArrayList<List<Gene>>();
      while (count < fastas.size()) {
         if (fastas.containsKey(i)) {
            if (gffs.containsKey(i)) {
               lastMatched = true;

               sequences.add(new Sequence(sequenceZip.getInputStream(fastas.get(i))));
               genes.add(parser.parse(gffZip.getInputStream(gffs.get(i))));
            }
            count++;
         } else if (lastMatched) {
            mSuperFastaFiles.add(new StringBuilder());
            List<Integer> offsets = mergeFasta(
                  mSuperFastaFiles.get(mSuperFastaFiles.size() - 1), sequences);
            mSuperGffFiles.add(convertAndMergeGFF(offsets, genes));
            lastMatched = false;

            sequences = new ArrayList<Sequence>();
            genes = new ArrayList<List<Gene>>();
         }
         i++;
      }

      if(lastMatched) {
         mSuperFastaFiles.add(new StringBuilder());
         List<Integer> offsets = mergeFasta(
               mSuperFastaFiles.get(mSuperFastaFiles.size() - 1), sequences);
         mSuperGffFiles.add(convertAndMergeGFF(offsets, genes));
      }
      System.out.println("done");

      return "Currently not detecting errors.";
   }

   /**
    * Save the instance variables representing the super fasta and super gff
    * files generated by runCreateSuperContigs.
    * 
    * @throws IOException
    */
   public void saveSuperFiles(File out) throws IOException {
      ZipOutputStream output = new ZipOutputStream(new FileOutputStream(out));

      int count = 0;
      for (StringBuilder fasta : mSuperFastaFiles) {
         count++;
         ZipEntry fastaEntry = new ZipEntry("superFasta" + count + ".0.fna");
         output.putNextEntry(fastaEntry);
         byte[] data = fasta.toString().getBytes();
         output.write(data, 0, data.length);
         output.closeEntry();
      }
      System.out.println("Num printed fasta files:" + count);
      
      count = 0;
      for(List<Gene> gff : mSuperGffFiles) {
         count++;
         ZipEntry gffEntry = new ZipEntry("superGff" + count + ".0.gff");
         output.putNextEntry(gffEntry);
         StringBuilder sb = new StringBuilder();
         for(Gene gene : gff) {
            sb.append(gene.toGff());
         }
         byte[] data = sb.toString().getBytes();
         output.write(data, 0, data.length);
         output.closeEntry();
      }
      output.close();
   }
}
