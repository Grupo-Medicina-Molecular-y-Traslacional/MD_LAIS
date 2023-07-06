/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author manager
 */
public class DescriptorSearchDialog extends javax.swing.JDialog {

    /**
     * Creates new form DescriptorSearchDialog
     */
    private HashMap<String, String> diccionary;

    public DescriptorSearchDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();
        setResizable(false);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width / 2 - getSize().width / 2, screen.height / 2 - getSize().height / 2);
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());

        diccionary = new HashMap<>();

        //0
        diccionary.put("AC", "1D Autocorrelation Invariant");
        diccionary.put("AC3D", "3D Autocorrelation Invariant");
        diccionary.put("GV", "1D Gravitational Invariant");
        diccionary.put("GV3D", "3D Gravitational Invariant");
        diccionary.put("MIC", "Mean Information Content Invariant");
        diccionary.put("SIC", "Standarized Information Content Invariant");
        diccionary.put("TIC", "Total Information Content Invariant");
        diccionary.put("MICN", "Mean Information Content (N-bins) Invariant");
        diccionary.put("SICN", "Standarized Information Content (N-bins) Invariant");
        diccionary.put("TICN", "Total Information Content (N-bins)Invariant");
        diccionary.put("TS", "1D Total Sum Invariant");
        diccionary.put("TS3D", "3D Total Sum Invariant");
        diccionary.put("IB", "1D Ivanciuc-Balaban Invariant");
        diccionary.put("IB3D", "3D Ivanciuc-Balaban Invariant");
        diccionary.put("ES", "1D ElectroTopological State Invariant");
        diccionary.put("ES3D", "3D ElectroTopological State Invariant");
        diccionary.put("KH", "1D Kier-Hall Invariant");
        diccionary.put("KH3D", "3D Kier-Hall Invariant");
        diccionary.put("GC", "1D Geary Coefficient Invariant");
        diccionary.put("GC3D", "3D Geary Coefficient Invariant");
        diccionary.put("PCD", "1D Potential of Charge Distribution Invariant");
        diccionary.put("PCD3D", "3D Potential of Charge Distribution Invariant");
        diccionary.put("CEI", "1D Connective Eccentricity Index Invariant");
        diccionary.put("CEI3D", "3D Connective Eccentricity Index Invariant");
        diccionary.put("RDF", "1D RDF Code Invariant");
        diccionary.put("RDF3D", "3D RDF Code Invariant");
        diccionary.put("MORSE", "1D MoRSE Invariant");
        diccionary.put("MORSE3D", "3D MoRSE Invariant");
        diccionary.put("IS", "1D Interaction Spectrum Invariant");
        diccionary.put("IS3D", "3D Interaction Spectrum Invariant");
        diccionary.put("BFT", "Beteringe-Filip-Tarko Invariant");
        diccionary.put("APM", "1D Amphiphilic Moments Invariant");
        diccionary.put("APM3D", "3D Amphiphilic Moments Invariant");
        diccionary.put("H", "Entropy Invariant");
        //1
        diccionary.put("STD", "Standarized LAIS");

        //2  
        diccionary.put("N1", "Manhattan Distance Invariant");
        diccionary.put("N2", "Euclidean Distance Invariant");
        diccionary.put("N3", "Minkowski Distance Invariant");

        diccionary.put("GM", "Geometric Mean Invariant");
        diccionary.put("AM", "Arithmetic Mean (alfa = 1) Invariant");
        diccionary.put("P2", "Quadratic Mean (alfa = 2) Invariant");
        diccionary.put("P3", "Potential Mean (alfa = 3) Invariant");
        diccionary.put("HM", "Harmonic Mean (alfa = -1) Invariant");

        diccionary.put("V", "Variance Invariant");
        diccionary.put("S", "Skewness Invariant");
        diccionary.put("K", "Kurtosis Invariant");
        diccionary.put("SD", "Standard Deviation Invariant");
        diccionary.put("VC", "Variation Coefficient Invariant");
        diccionary.put("RA", "Range Invariant");
        diccionary.put("Q1", "Percentile 25 Invariant");
        diccionary.put("Q2", "Percentile 50 Invariant");
        diccionary.put("Q3", "Percentile 75 Invariant");
        diccionary.put("i50", "Q3-Q1 Invariant");
        diccionary.put("MX", "Maximun Invariant");
        diccionary.put("MN", "Minimun Invariant");

        diccionary.put("GOWAWA", "Generalized Ordered Weighted Averaging (GOWA) - Weighted Averaging (WGM) Operator");
        diccionary.put("CHOQUET", "Choquet Integral with respect to the fuzzy Lmδ-measure");

        //3 (30)locales
        diccionary.put("T", "Total (Global) indices");
        diccionary.put("RAP", "Apolar");
        diccionary.put("RPC", "Polar positively charged");
        diccionary.put("RNC", "Polar negatively charged");
        diccionary.put("RPU", "Polar uncharged");
        diccionary.put("ARO", "Aromatic");
        diccionary.put("ALG", "Aliphatic");
        diccionary.put("FAH", "Alpha helix favoring");
        diccionary.put("FBS", "Beta sheet favoring");
        diccionary.put("AFT", "Beta turn favoring");
        diccionary.put("UFG", "Uncommon in alpha helix and or beta sheet");

        diccionary.put("ALA", "Alanine");
        diccionary.put("ARG", "Arginine");
        diccionary.put("ASN", "Asparagine");
        diccionary.put("ASP", "Aspartate");
        diccionary.put("CYS", "Cysteine");
        diccionary.put("GLU", "Glutamate");
        diccionary.put("GLN", "Glutamine");
        diccionary.put("GLY", "Glycine");
        diccionary.put("HIS", "Histidine");
        diccionary.put("ILE", "Isoleucine");
        diccionary.put("LEU", "Leucine");
        diccionary.put("LYS", "Lysine");
        diccionary.put("MET", "Methionine");
        diccionary.put("PHE", "Phenylalanine");
        diccionary.put("PRO", "Proline");
        diccionary.put("SER", "Serine");
        diccionary.put("THR", "Threonine");
        diccionary.put("TRP", "Tryptophan");
        diccionary.put("TYR", "Tyrosine");
        diccionary.put("VAL", "Valine");

        //4    
        diccionary.put("UW", "Means no weight is applied to all elements in the molecular vector");
        diccionary.put("LGSTN", "Fuzzy membership according to distance to N-terminal amino acid");
        diccionary.put("LGSTM", "Fuzzy membership according to distance to the center of the protein");
        diccionary.put("LGSTC", "Fuzzy membership according to distance to C-terminal amino acid");
 
        //5 16 propiedades  aminoacidos
        diccionary.put("MM", "Mass");
        diccionary.put("MV", "Side-chain volume");
        diccionary.put("Z1", "Z1-scale");
        diccionary.put("Z2", "Z2-scale");
        diccionary.put("Z3", "Z3-scale");
        diccionary.put("ECI", "Atomic charge");
        diccionary.put("ISA", "Isotropic Surface Area");
        diccionary.put("HWS", "Hoop-Woods hydropathy index");
        diccionary.put("KDS", "Kyte-Dolittle hydropathy index");
        diccionary.put("PIE", "Isoelectric point");
        diccionary.put("EPS", "Heat of formation");
        diccionary.put("PAH", "Alpha helix frequency");
        diccionary.put("PBS", "Beta sheet frequency");
        diccionary.put("PTT", "Reverse turn frequency");
        diccionary.put("GCP1", "Geometric compatibility parameter 1");
        diccionary.put("GCP2", "Geometric compatibility parameter 2");
         diccionary.put("U", "Unit property");
        
        diccionary.put("VHSE1", "Vector of Hydrophobic Properties");
        diccionary.put("VHSE4", "Vector of Steric Properties");
        diccionary.put("VHSE6", "Vector of Electronic Properties (PC6)");
        diccionary.put("VHSE7", "Vector of Electronic Properties (PC7)");
        diccionary.put("VHSE8", "Vector of Electronic Properties (PC8)");
        diccionary.put("T2", "T-scale derived from PCA of topological indices (PC2)");
        diccionary.put("T3", "T-scale derived from PCA of topological indices (PC3)");
        diccionary.put("T4", "T-scale derived from PCA of topological indices (PC4)");
        diccionary.put("MDL1", "Scale derived from PCA of MD-LOVIS indices (PC1)");
        diccionary.put("MDL2", "Scale derived from PCA of MD-LOVIS indices (PC2)");
        diccionary.put("MDL3", "Scale derived from PCA of MD-LOVIS indices (PC3)");
        diccionary.put("MDL4", "Scale derived from PCA of MD-LOVIS indices (PC4)");
        diccionary.put("MDL5", "Scale derived from PCA of MD-LOVIS indices (PC5)");
        diccionary.put("MDL6", "Scale derived from PCA of MD-LOVIS indices (PC6)");        
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

        tb_pattern = new javax.swing.JTextField();
        bt_close = new javax.swing.JButton();
        bt_parse = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_fullDescription = new javax.swing.JTextArea();

        setTitle("Descriptor Search");
        setMinimumSize(new java.awt.Dimension(480, 390));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tb_pattern.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tb_pattern.setText("simply paste or type a descriptor name");
        tb_pattern.setToolTipText("simply paste or type a descriptor name");
        tb_pattern.setMaximumSize(new java.awt.Dimension(327, 27));
        tb_pattern.setMinimumSize(new java.awt.Dimension(327, 27));
        tb_pattern.setName(""); // NOI18N
        tb_pattern.setNextFocusableComponent(bt_parse);
        tb_pattern.setPreferredSize(new java.awt.Dimension(327, 27));
        tb_pattern.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_patternMouseClicked(evt);
            }
        });
        tb_pattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tb_patternActionPerformed(evt);
            }
        });
        tb_pattern.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tb_patternKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tb_patternKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        getContentPane().add(tb_pattern, gridBagConstraints);

        bt_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/tipsclose.png"))); // NOI18N
        bt_close.setText("Close");
        bt_close.setMaximumSize(new java.awt.Dimension(115, 27));
        bt_close.setMinimumSize(new java.awt.Dimension(115, 27));
        bt_close.setPreferredSize(new java.awt.Dimension(115, 27));
        bt_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_closeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 350, 0, 0);
        getContentPane().add(bt_close, gridBagConstraints);

        bt_parse.setText("Search...");
        bt_parse.setMaximumSize(new java.awt.Dimension(115, 27));
        bt_parse.setMinimumSize(new java.awt.Dimension(115, 27));
        bt_parse.setPreferredSize(new java.awt.Dimension(115, 27));
        bt_parse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_parseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 350, 0, 0);
        getContentPane().add(bt_parse, gridBagConstraints);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(450, 280));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(450, 280));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(450, 280));

        ta_fullDescription.setColumns(20);
        ta_fullDescription.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        ta_fullDescription.setRows(5);
        jScrollPane1.setViewportView(ta_fullDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 12);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_closeActionPerformed

        this.setVisible(false);

    }//GEN-LAST:event_bt_closeActionPerformed

    private void bt_parseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_parseActionPerformed

        String fullDescription = "";

        String[] tags = tb_pattern.getText().split("_");
        if (tags.length < 3) {
            fullDescription = fullDescription + "Descriptor Name Lenght Fail, minimal codification needs 3 elements\n\n"
                    + "ToMoCOMD-CAMPS will try to understand individual codification of your query: \n\n";
        }

        try {
            for (String tag : tags) {
                String currentTag = tag.trim();

                //para los tags que no tienen cosas raras
                if (diccionary.containsKey(currentTag)) {
                    fullDescription = fullDescription + currentTag + " -> " + diccionary.get(currentTag) + "\n";

                } // para los tags con cosas raras                
                else if (currentTag.startsWith("AC")
                        || currentTag.startsWith("GV")
                        || currentTag.startsWith("TS")
                        || currentTag.startsWith("KH")) {
                    String invID = currentTag.substring(0, 2);
                    String invLag = currentTag.substring(2).substring(1, currentTag.substring(2).length() - 1);
                    fullDescription = fullDescription + currentTag + " -> " + diccionary.get(invID) + " with a lag value of " + invLag + ".\n";
                } else if (currentTag.startsWith("GOWAWA")) {
                    fullDescription = fullDescription + currentTag.substring(0, 6) + " -> " + diccionary.get(currentTag.substring(0, 6));

                    String[] values = currentTag.substring(currentTag.indexOf("[") + 1, currentTag.lastIndexOf("]")).split(";");
                    String[] items = {" GOWA λ: ", ", GOWA method: ", ", GOWA α: ", ", GOWA β: ",
                        "  WGM δ: ", ",  WGM method: ", ",  WGM α: ", ",  WGM β: "};

                    fullDescription = fullDescription + "\n" + "   GOWAWA β: " + values[0] + "\n";

                    for (int i = 0, j = 1; j < values.length;) {
                        if (values[j].equalsIgnoreCase("none")) {
                            fullDescription = fullDescription + items[i++] + "0";
                            fullDescription = fullDescription + items[i++] + values[j++];
                            fullDescription = fullDescription + items[i++] + "0.0";
                            fullDescription = fullDescription + items[i++] + "0.0" + "\n";
                        } else {
                            fullDescription = fullDescription + items[i++] + values[j++];
                            fullDescription = fullDescription + items[i++] + values[j++];
                            fullDescription = fullDescription + items[i++] + values[j++];

                            if (j < values.length && isFloat(values[j])) {
                                fullDescription = fullDescription + items[i++] + values[j++] + "\n";
                            } else {
                                fullDescription = fullDescription + items[i++] + "0.0" + "\n";
                            }
                        }
                    }
                    fullDescription = fullDescription + "\n";
                } else if (currentTag.startsWith("CHOQUET")) {
                    fullDescription = fullDescription + currentTag.substring(0, 7) + " -> " + diccionary.get(currentTag.substring(0, 7));

                    String[] values = currentTag.substring(currentTag.indexOf("[") + 1, currentTag.lastIndexOf("]")).split(";");
                    fullDescription = fullDescription + "\n" + "             LOVIs order: " + (values[0].equals("A") ? "Ascending" : "Descending") + "\n"
                            + "                 L-value: " + values[1] + "\n"
                            + "    Fuzzy Density Method: " + values[2] + "\n"
                            + "  Fuzzy Density Method α: " + values[3] + "\n";
                } else if (currentTag.startsWith("NS")
                        || currentTag.startsWith("SS")
                        || currentTag.startsWith("MP")) {
                    fullDescription = fullDescription
                            + currentTag.substring(0, 2) + " -> " + diccionary.get(currentTag.substring(0, 2)) + "\n";

                    String matrixOrder = currentTag.substring(2);
                    fullDescription = fullDescription + "Matrix Order -> " + matrixOrder + "\n";
                } else if (currentTag.startsWith("M9")) {
                    fullDescription = fullDescription
                            + currentTag.substring(0, 2) + " -> " + diccionary.get(currentTag.substring(0, 2)) + " ";

                    String lagValues = currentTag.substring(2).substring(1, currentTag.substring(2).length() - 1);
                    fullDescription = fullDescription + "p = " + lagValues + "\n";

                } else if (currentTag.startsWith("M33") || currentTag.startsWith("M34")
                        || currentTag.startsWith("M35") || currentTag.startsWith("M36")
                        || currentTag.startsWith("M37") || currentTag.startsWith("M38")
                        || currentTag.startsWith("M41") || currentTag.startsWith("M42")
                        || currentTag.startsWith("M43") || currentTag.startsWith("M44")
                        || currentTag.startsWith("M45") || currentTag.startsWith("M46")
                        || currentTag.startsWith("M47") || currentTag.startsWith("M48")
                        || currentTag.startsWith("M49") || currentTag.startsWith("M50")
                        || currentTag.startsWith("M51") || currentTag.startsWith("M52")
                        || currentTag.startsWith("M53") || currentTag.startsWith("M54")
                        || currentTag.startsWith("M55") || currentTag.startsWith("M56")
                        || currentTag.startsWith("M57") || currentTag.startsWith("M58")
                        || currentTag.startsWith("M59") || currentTag.startsWith("M60")
                        || currentTag.startsWith("M61") || currentTag.startsWith("M62")
                        || currentTag.startsWith("M63") || currentTag.startsWith("M64")
                        || currentTag.startsWith("M65") || currentTag.startsWith("M66")
                        || currentTag.startsWith("M67") || currentTag.startsWith("M68")) {
                    fullDescription = fullDescription
                            + currentTag.substring(0, 3) + " -> " + diccionary.get(currentTag.substring(0, 3)) + " ";

                    String lagValues = currentTag.substring(3).substring(1, currentTag.substring(3).length() - 1);
                    fullDescription = fullDescription + "(" + diccionary.get(lagValues) + ")\n";
                } else if (currentTag.startsWith("LG")) {

                    if (currentTag.length() >= 3) {

                        boolean istruncation = currentTag.startsWith("LGST");

                        int length = istruncation ? 5 : 3;

                        fullDescription = fullDescription
                                + currentTag.substring(0, length) + " -> "
                                + diccionary.get(currentTag.substring(0, length)) + "\n";

                        fullDescription += istruncation ? "\t" + "Function:" + currentTag.substring(currentTag.indexOf("(") + 1, currentTag.indexOf(")")) + "\n" + "\t"
                                + "Parameters:" + currentTag.substring(currentTag.indexOf("[") + 1, currentTag.indexOf("]"))
                                + "\n" : "";

                    }

                    /*String lagValues = currentTag.substring(2).substring(1, currentTag.substring(2).length() - 1);
                     fullDescription = fullDescription + "Lag K Values -> sss" + lagValues + "\n";*/
                } else if (currentTag.regionMatches(2, "-", 0, 1)) {
                    String[] properties = currentTag.split("-");
                    for (String propers : properties) {
                        if (diccionary.containsKey(propers)) {
                            fullDescription = fullDescription + propers + " -> " + diccionary.get(propers) + "\n";
                        } else {
                            fullDescription = fullDescription + "Syntax error: " + propers + "\n";
                        }
                    }
                } else {
                    fullDescription = fullDescription + "Syntax error: " + currentTag + "\n";
                }
            } //end for tags

            //Ex: AC[1]_STD_N2_F_AB_nCi_2_NS0_T_LG[2;4]_e-p
            ta_fullDescription.setText(fullDescription);
        } catch (Exception e) {
            e.printStackTrace();
            ta_fullDescription.setText("ERROR! in Descriptor Name check your typing\n");
        }

    }//GEN-LAST:event_bt_parseActionPerformed

    private void tb_patternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tb_patternActionPerformed

        bt_parse.doClick();

    }//GEN-LAST:event_tb_patternActionPerformed

    private void tb_patternKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_patternKeyTyped


    }//GEN-LAST:event_tb_patternKeyTyped

    private void tb_patternMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_patternMouseClicked

        if (tb_pattern.getText().equalsIgnoreCase("simply paste or type a descriptor name")) {
            tb_pattern.setText("");
        }

    }//GEN-LAST:event_tb_patternMouseClicked

    private void tb_patternKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_patternKeyReleased

        bt_parse.doClick();

    }//GEN-LAST:event_tb_patternKeyReleased

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return str.contains(".") || str.contains(",");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_close;
    private javax.swing.JButton bt_parse;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea ta_fullDescription;
    private javax.swing.JTextField tb_pattern;
    // End of variables declaration//GEN-END:variables
}
