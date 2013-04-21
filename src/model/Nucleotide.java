package model;

/**
 * A single Nucleotide.
 * @author Mitchell Rosen
 */
public enum Nucleotide {
   ADENINE, CYTOSINE, GUANINE, THYMINE, UNKNOWN;
   
   public static Nucleotide fromChar(char c) throws IllegalArgumentException {
      switch (c) {
      case 'A':
      case 'a':
         return ADENINE;
      case 'C':
      case 'c':
    	  return CYTOSINE;
      case 'G':
      case 'g':
         return GUANINE;
      case 'T':
      case 't':
         return THYMINE;
      case 'N':
      case 'n':
         return UNKNOWN;
      default:
         throw new IllegalArgumentException("Invalid nucleotide: <" + c + ">");
      }
   }
   
   public Nucleotide complement() {
      switch (this) {
      case ADENINE:
         return THYMINE;
      case CYTOSINE:
         return GUANINE;
      case GUANINE:
         return CYTOSINE;
      case THYMINE:
         return ADENINE;
      case UNKNOWN:
    	  return UNKNOWN;
      default:
         assert false;
         return null;
      }
   }
   
   public char toChar() {
      switch (this) {
      case ADENINE:
         return 'A';
      case CYTOSINE:
         return 'C';
      case GUANINE:
         return 'G';
      case THYMINE:
         return 'T';
      case UNKNOWN:
        return '?';
      default:
         assert false;
         return 0;
      }
   }
}
