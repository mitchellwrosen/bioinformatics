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
      // TODO - the rest
      
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
