package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Mitchell Rosen
 * @version 21-Apr-2013
 */
public class GeneTest {

   @Test
   public void testDisjunctSize() {
      GeneIsoform isoform1 = new GeneIsoform("", 1, 8, false, "", "");
      GeneIsoform isoform2 = new GeneIsoform("", 3, 4, false, "", "");
      GeneIsoform isoform3 = new GeneIsoform("", 8, 20, false, "", "");
      GeneIsoform isoform4 = new GeneIsoform("", 243, 506, true, "", "");
      Gene testGene = new Gene();
      testGene.addIsoform(isoform1);
      testGene.addIsoform(isoform2);
      testGene.addIsoform(isoform3);
      testGene.addIsoform(isoform4);

      assertEquals(505, testGene.size());
   }

   @Test
   public void testNonDisjunctSize() {
      GeneIsoform isoform1 = new GeneIsoform("", 1, 5, false, "", "");
      GeneIsoform isoform2 = new GeneIsoform("", 4, 7, true, "", "");
      GeneIsoform isoform3 = new GeneIsoform("", 4, 9, true, "", "");
      Gene testGene = new Gene();
      testGene.addIsoform(isoform1);
      testGene.addIsoform(isoform2);
      testGene.addIsoform(isoform3);

      assertEquals(8, testGene.size());

   }

   @Test
   public void testSingleSize() {
      GeneIsoform isoform = new GeneIsoform("", 1, 8, false, "", "");
      Gene testGene = new Gene();
      testGene.addIsoform(isoform);

      assertEquals(10, testGene.size());

   }
}
