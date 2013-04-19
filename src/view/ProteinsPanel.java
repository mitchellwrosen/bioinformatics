package view;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProteinsPanel extends JPanel {
   protected JTextArea mDisplay;
   
   public ProteinsPanel() {
      mDisplay = new JTextArea();
      
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(prepareDisplayArea());
   }
   
   protected JScrollPane prepareDisplayArea() {
      JScrollPane scrollDisplay = new JScrollPane(mDisplay);
      scrollDisplay.setPreferredSize(new Dimension(200, 300));
      return scrollDisplay;
   }
}
