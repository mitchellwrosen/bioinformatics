package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import controller.Main;

@SuppressWarnings("serial")
public class InputDialog extends JDialog {
   protected final int DIALOG_HEIGHT = 400, DIALOG_WIDTH = 500;

   // Selected tab "enum"
   protected final int GC_CONTENT = 0, CALCULATIONS = 1, PROTEINS = 2;

   protected Container mPane;
   protected JTextField mFile;
   protected JButton mBrowseButton;

   protected JTabbedPane mTabbedPane;
   protected GCContentInfoPanel mGCContentInfoPanel;
   protected CalculationsPanel mCalculationsPanel;
   protected ProteinsPanel mProteinsPanel;

   protected JButton mRunButton, mSaveButton, mQuitButton;

   public InputDialog() {
      super();

      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
      setResizable(false);
      setLocationRelativeTo(null);

      mPane = getContentPane();
      mPane.setLayout(new BoxLayout(mPane, BoxLayout.Y_AXIS));
      mPane.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

      mTabbedPane = new JTabbedPane();
      mGCContentInfoPanel = new GCContentInfoPanel();
      mCalculationsPanel = new CalculationsPanel();
      mProteinsPanel = new ProteinsPanel();
      
      mRunButton = new JButton("Run");
      mSaveButton = new JButton("Save");
      mQuitButton = new JButton("Quit");
      mRunButton.addActionListener(runButtonActionListener);
      mSaveButton.addActionListener(saveButtonActionListener);
      mQuitButton.addActionListener(quitButtonActionListener);

      
      mPane.add(prepareFileBox());
      mPane.add(prepareTabbedPane());
      mPane.add(prepareControlsBox());
      mPane.validate();
   }

   protected Box prepareFileBox() {
      mFile = new JTextField(20);

      mBrowseButton = new JButton("Browse");
      mBrowseButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(chooser);

            if (returnVal == JFileChooser.CANCEL_OPTION) {
               System.out.println("cancelled");
            } else if (returnVal == JFileChooser.APPROVE_OPTION) {
               File fastaFile = chooser.getSelectedFile();
               mFile.setText(fastaFile.getAbsolutePath());
            } else {
               System.out.println("Encountered Unknown Error");
               System.exit(0);
            }
         }
      });

      Box fileBox = Box.createHorizontalBox();
      fileBox.add(new JLabel("Select Input File:"));
      fileBox.add(mFile);
      fileBox.add(mBrowseButton);
      return fileBox;
   }

   protected JTabbedPane prepareTabbedPane() {
      mTabbedPane.addTab("GC Content", mGCContentInfoPanel);
      mTabbedPane.addTab("Calculations", mCalculationsPanel);
      mTabbedPane.addTab("Proteins", mProteinsPanel);

      return mTabbedPane;
   }

   protected Box prepareControlsBox() {
      Box dialogControls = Box.createHorizontalBox();
      dialogControls.add(mRunButton);
      dialogControls.add(mSaveButton);
      dialogControls.add(mQuitButton);
      dialogControls.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      return dialogControls;
   }

   protected ActionListener runButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         if (mFile.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "No FASTA file was selected",
                  "Invalid File", JOptionPane.ERROR_MESSAGE);
            return;
         }

         switch (mTabbedPane.getSelectedIndex()) {
         case GC_CONTENT:
            runGCContent();
            break;
         case CALCULATIONS:
            runCalculations();
            break;
         case PROTEINS:
            runProteins();
            break;
         default:
            assert false;
         }
      }
   };

   protected void runGCContent() {
      String filename = mFile.getText();
      String startPos = mGCContentInfoPanel.getStartPos();
      String endPos = mGCContentInfoPanel.getEndPos();
      boolean useSlidingWindow = mGCContentInfoPanel.getUseSlidingWindow();
      String winSize = mGCContentInfoPanel.getWinSize();
      String shiftIncr = mGCContentInfoPanel.getShiftIncr();
      
      String output = Main.handleRun(filename, startPos, endPos, useSlidingWindow, winSize, shiftIncr);
      mGCContentInfoPanel.setDisplay(output);
   }

   protected void runCalculations() {
      // TODO
   }

   protected void runProteins() {
      // TODO
   }

   protected ActionListener saveButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         switch (mTabbedPane.getSelectedIndex()) {
         case GC_CONTENT:
            saveGCContent();
            break;
         case CALCULATIONS:
            saveCalculations();
            break;
         case PROTEINS:
            saveProteins();
            break;
         default:
            assert false;

         }
      }
   };

   protected void saveGCContent() {
      String display = mGCContentInfoPanel.getDisplay();

      if (display.equals("")) {
         JOptionPane.showMessageDialog(null, "No output to save",
               "Empty output", JOptionPane.ERROR_MESSAGE);
         return;
      }
      
      JFileChooser chooser = new JFileChooser();
      int ret = chooser.showSaveDialog(mPane);

      if (ret == JFileChooser.APPROVE_OPTION) {
         try {
            FileWriter writer = new FileWriter(chooser.getSelectedFile());
            writer.write(display);
            writer.close();
         } catch (java.io.IOException ioErr) {
            JOptionPane.showMessageDialog(null,
                  "Encountered unknown error when saving output",
                  "Unable to save output", JOptionPane.ERROR_MESSAGE);
         }
      } else if (ret == JFileChooser.ERROR_OPTION) {
         JOptionPane.showMessageDialog(null,
               "Encountered unknown error when saving output",
               "Unable to save output", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   protected void saveCalculations() {
      // TODO
   }
   
   protected void saveProteins() {
      // TODO
   }

   protected ActionListener quitButtonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         dispose(); // closes the dialog window
         return;
      }
   };
}
