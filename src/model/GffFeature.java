package model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A Feature from a GFF file (gene isoform, exon, etc).
 * 
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 */
@AllArgsConstructor
public class GffFeature {
   @Getter protected String chromosome; // chromosome/scaffold
   @Getter protected String source; // name of program that generated this feature
   @Getter protected String feature;
   @Getter protected int start; // inclusive
   @Getter protected int stop; // exclusive
   @Getter protected String score;
   @Getter protected boolean reverse; // Relative to |sequence|
   @Getter protected String frame;
   @Getter protected Map<String, String> attributes;

   public static GffFeature fromLine(String line) {
      if (line == null)
         return null;
      
      String[] tokens = line.split(" ");

      String chromosome = tokens[0];
      String source = tokens[1];
      String feature = tokens[2];
      int start = Integer.parseInt(tokens[3]);
      int stop = Integer.parseInt(tokens[4]);
      String score = tokens[5];
      boolean reverse = tokens[6] == "-";
      String frame = tokens[7];

      Map<String, String> attributes = new HashMap<String, String>();
      for (int i = 8; i < tokens.length; i += 2)
         attributes.put(tokens[i], tokens[i + 1]);

      switch (feature) {
      case "mRNA":
         return new GeneIsoform(chromosome, source, feature, start, stop, score, reverse, frame,
               attributes);
      case "CDS":
         return new Exon(chromosome, source, feature, start, stop, score, reverse, frame,
               attributes);
      default:
         return new GffFeature(chromosome, source, feature, start, stop, score, reverse, frame,
               attributes);
      }
   }
   
   public int size() {
      return start - stop;
   }
}
