/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptorFactory;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptorReader;
import tomocomd.camps.mdlais.gui.CDKdesc;
import tomocomd.camps.mdlais.workers.ProteinCalculatorWorker4BM;
import tomocomd.camps.mdlais.workers.output.OutputFormats;

/**
 *
 * @author jricardo
 */
public class BatchManagerJDialog extends javax.swing.JDialog {

    private CDKdesc app;
    private ApplicationUI ui;
    private ApplicationMenu appMenu;
    private int loadedMolCounter;
    private final List<Integer> activeProjectsIDs;
    private final List<Integer> activeDatasetIDs;
    private File outputFileDirectory;
    private String outputFormatExt;
    private String outputFormat;
    private ArrayList<ProteinCalculatorWorker4BM> listOfTask;

    private final HashMap<Integer, String> inputDataList;

    private final HashMap<Integer, File[]> inputProteinFiles;
    private final HashMap<Integer, File[]> projectList;
    
    public BatchManagerJDialog(java.awt.Frame parent, ApplicationUI ui) {
        super(parent, false);

        initComponents();

        //init att
        this.ui = ui;
        this.app = ((CDKdesc) parent);
        this.appMenu = ((CDKdesc) parent).getAppMenu();

        outputFormatExt = "csv";
        outputFormat = OutputFormats.OUTPUT_CSV;

        radioButtonCSV.setSelected(true);

        inputDataList = new HashMap<>();
        inputProteinFiles = new HashMap<>();
        projectList = new HashMap<>();
        activeProjectsIDs = new ArrayList<>();
        activeDatasetIDs = new ArrayList<>();
        loadedMolCounter = 1;
        
        outputFileDirectory = null;

        jProgressBar1.setBorderPainted(true);
        jProgressBar2.setBorderPainted(true);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width / 2 - getSize().width / 2) - 38, screen.height / 2 - getSize().height / 2);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!run) {
                    for (int i = 0; i < BatchManagerJDialog.this.ui.tabbedPane.getTabCount() - 1; i++) {
                        BatchManagerJDialog.this.ui.tabbedPane.setSelectedIndex(i);

                        BatchManagerJDialog.this.ui.initialize();
                        BatchManagerJDialog.this.ui.setBlockDuringCompute(true);
                    }

                    BatchManagerJDialog.this.appMenu.unselectMatrixOptions();

                    BatchManagerJDialog.this.ui.setShowReport(false);

                    BatchManagerJDialog.this.ui.getOutFileTextField().setText("");
                    BatchManagerJDialog.this.ui.getPbdFileTextField().setText("");
                    BatchManagerJDialog.this.ui.tabbedPane.setSelectedIndex(BatchManagerJDialog.this.ui.tabbedPane.getTabCount() - 1);

                    app.setBlockDuringCompute(true, true);
                    BatchManagerJDialog.this.setVisible(false);
                }
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        if (b && !run) {
            jTextFieldOutput.setText("");
            radioButtonCSV.setSelected(true);
            radioButtonTXT.setSelected(false);
            radioButtonARFF.setSelected(false);

            jCheckBoxD1.setSelected(false);
            pdbButton1.setEnabled(false);
            jTextFieldD1.setText("");
            jTextFieldD1.setEnabled(false);
            loadButton1.setEnabled(false);
            jTextFieldP1.setText("");
            jTextFieldP1.setEnabled(false);

            jCheckBoxD2.setSelected(false);
            pdbButton2.setEnabled(false);
            jTextFieldD2.setText("");
            jTextFieldD2.setEnabled(false);

            jCheckBoxD3.setSelected(false);
            pdbButton3.setEnabled(false);
            jTextFieldD3.setText("");
            jTextFieldD3.setEnabled(false);

            jCheckBoxD4.setSelected(false);
            pdbButton4.setEnabled(false);
            jTextFieldD4.setText("");
            jTextFieldD4.setEnabled(false);

            jCheckBoxD5.setSelected(false);
            pdbButton5.setEnabled(false);
            jTextFieldD5.setText("");
            jTextFieldD5.setEnabled(false);

            jCheckBoxD6.setSelected(false);
            pdbButton6.setEnabled(false);
            jTextFieldD6.setText("");
            jTextFieldD6.setEnabled(false);

            jCheckBoxD7.setSelected(false);
            pdbButton7.setEnabled(false);
            jTextFieldD7.setText("");
            jTextFieldD7.setEnabled(false);
            
            jCheckBoxD8.setSelected(false);
            pdbButton8.setEnabled(false);
            jTextFieldD8.setText("");
            jTextFieldD8.setEnabled(false);
        }

        super.setVisible(b);
    }

    private void onLoadActionPerformed(java.awt.event.ActionEvent evt) {
        final String PROJECT_BUTTON_NAME = "loadButton";
        final String PDB_BUTTON_NAME = "pdbButton";

        String buttonName = ((JButton) evt.getSource()).getName();

        //set up the button container
        Container buttonContainer = ((JButton) evt.getSource()).getParent();

        if (buttonName.contains(PROJECT_BUTTON_NAME)) {
            //<editor-fold defaultstate="collapsed" desc="PROJECT_BUTTON_NAME">

            // set up the item number for uptade textfield
            String itemNumberString = buttonName.split(PROJECT_BUTTON_NAME)[1];
            int itemNumber = Integer.parseInt(itemNumberString);

            //preparing text fiel name
            String jTextFieldName = "jTextField" + "P" + itemNumberString;

            // read de XML file and verified it
            File[] settingFile = appMenu.getSettingsFile2BM();
            String settingFilePath = "";

            // checks if all ok?
            if (settingFile == null) {
                settingFilePath = "    - Error loading Projects -";
                projectList.remove(itemNumber);
                if (activeProjectsIDs.contains(itemNumber)) {
                    int index= activeProjectsIDs.indexOf(itemNumber);
                    activeProjectsIDs.remove(index);
                }

            } else {
                //store the loaded file in list

                projectList.remove(itemNumber);
                projectList.put(itemNumber, settingFile);
                if (!activeProjectsIDs.contains(itemNumber)) {
                    activeProjectsIDs.add(itemNumber);
                }
                settingFilePath= settingFile[0].getParent()+File.separator;
                
                for (File file : settingFile) 
                {
                 settingFilePath+=file.getName()+";";
                }                
            }

            // search corresponding textfild and update it
            for (int c = 0; c < buttonContainer.getComponentCount(); c++) {
                if ((buttonContainer.getComponent(c) instanceof JTextField) && (((JTextField) buttonContainer.getComponent(c)).getName().equals(jTextFieldName))) {
                    ((JTextField) buttonContainer.getComponent(c)).setText(settingFilePath);
                    break;
                }
            }

            //</editor-fold>
        } else if (buttonName.startsWith(PDB_BUTTON_NAME)) {
            //<editor-fold defaultstate="collapsed" desc="PDB BUTTON NAME">
            // set up the item number for uptade textfield
            String itemNumberString = buttonName.split(PDB_BUTTON_NAME)[1];
            int itemNumber = Integer.parseInt(itemNumberString);

            //preparing text fiel name
            String jTextFieldName = "jTextField" + "D" + itemNumberString;

            //set up input files
            String inputFilesPath = "- is empty: must deactivate it -";

            File[] pdbFile;

            Preferences prefs = Preferences.userNodeForPackage(this.getClass());
            String lastInputDirectory = prefs.get("LAST_INPUT_DIR", "");

            JFileChooser fileDialog = new JFileChooser();
            if (!lastInputDirectory.equals("")) {
                fileDialog.setCurrentDirectory(new File(lastInputDirectory));
            }
            fileDialog.setDialogTitle("Select input file(s)");
            FileFilter fileFilter1 = GUIUtil.getCustomFileFilter(new String[]{".fastax"}, "Internal FASTA files (*.fastax)");
            fileDialog.setFileFilter(fileFilter1);
            fileDialog.setMultiSelectionEnabled(true);
            fileDialog.setAcceptAllFileFilterUsed(false);
            fileDialog.setApproveButtonText("Load");

            List<File> validFiles = new LinkedList<>();

            int status = fileDialog.showOpenDialog(this);
            if (status == JFileChooser.APPROVE_OPTION && fileDialog.getSelectedFiles().length > 0) {
                pdbFile = fileDialog.getSelectedFiles();
                String tmp = pdbFile[0].getParent() + ";";
                int counter = 0;
                List<String> representations = new ArrayList<>();

                for (File currentFile : pdbFile) 
                {
//                    String fileRepresentation = null;
//
//                    try {
//                        fileRepresentation = PDBFilter.getAminoAcidRepresentation(currentFile);
//
//                    } catch (Exception ex) 
//                    {
//                        ui.getStatusArea().append("\r\n" + "ERROR: The file " + currentFile.getName() + " was not loaded" + ex.getMessage());
//
//                        continue;
//                    }

                    ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nLoading Protein File " + loadedMolCounter + ": " + currentFile.getName());

                    tmp += currentFile.getName() + ";";

                    prefs.put("LAST_INPUT_DIR", currentFile.getParent());

                    loadedMolCounter++;

                    counter++;
                    
                    validFiles.add(currentFile);
                }

                ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nSucessfully Loaded: " + counter + "/" + pdbFile.length + " protein file(s)");

               // if (counter > 0) {
                    JOptionPane.showMessageDialog(app, "Sucessfully Loaded: " + counter + "/" + pdbFile.length + " protein file(s)", "ToMoCoMD-CAMPS", JOptionPane.INFORMATION_MESSAGE);

                    inputFilesPath = tmp;
//                } else {
//                    JOptionPane.showMessageDialog(app, "Please provide valid protein files", "ToMoCoMD-CAMPS", JOptionPane.ERROR_MESSAGE);
//                }
            }

            //storing the current path in item position
            inputDataList.remove(itemNumber);

            inputDataList.put(itemNumber, inputFilesPath);

            File[] validPdbFiles = new File[validFiles.size()];

            for (int i = 0; i < validFiles.size(); i++) 
            {
                validPdbFiles[i] = validFiles.get(i);
            }

            inputProteinFiles.put(itemNumber, validPdbFiles);

            //search corresponding textfild and update it
            for (int c = 0; c < buttonContainer.getComponentCount(); c++) {
                if ((buttonContainer.getComponent(c) instanceof JTextField) && (((JTextField) buttonContainer.getComponent(c)).getName().equals(jTextFieldName))) {
                    ((JTextField) buttonContainer.getComponent(c)).setText(inputFilesPath);
                    break;
                }
            }

            //</editor-fold>
        }
    }

    private void setOutputFormatExt(String newExt) {
        this.outputFormatExt = newExt;
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

        buttonGroupMethods = new javax.swing.ButtonGroup();
        jPanelOutput = new javax.swing.JPanel();
        jButtonBrowse = new javax.swing.JButton();
        jTextFieldOutput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        radioButtonCSV = new javax.swing.JRadioButton();
        radioButtonTXT = new javax.swing.JRadioButton();
        radioButtonARFF = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        pdbButton1 = new javax.swing.JButton();
        jCheckBoxD1 = new javax.swing.JCheckBox();
        jTextFieldD1 = new javax.swing.JTextField();
        jCheckBoxD2 = new javax.swing.JCheckBox();
        pdbButton2 = new javax.swing.JButton();
        jTextFieldD2 = new javax.swing.JTextField();
        jCheckBoxD3 = new javax.swing.JCheckBox();
        pdbButton3 = new javax.swing.JButton();
        jTextFieldD3 = new javax.swing.JTextField();
        jCheckBoxD4 = new javax.swing.JCheckBox();
        pdbButton4 = new javax.swing.JButton();
        jTextFieldD4 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxD5 = new javax.swing.JCheckBox();
        pdbButton5 = new javax.swing.JButton();
        jTextFieldD5 = new javax.swing.JTextField();
        jCheckBoxD6 = new javax.swing.JCheckBox();
        pdbButton6 = new javax.swing.JButton();
        jTextFieldD6 = new javax.swing.JTextField();
        jCheckBoxD7 = new javax.swing.JCheckBox();
        pdbButton7 = new javax.swing.JButton();
        jTextFieldD7 = new javax.swing.JTextField();
        jCheckBoxD8 = new javax.swing.JCheckBox();
        jTextFieldD8 = new javax.swing.JTextField();
        pdbButton8 = new javax.swing.JButton();
        jPanelProjects = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTextFieldP1 = new javax.swing.JTextField();
        loadButton1 = new javax.swing.JButton();
        jButtonStart = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jProgressBar2 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Batch Process Manager  ");
        setMinimumSize(new java.awt.Dimension(620, 500));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanelOutput.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Output", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanelOutput.setMaximumSize(new java.awt.Dimension(600, 85));
        jPanelOutput.setMinimumSize(new java.awt.Dimension(600, 85));
        jPanelOutput.setName(""); // NOI18N
        jPanelOutput.setPreferredSize(new java.awt.Dimension(600, 85));
        jPanelOutput.setLayout(new java.awt.GridBagLayout());

        jButtonBrowse.setText("Browse");
        jButtonBrowse.setToolTipText("click on Browse to setup output folder");
        jButtonBrowse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonBrowse.setMaximumSize(new java.awt.Dimension(115, 27));
        jButtonBrowse.setMinimumSize(new java.awt.Dimension(115, 27));
        jButtonBrowse.setPreferredSize(new java.awt.Dimension(115, 27));
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
        jPanelOutput.add(jButtonBrowse, gridBagConstraints);

        jTextFieldOutput.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldOutput.setToolTipText("click on Browse to setup output folder");
        jTextFieldOutput.setMaximumSize(new java.awt.Dimension(290, 27));
        jTextFieldOutput.setMinimumSize(new java.awt.Dimension(290, 27));
        jTextFieldOutput.setPreferredSize(new java.awt.Dimension(290, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 18, 0, 0);
        jPanelOutput.add(jTextFieldOutput, gridBagConstraints);

        jLabel1.setText("Folder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 29, 0, 0);
        jPanelOutput.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Method");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 29, 0, 0);
        jPanelOutput.add(jLabel2, gridBagConstraints);

        buttonGroupMethods.add(radioButtonCSV);
        radioButtonCSV.setText("Comma Separated Values");
        radioButtonCSV.setToolTipText("");
        radioButtonCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonCSVActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 9, 0);
        jPanelOutput.add(radioButtonCSV, gridBagConstraints);
        radioButtonCSV.getAccessibleContext().setAccessibleName("radiobuttonCSV");

        buttonGroupMethods.add(radioButtonTXT);
        radioButtonTXT.setText(" Space Delimited");
        radioButtonTXT.setToolTipText("");
        radioButtonTXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonTXTActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 9, 0);
        jPanelOutput.add(radioButtonTXT, gridBagConstraints);
        radioButtonTXT.getAccessibleContext().setAccessibleName("radiobuttonTXT");

        buttonGroupMethods.add(radioButtonARFF);
        radioButtonARFF.setText("ARFF - Weka");
        radioButtonARFF.setToolTipText("");
        radioButtonARFF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonARFFActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 9, 18);
        jPanelOutput.add(radioButtonARFF, gridBagConstraints);
        radioButtonARFF.getAccessibleContext().setAccessibleName("radiobuttonARFF");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanelOutput, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datasets", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.setMaximumSize(new java.awt.Dimension(600, 200));
        jPanel2.setMinimumSize(new java.awt.Dimension(600, 200));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 200));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel1.setMaximumSize(new java.awt.Dimension(290, 170));
        jPanel1.setMinimumSize(new java.awt.Dimension(290, 170));
        jPanel1.setPreferredSize(new java.awt.Dimension(290, 170));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        pdbButton1.setText("Load");
        pdbButton1.setToolTipText("Browse for proteins");
        pdbButton1.setEnabled(false);
        pdbButton1.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton1.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton1.setName("pdbButton1"); // NOI18N
        pdbButton1.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 5, 1);
        jPanel1.add(pdbButton1, gridBagConstraints);
        pdbButton1.getAccessibleContext().setAccessibleName("sdfButton1");

        jCheckBoxD1.setText("1");
        jCheckBoxD1.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 5, 1);
        jPanel1.add(jCheckBoxD1, gridBagConstraints);

        jTextFieldD1.setEditable(false);
        jTextFieldD1.setEnabled(false);
        jTextFieldD1.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD1.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD1.setName("jTextFieldD1"); // NOI18N
        jTextFieldD1.setPreferredSize(new java.awt.Dimension(165, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 5, 1);
        jPanel1.add(jTextFieldD1, gridBagConstraints);

        jCheckBoxD2.setText("2");
        jCheckBoxD2.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD2ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel1.add(jCheckBoxD2, gridBagConstraints);

        pdbButton2.setText("Load");
        pdbButton2.setToolTipText("Browse for proteins");
        pdbButton2.setEnabled(false);
        pdbButton2.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton2.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton2.setName("pdbButton2"); // NOI18N
        pdbButton2.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel1.add(pdbButton2, gridBagConstraints);
        pdbButton2.getAccessibleContext().setAccessibleName("sdfButton2");

        jTextFieldD2.setEditable(false);
        jTextFieldD2.setEnabled(false);
        jTextFieldD2.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD2.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD2.setName("jTextFieldD2"); // NOI18N
        jTextFieldD2.setPreferredSize(new java.awt.Dimension(165, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        jPanel1.add(jTextFieldD2, gridBagConstraints);

        jCheckBoxD3.setText("3");
        jCheckBoxD3.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD3ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel1.add(jCheckBoxD3, gridBagConstraints);

        pdbButton3.setText("Load");
        pdbButton3.setToolTipText("Browse for proteins");
        pdbButton3.setEnabled(false);
        pdbButton3.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton3.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton3.setName("pdbButton3"); // NOI18N
        pdbButton3.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel1.add(pdbButton3, gridBagConstraints);
        pdbButton3.getAccessibleContext().setAccessibleName("sdfButton3");

        jTextFieldD3.setEditable(false);
        jTextFieldD3.setEnabled(false);
        jTextFieldD3.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD3.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD3.setName("jTextFieldD3"); // NOI18N
        jTextFieldD3.setPreferredSize(new java.awt.Dimension(165, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        jPanel1.add(jTextFieldD3, gridBagConstraints);

        jCheckBoxD4.setText("4");
        jCheckBoxD4.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD4.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD4ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel1.add(jCheckBoxD4, gridBagConstraints);

        pdbButton4.setText("Load");
        pdbButton4.setToolTipText("Browse for proteins");
        pdbButton4.setEnabled(false);
        pdbButton4.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton4.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton4.setName("pdbButton4"); // NOI18N
        pdbButton4.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel1.add(pdbButton4, gridBagConstraints);
        pdbButton4.getAccessibleContext().setAccessibleName("sdfButton4");

        jTextFieldD4.setEditable(false);
        jTextFieldD4.setEnabled(false);
        jTextFieldD4.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD4.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD4.setName("jTextFieldD4"); // NOI18N
        jTextFieldD4.setPreferredSize(new java.awt.Dimension(165, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        jPanel1.add(jTextFieldD4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, -5, 0, 0);
        jPanel2.add(jPanel1, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setMaximumSize(new java.awt.Dimension(290, 170));
        jPanel3.setMinimumSize(new java.awt.Dimension(290, 170));
        jPanel3.setPreferredSize(new java.awt.Dimension(290, 170));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jCheckBoxD5.setText("5");
        jCheckBoxD5.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD5.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD5ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jCheckBoxD5, gridBagConstraints);

        pdbButton5.setText("Load");
        pdbButton5.setToolTipText("Browse for proteins");
        pdbButton5.setEnabled(false);
        pdbButton5.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton5.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton5.setName("pdbButton5"); // NOI18N
        pdbButton5.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(pdbButton5, new java.awt.GridBagConstraints());
        pdbButton5.getAccessibleContext().setAccessibleName("sdfButton5");

        jTextFieldD5.setEditable(false);
        jTextFieldD5.setEnabled(false);
        jTextFieldD5.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD5.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD5.setName("jTextFieldD5"); // NOI18N
        jTextFieldD5.setPreferredSize(new java.awt.Dimension(165, 27));
        jTextFieldD5.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jTextFieldD5, gridBagConstraints);

        jCheckBoxD6.setText("6");
        jCheckBoxD6.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD6.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD6ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel3.add(jCheckBoxD6, gridBagConstraints);

        pdbButton6.setText("Load");
        pdbButton6.setToolTipText("Browse for proteins");
        pdbButton6.setEnabled(false);
        pdbButton6.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton6.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton6.setName("pdbButton6"); // NOI18N
        pdbButton6.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel3.add(pdbButton6, gridBagConstraints);
        pdbButton6.getAccessibleContext().setAccessibleName("sdfButton6");

        jTextFieldD6.setEditable(false);
        jTextFieldD6.setEnabled(false);
        jTextFieldD6.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD6.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD6.setName("jTextFieldD6"); // NOI18N
        jTextFieldD6.setPreferredSize(new java.awt.Dimension(165, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        jPanel3.add(jTextFieldD6, gridBagConstraints);

        jCheckBoxD7.setText("7");
        jCheckBoxD7.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD7ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel3.add(jCheckBoxD7, gridBagConstraints);

        pdbButton7.setText("Load");
        pdbButton7.setToolTipText("Browse for proteins");
        pdbButton7.setEnabled(false);
        pdbButton7.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton7.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton7.setName("pdbButton7"); // NOI18N
        pdbButton7.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel3.add(pdbButton7, gridBagConstraints);
        pdbButton7.getAccessibleContext().setAccessibleName("sdfButton7");

        jTextFieldD7.setEditable(false);
        jTextFieldD7.setEnabled(false);
        jTextFieldD7.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD7.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD7.setName("jTextFieldD7"); // NOI18N
        jTextFieldD7.setPreferredSize(new java.awt.Dimension(165, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        jPanel3.add(jTextFieldD7, gridBagConstraints);

        jCheckBoxD8.setText("8");
        jCheckBoxD8.setToolTipText("Activate / Deactivate Dataset");
        jCheckBoxD8.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxD8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxD8ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel3.add(jCheckBoxD8, gridBagConstraints);

        jTextFieldD8.setEditable(false);
        jTextFieldD8.setEnabled(false);
        jTextFieldD8.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldD8.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldD8.setName("jTextFieldD8"); // NOI18N
        jTextFieldD8.setPreferredSize(new java.awt.Dimension(165, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        jPanel3.add(jTextFieldD8, gridBagConstraints);

        pdbButton8.setText("Load");
        pdbButton8.setToolTipText("Browse for proteins");
        pdbButton8.setEnabled(false);
        pdbButton8.setMaximumSize(new java.awt.Dimension(70, 27));
        pdbButton8.setMinimumSize(new java.awt.Dimension(70, 27));
        pdbButton8.setName("pdbButton8"); // NOI18N
        pdbButton8.setPreferredSize(new java.awt.Dimension(70, 27));
        pdbButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
        jPanel3.add(pdbButton8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanelProjects.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Projects", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanelProjects.setMaximumSize(new java.awt.Dimension(600, 80));
        jPanelProjects.setMinimumSize(new java.awt.Dimension(600, 80));
        jPanelProjects.setPreferredSize(new java.awt.Dimension(600, 80));
        jPanelProjects.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel4.setMaximumSize(new java.awt.Dimension(560, 55));
        jPanel4.setMinimumSize(new java.awt.Dimension(280, 55));
        jPanel4.setName(""); // NOI18N
        jPanel4.setPreferredSize(new java.awt.Dimension(575, 55));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jTextFieldP1.setEditable(false);
        jTextFieldP1.setEnabled(false);
        jTextFieldP1.setMaximumSize(new java.awt.Dimension(165, 27));
        jTextFieldP1.setMinimumSize(new java.awt.Dimension(165, 27));
        jTextFieldP1.setName("jTextFieldP1"); // NOI18N
        jTextFieldP1.setPreferredSize(new java.awt.Dimension(470, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 2);
        jPanel4.add(jTextFieldP1, gridBagConstraints);

        loadButton1.setText("Load");
        loadButton1.setToolTipText("Load algebraic descriptors project configuration");
        loadButton1.setEnabled(false);
        loadButton1.setMaximumSize(new java.awt.Dimension(70, 27));
        loadButton1.setMinimumSize(new java.awt.Dimension(70, 27));
        loadButton1.setName("loadButton1"); // NOI18N
        loadButton1.setPreferredSize(new java.awt.Dimension(70, 27));
        loadButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 2);
        jPanel4.add(loadButton1, gridBagConstraints);
        loadButton1.getAccessibleContext().setAccessibleName("loadButton1");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(-5, 0, 0, 0);
        jPanelProjects.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jPanelProjects, gridBagConstraints);

        jButtonStart.setText("Start!");
        jButtonStart.setToolTipText("Start Calculation Process");
        jButtonStart.setMaximumSize(new java.awt.Dimension(115, 27));
        jButtonStart.setMinimumSize(new java.awt.Dimension(115, 27));
        jButtonStart.setName("go"); // NOI18N
        jButtonStart.setPreferredSize(new java.awt.Dimension(115, 27));
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        getContentPane().add(jButtonStart, gridBagConstraints);

        jPanel6.setMaximumSize(new java.awt.Dimension(600, 60));
        jPanel6.setMinimumSize(new java.awt.Dimension(600, 60));
        jPanel6.setName(""); // NOI18N
        jPanel6.setPreferredSize(new java.awt.Dimension(600, 60));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jProgressBar1.setMaximumSize(new java.awt.Dimension(600, 24));
        jProgressBar1.setMinimumSize(new java.awt.Dimension(600, 24));
        jProgressBar1.setPreferredSize(new java.awt.Dimension(600, 24));
        jProgressBar1.setString("Datasets");
        jProgressBar1.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel6.add(jProgressBar1, gridBagConstraints);

        jProgressBar2.setMaximumSize(new java.awt.Dimension(600, 24));
        jProgressBar2.setMinimumSize(new java.awt.Dimension(600, 24));
        jProgressBar2.setPreferredSize(new java.awt.Dimension(600, 24));
        jProgressBar2.setString("Projects");
        jProgressBar2.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel6.add(jProgressBar2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel6, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean run;
    private boolean stop;
    private long startTime;
    private Thread thread;

    private int getSelectedAminoAcidDatasets() {
        return (jCheckBoxD1.isSelected() ? 1 : 0) + (jCheckBoxD2.isSelected() ? 1 : 0)
                + (jCheckBoxD3.isSelected() ? 1 : 0) + (jCheckBoxD4.isSelected() ? 1 : 0)+
                (jCheckBoxD5.isSelected() ? 1 : 0) + (jCheckBoxD6.isSelected() ? 1 : 0) + (jCheckBoxD7.isSelected() ? 1 : 0)+
                (jCheckBoxD8.isSelected() ? 1 : 0);
    }

    
    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed

        if (jTextFieldOutput.getText().isEmpty() || outputFileDirectory == null) {
            JOptionPane.showMessageDialog(this,
                    "Output folder destination is missing\n\n"
                    + "Click on Browse and select a destination folder",
                    "Error: Output folder", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (activeDatasetIDs.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Dataset is missing\n\n"
                    + "Activate at least one dataset and\n"
                    + "Click on Load to select molecule(s) file(s)",
                    "Error: Dataset Missing", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (activeProjectsIDs.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Project is missing\n\n"
                    + "Activate at least one project and\n"
                    + "Click on Load to select XML project file",
                    "Error: Project Missing", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //if procces was cancelled exits.
        if (((JButton) evt.getSource()).getText().equalsIgnoreCase("cancel")) {
            run = false;
            stop = true;

            for (ProteinCalculatorWorker4BM calculator : listOfTask) {
                calculator.stop();
            }
            jButtonStart.setName("go");
            jButtonStart.setText("Start!");

            blockDuringExecution(true);
            System.gc();
            return;
        }

        //initializing task array        
        listOfTask = new ArrayList<>();
        
        for (Integer id : activeDatasetIDs) 
        {
            String jInputData = inputDataList.get(id);

            String[] molsNameArray = jInputData.split(";");

            File[] proteins = inputProteinFiles.get(id);

            for (File project : projectList.get(1)) 
            {
                String outputName_Ext = "";
                
                if (molsNameArray.length > 2) {
                    outputName_Ext = "D" + id
                            + "_"
                            + "P" + "_" + project.getName()
                            + "."
                            + outputFormatExt;
                } else {
                    outputName_Ext = "D" + id + "_" + molsNameArray[1]
                            + "_"
                            + "P" + "_" + project.getName()
                            + "."
                            + outputFormatExt;
                }

                try 
                {
                    File output = new File (outputFileDirectory.getPath() + File.separatorChar, "D" + id+"_" +"P"
                                           + project.getName().substring(0, project.getName().lastIndexOf(".")) 
                                           + "." + outputFormatExt);
                    
                    MolecularDescriptorReader adr = new MolecularDescriptorReader();

                    ProteinCalculatorWorker4BM pc4bm = null;
                    List<IDescriptor> descriptorsList = null;
                    Object[] parameters = null;
                    if(project.getName().endsWith("csv"))
                    {
                    descriptorsList = MolecularDescriptorReader.getAlgebraicDescriptorListFromList(project);
                    
                    parameters =  new Object[6];                    
                    parameters[3] = false;
                    parameters[5] = true;
                    
                    pc4bm = new ProteinCalculatorWorker4BM(descriptorsList, proteins, output,
                            outputFormat, ui.getProcessorsToUse(), 
                            parameters, adr.getCutConfig());
                    }
                    else
                    {
                     adr.readProjectFile(project);
                     
                    descriptorsList = new ArrayList<>();
                    descriptorsList.addAll(MolecularDescriptorFactory.getAlgebraicDescriptorsFromReader(adr));

                    parameters = adr.getNewParameters();
                    parameters[2] = outputFileDirectory.getPath() + File.separatorChar;
                    parameters[5] = false;                    
                   
                    }
                    
                     pc4bm = new ProteinCalculatorWorker4BM(descriptorsList, proteins, output,
                            outputFormat, ui.getProcessorsToUse(), 
                            parameters, adr.getCutConfig());

                    listOfTask.add(pc4bm);

                } catch (Exception ex) 
                {
                    ex.printStackTrace();

                    System.out.println("Error computing project:" + project.getName());

                    Logger.getLogger(BatchManagerJDialog.class.getName()).log(Level.SEVERE, null, ex);

                    return;
                }
            }
        }

        ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nSuccessful created " + listOfTask.size() + " task(s)");
        startTime = System.currentTimeMillis();

        Runnable runn = new Runnable() {
            @Override
            public void run() {

                int progressProjects = 0;

                int progressDatasets = 0;

                jProgressBar1.setMaximum(activeDatasetIDs.size());

                jProgressBar2.setMaximum(listOfTask.size() / activeDatasetIDs.size());

                for (ProteinCalculatorWorker4BM calculator : listOfTask) 
                {
                    calculator.go();

                    System.gc();

                    if (stop) {
                        break;
                    }

                    progressProjects++;

                    if (progressProjects == jProgressBar2.getMaximum()) {
                        jProgressBar1.setValue(++progressDatasets);
                        jProgressBar1.setString((int) ((jProgressBar1.getValue() * 100) / jProgressBar1.getMaximum()) + "%");

                        //reset projects progress                        
                        progressProjects = 0;
                    }

                    jProgressBar2.setValue(progressProjects);
                    jProgressBar2.setString((int) ((jProgressBar2.getValue() * 100) / jProgressBar2.getMaximum()) + "%");
                }

                jProgressBar1.setValue(0);
                jProgressBar1.setString("Datasets");
                jProgressBar2.setValue(0);
                jProgressBar2.setString("Projects");

                if (!stop) {
                    JOptionPane.showMessageDialog(app, "The process was successfully finished !", "Process Finished", JOptionPane.INFORMATION_MESSAGE);
                }
                double totaltime = (System.currentTimeMillis() - startTime) / 1000;
                int totalTime = new Double(totaltime).intValue();

                ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\n =============================");
                ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\n Tasks execution took "
                        + totalTime / (60 * 60) + "h :"
                        + (totalTime % (60 * 60)) / (60) + "m :"
                        + (totalTime % (60 * 60)) % 60 + "s");

                ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\n =============================");

                blockDuringExecution(true);

                run = false;
                stop = true;
                jButtonStart.setText("Start!");
            }
        };
        thread = new Thread(runn);
        thread.start();

        run = true;
        stop = false;
        jButtonStart.setText("Cancel");

        blockDuringExecution(false);
        ui.tabbedPane.setSelectedIndex(ui.tabbedPane.getTabCount() - 1);
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void blockDuringExecution(boolean status) {
        jTextFieldOutput.setEnabled(status);
        jButtonBrowse.setEnabled(status);
        radioButtonCSV.setEnabled(status);
        radioButtonTXT.setEnabled(status);
        radioButtonARFF.setEnabled(status);

        jCheckBoxD1.setEnabled(status);
        pdbButton1.setEnabled(status);
        jTextFieldD1.setEnabled(status);

        if (getSelectedAminoAcidDatasets() > 0) {
            loadButton1.setEnabled(status);
            jTextFieldP1.setEnabled(status);
        }

        jCheckBoxD2.setEnabled(status);
        pdbButton2.setEnabled(status);
        jTextFieldD2.setEnabled(status);

        jCheckBoxD3.setEnabled(status);
        pdbButton3.setEnabled(status);
        jTextFieldD3.setEnabled(status);

        jCheckBoxD4.setEnabled(status);
        pdbButton4.setEnabled(status);
        jTextFieldD4.setEnabled(status);

        jCheckBoxD5.setEnabled(status);
        pdbButton5.setEnabled(status);
        jTextFieldD5.setEnabled(status);
        
        jCheckBoxD6.setEnabled(status);
        pdbButton6.setEnabled(status);
        jTextFieldD6.setEnabled(status);

        jCheckBoxD7.setEnabled(status);
        pdbButton7.setEnabled(status);
        jTextFieldD7.setEnabled(status);
        
        jCheckBoxD8.setEnabled(status);
        pdbButton8.setEnabled(status);
        jTextFieldD8.setEnabled(status);
    }

    private void loadButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButton1ActionPerformed

        onLoadActionPerformed(evt);

    }//GEN-LAST:event_loadButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

        ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\n ======== Batch Process Manager was Closed ========");

    }//GEN-LAST:event_formWindowClosed

    private void jButtonBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed

        JFileChooser fileDialog = new JFileChooser();

        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String lastInputDirectory = prefs.get("LAST_INPUT_DIR", "");

        if (!lastInputDirectory.equals("")) {
            fileDialog.setCurrentDirectory(new File(lastInputDirectory));
        }

        //one level up to show the forlder in the working dir
        fileDialog.setCurrentDirectory(fileDialog.getCurrentDirectory().getParentFile());

        fileDialog.setDialogTitle("Select the Output Folder");

        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int status = fileDialog.showOpenDialog(this);

        if (status == JFileChooser.APPROVE_OPTION) {
            outputFileDirectory = fileDialog.getSelectedFile();

            jTextFieldOutput.setText(outputFileDirectory.getAbsolutePath());
            ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nOutput Folder updated to: " + outputFileDirectory.getAbsolutePath());
        } else {
            ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nERROR: Output Folder is missing ");
            jTextFieldOutput.setText("  - must provide a valid folder path -");
        }
    }//GEN-LAST:event_jButtonBrowseActionPerformed

    private void radioButtonCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonCSVActionPerformed

        radioButtonCSV.setSelected(true);
        this.outputFormat = OutputFormats.OUTPUT_CSV;
        setOutputFormatExt("csv");

    }//GEN-LAST:event_radioButtonCSVActionPerformed

    private void radioButtonTXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonTXTActionPerformed

        radioButtonTXT.setSelected(true);
        this.outputFormat = OutputFormats.OUTPUT_TAB;
        setOutputFormatExt("txt");
    }//GEN-LAST:event_radioButtonTXTActionPerformed

    private void radioButtonARFFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonARFFActionPerformed

        radioButtonARFF.setSelected(true);
        this.outputFormat = OutputFormats.OUTPUT_ARFF;
        setOutputFormatExt("arff");
    }//GEN-LAST:event_radioButtonARFFActionPerformed

    private void pdbButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton7ActionPerformed

        onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton7ActionPerformed

    private void jCheckBoxD7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD7ItemStateChanged

       if (jCheckBoxD7.isSelected()) {
            pdbButton7.setEnabled(true);
            jTextFieldD7.setEnabled(true);
            activeDatasetIDs.add((Integer) 7);
            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);

        } else {
            activeDatasetIDs.remove((Integer) 7);
            pdbButton7.setEnabled(false);
            jTextFieldD7.setEnabled(false);
            
            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD7ItemStateChanged

    private void pdbButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton6ActionPerformed

        onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton6ActionPerformed

    private void jCheckBoxD6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD6ItemStateChanged

        if (jCheckBoxD6.isSelected()) {
            pdbButton6.setEnabled(true);
            jTextFieldD6.setEnabled(true);
            activeDatasetIDs.add((Integer) 6);
            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);

        } else {
            activeDatasetIDs.remove((Integer) 6);
            pdbButton6.setEnabled(false);
            jTextFieldD6.setEnabled(false);
            
            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD6ItemStateChanged

    private void pdbButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton5ActionPerformed

        onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton5ActionPerformed

    private void jCheckBoxD5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD5ItemStateChanged

        if (jCheckBoxD5.isSelected()) {
            pdbButton5.setEnabled(true);
            jTextFieldD5.setEnabled(true);
            activeDatasetIDs.add((Integer) 5);
            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);

        } else {
            activeDatasetIDs.remove((Integer) 5);
            pdbButton5.setEnabled(false);
            jTextFieldD5.setEnabled(false);
            
            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD5ItemStateChanged

    private void pdbButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton4ActionPerformed

        onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton4ActionPerformed

    private void jCheckBoxD4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD4ItemStateChanged

        if (jCheckBoxD4.isSelected()) {
            pdbButton4.setEnabled(true);
            jTextFieldD4.setEnabled(true);
            activeDatasetIDs.add((Integer) 4);
            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);
        } else {
            activeDatasetIDs.remove((Integer) 4);
            pdbButton4.setEnabled(false);
            jTextFieldD4.setEnabled(false);

            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD4ItemStateChanged

    private void pdbButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton3ActionPerformed

        onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton3ActionPerformed

    private void jCheckBoxD3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD3ItemStateChanged

        if (jCheckBoxD3.isSelected()) {
            pdbButton3.setEnabled(true);
            jTextFieldD3.setEnabled(true);
            activeDatasetIDs.add((Integer) 3);
            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);
        } else {
            pdbButton3.setEnabled(false);
            jTextFieldD3.setEnabled(false);
            activeDatasetIDs.remove((Integer) 3);

            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD3ItemStateChanged

    private void pdbButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton2ActionPerformed

        onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton2ActionPerformed

    private void jCheckBoxD2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD2ItemStateChanged

        if (jCheckBoxD2.isSelected()) {
            pdbButton2.setEnabled(true);            
            jTextFieldD2.setEnabled(true);
            activeDatasetIDs.add((Integer) 2);

            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);
        } else {
            activeDatasetIDs.remove((Integer) 2);

            pdbButton2.setEnabled(false);
            jTextFieldD2.setEnabled(false);
            activeDatasetIDs.remove((Integer) 2);

            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD2ItemStateChanged

    private void jCheckBoxD1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD1ItemStateChanged

        if (jCheckBoxD1.isSelected()) {
            pdbButton1.setEnabled(true);
            jTextFieldD1.setEnabled(true);
            activeDatasetIDs.add((Integer) 1);

            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);
        } else {
            pdbButton1.setEnabled(false);
            jTextFieldD1.setEnabled(false);
            activeDatasetIDs.remove((Integer) 1);

            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD1ItemStateChanged

    private void pdbButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton1ActionPerformed

        onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton1ActionPerformed

    private void jCheckBoxD8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxD8ItemStateChanged
        if (jCheckBoxD8.isSelected()) {
            pdbButton8.setEnabled(true);
            jTextFieldD8.setEnabled(true);
            activeDatasetIDs.add((Integer) 8);

            loadButton1.setEnabled(true);
            jTextFieldP1.setEnabled(true);
        } else {
            pdbButton8.setEnabled(false);
            jTextFieldD8.setEnabled(false);
            activeDatasetIDs.remove((Integer) 8);

            if (getSelectedAminoAcidDatasets() == 0) {
                loadButton1.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jCheckBoxD8ItemStateChanged

    private void pdbButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbButton8ActionPerformed
       onLoadActionPerformed(evt);
    }//GEN-LAST:event_pdbButton8ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupMethods;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JCheckBox jCheckBoxD1;
    private javax.swing.JCheckBox jCheckBoxD2;
    private javax.swing.JCheckBox jCheckBoxD3;
    private javax.swing.JCheckBox jCheckBoxD4;
    private javax.swing.JCheckBox jCheckBoxD5;
    private javax.swing.JCheckBox jCheckBoxD6;
    private javax.swing.JCheckBox jCheckBoxD7;
    private javax.swing.JCheckBox jCheckBoxD8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelOutput;
    private javax.swing.JPanel jPanelProjects;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JTextField jTextFieldD1;
    private javax.swing.JTextField jTextFieldD2;
    private javax.swing.JTextField jTextFieldD3;
    private javax.swing.JTextField jTextFieldD4;
    private javax.swing.JTextField jTextFieldD5;
    private javax.swing.JTextField jTextFieldD6;
    private javax.swing.JTextField jTextFieldD7;
    private javax.swing.JTextField jTextFieldD8;
    private javax.swing.JTextField jTextFieldOutput;
    private javax.swing.JTextField jTextFieldP1;
    private javax.swing.JButton loadButton1;
    private javax.swing.JButton pdbButton1;
    private javax.swing.JButton pdbButton2;
    private javax.swing.JButton pdbButton3;
    private javax.swing.JButton pdbButton4;
    private javax.swing.JButton pdbButton5;
    private javax.swing.JButton pdbButton6;
    private javax.swing.JButton pdbButton7;
    private javax.swing.JButton pdbButton8;
    private javax.swing.JRadioButton radioButtonARFF;
    private javax.swing.JRadioButton radioButtonCSV;
    private javax.swing.JRadioButton radioButtonTXT;
    // End of variables declaration//GEN-END:variables
}
