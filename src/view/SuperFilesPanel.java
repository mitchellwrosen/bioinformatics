package view;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SuperFilesPanel extends JPanel {
   protected JTextArea mSuperContig;
   protected JTextArea mSuperGFF;
   
   public SuperFilesPanel() {
      mSuperContig = new JTextArea();
      mSuperGFF = new JTextArea();
      
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(prepareDisplayArea(mSuperContig));
      add(prepareDisplayArea(mSuperGFF));
   }
   
   protected JScrollPane prepareDisplayArea(JTextArea textArea) {
      JScrollPane scrollDisplay = new JScrollPane(textArea);
      scrollDisplay.setPreferredSize(new Dimension(200, 300));
      return scrollDisplay;
   }
   
   public String getSuperContig() { return mSuperContig.getText(); }
   
   public void setSuperContig(String text) { mSuperContig.setText(text); }

   public String getSuperGFF() { return mSuperGFF.getText(); }
   
   public void setSuperGFF(String text) { mSuperGFF.setText(text); }
}
