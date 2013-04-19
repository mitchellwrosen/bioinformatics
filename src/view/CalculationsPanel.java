package view;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class CalculationsPanel extends JPanel {
   protected final int TF_SIZE = 40;
   protected JTextField mAvgGene, mAvgCds, mAvgExon, mAvgIntron, mAvgIntergenic;
   
   public CalculationsPanel() {
      mAvgGene = prepareTextField(TF_SIZE);
      mAvgCds = prepareTextField(TF_SIZE);
      mAvgExon = prepareTextField(TF_SIZE);
      mAvgIntron = prepareTextField(TF_SIZE);
      mAvgIntergenic = prepareTextField(TF_SIZE);
      
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(prepareBox("Average gene size:", mAvgGene));
      add(prepareBox("Average CDS size:", mAvgCds));
      add(prepareBox("Average exon size:", mAvgExon));
      add(prepareBox("Average intron size:", mAvgIntron));
      add(prepareBox("Average intergenic regeion size:", mAvgIntergenic));
   }
   
   protected JTextField prepareTextField(int size) {
      JTextField tf = new JTextField(size);
      tf.setMaximumSize(tf.getPreferredSize());
      return tf;
   }
   
   protected Box prepareBox(String label, JTextField textField) {
      Box box = Box.createHorizontalBox();
      box.setAlignmentX(RIGHT_ALIGNMENT);
      box.add(new JLabel(label));
      box.add(textField);
      return box;
   }
}
