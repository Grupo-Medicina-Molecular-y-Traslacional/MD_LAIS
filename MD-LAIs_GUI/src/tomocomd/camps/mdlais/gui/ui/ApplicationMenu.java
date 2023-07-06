package tomocomd.camps.mdlais.gui.ui;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.logging.*;
import java.util.prefs.Preferences;
import javax.swing.JPopupMenu.Separator;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import tomocomd.camps.mdlais.gui.AppOptions;
import tomocomd.camps.mdlais.gui.CDKdesc;
import nu.xom.*;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptorHeading;
import tomocomd.camps.mdlais.exceptions.TomocomdException;
import tomocomd.camps.mdlais.workers.output.OutputFormats;

/**
 * @author jricardo
 */
public class ApplicationMenu {

    private final ApplicationUI ui;
    private final CDKdesc app;

    private JMenuItem newMenu;
    private JMenuItem saveMenu;
    private JMenuItem loadMenu;

    private JMenuItem loadProjectsMenu;

    JMenuItem loadFASTAMenuItem;
    JMenuItem exitMenu;        
    private JMenuItem duplicatesMenuItem, curatorMenuItem, formatConvertMenuItem, sequenceEditionMenuItem;
            
    private JMenuItem batchMenu;
    private JMenuItem exampleDataMenuItem;
    private JMenu outputFormatMenu;
    private JMenuItem CPUMenu,AALevelMenu;
    private JMenuItem showExceptionsMenu;
    private JMenuItem clearHistoryMenu;

    private JMenuItem featureMenuItem;
    
    private JCheckBoxMenuItemDistance distanceCenter;
    private JCheckBoxMenuItem showReport, showIconLabels;
    private JRadioButtonMenuItem arffFormatItem, txtFormatItem, csvFormatItem;
    private JButton jButtonNew;
    private JButton jButtonLoadFASTA;
    private JButton jButtonSave;
    private JButton jButtonLoad;
    private JButton jButtonBatch;
    private JToggleButton jButtonShowReport;
    private JButton jButtonExampleData;
    private JButton jButtonFeatureSelection;
    private JButton jButtonSearchDescriptors;
    private JButton jThanksButton;
    private JButton jTipsButton;
    private JButton jButtonHome;
    private JButton jButton12;
    private JButton jButtonQuitApplication;

    public ApplicationMenu(ApplicationUI ui, CDKdesc app) {
        this.ui = ui;
        this.app = app;
        this.aaDialog = new AALevelDialog(app, false, ApplicationMenu.this.ui);
    }

    public JMenuBar createMenu() {
        //<editor-fold defaultstate="collapsed" desc="Tool Bar Elementes and setup">
        /*
         * creating the icon bar
         */
        JToolBar jMainToolBar = new javax.swing.JToolBar();
        jMainToolBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMainToolBar.setRollover(true);
        jMainToolBar.setFloatable(false);
        jMainToolBar.setAutoscrolls(true);

        jButtonLoadFASTA = new javax.swing.JButton();
        jButtonLoadFASTA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/filter.png")));
        jButtonLoadFASTA.setToolTipText("Load FASTA");
        jButtonLoadFASTA.setFocusable(false);
        jButtonLoadFASTA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLoadFASTA.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLoadFASTA.addActionListener(new LoadPDBAction());
        
        /*
         * new
         */
        jButtonNew = new javax.swing.JButton();
        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/new.png")));
        jButtonNew.setToolTipText("Clean all config fields");
        jButtonNew.setFocusable(false);
        jButtonNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNew.addActionListener(new NewProjectAction());
        jMainToolBar.add(jButtonNew);

        /*
         * load
         */
        jButtonLoad = new javax.swing.JButton();
        jButtonLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/load.png")));
        jButtonLoad.setToolTipText("Load a previously saved project");
        jButtonLoad.setFocusable(false);
        jButtonLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLoad.addActionListener(new LoadProjectAction(null));
        jMainToolBar.add(jButtonLoad);
        /*
         * save
         */
        jButtonSave = new javax.swing.JButton();
        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/save.png")));
        jButtonSave.setToolTipText("Save the current project configuration");
        jButtonSave.setFocusable(false);
        jButtonSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSave.addActionListener(new SaveProjectAction());
        jMainToolBar.add(jButtonSave);

        /*
         * batch
         */
        jButtonBatch = new javax.swing.JButton();
        jButtonBatch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/batch.png")));
        jButtonBatch.setToolTipText("Launch Batch Project Manager");
        jButtonBatch.setFocusable(false);
        jButtonBatch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonBatch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonBatch.addActionListener(new BatchManagerAction());
        jMainToolBar.add(jButtonBatch);

        /*
         * report
         */
        jButtonShowReport = new JToggleButton();
        jButtonShowReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/report-off.png")));
        jButtonShowReport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/report-on.png")));
        jButtonShowReport.setToolTipText("Shows Debug Report");
        jButtonShowReport.setSelected(false);
        jButtonShowReport.setFocusable(false);
        jButtonShowReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonShowReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonShowReport.addActionListener(new ShowReportAction());
        jMainToolBar.add(jButtonShowReport);

        /*
         * example data
         */
        jButtonExampleData = new javax.swing.JButton();
        jButtonExampleData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/exampledata.png")));
        jButtonExampleData.setToolTipText("Shows the Example Input Data");
        jButtonExampleData.setFocusable(false);
        jButtonExampleData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExampleData.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExampleData.addActionListener(new ExampleDataAction());
        jMainToolBar.add(jButtonExampleData);
        
        /*
         * feaeture selection
         */
        jButtonFeatureSelection = new javax.swing.JButton();
        jButtonFeatureSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/feature_selection.png")));
        jButtonFeatureSelection.setToolTipText("Feature Selection Module");
        jButtonFeatureSelection.setFocusable(false);
        jButtonFeatureSelection.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonFeatureSelection.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonFeatureSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FeatureSelectionDialog fs = new FeatureSelectionDialog(app, true);
                fs.setVisible(true);
            }
        });
        jMainToolBar.add(jButtonFeatureSelection);
        
        /*
         * Search
         */
        jButtonSearchDescriptors = new javax.swing.JButton();
        jButtonSearchDescriptors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/search.png")));
        jButtonSearchDescriptors.setToolTipText("Dictionary of Descriptors");
        jButtonSearchDescriptors.setFocusable(false);
        jButtonSearchDescriptors.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSearchDescriptors.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSearchDescriptors.addActionListener(new descriptorSearchAction());
        jMainToolBar.add(jButtonSearchDescriptors);

        /*
         * thanks
         */
//        jThanksButton = new javax.swing.JButton();
//        jThanksButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/thanks.png")));
//        jThanksButton.setToolTipText("We are thankful to...");
//        jThanksButton.setFocusable(false);
//        jThanksButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
//        jThanksButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
//        jThanksButton.addActionListener(new helpMenuItem12Action());
//        jMainToolBar.add(jThanksButton);

        /*
         * tips
         */
        jTipsButton = new javax.swing.JButton();
        jTipsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/tips.png")));
        jTipsButton.setToolTipText("These are our advices and suggestions ");
        jTipsButton.setFocusable(false);
        jTipsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTipsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTipsButton.addActionListener(new helpMenuItem11Action());
        jMainToolBar.add(jTipsButton);

        /*
         * home
         */
        jButtonHome = new javax.swing.JButton();
        jButtonHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/home.png")));
        jButtonHome.setToolTipText("Find us here");
        jButtonHome.setFocusable(false);
        jButtonHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonHome.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonHome.addActionListener(new helpMenuItem9Action());
        jMainToolBar.add(jButtonHome);

        /*
         * help
         */
//        jButton12 = new javax.swing.JButton();
//        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/toolbar/help.png")));
//        jButton12.setToolTipText("Help Contens");
//        jButton12.setFocusable(false);
//        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
//        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
//        jButton12.addActionListener(new HelpContensAction());
//        jButton12.setEnabled(false);
        //jMainToolBar.add(jButton12);

        /*
         * exit
         */
        jButtonQuitApplication = new javax.swing.JButton();
        jButtonQuitApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/toolbar/exit.png")));
        jButtonQuitApplication.setToolTipText("Quit Application");
        jButtonQuitApplication.setFocusable(false);
        jButtonQuitApplication.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonQuitApplication.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonQuitApplication.addActionListener(new ExitAction());
        jMainToolBar.add(jButtonQuitApplication);

        /*
         * setup main tool bar icons
         */
        app.add(jMainToolBar, java.awt.BorderLayout.PAGE_START);
        //</editor-fold>

        /*
         * creating the menu elements
         */
        JMenuBar mb = new JMenuBar();

        //<editor-fold defaultstate="collapsed" desc="Separators">
        //javax.swing.JPopupMenu.Separator jSeparator1;
        Separator jSeparatorProject1 = new Separator();
        Separator jSeparatorProject2 = new Separator();

        Separator jSeparatorStructure1 = new Separator();
        Separator jSeparatorStructure2 = new Separator();
        Separator jSeparatorStructure3 = new Separator();

        Separator jSeparatorWorkset1 = new Separator();
        Separator jSeparatorWorkset2 = new Separator();
        Separator jSeparatorWorkset3 = new Separator();
        Separator jSeparatorWorkset4 = new Separator();

        Separator jSeparatorWindow1 = new Separator();

        Separator jSeparatorOnline1 = new Separator();
        Separator jSeparatorOnline2 = new Separator();
        Separator jSeparatorOnline3 = new Separator();
        Separator jSeparatorOnline4 = new Separator();

        Separator jSeparatorHelp1 = new Separator();
        Separator jSeparatorHelp2 = new Separator();

        Separator jSeparatorIconLabels1 = new Separator();
        Separator jSeparatorIconLabels2 = new Separator();

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Project Menu">
        JMenu projectMenu = new JMenu("Project");

        newMenu = new JMenuItem("New");
        newMenu.addActionListener(new NewProjectAction());
        newMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/new.png")));

        saveMenu = new JMenuItem("Save");
        saveMenu.addActionListener(new SaveProjectAction());
        saveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/save.png")));

        loadMenu = new JMenuItem("Load");
        loadMenu.addActionListener(new LoadProjectAction(null));
        loadMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        loadMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/load.png")));

        loadProjectsMenu = new JMenuItem("Load Default Projects");
        loadProjectsMenu.addActionListener(new LoadProjectAction(new File(System.getProperty("user.dir"), "projects" + File.separator)));
        loadProjectsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/load.png")));
        
        batchMenu = new JMenuItem("Batch Mode");
        batchMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/batch.png")));
        batchMenu.addActionListener(new BatchManagerAction());
        batchMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        
        exitMenu= new JMenuItem("Exit Program");
        exitMenu.addActionListener(new ExitAction());
        exitMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        exitMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/exit.png")));
        
        
        projectMenu.add(newMenu);
        projectMenu.add(loadMenu);
        projectMenu.add(loadProjectsMenu);
        projectMenu.add(saveMenu);
        projectMenu.add(exitMenu);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Options menu">
        JMenu optionMenu = new JMenu("Options");

        showReport = new JCheckBoxMenuItem("Show Debug Report");
        showReport.addActionListener(new ShowReportAction());

        clearHistoryMenu = new JMenuItem("Clear Logs");
        clearHistoryMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        clearHistoryMenu.addActionListener(new ClearHistoryAction());

        outputFormatMenu = new JMenu("Output Format");
        ButtonGroup group = new ButtonGroup();

        arffFormatItem = new JRadioButtonMenuItem("ARFF - Weka File");
        arffFormatItem.setActionCommand(OutputFormats.OUTPUT_ARFF);
        arffFormatItem.addActionListener(new OutputFormatAction());
        arffFormatItem.setSelected(false);
        group.add(arffFormatItem);
        outputFormatMenu.add(arffFormatItem);

        txtFormatItem = new JRadioButtonMenuItem("TXT - Space Separated Values");
        txtFormatItem.setActionCommand(OutputFormats.OUTPUT_SPC);
        txtFormatItem.addActionListener(new OutputFormatAction());
        txtFormatItem.setSelected(true);
        group.add(txtFormatItem);
        outputFormatMenu.add(txtFormatItem);

        csvFormatItem = new JRadioButtonMenuItem("CSV - Comma Separated Values");
        csvFormatItem.setActionCommand(OutputFormats.OUTPUT_CSV);
        csvFormatItem.addActionListener(new OutputFormatAction());
        csvFormatItem.setSelected(false);
        group.add(csvFormatItem);
        outputFormatMenu.add(csvFormatItem);

        showIconLabels = new JCheckBoxMenuItem("Show ToolBar Text");
        showIconLabels.addActionListener(new showIconLabelsAction());

        showExceptionsMenu = new JMenuItem("Show Last List of Exceptions");
        showExceptionsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/exceptions.png")));
        showExceptionsMenu.addActionListener(new ShowExceptionsAction());

        JMenuItem MemoryMenu = new JMenuItem("Memory Manager");
        MemoryMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/options.mem.png")));
        MemoryMenu.addActionListener(new MemoryAction());
        MemoryMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));

        CPUMenu = new JMenuItem("CPU Manager");
        CPUMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/cpu.png")));
        CPUMenu.addActionListener(new CPUAction());
        
        AALevelMenu = new JMenuItem("Amino Acid-Level Output");
        AALevelMenu.addActionListener(new AALevelAction());

        optionMenu.add(showReport);
        optionMenu.add(clearHistoryMenu);
        optionMenu.add(outputFormatMenu);
        optionMenu.add(AALevelMenu);
        optionMenu.add(jSeparatorIconLabels1);
        optionMenu.add(showExceptionsMenu);
        optionMenu.add(jSeparatorIconLabels2);
        optionMenu.add(MemoryMenu);
        optionMenu.add(CPUMenu);
        optionMenu.add(jSeparatorIconLabels1);
        optionMenu.add(batchMenu);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Structure Menu">
//        JMenu structureMenu = new JMenu("File");
//
////        JMenuItem removeDuplicatesMenuItem = new JMenuItem("Remove Duplicates");
////
////        removeDuplicatesMenuItem.addActionListener(new Action4StructureMenu(
////              app));
////        removeDuplicatesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/structure.duplicate.png")));
//        loadFASTAMenuItem = new JMenuItem("Load FASTA");
//        loadFASTAMenuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                app.getUI().getPdbBrowseButton().doClick();
//            }
//        });
//        loadFASTAMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/filter_PDB.png")));
//        loadFASTAMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
//        
        
//
//        structureMenu.add(loadFASTAMenuItem);
//
//        structureMenu.add(exitMenu);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Dataset Menu">
        JMenu datasetMenu = new JMenu("Dataset");

        duplicatesMenuItem = new JMenuItem("Remove Duplicates");

        duplicatesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FASTADuplicatesDialog fd = new FASTADuplicatesDialog(app, true);
                fd.setVisible(true);
            }
        });
        duplicatesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/duplicates.png")));
        
        curatorMenuItem = new JMenuItem("FASTA Curator");

        curatorMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FastaCuratorJDialog fc = new FastaCuratorJDialog(app, true);
                fc.setVisible(true);
            }
        });
        
        curatorMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/curator.png")));
        
        formatConvertMenuItem = new JMenuItem("Format Convert");
        formatConvertMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormatConverterDialog fc= new FormatConverterDialog(app, true);
                fc.setVisible(true);
            }
        });
        formatConvertMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/format_conversion.png")));
        
        sequenceEditionMenuItem = new JMenuItem("Sequence Edition");
        sequenceEditionMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SequenceEditionDialog se= new SequenceEditionDialog(app, true);
                se.setVisible(true);
            }
        });
        
        sequenceEditionMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/sequence_edition.png")));
        
        exampleDataMenuItem = new JMenuItem("Example Data");
        exampleDataMenuItem.addActionListener(new ExampleDataAction());
        exampleDataMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/exampledata.png")));

        datasetMenu.add(duplicatesMenuItem);
        datasetMenu.add(curatorMenuItem);
        datasetMenu.add(formatConvertMenuItem);
        datasetMenu.add(sequenceEditionMenuItem);
        datasetMenu.add(exampleDataMenuItem);
        //</editor-fold>   
        //<editor-fold defaultstate="collapsed" desc="Feature Menu">
        JMenu featureMenu = new JMenu("Feature");

        featureMenuItem = new JMenuItem("Select...");
        featureMenuItem.setToolTipText("Select high relevant/low redundant features");
        featureMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FeatureSelectionDialog fs = new FeatureSelectionDialog(app, true);
                fs.setVisible(true);
            }
        });
        featureMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/feature_selection.png")));
              
        featureMenu.add(featureMenuItem);
        
        //</editor-fold> 
        //<editor-fold defaultstate="collapsed" desc="Workset Menu">
        JMenu worksetMenu = new JMenu("Workset");

        JMenuItem descriptorSearchMenuItem = new JMenuItem("Descriptor Search");
        descriptorSearchMenuItem.addActionListener(new descriptorSearchAction());
        descriptorSearchMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/search.png")));

        worksetMenu.add(descriptorSearchMenuItem);

        worksetMenu.add(jSeparatorWorkset1);
//        worksetMenu.add(worksetMenuItem2);
//        worksetMenu.add(worksetMenuItem3);
//
//        worksetMenu.add(jSeparatorWorkset2);
//        worksetMenu.add(worksetMenuItem4);
//        worksetMenu.add(worksetMenuItem5);
//        worksetMenu.add(worksetMenuItem6);
//        worksetMenu.add(worksetMenuItem7);
//
//        worksetMenu.add(jSeparatorWorkset3);
//        worksetMenu.add(worksetMenuItem8);
//        worksetMenu.add(worksetMenuItem9);
//
//        worksetMenu.add(jSeparatorWorkset4);
//        worksetMenu.add(worksetMenuItem10);
//        worksetMenu.add(worksetMenuItem11);
//        worksetMenu.add(worksetMenuItem12);
//        worksetMenu.add(worksetMenuItem13);
//        worksetMenu.add(worksetMenuItem14);
//        worksetMenu.add(worksetMenuItem15);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Window menu">
//        JMenu windowMenu = new JMenu("Window");
//        JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
//        preferencesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/windows.pref.png")));
//        preferencesMenuItem.setEnabled(false);
//        preferencesMenuItem.addActionListener(new preferencesMenuAction());
//
//        JMenuItem windowsMenuItem2 = new JCheckBoxMenuItem("Application Perspective");
//        windowsMenuItem2.setSelected(true);
//        windowsMenuItem2.setEnabled(false);
//
//        windowsMenuItem2.addActionListener(new windowsMenuItem2Action());
//
//        JMenuItem windowsMenuItem3 = new JMenuItem("Wizard Perspective");
//        windowsMenuItem3.setEnabled(false);
//        windowsMenuItem3.addActionListener(new windowsMenuItem3Action());
//
//        JMenuItem windowsMenuItem4 = new JMenuItem("Workflow Perspective");
//        windowsMenuItem4.setEnabled(false);
//        windowsMenuItem4.addActionListener(new windowsMenuItem4Action());
//
//        JMenuItem windowsMenuItem5 = new JMenuItem("Console Perspective");
//        windowsMenuItem5.setEnabled(false);
//        windowsMenuItem5.addActionListener(new windowsMenuItem5Action());
//
//        windowMenu.add(windowsMenuItem2);
//        windowMenu.add(windowsMenuItem3);
//        windowMenu.add(windowsMenuItem4);
//        windowMenu.add(windowsMenuItem5);
//        windowMenu.add(jSeparatorWindow1);
//        windowMenu.add(preferencesMenuItem);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Online Menu">
//        JMenu onlineMenu = new JMenu("Online");
//
//        JMenuItem onlineMenuItem1 = new JMenuItem("Find Implementation of QuBiLs-MAS");
//        onlineMenuItem1.addActionListener(new onlineMenuItem1Action());
//        //onlineMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/online.find.png")));
//        onlineMenuItem1.setEnabled(false);
//
//        JMenuItem onlineMenuItem2 = new JMenuItem("Browse CAMD-BIR Unit");
//        onlineMenuItem2.addActionListener(new onlineMenuItem2Action());
//        onlineMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/online.browse.png")));
//
//        JMenuItem onlineMenuItem3 = new JMenuItem("ToMoCOMD-CAMPS on the Web");
//        onlineMenuItem3.addActionListener(new onlineMenuItem3Action());
//        onlineMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/online.web.png")));
//
//        JMenuItem onlineMenuItem4 = new JMenuItem("Register Online");
//        onlineMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/online.register.png")));
//
//        onlineMenuItem4.addActionListener(new onlineMenuItem4Action());
//        onlineMenuItem4.setEnabled(false);
//
//        JMenuItem onlineMenuItem5 = new JMenuItem("Contact us by E-mail");
//        onlineMenuItem5.addActionListener(new onlineMenuItem5Action());
//        onlineMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/online.sendmail.png")));
//
//        JMenuItem onlineMenuItem6 = new JMenuItem("Check for Uptates");
//        onlineMenuItem6.addActionListener(new onlineMenuItem6Action());
//        onlineMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/online.update.png")));
//        onlineMenuItem6.setEnabled(false);
//
//        onlineMenu.add(onlineMenuItem1);
//        onlineMenu.add(onlineMenuItem2);
//        onlineMenu.add(onlineMenuItem3);
//
//        onlineMenu.add(jSeparatorOnline1);
//        onlineMenu.add(onlineMenuItem4);
//        onlineMenu.add(onlineMenuItem5);
//
//        onlineMenu.add(jSeparatorOnline1);
//        onlineMenu.add(onlineMenuItem6);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Help Menu">
        JMenu helpMenu = new JMenu("Help");
//        JMenuItem helpContentsMenuItem = new JMenuItem("Help Contents");
//        helpContentsMenuItem.addActionListener(new HelpContensAction());
//        helpContentsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
//        helpContentsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.png")));
//        helpContentsMenuItem.setEnabled(false);

        JMenuItem helpMenuItem2 = new JMenuItem("First Steps");
        helpMenuItem2.addActionListener(new helpMenuItem2Action());
        helpMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.first.png")));

        JMenuItem helpMenuItem1 = new JMenuItem("Overview");
        helpMenuItem1.addActionListener(new helpMenuItem1Action());
        helpMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.overview.png")));

        JMenuItem helpMenuItemTheory = new JMenuItem("Theory");
        helpMenuItemTheory.addActionListener(Util.openFile(app, "Theory.pdf"));

        JMenuItem helpMenuItemManual = new JMenuItem("User's Manual");
        helpMenuItemManual.addActionListener(Util.openFile(app, "Manual.pdf"));

        JMenuItem helpMenuAcronym = new JMenuItem("Acronyms");
        helpMenuAcronym.addActionListener(Util.openFile(app, "Acronym.pdf"));

        JMenu helpMenu3 = new JMenu("User's Documents");
        helpMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.theory.png")));
        helpMenu3.add(helpMenuAcronym);
        helpMenu3.add(helpMenuItemTheory);
        helpMenu3.add(helpMenuItemManual);

        JMenuItem helpMenuItem6 = new JMenuItem("Glossary");
        helpMenuItem6.addActionListener(new helpMenuItemGlossary());
        helpMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.glossary.png")));

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        JMenuItem helpMenuItem7 = new JMenuItem("Icons");
        helpMenuItem7.addActionListener(new helpMenuItem7Action());
        helpMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.icons.png")));

//        JMenuItem helpMenuItem8 = new JMenuItem("Flowcharts");
//        helpMenuItem8.addActionListener(new helpMenuItem8Action());
//        helpMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mulims/gui/data/menu/help.flowchart.png")));

        JMenuItem helpMenuItem10 = new JMenuItem("KeyBoard Shortcuts");
        helpMenuItem10.addActionListener(new helpMenuItem10Action());
        helpMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.keys.png")));

        JMenuItem helpMenuItem11 = new JMenuItem("Tips");
        helpMenuItem11.addActionListener(new helpMenuItem11Action());
        helpMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/tips.png")));

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        JMenuItem helpMenuItem9 = new JMenuItem("Home");
        helpMenuItem9.addActionListener(new helpMenuItem9Action());
        helpMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/home.png")));

        JMenuItem releaseNotesMenuItem = new JMenuItem("Release Notes");
        releaseNotesMenuItem.addActionListener(new ReleaseNotesAction());
        releaseNotesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.notes.png")));

//        JMenuItem helpMenuItem12 = new JMenuItem("Thanks");
//        helpMenuItem12.addActionListener(new helpMenuItem12Action());
//        helpMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/thanks.png")));

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new AboutAction());
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/menu/help.about.png")));

        //helpMenu.add(helpContentsMenuItem);
        helpMenu.add(helpMenuItem1);
        helpMenu.add(helpMenuItem2);
        helpMenu.add(helpMenu3);
        helpMenu.add(helpMenuItem6);

        helpMenu.add(jSeparatorHelp1);
        helpMenu.add(helpMenuItem7);
        //helpMenu.add(helpMenuItem8);        
        helpMenu.add(helpMenuItem10);
        helpMenu.add(helpMenuItem11);
        
        helpMenu.add(jSeparatorHelp2);
        helpMenu.add(releaseNotesMenuItem);
        helpMenu.add(helpMenuItem9);
        //helpMenu.add(helpMenuItem12);
        helpMenu.add(aboutMenuItem);
        //</editor-fold>

        mb.add(projectMenu);
        mb.add(datasetMenu);        
        mb.add(optionMenu);
        mb.add(featureMenu);
        mb.add(helpMenu);
        JFileChooser.setDefaultLocale(Locale.US);

        return mb;
    }

    //To do click on the respective output method
    void setOutPutCommand(String ext) {
        switch (ext) {
            case "txt":
                txtFormatItem.doClick();
                break;
            case "csv":
                csvFormatItem.doClick();
                break;
            case "arff":
                arffFormatItem.doClick();
                break;
        }
    }

    private BatchManagerJDialog dlg;

    private class BatchManagerAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (app.isComputingDescriptors()) {
                JOptionPane.showMessageDialog(app, "Batch mode cannot be executed because the application is computing descriptors", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ui.setShowReport(false);
            showReport.setSelected(false);
            jButtonShowReport.setSelected(false);

            ui.tabbedPane.setSelectedIndex(ui.tabbedPane.getTabCount() - 2);
            ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\n\n ======== Starting  Batch Process Manager  ========");

            app.setBlockDuringCompute(false, true);
            dlg = dlg == null ? new BatchManagerJDialog(app, ui) : dlg;
            dlg.setVisible(true);
            ui.tabbedPane.setSelectedIndex(ui.tabbedPane.getTabCount() - 2);
        }
    }
    

    class ExitAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int dialogOption = JOptionPane.showConfirmDialog(app,
                    "Do you want ToMoCoMD-CAMPS to save your descriptors configuration?",
                    "Quit ToMoCoMD-CAMPS",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (dialogOption == JOptionPane.NO_OPTION) {
                app.shutdown();
            } else if (dialogOption == JOptionPane.YES_OPTION) {
                doSaveProject();
            }
        }
    }

    class ClearHistoryAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (app.isComputingDescriptors()) {
                AbstractButton ab = (AbstractButton) event.getSource();
                ab.setSelected(!ab.isSelected());

                JOptionPane.showMessageDialog(app, "The history cannot be cleaned because the application is computing descriptors", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ui.getStatusArea().setText("");
        }
    }

    class ShowReportAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (app.isComputingDescriptors()) {
                AbstractButton ab = (AbstractButton) event.getSource();
                ab.setSelected(!ab.isSelected());

                JOptionPane.showMessageDialog(app, "The report cannot be showed because the application is computing descriptors", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean state = ui.isShowReport();
            if (!state) {
                ui.setShowReport(true);
                showReport.setSelected(true);
                jButtonShowReport.setSelected(true);
                ui.getStatusArea().append("\r\nShow All Internal Report");
            } else {
                ui.setShowReport(false);
                showReport.setSelected(false);
                jButtonShowReport.setSelected(false);
                ui.getStatusArea().append("\r\nDon't Show All Internal Report");
            }
        }
    }

    class showIconLabelsAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            ((JCheckBoxMenuItem) event.getSource()).setSelected(!((JCheckBoxMenuItem) event.getSource()).isSelected());

            if (showIconLabels.isSelected()) {
                showIconLabels.setSelected(false);
                jButtonNew.setText("");
                jButtonSave.setText("");
                jButtonLoad.setText("");
                jButtonBatch.setText("");
                jButtonShowReport.setText("");
                jButtonExampleData.setText("");
                jButtonSearchDescriptors.setText("");
                jThanksButton.setText("");
                jTipsButton.setText("");
                jButtonHome.setText("");
                jButton12.setText("");
                jButtonQuitApplication.setText("");
            } else {
                showIconLabels.setSelected(true);
                jButtonNew.setText("New");
                jButtonSave.setText("Save");
                jButtonLoad.setText("Load");
                jButtonBatch.setText("Batch");
                jButtonShowReport.setText("Report");
                jButtonExampleData.setText("Data");
                jButtonSearchDescriptors.setText("Search");
                jThanksButton.setText("Thanks");
                jTipsButton.setText("Tips");
                jButtonHome.setText("Home");
                jButton12.setText("Help");
                jButtonQuitApplication.setText("Exit");
            }
        }
    }

    class HelpContensAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            HelpContents dlg = new HelpContents(app, false);
            dlg.setLocation(app.getLocationOnScreen().x + app.getSize().width, app.getLocationOnScreen().y);
            dlg.setVisible(true);
        }
    }

    private class MemoryAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MemoryDialog dlg = new MemoryDialog(app, false);
            dlg.setLocation(app.getLocationOnScreen().x + app.getSize().width, app.getLocationOnScreen().y);
            dlg.setVisible(true);
        }
    }

    private CPUDialog cpudlg;

    private class CPUAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (cpudlg == null) {
                cpudlg = new CPUDialog(app, false, ApplicationMenu.this.ui);
            }

            cpudlg.setLocation(app.getLocationOnScreen().x + app.getSize().width, app.getLocationOnScreen().y);
            cpudlg.setVisible(true);
        }
    }
    
    private AALevelDialog aaDialog;

    private class AALevelAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (aaDialog == null) {
                aaDialog = new AALevelDialog(app, false, ApplicationMenu.this.ui);
            }

            aaDialog.setLocation(app.getLocationOnScreen().x + app.getSize().width, app.getLocationOnScreen().y);
            aaDialog.setVisible(true);
        }
    }

    public AALevelDialog getAADialog() {
        return aaDialog;
    }
    
    private class ShowExceptionsAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (CDKdesc.eld == null) {
                JOptionPane.showMessageDialog(app, "Please run a calculation task first.\n",
                        "List of Exceptions",
                        JOptionPane.OK_OPTION);
            } else {
                CDKdesc.eld.setVisible(true);
            }
        }
    }

    class ExampleDataAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ExampleDataDialog dlg = new ExampleDataDialog(app, false);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class ReleaseNotesAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ReleaseNotesDialog dlg = new ReleaseNotesDialog(app, true);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class AboutAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                AboutTabJDialog dlg = new AboutTabJDialog(app, true);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class PluginAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PluginUI pui = new PluginUI(null);
            pui.pack();
            pui.setVisible(false);
        }
    }

    class OutputFormatAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AppOptions.getInstance().setOutputMethod(e.getActionCommand());

            /*
             * gathering the new selected output file extension
             */
            String ext = "";
            if (e.getActionCommand().equalsIgnoreCase("SPC")) {
                ext = "txt";
            } else {
                ext = e.getActionCommand().toLowerCase();
            }

            /*
             * updating the selected file extension
             */
            ui.getOutFileTextField().setText(ui.checkExtension(ui.getOutFileTextField().getText(), ext));

            /*
             * append the new selected file extension to status panel
             */
            ui.getStatusArea().append("\r\nSelected Output Method: " + ext.toUpperCase());
        }
    }

    class NewProjectAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (app.isComputingDescriptors()) {
                JOptionPane.showMessageDialog(app, "It cannot be created a new project because the application is computing descriptors", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ui.initialize();
            ui.getOutFileTextField().setText("");
            ui.getPbdFileTextField().setText("");
            ui.getStatusArea().setText("");
            ui.setIsAA_level(false);
            aaDialog = new AALevelDialog(app, true, ui);
            ui.getStatusArea().append("\r\nDon't Adds Amino Acid Level Descriptors");
        }
    }

    class LoadPDBAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ui.doLoadPDB();
        }
    }

    public void unselectMatrixOptions() {
        showReport.setSelected(false);
        jButtonShowReport.setSelected(false);
    }

    class descriptorSearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DescriptorSearchDialog dlg = new DescriptorSearchDialog(app, false);
            dlg.setVisible(true);
        }
    }

    private class helpMenuItem10Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                KeyBoardDialog dlg = new KeyBoardDialog(app, false);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class helpMenuItem11Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                TipsDialog dlg = new TipsDialog(app, true);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class helpMenuItemGlossary implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            GlossaryDialog dlg = new GlossaryDialog(app, false);
            dlg.setVisible(true);
        }
    }

    private class helpMenuItem1Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                OverviewDialog dlg = new OverviewDialog(app, true);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class helpMenuItem2Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FirstStepsDialog dlg = new FirstStepsDialog(app, true);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class helpMenuItem7Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            IconsDialog dlg = new IconsDialog(app, false);
            dlg.setVisible(true);
        }
    }

    private class helpMenuItem9Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                HomeDialog dlg = new HomeDialog(app, true);
                dlg.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private static class onlineMenuItem1Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class onlineMenuItem2Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
            URI website = null;
            try {
                website = new URI("http://www.uv.es/yoma/Documents/01%20CAMD-BIR%20Unit%201.0/CAMD-BIR%20UNIT.htm");
            } catch (URISyntaxException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(website);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(app, " ERROR open website: \n "
                            + "--> " + website + " \n"
                            + "restart the application or check the internet connection",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(app, " ERROR getting Desktop API \n "
                        + "this class is NOT supported by the current platform \n"
                        + "restart the application or Reinstall the JRE",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class onlineMenuItem3Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
            URI website = null;
            try {
                website = new URI("http://www.google.com/search?q=ToMoCOMD-CAMPS");
            } catch (URISyntaxException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(website);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(app, " ERROR open website: \n "
                            + "--> " + website + " \n"
                            + "restart the application or check the internet connection",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(app, " ERROR getting Desktop API \n "
                        + "this class is NOT supported on the current platform \n"
                        + "restart the application or Reinstall the JRE",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class onlineMenuItem5Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
            URI mailtext = null;

            try {
                mailtext = new URI("mailto:ymarrero77@yahoo.es?cc=jricky31@gmail.com&subject=About%20ToMoCOMD-CAMPS");
            } catch (URISyntaxException ex) {
                Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(mailtext);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(app, " ERROR sending the E-Mail: \n "
                            + "restart the application or check your default mail client",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(app, " ERROR getting Desktop API \n "
                        + "this class is NOT supported on the current platform \n"
                        + "restart the application or Reinstall the JRE",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Show the error dialog
    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(app,
                msg,
                "ToMoCOMD-CAMPS Error",
                JOptionPane.ERROR_MESSAGE);
    }

    //To check the extension on the save file    
    private String checkExtension(String filename, String extension) {
        String aux = "";
        int p = 0;
        while (p < filename.length() && filename.charAt(p) != '.') {
            aux += filename.charAt(p);
            p++;
        }
        String[] tmp = extension.split(" ");
        extension = tmp[tmp.length - 1].replace("(", "");
        extension = extension.replace(")", "");
        return aux + "." + extension;
    }

    public void doSaveProject() 
    {
        int selectedPanel = ui.tabbedPane.getSelectedIndex();
        
        if(selectedPanel==0||selectedPanel==1){
        if (!ui.doApply()) {
            return;
        }
        }
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String lastInputDirectory = prefs.get("LAST_INPUT_DIR", "");
        // first get a filename, using the loaded filename if available
        JFileChooser fileDialog = new JFileChooser();
        if (!lastInputDirectory.equals("")) {
            fileDialog.setCurrentDirectory(new File(lastInputDirectory));
        }
        fileDialog.setApproveButtonText("Save File");
        fileDialog.setDialogTitle("Save Project");
        FileNameExtensionFilter fileFilter = null;
        
        if(selectedPanel==0){
        
        fileFilter = new FileNameExtensionFilter("Project Files (xml)", new String[]{"xml"});
        
        fileDialog.setFileFilter(fileFilter);
        int status = fileDialog.showSaveDialog(ui.getSubpanel());
        if (status != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File settingsFile = fileDialog.getSelectedFile();

        String nameAndExtension = checkExtension(settingsFile.getName(), fileDialog.getFileFilter().getDescription());
        String full_path = settingsFile.getParent() + File.separatorChar + nameAndExtension;

        Element root = new Element("MD-LAIS_project");
        Element options = new Element("options");
        
        Element e = new Element("aaLevel");

        if (ui.isIsAA_level()) {
            e.addAttribute(new Attribute("status", "ON"));

            String windowSize = String.valueOf(aaDialog.getWindowSize());

            e.addAttribute(new Attribute("windowSize", windowSize));
        } else {
            e.addAttribute(new Attribute("status", "OFF"));
        }
        
        options.appendChild(e);
                
        root.appendChild(options);

        ui.saveConfiguration(root);

        Document doc = new Document(root);
        Serializer serializer;
        try 
        {
            serializer = new Serializer(new FileOutputStream(full_path), "ISO-8859-1");
            serializer.setIndent(4);
            serializer.setMaxLength(128);
            serializer.write(doc);

            JOptionPane.showMessageDialog(app, "Project configuration was successfully saved.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            ui.getStatusArea().append("\r\nProject configuration was successfully saved");
        } 
        catch (UnsupportedEncodingException e1) 
        {
            showErrorDialog("Error in serialization of XML document");
        } 
        catch (FileNotFoundException ex) 
        {
            showErrorDialog("Error opening the file to save descriptor selection");
        } 
        catch (IOException ex) 
        {
            showErrorDialog("Error when writing XML document");
        }
        }
        else if(selectedPanel==1)
        {
            fileFilter = new FileNameExtensionFilter("Project Files (csv)", new String[]{"csv"});
        
        fileDialog.setFileFilter(fileFilter);
        int status = fileDialog.showOpenDialog(ui.getSubpanel());
        if (status != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File settingsFile = fileDialog.getSelectedFile();
        
        String nameAndExtension = checkExtension(settingsFile.getName(), fileDialog.getFileFilter().getDescription());
        String full_path = settingsFile.getParent() + File.separatorChar + nameAndExtension;
        
        MDLAIsFormPanelList panel = (MDLAIsFormPanelList) ui.getFormPanel();
        
            try 
            {
                panel.doSaveProject(full_path);
                
                JOptionPane.showMessageDialog(app, "Descriptor list was successfully saved.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
                
                ui.getStatusArea().append("\r\nDescriptor list was successfully saved");
            } catch (IOException ex) 
            {
                showErrorDialog("Error when writing CSV descriptor list");
            }
        }
    }

    class SaveProjectAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doSaveProject();
        }
    }

    public void enabledJToggleButtonDistance(boolean enabled) {
        distanceCenter.setEnabled(enabled);
    }

    private class JCheckBoxMenuItemDistance extends JCheckBoxMenuItem {

        public JCheckBoxMenuItemDistance(String label) {
            super(label);
        }

        @Override
        public void setEnabled(boolean b) {
            super.setEnabled(b);
        }
    }

    public boolean doLoadAndApply(File settingsFile, boolean batchMode) {
        String settingsFileName = settingsFile.getAbsolutePath();
        AppOptions.getInstance().setSettingsFile(settingsFileName);
        Builder parser = new Builder();
        Document doc = null;
        try {
            doLoadProject(settingsFile, parser, doc);

            // do apply  - return true if ok
            return ui.doApply();
        } catch (ParsingException pe) {
            showErrorDialog("The settings file contained invalid XML");
            return false;
        } catch (IOException e1) {
            showErrorDialog("There was an IO error when reading\n" + settingsFileName);
            return false;
        } catch (Exception ex) {
            showErrorDialog("Unknow Error while loading proyect \n\n"
                    + "The settings file may contain invalid XML format \n "
                    + "Error Msg: " + ex.getMessage() + " \n"
                    + "Error Code: " + ex.toString());
            return false;
        } finally {
            if (batchMode) {
                setBlockDuringCompute(false);
            }
        }
    }

    public File[] getSettingsFile2BM() 
    {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        String lastInputDirectory = prefs.get("LAST_INPUT_DIR", "");

        File inputDirectory = new File(System.getProperty("user.dir"), "projects");

        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Project Files (*.xml,*.csv)", new String[]{"xml","csv"});

        JFileChooser fileDialog = new JFileChooser();

        if (!lastInputDirectory.equals("")) 
        {
            fileDialog.setCurrentDirectory(new File(lastInputDirectory));
        } else if (inputDirectory.exists()) 
        {
            fileDialog.setCurrentDirectory(inputDirectory);
        }

        fileDialog.setApproveButtonText("Load File");
        fileDialog.setDialogTitle("Select project files");
        fileDialog.setAcceptAllFileFilterUsed(false);
        fileDialog.setFileFilter(fileFilter);
        
        fileDialog.setMultiSelectionEnabled(true);
        
        int status = fileDialog.showOpenDialog(null);
        
        String partiallyVerified = "";
        
        if (status == JFileChooser.APPROVE_OPTION) {
            for (File settingsFile : fileDialog.getSelectedFiles()) 
            {
                String fileName = settingsFile.getName();
                
                if(fileName.endsWith(".xml"))
            {
            
                try {
                    Builder parser = new Builder();
                    Document doc = null;

                    doLoadProject2BM(settingsFile, parser, doc);

                    ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nProject " + settingsFile.getName() + " was verified");
                    continue;
                } catch (Exception e) 
                {
                    if(e instanceof TomocomdException) 
                    {
                        showErrorDialog(e.getMessage());
                    }
                    else{
                        showErrorDialog("The settings file contained invalid XML");
                    }
                }
            }
                else if(fileName.endsWith(".csv"))
                {
                    MolecularDescriptorHeading dh = new MolecularDescriptorHeading();
                    
                    List<IDescriptor> descriptors = dh.getAlgebraicDescriptorListFromList(settingsFile);
                    
                    if(!descriptors.isEmpty())
                    {
                     if(!dh.getExceptionList().isEmpty()) 
                     {
                        ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nProject " + settingsFile.getName() + " was not sucessfully verified, it contains some invalid descriptor definitions");
                        
                        partiallyVerified+=fileName+"\n";
                     }
                     else
                     {
                       ui.getStatusArea().setText(ui.getStatusArea().getText() + "\r\nProject " + settingsFile.getName() + " was sucessfully verified");
                     }
                      
                      continue;
                    }
                    else
                    {
                    showErrorDialog("The settings file contained invalid descriptor list definition");
                    }
                }

                return null;
            }
        
            if(partiallyVerified.isEmpty())
            {
            JOptionPane.showMessageDialog(app, "Configuration projects were successfully verified", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
            JOptionPane.showMessageDialog(app, "Some projects were not sucessfuly verified. For details, see History TAB.", "Information", JOptionPane.WARNING_MESSAGE);
            }
            
            return fileDialog.getSelectedFiles();
        } else {
            return null;
        }
    }

    class LoadProjectAction implements ActionListener {

        private File directory;

        public LoadProjectAction(File directory) {
            this.directory = directory;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (app.isComputingDescriptors()) {
                JOptionPane.showMessageDialog(app, "It cannot be loaded a new project because the application is computing descriptors", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            doLoadProject(directory);
        }
    }

    public void doLoadProject(File directory) 
    {
        JFileChooser fileDialog = new JFileChooser();

        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
            
        if (directory == null) 
        {
            String lastInputDirectory = prefs.get("LAST_INPUT_DIR", "");

            if (!lastInputDirectory.equals(""))
            {
                fileDialog.setCurrentDirectory(new File(lastInputDirectory));
            }
        } 
        else
        {
            fileDialog.setCurrentDirectory(directory);
        }
        
        fileDialog.setApproveButtonText("Load File");
        fileDialog.setDialogTitle("Load Project");
        fileDialog.setAcceptAllFileFilterUsed(false);
        
        int selectedPanel = ui.tabbedPane.getSelectedIndex();
        FileNameExtensionFilter fileFilter;
        
        if(selectedPanel==0)
        {
        fileFilter = new FileNameExtensionFilter("Project Files (XML)", new String[]{"xml"});
        fileDialog.setFileFilter(fileFilter);
        int status = fileDialog.showOpenDialog(ui.getSubpanel());
        if (status == JFileChooser.APPROVE_OPTION) 
        {
            File settingsFile = fileDialog.getSelectedFile();
            String settingsFileName = settingsFile.getAbsolutePath();
            AppOptions.getInstance().setSettingsFile(settingsFileName);
            Builder parser = new Builder();
            Document doc = null;
            try 
            {
                doLoadProject(settingsFile, parser, doc);

                JOptionPane.showMessageDialog(app, "Project configuration was successfully loaded",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                ui.getStatusArea().append("\r\nProject was Loaded.");
                ui.doApply();
                
            prefs.put("LAST_INPUT_DIR", settingsFile.getParent());
            } 
            catch (ParsingException pe) 
            {
                showErrorDialog("The settings file contains an invalid XML");
            } 
            catch (IOException e1) 
            {
                showErrorDialog("There was an IO error when reading\n" + settingsFileName);
            } 
            catch (HeadlessException ex) 
            {
                showErrorDialog("Unknown Error while loading project \n\n"
                        + "The settings file may contain invalid XML format \n "
                        + "Error Msg: " + ex.getMessage() + " \n"
                        + "Error Code: " + ex.toString());
            } 
            catch (TomocomdException ex) 
            {
                showErrorDialog("Error while loading project \n\n"
                        + "Error Msg: " + ex.getMessage() + " \n");
            }
        }
        }
        else if(selectedPanel==1)
        {
            MDLAIsFormPanelList panel = (MDLAIsFormPanelList) ui.getFormPanel();
            
            try {
                panel.doLoadProject(false);
            } catch (IOException ex) {
                showErrorDialog("Error while loading project \n\n"
                        + "Error Msg: " + ex.getMessage() + " \n");
            }
        }
    }
    
    private void doLoadProject(File settingsFile, Builder parser, Document doc) throws ParsingException, ValidityException, IOException,TomocomdException 
    {
        doc = parser.build(settingsFile);

        Element root = doc.getRootElement();

        ui.initialize();

        setAttributeOnOptions(root.getFirstChildElement("options"));
        
        ui.readConfiguration(root);
    }

    private void doLoadProject2BM(File settingsFile, Builder parser, Document doc) throws ParsingException, ValidityException, IOException, TomocomdException 
    {
        doc = parser.build(settingsFile);

        Element root = doc.getRootElement();

        setAttributeOnOptions(root.getFirstChildElement("options"));        
    }

    private void setAttributeOnOptions(Node node) {
        Element e = (Element) node;
        int n = e.getChildCount();
        for (int i = 0; i < n; i++) {
            if (e.getChild(i) instanceof Element) {
                Element ec = (Element) e.getChild(i);
                String name = ec.getLocalName();
                if (name.equalsIgnoreCase("aaLevel")) {
                    Element el = (Element) e.getChild(i);
                    String status = el.getAttributeValue("status");

                    if (status.equalsIgnoreCase("ON")) {
                        
                        aaDialog.setSelected(true);
                        
                        int windowSize = Integer.parseInt(el.getAttribute("windowSize").getValue());

                        aaDialog.setLagValue(windowSize);
                    } else {
                        aaDialog.setSelected(false);
                    }
                }
            }
        }
    }

    public void setBlockDuringCompute(boolean status) {
        loadMenu.setEnabled(status);
        newMenu.setEnabled(status);
        loadMenu.setEnabled(status);
        loadProjectsMenu.setEnabled(status);
        saveMenu.setEnabled(status);
        batchMenu.setEnabled(status);
        duplicatesMenuItem.setEnabled(status);
        curatorMenuItem.setEnabled(status);
        formatConvertMenuItem.setEnabled(status);
        sequenceEditionMenuItem.setEnabled(status);
        exampleDataMenuItem.setEnabled(status);
        showExceptionsMenu.setEnabled(status);
        featureMenuItem.setEnabled(status);
        
        jButtonLoadFASTA.setEnabled(status);
        jButtonNew.setEnabled(status);
        jButtonLoad.setEnabled(status);
        jButtonSave.setEnabled(status);
        jButtonBatch.setEnabled(status);
        jButtonShowReport.setEnabled(status);
        jButtonExampleData.setEnabled(status);
        jButtonSearchDescriptors.setEnabled(status);
        jTipsButton.setEnabled(status);
        jButtonHome.setEnabled(status);
        showReport.setEnabled(status);
        outputFormatMenu.setEnabled(status);
        CPUMenu.setEnabled(status);
    }
}
