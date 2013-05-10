package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class FindRepeatsPanel extends JPanel {
   protected JTextArea mDisplay;
   /** Optional field representing minimum length for returned repeat sequences. */
   protected JTextField mMinimumLength;
   /** Maximum distance from END of repeat to START of gene sequence. */
   protected JTextField mMaxDistanceFromStart;
   /** Optional field representing a specific string to be matched. */
   protected JTextField mSearchForString;

   /** The box that contains the search field. */
   protected Box mSearchBox;
   /** The box that contains the filter components. */
   protected Box mFilterBox;

   protected JRadioButton filterButton;
   protected JRadioButton searchButton;

   public FindRepeatsPanel() {
      mDisplay = new JTextArea();
      mMinimumLength = new JTextField(20);
      mMaxDistanceFromStart = new JTextField(20);
      mSearchForString = new JTextField(20);

      mMinimumLength.setEnabled(true);
      mMaxDistanceFromStart.setEnabled(true);

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
      searchButton = new JRadioButton("Use Exact Match");

      Box filterContainerBox = Box.createVerticalBox();
      Box searchContainerBox = Box.createVerticalBox();

      Box minLengthBox = Box.createHorizontalBox();
      Box maxDistanceBox = Box.createHorizontalBox();

      mFilterBox = Box.createVerticalBox();
      mSearchBox = Box.createHorizontalBox();

      filterButton = new JRadioButton("Use Filtering");

      filterContainerBox.add(filterButton);
      filterContainerBox.add(mFilterBox);
      mFilterBox.add(minLengthBox);
      mFilterBox.add(maxDistanceBox);
      minLengthBox.add(new JLabel("Minimum Length:"));
      minLengthBox.add(mMinimumLength);
      maxDistanceBox.add(new JLabel("Maximum distance to next mRNA start:"));
      maxDistanceBox.add(mMaxDistanceFromStart);
      mSearchBox.add(new JLabel("Exact String to match:"));
      mSearchBox.add(mSearchForString);
      searchContainerBox.add(searchButton);
      searchContainerBox.add(mSearchBox);

      controlBox.add(filterContainerBox);
      controlBox.add(searchContainerBox);

      ButtonGroup filterChooser = new ButtonGroup();
      filterChooser.add(filterButton);
      filterChooser.add(searchButton);

      filterButton.addActionListener(radioListen);
      filterButton.setSelected(true);
      searchButton.addActionListener(radioListen);
      return controlBox;
   }

   protected ActionListener radioListen = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         mMaxDistanceFromStart.setEnabled(filterButton.isSelected());
         mMinimumLength.setEnabled(filterButton.isSelected());
         mSearchForString.setEnabled(searchButton.isSelected());
      }
   };

   public String getDisplay() {
      return mDisplay.getText();
   }

   public void setDisplay(String text) {
      mDisplay.setText(text);
   }

   /**
    * Public method for accessing which filter mode is selected.
    * 
    * @return Returns true if we are expecting to match an exact string. Returns
    *         false if we expect to use filtering.
    */
   public boolean isMatchExactString() {
      return searchButton.isSelected();
   }
}
