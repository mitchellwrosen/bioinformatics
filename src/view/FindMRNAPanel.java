package view;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FindMRNAPanel extends JPanel {
   protected JTextArea mDisplay;
   
   // The gap between outer and inner palindrome.
   protected JTextField mNucleotideGap;
   
   public FindMRNAPanel() {
      mDisplay = new JTextArea();
      
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      Box controlBox = Box.createHorizontalBox();
      controlBox.add(new JLabel("Nucleotide Gap: "));
      mNucleotideGap = new JTextField(20);
      controlBox.add(mDisplay);
      controlBox.add(mNucleotideGap);
      
      add(controlBox);
      add(prepareDisplayArea());
   }
   
   protected JScrollPane prepareDisplayArea() {
      JScrollPane scrollDisplay = new JScrollPane(mDisplay);
      scrollDisplay.setPreferredSize(new Dimension(5000, 5000));
      return scrollDisplay;
   }
   
   public String getDisplay() { return mDisplay.getText(); }
   
   public void setDisplay(String text) { mDisplay.setText(text); }

   /**
    * @return Returns the gap size between inner and outer palindromes in
    *         imperfect palindromes.
    */
   public String getNucleotideGap() {
      return mNucleotideGap.getText();
   }
}
