package tomocomd.camps.mdlais.gui.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import nu.xom.Element;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.gui.CDKdesc;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.pdbfilter.PDBFilter;
import tomocomd.camps.mdlais.tools.invariants.InvariantType;

/**
 * @author Rajarshi Guha
 */
public class ApplicationUI {

    private final JTextField pdbFileTextField;
    private final JTextField outFileTextField;
    private final JPanel panel;
    private final JButton pdbBrowseButton;
    private final JButton outBrowseButton;
    private File[] pdbFiles;
    private File outFile;
    private JPanel subpanel;
    public JTabbedPane tabbedPane;
    private StatusPanel statusPanel;
    private boolean showReport = false;
    private boolean isAA_level = false;
    private CDKdesc cdk;

    private IFormPanel formPanel;
    private MDLAIsFormPanel mdlaisFormPanel;

    private MDLAIsFormPanelList mdlaisFormPanelList;

    private boolean listMode;

    public ApplicationUI(CDKdesc cdk) {
        procsToUse = Runtime.getRuntime().availableProcessors();

        this.cdk = cdk;
        subpanel = new JPanel(new BorderLayout());

        mdlaisFormPanel = new MDLAIsFormPanel(cdk);
        mdlaisFormPanelList = new MDLAIsFormPanelList(cdk);

        JScrollPane mdlaisFormScrollPane = new JScrollPane(mdlaisFormPanel, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

        JScrollPane mdlaisFormListScrollPane = new JScrollPane(mdlaisFormPanelList, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

        statusPanel = new StatusPanel();

        JScrollPane statusScrollPane = new JScrollPane(statusPanel, VERTICAL_SCROLLBAR_AS_NEEDED,
                HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                JScrollPane scroll = (JScrollPane) tabbedPane.getSelectedComponent();

                if (tabbedPane.getSelectedIndex() == 1) 
                {
                    listMode = true;
                    cdk.getUI().setIsAA_level(false);
                }
                else
                {
                    listMode = false;
                }

                Component[] components = scroll.getComponents();

                for (Component comp : components) {
                    if (comp instanceof JViewport) {
                        for (Component comp_ : ((JViewport) comp).getComponents()) {
                            if (comp_ instanceof IFormPanel) {
                                formPanel = (IFormPanel) comp_;

                                return;
                            }
                        }
                    }
                }
            }
        });

        //TODO: fix default configuration for selecting descritor form list.
        javax.swing.ImageIcon tabIcon0 = new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/tab0ico.png"));
        javax.swing.ImageIcon tabIcon1 = new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/tab1ico.png"));
        javax.swing.ImageIcon tabIcon2 = new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/tab2ico.png"));
        tabbedPane.addTab("Projects", tabIcon0, mdlaisFormScrollPane);
        tabbedPane.addTab("Lists", tabIcon1, mdlaisFormListScrollPane);
        tabbedPane.addTab("Logs", tabIcon2, statusScrollPane);

        tabbedPane.setSelectedIndex(0);

        subpanel.add(tabbedPane, BorderLayout.CENTER);

        pdbBrowseButton = new JButton("Browse");
        pdbBrowseButton.setName("pdbButton");
        pdbBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowse(e);
            }
        });

        outBrowseButton = new JButton("Browse");
        outBrowseButton.setName("outButton");
        outBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowse(e);
            }
        });

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(2, 4, 2, 4);

        JLabel label = new JLabel("Protein file(s)");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.CENTER);
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.0;
        panel.add(label, c);

        pdbFileTextField = new JTextField(10);
        pdbFileTextField.setEditable(false);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        panel.add(pdbFileTextField, c);

        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        panel.add(pdbBrowseButton, c);

        label = new JLabel("Output file");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.CENTER);
        c.gridx = 0;
        c.gridy = 1;

        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label.setToolTipText("Click to open output directory");
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelMouseClicked(evt);
            }
        });

        panel.add(label, c);

        outFileTextField = new JTextField(10);
        outFileTextField.setEditable(false);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0;
        panel.add(outFileTextField, c);

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.0;
        panel.add(outBrowseButton, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.ipady = 10;
        c.fill = GridBagConstraints.BOTH;
        panel.add(subpanel, c);
    }

    private void labelMouseClicked(java.awt.event.MouseEvent evt) {
        if (outFile != null) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(outFile.getParentFile());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(cdk, " ERROR open directory: \n "
                            + "--> " + outFile.getParentFile() + " \n"
                            + "restart the application or check the file system",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(cdk, " ERROR getting Desktop API \n "
                        + "this class is NOT supported on the current platform \n"
                        + "restart the application or reinstall the JRE",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(cdk, " ERROR opening output folder \n "
                    + "please provide a valid output folder",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int procsToUse;

    public int getProcessorsToUse() {
        return procsToUse;
    }

    public void setProcessorsToUse(int procs) {
        procsToUse = procs;
    }

    public JButton getPdbBrowseButton() {
        return pdbBrowseButton;
    }

    //Get the application sub panel to use on the other parts of the application
    public JPanel getSubpanel() {
        return subpanel;
    }

    //Get the Non Classics Invariants List    
    public ArrayList<String> getNoClassicsInvariantList() {
        return formPanel.getNoClassicsInvariantList();
    }

    //Get the Classics Invariants List
    public ArrayList<String> getClassicsInvariantList() {
        return formPanel.getClassicsInvariantList();
    }

    //Get the molecule textbox to use on the other parts of the application
    public JTextField getPbdFileTextField() {
        return pdbFileTextField;
    }

    //Get the out file textbox to use on the other parts of the application
    public int getSelectedDescriptors() {
        return formPanel.getSelectedDescriptors();
    }

    public int getTotalDesc() {
        return formPanel.getTotalDesc();
    }

    public JTextField getOutFileTextField() {
        return outFileTextField;
    }

    //Get the application panel to use on the other parts of the application
    public JPanel getPanel() {
        return panel;
    }

    public MDLAIsFormPanel getMcompasFormPanel() {
        return mdlaisFormPanel;
    }

    public IFormPanel getFormPanel() {
        return formPanel;
    }

    //To check the extension on the molecule and out files
    public String checkExtension(String filename, String extension) {
        String aux = "";
        int p = 0;
        while (p < filename.length() && filename.charAt(p) != '.') {
            aux += filename.charAt(p);
            p++;
        }
        String[] tmp = extension.split(" ");
        extension = tmp[tmp.length - 1].replace("(", "");
        extension = extension.replace(")", "");
        if (extension.equalsIgnoreCase("files")) {
            extension = "txt";
        }
        return aux + "." + extension;
    }

    //Used to load the protein and the output files
    public void doLoadPDB() {
        pdbBrowseButton.doClick();
    }

    private void onBrowse(ActionEvent e) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        String lastInputDirectory = prefs.get("LAST_INPUT_DIR", "");

        String buttonName = ((JButton) e.getSource()).getName();

        JFileChooser.setDefaultLocale(Locale.US);

        JFileChooser fileChooser = new JFileChooser();

        FileFilter fileFilter;

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (!lastInputDirectory.equals("")) {
            fileChooser.setCurrentDirectory(new File(lastInputDirectory));
        }
        if (buttonName.startsWith("pdbButton")) {

            FileFilter fileFilter1 = GUIUtil.getCustomFileFilter(new String[]{".fasta"}, "FASTA files (*.fasta)");

            fileFilter = GUIUtil.getCustomFileFilter(new String[]{".fastax"}, "Internal FASTA files (*.fastax)");

            fileChooser.setDialogTitle("Select the input file(s)");

            fileChooser.setAcceptAllFileFilterUsed(false);

            fileChooser.addChoosableFileFilter(fileFilter1);

            fileChooser.addChoosableFileFilter(fileFilter);

            fileChooser.setMultiSelectionEnabled(true);

            fileChooser.setApproveButtonText("Load");

            int status = fileChooser.showOpenDialog(this.getPanel());

            if (status == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFiles().length > 0) {
                //chequeo de duplicados
                pdbFiles = fileChooser.getSelectedFiles();

                String option = fileChooser.getFileFilter().getDescription();
                
                prefs.put("LAST_INPUT_DIR", pdbFiles[0].getParent());

                if (!option.contains("Internal")) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            FastaCuratorJDialog fc = new FastaCuratorJDialog(cdk, true);

                            fc.setInputFiles(pdbFiles);

                            fc.setVisible(true);
                        }
                    });

                } else {
                    // No hay filtrado ya vienen en pdbx (actualizar directamente el textfield)
                    String tmp = pdbFiles[0].getParent() + ";";

                    int counter = 0;
                    // procesar los archivos
                    for (File currentFile : pdbFiles) {

                        getStatusArea().append("\r\nLoad Protein File: " + currentFile.getName());

                        tmp += currentFile.getName() + ";";

                        counter++;
                    }

                    if (counter > 0) {
                        pdbFileTextField.setText(tmp);

                        getStatusArea().append("\r\nLoaded: " + counter + " protein file(s)");

                    } else {
                        JOptionPane.showMessageDialog(cdk, "Please provide valid protein files", "ToMoCoMD-CAMPS", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        } else {
            fileChooser.setDialogTitle("Select the output file");

            fileFilter = new FileNameExtensionFilter("Plain Text Files (txt)", new String[]{"txt"});

            fileChooser.setFileFilter(fileFilter);

            fileFilter = new FileNameExtensionFilter("Attribute-Relation Files Format (arff)", new String[]{"arff"});

            fileChooser.setFileFilter(fileFilter);

            fileFilter = new FileNameExtensionFilter("Comma-separated values Files (csv)", new String[]{"csv"});

            fileChooser.setFileFilter(fileFilter);

            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            fileChooser.setApproveButtonText("Save");
            int status = fileChooser.showSaveDialog(this.getPanel());
            if (status == JFileChooser.APPROVE_OPTION) {
                String ext = fileChooser.getFileFilter().getDescription();
                String[] tmp = ext.split(" ");
                ext = tmp[tmp.length - 1].replace("(", "");
                ext = ext.replace(")", "").toLowerCase();
                switch (ext) {
                    case "files":
                    case "txt":
                        cdk.getAppMenu().setOutPutCommand(ext);
                        break;
                    case "csv":
                        cdk.getAppMenu().setOutPutCommand(ext);
                        break;
                    case "arff":
                        cdk.getAppMenu().setOutPutCommand(ext);
                        break;
                }
                outFile = fileChooser.getSelectedFile();
                String nameAndExtension = checkExtension(outFile.getName(), fileChooser.getFileFilter().getDescription());
                outFileTextField.setText(outFile.getParent() + outFile.separatorChar + nameAndExtension);
                getStatusArea().append("\r\nSelect Out File: " + nameAndExtension);
            }
        }
    }

    public void setOutputFileText(String filepath) {
        outFileTextField.setText(filepath);
    }

    public void setInputFileText(String filepath) {
        pdbFileTextField.setText(filepath);
    }

    //Get the properties list
    public ArrayList<String> getAminoAcidPropertiesList() {
        if (formPanel != null) {
            return formPanel.getAminoAcidPropertiesList();
        } else {
            return null;
        }
    }

    //To show report or not, on the exteded cdk
    public void setShowReport(boolean state) {
        this.showReport = state;
    }

    public boolean isShowReport() {
        return showReport;
    }

    public boolean isIsAA_level() {
        return isAA_level;
    }

    public void setIsAA_level(boolean isAA_level) {
        this.isAA_level = isAA_level;
    }

    //Get the invariants list (Classics + NonClassics)
    public ArrayList<String> getInvariantsList() {
        return formPanel.getInvariantsList();
    }

    //Get the group list, (Total + Locals)
    public ArrayList<String> getGroupsList() {
        return formPanel.getGroupsList();
    }

    public Object[] getNewParameters() {
        Object[] result = new Object[6];
        result[0] = false;
        result[1] = showReport;
        result[2] = outFile.getParent() + File.separatorChar;
        result[3] = isAA_level;
        result[4] = !isAA_level ? -1 : cdk.getAppMenu().getAADialog().getWindowSize();
        result[5] = listMode;
        if (formPanel.getCutOffsList() != null) {
            for (String s : formPanel.getCutOffsList().keySet()) {
                if (s.equalsIgnoreCase("all")) {
                    result[0] = true;
                    break;
                }
            }
        }

        return result;
    }

    //Get the text area on the status panel
    public JTextArea getStatusArea() {
        return statusPanel.getTextArea();
    }

    void setPanelAttribute(String attribute) {
        formPanel.setPanelAttribute(attribute);
    }

    public boolean doApply() {
        return formPanel.doApply();
    }

    void initialize() {
        formPanel.initialize();
    }

    public void setSelectHistory() {
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    public void setBlockDuringCompute(boolean status) {
        pdbFileTextField.setEnabled(status);
        outFileTextField.setEnabled(status);

        pdbBrowseButton.setEnabled(status);
        outBrowseButton.setEnabled(status);

        int tab = tabbedPane.getSelectedIndex();
        for (int i = 0; i < tabbedPane.getTabCount() - 1; i++) {
            if (i != tab) {
                tabbedPane.setEnabledAt(i, status);
            }
        }

        formPanel.setBlockDuringCompute(status);

    }

    public WeightConfiguration getCutoffConfiguration() {
        return formPanel.getCutoffConfiguration();
    }

    void readConfiguration(Element root) {
        formPanel.readConfiguration(root);
    }

    void saveConfiguration(Element root) {
        formPanel.saveConfiguration(root);
    }

    public void reportInvalidFiles(PDBFilter filter) {
        List<String> nonValidFiles = filter.getInvalidFormatFiles();

        if (!filter.getInvalidFormatFiles().isEmpty()) {
            for (String fileName : nonValidFiles) {
                getStatusArea().append("\r\n" + "ERROR: The file " + fileName + " was not loaded");
            }
        }
    }

    public HashMap<InvariantType, Object[]> getInvariantParameters() {
        return formPanel.getInvParameters();
    }

    public List<IDescriptor> getDescriptorList() {
        return formPanel.getDescriptorList();
    }
}
