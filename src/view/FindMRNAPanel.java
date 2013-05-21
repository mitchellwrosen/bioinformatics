package view;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FindMRNAPanel extends JPanel {
   protected JTextArea mDisplay;
   
   public FindMRNAPanel() {
      mDisplay = new JTextArea();
      
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(prepareDisplayArea());
   }
   
   protected JScrollPane prepareDisplayArea() {
      JScrollPane scrollDisplay = new JScrollPane(mDisplay);
      scrollDisplay.setPreferredSize(new Dimension(200, 300));
      return scrollDisplay;
   }
   
   public String getDisplay() { return mDisplay.getText(); }
   
   public void setDisplay(String text) { mDisplay.setText(text); }
}
