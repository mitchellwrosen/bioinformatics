package controller;

import java.io.IOException;
import java.util.Iterator;

import model.GCContentInfo;
import model.Sequence;
import view.InputDialog;

/**
 * Main controller for running the application.
 * 
 * @author cstearns
 * 
 */
public class Main {
   /**
    * Launch the program.
    * 
    * @param args
    *           Unused command line arguments.
    */
   public static void main(String[] args) {
      InputDialog dialog = new InputDialog();

      dialog.init();
      dialog.setVisible(true);
   }

   /**
    * Does the internal control for pressing the "run" button of the UI.
    * Essentially, this stage links our model and view. Note that while most
    * parameters are strings, they will be used as ints or other features
    * internally. misuse of these parameters will result in error messages being
    * placed in the output field.
    * 
    * @param mFile
    *           The name of the file to process
    * @param mStartPos
    *           The start position in the file to process. Indexed inclusively
    *           from 1. If the string is empty, we index from the start of file.
    * @param mEndPos
    *           The end position in the file to process. Indexed inclusively
    *           from 1. (Or exclusively from 0). If empty, we set to end of file.
    * @param mUseSlidingWindow
    *           True if we are using sliding window logic.
    * @param mWinSize
    *           A string containing the size of individual windows to use.
    * @param mShiftIncr
    *           The amount to shift the window in our sliding window protocol.
    * @return A string representing information about a nucleotide sequence, or
    *         an appropriate error message detailing why one could not be
    *         provided.
    */
   public static String handleRun(String mFile, String mStartPos,
         String mEndPos, boolean mUseSlidingWindow, String mWinSize,
         String mShiftIncr) {
      String newTextArea = "Start, stop, min %, max %\n";
      try {
         Sequence s = new Sequence(mFile);
         if (s.isValid()) {
            if(mStartPos.isEmpty()) {
               mStartPos = "1";
            }
            if(mEndPos.isEmpty()) {
               mEndPos = Integer.toString(s.size());
            }
            s = s.slice(Integer.valueOf(mStartPos) - 1, Integer.valueOf(mEndPos));
            if (mUseSlidingWindow) {
               GCContentInfo[] gcs = s.gcContentHistogram(Integer
                     .valueOf(mWinSize), Integer.valueOf(mShiftIncr));
               // Potential area for performance improvement, replace string with
               // stringbuilder.
               for (GCContentInfo gc : gcs) {
                  newTextArea += gc + "\n";
               }
               return newTextArea;
            } else {
               newTextArea += String.format("%d, %d, %.2f%%, %.2f%%", Integer
                     .valueOf(mStartPos), Integer.valueOf(mEndPos), s
                     .gcContentMin() * 100, s.gcContentMax() * 100);
               return newTextArea;
            }
         } else {
            StringBuilder b = new StringBuilder();
            Iterator<String> errors = s.getErrors();
            while (errors.hasNext()) {
               b.append(errors.next() + "\n");
            }
            s.clearErrors();
            newTextArea = b.toString();
         }
      } catch (IOException err) {
         newTextArea = "Error trying to open file: " + err.getMessage();
      } catch (NumberFormatException err) {
         newTextArea = "Please only use numbers for start/end positions, window sizes, and shift increment.";
      } catch (IndexOutOfBoundsException err) {
         newTextArea = "Ensure that start position and end position are within the file size.";
      }
      return newTextArea;
   }
}
