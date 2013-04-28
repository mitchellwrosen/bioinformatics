package model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import model.GFFParser.ParseException;

import org.junit.Test;

public class GFFParserTest {
   public void testGeneIsoform(GeneIsoform iso, String chromosome, int start, int stop, boolean reverse,
         String geneId, String transcriptId) {
      assertThat(iso.getChromosome(), is(chromosome));
      assertThat(iso.getStart(), is(start));
      assertThat(iso.getStop(), is(stop));
      assertThat(iso.isReverse(), is(reverse));
      assertThat(iso.getGeneId(), is(geneId));
      assertThat(iso.getTranscriptId(), is(transcriptId));
   }
   
   public void testExon(Exon exon, int start, int stop) {
      assertThat(exon.getStart(), is(start));
      assertThat(exon.getStop(), is(stop));
   }
   
   @Test
   public void parse() throws IOException, ParseException {
      Gene g;
      GeneIsoform iso;
      
      // Two genes, each with two isoforms, each with two exons
      GFFParser parser = new GFFParser("test/files/gff1.txt");
      List<Gene> genes = parser.parse();
      assertThat(genes.size(), is(2));
      
      // Gene 1 isoform 1
      g = genes.get(0);
      assertThat(g.getIsoforms().size(), is(2));
      iso = g.getIsoforms().get(0);
      testGeneIsoform(iso, "fosmid10", 732, 4493, true, "\"alphaCop\";", "\"alphaCop-RA\";");
      
      // Gene 1 isoform 1 exons
      assertThat(iso.getExons().size(), is(2));
      testExon(iso.getExons().get(0), 3547, 4493);
      testExon(iso.getExons().get(1), 732, 3491);
      
      // Gene 1 isoform 2
      iso = g.getIsoforms().get(1);
      testGeneIsoform(iso, "fosmid10", 732, 4493, true, "\"alphaCop\";", "\"alphaCop-RB\";");
      
      // Gene 1 isoform 2 exons
      assertThat(iso.getExons().size(), is(2));
      testExon(iso.getExons().get(0), 3547, 4493);
      testExon(iso.getExons().get(1), 732, 3491);
      
      // Gene 2 isoform 1
      g = genes.get(1);
      assertThat(g.getIsoforms().size(), is(2));
      iso = g.getIsoforms().get(0);
      testGeneIsoform(iso, "fosmid10", 732, 4493, false, "\"alphaCop2\";", "\"alphaCop2-RA\";");
      
      // Gene 2 isoform 1 exons
      assertThat(iso.getExons().size(), is(2));
      testExon(iso.getExons().get(0), 3547, 4493);
      testExon(iso.getExons().get(1), 732, 3491);
      
      // Gene 2 isoform 2
      iso = g.getIsoforms().get(1);
      testGeneIsoform(iso, "fosmid10", 732, 4493, false, "\"alphaCop2\";", "\"alphaCop2-RB\";");
      
      // Gene 2 isoform 2 exons
      assertThat(iso.getExons().size(), is(2));
      testExon(iso.getExons().get(0), 3547, 4493);
      testExon(iso.getExons().get(1), 732, 3491);
   }
}
