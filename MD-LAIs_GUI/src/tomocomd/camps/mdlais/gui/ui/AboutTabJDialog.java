/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author manager
 */
public class AboutTabJDialog extends javax.swing.JDialog 
{
    public AboutTabJDialog(java.awt.Frame parent, boolean modal) throws IOException 
    {
        super(parent, modal);
        
        initComponents();
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width/2 - getSize().width/2, screen.height/2 - getSize().height/2);
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
        
        jTextPane1.setPage(getClass().getResource( "/tomocomd/camps/mdlais/gui/data/webpages/about/about.html" ));
        jTextPane2.setPage(getClass().getResource( "/tomocomd/camps/mdlais/gui/data/webpages/about/how.html" ));
        jTextPane3.setPage(getClass().getResource( "/tomocomd/camps/mdlais/gui/data/webpages/about/system.html" ));
    }
    
    /**  This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About and Citations");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(550, 472));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/about-cambird.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(570, 75));
        jLabel1.setMinimumSize(new java.awt.Dimension(570, 75));
        jLabel1.setPreferredSize(new java.awt.Dimension(570, 75));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(550, 380));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(550, 380));
        jTabbedPane1.setName(""); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(550, 380));

        jTextPane1.setEditable(false);
        jScrollPane3.setViewportView(jTextPane1);

        jTabbedPane1.addTab("About MD-LAIs Program", jScrollPane3);

        jTextPane2.setEditable(false);
        jScrollPane4.setViewportView(jTextPane2);

        jTabbedPane1.addTab("How to cite?", jScrollPane4);

        jTextPane3.setEditable(false);
        jScrollPane6.setViewportView(jTextPane3);

        jTabbedPane1.addTab("System Requirements", jScrollPane6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    // End of variables declaration//GEN-END:variables
}
