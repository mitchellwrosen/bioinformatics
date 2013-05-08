package view;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class FindRepeatsPanel extends JPanel  {
   protected JTextArea mDisplay;
   /** Optional field representing minimum length for returned repeat sequences. */
   protected JTextField mMinimumLength;
   /** Maximum distance from END of repeat to START of gene sequence. */
   protected JTextField mMaxDistanceFromStart;
   /** Optional field representing a specific string to be matched. */
   protected JTextField mSearchForString;

   public FindRepeatsPanel() {
      mDisplay = new JTextArea();
      mMinimumLength = new JTextField(20);
      mMaxDistanceFromStart = new JTextField(20);
      mSearchForString = new JTextField(20);
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(setupFilterBox());
      add(prepareDisplayArea());
   }
   
   protected JScrollPane prepareDisplayArea() {
      JScrollPane scrollDisplay = new JScrollPane(mDisplay);
      scrollDisplay.setPreferredSize(new Dimension(5000, 5000));
      return scrollDisplay;
   }

   protected Box setupFilterBox() {
      Box controlBox = Box.createVerticalBox();
      Box minLengthBox = Box.createHorizontalBox();
      Box maxDistanceBox = Box.createHorizontalBox();
      Box searchBox = Box.createHorizontalBox();

      minLengthBox.add(new JLabel("Minimum Length:"));
      minLengthBox.add(mMinimumLength);
      maxDistanceBox.add(new JLabel("Maximum distance to next mRNA start:"));
      maxDistanceBox.add(mMaxDistanceFromStart);
      searchBox.add(new JLabel("Exact String to match:"));
      searchBox.add(mSearchForString);

      controlBox.add(minLengthBox);
      controlBox.add(maxDistanceBox);
      controlBox.add(searchBox);
      return controlBox;
   }
   
   public String getDisplay() { return mDisplay.getText(); }
   
   public void setDisplay(String text) { mDisplay.setText(text); }
}
