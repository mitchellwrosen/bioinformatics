package model;

import java.util.Map;

public class Exon extends GffFeature {
   public Exon(String chromosome, String source, String feature, int start, int stop, String score,
         boolean reverse, String frame, Map<String, String> attributes) {
      super(chromosome, source, feature, start, stop, score, reverse, frame, attributes);
   }
}
