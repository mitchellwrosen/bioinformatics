package controller;

import static utils.Functional.foldl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.GCContentInfo;
import model.Gene;
import model.GeneIsoform;
import model.GeneUtils;
import model.Sequence;
import utils.Folder;

/**
 * The Controller
 * 
 * @author MitchellRosen
 * @version 20-Apr-2013
 */
public class Controller {
   protected String mSequenceFile;
   protected String mGffFile;

   protected Sequence mSequence;
   protected List<Gene> mGenes;

   public Controller() {
      mSequenceFile = new String();
      mGffFile = new String();
   }

   public void useSequenceFile(String filename) throws IOException, IllegalArgumentException {
      if (!mSequenceFile.equals(filename)) {
         mSequenceFile = new String(filename);
         mSequence = new Sequence(mSequenceFile);
      }
   }

   public void useGffFile(String filename) throws IOException {
      // TODO
      mGenes = new ArrayList<Gene>();
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
   public String getGcContent(String startPos, String endPos, boolean useSlidingWindow,
         String winSize, String shiftIncr) {
      StringBuilder sb = new StringBuilder();
      sb.append("Start, stop, min %, max %\n");

      if (startPos.isEmpty())
         startPos = "1";

      if (endPos.isEmpty())
         endPos = Integer.toString(mSequence.size());

      mSequence = mSequence.slice(Integer.parseInt(startPos) - 1, Integer.parseInt(endPos));

      if (useSlidingWindow) {
         GCContentInfo[] gcs = mSequence.gcContentHistogram(Integer.parseInt(winSize),
               Integer.parseInt(shiftIncr));

         for (GCContentInfo gc : gcs)
            sb.append(gc + "\n");
      } else {
         sb.append(String.format("%5d, %5d, %5.2f%%, %5.2f%%", Integer.parseInt(startPos),
               Integer.parseInt(endPos), mSequence.gcContentMin() * 100,
               mSequence.gcContentMax() * 100));
      }

      return sb.toString();
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
      return foldl(new Folder<StringBuilder, Gene>() {
         public StringBuilder execute(StringBuilder sb, Gene g) {
            final String geneName = g.getName();

            String isos = foldl(new Folder<StringBuilder, GeneIsoform>() {
               public StringBuilder execute(StringBuilder sb, GeneIsoform iso) {
                  sb.append(geneName + ", ");
                  sb.append(iso.getName() + ", ");
                  sb.append(iso.getSequence().toProteinString() + "\n");
                  return sb;
               }
            }, new StringBuilder(), g.getIsoforms()).toString();

            sb.append(isos);
            return sb;
         }
      }, new StringBuilder("gene name, isoform name, protein"), mGenes).toString();
   }
}
