package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser of a GFF file. Uses the following "grammar":
 * 
 *    terminals:
 *       unknown
 *       isoform_data
 *       exon_data
 *    
 *    nonterminals:
 *       data    -> feature+
 *       feature -> gene | unknown
 *       gene    -> isoform+
 *       isoform -> isoform_data exon+
 *       exon    -> exon_data
 *
 * There are some additional rules, such as a gene will not consume as many
 * isoforms as it can, but rather stops once the isoform's gene_id changes.
 * 
 * @author Mitchell Rosen
 * @author Erik Sandberg
 * @version 23-Apr-2013
 */
public class GFFParser {

   /**
    * Class representing one feature constructed from one line of a GFF file.
    */
   protected static class Feature {
      public static final String ISOFORM_FEATURE = "mRNA";
      public static final String EXON_FEATURE    = "CDS";

      public String              chromosome;              // chromosome/scaffold
      public String              source;                  // name of program
                                                           // that generated
                                                           // this feature
      public String              feature;
      public int                 start;                   // inclusive, 0
                                                           // indexed
      public int                 stop;                    // exclusive, 0
                                                           // indexed
      public String              score;
      public boolean             reverse;                 // Relative to
                                                           // |sequence|
      public String              frame;
      public Map<String, String> attributes;

      public Feature(String chromosome, String source, String feature,
            int start, int stop, String score, boolean reverse, String frame,
            Map<String, String> attributes) {
         this.chromosome = chromosome;
         this.source = source;
         this.feature = feature;
         this.start = start;
         this.stop = stop;
         this.score = score;
         this.reverse = reverse;
         this.frame = frame;
         this.attributes = attributes;
      }

      public static Feature fromLine(String line) {
         if (line == null)
            return null;

         String[] tokens = line.split("\\s+");

         String chromosome = tokens[0];
         String source = tokens[1];
         String feature = tokens[2];
         int start = Integer.parseInt(tokens[3]);
         int stop = Integer.parseInt(tokens[4]);
         String score = tokens[5];
         boolean reverse = tokens[6].equals("-");
         String frame = tokens[7];

         Map<String, String> attributes = new HashMap<String, String>();
         for (int i = 8; i < tokens.length; i += 2)
            attributes.put(tokens[i], tokens[i + 1]);

         return new Feature(chromosome, source, feature, start, stop, score,
               reverse, frame, attributes);
      }

      /**
       * Creates a GeneIsoform from this Feature.
       */
      public GeneIsoform toGeneIsoform() {
         if (!Feature.ISOFORM_FEATURE.equals(feature)) {
            return null;
         }
         String geneId = attributes.get("gene_id");
         String transcriptId = attributes.get("transcript_id");
         return new GeneIsoform(chromosome, start, stop, reverse, geneId,
               transcriptId);
      }

      /**
       * Creates an Exon from this Feature.
       */
      public Exon toExon() {
         if (!Feature.EXON_FEATURE.equals(feature)) {
            return null;
         }
         return new Exon(start, stop);
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append(String.format("Chromosome: %s\n", chromosome));
         sb.append(String.format("Source: %s\n", source));
         sb.append(String.format("Feature: %s\n", feature));
         sb.append(String.format("Start: %d\n", start));
         sb.append(String.format("Stop: %d\n", stop));
         sb.append(String.format("Score: %s\n", score));
         sb.append(String.format("Reverse: %s\n", reverse));
         sb.append(String.format("Frame: %s\n", frame));

         sb.append("Attributes: {\n");
         for (Map.Entry<String, String> entry : attributes.entrySet())
            sb.append(String.format("   %s: %s\n", entry.getKey(),
                  entry.getValue()));
         sb.append("}\n");
         return sb.toString();
      }
   }

   protected enum State {
      INIT, GENE_START, ISOFORM_START, EXON;
   }

   protected State          state = State.INIT;

   protected BufferedReader r;                 // Input stream
   protected List<Gene>     genes;             // Output

   protected Gene           gene;              // Current gene, to know when to
                                                // switch genes
   protected GeneIsoform    iso;
   protected Feature        feature;           // Current line

   public static List<Gene> parse(String filename) throws IOException {
      GFFParser parser = new GFFParser(filename);
      return parser.parse();
   }

   protected GFFParser(String filename) throws IOException {
      r = new BufferedReader(new FileReader(filename));
      genes = new ArrayList<Gene>();
      feature = Feature.fromLine(r.readLine());
   }

   protected void next() throws IOException {
      feature = Feature.fromLine(r.readLine());
   }

   protected List<Gene> parse() throws IOException {
      while (feature != null) {
         parseFeature();
      }
      return genes;
   }

   protected void parseFeature() throws IOException {
      switch (state) {
         case INIT:
            if (Feature.ISOFORM_FEATURE.equals(feature.feature)) {
               state = State.GENE_START;
            } else {
               next();
            }
            break;
         case GENE_START:
            gene = new Gene();
            genes.add(gene);
            state = State.ISOFORM_START;
            break;
         case ISOFORM_START:
            iso = feature.toGeneIsoform();
            if (iso != null) {
               if (gene.getIsoforms().size() == 0
                     || gene.getId().equals(iso.getGeneId())) {
                  // This is the first isoform of the gene or part of the
                  // current
                  // gene.
                  gene.addIsoform(iso);
                  state = State.EXON;
                  next();
               } else {
                  // This is part of a different gene.
                  state = State.GENE_START;
               }
            } else {
               // Feature is not an isoform so ignore it.
               next();
            }
            break;
         case EXON:
            if (Feature.EXON_FEATURE.equals(feature.feature)) {
               iso.addExon(feature.toExon());
               next();
            } else if (Feature.ISOFORM_FEATURE.equals(feature.feature)) {
               state = State.ISOFORM_START;
            } else {
               next();
            }
            break;
         default:
            // This should never happen.
            assert false;
      }
   }
}
