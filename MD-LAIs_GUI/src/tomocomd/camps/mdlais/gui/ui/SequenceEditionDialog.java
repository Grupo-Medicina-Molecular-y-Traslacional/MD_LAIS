/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import dataset.SequenceEditionType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.filechooser.FileFilter;
import util.GUIUtil;
import workers.SequenceEditionSwingWorkerTask;

/**
 *
 * @author econtreras
 */
public class SequenceEditionDialog extends javax.swing.JDialog implements ActionListener, PropertyChangeListener {

    File[] inputFiles;

    File outputFolder;

    private final String TITLE = "Sequence Edition";

    private ProgressMonitor progressMonitor;

    private SequenceEditionSwingWorkerTask task;

    /**
     * Creates new form NewJDialog
     */
    public SequenceEditionDialog(java.awt.Frame parent, boolean modal) {
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

        progressMonitor = new ProgressMonitor(this,
                "",
                "", 0, 100);

        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(0);
        
        if (inputFiles != null && outputFolder != null && outputFolder.exists()
            &&(cb_nterminal.isSelected()||cb_cterminal.isSelected()
            ||cb_middle.isSelected()||cb_ncterminal.isSelected())) 
        {
            Object[] params = new Object[]{getSequenceEditionList(), getLengthList()};

            task = new SequenceEditionSwingWorkerTask(inputFiles, outputFolder, params);

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
        else if (!cb_nterminal.isSelected()&&!cb_cterminal.isSelected()
            &&!cb_middle.isSelected()&&!cb_ncterminal.isSelected()) 
        {
            GUIUtil.showMessage(this, "Please, select at least one option", TITLE, JOptionPane.WARNING_MESSAGE);
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
                    ExceptionListDialog1 eld = new ExceptionListDialog1(this, task.getExceptionList());

                    eld.setVisible(true);
                }
            }
        }
    }

    public void blockDuringTask(boolean status) {
        inputPanel.setEnabled(status);
        tf_InputFiles.setEnabled(status);
        inputBrowseButton.setEnabled(status);

        cb_nterminal.setEnabled(status);
        cb_cterminal.setEnabled(status);
        cb_ncterminal.setEnabled(status);
        cb_middle.setEnabled(status);
        cmb_nterminal.setEnabled(status);
        cmb_cterminal.setEnabled(status);
        cmb_ncterminal.setEnabled(status);
        cmb_middle.setEnabled(status);
        
        outputPanel.setEnabled(status);
        tf_OutputFiles.setEnabled(status);
        outputBrowseButton.setEnabled(status);

        convertButton.setEnabled(status);
        cancelButton.setEnabled(status);
    }
    
    List<SequenceEditionType> getSequenceEditionList()
    {
        List<SequenceEditionType> sequenceEditionList = new LinkedList<>();
        
        if(cb_nterminal.isSelected())
        {
          sequenceEditionList.add(SequenceEditionType.N);
        }
        
        if(cb_cterminal.isSelected())
        {
          sequenceEditionList.add(SequenceEditionType.C);
        }
        
        if(cb_ncterminal.isSelected())
        {
          sequenceEditionList.add(SequenceEditionType.NC);
        }
        if(cb_middle.isSelected())
        {
          sequenceEditionList.add(SequenceEditionType.M);
        }
        
        return sequenceEditionList;
    }
    
    List<Integer> getLengthList()
    {
        List<Integer> lengthList = new LinkedList<>();
        
        if(cb_nterminal.isSelected())
        {
          lengthList.add(Integer.parseInt(cmb_nterminal.getSelectedItem().toString()));
        }
        
        if(cb_cterminal.isSelected())
        {
          lengthList.add(Integer.parseInt(cmb_cterminal.getSelectedItem().toString()));
        }
        
        if(cb_cterminal.isSelected())
        {
          lengthList.add(Integer.parseInt(cmb_ncterminal.getSelectedItem().toString()));
        }
        
        if(cb_middle.isSelected())
        {
          lengthList.add(Integer.parseInt(cmb_middle.getSelectedItem().toString()));
        }
        
        return lengthList;
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
        jPanel5 = new javax.swing.JPanel();
        cb_nterminal = new javax.swing.JCheckBox();
        cmb_nterminal = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        cb_cterminal = new javax.swing.JCheckBox();
        cmb_cterminal = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        cb_ncterminal = new javax.swing.JCheckBox();
        cmb_ncterminal = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        cb_middle = new javax.swing.JCheckBox();
        cmb_middle = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        convertButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(600, 300));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.inputPanel.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        inputPanel.setMaximumSize(new java.awt.Dimension(550, 80));
        inputPanel.setMinimumSize(new java.awt.Dimension(550, 80));
        inputPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        inputPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jLabel1.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inputBrowseButton, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.inputBrowseButton.text")); // NOI18N
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

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.outputPanel.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        outputPanel.setToolTipText(org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.outputPanel.toolTipText")); // NOI18N
        outputPanel.setMaximumSize(new java.awt.Dimension(550, 80));
        outputPanel.setMinimumSize(new java.awt.Dimension(550, 80));
        outputPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        outputPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jLabel2.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(outputBrowseButton, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.outputBrowseButton.text")); // NOI18N
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

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jPanel6.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        jPanel6.setToolTipText(org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jPanel6.toolTipText")); // NOI18N
        jPanel6.setMaximumSize(new java.awt.Dimension(550, 90));
        jPanel6.setMinimumSize(new java.awt.Dimension(550, 90));
        jPanel6.setPreferredSize(new java.awt.Dimension(550, 90));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jPanel5.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        jPanel5.setMinimumSize(new java.awt.Dimension(130, 60));
        jPanel5.setPreferredSize(new java.awt.Dimension(130, 60));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(cb_nterminal, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.cb_nterminal.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel5.add(cb_nterminal, gridBagConstraints);

        cmb_nterminal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15", "20" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel5.add(cmb_nterminal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel6.add(jPanel5, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jPanel7.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        jPanel7.setMinimumSize(new java.awt.Dimension(130, 60));
        jPanel7.setPreferredSize(new java.awt.Dimension(130, 60));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(cb_cterminal, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.cb_cterminal.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel7.add(cb_cterminal, gridBagConstraints);

        cmb_cterminal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15", "20" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel7.add(cmb_cterminal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel6.add(jPanel7, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jPanel8.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        jPanel8.setMinimumSize(new java.awt.Dimension(130, 60));
        jPanel8.setPreferredSize(new java.awt.Dimension(130, 60));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(cb_ncterminal, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.cb_ncterminal.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel8.add(cb_ncterminal, gridBagConstraints);

        cmb_ncterminal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15", "20" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel8.add(cmb_ncterminal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel6.add(jPanel8, gridBagConstraints);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.jPanel9.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
        jPanel9.setMinimumSize(new java.awt.Dimension(130, 60));
        jPanel9.setPreferredSize(new java.awt.Dimension(130, 60));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(cb_middle, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.cb_middle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel9.add(cb_middle, gridBagConstraints);

        cmb_middle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15", "20" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel9.add(cmb_middle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel6.add(jPanel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jPanel6, gridBagConstraints);

        jPanel1.setMaximumSize(new java.awt.Dimension(550, 40));
        jPanel1.setMinimumSize(new java.awt.Dimension(550, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 40));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(convertButton, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.convertButton.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(SequenceEditionDialog.class, "SequenceEditionDialog.cancelButton.text")); // NOI18N
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

        FileFilter[] filters = new FileFilter[]{GUIUtil.getCustomFileFilter(GUIUtil.FASTA_EXTENSION, GUIUtil.FASTA_DESCRIPTION)};

        JFileChooser chooser = new GUIUtil().loadFilesandFileChooserLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);

       if (chooser != null) {
            inputFiles = chooser.getSelectedFiles();
        }

        if (inputFiles != null) {
            String dir = inputFiles[0].getParent() + ";";

            for (int i = 0; i < inputFiles.length; i++) {
                dir += inputFiles[i].getName() + ";";
            }

            tf_InputFiles.setText(dir);
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

    private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed

    }//GEN-LAST:event_convertButtonActionPerformed

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
            java.util.logging.Logger.getLogger(SequenceEditionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SequenceEditionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SequenceEditionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SequenceEditionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                SequenceEditionDialog dialog = new SequenceEditionDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox cb_cterminal;
    private javax.swing.JCheckBox cb_middle;
    private javax.swing.JCheckBox cb_ncterminal;
    private javax.swing.JCheckBox cb_nterminal;
    private javax.swing.JComboBox<String> cmb_cterminal;
    private javax.swing.JComboBox<String> cmb_middle;
    private javax.swing.JComboBox<String> cmb_ncterminal;
    private javax.swing.JComboBox<String> cmb_nterminal;
    private javax.swing.JButton convertButton;
    private javax.swing.JButton inputBrowseButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton outputBrowseButton;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextField tf_InputFiles;
    private javax.swing.JTextField tf_OutputFiles;
    // End of variables declaration//GEN-END:variables
}
