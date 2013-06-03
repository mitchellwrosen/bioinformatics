package view;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GCContentInfoPanel extends JPanel {
   protected JTextField mStartPos, mEndPos;
   protected JCheckBox mUseSlidingWindow;
   protected Box mSlideBox;
   protected JTextField mWinSize, mShiftIncr;
   protected JTextArea mDisplay;
   
   public GCContentInfoPanel() {
      mStartPos = new JTextField(20);
      mEndPos = new JTextField(20);
      
      mUseSlidingWindow = new JCheckBox("Use Sliding Window", false);
      
      mSlideBox = Box.createVerticalBox();
      mWinSize = new JTextField(20);
      mShiftIncr = new JTextField(20);
      
      mDisplay = new JTextArea();

      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(prepareParamBox());
      add(prepareDisplayArea());
   }
   
   protected Box prepareParamBox() {
      Box controlBox = Box.createVerticalBox();
      Box startBox = Box.createHorizontalBox();
      Box endBox = Box.createHorizontalBox();

      Box windowBox = Box.createHorizontalBox();
      Box shiftBox = Box.createHorizontalBox();

      startBox.add(new JLabel("Start Position:"));
      startBox.add(mStartPos);

      endBox.add(new JLabel("End Position:"));
      endBox.add(mEndPos);

      mUseSlidingWindow.addItemListener(slidingWindowActionListener);

      windowBox.add(new JLabel("Window Size"));
      windowBox.add(mWinSize);

      shiftBox.add(new JLabel("Window Shift"));
      shiftBox.add(mShiftIncr);

      mSlideBox.add(windowBox);
      mSlideBox.add(shiftBox);
      mSlideBox.setVisible(false);

      controlBox.add(startBox);
      controlBox.add(endBox);
      controlBox.add(mUseSlidingWindow);
      controlBox.add(mSlideBox);

      return controlBox;
   }
   
   protected JScrollPane prepareDisplayArea() {
      JScrollPane scrollDisplay = new JScrollPane(mDisplay);
      // Very big, to avoid weird packing stuff.
      scrollDisplay.setPreferredSize(new Dimension(5000, 5000));
      return scrollDisplay;
   }
   
   protected ItemListener slidingWindowActionListener = new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
         switch (e.getStateChange()) {
         case ItemEvent.SELECTED:
            mSlideBox.setVisible(true);
            break;
         case ItemEvent.DESELECTED:
            mSlideBox.setVisible(false);
            break;
         }
      }
   };
   
   // Getters
   public String getStartPos() { return mStartPos.getText(); }
   public String getEndPos() { return mEndPos.getText(); }
   public boolean getUseSlidingWindow() { return mUseSlidingWindow.isSelected(); }
   public String getWinSize() { return mWinSize.getText(); }
   public String getShiftIncr() { return mShiftIncr.getText(); }
   public String getDisplay() { return mDisplay.getText(); }
   
   // Setters
   public void setDisplay(String s) { mDisplay.setText(s); }
}
