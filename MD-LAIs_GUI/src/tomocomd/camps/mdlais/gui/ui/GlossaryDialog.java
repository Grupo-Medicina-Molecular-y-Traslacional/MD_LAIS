/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author jricardo
 */
public class GlossaryDialog extends javax.swing.JDialog {

    HashMap<Integer, String> glossary;
    /**
     * Creates new form GlossaryDialog
     */
    public GlossaryDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width/2 - getSize().width/2, screen.height/2 - getSize().height/2);
        glossary = new HashMap<>();
        
        glossary.put(0, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary0.html");
        glossary.put(1, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary1.html");
        glossary.put(2, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary2.html");        
        glossary.put(3, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary3.html");
        glossary.put(4, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary4.html");
        glossary.put(5, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary5.html");
        glossary.put(6, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary6.html");
        glossary.put(7, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary7.html");
        glossary.put(8, "/tomocomd/camps/mdlais/gui/data/webpages/glossary/glossary8.html");
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bt_close = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tp_showMeans = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Glossary");
        setMinimumSize(new java.awt.Dimension(805, 350));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bt_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/tipsclose.png"))); // NOI18N
        bt_close.setText("Close");
        bt_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_closeActionPerformed(evt);
            }
        });
        getContentPane().add(bt_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 290, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Definition(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.setMaximumSize(new java.awt.Dimension(413, 153));
        jPanel2.setMinimumSize(new java.awt.Dimension(413, 153));
        jPanel2.setPreferredSize(new java.awt.Dimension(413, 153));

        tp_showMeans.setEditable(false);
        tp_showMeans.setContentType("text/html"); // NOI18N
        tp_showMeans.setText("<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 5\" align=center>\r\n<br>\n<br>\n<br>\n     <i> \rSelect an element from left panel to display the associated information</i>\n    </p>\r\n  </body>\r\n</html>\r\n");
        tp_showMeans.setToolTipText("");
        jScrollPane2.setViewportView(tp_showMeans);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 390, 290));

        jScrollPane1.setMaximumSize(new java.awt.Dimension(413, 153));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(413, 153));

        jList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dictionary", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jList1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Molecular descriptor", "CAMD-BIR Unit", "ToMoCoMD", "CAMPS", "MD-LAIs", "Fuzzy Weight", "Amino acid properties", "Aggregation operators" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 380, 273));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_closeActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_bt_closeActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        int posSelect = jList1.getSelectedIndex();
        try {
            tp_showMeans.setPage(getClass().getResource(glossary.get(posSelect)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jList1ValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GlossaryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GlossaryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GlossaryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GlossaryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                GlossaryDialog dialog = new GlossaryDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton bt_close;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane tp_showMeans;
    // End of variables declaration//GEN-END:variables
}