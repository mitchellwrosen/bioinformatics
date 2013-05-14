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
   protected JTextField mMaxDistanceToStart;
   /** Optional field representing a specific string to be matched. */
   protected JTextField mSearchForString;

   /** Label for the min length filter. */
   protected JLabel mMinimumLengthLabel;
   /** Label for the max distance filter. */
   protected JLabel mMaxDistanceLabel;
   /** Label for the mSearchButton */
   protected JLabel mSearchLabel;

   /**
    * Radio button option for selecting to use filters instead of exact string
    * matching.
    */
   protected JRadioButton mFilterButton;
   /** Radio button option for exact string matching. */
   protected JRadioButton mSearchButton;

   public FindRepeatsPanel() {
      mDisplay = new JTextArea();
      mMinimumLength = new JTextField(20);
      mMaxDistanceToStart = new JTextField(20);
      mSearchForString = new JTextField(20);

      mMinimumLength.setEnabled(true);

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
 
      Box filterContainerBox = Box.createVerticalBox();
      Box searchContainerBox = Box.createVerticalBox();

      Box minLengthBox = Box.createHorizontalBox();
      Box maxDistanceBox = Box.createHorizontalBox();

      Box filterBox = Box.createVerticalBox();
      Box searchBox = Box.createHorizontalBox();

      mSearchButton = new JRadioButton("Use Exact Match");
      mFilterButton = new JRadioButton("Use Minimum Length Filter");

      mMinimumLengthLabel = new JLabel("Minimum Length:");
      mMaxDistanceLabel = new JLabel("Maximum distance to next mRNA start:");
      mSearchLabel = new JLabel("Exact String to match:");

      filterContainerBox.add(mFilterButton);
      filterContainerBox.add(filterBox);
      filterBox.add(minLengthBox);
      //filterBox.add(maxDistanceBox);
      minLengthBox.add(mMinimumLengthLabel);
      minLengthBox.add(mMinimumLength);
      maxDistanceBox.add(mMaxDistanceLabel);
      maxDistanceBox.add(mMaxDistanceToStart);
      searchBox.add(mSearchLabel);
      searchBox.add(mSearchForString);
      searchContainerBox.add(mSearchButton);
      searchContainerBox.add(searchBox);

      controlBox.add(maxDistanceBox);
      controlBox.add(filterContainerBox);
      controlBox.add(searchContainerBox);

      ButtonGroup filterChooser = new ButtonGroup();
      filterChooser.add(mFilterButton);
      filterChooser.add(mSearchButton);

      mFilterButton.setSelected(true);

      mFilterButton.addActionListener(radioListen);
      mSearchButton.addActionListener(radioListen);
      radioListen.actionPerformed(null);
      return controlBox;
   }

   protected ActionListener radioListen = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         mMinimumLength.setEnabled(mFilterButton.isSelected());
         mMinimumLengthLabel.setEnabled(mFilterButton.isSelected());
         mSearchForString.setEnabled(mSearchButton.isSelected());
         mSearchLabel.setEnabled(mSearchButton.isSelected());
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
      return mSearchButton.isSelected();
   }

   /**
    * @return Return the minimum length filter as a string.
    */
   public String getMinimumLengthText() {
      return mMinimumLength.getText();
   }

   /**
    * @return Return the maximum distance to the next mRNA start as a string.
    */
   public String getMaximumDistanceToStartText() {
      return mMaxDistanceToStart.getText();
   }

   /**
    * @return Return the String to be searched for in the sequence.
    */
   public String getMatchStringText() {
      return mSearchForString.getText();
   }
}
