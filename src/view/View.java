package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.GFFParser.ParseException;

import controller.Controller;

@SuppressWarnings("serial")
public class View extends JDialog {
   protected final int DIALOG_HEIGHT = 400, DIALOG_WIDTH = 500;

   // Selected tab "enum"
   protected final int GC_CONTENT_TAB = 0, CALCULATIONS_TAB = 1, PROTEINS_TAB = 2, FIND_REPEATS_TAB = 3, FIND_MRNA_TAB = 4, SUPER_FILES_TAB = 5;

   protected Controller controller;

   protected Container mPane;

   protected Box mSequenceFileBox;
   protected JTextField mSequenceFile;
   protected boolean mValidSequenceFile;
   protected JButton mBrowseSequenceButton;

   protected Box mGffFileBox;
   protected JTextField mGffFile;
   protected boolean mValidGffFile;
   protected JButton mBrowseGffButton;
   
   protected Box mSequenceZipBox;
   protected boolean  mValidSequenceZip;
   protected JTextField mSequenceZip;
   protected JButton mBrowseSequenceZipButton;

   protected Box mGFFZipBox;
   protected boolean mValidGFFZip;
   protected JTextField mGFFZip;
   protected JButton mBrowseGFFZipButton;

   protected JTabbedPane mTabbedPane;
   protected GCContentInfoPanel mGcContentInfoPanel;
   protected CalculationsPanel mCalculationsPanel;
   protected ProteinsPanel mProteinsPanel;
   protected volatile FindRepeatsPanel mFindRepeatsPanel;
   protected FindMRNAPanel mFindMRNAPanel;
   protected SuperFilesPanel mSuperFilesPanel;

   protected JButton mRunButton, mSaveButton, mQuitButton;

   public View(final Controller controller) {
      this.controller = controller;

      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
      setResizable(false);
      setLocationRelativeTo(null);

      mPane = getContentPane();
      mPane.setLayout(new BoxLayout(mPane, BoxLayout.Y_AXIS));
      mPane.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

      // Took this out of initializeControlsBox() for now, since the tabbed pane
      // change listener needs to update it. Will refactor later.
      mRunButton = new JButton("Run");

      initializeSequenceBox(); // Box is member variable (for hiding)
      initializeGffBox(); // Box is member variable (for hiding)
      initializeGFFZipBox();
      initializeSequenceZipBox();
      initializeTabbedPane(); // Tabbed pane is member variable
      Box controlsBox = initializeControlsBox();

      mPane.add(mSequenceFileBox);
      mPane.add(mGffFileBox);
      mPane.add(mSequenceZipBox);
      mPane.add(mGFFZipBox);
      mPane.add(mTabbedPane);
      mPane.add(controlsBox);
      mPane.validate();
      this.setSize(600, 600);
   }

   protected void initializeSequenceBox() {
      mSequenceFile = new JTextField(20);
      mSequenceFile.setEditable(false);
      mValidSequenceFile = false;

      mBrowseSequenceButton = new JButton("Browse");
      mBrowseSequenceButton.addActionListener(browseSequenceButtonActionListener);

      mSequenceFileBox = Box.createHorizontalBox();
      mSequenceFileBox.add(new JLabel("Sequence File:"));
      mSequenceFileBox.add(mSequenceFile);
      mSequenceFileBox.add(mBrowseSequenceButton);
   }

   protected void initializeGffBox() {
      mGffFile = new JTextField(20);
      mGffFile.setEditable(false);
      mValidGffFile = false;

      mBrowseGffButton = new JButton("Browse");
      mBrowseGffButton.addActionListener(browseGffButtonActionListener);

      mGffFileBox = Box.createHorizontalBox();
      mGffFileBox.add(new JLabel("GFF File:"));
      mGffFileBox.add(mGffFile);
      mGffFileBox.add(mBrowseGffButton);
      mGffFileBox.setVisible(false);
   }

   protected void initializeSequenceZipBox() {
      mSequenceZip = new JTextField(20);
      mSequenceZip.setEditable(false);
      mValidSequenceZip = false;

      mBrowseSequenceZipButton = new JButton("Browse");
      mBrowseSequenceZipButton.addActionListener(browseSequenceZipButtonActionListener);

      mSequenceZipBox = Box.createHorizontalBox();
      mSequenceZipBox.add(new JLabel("Sequence Zip:"));
      mSequenceZipBox.add(mSequenceZip);
      mSequenceZipBox.add(mBrowseSequenceZipButton);
   }

   protected void initializeGFFZipBox() {
      mGFFZip = new JTextField(20);
      mGFFZip.setEditable(false);
      mValidGFFZip = false;
      
      mBrowseGFFZipButton = new JButton("Browse");
      mBrowseGFFZipButton.addActionListener(browseGffZipButtonActionListener);
      
      mGFFZipBox = Box.createHorizontalBox();
      mGFFZipBox.add(new JLabel("GFF Zip:"));
      mGFFZipBox.add(mGFFZip);
      mGFFZipBox.add(mBrowseGFFZipButton);
      mGFFZipBox.setVisible(false);
   }
   protected void initializeTabbedPane() {
      mGcContentInfoPanel = new GCContentInfoPanel();
      mCalculationsPanel = new CalculationsPanel();
      mProteinsPanel = new ProteinsPanel();
      mFindRepeatsPanel = new FindRepeatsPanel();
      mFindMRNAPanel = new FindMRNAPanel();
      mSuperFilesPanel = new SuperFilesPanel();

      mTabbedPane = new JTabbedPane();

      mTabbedPane.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            updateRunButton();
            switch (mTabbedPane.getSelectedIndex()) {
            case GC_CONTENT_TAB:
            case FIND_MRNA_TAB:
               mSequenceFileBox.setVisible(true);
               mGffFileBox.setVisible(false);
               mSequenceZipBox.setVisible(false);
               mGFFZipBox.setVisible(false);
               break;
            case FIND_REPEATS_TAB:
            case CALCULATIONS_TAB:
            case PROTEINS_TAB:
               mSequenceFileBox.setVisible(true);
               mGffFileBox.setVisible(true);
               mSequenceZipBox.setVisible(false);
               mGFFZipBox.setVisible(false);
               break;
            case SUPER_FILES_TAB:
               mSequenceFileBox.setVisible(false);
               mGffFileBox.setVisible(false);
               mSequenceZipBox.setVisible(true);
               mGFFZipBox.setVisible(true);
               break;
            default:
               assert false;
            }
         }
      });

      mTabbedPane.addTab("GC Content", mGcContentInfoPanel);
      mTabbedPane.addTab("Calculations", mCalculationsPanel);
      mTabbedPane.addTab("Proteins", mProteinsPanel);
      mTabbedPane.addTab("Find Repeats", mFindRepeatsPanel);
      mTabbedPane.addTab("Find miRNA", mFindMRNAPanel);
      mTabbedPane.addTab("Multiple Files", mSuperFilesPanel);
   }

   protected Box initializeControlsBox() {
      mRunButton.setEnabled(false);
      mRunButton.addActionListener(runButtonActionListener);

      mSaveButton = new JButton("Save");
      mSaveButton.addActionListener(saveButtonActionListener);

      mQuitButton = new JButton("Quit");
      mQuitButton.addActionListener(quitButtonActionListener);

      Box box = Box.createHorizontalBox();
      box.add(mRunButton);
      box.add(mSaveButton);
      box.add(mQuitButton);
      box.setAlignmentX(Component.CENTER_ALIGNMENT);

      return box;
   }

   /**
    * The Run button is enabled conditionally on valid Sequence/GFF files. 
    * GC Content: Requires valid sequence file
    * Calculations: Requires valid GFF file
    * Proteins: Requires both
    */
   protected void updateRunButton() {
      switch (mTabbedPane.getSelectedIndex()) {
      case GC_CONTENT_TAB:
      case FIND_MRNA_TAB:
         mRunButton.setEnabled(mValidSequenceFile);
         break;
      case FIND_REPEATS_TAB:
      case CALCULATIONS_TAB:
      case PROTEINS_TAB:
         mRunButton.setEnabled(mValidSequenceFile && mValidGffFile);
         break;
      case SUPER_FILES_TAB:
         mRunButton.setEnabled(mValidGFFZip && mValidSequenceZip);
      default:
         assert false;
      }
   }

   protected void sequenceStateUpdate() {
      try {
         controller.useSequenceFile(mSequenceFile.getText());
         mValidSequenceFile = true;
         updateRunButton();
      } catch (Exception ex) {
         ex.printStackTrace();
         JOptionPane.showMessageDialog(null, ex.toString(), "Error",
               JOptionPane.ERROR_MESSAGE);
         mValidSequenceFile = false;
         updateRunButton();
         return;
      }
   }

   protected void gffStateUpdate() {
      try {
         controller.useGffFile(mGffFile.getText());
         mValidGffFile = true;
         updateRunButton();
      } catch (IOException ex) {
         JOptionPane.showMessageDialog(null, ex.toString(), "Error",
               JOptionPane.ERROR_MESSAGE);
         mValidGffFile = false;
         updateRunButton();
         return;
      } catch (ParseException ex) {
         JOptionPane.showMessageDialog(null, ex.toString(), "Error",
               JOptionPane.ERROR_MESSAGE);
         mValidGffFile = false;
         updateRunButton();
         return;
      }
   }

   // Action listeners ////////////////////////////////////////////////////////

   protected ActionListener browseSequenceButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         JFileChooser chooser = new JFileChooser();
         int returnVal = chooser.showOpenDialog(chooser);
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            mSequenceFile.setText(filename);
            sequenceStateUpdate();
         }
      }
   };

   protected ActionListener browseGffButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         JFileChooser chooser = new JFileChooser();
         int returnVal = chooser.showOpenDialog(chooser);
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            mGffFile.setText(filename);
            gffStateUpdate();
            updateRunButton();
         }
      }
   };

   protected ActionListener browseGffZipButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         JFileChooser chooser = new JFileChooser();
         int returnVal = chooser.showOpenDialog(chooser);
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            mGFFZip.setText(filename);
            mValidGFFZip = true;
            updateRunButton();
         }
      }
   };
   
   protected ActionListener browseSequenceZipButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         JFileChooser chooser = new JFileChooser();
         int returnVal = chooser.showOpenDialog(chooser);
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            mSequenceZip.setText(filename);
            mValidSequenceZip = true;
         }
      }
   };
   
   
   protected ActionListener runButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         if (mTabbedPane.getSelectedIndex() != SUPER_FILES_TAB) {
            if (mSequenceFile.getText().equals("")) {
               JOptionPane.showMessageDialog(null,
                     "No FASTA file was selected", "Invalid File",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         switch (mTabbedPane.getSelectedIndex()) {
         case FIND_MRNA_TAB:
            runFindMRNA();
            break;
         case GC_CONTENT_TAB:
            runGCContent();
            break;
         case CALCULATIONS_TAB:
            runCalculations();
            break;
         case PROTEINS_TAB:
            runProteins();
            break;
         case FIND_REPEATS_TAB:
            runFindRepeats();
            break;
         case SUPER_FILES_TAB:
            runSuperFiles();
            break;
         default:
            assert false;
         }
      }
   };

   protected void runFindMRNA() {
      String nucleotideGapString = mFindMRNAPanel.getNucleotideGap();
      int nucleotideGap = -1;
      try {
         nucleotideGap = Integer.parseInt(nucleotideGapString);
      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(null,
               "Nucleotide gap must be a number.", "Error",
               JOptionPane.ERROR_MESSAGE);
         return;
      }
      // Find all indices of repeats that meat filter criteria.
      mFindMRNAPanel.setDisplay("Running...");
      Thread thread = new Thread(new FindMRNARunner(nucleotideGap));
      thread.start();
   }

   protected void runGCContent() {
      mGcContentInfoPanel.setDisplay(controller.getGcContent(mGcContentInfoPanel.getStartPos(),
            mGcContentInfoPanel.getEndPos(), mGcContentInfoPanel.getUseSlidingWindow(),
            mGcContentInfoPanel.getWinSize(), mGcContentInfoPanel.getShiftIncr()));

   }

   protected void runCalculations() {
      mCalculationsPanel.setNucleotides(controller.getNucleotides());
      mCalculationsPanel.setGenes(controller.getGenes());
      mCalculationsPanel.setIsoforms(controller.getIsoforms());
      mCalculationsPanel.setAvgGene(controller.avgGeneSize());
      mCalculationsPanel.setAvgCds(controller.avgCdsSize());
      mCalculationsPanel.setAvgExon(controller.avgExonSize());
      mCalculationsPanel.setAvgIntron(controller.avgIntronSize());
      mCalculationsPanel.setAvgIntergenic(controller.avgIntergenicRegionSize());
      mCalculationsPanel.setGeneDensity(controller.geneDensity());
      mCalculationsPanel.setCdsDensity(controller.cdsDensity());
      mCalculationsPanel.setGenesPerKilobase(controller.genesPerKilobase());
      mCalculationsPanel.setKilobasesPerGene(controller.kilobasesPerGene());
   }

   protected void runProteins() {
      mProteinsPanel.setDisplay(controller.getProteins());
   }

   /**
    * Hacky inner class for running the micro-RNA finder in another thread.
    */
   private class FindMRNARunner implements Runnable {
      int nucleotideGap;

      public FindMRNARunner(int nucleotideGap) {
         this.nucleotideGap = nucleotideGap;
      }

      public void run() {
         String output = controller.findMRNA(nucleotideGap);
         mFindMRNAPanel.setDisplay(output);
      }
   }

   /**
    * Hacky inner class for running string matching in a separate thread.
    */
   private class ExactStringMatchRunner implements Runnable {
      int maxDistanceFromStart;

      public ExactStringMatchRunner(int maxDistanceFromStart) {
         this.maxDistanceFromStart = maxDistanceFromStart;
      }

      public void run() {
         String output = controller.matchString(
               mFindRepeatsPanel.getMatchStringText(), maxDistanceFromStart);
         mFindRepeatsPanel.setDisplay(output);
      }
   }

   /**
    * Hacky inner class for running find repeats in a seperate thread.
    */
   private class FilterRepeatRunner implements Runnable {
      int maxDistanceFromStart;
      int minRepeatLength;

      public FilterRepeatRunner(int minRepeatLength, int maxDistanceFromStart) {
         this.maxDistanceFromStart = maxDistanceFromStart;
         this.minRepeatLength = minRepeatLength;
      }

      public void run() {
         String output = controller.getRepeats(minRepeatLength,
               maxDistanceFromStart);
         mFindRepeatsPanel.setDisplay(output);
      }
   }

   protected void runFindRepeats() {
      String maxDistanceFromStartText = mFindRepeatsPanel
            .getMaximumDistanceToStartText();
      int maxDistanceFromStart = 0;
      if (maxDistanceFromStartText.isEmpty()) {
         maxDistanceFromStart = -1;
      } else {
         try {
            maxDistanceFromStart = Integer.parseInt(maxDistanceFromStartText);
         } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                  "Max distance to next mRNA start must be a number.", "Error",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }
      }
      if (mFindRepeatsPanel.isMatchExactString()) {
         // Find incidences of exact string match
         mFindRepeatsPanel.setDisplay("Running...");
         Thread thread = new Thread(new ExactStringMatchRunner(
               maxDistanceFromStart));
         thread.start();
      } else {
         String minRepeatLengthText = mFindRepeatsPanel.getMinimumLengthText();
         int minRepeatLength = 0;
         if (!minRepeatLengthText.isEmpty()) {
            try {
               minRepeatLength = Integer.parseInt(minRepeatLengthText);
            } catch (NumberFormatException e) {
               JOptionPane.showMessageDialog(null,
                     "Minimum length must be a number.", "Error",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }
         }
         // Find all indices of repeats that meat filter criteria.
         mFindRepeatsPanel.setDisplay("Running...");
         Thread thread = new Thread(new FilterRepeatRunner(minRepeatLength, maxDistanceFromStart));
         thread.start();
      }
   }

   protected void runSuperFiles() {
      try {
         mSuperFilesPanel.setErrors(controller.runCreateSuperContigs(
               mSequenceZip.getText(), mGFFZip.getText()));
      } catch (IOException e) {
         JOptionPane.showMessageDialog(null,
               "Invalid zip file." + e, "Error",
               JOptionPane.ERROR_MESSAGE);
         return;
      }
   }

   protected ActionListener saveButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         switch (mTabbedPane.getSelectedIndex()) {
         case FIND_MRNA_TAB:
            saveMRNA();
            break;
         case GC_CONTENT_TAB:
            saveGcContent();
            break;
         case CALCULATIONS_TAB:
            saveCalculations();
            break;
         case PROTEINS_TAB:
            saveProteins();
            break;
         case FIND_REPEATS_TAB:
            saveRepeats();
            break;
         case SUPER_FILES_TAB:
            saveSuperFiles();
            break;
         default:
            assert false;
         }
      }
   };

   protected void saveGcContent() {
      saveString(mGcContentInfoPanel.getDisplay(), "Save GC Content");
   }

   protected void saveMRNA() {
      saveString(mFindMRNAPanel.getDisplay(), "Save MRNA");
   }

   protected void saveCalculations() {
      StringBuilder sb = new StringBuilder();
      
      for (String key : mCalculationsPanel.getKeys()) {
         sb.append(key);
         sb.append(",");
      }
      sb.append("\n");

      for (String value : mCalculationsPanel.getValues()) {
         sb.append(value);
         sb.append(",");
      }
      sb.append("\n");
      
      saveString(sb.toString(), "Save Calculations");
   }

   protected void saveProteins() {
      saveString(mProteinsPanel.getDisplay(), "Save Proteins");
   }
   protected void saveRepeats() {
      saveString(mFindRepeatsPanel.getDisplay(), "Save Repeats");
   }

   protected void saveSuperFiles() {
      // TODO: Complicated saving logic.
   }

   protected void saveString(String data, String title) {
      if (data.equals("")) {
         JOptionPane.showMessageDialog(null, "No output to save", "Empty output",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      JFileChooser chooser = new JFileChooser();
      chooser.setDialogTitle(title);
      int ret = chooser.showSaveDialog(mPane);

      if (ret == JFileChooser.APPROVE_OPTION) {
         try {
            FileWriter writer = new FileWriter(chooser.getSelectedFile());
            writer.write(data);
            writer.close();
         } catch (java.io.IOException ioErr) {
            JOptionPane.showMessageDialog(null, "Encountered unknown error when saving output",
                  "Unable to save output", JOptionPane.ERROR_MESSAGE);
         }
      } else if (ret == JFileChooser.ERROR_OPTION) {
         JOptionPane.showMessageDialog(null, "Encountered unknown error when saving output",
               "Unable to save output", JOptionPane.ERROR_MESSAGE);
      }
   }

   protected ActionListener quitButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         dispose(); // closes the dialog window
         return;
      }
   };
}
