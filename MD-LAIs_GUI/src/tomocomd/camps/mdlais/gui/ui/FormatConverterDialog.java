/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import dataset.FormatConverterType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.filechooser.FileFilter;
import util.GUIUtil;
import workers.FormatConverterSwingWorkerTask;

/**
 *
 * @author ERNESTO
 */
public class FormatConverterDialog extends javax.swing.JDialog implements ActionListener, PropertyChangeListener {

    File[] inputFiles;

    File outputFolder;

    private FormatConverterType fct;

    private String plainTextExtension;

    private final String TITLE = "Dataset Format Converter";

    private ProgressMonitor progressMonitor;

    private FormatConverterSwingWorkerTask task;

    /**
     * Creates new form NewJDialog
     */
    public FormatConverterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        convertButton.addActionListener(this);
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width / 2 - getSize().width / 2) - 38, screen.height / 2 - getSize().height / 2);
        
    }

    void deleteInputFiles() {
        if (inputFiles != null) {
            inputFiles = null;
        }

        tf_InputFiles.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // setup the progress monitor

        progressMonitor = new ProgressMonitor(FormatConverterDialog.this,
                "",
                "", 0, 100);

        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(0);

        if (inputFiles != null && outputFolder != null && outputFolder.exists()) {
            Object[] params = new Object[]{fct, plainTextExtension};

            task = new FormatConverterSwingWorkerTask(inputFiles, outputFolder, params);

            task.addPropertyChangeListener(this);

            task.execute();

            blockDuringTask(false);

        } else if (inputFiles == null) {
            GUIUtil.showMessage(this, "Please, select at least one input file", TITLE, JOptionPane.WARNING_MESSAGE);
        } else if (outputFolder == null) {
            GUIUtil.showMessage(this, "Please, define an output folder", TITLE, JOptionPane.WARNING_MESSAGE);
        } else if (!outputFolder.exists()) {
            GUIUtil.showMessage(this, "The output folder: " + outputFolder.getPath() + "\n does not exist", TITLE, JOptionPane.WARNING_MESSAGE);
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
                blockDuringTask(true);

                if (progressMonitor.isCanceled()) {
                    task.cancel(true);
                    GUIUtil.showMessage(this, "The process was cancelled", TITLE, JOptionPane.INFORMATION_MESSAGE);
                } else if (task.getExceptionList().isEmpty()) {
                    GUIUtil.showMessage(this, "The process was succesfully finished!", TITLE, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    ExceptionListDialog1 eld = new ExceptionListDialog1(this,task.getExceptionList());

                    eld.setVisible(true);
                }
            }
        }
    }

    public void blockDuringTask(boolean status) {
        inputPanel.setEnabled(status);
        tf_InputFiles.setEnabled(status);
        inputBrowseButton.setEnabled(status);

        outputPanel.setEnabled(status);
        tf_OutputFiles.setEnabled(status);
        outputBrowseButton.setEnabled(status);

        rb_Fasta2Multifasta.setEnabled(status);
        rb_Multifasta2Fasta.setEnabled(status);
        rb_PDB2Fasta.setEnabled(status);
        rb_PDB2Multifasta.setEnabled(status);

        rb_multiFasta2plainText.setEnabled(status);
        rb_plainText2MultiFasta.setEnabled(status);

        convertButton.setEnabled(status);
        cancelButton.setEnabled(status);
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
        rb_PDB2Fasta = new javax.swing.JRadioButton();
        rb_Fasta2Multifasta = new javax.swing.JRadioButton();
        rb_PDB2Multifasta = new javax.swing.JRadioButton();
        rb_Multifasta2Fasta = new javax.swing.JRadioButton();
        rb_plainText2MultiFasta = new javax.swing.JRadioButton();
        rb_multiFasta2plainText = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        convertButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(600, 300));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.inputPanel.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        inputPanel.setMaximumSize(new java.awt.Dimension(550, 80));
        inputPanel.setMinimumSize(new java.awt.Dimension(550, 80));
        inputPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        inputPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.jLabel1.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inputBrowseButton, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.inputBrowseButton.text")); // NOI18N
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
        gridBagConstraints.gridy = 1;
        getContentPane().add(inputPanel, gridBagConstraints);

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.outputPanel.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        outputPanel.setMaximumSize(new java.awt.Dimension(550, 80));
        outputPanel.setMinimumSize(new java.awt.Dimension(550, 80));
        outputPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        outputPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.jLabel2.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(outputBrowseButton, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.outputBrowseButton.text")); // NOI18N
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
        getContentPane().add(outputPanel, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.jPanel6.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        jPanel6.setMaximumSize(new java.awt.Dimension(550, 70));
        jPanel6.setMinimumSize(new java.awt.Dimension(550, 70));
        jPanel6.setPreferredSize(new java.awt.Dimension(550, 70));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(rb_PDB2Fasta);
        org.openide.awt.Mnemonics.setLocalizedText(rb_PDB2Fasta, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.rb_PDB2Fasta.text")); // NOI18N
        rb_PDB2Fasta.setMaximumSize(new java.awt.Dimension(160, 23));
        rb_PDB2Fasta.setMinimumSize(new java.awt.Dimension(160, 23));
        rb_PDB2Fasta.setPreferredSize(new java.awt.Dimension(160, 23));
        rb_PDB2Fasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_PDB2FastaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel6.add(rb_PDB2Fasta, gridBagConstraints);

        buttonGroup1.add(rb_Fasta2Multifasta);
        rb_Fasta2Multifasta.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(rb_Fasta2Multifasta, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.rb_Fasta2Multifasta.text")); // NOI18N
        rb_Fasta2Multifasta.setMaximumSize(new java.awt.Dimension(160, 23));
        rb_Fasta2Multifasta.setMinimumSize(new java.awt.Dimension(160, 23));
        rb_Fasta2Multifasta.setPreferredSize(new java.awt.Dimension(160, 23));
        rb_Fasta2Multifasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_Fasta2MultifastaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel6.add(rb_Fasta2Multifasta, gridBagConstraints);

        buttonGroup1.add(rb_PDB2Multifasta);
        org.openide.awt.Mnemonics.setLocalizedText(rb_PDB2Multifasta, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.rb_PDB2Multifasta.text")); // NOI18N
        rb_PDB2Multifasta.setMaximumSize(new java.awt.Dimension(160, 23));
        rb_PDB2Multifasta.setMinimumSize(new java.awt.Dimension(160, 23));
        rb_PDB2Multifasta.setPreferredSize(new java.awt.Dimension(160, 23));
        rb_PDB2Multifasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_PDB2MultifastaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel6.add(rb_PDB2Multifasta, gridBagConstraints);

        buttonGroup1.add(rb_Multifasta2Fasta);
        org.openide.awt.Mnemonics.setLocalizedText(rb_Multifasta2Fasta, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.rb_Multifasta2Fasta.text")); // NOI18N
        rb_Multifasta2Fasta.setMaximumSize(new java.awt.Dimension(160, 23));
        rb_Multifasta2Fasta.setMinimumSize(new java.awt.Dimension(160, 23));
        rb_Multifasta2Fasta.setPreferredSize(new java.awt.Dimension(160, 23));
        rb_Multifasta2Fasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_Multifasta2FastaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel6.add(rb_Multifasta2Fasta, gridBagConstraints);

        buttonGroup1.add(rb_plainText2MultiFasta);
        org.openide.awt.Mnemonics.setLocalizedText(rb_plainText2MultiFasta, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.rb_plainText2MultiFasta.text")); // NOI18N
        rb_plainText2MultiFasta.setMaximumSize(new java.awt.Dimension(160, 23));
        rb_plainText2MultiFasta.setMinimumSize(new java.awt.Dimension(160, 23));
        rb_plainText2MultiFasta.setPreferredSize(new java.awt.Dimension(160, 23));
        rb_plainText2MultiFasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_plainText2MultiFastaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel6.add(rb_plainText2MultiFasta, gridBagConstraints);

        buttonGroup1.add(rb_multiFasta2plainText);
        org.openide.awt.Mnemonics.setLocalizedText(rb_multiFasta2plainText, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.rb_multiFasta2plainText.text")); // NOI18N
        rb_multiFasta2plainText.setMaximumSize(new java.awt.Dimension(160, 23));
        rb_multiFasta2plainText.setMinimumSize(new java.awt.Dimension(160, 23));
        rb_multiFasta2plainText.setPreferredSize(new java.awt.Dimension(160, 23));
        rb_multiFasta2plainText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_multiFasta2plainTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel6.add(rb_multiFasta2plainText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel6, gridBagConstraints);

        jPanel1.setMaximumSize(new java.awt.Dimension(550, 40));
        jPanel1.setMinimumSize(new java.awt.Dimension(550, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 40));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(convertButton, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.convertButton.text")); // NOI18N
        convertButton.setMaximumSize(new java.awt.Dimension(100, 23));
        convertButton.setMinimumSize(new java.awt.Dimension(100, 23));
        convertButton.setPreferredSize(new java.awt.Dimension(100, 23));
        convertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel1.add(convertButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(FormatConverterDialog.class, "FormatConverterDialog.cancelButton.text")); // NOI18N
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

        jPanel2.setMaximumSize(new java.awt.Dimension(200, 10));
        jPanel2.setMinimumSize(new java.awt.Dimension(200, 10));
        jPanel2.setPreferredSize(new java.awt.Dimension(340, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBrowseButtonActionPerformed

        FileFilter[] filters = null;

        if (rb_Fasta2Multifasta.isSelected() || rb_Multifasta2Fasta.isSelected()) {
            filters = new FileFilter[]{GUIUtil.getCustomFileFilter(GUIUtil.FASTA_EXTENSION, GUIUtil.FASTA_DESCRIPTION)};

            inputFiles = new GUIUtil().loadFilesLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);

        }
        if (rb_PDB2Fasta.isSelected() || rb_PDB2Multifasta.isSelected()) {
            filters = new FileFilter[]{GUIUtil.getCustomFileFilter(GUIUtil.PDB_EXTENSION, GUIUtil.PDB_DESCRIPTION)};

            inputFiles = new GUIUtil().loadFilesLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);
        }

        JFileChooser chooser = null;

        if (rb_plainText2MultiFasta.isSelected()) {
            filters = new FileFilter[]{GUIUtil.getCustomFileFilter(GUIUtil.PLAIN_TEXT_EXTENSION, GUIUtil.PLAIN_TEXT_DESCRIPTION),
                GUIUtil.getCustomFileFilter(GUIUtil.CSV_EXTENSION, GUIUtil.CSV_DESCRIPTION)};

            chooser = new GUIUtil().loadFilesandFileChooserLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);

            if (chooser != null) {
                inputFiles = chooser.getSelectedFiles();

                String desc = chooser.getFileFilter().getDescription();

                if (desc.equals(GUIUtil.PLAIN_TEXT_DESCRIPTION)) {
                    plainTextExtension = ".txt";
                }

                if (desc.equals(GUIUtil.CSV_DESCRIPTION)) {
                    plainTextExtension = ".csv";
                }
            }
        }

        if (rb_multiFasta2plainText.isSelected()) {
            filters = new FileFilter[]{GUIUtil.getCustomFileFilter(GUIUtil.FASTA_EXTENSION, GUIUtil.FASTA_DESCRIPTION)};

            chooser = new GUIUtil().loadFilesandFileChooserLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);

            if (chooser != null) {
                inputFiles = chooser.getSelectedFiles();

                plainTextExtension = ".csv";
            }
        }

        if (inputFiles != null) {
            String dir = inputFiles[0].getParent() + ";";

            for (int i = 0; i < inputFiles.length; i++) {
                dir += inputFiles[i].getName() + ";";
            }

            tf_InputFiles.setText(dir);
        }

        if (rb_Fasta2Multifasta.isSelected()) {
            fct = FormatConverterType.FASTA2MULTIFASTA;
        }

        if (rb_Multifasta2Fasta.isSelected()) {
            fct = FormatConverterType.MULTIFASTA2FASTA;
        }

        if (rb_PDB2Fasta.isSelected()) {
            fct = FormatConverterType.PDB2FASTA;
        }

        if (rb_PDB2Multifasta.isSelected()) {
            fct = FormatConverterType.PDB2MULTIFASTA;
        }

        if (rb_multiFasta2plainText.isSelected()) {
            fct = FormatConverterType.PLAINTEXT2FASTA;
        }

        if (rb_plainText2MultiFasta.isSelected()) {
            fct = FormatConverterType.MULTIFASTA2PLAINTEXT;
        }
    }//GEN-LAST:event_inputBrowseButtonActionPerformed

    private void outputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputBrowseButtonActionPerformed
        Object[] info = new GUIUtil().loadFolderLastInputDirectory(this, false);

        int status = (int) info[0];

        if (status == JFileChooser.APPROVE_OPTION) {
            JFileChooser fileChooser = (JFileChooser) info[1];

            outputFolder = fileChooser.getSelectedFile();

            tf_OutputFiles.setText(outputFolder.getPath());
        }
    }//GEN-LAST:event_outputBrowseButtonActionPerformed

    private void rb_PDB2FastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_PDB2FastaActionPerformed
        deleteInputFiles();

        fct = FormatConverterType.PDB2FASTA;
    }//GEN-LAST:event_rb_PDB2FastaActionPerformed

    private void rb_Fasta2MultifastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_Fasta2MultifastaActionPerformed

        deleteInputFiles();

        fct = FormatConverterType.FASTA2MULTIFASTA;
    }//GEN-LAST:event_rb_Fasta2MultifastaActionPerformed

    private void rb_PDB2MultifastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_PDB2MultifastaActionPerformed
        deleteInputFiles();
        fct = FormatConverterType.PDB2MULTIFASTA;
    }//GEN-LAST:event_rb_PDB2MultifastaActionPerformed

    private void rb_Multifasta2FastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_Multifasta2FastaActionPerformed
        deleteInputFiles();
        fct = FormatConverterType.MULTIFASTA2FASTA;
    }//GEN-LAST:event_rb_Multifasta2FastaActionPerformed

    private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed

    }//GEN-LAST:event_convertButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void rb_plainText2MultiFastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_plainText2MultiFastaActionPerformed
        deleteInputFiles();
        fct = FormatConverterType.MULTIFASTA2PLAINTEXT;
    }//GEN-LAST:event_rb_plainText2MultiFastaActionPerformed

    private void rb_multiFasta2plainTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_multiFasta2plainTextActionPerformed
        deleteInputFiles();
        fct = FormatConverterType.MULTIFASTA2PLAINTEXT;
    }//GEN-LAST:event_rb_multiFasta2plainTextActionPerformed

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
            java.util.logging.Logger.getLogger(FormatConverterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormatConverterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormatConverterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormatConverterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                FormatConverterDialog dialog = new FormatConverterDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton convertButton;
    private javax.swing.JButton inputBrowseButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton outputBrowseButton;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JRadioButton rb_Fasta2Multifasta;
    private javax.swing.JRadioButton rb_Multifasta2Fasta;
    private javax.swing.JRadioButton rb_PDB2Fasta;
    private javax.swing.JRadioButton rb_PDB2Multifasta;
    private javax.swing.JRadioButton rb_multiFasta2plainText;
    private javax.swing.JRadioButton rb_plainText2MultiFasta;
    private javax.swing.JTextField tf_InputFiles;
    private javax.swing.JTextField tf_OutputFiles;
    // End of variables declaration//GEN-END:variables
}
