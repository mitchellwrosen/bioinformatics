package model;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * An overly simple test suite for our protien functionality.
 * @author cstearns
 *
 */
public class AminoAcidTest {

   private static final String ALL_AMINO_ACIDS = "KNKNTTTTQHQHPPPPGGGGLFLF#Y#YRSRSVVVV#CWCRRRRAAAAEDEDSSSSLLLLIIMI";
   @Test
   public void testForward() throws IllegalArgumentException, IOException {
      Sequence testSeq = new Sequence("test/files/all_amino_acids.fna");
      
      String protein = testSeq.toProteinString();
      
      Assert.assertEquals(ALL_AMINO_ACIDS, protein);
   }

   @Test
   public void testReverse() throws IllegalArgumentException, IOException {
      Sequence testSeq = new Sequence("test/files/reversed_all_amino_acids.fna");
      
      String protein = testSeq.reverseCompliment().toProteinString();
      
      Assert.assertEquals(ALL_AMINO_ACIDS, protein);
   }
}
