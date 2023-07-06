/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import util.GUIUtil;
import dataset.DuplicatesType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.filechooser.FileFilter;
import workers.DuplicatesSwingWorkerTask;

/**
 *
 * @author ERNESTO
 */
public class FASTADuplicatesDialog extends javax.swing.JDialog implements ActionListener, PropertyChangeListener {

    File[] inputFiles;

    File outputFolder;

    public String TITLE = "Duplicates";

    private ProgressMonitor progressMonitor;

    private DuplicatesSwingWorkerTask task;

    /**
     * Creates new form NewJDialog
     */
    public FASTADuplicatesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        runButton.addActionListener(this);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width / 2 - getSize().width / 2) - 38, screen.height / 2 - getSize().height / 2);
    }

    void deleteInputFiles() {
        if (inputFiles != null) {
            inputFiles = null;
        }

        tf_InputFiles.setText("");
    }

    public void blockDuringTask(boolean status) {
        inputPanel.setEnabled(status);
        tf_InputFiles.setEnabled(status);
        inputBrowseButton.setEnabled(status);

        outputPanel.setEnabled(status);
        tf_OutputFiles.setEnabled(status);
        outputBrowseButton.setEnabled(status);

        cb_duplicatedByID.setEnabled(status);
        cb_duplicatedBySequence.setEnabled(status);

        runButton.setEnabled(status);
        cancelButton.setEnabled(status);
    }

    public boolean checkConfiguration() {
        return (inputFiles != null && outputFolder != null && outputFolder.exists() && getDuplicatesType() != null);
    }

    public DuplicatesType getDuplicatesType() {

        if (cb_duplicatedByID.isSelected() && !cb_duplicatedBySequence.isSelected()) {
            return DuplicatesType.FASTA_DUPLICATED_ID;
        } else if (!cb_duplicatedByID.isSelected() && cb_duplicatedBySequence.isSelected()) {
            return DuplicatesType.FASTA_DUPLICATED_SEQUENCE;
        } else {
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // setup the progress monitor

        progressMonitor = new ProgressMonitor(this,
                "",
                "", 0, 100);

        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(0);
        
        if (checkConfiguration()) {
            task = new DuplicatesSwingWorkerTask(inputFiles, outputFolder, getDuplicatesType());

            task.addPropertyChangeListener(this);

            task.execute();

            blockDuringTask(false);
        } else {

            if (getDuplicatesType() == null) {
                GUIUtil.showMessage(this, "Please, select at least one option to remove duplicates", TITLE, JOptionPane.WARNING_MESSAGE);
            } else if (inputFiles == null) {
                GUIUtil.showMessage(this, "Please, select at least one input file", TITLE, JOptionPane.WARNING_MESSAGE);

            } else if (outputFolder == null) {
                GUIUtil.showMessage(this, "Please, select an output folder", TITLE, JOptionPane.WARNING_MESSAGE);
            } else if (!outputFolder.exists()) {
                GUIUtil.showMessage(this, "The output folder: " + outputFolder.getPath() + "\n does not exist", TITLE, JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();

            progressMonitor.setProgress(progress);

            String message = String.format("Completed %d%%.\n", progress);

            progressMonitor.setNote(message);

            if (progressMonitor.isCanceled() || task.isDone()) {
                Toolkit.getDefaultToolkit().beep();

                if (progressMonitor.isCanceled()) {
                    task.cancel(true);
                }
                if (task.isDone()) {
                    blockDuringTask(true);
                }

                if (task.isDone() && !task.isCancelled() && task.getExceptionList().isEmpty()) {
                    GUIUtil.showMessage(this, "The process was succesfully finished!", TITLE, JOptionPane.INFORMATION_MESSAGE);

                } else if (!task.getExceptionList().isEmpty()) 
                {
                    ExceptionListDialog1 eld = new ExceptionListDialog1(this,task.getExceptionList());

                    eld.setVisible(true);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        inputPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tf_InputFiles = new javax.swing.JTextField();
        inputBrowseButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        outputPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tf_OutputFiles = new javax.swing.JTextField();
        outputBrowseButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        cb_duplicatedByID = new javax.swing.JCheckBox();
        cb_duplicatedBySequence = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FASTA Duplicates");
        setMinimumSize(new java.awt.Dimension(570, 310));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Input", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        inputPanel.setMaximumSize(new java.awt.Dimension(450, 80));
        inputPanel.setMinimumSize(new java.awt.Dimension(450, 80));
        inputPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        inputPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Files:");
        jLabel1.setMaximumSize(new java.awt.Dimension(34, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(34, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(34, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, -12, 0, 0);
        inputPanel.add(jLabel1, gridBagConstraints);

        tf_InputFiles.setEditable(false);
        tf_InputFiles.setMaximumSize(new java.awt.Dimension(400, 30));
        tf_InputFiles.setMinimumSize(new java.awt.Dimension(400, 30));
        tf_InputFiles.setPreferredSize(new java.awt.Dimension(400, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        inputPanel.add(tf_InputFiles, gridBagConstraints);

        inputBrowseButton.setText("Browse");
        inputBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputBrowseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        inputPanel.add(inputBrowseButton, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        inputPanel.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(inputPanel, gridBagConstraints);

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Output", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        outputPanel.setMaximumSize(new java.awt.Dimension(550, 80));
        outputPanel.setMinimumSize(new java.awt.Dimension(550, 80));
        outputPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        outputPanel.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Folder:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, -12, 0, 0);
        outputPanel.add(jLabel2, gridBagConstraints);

        tf_OutputFiles.setEditable(false);
        tf_OutputFiles.setMaximumSize(new java.awt.Dimension(400, 30));
        tf_OutputFiles.setMinimumSize(new java.awt.Dimension(400, 30));
        tf_OutputFiles.setPreferredSize(new java.awt.Dimension(400, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        outputPanel.add(tf_OutputFiles, gridBagConstraints);

        outputBrowseButton.setText("Browse");
        outputBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputBrowseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        outputPanel.add(outputBrowseButton, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        outputPanel.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(outputPanel, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Actions", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel6.setMaximumSize(new java.awt.Dimension(550, 70));
        jPanel6.setMinimumSize(new java.awt.Dimension(550, 70));
        jPanel6.setPreferredSize(new java.awt.Dimension(550, 70));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(cb_duplicatedByID);
        cb_duplicatedByID.setText("Remove sequences with duplicated ID (RDI)");
        cb_duplicatedByID.setMaximumSize(new java.awt.Dimension(270, 23));
        cb_duplicatedByID.setMinimumSize(new java.awt.Dimension(270, 23));
        cb_duplicatedByID.setPreferredSize(new java.awt.Dimension(270, 23));
        cb_duplicatedByID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_duplicatedByIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel6.add(cb_duplicatedByID, gridBagConstraints);

        buttonGroup1.add(cb_duplicatedBySequence);
        cb_duplicatedBySequence.setText("Remove duplicated sequences (RDS)");
        cb_duplicatedBySequence.setMaximumSize(new java.awt.Dimension(270, 23));
        cb_duplicatedBySequence.setMinimumSize(new java.awt.Dimension(270, 23));
        cb_duplicatedBySequence.setPreferredSize(new java.awt.Dimension(270, 23));
        cb_duplicatedBySequence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_duplicatedBySequenceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel6.add(cb_duplicatedBySequence, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel6, gridBagConstraints);

        jPanel1.setMaximumSize(new java.awt.Dimension(550, 40));
        jPanel1.setMinimumSize(new java.awt.Dimension(550, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 40));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        runButton.setText("Run!");
        runButton.setMaximumSize(new java.awt.Dimension(100, 23));
        runButton.setMinimumSize(new java.awt.Dimension(100, 23));
        runButton.setPreferredSize(new java.awt.Dimension(100, 23));
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel1.add(runButton, gridBagConstraints);

        cancelButton.setText("Close");
        cancelButton.setMaximumSize(new java.awt.Dimension(100, 23));
        cancelButton.setMinimumSize(new java.awt.Dimension(100, 23));
        cancelButton.setPreferredSize(new java.awt.Dimension(100, 23));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel1.add(cancelButton, gridBagConstraints);

        jPanel7.setPreferredSize(new java.awt.Dimension(345, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jPanel7, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(5, 10));
        jPanel2.setPreferredSize(new java.awt.Dimension(5, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBrowseButtonActionPerformed

        try {
            FileFilter[] filters = new FileFilter[]{GUIUtil.getCustomFileFilter(GUIUtil.FASTA_EXTENSION, GUIUtil.FASTA_DESCRIPTION)};

            inputFiles = new GUIUtil().loadFilesLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);

            if (inputFiles != null) {
                String dir = inputFiles[0].getParent() + ";";

                for (int i = 0; i < inputFiles.length; i++) {
                    dir += inputFiles[i].getName() + ";";
                }

                tf_InputFiles.setText(dir);
            } else {
                tf_InputFiles.setText("");
            }

        } catch (Exception ex) {
            Logger.getLogger(FASTADuplicatesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_inputBrowseButtonActionPerformed

    private void outputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputBrowseButtonActionPerformed
        Object[] info = new GUIUtil().loadFolderLastInputDirectory(this,false);

        int status = (int) info[0];

        if (status == JFileChooser.APPROVE_OPTION) {
            JFileChooser fileChooser = (JFileChooser) info[1];

            outputFolder = fileChooser.getSelectedFile();

            tf_OutputFiles.setText(outputFolder.getPath());
        }
    }//GEN-LAST:event_outputBrowseButtonActionPerformed

    private void cb_duplicatedByIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_duplicatedByIDActionPerformed

    }//GEN-LAST:event_cb_duplicatedByIDActionPerformed

    private void cb_duplicatedBySequenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_duplicatedBySequenceActionPerformed

    }//GEN-LAST:event_cb_duplicatedBySequenceActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed

    }//GEN-LAST:event_runButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FASTADuplicatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FASTADuplicatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FASTADuplicatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FASTADuplicatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FASTADuplicatesDialog dialog = new FASTADuplicatesDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox cb_duplicatedByID;
    private javax.swing.JCheckBox cb_duplicatedBySequence;
    private javax.swing.JButton inputBrowseButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton outputBrowseButton;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JButton runButton;
    private javax.swing.JTextField tf_InputFiles;
    private javax.swing.JTextField tf_OutputFiles;
    // End of variables declaration//GEN-END:variables
}
