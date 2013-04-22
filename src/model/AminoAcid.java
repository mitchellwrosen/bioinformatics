package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 */
public class AminoAcid {
   protected static final Map<String,Character> acids;
   static {
      Map<String,Character> temp = new HashMap<String,Character>();
      temp.put("TTT", 'F');
      temp.put("TTC", 'F');
      temp.put("TTA", 'L');
      temp.put("TTG", 'L');
      temp.put("CTT", 'L');
      temp.put("CTC", 'L');
      temp.put("CTA", 'L');
      temp.put("CTG", 'L');
      temp.put("ATT", 'I');
      temp.put("ATC", 'I');
      temp.put("ATA", 'I');
      temp.put("ATG", 'M');
      temp.put("GTT", 'V');
      temp.put("GTC", 'V');
      temp.put("GTA", 'V');
      temp.put("GTG", 'V');
      temp.put("TCT", 'S');
      temp.put("TCC", 'S');
      temp.put("TCA", 'S');
      temp.put("TCG", 'S');
      temp.put("CCT", 'P');
      temp.put("CCC", 'P');
      temp.put("CCA", 'P');
      temp.put("CCG", 'P');
      temp.put("ACT", 'T');
      temp.put("ACC", 'T');
      temp.put("ACA", 'T');
      temp.put("ACG", 'T');
      temp.put("GCT", 'A');
      temp.put("GCC", 'A');
      temp.put("GCA", 'A');
      temp.put("GCG", 'A');
      temp.put("TAT", 'Y');
      temp.put("TAC", 'Y');
      temp.put("TAA", '#');
      temp.put("TAG", '#');
      temp.put("CAT", 'H');
      temp.put("CAC", 'H');
      temp.put("CAA", 'Q');
      temp.put("CAG", 'Q');
      temp.put("AAT", 'N');
      temp.put("AAC", 'N');
      temp.put("AAA", 'K');
      temp.put("AAG", 'K');
      temp.put("GAT", 'D');
      temp.put("GAC", 'D');
      temp.put("GAA", 'E');
      temp.put("GAG", 'E');
      temp.put("TGT", 'C');
      temp.put("TGC", 'C');
      temp.put("TGA", '#');
      temp.put("TGG", 'W');
      temp.put("CGT", 'R');
      temp.put("CGC", 'R');
      temp.put("CGA", 'R');
      temp.put("CGG", 'R');
      temp.put("AGT", 'S');
      temp.put("AGC", 'S');
      temp.put("AGA", 'R');
      temp.put("AGG", 'R');
      temp.put("GGT", 'G');
      temp.put("GGC", 'G');
      temp.put("GGA", 'G');
      temp.put("GGG", 'G');
      
      acids = Collections.unmodifiableMap(temp);
   }
   
   /**
    * Gets the single-character Amino Acid representation of sequence |s|.
    * It is assumed that |s|.length == 3, otherwise the lookup will fail.
    */
   public static Character fromSequence(Sequence s) {
      Character c;
      if ((c = acids.get(s.toString())) != null)
         return c;
      return '?';
   }
}
