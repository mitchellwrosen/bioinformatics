package view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalculationsPanel extends JPanel {
   protected JTextField mNucleotides, mGenes, mIsoforms, mAvgGene, mAvgCds, mAvgExon, 
         mAvgIntron, mAvgIntergenic, mCdsDensity, mGenesPerKilobase, mKilobasesPerGene;
   
   public CalculationsPanel() {
      mNucleotides = prepareTextField(40);
      mGenes = prepareTextField(40);
      mIsoforms = prepareTextField(40);
      
      mAvgGene = prepareTextField(40);
      mAvgCds = prepareTextField(40);
      mAvgExon = prepareTextField(40);
      mAvgIntron = prepareTextField(40);
      mAvgIntergenic = prepareTextField(40);
      mCdsDensity = prepareTextField(40);
      mGenesPerKilobase = prepareTextField(40);
      mKilobasesPerGene = prepareTextField(40);
      
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(prepareBox("Nucleotides:", mNucleotides));
      add(prepareBox("Genes:", mGenes));
      add(prepareBox("Isoforms:", mIsoforms));
      add(Box.createVerticalStrut(20));
      add(prepareBox("Average gene size:", mAvgGene));
      add(prepareBox("Average CDS size:", mAvgCds));
      add(prepareBox("Average exon size:", mAvgExon));
      add(prepareBox("Average intron size:", mAvgIntron));
      add(prepareBox("Average intergenic region size:", mAvgIntergenic));
      add(prepareBox("CDS density:", mCdsDensity));
      add(prepareBox("Genes per kilobase:", mGenesPerKilobase));
      add(prepareBox("Kilobases per gene:", mKilobasesPerGene));
   }
   
   public String[] getKeys() {
      return new String[]{"nucleotides", "genes", "isoforms", "avg gene size", "avg cds size", 
            "avg exon size", "avg intron size", "avg intergenic region size", "cds density", 
            "genes per kilobase", "kilobases per gene"};
   }
   
   public String[] getValues() {
      return new String[]{mNucleotides.getText(), mGenes.getText(), mIsoforms.getText(),
            mAvgGene.getText(), mAvgCds.getText(), mAvgExon.getText(), mAvgIntron.getText(), 
            mAvgIntergenic.getText(), mCdsDensity.getText(), mGenesPerKilobase.getText(),
            mKilobasesPerGene.getText()};
   }
   
   public void setNucleotides(String text)      { mNucleotides.setText(text); }
   public void setGenes(String text)            { mGenes.setText(text); }
   public void setIsoforms(String text)         { mIsoforms.setText(text); }
   public void setAvgGene(String text)          { mAvgGene.setText(text); }
   public void setAvgCds(String text)           { mAvgCds.setText(text); }
   public void setAvgExon(String text)          { mAvgExon.setText(text); }
   public void setAvgIntron(String text)        { mAvgIntron.setText(text); }
   public void setAvgIntergenic(String text)    { mAvgIntergenic.setText(text); }
   public void setCdsDensity(String text)       { mCdsDensity.setText(text); }
   public void setGenesPerKilobase(String text) { mGenesPerKilobase.setText(text); }
   public void setKilobasesPerGene(String text) { mKilobasesPerGene.setText(text); }
   
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
