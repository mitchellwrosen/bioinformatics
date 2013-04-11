
public class Nucleotide {
   enum Type { ADENINE, CYTOSINE, GUANINE, THYMINE }
   
   protected Type type;
   
   public Nucleotide(Type type) { this.type = type; }
   
   public Nucleotide(char c) {
      switch (c) {
      case 'A':
      case 'a':
         type = Type.ADENINE;
         break;
      case 'C':
      case 'c':
         type = Type.CYTOSINE;
         break;
      case 'G':
      case 'g':
         type = Type.GUANINE;
         break;
      case 'T':
      case 't':
         type = Type.THYMINE;
         break;
      default:
         throw new IllegalArgumentException("Invalid nucleotide: " + c);
      }
   }
   
   public Type getType() { return type; }
   
   public Nucleotide complement() {
      switch (type) {
      case ADENINE:
         return new Nucleotide(Type.THYMINE);
      case CYTOSINE:
         return new Nucleotide(Type.GUANINE);
      case GUANINE:
         return new Nucleotide(Type.CYTOSINE);
      case THYMINE:
         return new Nucleotide(Type.ADENINE);
      default:
         assert false;
         return null;
      }
   }
}
