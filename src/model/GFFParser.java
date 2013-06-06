package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * @version 22-Apr-2013
 */
public class GFFParser {
   public static class ParseException extends Exception { }
   
   /**
    * Class representing one feature constructed from one line of a GFF file.
    */
   protected static class Feature {
      public String chromosome; // chromosome/scaffold
      public String source; // name of program that generated this feature
      public String feature;
      public int start; // inclusive, 0 indexed
      public int stop; // exclusive, 0 indexed
      public String score;
      public boolean reverse; // Relative to |sequence|
      public String frame;
      public Map<String, String> attributes;

      public Feature(String chromosome, String source, String feature, int start, int stop,
            String score, boolean reverse, String frame, Map<String, String> attributes) {
         this.chromosome = chromosome;
         this.source = source;
         this.feature = feature;
         this.start = start - 1;
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
         
         return new Feature(chromosome, source, feature, start, stop, score, reverse, frame, attributes);
      }
      
      /**
       * Creates a GeneIsoform from this Feature.
       */
      public GeneIsoform toGeneIsoform() {
         if (!(feature.equals("mRNA")))
            return null;
         String geneId = attributes.get("gene_id");
         String transcriptId = attributes.get("transcript_id");
         return new GeneIsoform(chromosome, start, stop, reverse, geneId, transcriptId);
      }
      
      /**
       * Creates an Exon from this Feature.
       */
      public Exon toExon() {
         if (!(feature.equals("CDS")))
            return null;
         return new Exon(start, stop);
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append(String.format("Chromosome: %s\n", chromosome));
         sb.append(String.format("Source: %s\n", source));
         sb.append(String.format("Feature: %s\n", feature));
         sb.append(String.format("Start: %d\n", start + 1));
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
   
   protected BufferedReader r; // Input stream
   protected List<Gene> genes; // Output
   
   protected Gene gene;        // Current gene, to know when to switch genes
   protected GeneIsoform iso;  // Current isoform
   protected Feature feature;  // Current line
   
   protected void next() throws IOException {
      feature = Feature.fromLine(r.readLine());
   }

   public List<Gene> parse(InputStream stream) throws IOException, ParseException {
      r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
      genes = new ArrayList<Gene>();
      feature = Feature.fromLine(r.readLine());
      
      parseData();
      return genes;
   }

   public List<Gene> parse(String filename) throws IOException, ParseException {
      r = new BufferedReader(new FileReader(filename));
      genes = new ArrayList<Gene>();
      feature = Feature.fromLine(r.readLine());
      
      parseData();
      return genes;
   }
   
   // gene -> feature+
   protected void parseData() throws IOException, ParseException {
      parseFeature();
      while (feature != null)
         parseFeature();
   }
   
   // feature -> gene | unknown
   protected void parseFeature() throws IOException, ParseException {
      if (feature.feature.equals("mRNA")) {
         genes.add(parseGene());
         gene = null; // Forces the next isoform to initialize a new gene
      }
      else {
         parseUnknown();
      }
   }
   
   // gene -> isoform+
   protected Gene parseGene() throws IOException, ParseException {
      GeneIsoform iso = parseIsoform();
      if (iso == null)
         throw new ParseException();
      gene = Gene.create(iso);
      
      iso = parseIsoform();
      while (iso != null) {
         gene.addIsoform(iso);
         iso = parseIsoform();
      }
   
      return gene;
   }
   
   // isoform -> isoform_data exon+
   protected GeneIsoform parseIsoform() throws IOException, ParseException {
      GeneIsoform iso = parseIsoformData();
      if (iso == null)
         return null;
      
      Exon exon = parseExon();
      if (exon == null)
         throw new ParseException();
      iso.addExon(exon);
      
      exon = parseExon();
      while (exon != null) {
         iso.addExon(exon);
         exon = parseExon();
      }
      
      return iso;
   }
   
   // exon -> exon_data
   protected Exon parseExon() throws IOException, ParseException {
      return parseExonData();
   }
   
   protected GeneIsoform parseIsoformData() throws ParseException, IOException {
      if (feature == null)
         return null;
      
      GeneIsoform iso = feature.toGeneIsoform();
      if (iso == null)
         return null;
      
      // Only advance if this is an isoform of the current gene.
      if (gene != null && !(gene.getId().equals(iso.getGeneId()))) 
         return null;
      
      next();
      return iso;
   }
   
   protected Exon parseExonData() throws ParseException, IOException {
      if (feature == null)
         return null;
      
      Exon exon = feature.toExon();
      if (exon == null)
         return null;
      
      next();
      return exon;
   }
   
   protected Feature parseUnknown() throws ParseException, IOException {
      Feature f = feature;
      next();
      return f;
   }
}
