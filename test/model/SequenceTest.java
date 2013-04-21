package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

public class SequenceTest {

   private static Sequence smallSequence;

   // This was the filename, I have no idea what it actually means.
   private static Sequence dereDotContig2;

   private static Sequence dereLarge;
   private static Sequence listSequence;
   private static Sequence emptySequence;
   private static Sequence contig1234;
   private static Sequence contig7435;
   private static Sequence slide;

   @BeforeClass
   public static void beforeClass() throws IOException {
      smallSequence = new Sequence("smallfile.txt");
      dereDotContig2 = new Sequence("test_Dere_dot_contig2.txt");
      contig1234 = new Sequence("contig1_1234_bp.txt");
      contig7435 = new Sequence("contig1_7435_bp.txt");
      slide = new Sequence("slide1.txt");
      List<Nucleotide> nucleotides = new ArrayList<Nucleotide>();
      nucleotides.add(Nucleotide.ADENINE);
      nucleotides.add(Nucleotide.CYTOSINE);
      nucleotides.add(Nucleotide.GUANINE);
      nucleotides.add(Nucleotide.THYMINE);
      listSequence = new Sequence(nucleotides);
      emptySequence = new Sequence(Collections.<Nucleotide> emptyList());

      /*
       * This file has some weird stuff we should ask bio students about. As far
       * as I can tell, it is invalid.
       */
      dereLarge = new Sequence("test_Dere_large.txt");
   }

   @Test
   public void testSlice() {
      Sequence sliced = listSequence.slice(1, 2);
      Assert.assertEquals(1, sliced.size());
      Assert.assertEquals(1, sliced.gcContentMax(), .001);

      sliced = dereDotContig2.slice(0, 4);
      Assert.assertEquals(4, sliced.size());
      Assert.assertEquals(.25, sliced.gcContentMax(), .001);

   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testBadSlice() {
      emptySequence.slice(1, 2);
   }

   @Test
   public void testGcContentHistogram() {
      GCContentInfo[] info = slide.gcContentHistogram(4, 3);

      Assert.assertEquals("1,4,25.00%,50.00%", info[0].toString());
      Assert.assertEquals("4,7,50.00%,50.00%", info[1].toString());
      Assert.assertEquals("7,10,50.00%,100.00%", info[2].toString());
      Assert.assertEquals("10,13,50.00%,75.00%", info[3].toString());
      Assert.assertEquals("13,16,25.00%,50.00%", info[4].toString());
      Assert.assertEquals("16,19,25.00%,50.00%", info[5].toString());
      Assert.assertEquals("19,20,100.00%,100.00%", info[6].toString());

      
   }

   @Test
   public void testGcContentMin() {
      Assert.assertEquals(.4, smallSequence.gcContentMin(), .001);

      // TODO: Fix/investigate this test.
      Assert.assertEquals(.174, dereDotContig2.gcContentMin(), .003); // Our
                                                                      // tests
                                                                      // say
                                                                      // this
                                                                      // should
                                                                      // be .176

      Assert.assertEquals(.31, contig1234.gcContentMin(), .001);

      // Run to check performance, kind of...
      dereLarge.gcContentMin();
      contig7435.gcContentMin();
   }

   @Test
   public void testGcContentMax() {
      Assert.assertEquals(.6, smallSequence.gcContentMax(), .001);
      Assert.assertEquals(.31, contig1234.gcContentMax(), .001);
   }

}
