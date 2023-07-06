/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/*
 * InvariantsDialog.java
 *
 * Created on Feb 23, 2011, 12:01:20 AM
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import nu.xom.Attribute;
import nu.xom.Element;
import tomocomd.camps.mdlais.gui.CDKdesc;
import tomocomd.camps.mdlais.tools.invariants.Choquet;
import tomocomd.camps.mdlais.tools.invariants.DimensionType;
import tomocomd.camps.mdlais.tools.invariants.InvariantType;
import tomocomd.camps.mdlais.tools.invariants.OWAWA;
import tomocomd.camps.mdlais.tools.invariants.InvariantSymbol;

/**
 *
 * @author jricardo
 * @author crjacas
 */
public class InvariantsDialog extends javax.swing.JDialog {

    private int TOTAL_NORMS = 3;
    private int TOTAL_MEANS = 5;
    private int TOTAL_STATISTICS = 12;
    final private int TOTAL_CLASSICS = 22;
    final private int TOTAL_CLASSICS_LAI = 1;
    
    private int TOTAL_CLASSICS_INF_THEORY = 7;
    
    private int TOTAL_CLASSICS_ALGORITHMS = 9;
    
    private int TOTAL_CLASSICS_OTHER = 5;
    
    private int currentNorms;
    private int currentMeans;
    private int currentStatistics;
    private int currentClassicsLAI, currentClassicsInfTheory,currentClassicsAlgortihms, currentClassicsOther,
            currentClassics;
    
    private String maxK = "7";

    private final ArrayList<String> list_classics_invariants;
    private final ArrayList<String> list_no_classic_invariants;
    private final HashMap<InvariantType, Object[]> invariant_parameters;
    private final HashMap<String, HashMap<OWAWA.PARAMETER_NAMES, Object>> list_owawas;
    private final HashMap<String, HashMap<Choquet.PARAMETER_NAMES, Object>> list_choquets;

    final private ListDialog4ChoquetOWAWA owawaListDialog;
    final private ListDialog4ChoquetOWAWA choquetListDialog;
    

    public InvariantsDialog(CDKdesc cdk) {
        super(cdk, true);

        this.list_classics_invariants = new ArrayList<>();
        this.list_no_classic_invariants = new ArrayList<>();
        this.list_owawas = new HashMap<>();
        this.list_choquets = new HashMap<>();
        this.invariant_parameters = new HashMap<>();

        initComponents();
        uncheck_all.doClick();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width / 2 - getSize().width / 2) - 38, screen.height / 2 - getSize().height / 2);
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
        setTitle("Aggregation Operators");

        owawaListDialog = new ListDialog4ChoquetOWAWA("GOWAWA-based Configurations List", this, false, new IListDialog4ChoquetOWAWA() {
            @Override
            public void updateOperatorList(String key) {
                String[] attr = key.split("//");

                jSpinnerOWAWABeta.setValue(Float.parseFloat(attr[0]));
                jSpinnerOWALambda.setValue(Integer.parseInt(attr[1]));
                jComboBoxOWAWeighted.setSelectedItem(attr[2]);
                jSpinnerOWAAlfa.setValue(Float.parseFloat(attr[3]));
                jSpinnerOWABeta.setValue(Float.parseFloat(attr[4]));
                jSpinnerWADelta.setValue(Integer.parseInt(attr[5]));
                jComboBoxWAWeighted.setSelectedItem(attr[6]);
                jSpinnerWAAlfa.setValue(Float.parseFloat(attr[7]));
                jSpinnerWABeta.setValue(Float.parseFloat(attr[8]));
            }

            @Override
            public void removeOperator(String key) {
                list_owawas.remove(key);
                owawaListDialog.updateList(list_owawas.keySet());
                jLabelOWAWAInfo.setText(list_owawas.size() == 1 ? "1 configuration" : list_owawas.size() + " configurations");
            }
        });

        choquetListDialog = new ListDialog4ChoquetOWAWA("Choquet Integral-based Configurations List", this, false, new IListDialog4ChoquetOWAWA() {
            @Override
            public void updateOperatorList(String key) {
                String[] attr = key.split("//");

                jComboBoxLovisOrder.setSelectedItem(attr[0]);
                jSpinnerChoquet.setValue(Float.parseFloat(attr[1]));
                jComboBoxChoquetMethod.setSelectedItem(attr[2]);
                jSpinnerChoquetAlfaMethod.setValue(Float.parseFloat(attr[3]));
            }

            @Override
            public void removeOperator(String key) {
                list_choquets.remove(key);
                choquetListDialog.updateList(list_choquets.keySet());
                jLabelChoquetInfo.setText(list_choquets.size() == 1 ? "1 configuration" : list_choquets.size() + " configurations");
            }
        });

        setNoClassicInvPanelState(false);
        //exclude owawawa y choquet
        jTabbedPane1.remove(4);
        jTabbedPane1.remove(4);
    }

    public HashMap<InvariantType, Object[]> getInvariantParameters() {
        return invariant_parameters;
    }

    public String getMaximumK() {
        return maxK;
    }

    public void setMaximumK(String k) {
        maxK = k;
    }

    public ArrayList<String> getClassicsInvariantsList() {
        return list_classics_invariants;
    }

    public void setNewClassicInvariant(String newInvariant) {
        if (!list_classics_invariants.contains(newInvariant)) {
            list_classics_invariants.add(newInvariant);
        }
    }

    public ArrayList<String> getNoClassicsInvariants() {
        return list_no_classic_invariants;
    }

    public void setNewNonClassicInvariant(String newInvariant) {
        if (!list_no_classic_invariants.contains(newInvariant)) {
            list_no_classic_invariants.add(newInvariant);
        }
    }

    public int getNumberOWAWAConfigurations() {
        return list_owawas.size();
    }

    public int getNumberChoquetConfigurations() {
        return list_choquets.size();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel39 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        invLaiToggleButton = new javax.swing.JToggleButton();
        lai = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        sic = new javax.swing.JCheckBox();
        tic = new javax.swing.JCheckBox();
        mic = new javax.swing.JCheckBox();
        sicn = new javax.swing.JCheckBox();
        micn = new javax.swing.JCheckBox();
        ticn = new javax.swing.JCheckBox();
        entropy = new javax.swing.JCheckBox();
        jPanel40 = new javax.swing.JPanel();
        invInfTheoryToggleButton = new javax.swing.JToggleButton();
        jPanel15 = new javax.swing.JPanel();
        kh = new javax.swing.JCheckBox();
        ts = new javax.swing.JCheckBox();
        ac = new javax.swing.JCheckBox();
        ib = new javax.swing.JCheckBox();
        gv = new javax.swing.JCheckBox();
        es = new javax.swing.JCheckBox();
        rdf = new javax.swing.JCheckBox();
        morse = new javax.swing.JCheckBox();
        is = new javax.swing.JCheckBox();
        jPanel41 = new javax.swing.JPanel();
        invClassicalAlgorithmsToggleButton = new javax.swing.JToggleButton();
        jPanel37 = new javax.swing.JPanel();
        apm = new javax.swing.JCheckBox();
        bft = new javax.swing.JCheckBox();
        cei = new javax.swing.JCheckBox();
        gc = new javax.swing.JCheckBox();
        pcd = new javax.swing.JCheckBox();
        jPanel42 = new javax.swing.JPanel();
        invOtherToggleButton = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        n1 = new javax.swing.JCheckBox();
        n2 = new javax.swing.JCheckBox();
        n3 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        normToggleButton = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        g = new javax.swing.JCheckBox();
        a = new javax.swing.JCheckBox();
        p2 = new javax.swing.JCheckBox();
        p3 = new javax.swing.JCheckBox();
        h = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        meanToggleButton = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        v = new javax.swing.JCheckBox();
        s = new javax.swing.JCheckBox();
        k = new javax.swing.JCheckBox();
        sd = new javax.swing.JCheckBox();
        vc = new javax.swing.JCheckBox();
        ra = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        q1 = new javax.swing.JCheckBox();
        q2 = new javax.swing.JCheckBox();
        q3 = new javax.swing.JCheckBox();
        i50 = new javax.swing.JCheckBox();
        mx = new javax.swing.JCheckBox();
        mn = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        statsToggleButton = new javax.swing.JToggleButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jSpinnerOWAAlfa = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerOWABeta = new javax.swing.JSpinner();
        jComboBoxOWAWeighted = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSpinnerOWALambda = new javax.swing.JSpinner();
        jPanel23 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jSpinnerWAAlfa = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jSpinnerWABeta = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        jComboBoxWAWeighted = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jSpinnerWADelta = new javax.swing.JSpinner();
        jPanel27 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jSpinnerOWAWABeta = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        owawa = new javax.swing.JCheckBox();
        jPanel29 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jButtonAddOWAWAConfig = new javax.swing.JButton();
        jButtonRemoveOWAWAConfig = new javax.swing.JButton();
        jButtonViewOWAWAConfig = new javax.swing.JButton();
        jLabelOWAWAInfo = new javax.swing.JLabel();
        jButtonDefaultOWAWA = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jSpinnerChoquetAlfaMethod = new javax.swing.JSpinner();
        jComboBoxChoquetMethod = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxLovisOrder = new javax.swing.JComboBox<>();
        jPanel34 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jSpinnerChoquet = new javax.swing.JSpinner();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        choquet = new javax.swing.JCheckBox();
        jPanel35 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jButtonAddChoquetConfig = new javax.swing.JButton();
        jButtonRemoveChoquetConfig = new javax.swing.JButton();
        jButtonViewChoquetConfig = new javax.swing.JButton();
        jLabelChoquetInfo = new javax.swing.JLabel();
        jButtonDefaultChoquet = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        check_all = new javax.swing.JButton();
        uncheck_all = new javax.swing.JButton();
        ok = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Invariants");
        setMaximumSize(new java.awt.Dimension(580, 350));
        setMinimumSize(new java.awt.Dimension(580, 350));
        setPreferredSize(new java.awt.Dimension(580, 350));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(560, 260));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(560, 260));
        jTabbedPane1.setNextFocusableComponent(n1);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(560, 260));
        jTabbedPane1.setRequestFocusEnabled(false);

        jPanel5.setMaximumSize(new java.awt.Dimension(550, 240));
        jPanel5.setMinimumSize(new java.awt.Dimension(550, 240));
        jPanel5.setPreferredSize(new java.awt.Dimension(550, 240));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(560, 250));
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(560, 250));

        jPanel39.setMinimumSize(new java.awt.Dimension(520, 180));
        jPanel39.setPreferredSize(new java.awt.Dimension(520, 180));
        jPanel39.setLayout(new java.awt.GridBagLayout());

        jPanel19.setMinimumSize(new java.awt.Dimension(520, 32));
        jPanel19.setPreferredSize(new java.awt.Dimension(520, 32));
        jPanel19.setLayout(new java.awt.GridBagLayout());

        invLaiToggleButton.setText("Check");
        invLaiToggleButton.setMaximumSize(new java.awt.Dimension(115, 27));
        invLaiToggleButton.setMinimumSize(new java.awt.Dimension(115, 27));
        invLaiToggleButton.setPreferredSize(new java.awt.Dimension(115, 27));
        invLaiToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invLaiToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, -425, 5, 0);
        jPanel19.add(invLaiToggleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel39.add(jPanel19, gridBagConstraints);

        lai.setText("LAI Vector ");
        lai.setMaximumSize(new java.awt.Dimension(200, 25));
        lai.setMinimumSize(new java.awt.Dimension(200, 25));
        lai.setPreferredSize(new java.awt.Dimension(200, 25));
        lai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel39.add(lai, gridBagConstraints);

        jLabel6.setPreferredSize(new java.awt.Dimension(300, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel39.add(jLabel6, gridBagConstraints);

        jPanel38.setMinimumSize(new java.awt.Dimension(10, 100));
        jPanel38.setPreferredSize(new java.awt.Dimension(10, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel39.add(jPanel38, gridBagConstraints);

        jTabbedPane2.addTab("Original Vector", jPanel39);

        jPanel14.setMaximumSize(new java.awt.Dimension(520, 180));
        jPanel14.setMinimumSize(new java.awt.Dimension(520, 180));
        jPanel14.setPreferredSize(new java.awt.Dimension(520, 180));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        sic.setText("(SIC) Std. Information Content");
        sic.setMaximumSize(new java.awt.Dimension(240, 25));
        sic.setMinimumSize(new java.awt.Dimension(240, 25));
        sic.setPreferredSize(new java.awt.Dimension(240, 25));
        sic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(sic, gridBagConstraints);

        tic.setText("(TIC) Total Information Content");
        tic.setMaximumSize(new java.awt.Dimension(240, 25));
        tic.setMinimumSize(new java.awt.Dimension(240, 25));
        tic.setPreferredSize(new java.awt.Dimension(240, 25));
        tic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(tic, gridBagConstraints);

        mic.setText("(MIC) Mean Information Content");
        mic.setMaximumSize(new java.awt.Dimension(240, 25));
        mic.setMinimumSize(new java.awt.Dimension(240, 25));
        mic.setPreferredSize(new java.awt.Dimension(240, 25));
        mic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                micActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(mic, gridBagConstraints);

        sicn.setText("(SICN) Std.Information Content (N-bins)");
        sicn.setMaximumSize(new java.awt.Dimension(270, 25));
        sicn.setMinimumSize(new java.awt.Dimension(270, 25));
        sicn.setPreferredSize(new java.awt.Dimension(270, 25));
        sicn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sicnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(sicn, gridBagConstraints);

        micn.setText("(MICN) Mean Information Content (N-bins)");
        micn.setMaximumSize(new java.awt.Dimension(270, 25));
        micn.setMinimumSize(new java.awt.Dimension(270, 25));
        micn.setPreferredSize(new java.awt.Dimension(270, 25));
        micn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                micnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(micn, gridBagConstraints);

        ticn.setText("(TICN) Total Information Content (N-bins)");
        ticn.setMaximumSize(new java.awt.Dimension(270, 25));
        ticn.setMinimumSize(new java.awt.Dimension(270, 25));
        ticn.setPreferredSize(new java.awt.Dimension(270, 25));
        ticn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(ticn, gridBagConstraints);

        entropy.setText("(H) Entropy");
        entropy.setMaximumSize(new java.awt.Dimension(270, 25));
        entropy.setMinimumSize(new java.awt.Dimension(270, 25));
        entropy.setPreferredSize(new java.awt.Dimension(270, 25));
        entropy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entropyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(entropy, gridBagConstraints);

        jPanel40.setMinimumSize(new java.awt.Dimension(520, 32));
        jPanel40.setPreferredSize(new java.awt.Dimension(520, 32));
        jPanel40.setLayout(new java.awt.GridBagLayout());

        invInfTheoryToggleButton.setText("Check");
        invInfTheoryToggleButton.setMaximumSize(new java.awt.Dimension(115, 27));
        invInfTheoryToggleButton.setMinimumSize(new java.awt.Dimension(115, 27));
        invInfTheoryToggleButton.setPreferredSize(new java.awt.Dimension(115, 27));
        invInfTheoryToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invInfTheoryToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, -425, 5, 0);
        jPanel40.add(invInfTheoryToggleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 5, 0, 0);
        jPanel14.add(jPanel40, gridBagConstraints);

        jTabbedPane2.addTab("Information-Theory", jPanel14);

        jPanel15.setPreferredSize(new java.awt.Dimension(540, 160));
        jPanel15.setLayout(new java.awt.GridBagLayout());

        kh.setText("(KH) Kier-Hall");
        kh.setMaximumSize(new java.awt.Dimension(240, 25));
        kh.setMinimumSize(new java.awt.Dimension(240, 25));
        kh.setPreferredSize(new java.awt.Dimension(240, 25));
        kh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                khActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(kh, gridBagConstraints);

        ts.setText("(TS) Total Sum");
        ts.setMaximumSize(new java.awt.Dimension(240, 25));
        ts.setMinimumSize(new java.awt.Dimension(240, 25));
        ts.setPreferredSize(new java.awt.Dimension(240, 25));
        ts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(ts, gridBagConstraints);

        ac.setText("(AC) AutoCorrelation");
        ac.setMaximumSize(new java.awt.Dimension(240, 25));
        ac.setMinimumSize(new java.awt.Dimension(240, 25));
        ac.setPreferredSize(new java.awt.Dimension(240, 25));
        ac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(ac, gridBagConstraints);

        ib.setText("(IB) Ivanciuc-Balaban");
        ib.setMaximumSize(new java.awt.Dimension(240, 25));
        ib.setMinimumSize(new java.awt.Dimension(240, 25));
        ib.setPreferredSize(new java.awt.Dimension(240, 25));
        ib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ibActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(ib, gridBagConstraints);

        gv.setText("(GV) Gravitational");
        gv.setMaximumSize(new java.awt.Dimension(240, 25));
        gv.setMinimumSize(new java.awt.Dimension(240, 25));
        gv.setPreferredSize(new java.awt.Dimension(240, 25));
        gv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gvActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(gv, gridBagConstraints);

        es.setText("(ES) Electrotopological");
        es.setMaximumSize(new java.awt.Dimension(240, 25));
        es.setMinimumSize(new java.awt.Dimension(240, 25));
        es.setPreferredSize(new java.awt.Dimension(240, 25));
        es.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                esActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(es, gridBagConstraints);

        rdf.setText("(RDF) RDF Code");
        rdf.setMaximumSize(new java.awt.Dimension(240, 25));
        rdf.setMinimumSize(new java.awt.Dimension(240, 25));
        rdf.setPreferredSize(new java.awt.Dimension(220, 25));
        rdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdfActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(rdf, gridBagConstraints);

        morse.setText("(MSE) MoRSE");
        morse.setMaximumSize(new java.awt.Dimension(240, 25));
        morse.setMinimumSize(new java.awt.Dimension(240, 25));
        morse.setPreferredSize(new java.awt.Dimension(220, 25));
        morse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                morseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(morse, gridBagConstraints);

        is.setText("(IS) Interaction Spectrum");
        is.setMaximumSize(new java.awt.Dimension(240, 25));
        is.setMinimumSize(new java.awt.Dimension(240, 25));
        is.setPreferredSize(new java.awt.Dimension(220, 25));
        is.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(is, gridBagConstraints);

        jPanel41.setMinimumSize(new java.awt.Dimension(520, 32));
        jPanel41.setPreferredSize(new java.awt.Dimension(520, 32));
        jPanel41.setLayout(new java.awt.GridBagLayout());

        invClassicalAlgorithmsToggleButton.setText("Check");
        invClassicalAlgorithmsToggleButton.setMaximumSize(new java.awt.Dimension(115, 27));
        invClassicalAlgorithmsToggleButton.setMinimumSize(new java.awt.Dimension(115, 27));
        invClassicalAlgorithmsToggleButton.setPreferredSize(new java.awt.Dimension(115, 27));
        invClassicalAlgorithmsToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invClassicalAlgorithmsToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, -425, 5, 0);
        jPanel41.add(invClassicalAlgorithmsToggleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel15.add(jPanel41, gridBagConstraints);

        jTabbedPane2.addTab("Classical Algorithms", jPanel15);

        jPanel37.setPreferredSize(new java.awt.Dimension(540, 160));
        jPanel37.setLayout(new java.awt.GridBagLayout());

        apm.setText("(APM) Amphiphilic/Amphipatic Moments");
        apm.setMaximumSize(new java.awt.Dimension(250, 25));
        apm.setMinimumSize(new java.awt.Dimension(250, 25));
        apm.setPreferredSize(new java.awt.Dimension(250, 25));
        apm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apmActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel37.add(apm, gridBagConstraints);

        bft.setText("(BFT) Beteringhe-Filip-Tarko ");
        bft.setMaximumSize(new java.awt.Dimension(240, 25));
        bft.setMinimumSize(new java.awt.Dimension(240, 25));
        bft.setPreferredSize(new java.awt.Dimension(240, 25));
        bft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bftActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel37.add(bft, gridBagConstraints);

        cei.setText("(CEI) Connective Eccentricity Index");
        cei.setMaximumSize(new java.awt.Dimension(240, 25));
        cei.setMinimumSize(new java.awt.Dimension(240, 25));
        cei.setPreferredSize(new java.awt.Dimension(270, 25));
        cei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel37.add(cei, gridBagConstraints);

        gc.setText("(GC) Geary Coefficient");
        gc.setMaximumSize(new java.awt.Dimension(240, 25));
        gc.setMinimumSize(new java.awt.Dimension(240, 25));
        gc.setPreferredSize(new java.awt.Dimension(270, 25));
        gc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel37.add(gc, gridBagConstraints);

        pcd.setText("(PCD) Potential of Charge Distribution");
        pcd.setMaximumSize(new java.awt.Dimension(240, 25));
        pcd.setMinimumSize(new java.awt.Dimension(240, 25));
        pcd.setPreferredSize(new java.awt.Dimension(270, 25));
        pcd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pcdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel37.add(pcd, gridBagConstraints);

        jPanel42.setMinimumSize(new java.awt.Dimension(520, 32));
        jPanel42.setPreferredSize(new java.awt.Dimension(520, 32));
        jPanel42.setLayout(new java.awt.GridBagLayout());

        invOtherToggleButton.setText("Check");
        invOtherToggleButton.setMaximumSize(new java.awt.Dimension(115, 27));
        invOtherToggleButton.setMinimumSize(new java.awt.Dimension(115, 27));
        invOtherToggleButton.setPreferredSize(new java.awt.Dimension(115, 27));
        invOtherToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invOtherToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, -425, 5, 0);
        jPanel42.add(invOtherToggleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 5, 0, 0);
        jPanel37.add(jPanel42, gridBagConstraints);

        jTabbedPane2.addTab("Other", jPanel37);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel5.add(jTabbedPane2, gridBagConstraints);

        jTabbedPane1.addTab("Classics", jPanel5);

        jPanel2.setAutoscrolls(true);
        jPanel2.setDoubleBuffered(false);
        jPanel2.setMaximumSize(new java.awt.Dimension(519, 222));
        jPanel2.setMinimumSize(new java.awt.Dimension(519, 222));
        jPanel2.setPreferredSize(new java.awt.Dimension(519, 222));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel7.setMaximumSize(new java.awt.Dimension(250, 150));
        jPanel7.setMinimumSize(new java.awt.Dimension(250, 150));
        jPanel7.setPreferredSize(new java.awt.Dimension(250, 150));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        n1.setText("(N1) Manhattan Distance");
        n1.setMaximumSize(new java.awt.Dimension(250, 25));
        n1.setMinimumSize(new java.awt.Dimension(250, 25));
        n1.setPreferredSize(new java.awt.Dimension(250, 25));
        n1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel7.add(n1, gridBagConstraints);

        n2.setText("(N2) Euclidean Distance");
        n2.setMaximumSize(new java.awt.Dimension(250, 25));
        n2.setMinimumSize(new java.awt.Dimension(250, 25));
        n2.setPreferredSize(new java.awt.Dimension(250, 25));
        n2.setRequestFocusEnabled(false);
        n2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel7.add(n2, gridBagConstraints);

        n3.setText("(N3) Minkowski Distance");
        n3.setMaximumSize(new java.awt.Dimension(250, 25));
        n3.setMinimumSize(new java.awt.Dimension(250, 25));
        n3.setPreferredSize(new java.awt.Dimension(250, 25));
        n3.setRequestFocusEnabled(false);
        n3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel7.add(n3, gridBagConstraints);

        jLabel1.setMaximumSize(new java.awt.Dimension(250, 25));
        jLabel1.setMinimumSize(new java.awt.Dimension(250, 25));
        jLabel1.setPreferredSize(new java.awt.Dimension(250, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel7.add(jLabel1, gridBagConstraints);

        jLabel3.setMaximumSize(new java.awt.Dimension(250, 25));
        jLabel3.setMinimumSize(new java.awt.Dimension(250, 25));
        jLabel3.setPreferredSize(new java.awt.Dimension(250, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel7.add(jLabel3, gridBagConstraints);

        jPanel2.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        normToggleButton.setText("Check");
        normToggleButton.setMaximumSize(new java.awt.Dimension(95, 27));
        normToggleButton.setMinimumSize(new java.awt.Dimension(95, 27));
        normToggleButton.setPreferredSize(new java.awt.Dimension(95, 27));
        normToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, -425, 5, 0);
        jPanel16.add(normToggleButton, gridBagConstraints);

        jPanel2.add(jPanel16, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Norms", jPanel2);

        jPanel3.setDoubleBuffered(false);
        jPanel3.setMaximumSize(new java.awt.Dimension(519, 222));
        jPanel3.setMinimumSize(new java.awt.Dimension(519, 222));
        jPanel3.setPreferredSize(new java.awt.Dimension(519, 222));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel8.setMaximumSize(new java.awt.Dimension(255, 150));
        jPanel8.setMinimumSize(new java.awt.Dimension(255, 150));
        jPanel8.setPreferredSize(new java.awt.Dimension(255, 150));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        g.setText("(GM) Geometric Mean");
        g.setMaximumSize(new java.awt.Dimension(255, 25));
        g.setMinimumSize(new java.awt.Dimension(255, 25));
        g.setPreferredSize(new java.awt.Dimension(255, 25));
        g.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel8.add(g, gridBagConstraints);

        a.setText("(AM) Arithmetic Mean (alfa = 1)");
        a.setMaximumSize(new java.awt.Dimension(255, 25));
        a.setMinimumSize(new java.awt.Dimension(255, 25));
        a.setPreferredSize(new java.awt.Dimension(255, 25));
        a.setRequestFocusEnabled(false);
        a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel8.add(a, gridBagConstraints);

        p2.setText("(P2) Quadratic Mean (alfa = 2)");
        p2.setMaximumSize(new java.awt.Dimension(255, 25));
        p2.setMinimumSize(new java.awt.Dimension(255, 25));
        p2.setPreferredSize(new java.awt.Dimension(255, 25));
        p2.setRequestFocusEnabled(false);
        p2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel8.add(p2, gridBagConstraints);

        p3.setText("(P3) Potential Mean (alfa = 3)");
        p3.setMaximumSize(new java.awt.Dimension(255, 25));
        p3.setMinimumSize(new java.awt.Dimension(255, 25));
        p3.setPreferredSize(new java.awt.Dimension(255, 25));
        p3.setRequestFocusEnabled(false);
        p3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel8.add(p3, gridBagConstraints);

        h.setText("(HM) Harmonic Mean (alfa = -1)");
        h.setMaximumSize(new java.awt.Dimension(255, 25));
        h.setMinimumSize(new java.awt.Dimension(255, 25));
        h.setPreferredSize(new java.awt.Dimension(255, 25));
        h.setRequestFocusEnabled(false);
        h.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel8.add(h, gridBagConstraints);

        jLabel2.setMaximumSize(new java.awt.Dimension(255, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(255, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(255, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel8.add(jLabel2, gridBagConstraints);

        jPanel3.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel17.setLayout(new java.awt.GridBagLayout());

        meanToggleButton.setText("Check");
        meanToggleButton.setMaximumSize(new java.awt.Dimension(95, 27));
        meanToggleButton.setMinimumSize(new java.awt.Dimension(95, 27));
        meanToggleButton.setPreferredSize(new java.awt.Dimension(95, 27));
        meanToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meanToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, -425, 5, 0);
        jPanel17.add(meanToggleButton, gridBagConstraints);

        jPanel3.add(jPanel17, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Means", jPanel3);

        jPanel4.setMaximumSize(new java.awt.Dimension(519, 222));
        jPanel4.setMinimumSize(new java.awt.Dimension(519, 222));
        jPanel4.setPreferredSize(new java.awt.Dimension(519, 222));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel9.setMaximumSize(new java.awt.Dimension(595, 125));
        jPanel9.setMinimumSize(new java.awt.Dimension(595, 125));
        jPanel9.setPreferredSize(new java.awt.Dimension(595, 125));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel10.setMaximumSize(new java.awt.Dimension(205, 150));
        jPanel10.setMinimumSize(new java.awt.Dimension(205, 150));
        jPanel10.setPreferredSize(new java.awt.Dimension(205, 150));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        v.setText("(V) Variance");
        v.setMaximumSize(new java.awt.Dimension(200, 25));
        v.setMinimumSize(new java.awt.Dimension(200, 25));
        v.setPreferredSize(new java.awt.Dimension(200, 25));
        v.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel10.add(v, gridBagConstraints);

        s.setText("(S) Skewness");
        s.setMaximumSize(new java.awt.Dimension(200, 25));
        s.setMinimumSize(new java.awt.Dimension(200, 25));
        s.setPreferredSize(new java.awt.Dimension(200, 25));
        s.setRequestFocusEnabled(false);
        s.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel10.add(s, gridBagConstraints);

        k.setText("(K) Kurtosis");
        k.setMaximumSize(new java.awt.Dimension(200, 25));
        k.setMinimumSize(new java.awt.Dimension(200, 25));
        k.setPreferredSize(new java.awt.Dimension(200, 25));
        k.setRequestFocusEnabled(false);
        k.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel10.add(k, gridBagConstraints);

        sd.setText("(SD) Standard Deviation");
        sd.setMaximumSize(new java.awt.Dimension(200, 25));
        sd.setMinimumSize(new java.awt.Dimension(200, 25));
        sd.setPreferredSize(new java.awt.Dimension(200, 25));
        sd.setRequestFocusEnabled(false);
        sd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel10.add(sd, gridBagConstraints);

        vc.setText("(VC) Variation Coefficient");
        vc.setMaximumSize(new java.awt.Dimension(205, 25));
        vc.setMinimumSize(new java.awt.Dimension(205, 25));
        vc.setPreferredSize(new java.awt.Dimension(205, 25));
        vc.setRequestFocusEnabled(false);
        vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel10.add(vc, gridBagConstraints);

        ra.setText("(RA) Range");
        ra.setMaximumSize(new java.awt.Dimension(155, 25));
        ra.setMinimumSize(new java.awt.Dimension(155, 25));
        ra.setPreferredSize(new java.awt.Dimension(155, 25));
        ra.setRequestFocusEnabled(false);
        ra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                raActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel10.add(ra, gridBagConstraints);

        jPanel9.add(jPanel10, java.awt.BorderLayout.WEST);

        jPanel11.setMaximumSize(new java.awt.Dimension(155, 150));
        jPanel11.setMinimumSize(new java.awt.Dimension(155, 150));
        jPanel11.setPreferredSize(new java.awt.Dimension(155, 150));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        q1.setText("(Q1) Percentile 25");
        q1.setMaximumSize(new java.awt.Dimension(270, 25));
        q1.setMinimumSize(new java.awt.Dimension(270, 25));
        q1.setPreferredSize(new java.awt.Dimension(270, 25));
        q1.setRequestFocusEnabled(false);
        q1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                q1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel11.add(q1, gridBagConstraints);

        q2.setText("(Q2) Percentile 50 ");
        q2.setMaximumSize(new java.awt.Dimension(155, 25));
        q2.setMinimumSize(new java.awt.Dimension(155, 25));
        q2.setPreferredSize(new java.awt.Dimension(155, 25));
        q2.setRequestFocusEnabled(false);
        q2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                q2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel11.add(q2, gridBagConstraints);

        q3.setText("(Q3) Percentile 75 ");
        q3.setMaximumSize(new java.awt.Dimension(155, 25));
        q3.setMinimumSize(new java.awt.Dimension(155, 25));
        q3.setPreferredSize(new java.awt.Dimension(155, 25));
        q3.setRequestFocusEnabled(false);
        q3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                q3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel11.add(q3, gridBagConstraints);

        i50.setText("(i50) Q3-Q1");
        i50.setToolTipText("");
        i50.setMaximumSize(new java.awt.Dimension(150, 25));
        i50.setMinimumSize(new java.awt.Dimension(150, 25));
        i50.setPreferredSize(new java.awt.Dimension(150, 25));
        i50.setRequestFocusEnabled(false);
        i50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i50ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel11.add(i50, gridBagConstraints);

        mx.setText("(MX) XMax");
        mx.setMaximumSize(new java.awt.Dimension(105, 25));
        mx.setMinimumSize(new java.awt.Dimension(105, 25));
        mx.setPreferredSize(new java.awt.Dimension(105, 25));
        mx.setRequestFocusEnabled(false);
        mx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel11.add(mx, gridBagConstraints);

        mn.setText("(MN) XMin");
        mn.setMaximumSize(new java.awt.Dimension(105, 25));
        mn.setMinimumSize(new java.awt.Dimension(105, 25));
        mn.setPreferredSize(new java.awt.Dimension(105, 25));
        mn.setRequestFocusEnabled(false);
        mn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel11.add(mn, gridBagConstraints);

        jPanel9.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel9, java.awt.BorderLayout.WEST);

        jPanel18.setLayout(new java.awt.GridBagLayout());

        statsToggleButton.setText("Check");
        statsToggleButton.setMaximumSize(new java.awt.Dimension(95, 27));
        statsToggleButton.setMinimumSize(new java.awt.Dimension(95, 27));
        statsToggleButton.setPreferredSize(new java.awt.Dimension(95, 27));
        statsToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, -425, 5, 0);
        jPanel18.add(statsToggleButton, gridBagConstraints);

        jPanel4.add(jPanel18, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Statistics", jPanel4);

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel20.setMaximumSize(new java.awt.Dimension(540, 150));
        jPanel20.setMinimumSize(new java.awt.Dimension(540, 150));
        jPanel20.setPreferredSize(new java.awt.Dimension(540, 150));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jPanel22.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel22.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel22.setName(""); // NOI18N
        jPanel22.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("α: ");
        jLabel7.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel7.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel7.setName(""); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel22.add(jLabel7, gridBagConstraints);

        jSpinnerOWAAlfa.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        jSpinnerOWAAlfa.setEnabled(false);
        jSpinnerOWAAlfa.setMaximumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWAAlfa.setMinimumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWAAlfa.setPreferredSize(new java.awt.Dimension(47, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel22.add(jSpinnerOWAAlfa, gridBagConstraints);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("β: ");
        jLabel8.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel8.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel8.setName(""); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanel22.add(jLabel8, gridBagConstraints);

        jSpinnerOWABeta.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        jSpinnerOWABeta.setEnabled(false);
        jSpinnerOWABeta.setMaximumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWABeta.setMinimumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWABeta.setPreferredSize(new java.awt.Dimension(47, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        jPanel22.add(jSpinnerOWABeta, gridBagConstraints);

        jComboBoxOWAWeighted.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "S_OWA", "WINDOW_OWA", "EXPONENTIAL_SMOOTHING_1", "EXPONENTIAL_SMOOTHING_2", "AGGREGATED_OBJECTS_1", "AGGREGATED_OBJECTS_2" }));
        jComboBoxOWAWeighted.setEnabled(false);
        jComboBoxOWAWeighted.setMaximumSize(new java.awt.Dimension(210, 25));
        jComboBoxOWAWeighted.setMinimumSize(new java.awt.Dimension(210, 25));
        jComboBoxOWAWeighted.setPreferredSize(new java.awt.Dimension(210, 25));
        jComboBoxOWAWeighted.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxOWAWeightedItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 17, 0, 0);
        jPanel22.add(jComboBoxOWAWeighted, gridBagConstraints);

        jLabel11.setText("GOWA");
        jLabel11.setMaximumSize(new java.awt.Dimension(75, 25));
        jLabel11.setMinimumSize(new java.awt.Dimension(75, 25));
        jLabel11.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel22.add(jLabel11, gridBagConstraints);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("λ:");
        jLabel15.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel15.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel15.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        jPanel22.add(jLabel15, gridBagConstraints);

        jSpinnerOWALambda.setModel(new javax.swing.SpinnerNumberModel(1, -1, 2, 1));
        jSpinnerOWALambda.setEnabled(false);
        jSpinnerOWALambda.setMaximumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWALambda.setMinimumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWALambda.setPreferredSize(new java.awt.Dimension(47, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel22.add(jSpinnerOWALambda, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel20.add(jPanel22, gridBagConstraints);

        jPanel23.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel23.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel23.setName(""); // NOI18N
        jPanel23.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel23.setRequestFocusEnabled(false);
        jPanel23.setLayout(new java.awt.GridBagLayout());

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("α: ");
        jLabel9.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel9.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel9.setName(""); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jLabel9, gridBagConstraints);

        jSpinnerWAAlfa.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        jSpinnerWAAlfa.setEnabled(false);
        jSpinnerWAAlfa.setMaximumSize(new java.awt.Dimension(47, 25));
        jSpinnerWAAlfa.setMinimumSize(new java.awt.Dimension(47, 25));
        jSpinnerWAAlfa.setName(""); // NOI18N
        jSpinnerWAAlfa.setPreferredSize(new java.awt.Dimension(47, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jSpinnerWAAlfa, gridBagConstraints);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("β: ");
        jLabel10.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel10.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel10.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jLabel10, gridBagConstraints);

        jSpinnerWABeta.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        jSpinnerWABeta.setEnabled(false);
        jSpinnerWABeta.setMaximumSize(new java.awt.Dimension(47, 25));
        jSpinnerWABeta.setMinimumSize(new java.awt.Dimension(47, 25));
        jSpinnerWABeta.setPreferredSize(new java.awt.Dimension(47, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jSpinnerWABeta, gridBagConstraints);

        jLabel14.setText("  WGM");
        jLabel14.setMaximumSize(new java.awt.Dimension(75, 25));
        jLabel14.setMinimumSize(new java.awt.Dimension(75, 25));
        jLabel14.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jLabel14, gridBagConstraints);

        jComboBoxWAWeighted.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "S_OWA", "WINDOW_OWA", "EXPONENTIAL_SMOOTHING_1", "EXPONENTIAL_SMOOTHING_2" }));
        jComboBoxWAWeighted.setEnabled(false);
        jComboBoxWAWeighted.setMaximumSize(new java.awt.Dimension(210, 25));
        jComboBoxWAWeighted.setMinimumSize(new java.awt.Dimension(210, 25));
        jComboBoxWAWeighted.setPreferredSize(new java.awt.Dimension(210, 25));
        jComboBoxWAWeighted.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxWAWeightedItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 17, 0, 0);
        jPanel23.add(jComboBoxWAWeighted, gridBagConstraints);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("δ:");
        jLabel21.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel21.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel21.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jLabel21, gridBagConstraints);

        jSpinnerWADelta.setModel(new javax.swing.SpinnerNumberModel(1, -1, 2, 1));
        jSpinnerWADelta.setEnabled(false);
        jSpinnerWADelta.setMaximumSize(new java.awt.Dimension(47, 25));
        jSpinnerWADelta.setMinimumSize(new java.awt.Dimension(47, 25));
        jSpinnerWADelta.setPreferredSize(new java.awt.Dimension(47, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jSpinnerWADelta, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel20.add(jPanel23, gridBagConstraints);

        jPanel27.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel27.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel27.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel27.setLayout(new java.awt.GridBagLayout());

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("β: ");
        jLabel17.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel17.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel17.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel27.add(jLabel17, gridBagConstraints);

        jSpinnerOWAWABeta.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        jSpinnerOWAWABeta.setEnabled(false);
        jSpinnerOWAWABeta.setMaximumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWAWABeta.setMinimumSize(new java.awt.Dimension(47, 25));
        jSpinnerOWAWABeta.setPreferredSize(new java.awt.Dimension(47, 25));
        jSpinnerOWAWABeta.setValue(0.5);
        jSpinnerOWAWABeta.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerOWAWABetaStateChanged(evt);
            }
        });
        jPanel27.add(jSpinnerOWAWABeta, new java.awt.GridBagConstraints());

        jLabel18.setMaximumSize(new java.awt.Dimension(280, 25));
        jLabel18.setMinimumSize(new java.awt.Dimension(280, 25));
        jLabel18.setPreferredSize(new java.awt.Dimension(280, 25));
        jPanel27.add(jLabel18, new java.awt.GridBagConstraints());

        jLabel19.setText("GOWAWA Beta");
        jLabel19.setMaximumSize(new java.awt.Dimension(165, 25));
        jLabel19.setMinimumSize(new java.awt.Dimension(165, 25));
        jLabel19.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel27.add(jLabel19, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel20.add(jPanel27, gridBagConstraints);

        jPanel28.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel28.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel28.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel28.setLayout(new java.awt.GridBagLayout());

        owawa.setText("GOWAWA");
        owawa.setMaximumSize(new java.awt.Dimension(195, 25));
        owawa.setMinimumSize(new java.awt.Dimension(195, 25));
        owawa.setPreferredSize(new java.awt.Dimension(195, 25));
        owawa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                owawaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel28.add(owawa, gridBagConstraints);

        jPanel29.setMaximumSize(new java.awt.Dimension(330, 25));
        jPanel29.setMinimumSize(new java.awt.Dimension(330, 25));
        jPanel29.setPreferredSize(new java.awt.Dimension(330, 25));
        jPanel28.add(jPanel29, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(-3, 9, 0, 0);
        jPanel20.add(jPanel28, gridBagConstraints);

        jPanel24.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel24.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel24.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel24.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel20.add(jPanel24, gridBagConstraints);

        jPanel12.add(jPanel20, java.awt.BorderLayout.WEST);

        jPanel21.setMaximumSize(new java.awt.Dimension(365, 32));
        jPanel21.setMinimumSize(new java.awt.Dimension(365, 32));
        jPanel21.setPreferredSize(new java.awt.Dimension(365, 32));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jButtonAddOWAWAConfig.setText("Add");
        jButtonAddOWAWAConfig.setEnabled(false);
        jButtonAddOWAWAConfig.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonAddOWAWAConfig.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonAddOWAWAConfig.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonAddOWAWAConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddOWAWAConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel21.add(jButtonAddOWAWAConfig, gridBagConstraints);

        jButtonRemoveOWAWAConfig.setText("Remove");
        jButtonRemoveOWAWAConfig.setEnabled(false);
        jButtonRemoveOWAWAConfig.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonRemoveOWAWAConfig.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonRemoveOWAWAConfig.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonRemoveOWAWAConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveOWAWAConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel21.add(jButtonRemoveOWAWAConfig, gridBagConstraints);

        jButtonViewOWAWAConfig.setText("View");
        jButtonViewOWAWAConfig.setEnabled(false);
        jButtonViewOWAWAConfig.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonViewOWAWAConfig.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonViewOWAWAConfig.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonViewOWAWAConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewOWAWAConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel21.add(jButtonViewOWAWAConfig, gridBagConstraints);

        jLabelOWAWAInfo.setText("0 configurations");
        jLabelOWAWAInfo.setMaximumSize(new java.awt.Dimension(130, 25));
        jLabelOWAWAInfo.setMinimumSize(new java.awt.Dimension(130, 25));
        jLabelOWAWAInfo.setPreferredSize(new java.awt.Dimension(130, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        jPanel21.add(jLabelOWAWAInfo, gridBagConstraints);

        jButtonDefaultOWAWA.setText("Default");
        jButtonDefaultOWAWA.setEnabled(false);
        jButtonDefaultOWAWA.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonDefaultOWAWA.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonDefaultOWAWA.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonDefaultOWAWA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDefaultOWAWAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel21.add(jButtonDefaultOWAWA, gridBagConstraints);

        jPanel12.add(jPanel21, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("GOWAWA", jPanel12);

        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel26.setMaximumSize(new java.awt.Dimension(540, 150));
        jPanel26.setMinimumSize(new java.awt.Dimension(540, 150));
        jPanel26.setPreferredSize(new java.awt.Dimension(540, 150));
        jPanel26.setLayout(new java.awt.GridBagLayout());

        jPanel30.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel30.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel30.setName(""); // NOI18N
        jPanel30.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel30.setLayout(new java.awt.GridBagLayout());

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("α: ");
        jLabel13.setMaximumSize(new java.awt.Dimension(25, 25));
        jLabel13.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabel13.setName(""); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel30.add(jLabel13, gridBagConstraints);

        jSpinnerChoquetAlfaMethod.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        jSpinnerChoquetAlfaMethod.setEnabled(false);
        jSpinnerChoquetAlfaMethod.setMaximumSize(new java.awt.Dimension(60, 25));
        jSpinnerChoquetAlfaMethod.setMinimumSize(new java.awt.Dimension(60, 25));
        jSpinnerChoquetAlfaMethod.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel30.add(jSpinnerChoquetAlfaMethod, gridBagConstraints);

        jComboBoxChoquetMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AGGREGATED_OBJECTS_1", "AGGREGATED_OBJECTS_2" }));
        jComboBoxChoquetMethod.setEnabled(false);
        jComboBoxChoquetMethod.setMaximumSize(new java.awt.Dimension(210, 25));
        jComboBoxChoquetMethod.setMinimumSize(new java.awt.Dimension(210, 25));
        jComboBoxChoquetMethod.setPreferredSize(new java.awt.Dimension(210, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel30.add(jComboBoxChoquetMethod, gridBagConstraints);

        jLabel16.setText("Fuzzy density function");
        jLabel16.setMaximumSize(new java.awt.Dimension(165, 25));
        jLabel16.setMinimumSize(new java.awt.Dimension(165, 25));
        jLabel16.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel30.add(jLabel16, gridBagConstraints);

        jLabel20.setMaximumSize(new java.awt.Dimension(49, 25));
        jLabel20.setMinimumSize(new java.awt.Dimension(49, 25));
        jLabel20.setPreferredSize(new java.awt.Dimension(49, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel30.add(jLabel20, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel26.add(jPanel30, gridBagConstraints);

        jPanel31.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel31.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel31.setName(""); // NOI18N
        jPanel31.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel31.setRequestFocusEnabled(false);
        jPanel31.setLayout(new java.awt.GridBagLayout());

        jLabel22.setText("LOVIs order");
        jLabel22.setMaximumSize(new java.awt.Dimension(165, 25));
        jLabel22.setMinimumSize(new java.awt.Dimension(165, 25));
        jLabel22.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel31.add(jLabel22, gridBagConstraints);

        jComboBoxLovisOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DESCENDING", "ASCENDING" }));
        jComboBoxLovisOrder.setEnabled(false);
        jComboBoxLovisOrder.setMaximumSize(new java.awt.Dimension(210, 25));
        jComboBoxLovisOrder.setMinimumSize(new java.awt.Dimension(210, 25));
        jComboBoxLovisOrder.setPreferredSize(new java.awt.Dimension(210, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel31.add(jComboBoxLovisOrder, gridBagConstraints);

        jPanel34.setMaximumSize(new java.awt.Dimension(136, 25));
        jPanel34.setMinimumSize(new java.awt.Dimension(136, 25));
        jPanel34.setPreferredSize(new java.awt.Dimension(136, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel31.add(jPanel34, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel26.add(jPanel31, gridBagConstraints);

        jPanel32.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel32.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel32.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel32.setLayout(new java.awt.GridBagLayout());

        jSpinnerChoquet.setModel(new javax.swing.SpinnerNumberModel(0.0f, -1.0f, null, 0.25f));
        jSpinnerChoquet.setToolTipText("");
        jSpinnerChoquet.setEnabled(false);
        jSpinnerChoquet.setMaximumSize(new java.awt.Dimension(60, 25));
        jSpinnerChoquet.setMinimumSize(new java.awt.Dimension(60, 25));
        jSpinnerChoquet.setPreferredSize(new java.awt.Dimension(60, 25));
        jSpinnerChoquet.setValue(0.5);
        jSpinnerChoquet.setValue(0.0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel32.add(jSpinnerChoquet, gridBagConstraints);

        jLabel24.setMaximumSize(new java.awt.Dimension(286, 25));
        jLabel24.setMinimumSize(new java.awt.Dimension(286, 25));
        jLabel24.setPreferredSize(new java.awt.Dimension(286, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel32.add(jLabel24, gridBagConstraints);

        jLabel25.setText("L value (synergism)");
        jLabel25.setMaximumSize(new java.awt.Dimension(165, 25));
        jLabel25.setMinimumSize(new java.awt.Dimension(165, 25));
        jLabel25.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel32.add(jLabel25, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel26.add(jPanel32, gridBagConstraints);

        jPanel33.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel33.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel33.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel33.setLayout(new java.awt.GridBagLayout());

        choquet.setText("Choquet Integral with respect to Lmδ-measure");
        choquet.setMaximumSize(new java.awt.Dimension(525, 25));
        choquet.setMinimumSize(new java.awt.Dimension(525, 25));
        choquet.setPreferredSize(new java.awt.Dimension(525, 25));
        choquet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choquetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel33.add(choquet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(-3, 9, 0, 0);
        jPanel26.add(jPanel33, gridBagConstraints);

        jPanel35.setMaximumSize(new java.awt.Dimension(525, 30));
        jPanel35.setMinimumSize(new java.awt.Dimension(525, 30));
        jPanel35.setPreferredSize(new java.awt.Dimension(525, 30));
        jPanel35.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel26.add(jPanel35, gridBagConstraints);

        jPanel25.add(jPanel26, java.awt.BorderLayout.WEST);

        jPanel36.setMaximumSize(new java.awt.Dimension(365, 32));
        jPanel36.setMinimumSize(new java.awt.Dimension(365, 32));
        jPanel36.setPreferredSize(new java.awt.Dimension(365, 32));
        jPanel36.setLayout(new java.awt.GridBagLayout());

        jButtonAddChoquetConfig.setText("Add");
        jButtonAddChoquetConfig.setEnabled(false);
        jButtonAddChoquetConfig.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonAddChoquetConfig.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonAddChoquetConfig.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonAddChoquetConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddChoquetConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel36.add(jButtonAddChoquetConfig, gridBagConstraints);

        jButtonRemoveChoquetConfig.setText("Remove");
        jButtonRemoveChoquetConfig.setEnabled(false);
        jButtonRemoveChoquetConfig.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonRemoveChoquetConfig.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonRemoveChoquetConfig.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonRemoveChoquetConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveChoquetConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel36.add(jButtonRemoveChoquetConfig, gridBagConstraints);

        jButtonViewChoquetConfig.setText("View");
        jButtonViewChoquetConfig.setEnabled(false);
        jButtonViewChoquetConfig.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonViewChoquetConfig.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonViewChoquetConfig.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonViewChoquetConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewChoquetConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel36.add(jButtonViewChoquetConfig, gridBagConstraints);

        jLabelChoquetInfo.setText("0 configurations");
        jLabelChoquetInfo.setMaximumSize(new java.awt.Dimension(130, 25));
        jLabelChoquetInfo.setMinimumSize(new java.awt.Dimension(130, 25));
        jLabelChoquetInfo.setPreferredSize(new java.awt.Dimension(130, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        jPanel36.add(jLabelChoquetInfo, gridBagConstraints);

        jButtonDefaultChoquet.setText("Default");
        jButtonDefaultChoquet.setEnabled(false);
        jButtonDefaultChoquet.setMaximumSize(new java.awt.Dimension(95, 27));
        jButtonDefaultChoquet.setMinimumSize(new java.awt.Dimension(95, 27));
        jButtonDefaultChoquet.setPreferredSize(new java.awt.Dimension(95, 27));
        jButtonDefaultChoquet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDefaultChoquetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel36.add(jButtonDefaultChoquet, gridBagConstraints);

        jPanel25.add(jPanel36, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Choquet", jPanel25);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jPanel6.setMaximumSize(new java.awt.Dimension(560, 35));
        jPanel6.setMinimumSize(new java.awt.Dimension(560, 35));
        jPanel6.setPreferredSize(new java.awt.Dimension(560, 35));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        check_all.setText("Check All");
        check_all.setMaximumSize(new java.awt.Dimension(115, 27));
        check_all.setMinimumSize(new java.awt.Dimension(115, 27));
        check_all.setPreferredSize(new java.awt.Dimension(115, 27));
        check_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_allActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel6.add(check_all, gridBagConstraints);

        uncheck_all.setText("Uncheck All");
        uncheck_all.setMaximumSize(new java.awt.Dimension(115, 27));
        uncheck_all.setMinimumSize(new java.awt.Dimension(115, 27));
        uncheck_all.setPreferredSize(new java.awt.Dimension(115, 27));
        uncheck_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uncheck_allActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel6.add(uncheck_all, gridBagConstraints);

        ok.setText("OK");
        ok.setMaximumSize(new java.awt.Dimension(115, 27));
        ok.setMinimumSize(new java.awt.Dimension(115, 27));
        ok.setPreferredSize(new java.awt.Dimension(115, 27));
        ok.setRequestFocusEnabled(false);
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 90, 0, 0);
        jPanel6.add(ok, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/panelhelp.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton1.setMaximumSize(new java.awt.Dimension(28, 27));
        jButton1.setMinimumSize(new java.awt.Dimension(28, 27));
        jButton1.setPreferredSize(new java.awt.Dimension(28, 27));
        jButton1.addActionListener( Util.openFile( this, "Invariants.pdf" ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 90, 0, 0);
        jPanel6.add(jButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel6, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed

        showMessgInGenerate = true;

        list_classics_invariants.clear();
        list_no_classic_invariants.clear();
        invariant_parameters.clear();

        if (owawa.isSelected() && list_owawas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "OWAWA operator-based configurations do not have been created.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            showMessgInGenerate = false;
            return;
        }

        //Norms
        if (n1.isSelected()) {
            list_no_classic_invariants.add("n1");
        }
        if (n2.isSelected()) {
            list_no_classic_invariants.add("n2");
        }
        if (n3.isSelected()) {
            list_no_classic_invariants.add("n3");
        }

        //Means
        if (g.isSelected()) {
            list_no_classic_invariants.add("gm");
        }
        if (a.isSelected()) {
            list_no_classic_invariants.add("am");
        }
        if (p2.isSelected()) {
            list_no_classic_invariants.add("p2");
        }
        if (p3.isSelected()) {
            list_no_classic_invariants.add("p3");
        }
        if (h.isSelected()) {
            list_no_classic_invariants.add("hm");
        }

        //Statistics
        if (v.isSelected()) {
            list_no_classic_invariants.add("v");
        }
        if (s.isSelected()) {
            list_no_classic_invariants.add("s");
        }
        if (k.isSelected()) {
            list_no_classic_invariants.add("k");
        }
        if (sd.isSelected()) {
            list_no_classic_invariants.add("sd");
        }
        if (vc.isSelected()) {
            list_no_classic_invariants.add("vc");
        }
        if (ra.isSelected()) {
            list_no_classic_invariants.add("ra");
        }
        if (q1.isSelected()) {
            list_no_classic_invariants.add("q1");
        }
        if (q2.isSelected()) {
            list_no_classic_invariants.add("q2");
        }
        if (q3.isSelected()) {
            list_no_classic_invariants.add("q3");
        }
        if (i50.isSelected()) {
            list_no_classic_invariants.add("i50");
        }
        if (mx.isSelected()) {
            list_no_classic_invariants.add("mx");
        }
        if (mn.isSelected()) {
            list_no_classic_invariants.add("mn");
        }

        //GOWAWA
        if (owawa.isSelected() && !list_owawas.isEmpty()) {
            list_no_classic_invariants.add(InvariantType.GOWAWA.toString());
            invariant_parameters.put(InvariantType.GOWAWA, new Object[]{list_owawas});
        }

        //Choquet Integral
        if (choquet.isSelected() && !list_choquets.isEmpty()) {
            list_no_classic_invariants.add(InvariantType.CHOQUET.toString());
            invariant_parameters.put(InvariantType.CHOQUET, new Object[]{list_choquets});
        }

        //Classics
        if (lai.isSelected()) {
            list_classics_invariants.add("lai");
        }
        if (entropy.isSelected()) {
            list_classics_invariants.add("h");
        }
        if (sic.isSelected()) {
            list_classics_invariants.add("sic");
        }
        if (tic.isSelected()) {
            list_classics_invariants.add("tic");
        }
        if (mic.isSelected()) {
            list_classics_invariants.add("mic");
        }

        if (sicn.isSelected()) {
            list_classics_invariants.add("sicn");
        }
        if (ticn.isSelected()) {
            list_classics_invariants.add("ticn");
        }
        if (micn.isSelected()) {
            list_classics_invariants.add("micn");
        }

        if (ac.isSelected()) {
            list_classics_invariants.add("ac");
            invariant_parameters.put(InvariantType.AUTOCORRELATION, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (gv.isSelected()) {
            list_classics_invariants.add("gv");
            invariant_parameters.put(InvariantType.GRAVITATIONAL, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (ts.isSelected()) {
            list_classics_invariants.add("ts");
            invariant_parameters.put(InvariantType.TOTAL_SUM, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (kh.isSelected()) {
            list_classics_invariants.add("kh");
            invariant_parameters.put(InvariantType.KIER_HALL, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (es.isSelected()) {
            list_classics_invariants.add("es");
            invariant_parameters.put(InvariantType.ELECTROTOPOLOGICAL_STATE, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (ib.isSelected()) {
            list_classics_invariants.add("ib");
            invariant_parameters.put(InvariantType.IVANCIUC_BALABAN, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (gc.isSelected()) {
            list_classics_invariants.add("gc");
            invariant_parameters.put(InvariantType.GEARY, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (pcd.isSelected()) {
            list_classics_invariants.add("pcd");
            invariant_parameters.put(InvariantType.POTENTIAL_CHARGE_DISTRIBUTION, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (cei.isSelected()) {
            list_classics_invariants.add("cei");
            invariant_parameters.put(InvariantType.CONNECTIVE_ECCENTRICITY_INDEX, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        
        if (rdf.isSelected()) {
            list_classics_invariants.add("rdf");

            invariant_parameters.put(InvariantType.RADIAL_DISTRIBUTION_FUNCTION,
                    new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (is.isSelected()) {
            list_classics_invariants.add("is");
            invariant_parameters.put(InvariantType.INTERACTION_SPECTRUM, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (morse.isSelected()) {
            list_classics_invariants.add("mse");
            invariant_parameters.put(InvariantType.MORSE, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (apm.isSelected()) {
            list_classics_invariants.add("apm");
            invariant_parameters.put(InvariantType.AMPHIPHILIC_MOMENTS, new Object[]{DimensionType.TOPOLOGICAL, null});
        }

        if (bft.isSelected()) {
            list_classics_invariants.add("bft");
        }

        if (list_no_classic_invariants.isEmpty() && !list_classics_invariants.isEmpty() && !onlyContainSimpleInvs()) {
//            JOptionPane.showMessageDialog(this, "You must select at least a norm, mean, statistical, GOWAWA or Choquet integral invariant when you select a classic invariant",
//                    "Warning", JOptionPane.WARNING_MESSAGE);
            
            JOptionPane.showMessageDialog(this, "You must select at least a norm, mean or statistical when you select a classic invariant",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            setVisible(false);
        }

    }//GEN-LAST:event_okActionPerformed

    private void check_allActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_allActionPerformed

        currentNorms = TOTAL_NORMS;
        currentMeans = TOTAL_MEANS;
        currentStatistics = TOTAL_STATISTICS;
        currentClassics = TOTAL_CLASSICS;

        //4 Norms
        n1.setSelected(true);
        n2.setSelected(true);
        n3.setSelected(true);

        //5 Means
        g.setSelected(true);
        a.setSelected(true);
        p2.setSelected(true);
        p3.setSelected(true);
        h.setSelected(true);

        //12 Statistics
        v.setSelected(true);
        s.setSelected(true);
        k.setSelected(true);
        sd.setSelected(true);
        vc.setSelected(true);
        ra.setSelected(true);
        q1.setSelected(true);
        q2.setSelected(true);
        q3.setSelected(true);
        i50.setSelected(true);
        mx.setSelected(true);
        mn.setSelected(true);

//        //OWAWA
//        owawa.setSelected(!standarized.isSelected());
//        if (!standarized.isSelected()) {
//            owawaActionPerformed(null);
//            jButtonDefaultOWAWA.doClick();
//        }
//
//        //Choquet
//        choquet.setSelected(!standarized.isSelected());
//        if (!standarized.isSelected()) {
//            choquetActionPerformed(null);
//            jButtonDefaultChoquet.doClick();
//        }

        //7 Classics
        lai.setSelected(true);
        entropy.setSelected(true);
        ac.setSelected(true);
        gv.setSelected(true);
        ts.setSelected(true);
        mic.setSelected(true);
        tic.setSelected(true);
        sic.setSelected(true);
        micn.setSelected(true);
        ticn.setSelected(true);
        sicn.setSelected(true);
        es.setSelected(true);
        ib.setSelected(true);
        kh.setSelected(true);
        gc.setSelected(true);
        pcd.setSelected(true);
        cei.setSelected(true);
        rdf.setSelected(true);
        morse.setSelected(true);
        apm.setSelected(true);
        is.setSelected(true);
        bft.setSelected(true);

        invLaiToggleButton.setSelected(true);
        invLaiToggleButton.setText("Uncheck");

        statsToggleButton.setSelected(true);
        statsToggleButton.setText("Uncheck");

        meanToggleButton.setSelected(true);
        meanToggleButton.setText("Uncheck");

        normToggleButton.setSelected(true);
        normToggleButton.setText("Uncheck");

        setNoClassicInvPanelState(true);
    }//GEN-LAST:event_check_allActionPerformed

    private void uncheck_allActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uncheck_allActionPerformed

        currentNorms = 0;
        currentMeans = 0;
        currentStatistics = 0;
        currentClassics = 0;

        //4 Norms
        n1.setSelected(false);
        n2.setSelected(false);
        n3.setSelected(false);

        //5 Means
        g.setSelected(false);
        a.setSelected(false);
        p2.setSelected(false);
        p3.setSelected(false);
        h.setSelected(false);

        //12 Statistics
        v.setSelected(false);
        s.setSelected(false);
        k.setSelected(false);
        sd.setSelected(false);
        vc.setSelected(false);
        ra.setSelected(false);
        q1.setSelected(false);
        q2.setSelected(false);
        q3.setSelected(false);
        i50.setSelected(false);
        mx.setSelected(false);
        mn.setSelected(false);

        //OWAWA
        owawa.setSelected(false);
        owawaActionPerformed(null);

        //Choquet
        choquet.setSelected(false);
        choquetActionPerformed(null);

//        //7 Classics
        lai.setSelected(false);
        entropy.setSelected(false);
        ac.setSelected(false);
        gv.setSelected(false);
        ts.setSelected(false);
        mic.setSelected(false);
        tic.setSelected(false);
        sic.setSelected(false);
        micn.setSelected(false);
        ticn.setSelected(false);
        sicn.setSelected(false);
        es.setSelected(false);
        ib.setSelected(false);
        kh.setSelected(false);
        gc.setSelected(false);
        pcd.setSelected(false);
        cei.setSelected(false);
        rdf.setSelected(false);
        morse.setSelected(false);
        is.setSelected(false);
        apm.setSelected(false);
        bft.setSelected(false);

        invLaiToggleButton.setSelected(false);
        invLaiToggleButton.setText("Check");

        meanToggleButton.setSelected(false);
        meanToggleButton.setText("Check");

        statsToggleButton.setSelected(false);
        statsToggleButton.setText("Check");

        normToggleButton.setSelected(false);
        normToggleButton.setText("Check");

        setNoClassicInvPanelState(false);

        list_classics_invariants.clear();
        list_no_classic_invariants.clear();
        invariant_parameters.clear();
        list_owawas.clear();
        list_choquets.clear();
        
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_uncheck_allActionPerformed

    private void jButtonDefaultChoquetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDefaultChoquetActionPerformed

        for (String key : defaultChoquet) {
            if (!list_choquets.containsKey(key)) {
                String[] values = key.split("//");

                HashMap<Choquet.PARAMETER_NAMES, Object> parameters = new HashMap<>();
                parameters.put(Choquet.PARAMETER_NAMES.ORDER, values[0]);
                parameters.put(Choquet.PARAMETER_NAMES.L_VALUE, Float.parseFloat(values[1]));
                parameters.put(Choquet.PARAMETER_NAMES.SINGLETON_METHOD, values[2]);
                parameters.put(Choquet.PARAMETER_NAMES.ALFA_SINGLETON_METHOD, Float.parseFloat(values[3]));

                list_choquets.put(key, parameters);
                jLabelChoquetInfo.setText(list_choquets.size() == 1 ? "1 configuration" : list_choquets.size() + " configurations");
                choquetListDialog.updateList(list_choquets.keySet());
            }
        }
    }//GEN-LAST:event_jButtonDefaultChoquetActionPerformed

    private void jButtonViewChoquetConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewChoquetConfigActionPerformed

        choquetListDialog.setLocation(getLocationOnScreen().x + getSize().width, getLocationOnScreen().y);
        choquetListDialog.updateList(list_choquets.keySet());
        choquetListDialog.setVisible(true);
    }//GEN-LAST:event_jButtonViewChoquetConfigActionPerformed

    private void jButtonRemoveChoquetConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveChoquetConfigActionPerformed

        list_choquets.clear();
        jLabelChoquetInfo.setText("0 configurations");
        choquetListDialog.updateList(list_choquets.keySet());
    }//GEN-LAST:event_jButtonRemoveChoquetConfigActionPerformed

    private void jButtonAddChoquetConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddChoquetConfigActionPerformed

        String order = jComboBoxLovisOrder.getSelectedItem().toString();
        float L_value = Float.parseFloat(jSpinnerChoquet.getValue().toString());

        String method = jComboBoxChoquetMethod.isEnabled() ? jComboBoxChoquetMethod.getSelectedItem().toString() : OWAWA.WEIGHT_METHODS.NONE.toString();
        float alfa = jComboBoxChoquetMethod.isEnabled() ? Float.parseFloat(jSpinnerChoquetAlfaMethod.getValue().toString()) : 0.0f;

        String key = order + "//" + L_value + "//" + method + "//" + alfa;
        if (list_choquets.containsKey(key)) {
            JOptionPane.showMessageDialog(this, "This Choquet integral configuration already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        HashMap<Choquet.PARAMETER_NAMES, Object> parameters = new HashMap<>();
        parameters.put(Choquet.PARAMETER_NAMES.ORDER, order);
        parameters.put(Choquet.PARAMETER_NAMES.L_VALUE, L_value);
        parameters.put(Choquet.PARAMETER_NAMES.SINGLETON_METHOD, method);
        parameters.put(Choquet.PARAMETER_NAMES.ALFA_SINGLETON_METHOD, alfa);

        list_choquets.put(key, parameters);
        jLabelChoquetInfo.setText(list_choquets.size() == 1 ? "1 configuration" : list_choquets.size() + " configurations");
        choquetListDialog.updateList(list_choquets.keySet());
    }//GEN-LAST:event_jButtonAddChoquetConfigActionPerformed

    private void choquetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choquetActionPerformed

        actionCheckBoxChoquet();
    }//GEN-LAST:event_choquetActionPerformed

    private void jButtonDefaultOWAWAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDefaultOWAWAActionPerformed

        for (String key : defaultOWAWAs) {
            if (!list_owawas.containsKey(key)) {
                String[] values = key.split("//");

                HashMap<OWAWA.PARAMETER_NAMES, Object> parameters = new HashMap<>();
                parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWAWA, Float.parseFloat(values[0]));
                parameters.put(OWAWA.PARAMETER_NAMES.LAMBDA_OWA, Integer.parseInt(values[1]));
                parameters.put(OWAWA.PARAMETER_NAMES.METHOD_OWA, values[2]);
                parameters.put(OWAWA.PARAMETER_NAMES.ALFA_OWA, Float.parseFloat(values[3]));
                parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWA, Float.parseFloat(values[4]));
                parameters.put(OWAWA.PARAMETER_NAMES.DELTA_WA, Integer.parseInt(values[5]));
                parameters.put(OWAWA.PARAMETER_NAMES.METHOD_WA, values[6]);
                parameters.put(OWAWA.PARAMETER_NAMES.ALFA_WA, Float.parseFloat(values[7]));
                parameters.put(OWAWA.PARAMETER_NAMES.BETA_WA, Float.parseFloat(values[8]));

                list_owawas.put(key, parameters);
                jLabelOWAWAInfo.setText(list_owawas.size() == 1 ? "1 configuration" : list_owawas.size() + " configurations");
                owawaListDialog.updateList(list_owawas.keySet());
            }
        }
    }//GEN-LAST:event_jButtonDefaultOWAWAActionPerformed

    private void jButtonViewOWAWAConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewOWAWAConfigActionPerformed

        owawaListDialog.setLocation(getLocationOnScreen().x + getSize().width, getLocationOnScreen().y);
        owawaListDialog.updateList(list_owawas.keySet());
        owawaListDialog.setVisible(true);
    }//GEN-LAST:event_jButtonViewOWAWAConfigActionPerformed

    private void jButtonRemoveOWAWAConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveOWAWAConfigActionPerformed

        list_owawas.clear();
        jLabelOWAWAInfo.setText("0 configurations");
        owawaListDialog.updateList(list_owawas.keySet());
    }//GEN-LAST:event_jButtonRemoveOWAWAConfigActionPerformed

    private void jButtonAddOWAWAConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddOWAWAConfigActionPerformed

        float betaOWAWA = Math.round(Float.parseFloat(jSpinnerOWAWABeta.getValue().toString()) * 100f) / 100f;

        int lambdaOWA = jComboBoxOWAWeighted.isEnabled() ? Integer.parseInt(jSpinnerOWALambda.getValue().toString()) : 1;
        String methodOWA = jComboBoxOWAWeighted.isEnabled() ? jComboBoxOWAWeighted.getSelectedItem().toString() : OWAWA.WEIGHT_METHODS.NONE.toString();
        float alfaOWA = jComboBoxOWAWeighted.isEnabled() ? Float.parseFloat(jSpinnerOWAAlfa.getValue().toString()) : 0.0f;
        alfaOWA = Math.round(alfaOWA * 100f) / 100f;
        float betaOWA = jComboBoxOWAWeighted.isEnabled() ? Float.parseFloat(jSpinnerOWABeta.getValue().toString()) : 0.0f;
        betaOWA = Math.round(betaOWA * 100f) / 100f;

        int deltaWA = jComboBoxWAWeighted.isEnabled() ? Integer.parseInt(jSpinnerWADelta.getValue().toString()) : 1;
        String methodWA = jComboBoxWAWeighted.isEnabled() ? jComboBoxWAWeighted.getSelectedItem().toString() : OWAWA.WEIGHT_METHODS.NONE.toString();
        float alfaWA = jComboBoxWAWeighted.isEnabled() ? Float.parseFloat(jSpinnerWAAlfa.getValue().toString()) : 0.0f;
        alfaWA = Math.round(alfaWA * 100f) / 100f;
        float betaWA = jComboBoxWAWeighted.isEnabled() ? Float.parseFloat(jSpinnerWABeta.getValue().toString()) : 0.0f;
        betaWA = Math.round(betaWA * 100f) / 100f;

        String key = betaOWAWA + "//" + lambdaOWA + "//" + methodOWA + "//" + alfaOWA + "//" + betaOWA + "//" + deltaWA + "//" + methodWA + "//" + alfaWA + "//" + betaWA;
        if (list_owawas.containsKey(key)) {
            JOptionPane.showMessageDialog(this, "This OWAWA configuration already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ((methodOWA.equals(OWAWA.WEIGHT_METHODS.S_OWA.toString()) && (alfaOWA + betaOWA > 1))
                || (methodWA.equals(OWAWA.WEIGHT_METHODS.S_OWA.toString()) && (alfaWA + betaWA > 1))) {
            JOptionPane.showMessageDialog(this, "The sum of the α and β values of the S-OWA weighting method must be lesser or equal than 1.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            showMessgInGenerate = false;
            return;
        }

        if ((methodOWA.equals(OWAWA.WEIGHT_METHODS.WINDOW_OWA.toString()) && alfaOWA >= betaOWA)
                || (methodWA.equals(OWAWA.WEIGHT_METHODS.WINDOW_OWA.toString()) && alfaWA >= betaWA)) {
            JOptionPane.showMessageDialog(this, "In the Window-OWA weighting method the α value must be less than β value.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        HashMap<OWAWA.PARAMETER_NAMES, Object> parameters = new HashMap<>();
        parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWAWA, (float) betaOWAWA);
        parameters.put(OWAWA.PARAMETER_NAMES.LAMBDA_OWA, (int) lambdaOWA);
        parameters.put(OWAWA.PARAMETER_NAMES.METHOD_OWA, methodOWA);
        parameters.put(OWAWA.PARAMETER_NAMES.ALFA_OWA, (float) alfaOWA);
        parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWA, (float) betaOWA);
        parameters.put(OWAWA.PARAMETER_NAMES.DELTA_WA, (int) deltaWA);
        parameters.put(OWAWA.PARAMETER_NAMES.METHOD_WA, methodWA);
        parameters.put(OWAWA.PARAMETER_NAMES.ALFA_WA, (float) alfaWA);
        parameters.put(OWAWA.PARAMETER_NAMES.BETA_WA, (float) betaWA);

        list_owawas.put(key, parameters);
        jLabelOWAWAInfo.setText(list_owawas.size() == 1 ? "1 configuration" : list_owawas.size() + " configurations");
        owawaListDialog.updateList(list_owawas.keySet());
    }//GEN-LAST:event_jButtonAddOWAWAConfigActionPerformed

    private void owawaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_owawaActionPerformed

        actionCheckBoxOWAWA();
        jSpinnerOWAWABetaStateChanged();
    }//GEN-LAST:event_owawaActionPerformed

    private void jSpinnerOWAWABetaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerOWAWABetaStateChanged

        actionCheckBoxOWAWA();
        jSpinnerOWAWABetaStateChanged();
    }//GEN-LAST:event_jSpinnerOWAWABetaStateChanged

    private void jComboBoxWAWeightedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxWAWeightedItemStateChanged

        if (jComboBoxWAWeighted.getSelectedIndex() >= 2) {
            jSpinnerWABeta.setEnabled(false);
            jSpinnerWABeta.setValue(Float.parseFloat("0"));
        } else {
            jSpinnerWABeta.setEnabled(true);
        }
    }//GEN-LAST:event_jComboBoxWAWeightedItemStateChanged

    private void jComboBoxOWAWeightedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxOWAWeightedItemStateChanged

        if (jComboBoxOWAWeighted.getSelectedIndex() >= 2) {
            jSpinnerOWABeta.setEnabled(false);
            jSpinnerOWABeta.setValue(Float.parseFloat("0"));
        } else {
            jSpinnerOWABeta.setEnabled(true);
        }
    }//GEN-LAST:event_jComboBoxOWAWeightedItemStateChanged

    private void statsToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsToggleButtonActionPerformed

        if (statsToggleButton.isSelected()) {
            currentStatistics = TOTAL_STATISTICS;
            v.setSelected(true);
            k.setSelected(true);
            sd.setSelected(true);
            s.setSelected(true);
            vc.setSelected(true);
            ra.setSelected(true);
            q1.setSelected(true);
            q2.setSelected(true);
            q3.setSelected(true);
            i50.setSelected(true);
            mx.setSelected(true);
            mn.setSelected(true);
            statsToggleButton.setText("Uncheck");
        } else {
            currentStatistics = 0;

            v.setSelected(false);
            k.setSelected(false);
            sd.setSelected(false);
            s.setSelected(false);
            vc.setSelected(false);
            ra.setSelected(false);
            q1.setSelected(false);
            q2.setSelected(false);
            q3.setSelected(false);
            i50.setSelected(false);
            mx.setSelected(false);
            mn.setSelected(false);
            statsToggleButton.setText("Check");
        }
    }//GEN-LAST:event_statsToggleButtonActionPerformed

    private void mnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnActionPerformed

        actionCheckBoxStatistics(mn);
    }//GEN-LAST:event_mnActionPerformed

    private void mxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mxActionPerformed

        actionCheckBoxStatistics(mx);
    }//GEN-LAST:event_mxActionPerformed

    private void i50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i50ActionPerformed

        actionCheckBoxStatistics(i50);
    }//GEN-LAST:event_i50ActionPerformed

    private void q3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_q3ActionPerformed

        actionCheckBoxStatistics(q3);
    }//GEN-LAST:event_q3ActionPerformed

    private void q2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_q2ActionPerformed

        actionCheckBoxStatistics(q2);
    }//GEN-LAST:event_q2ActionPerformed

    private void q1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_q1ActionPerformed

        actionCheckBoxStatistics(q1);
    }//GEN-LAST:event_q1ActionPerformed

    private void raActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_raActionPerformed

        actionCheckBoxStatistics(ra);
    }//GEN-LAST:event_raActionPerformed

    private void vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vcActionPerformed

        actionCheckBoxStatistics(vc);
    }//GEN-LAST:event_vcActionPerformed

    private void sdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sdActionPerformed

        actionCheckBoxStatistics(sd);
    }//GEN-LAST:event_sdActionPerformed

    private void kActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kActionPerformed

        actionCheckBoxStatistics(k);
    }//GEN-LAST:event_kActionPerformed

    private void sActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sActionPerformed

        actionCheckBoxStatistics(s);
    }//GEN-LAST:event_sActionPerformed

    private void vActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vActionPerformed

        actionCheckBoxStatistics(v);
    }//GEN-LAST:event_vActionPerformed

    private void meanToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meanToggleButtonActionPerformed

        if (meanToggleButton.isSelected()) {
            currentMeans = TOTAL_MEANS;

            g.setSelected(true);
            a.setSelected(true);
            p2.setSelected(true);
            p3.setSelected(true);
            h.setSelected(true);
            meanToggleButton.setText("Uncheck");
        } else {
            currentMeans = 0;

            g.setSelected(false);
            a.setSelected(false);
            p2.setSelected(false);
            p3.setSelected(false);
            h.setSelected(false);
            meanToggleButton.setText("Check");
        }
    }//GEN-LAST:event_meanToggleButtonActionPerformed

    private void hActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hActionPerformed

        actionCheckBoxMeans(h);
    }//GEN-LAST:event_hActionPerformed

    private void p3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p3ActionPerformed

        actionCheckBoxMeans(p3);
    }//GEN-LAST:event_p3ActionPerformed

    private void p2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p2ActionPerformed

        actionCheckBoxMeans(p2);
    }//GEN-LAST:event_p2ActionPerformed

    private void aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aActionPerformed

        actionCheckBoxMeans(a);
    }//GEN-LAST:event_aActionPerformed

    private void gActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gActionPerformed

        actionCheckBoxMeans(g);
    }//GEN-LAST:event_gActionPerformed

    private void normToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normToggleButtonActionPerformed

        if (normToggleButton.isSelected()) {
            currentNorms = TOTAL_NORMS;

            n1.setSelected(true);    
            n2.setSelected(true);
            n3.setSelected(true);
            normToggleButton.setText("Uncheck");
        } else {
            currentNorms = 0;

            n1.setSelected(false);
            n2.setSelected(false);
            n3.setSelected(false);
            normToggleButton.setText("Check");
        }
    }//GEN-LAST:event_normToggleButtonActionPerformed

    private void n3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_n3ActionPerformed

        actionCheckBoxNorms(n3);
    }//GEN-LAST:event_n3ActionPerformed

    private void n2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_n2ActionPerformed

        actionCheckBoxNorms(n2);
    }//GEN-LAST:event_n2ActionPerformed

    private void n1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_n1ActionPerformed

        actionCheckBoxNorms(n1);
    }//GEN-LAST:event_n1ActionPerformed

    private void invLaiToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invLaiToggleButtonActionPerformed

        if (invLaiToggleButton.isSelected()) {
            lai.setSelected(true);
            currentClassicsLAI = TOTAL_CLASSICS_LAI;
            invLaiToggleButton.setText("Uncheck"); 
            currentClassics+=TOTAL_CLASSICS_LAI;
            
        } else {
            currentClassicsLAI = 0;
            lai.setSelected(false);
            invLaiToggleButton.setText("Check");
            currentClassics-=TOTAL_CLASSICS_LAI;
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }        
    }//GEN-LAST:event_invLaiToggleButtonActionPerformed

    private void micActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_micActionPerformed
        actionCheckBoxInfTheory(mic);
    }//GEN-LAST:event_micActionPerformed

    private void acActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acActionPerformed
        actionCheckBoxClassicAlgorithms(ac);
    }//GEN-LAST:event_acActionPerformed

    private void laiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laiActionPerformed
        actionCheckBoxClassicsLAI(lai);
    }//GEN-LAST:event_laiActionPerformed

    private void gvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gvActionPerformed
        actionCheckBoxClassicAlgorithms(gv);
    }//GEN-LAST:event_gvActionPerformed

    private void tsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tsActionPerformed
        actionCheckBoxClassicAlgorithms(ts);
    }//GEN-LAST:event_tsActionPerformed

    private void khActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_khActionPerformed
        actionCheckBoxClassicAlgorithms(kh);
    }//GEN-LAST:event_khActionPerformed

    private void esActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_esActionPerformed
        actionCheckBoxClassicAlgorithms(es);
    }//GEN-LAST:event_esActionPerformed

    private void ibActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ibActionPerformed
        actionCheckBoxClassicAlgorithms(ib);
    }//GEN-LAST:event_ibActionPerformed

    private void gcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gcActionPerformed
        actionCheckBoxClassicOther(gc);
    }//GEN-LAST:event_gcActionPerformed

    private void pcdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pcdActionPerformed
        actionCheckBoxClassicOther(pcd);
    }//GEN-LAST:event_pcdActionPerformed

    private void ceiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceiActionPerformed
        actionCheckBoxClassicOther(cei);
    }//GEN-LAST:event_ceiActionPerformed

    private void rdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdfActionPerformed
        actionCheckBoxClassicAlgorithms(rdf);
    }//GEN-LAST:event_rdfActionPerformed

    private void isActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isActionPerformed
        actionCheckBoxClassicAlgorithms(is);
    }//GEN-LAST:event_isActionPerformed

    private void morseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_morseActionPerformed
        actionCheckBoxClassicAlgorithms(morse);
    }//GEN-LAST:event_morseActionPerformed

    private void sicnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sicnActionPerformed
        actionCheckBoxInfTheory(sicn);
    }//GEN-LAST:event_sicnActionPerformed

    private void micnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_micnActionPerformed
        actionCheckBoxInfTheory(micn);
    }//GEN-LAST:event_micnActionPerformed

    private void ticnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ticnActionPerformed
        actionCheckBoxInfTheory(ticn);
    }//GEN-LAST:event_ticnActionPerformed

    private void apmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apmActionPerformed
        actionCheckBoxClassicOther(apm);// TODO add your handling code here:
    }//GEN-LAST:event_apmActionPerformed

    private void entropyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entropyActionPerformed
        actionCheckBoxInfTheory(entropy);
    }//GEN-LAST:event_entropyActionPerformed

    private void invInfTheoryToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invInfTheoryToggleButtonActionPerformed
        if (invInfTheoryToggleButton.isSelected()) {
            sic.setSelected(true);
            tic.setSelected(true);
            mic.setSelected(true);
            sicn.setSelected(true);
            ticn.setSelected(true);
            micn.setSelected(true);
            entropy.setSelected(true);
            
            currentClassicsInfTheory = TOTAL_CLASSICS_INF_THEORY;
            currentClassics+=TOTAL_CLASSICS_INF_THEORY;
            invInfTheoryToggleButton.setText("Uncheck");
            
        } else {
            currentClassicsInfTheory = 0;
            currentClassics-=TOTAL_CLASSICS_INF_THEORY;
            sic.setSelected(false);
            tic.setSelected(false);
            mic.setSelected(false);
            sicn.setSelected(false);
            ticn.setSelected(false);
            micn.setSelected(false);
            entropy.setSelected(false);
            invInfTheoryToggleButton.setText("Check");
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }
    }//GEN-LAST:event_invInfTheoryToggleButtonActionPerformed

    private void invClassicalAlgorithmsToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invClassicalAlgorithmsToggleButtonActionPerformed
        if (invClassicalAlgorithmsToggleButton.isSelected()) {
            ac.setSelected(true);
            gv.setSelected(true);
            ts.setSelected(true);
            kh.setSelected(true);
            es.setSelected(true);
            ib.setSelected(true);
            rdf.setSelected(true);
            morse.setSelected(true);
            is.setSelected(true);
            
            currentClassicsAlgortihms = TOTAL_CLASSICS_ALGORITHMS;
            currentClassics+=TOTAL_CLASSICS_ALGORITHMS;
            invClassicalAlgorithmsToggleButton.setText("Uncheck");
            
        } else {
            currentClassicsAlgortihms = 0;
            currentClassics-=TOTAL_CLASSICS_ALGORITHMS;
            ac.setSelected(false);
            gv.setSelected(false);
            ts.setSelected(false);
            kh.setSelected(false);
            es.setSelected(false);
            ib.setSelected(false);
            rdf.setSelected(false);
            morse.setSelected(false);
            is.setSelected(false);
            invClassicalAlgorithmsToggleButton.setText("Check");
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }
    }//GEN-LAST:event_invClassicalAlgorithmsToggleButtonActionPerformed

    private void invOtherToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invOtherToggleButtonActionPerformed
       if (invOtherToggleButton.isSelected()) {
            gc.setSelected(true);
            pcd.setSelected(true);
            cei.setSelected(true);
            bft.setSelected(true);
            apm.setSelected(true);
            
            currentClassicsOther = TOTAL_CLASSICS_OTHER;
            currentClassics+=TOTAL_CLASSICS_OTHER;
            invOtherToggleButton.setText("Uncheck");
            
        } else {
            currentClassicsOther = 0;
            currentClassics-=TOTAL_CLASSICS_OTHER;
            gc.setSelected(false);
            pcd.setSelected(false);
            cei.setSelected(false);
            bft.setSelected(false);
            apm.setSelected(false);
            invOtherToggleButton.setText("Check");
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }
    }//GEN-LAST:event_invOtherToggleButtonActionPerformed

    private void bftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bftActionPerformed
        actionCheckBoxClassicOther(bft);
    }//GEN-LAST:event_bftActionPerformed

    private void ticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ticActionPerformed
        actionCheckBoxInfTheory(tic);
    }//GEN-LAST:event_ticActionPerformed

    private void sicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sicActionPerformed
        actionCheckBoxInfTheory(sic);
    }//GEN-LAST:event_sicActionPerformed
    private void setNoClassicInvPanelState(boolean state) {
        for (int i = 1; i <= 3; i++) {
            jTabbedPane1.setEnabledAt(i, state);
        }
    }

    private void actionCheckBoxNorms(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentNorms--;
            normToggleButton.setSelected(false);
            normToggleButton.setText("Check");
        } else {
            currentNorms++;
            if (currentNorms == TOTAL_NORMS) {
                normToggleButton.setSelected(true);
                normToggleButton.setText("Uncheck");
            }
        }
    }

    private void actionCheckBoxMeans(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentMeans--;
            meanToggleButton.setSelected(false);
            meanToggleButton.setText("Check");
        } else {
            currentMeans++;
            if (currentMeans == TOTAL_MEANS) {
                meanToggleButton.setSelected(true);
                meanToggleButton.setText("Uncheck");
            }
        }
    }

    private void actionCheckBoxStatistics(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentStatistics--;
            statsToggleButton.setSelected(false);
            statsToggleButton.setText("Check");
        } else {
            currentStatistics++;
            if (currentStatistics == TOTAL_STATISTICS) {
                statsToggleButton.setSelected(true);
                statsToggleButton.setText("Uncheck");
            }
        }
    }

    private void actionCheckBoxClassicsLAI(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentClassicsLAI--;
            invLaiToggleButton.setSelected(false);
            invLaiToggleButton.setText("Check");
            currentClassics--;
        } else {
            currentClassicsLAI++;
            currentClassics++;
            if (currentClassicsLAI == TOTAL_CLASSICS_LAI) {
                invLaiToggleButton.setSelected(true);
                invLaiToggleButton.setText("Uncheck");
            }
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }     
        
    }
    
    private void actionCheckBoxInfTheory(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentClassicsInfTheory--;
            invInfTheoryToggleButton.setSelected(false);
            invInfTheoryToggleButton.setText("Check");
            currentClassics--;
        } else {
            currentClassicsInfTheory++;
            currentClassics++;
            if (currentClassicsInfTheory == TOTAL_CLASSICS_INF_THEORY) {
                invInfTheoryToggleButton.setSelected(true);
                invInfTheoryToggleButton.setText("Uncheck");
            }
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }    
        
    }
    
    private void actionCheckBoxClassicAlgorithms(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentClassicsAlgortihms--;
            invClassicalAlgorithmsToggleButton.setSelected(false);
            invClassicalAlgorithmsToggleButton.setText("Check");
            currentClassics--;
        } else {
            currentClassicsAlgortihms++;
            currentClassics++;
            if (currentClassicsAlgortihms == TOTAL_CLASSICS_ALGORITHMS) {
                invClassicalAlgorithmsToggleButton.setSelected(true);
                invClassicalAlgorithmsToggleButton.setText("Uncheck");
            }
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }
    }
    
    private void actionCheckBoxClassicOther(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentClassicsOther--;
            invOtherToggleButton.setSelected(false);
            invOtherToggleButton.setText("Check");
            currentClassics--;
        } else {
            currentClassicsOther++;
            currentClassics++;
            if (currentClassicsOther == TOTAL_CLASSICS_OTHER) {
                invOtherToggleButton.setSelected(true);
                invOtherToggleButton.setText("Uncheck");
            }
        }
        
        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }
    }
    
    private void actionCheckBoxClassics(JCheckBox cb) {
        if (!cb.isSelected()) {
            currentClassics--;
            invLaiToggleButton.setSelected(false);
            invLaiToggleButton.setText("Check");
        } else {
            currentClassics++;
            if (currentClassics == TOTAL_CLASSICS) {
                invLaiToggleButton.setSelected(true);
                invLaiToggleButton.setText("Uncheck");
            }
        }

        if (currentClassics == 0) {
            uncheck_all.doClick();
            setNoClassicInvPanelState(false);
        } else {
            if (currentClassics==getSimpleInvCount()) {
                setNoClassicInvPanelState(false);
            } else {
                setNoClassicInvPanelState(true);
            }
        }
    }

    private int getSimpleInvCount() 
    {
        int simpleInvCount = 0;
        
        if(sic.isSelected())
        {
            simpleInvCount++;
        }
        
        if(tic.isSelected())
        {
            simpleInvCount++;
        }
        
        if(sicn.isSelected())
        {
            simpleInvCount++;
        }
        
        if(ticn.isSelected())
        {
            simpleInvCount++;
        }
        
        return simpleInvCount;    
    }
    
    private boolean onlyContainSimpleInvs() {

        for (String classic_invariant : list_classics_invariants) 
        {
            switch (classic_invariant) 
            {
                case "sic":
                case "tic":
                case "sicn":
                case "ticn":
                    break;
                default:
                    return false;
            }
        }

        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox a;
    private javax.swing.JCheckBox ac;
    private javax.swing.JCheckBox apm;
    private javax.swing.JCheckBox bft;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cei;
    private javax.swing.JButton check_all;
    private javax.swing.JCheckBox choquet;
    private javax.swing.JCheckBox entropy;
    private javax.swing.JCheckBox es;
    private javax.swing.JCheckBox g;
    private javax.swing.JCheckBox gc;
    private javax.swing.JCheckBox gv;
    private javax.swing.JCheckBox h;
    private javax.swing.JCheckBox i50;
    private javax.swing.JCheckBox ib;
    private javax.swing.JToggleButton invClassicalAlgorithmsToggleButton;
    private javax.swing.JToggleButton invInfTheoryToggleButton;
    private javax.swing.JToggleButton invLaiToggleButton;
    private javax.swing.JToggleButton invOtherToggleButton;
    private javax.swing.JCheckBox is;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAddChoquetConfig;
    private javax.swing.JButton jButtonAddOWAWAConfig;
    private javax.swing.JButton jButtonDefaultChoquet;
    private javax.swing.JButton jButtonDefaultOWAWA;
    private javax.swing.JButton jButtonRemoveChoquetConfig;
    private javax.swing.JButton jButtonRemoveOWAWAConfig;
    private javax.swing.JButton jButtonViewChoquetConfig;
    private javax.swing.JButton jButtonViewOWAWAConfig;
    private javax.swing.JComboBox jComboBoxChoquetMethod;
    private javax.swing.JComboBox<String> jComboBoxLovisOrder;
    private javax.swing.JComboBox jComboBoxOWAWeighted;
    private javax.swing.JComboBox jComboBoxWAWeighted;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelChoquetInfo;
    private javax.swing.JLabel jLabelOWAWAInfo;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSpinner jSpinnerChoquet;
    private javax.swing.JSpinner jSpinnerChoquetAlfaMethod;
    private javax.swing.JSpinner jSpinnerOWAAlfa;
    private javax.swing.JSpinner jSpinnerOWABeta;
    private javax.swing.JSpinner jSpinnerOWALambda;
    private javax.swing.JSpinner jSpinnerOWAWABeta;
    private javax.swing.JSpinner jSpinnerWAAlfa;
    private javax.swing.JSpinner jSpinnerWABeta;
    private javax.swing.JSpinner jSpinnerWADelta;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JCheckBox k;
    private javax.swing.JCheckBox kh;
    private javax.swing.JCheckBox lai;
    private javax.swing.JToggleButton meanToggleButton;
    private javax.swing.JCheckBox mic;
    private javax.swing.JCheckBox micn;
    private javax.swing.JCheckBox mn;
    private javax.swing.JCheckBox morse;
    private javax.swing.JCheckBox mx;
    private javax.swing.JCheckBox n1;
    private javax.swing.JCheckBox n2;
    private javax.swing.JCheckBox n3;
    private javax.swing.JToggleButton normToggleButton;
    private javax.swing.JButton ok;
    private javax.swing.JCheckBox owawa;
    private javax.swing.JCheckBox p2;
    private javax.swing.JCheckBox p3;
    private javax.swing.JCheckBox pcd;
    private javax.swing.JCheckBox q1;
    private javax.swing.JCheckBox q2;
    private javax.swing.JCheckBox q3;
    private javax.swing.JCheckBox ra;
    private javax.swing.JCheckBox rdf;
    private javax.swing.JCheckBox s;
    private javax.swing.JCheckBox sd;
    private javax.swing.JCheckBox sic;
    private javax.swing.JCheckBox sicn;
    private javax.swing.JToggleButton statsToggleButton;
    private javax.swing.JCheckBox tic;
    private javax.swing.JCheckBox ticn;
    private javax.swing.JCheckBox ts;
    private javax.swing.JButton uncheck_all;
    private javax.swing.JCheckBox v;
    private javax.swing.JCheckBox vc;
    // End of variables declaration//GEN-END:variables

    void readConfiguration(Element root) {
        uncheck_all.doClick();
        Element e = (Element) root.getFirstChildElement("invariants");

        int n = e.getChildCount();

        for (int i = 0; i < n; i++) {
            if (e.getChild(i) instanceof Element) {
                Element ec = (Element) e.getChild(i);
                String name = ec.getLocalName();

                setInvariantsPanelAttributes(name, ec);
            }
        }

        actionCheckBoxOWAWA();
        jLabelOWAWAInfo.setText(list_owawas.size() == 1 ? "1 configuration" : list_owawas.size() + " configurations");

        actionCheckBoxChoquet();
        jLabelChoquetInfo.setText(list_choquets.size() == 1 ? "1 configuration" : list_choquets.size() + " configurations");

        generate();
    }

    void saveConfiguration(Element root) {
        Element invariants = new Element("invariants");

        list_classics_invariants.forEach((inv) -> {
            invariants.appendChild(new Element(inv.toLowerCase()));
        });

        list_no_classic_invariants.forEach((inv) -> {
            switch (inv) {
                case "GOWAWA": {
                    list_owawas.values().stream().forEach((owawa_params) -> {
                        String betaOWAWA = owawa_params.get(OWAWA.PARAMETER_NAMES.BETA_OWAWA).toString();
                        String lambdaOWA = owawa_params.get(OWAWA.PARAMETER_NAMES.LAMBDA_OWA).toString();
                        String methodOWA = owawa_params.get(OWAWA.PARAMETER_NAMES.METHOD_OWA).toString();
                        String alfaOWA = owawa_params.get(OWAWA.PARAMETER_NAMES.ALFA_OWA).toString();
                        String betaOWA = owawa_params.get(OWAWA.PARAMETER_NAMES.BETA_OWA).toString();
                        String deltaWA = owawa_params.get(OWAWA.PARAMETER_NAMES.DELTA_WA).toString();
                        String methodWA = owawa_params.get(OWAWA.PARAMETER_NAMES.METHOD_WA).toString();
                        String alfaWA = owawa_params.get(OWAWA.PARAMETER_NAMES.ALFA_WA).toString();
                        String betaWA = owawa_params.get(OWAWA.PARAMETER_NAMES.BETA_WA).toString();

                        Element temp = new Element(InvariantSymbol.gowawa.toString().toLowerCase());
                        temp.addAttribute(new Attribute("value", betaOWAWA + "//" + lambdaOWA + "//" + methodOWA + "//" + alfaOWA + "//" + betaOWA
                                + "//" + deltaWA + "//" + methodWA + "//" + alfaWA + "//" + betaWA));
                        invariants.appendChild(temp);
                    });
                }
                break;
                case "CHOQUET": {
                    list_choquets.values().stream().forEach((choquet_params) -> {
                        String order = choquet_params.get(Choquet.PARAMETER_NAMES.ORDER).toString();
                        String L_value = choquet_params.get(Choquet.PARAMETER_NAMES.L_VALUE).toString();
                        String method = choquet_params.get(Choquet.PARAMETER_NAMES.SINGLETON_METHOD).toString();
                        String alfa = choquet_params.get(Choquet.PARAMETER_NAMES.ALFA_SINGLETON_METHOD).toString();

                        Element temp = new Element(InvariantSymbol.choquet.toString().toLowerCase());
                        temp.addAttribute(new Attribute("value", order + "//" + L_value + "//" + method + "//" + alfa));
                        invariants.appendChild(temp);
                    });
                }
                break;
                default: {
                    invariants.appendChild(new Element(inv.toLowerCase()));
                }
            }
        });

        root.appendChild(invariants);
    }

    private void setInvariantsPanelAttributes(String attribute, Element ec) {
        switch (attribute.toLowerCase()) {

            case "lai":
                lai.setSelected(true);
                actionCheckBoxClassics(lai);
                break;
            case "h":
                entropy.setSelected(true);
                actionCheckBoxClassics(entropy);
                break;
            case "ac":
                ac.setSelected(true);
                actionCheckBoxClassics(ac);
                break;
            case "gv":
                gv.setSelected(true);
                actionCheckBoxClassics(gv);
                break;
            case "ts":
                ts.setSelected(true);
                actionCheckBoxClassics(ts);
                break;
            case "mic":
                mic.setSelected(true);
                actionCheckBoxClassics(mic);
                break;
            case "tic":
                tic.setSelected(true);
                actionCheckBoxClassics(tic);
                break;
            case "sic":
                sic.setSelected(true);
                actionCheckBoxClassics(sic);
                break;
            case "micn":
                micn.setSelected(true);
                actionCheckBoxClassics(micn);
                break;
            case "ticn":
                ticn.setSelected(true);
                actionCheckBoxClassics(ticn);
                break;
            case "sicn":
                sicn.setSelected(true);
                actionCheckBoxClassics(sicn);
                break;
            case "es":
                es.setSelected(true);
                actionCheckBoxClassics(es);
                break;
            case "ib":
                ib.setSelected(true);
                actionCheckBoxClassics(ib);
                break;
            case "kh":
                kh.setSelected(true);
                actionCheckBoxClassics(kh);
                break;
            case "gc":
                gc.setSelected(true);
                actionCheckBoxClassics(gc);
                break;
            case "pcd":
                pcd.setSelected(true);
                actionCheckBoxClassics(pcd);
                break;
            case "cei":
                cei.setSelected(true);
                actionCheckBoxClassics(cei);
                break;
            case "rdf":
                rdf.setSelected(true);
                actionCheckBoxClassics(rdf);
                break;
            case "mse":
                morse.setSelected(true);
                actionCheckBoxClassics(morse);
                break;
            case "is":
                is.setSelected(true);
                actionCheckBoxClassics(is);
                break;
            case "bft":
                bft.setSelected(true);
                actionCheckBoxClassics(bft);
                break;
            case "apm":
                apm.setSelected(true);
                actionCheckBoxClassics(apm);
                break;
            case "n1":
                n1.setSelected(true);
                actionCheckBoxNorms(n1);
                break;
            case "n2":
                n2.setSelected(true);
                actionCheckBoxNorms(n2);
                break;
            case "n3":
                n3.setSelected(true);
                actionCheckBoxNorms(n3);
                break;
            case "gm":
                g.setSelected(true);
                actionCheckBoxMeans(g);
                break;
            case "am":
                a.setSelected(true);
                actionCheckBoxMeans(a);
                break;
            case "p2":
                p2.setSelected(true);
                actionCheckBoxMeans(p2);
                break;
            case "p3":
                p3.setSelected(true);
                actionCheckBoxMeans(p3);
                break;
            case "hm":
                h.setSelected(true);
                actionCheckBoxMeans(h);
                break;
            case "v":
                v.setSelected(true);
                actionCheckBoxStatistics(v);
                break;
            case "s":
                s.setSelected(true);
                actionCheckBoxStatistics(s);
                break;
            case "k":
                k.setSelected(true);
                actionCheckBoxStatistics(k);
                break;
            case "sd":
                sd.setSelected(true);
                actionCheckBoxStatistics(sd);
                break;
            case "vc":
                vc.setSelected(true);
                actionCheckBoxStatistics(vc);
                break;
            case "ra":
                ra.setSelected(true);
                actionCheckBoxStatistics(ra);
                break;
            case "q1":
                q1.setSelected(true);
                actionCheckBoxStatistics(q1);
                break;
            case "q2":
                q2.setSelected(true);
                actionCheckBoxStatistics(q2);
                break;
            case "q3":
                q3.setSelected(true);
                actionCheckBoxStatistics(q3);
                break;
            case "i50":
                i50.setSelected(true);
                actionCheckBoxStatistics(i50);
                break;
            case "mx":
                mx.setSelected(true);
                actionCheckBoxStatistics(mx);
                break;
            case "mn":
                mn.setSelected(true);
                actionCheckBoxStatistics(mn);
                break;

            case "gowawa": {
                String[] attr = ec.getAttributeValue("value").split("//");
                owawa.setSelected(true);

                HashMap<OWAWA.PARAMETER_NAMES, Object> parameters = new HashMap<>();
                parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWAWA, Float.parseFloat(attr[0]));
                parameters.put(OWAWA.PARAMETER_NAMES.LAMBDA_OWA, Integer.parseInt(attr[1]));
                parameters.put(OWAWA.PARAMETER_NAMES.METHOD_OWA, attr[2]);
                parameters.put(OWAWA.PARAMETER_NAMES.ALFA_OWA, Float.parseFloat(attr[3]));
                parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWA, Float.parseFloat(attr[4]));
                parameters.put(OWAWA.PARAMETER_NAMES.DELTA_WA, Integer.parseInt(attr[5]));
                parameters.put(OWAWA.PARAMETER_NAMES.METHOD_WA, attr[6]);
                parameters.put(OWAWA.PARAMETER_NAMES.ALFA_WA, Float.parseFloat(attr[7]));
                parameters.put(OWAWA.PARAMETER_NAMES.BETA_WA, Float.parseFloat(attr[8]));

                list_owawas.put(ec.getAttributeValue("value"), parameters);
            }
            break;

            case "choquet": {
                String[] attr = ec.getAttributeValue("value").split("//");
                choquet.setSelected(true);

                HashMap<Choquet.PARAMETER_NAMES, Object> parameters = new HashMap<>();
                parameters.put(Choquet.PARAMETER_NAMES.ORDER, attr[0]);
                parameters.put(Choquet.PARAMETER_NAMES.L_VALUE, Float.parseFloat(attr[1]));
                parameters.put(Choquet.PARAMETER_NAMES.SINGLETON_METHOD, attr[2]);
                parameters.put(Choquet.PARAMETER_NAMES.ALFA_SINGLETON_METHOD, Float.parseFloat(attr[3]));

                list_choquets.put(ec.getAttributeValue("value"), parameters);
            }
            break;
        }
    }

    private boolean showMessgInGenerate;

    void generate() {
        ok.doClick();

        if (showMessgInGenerate && list_no_classic_invariants.isEmpty() && list_classics_invariants.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must select at least one aggregation operator.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }

        if (list_no_classic_invariants.isEmpty() && !list_classics_invariants.isEmpty()&&!onlyContainSimpleInvs()) {
            list_classics_invariants.clear();
            list_no_classic_invariants.clear();
            invariant_parameters.clear();
        }
    }

    private void actionCheckBoxOWAWA() {
        if (!owawa.isSelected()) {
            list_owawas.clear();
            jLabelOWAWAInfo.setText("0 configurations");
            if (owawaListDialog != null) {
                owawaListDialog.updateList(list_owawas.keySet());
            }
        }

        jSpinnerOWAWABeta.setEnabled(owawa.isSelected());
        jComboBoxOWAWeighted.setEnabled(owawa.isSelected());
        jSpinnerOWALambda.setEnabled(owawa.isSelected());
        jSpinnerOWAAlfa.setEnabled(owawa.isSelected());
        jSpinnerOWABeta.setEnabled(owawa.isSelected() && jComboBoxOWAWeighted.getSelectedIndex() < 2);
        jComboBoxWAWeighted.setEnabled(owawa.isSelected());
        jSpinnerWADelta.setEnabled(owawa.isSelected());
        jSpinnerWAAlfa.setEnabled(owawa.isSelected());
        jSpinnerWABeta.setEnabled(owawa.isSelected() && jComboBoxWAWeighted.getSelectedIndex() < 2);

        jButtonAddOWAWAConfig.setEnabled(owawa.isSelected());
        jButtonRemoveOWAWAConfig.setEnabled(owawa.isSelected());
        jButtonDefaultOWAWA.setEnabled(owawa.isSelected());
        jButtonViewOWAWAConfig.setEnabled(owawa.isSelected());
    }

    private void actionCheckBoxChoquet() {
        if (!choquet.isSelected()) {
            list_choquets.clear();
            jLabelChoquetInfo.setText("0 configurations");
            if (choquetListDialog != null) {
                choquetListDialog.updateList(list_choquets.keySet());
            }
        }

        jComboBoxLovisOrder.setEnabled(choquet.isSelected());
        jSpinnerChoquet.setEnabled(choquet.isSelected());
        jComboBoxChoquetMethod.setEnabled(choquet.isSelected());
        jSpinnerChoquetAlfaMethod.setEnabled(choquet.isSelected());

        jButtonAddChoquetConfig.setEnabled(choquet.isSelected());
        jButtonRemoveChoquetConfig.setEnabled(choquet.isSelected());
        jButtonViewChoquetConfig.setEnabled(choquet.isSelected());
        jButtonDefaultChoquet.setEnabled(choquet.isSelected());
    }

    private void jSpinnerOWAWABetaStateChanged() {
        if (owawa.isSelected()) {
            boolean value = Float.parseFloat(jSpinnerOWAWABeta.getValue().toString()) == 0.0f;

            jSpinnerOWALambda.setEnabled(!value);
            jSpinnerOWAAlfa.setEnabled(!value);
            jSpinnerOWABeta.setEnabled(!value && jSpinnerOWABeta.isEnabled());
            jComboBoxOWAWeighted.setEnabled(!value);

            value = Float.parseFloat(jSpinnerOWAWABeta.getValue().toString()) == 1.0f;

            jSpinnerWADelta.setEnabled(!value);
            jSpinnerWAAlfa.setEnabled(!value);
            jSpinnerWABeta.setEnabled(!value && jSpinnerWABeta.isEnabled());
            jComboBoxWAWeighted.setEnabled(!value);
        }
    }

    final private String[] defaultOWAWAs = {
        "0.0//1//NONE//0.0//0.0//0//S_OWA//0.0//1.0",
        "0.0//1//NONE//0.0//0.0//1//S_OWA//1.0//0.0",
        "0.0//1//NONE//0.0//0.0//2//WINDOW_OWA//0.4//0.5",
        "0.0//1//NONE//0.0//0.0//2//WINDOW_OWA//0.5//0.6",
        "0.0//1//NONE//0.0//0.0//2//WINDOW_OWA//0.5//0.7",
        "0.0//1//NONE//0.0//0.0//2//WINDOW_OWA//0.7//0.8",
        "0.1//0//AGGREGATED_OBJECTS_1//1.0//0.0//2//S_OWA//0.8//0.1",
        "0.1//0//WINDOW_OWA//0.1//0.6//2//WINDOW_OWA//0.1//0.2",
        "0.1//2//EXPONENTIAL_SMOOTHING_2//0.9//0.0//0//WINDOW_OWA//0.0//0.1",
        "0.1//2//EXPONENTIAL_SMOOTHING_2//0.9//0.0//2//WINDOW_OWA//0.2//0.3",
        "0.1//2//EXPONENTIAL_SMOOTHING_2//0.9//0.0//2//WINDOW_OWA//0.3//0.4",
        "0.1//2//EXPONENTIAL_SMOOTHING_2//0.9//0.0//2//WINDOW_OWA//0.7//0.8",
        "0.2//2//EXPONENTIAL_SMOOTHING_2//0.9//0.0//2//WINDOW_OWA//0.4//0.6",
        "0.2//2//S_OWA//0.6//0.0//2//S_OWA//0.8//0.1",
        "0.3//1//S_OWA//1.0//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "0.3//2//S_OWA//0.6//0.0//2//WINDOW_OWA//0.9//1.0",
        "0.4//1//AGGREGATED_OBJECTS_2//1.0//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "0.4//1//EXPONENTIAL_SMOOTHING_1//0.7//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "0.5//1//AGGREGATED_OBJECTS_2//1.0//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "0.6//1//S_OWA//1.0//0.0//2//WINDOW_OWA//0.9//1.0",
        "0.7//2//EXPONENTIAL_SMOOTHING_2//0.9//0.0//0//S_OWA//0.0//1.0",
        "0.8//1//AGGREGATED_OBJECTS_2//1.0//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "0.9//1//AGGREGATED_OBJECTS_2//1.0//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "0.9//1//EXPONENTIAL_SMOOTHING_1//0.7//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "0.9//1//S_OWA//1.0//0.0//1//EXPONENTIAL_SMOOTHING_2//0.9//0.0",
        "1.0//1//S_OWA//1.0//0.0//1//NONE//0.0//0.0",
        "1.0//2//EXPONENTIAL_SMOOTHING_2//0.9//0.0//1//NONE//0.0//0.0",
        "1.0//1//AGGREGATED_OBJECTS_2//1.0//0.0//1//NONE//0.0//0.0",
        "1.0//1//EXPONENTIAL_SMOOTHING_1//0.7//0.0//1//NONE//0.0//0.0",
        "1.0//1//S_OWA//0.8//0.2//1//NONE//0.0//0.0"
    };

    final private String[] defaultChoquet = {
        "ASCENDING//0.25//AGGREGATED_OBJECTS_2//0.8",
        "DESCENDING//0.25//AGGREGATED_OBJECTS_1//0.8",
        "DESCENDING//0.25//AGGREGATED_OBJECTS_1//0.9",
        "DESCENDING//0.25//AGGREGATED_OBJECTS_2//0.5",
        "DESCENDING//0.25//AGGREGATED_OBJECTS_2//0.6",
        "DESCENDING//0.5//AGGREGATED_OBJECTS_1//0.2",
        "DESCENDING//0.5//AGGREGATED_OBJECTS_1//0.9",
        "DESCENDING//0.5//AGGREGATED_OBJECTS_2//0.5",
        "DESCENDING//0.5//AGGREGATED_OBJECTS_2//0.6",
        "DESCENDING//0.75//AGGREGATED_OBJECTS_1//0.2",
        "DESCENDING//0.75//AGGREGATED_OBJECTS_1//0.8",
        "DESCENDING//0.75//AGGREGATED_OBJECTS_1//0.9",
        "DESCENDING//0.75//AGGREGATED_OBJECTS_2//0.5",
        "DESCENDING//0.75//AGGREGATED_OBJECTS_2//0.6",
        "DESCENDING//0.9//AGGREGATED_OBJECTS_2//0.2",
        "ASCENDING//-0.3//AGGREGATED_OBJECTS_2//0.0",
        "ASCENDING//-0.8//AGGREGATED_OBJECTS_1//0.1",
        "ASCENDING//-0.9//AGGREGATED_OBJECTS_2//0.0",
        "ASCENDING//-0.9//AGGREGATED_OBJECTS_2//0.6",
        "ASCENDING//-0.9//AGGREGATED_OBJECTS_2//0.8",
        "DESCENDING//-0.25//AGGREGATED_OBJECTS_2//0.6",
        "DESCENDING//-0.25//AGGREGATED_OBJECTS_2//1.0",
        "DESCENDING//-0.75//AGGREGATED_OBJECTS_1//0.2",
        "DESCENDING//-0.75//AGGREGATED_OBJECTS_2//0.0",
        "DESCENDING//-0.75//AGGREGATED_OBJECTS_2//0.2",
        "DESCENDING//-0.9//AGGREGATED_OBJECTS_1//1.0",
        "DESCENDING//-0.9//AGGREGATED_OBJECTS_2//0.7",
        "DESCENDING//-0.9//AGGREGATED_OBJECTS_2//0.9",
        "DESCENDING//-0.9//AGGREGATED_OBJECTS_2//1.0"
    };
}
