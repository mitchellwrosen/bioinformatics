package view;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SuperFilesPanel extends JPanel {
   protected JTextArea mErrors;
   
   public SuperFilesPanel() {
      mErrors = new JTextArea();
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.add(prepareDisplayArea(mErrors));
   }
   
   protected JScrollPane prepareDisplayArea(JTextArea textArea) {
      JScrollPane scrollDisplay = new JScrollPane(textArea);
      scrollDisplay.setPreferredSize(new Dimension(200, 300));
      return scrollDisplay;
   }
   
   public String getErrors() { return mErrors.getText(); }
   
   public void setErrors(String text) { mErrors.setText(text); }
}
