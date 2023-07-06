/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import dataset.IOConfiguration;
import dataset.Preprocess;
import dataset.PreprocessConfiguration;
import dataset.PreprocessType;
import representations.selection.OutputType;
import util.GUIUtil;
import workers.TaskType;
import exceptions.ExceptionType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.filechooser.FileFilter;
import tomocomd.camps.mdlais.gui.CDKdesc;
import workers.GAPSSwingWorkerTask;

/**
 *
 * @author econtreras
 */
public class FastaCuratorJDialog extends javax.swing.JDialog implements ActionListener, PropertyChangeListener {

    File[] inputFiles;

    public File outputFile;

    public TaskType taskType;

    public String TITLE = "Dataset Curator";

    public ProgressMonitor progressMonitor;

    private GAPSSwingWorkerTask task;

    public CDKdesc app;

    public OutputType outputType;

    boolean cancellationNotified;

    List<Preprocess> seqReps;

    IOConfiguration config;

    boolean fullMode;

    /**
     * Creates new form NewJDialog
     */
    public FastaCuratorJDialog(CDKdesc app, boolean modal) {
        super(app, modal);

        initComponents();

        convertButton.addActionListener(this);

        this.app = app;

        this.taskType = TaskType.FASTA2FASTAX;

        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width / 2 - getSize().width / 2) - 20, screen.height / 2 - getSize().height / 2);
        
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());

    }

    void deleteInputFiles() {
        if (inputFiles != null) {
            inputFiles = null;
        }

        tf_InputFiles.setText("");
    }

    public void setInputFiles(File[] files) {
        this.inputFiles = files;

        if (inputFiles != null) {
            Preferences prefs = Preferences.userNodeForPackage(this.getClass());

            prefs.put("LAST_INPUT_DIR", inputFiles[0].getParent());

            String dir = inputFiles[0].getParent() + ";";

            for (int i = 0; i < inputFiles.length; i++) {
                dir += inputFiles[i].getName() + ";";
            }

            tf_InputFiles.setText(dir);
        }

        inputPanel.setEnabled(false);
        lb_files.setEnabled(false);
        tf_InputFiles.setEnabled(false);
        inputBrowseButton.setEnabled(false);

        rb_Fasta.setEnabled(false);
        rb_fastax.setSelected(true);

        fullMode = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // setup the progress monitor
        progressMonitor = new ProgressMonitor(FastaCuratorJDialog.this,
                "",
                "processing", 0, 100);

        progressMonitor.setProgress(0);

        fillConfig();

        if (inputFiles != null && outputFile != null) {

            Object[] params = new Object[]{taskType, null, null, false, config, app.getUI().getPbdFileTextField(), getExtension(), null};

            task = new GAPSSwingWorkerTask(inputFiles, outputFile, params);

            task.addPropertyChangeListener(this);

            try {
                task.execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            blockDuringTask(false);

            cancellationNotified = false;

        } else if (inputFiles == null) {
            GUIUtil.showMessage(this, "Please, select at least one input file", TITLE, JOptionPane.WARNING_MESSAGE);
        } else if (outputFile == null) {
            GUIUtil.showMessage(this, "Please, define an output folder", TITLE, JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();

            progressMonitor.setProgress(progress);

            String message = String.format("Completed %d%%.\n", progress);

            progressMonitor.setNote(message);

            if (progressMonitor.isCanceled() && !cancellationNotified) {
                task.cancel(true);
                GUIUtil.showMessage(this, "The process was cancelled", TITLE, JOptionPane.INFORMATION_MESSAGE);
                cancellationNotified = true;
            }

            if (task.isDone() && !task.isCancelled() && task.getExceptionList().isEmpty()) {
                GUIUtil.showMessage(this, "The process was succesfully finished!", TITLE, JOptionPane.INFORMATION_MESSAGE);
                Toolkit.getDefaultToolkit().beep();
                // set parent list files

            } else if (!task.getExceptionList().isEmpty()) {
                ExceptionListDialog1 eld = new ExceptionListDialog1(this,task.getExceptionList());

                    eld.setVisible(true);
            }

            if (task.isDone()) {
                blockDuringTask(true);

                cb_specialSymbols.setSelectedIndex(cb_specialSymbols.getSelectedIndex());
            }

        }
    }

    public void blockDuringTask(boolean status) {

        if (fullMode) {
            inputPanel.setEnabled(status);
            tf_InputFiles.setEnabled(status);
            inputBrowseButton.setEnabled(status);
        }
        outputPanel.setEnabled(status);
        tf_OutputFile.setEnabled(status);
        outputBrowseButton.setEnabled(status);

        lb_header.setEnabled(status);
        cb_header.setEnabled(status);

        lb_specialSymbols.setEnabled(status);
        cb_specialSymbols.setEnabled(status);
        lb_rep.setEnabled(status);
        lb_asx.setEnabled(status);
        cb_action_asx.setEnabled(status);
        cb_asx.setEnabled(status);
        lb_ijx.setEnabled(status);
        cb_action_ijx.setEnabled(status);
        cb_ijx.setEnabled(status);

        lb_x.setEnabled(status);
        cb_action_x.setEnabled(status);
        cb_x.setEnabled(status);
        lb_glx.setEnabled(status);
        cb_action_z.setEnabled(status);
        cb_glx.setEnabled(status);
        lb_traductionStop.setEnabled(status);
        cb_action_stop.setEnabled(status);
        cb_traductionStop.setEnabled(status);
        cb_action_stop.setEnabled(status);
        lb_Gap.setEnabled(status);
        cb_action_gap.setEnabled(status);
        cb_Gap.setEnabled(status);

        convertButton.setEnabled(status);
        cancelButton.setEnabled(status);

    }

//    public List<Preprocess> getSeqReps() 
//    {
//        seqReps = new LinkedList<>();
//
//        if (cb_asx.isEnabled()) 
//        {
//            String rep = (String) cb_asx.getSelectedItem();
//
//            seqReps.add(new ReplaceByCustomAminoAcid(rep));
//        }
//        
//
//        if (cb_ijx.isEnabled()) 
//        {
//            String rep = (String) cb_ijx.getSelectedItem();
//
//            seqReps.add(new ReplaceByCustomAminoAcid(rep));
//        }
//
//        if (cb_x.isEnabled()) 
//        {
//            String rep = (String) cb_x.getSelectedItem();
//
//            seqReps.add(new ReplaceByCustomAminoAcid(rep));
//        }
//
//        if (cb_glx.isEnabled()) {
//            String rep = (String) cb_glx.getSelectedItem();
//
//            seqReps.add(new ReplaceByCustomAminoAcid(rep));
//        }
//
//        if (cb_traductionStop.isEnabled()) {
//            String rep = (String) cb_traductionStop.getSelectedItem();
//
//            seqReps.add(new ReplaceByCustomAminoAcid(rep));
//        }
//
//        if (cb_Gap.isEnabled()) {
//            String rep = (String) cb_Gap.getSelectedItem();
//
//            seqReps.add(new ReplaceByCustomAminoAcid(rep));
//        }
//
//        return seqReps;
//    }
    void fillConfig() {
        config = new IOConfiguration();

        config.setExcludeSequencesWithWrongHeader(cb_header.getSelectedItem().equals("EXCLUDE SEQUENCE"));

        boolean excludeIfSpecialSymbols = cb_specialSymbols.getSelectedItem().equals("EXCLUDE SEQUENCE");

        config.setExcludeSequencesWithSpecialSymbols(excludeIfSpecialSymbols);

        if (!excludeIfSpecialSymbols) {
            configure(cb_action_asx, cb_asx, ExceptionType.ASX);
            configure(cb_action_ijx, cb_ijx, ExceptionType.IJX);
            configure(cb_action_x, cb_x, ExceptionType.AMBIGUOUS_RESIDUE);
            configure(cb_action_z, cb_glx, ExceptionType.GLX);
            configure(cb_action_stop, cb_traductionStop, ExceptionType.TRANSLATION_STOP);
            configure(cb_action_gap, cb_Gap, ExceptionType.GAP);
        }
    }

    void configure(JComboBox cbException, JComboBox cb_Action, ExceptionType exType) {

        String action = (String) cbException.getSelectedItem();

        PreprocessConfiguration src = null;

        switch (action) {
            case "REMOVE":

                src = new PreprocessConfiguration("", PreprocessType.REMOVE_POSITION);

                break;

            case "CUSTOM AMINO ACID":

                String rep = (String) cb_Action.getSelectedItem();

                src = new PreprocessConfiguration(rep, PreprocessType.REPLACE_BY_CUSTOM_AMINOACID);

                break;

            case "MOST FREQUENT":

                src = new PreprocessConfiguration("", PreprocessType.REPLACE_BY_MODE_AMINOACID);

                break;
        }

        config.addSequenceRepair(exType, src);
    }

    String getExtension() {
        return rb_Fasta.isSelected() ? ".fasta" : ".fastax";
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

        outputExtensionGroup = new javax.swing.ButtonGroup();
        inputPanel = new javax.swing.JPanel();
        lb_files = new javax.swing.JLabel();
        tf_InputFiles = new javax.swing.JTextField();
        inputBrowseButton = new javax.swing.JButton();
        outputPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tf_OutputFile = new javax.swing.JTextField();
        outputBrowseButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        rb_fastax = new javax.swing.JRadioButton();
        rb_Fasta = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        convertButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        lb_header = new javax.swing.JLabel();
        cb_header = new javax.swing.JComboBox<>();
        sequencePanel = new javax.swing.JPanel();
        lb_Gap = new javax.swing.JLabel();
        lb_asx = new javax.swing.JLabel();
        cb_Gap = new javax.swing.JComboBox<>();
        lb_traductionStop = new javax.swing.JLabel();
        cb_traductionStop = new javax.swing.JComboBox<>();
        cb_asx = new javax.swing.JComboBox<>();
        lb_glx = new javax.swing.JLabel();
        cb_glx = new javax.swing.JComboBox<>();
        lb_x = new javax.swing.JLabel();
        cb_x = new javax.swing.JComboBox<>();
        lb_specialSymbols = new javax.swing.JLabel();
        cb_specialSymbols = new javax.swing.JComboBox<>();
        lb_ijx = new javax.swing.JLabel();
        cb_ijx = new javax.swing.JComboBox<>();
        cb_action_asx = new javax.swing.JComboBox<>();
        lb_rep = new javax.swing.JLabel();
        cb_action_ijx = new javax.swing.JComboBox<>();
        cb_action_x = new javax.swing.JComboBox<>();
        cb_action_stop = new javax.swing.JComboBox<>();
        cb_action_z = new javax.swing.JComboBox<>();
        cb_action_gap = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FASTA Curator");
        setMinimumSize(new java.awt.Dimension(560, 540));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Input", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        inputPanel.setMaximumSize(new java.awt.Dimension(550, 70));
        inputPanel.setMinimumSize(new java.awt.Dimension(550, 70));
        inputPanel.setPreferredSize(new java.awt.Dimension(550, 80));
        inputPanel.setLayout(new java.awt.GridBagLayout());

        lb_files.setText("Files:");
        lb_files.setMaximumSize(new java.awt.Dimension(34, 14));
        lb_files.setMinimumSize(new java.awt.Dimension(34, 14));
        lb_files.setPreferredSize(new java.awt.Dimension(34, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        inputPanel.add(lb_files, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(inputPanel, gridBagConstraints);

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Output", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        outputPanel.setMaximumSize(new java.awt.Dimension(550, 80));
        outputPanel.setMinimumSize(new java.awt.Dimension(550, 100));
        outputPanel.setPreferredSize(new java.awt.Dimension(550, 100));
        outputPanel.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Folder:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        outputPanel.add(jLabel2, gridBagConstraints);

        tf_OutputFile.setEditable(false);
        tf_OutputFile.setMaximumSize(new java.awt.Dimension(400, 30));
        tf_OutputFile.setMinimumSize(new java.awt.Dimension(400, 30));
        tf_OutputFile.setPreferredSize(new java.awt.Dimension(400, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        outputPanel.add(tf_OutputFile, gridBagConstraints);

        outputBrowseButton.setText("Browse");
        outputBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputBrowseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        outputPanel.add(outputBrowseButton, gridBagConstraints);

        jPanel4.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        outputExtensionGroup.add(rb_fastax);
        rb_fastax.setText("FastaX");
        rb_fastax.setMaximumSize(new java.awt.Dimension(80, 23));
        rb_fastax.setMinimumSize(new java.awt.Dimension(80, 23));
        rb_fastax.setPreferredSize(new java.awt.Dimension(80, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel4.add(rb_fastax, gridBagConstraints);

        outputExtensionGroup.add(rb_Fasta);
        rb_Fasta.setSelected(true);
        rb_Fasta.setText("Fasta");
        rb_Fasta.setMaximumSize(new java.awt.Dimension(80, 23));
        rb_Fasta.setMinimumSize(new java.awt.Dimension(80, 23));
        rb_Fasta.setPreferredSize(new java.awt.Dimension(80, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel4.add(rb_Fasta, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        outputPanel.add(jPanel4, gridBagConstraints);

        jLabel4.setText("Format:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        outputPanel.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(outputPanel, gridBagConstraints);

        jPanel1.setMaximumSize(new java.awt.Dimension(550, 40));
        jPanel1.setMinimumSize(new java.awt.Dimension(550, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 40));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        convertButton.setText("Run!");
        convertButton.setMaximumSize(new java.awt.Dimension(100, 23));
        convertButton.setMinimumSize(new java.awt.Dimension(100, 23));
        convertButton.setPreferredSize(new java.awt.Dimension(100, 23));
        convertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel1.add(convertButton, gridBagConstraints);

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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(cancelButton, gridBagConstraints);

        jPanel2.setMaximumSize(new java.awt.Dimension(300, 10));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 10));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jPanel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuration", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel6.setMinimumSize(new java.awt.Dimension(550, 300));
        jPanel6.setPreferredSize(new java.awt.Dimension(550, 300));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        headerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Header", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        headerPanel.setMaximumSize(new java.awt.Dimension(500, 60));
        headerPanel.setMinimumSize(new java.awt.Dimension(500, 60));
        headerPanel.setPreferredSize(new java.awt.Dimension(500, 60));
        headerPanel.setLayout(new java.awt.GridBagLayout());

        lb_header.setText("Action on wrong format:");
        lb_header.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_header.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_header.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        headerPanel.add(lb_header, gridBagConstraints);

        cb_header.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EXCLUDE SEQUENCE", "AUTOMATIC ASSIGNMENT" }));
        cb_header.setMinimumSize(new java.awt.Dimension(210, 20));
        cb_header.setPreferredSize(new java.awt.Dimension(210, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        headerPanel.add(cb_header, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel6.add(headerPanel, gridBagConstraints);

        sequencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sequence", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        sequencePanel.setMaximumSize(new java.awt.Dimension(500, 200));
        sequencePanel.setMinimumSize(new java.awt.Dimension(500, 200));
        sequencePanel.setPreferredSize(new java.awt.Dimension(500, 200));
        sequencePanel.setLayout(new java.awt.GridBagLayout());

        lb_Gap.setText("Gap (-)");
        lb_Gap.setEnabled(false);
        lb_Gap.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_Gap.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_Gap.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        sequencePanel.add(lb_Gap, gridBagConstraints);

        lb_asx.setText("ASX (B)");
        lb_asx.setEnabled(false);
        lb_asx.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_asx.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_asx.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        sequencePanel.add(lb_asx, gridBagConstraints);

        cb_Gap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "Y" }));
        cb_Gap.setEnabled(false);
        cb_Gap.setMaximumSize(new java.awt.Dimension(50, 20));
        cb_Gap.setMinimumSize(new java.awt.Dimension(50, 20));
        cb_Gap.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        sequencePanel.add(cb_Gap, gridBagConstraints);

        lb_traductionStop.setText("Translation stop (*)");
        lb_traductionStop.setEnabled(false);
        lb_traductionStop.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_traductionStop.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_traductionStop.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        sequencePanel.add(lb_traductionStop, gridBagConstraints);

        cb_traductionStop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "Y" }));
        cb_traductionStop.setEnabled(false);
        cb_traductionStop.setMaximumSize(new java.awt.Dimension(50, 20));
        cb_traductionStop.setMinimumSize(new java.awt.Dimension(50, 20));
        cb_traductionStop.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        sequencePanel.add(cb_traductionStop, gridBagConstraints);

        cb_asx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "D", "N" }));
        cb_asx.setEnabled(false);
        cb_asx.setMaximumSize(new java.awt.Dimension(50, 20));
        cb_asx.setMinimumSize(new java.awt.Dimension(50, 20));
        cb_asx.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        sequencePanel.add(cb_asx, gridBagConstraints);

        lb_glx.setText("GLX (Z)");
        lb_glx.setEnabled(false);
        lb_glx.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_glx.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_glx.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        sequencePanel.add(lb_glx, gridBagConstraints);

        cb_glx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "E", "Q" }));
        cb_glx.setEnabled(false);
        cb_glx.setMaximumSize(new java.awt.Dimension(50, 20));
        cb_glx.setMinimumSize(new java.awt.Dimension(50, 20));
        cb_glx.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        sequencePanel.add(cb_glx, gridBagConstraints);

        lb_x.setText("Ambiguous residue (X)");
        lb_x.setEnabled(false);
        lb_x.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_x.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_x.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        sequencePanel.add(lb_x, gridBagConstraints);

        cb_x.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "Y" }));
        cb_x.setEnabled(false);
        cb_x.setMaximumSize(new java.awt.Dimension(50, 20));
        cb_x.setMinimumSize(new java.awt.Dimension(50, 20));
        cb_x.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        sequencePanel.add(cb_x, gridBagConstraints);

        lb_specialSymbols.setText("Action on non-standard symbols:");
        lb_specialSymbols.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_specialSymbols.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_specialSymbols.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        sequencePanel.add(lb_specialSymbols, gridBagConstraints);

        cb_specialSymbols.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EXCLUDE SEQUENCE", "REPLACE POSITION" }));
        cb_specialSymbols.setMinimumSize(new java.awt.Dimension(160, 20));
        cb_specialSymbols.setPreferredSize(new java.awt.Dimension(160, 20));
        cb_specialSymbols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_specialSymbolsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        sequencePanel.add(cb_specialSymbols, gridBagConstraints);

        lb_ijx.setText("IJX (J)");
        lb_ijx.setEnabled(false);
        lb_ijx.setMaximumSize(new java.awt.Dimension(200, 14));
        lb_ijx.setMinimumSize(new java.awt.Dimension(200, 14));
        lb_ijx.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        sequencePanel.add(lb_ijx, gridBagConstraints);

        cb_ijx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "I", "L" }));
        cb_ijx.setEnabled(false);
        cb_ijx.setMaximumSize(new java.awt.Dimension(50, 20));
        cb_ijx.setMinimumSize(new java.awt.Dimension(50, 20));
        cb_ijx.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        sequencePanel.add(cb_ijx, gridBagConstraints);

        cb_action_asx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REMOVE", "CUSTOM AMINO ACID", "MOST FREQUENT" }));
        cb_action_asx.setEnabled(false);
        cb_action_asx.setMaximumSize(new java.awt.Dimension(160, 20));
        cb_action_asx.setMinimumSize(new java.awt.Dimension(160, 20));
        cb_action_asx.setPreferredSize(new java.awt.Dimension(160, 20));
        cb_action_asx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_action_asxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sequencePanel.add(cb_action_asx, gridBagConstraints);

        lb_rep.setText("Replacement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        sequencePanel.add(lb_rep, gridBagConstraints);

        cb_action_ijx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REMOVE", "CUSTOM AMINO ACID", "MOST FREQUENT" }));
        cb_action_ijx.setEnabled(false);
        cb_action_ijx.setMaximumSize(new java.awt.Dimension(160, 20));
        cb_action_ijx.setMinimumSize(new java.awt.Dimension(160, 20));
        cb_action_ijx.setPreferredSize(new java.awt.Dimension(160, 20));
        cb_action_ijx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_action_ijxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sequencePanel.add(cb_action_ijx, gridBagConstraints);

        cb_action_x.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REMOVE", "CUSTOM AMINO ACID", "MOST FREQUENT" }));
        cb_action_x.setEnabled(false);
        cb_action_x.setMaximumSize(new java.awt.Dimension(160, 20));
        cb_action_x.setMinimumSize(new java.awt.Dimension(160, 20));
        cb_action_x.setPreferredSize(new java.awt.Dimension(160, 20));
        cb_action_x.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_action_xActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sequencePanel.add(cb_action_x, gridBagConstraints);

        cb_action_stop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REMOVE", "CUSTOM AMINO ACID", "MOST FREQUENT" }));
        cb_action_stop.setEnabled(false);
        cb_action_stop.setMaximumSize(new java.awt.Dimension(160, 20));
        cb_action_stop.setMinimumSize(new java.awt.Dimension(160, 20));
        cb_action_stop.setPreferredSize(new java.awt.Dimension(160, 20));
        cb_action_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_action_stopActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sequencePanel.add(cb_action_stop, gridBagConstraints);

        cb_action_z.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REMOVE", "CUSTOM AMINO ACID", "MOST FREQUENT" }));
        cb_action_z.setEnabled(false);
        cb_action_z.setMaximumSize(new java.awt.Dimension(160, 20));
        cb_action_z.setMinimumSize(new java.awt.Dimension(160, 20));
        cb_action_z.setPreferredSize(new java.awt.Dimension(160, 20));
        cb_action_z.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_action_zActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sequencePanel.add(cb_action_z, gridBagConstraints);

        cb_action_gap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REMOVE", "CUSTOM AMINO ACID", "MOST FREQUENT" }));
        cb_action_gap.setEnabled(false);
        cb_action_gap.setMaximumSize(new java.awt.Dimension(160, 20));
        cb_action_gap.setMinimumSize(new java.awt.Dimension(160, 20));
        cb_action_gap.setPreferredSize(new java.awt.Dimension(160, 20));
        cb_action_gap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_action_gapActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sequencePanel.add(cb_action_gap, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel6.add(sequencePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jPanel6, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBrowseButtonActionPerformed

        FileFilter[] filters = new FileFilter[1];

        filters[0] = GUIUtil.getCustomFileFilter(GUIUtil.FASTA_EXTENSION, GUIUtil.FASTA_DESCRIPTION);

        JFileChooser fileChooser = new GUIUtil().loadFilesandFileChooserLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);

        if (fileChooser != null) {
            inputFiles = fileChooser.getSelectedFiles();

            Preferences prefs = Preferences.userNodeForPackage(this.getClass());

            prefs.put("LAST_INPUT_DIR", inputFiles[0].getParent());

            String dir = inputFiles[0].getParent() + ";";

            for (int i = 0; i < inputFiles.length; i++) {
                dir += inputFiles[i].getName() + ";";
            }

            tf_InputFiles.setText(dir);
        }
    }//GEN-LAST:event_inputBrowseButtonActionPerformed

    private void outputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputBrowseButtonActionPerformed

        Object[] info = new GUIUtil().loadFolderLastInputDirectory(this, false);

        int status = (int) info[0];

        if (status == JFileChooser.APPROVE_OPTION) {
            JFileChooser fileChooser = (JFileChooser) info[1];

            outputFile = fileChooser.getSelectedFile();

            tf_OutputFile.setText(outputFile.getPath());
        }

    }//GEN-LAST:event_outputBrowseButtonActionPerformed

    private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed

    }//GEN-LAST:event_convertButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void cb_specialSymbolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_specialSymbolsActionPerformed
        String txt = (String) cb_specialSymbols.getSelectedItem();

        if (txt.equalsIgnoreCase("REPLACE POSITION")) {

            lb_rep.setEnabled(true);
            lb_asx.setEnabled(true);
            lb_ijx.setEnabled(true);
            lb_x.setEnabled(true);
            lb_glx.setEnabled(true);
            lb_traductionStop.setEnabled(true);
            lb_Gap.setEnabled(true);

            cb_action_asx.setEnabled(true);
            cb_action_ijx.setEnabled(true);
            cb_action_x.setEnabled(true);
            cb_action_z.setEnabled(true);
            cb_action_stop.setEnabled(true);
            cb_action_gap.setEnabled(true);

        } else {
            lb_rep.setEnabled(false);
            lb_asx.setEnabled(false);
            lb_ijx.setEnabled(false);
            lb_x.setEnabled(false);
            lb_glx.setEnabled(false);
            lb_traductionStop.setEnabled(false);
            lb_Gap.setEnabled(false);

            cb_action_asx.setSelectedIndex(0);
            cb_action_asx.setEnabled(false);

            cb_action_ijx.setSelectedIndex(0);
            cb_action_ijx.setEnabled(false);

            cb_action_x.setSelectedIndex(0);
            cb_action_x.setEnabled(false);
            cb_action_z.setSelectedIndex(0);
            cb_action_z.setEnabled(false);
            cb_action_stop.setSelectedIndex(0);
            cb_action_stop.setEnabled(false);
            cb_action_gap.setSelectedIndex(0);
            cb_action_gap.setEnabled(false);

            cb_asx.setEnabled(false);
            cb_ijx.setEnabled(false);
            cb_x.setEnabled(false);
            cb_glx.setEnabled(false);
            cb_traductionStop.setEnabled(false);
            cb_Gap.setEnabled(false);

            cb_asx.setSelectedIndex(0);
            cb_x.setSelectedIndex(0);
            cb_glx.setSelectedIndex(0);
            cb_traductionStop.setSelectedIndex(0);
            cb_Gap.setSelectedIndex(0);
        }
    }//GEN-LAST:event_cb_specialSymbolsActionPerformed

    private void cb_action_asxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_action_asxActionPerformed
        String opt = (String) cb_action_asx.getSelectedItem();

        boolean status = opt.equalsIgnoreCase("CUSTOM AMINO ACID");

        cb_asx.setEnabled(status);
    }//GEN-LAST:event_cb_action_asxActionPerformed

    private void cb_action_ijxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_action_ijxActionPerformed
        String opt = (String) cb_action_ijx.getSelectedItem();

        boolean status = opt.equalsIgnoreCase("CUSTOM AMINO ACID");

        cb_ijx.setEnabled(status);
    }//GEN-LAST:event_cb_action_ijxActionPerformed

    private void cb_action_xActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_action_xActionPerformed
        String opt = (String) cb_action_x.getSelectedItem();

        boolean status = opt.equalsIgnoreCase("CUSTOM AMINO ACID");

        cb_x.setEnabled(status);
    }//GEN-LAST:event_cb_action_xActionPerformed

    private void cb_action_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_action_stopActionPerformed
        String opt = (String) cb_action_stop.getSelectedItem();

        boolean status = opt.equalsIgnoreCase("CUSTOM AMINO ACID");

        cb_traductionStop.setEnabled(status);
    }//GEN-LAST:event_cb_action_stopActionPerformed

    private void cb_action_zActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_action_zActionPerformed
        String opt = (String) cb_action_z.getSelectedItem();

        boolean status = opt.equalsIgnoreCase("CUSTOM AMINO ACID");

        cb_glx.setEnabled(status);
    }//GEN-LAST:event_cb_action_zActionPerformed

    private void cb_action_gapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_action_gapActionPerformed
        String opt = (String) cb_action_gap.getSelectedItem();

        boolean status = opt.equalsIgnoreCase("CUSTOM AMINO ACID");

        cb_Gap.setEnabled(status);
    }//GEN-LAST:event_cb_action_gapActionPerformed

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
            java.util.logging.Logger.getLogger(FastaCuratorJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FastaCuratorJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FastaCuratorJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FastaCuratorJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
//                DatasetCuratorJDialog dialog = new DatasetCuratorJDialog(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> cb_Gap;
    private javax.swing.JComboBox<String> cb_action_asx;
    private javax.swing.JComboBox<String> cb_action_gap;
    private javax.swing.JComboBox<String> cb_action_ijx;
    private javax.swing.JComboBox<String> cb_action_stop;
    private javax.swing.JComboBox<String> cb_action_x;
    private javax.swing.JComboBox<String> cb_action_z;
    private javax.swing.JComboBox<String> cb_asx;
    private javax.swing.JComboBox<String> cb_glx;
    private javax.swing.JComboBox<String> cb_header;
    private javax.swing.JComboBox<String> cb_ijx;
    private javax.swing.JComboBox<String> cb_specialSymbols;
    private javax.swing.JComboBox<String> cb_traductionStop;
    private javax.swing.JComboBox<String> cb_x;
    private javax.swing.JButton convertButton;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton inputBrowseButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lb_Gap;
    private javax.swing.JLabel lb_asx;
    private javax.swing.JLabel lb_files;
    private javax.swing.JLabel lb_glx;
    private javax.swing.JLabel lb_header;
    private javax.swing.JLabel lb_ijx;
    private javax.swing.JLabel lb_rep;
    private javax.swing.JLabel lb_specialSymbols;
    private javax.swing.JLabel lb_traductionStop;
    private javax.swing.JLabel lb_x;
    private javax.swing.JButton outputBrowseButton;
    private javax.swing.ButtonGroup outputExtensionGroup;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JRadioButton rb_Fasta;
    private javax.swing.JRadioButton rb_fastax;
    private javax.swing.JPanel sequencePanel;
    private javax.swing.JTextField tf_InputFiles;
    private javax.swing.JTextField tf_OutputFile;
    // End of variables declaration//GEN-END:variables
}
