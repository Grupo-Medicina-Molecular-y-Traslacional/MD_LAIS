/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.Vector;
import javax.swing.ImageIcon;

/**
 *
 * @author yasilvalmeida
 */
public class ListDialog4ChoquetOWAWA extends javax.swing.JDialog 
{
    final private IListDialog4ChoquetOWAWA dialog;
    
    public ListDialog4ChoquetOWAWA( String title, java.awt.Dialog parent, boolean modal, IListDialog4ChoquetOWAWA dialog )
    {
        super( parent, modal );
        
        initComponents();        
        setTitle( title );
        
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width/2 - getSize().width/2, screen.height/2 - getSize().height/2);
        
        this.dialog = dialog;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jButtonClose = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButtonRemove = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 204));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);
        setMinimumSize(new java.awt.Dimension(562, 354));
        setName("tipsDialog"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jButtonClose.setText("Close");
        jButtonClose.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButtonClose.setMaximumSize(new java.awt.Dimension(100, 27));
        jButtonClose.setMinimumSize(new java.awt.Dimension(100, 27));
        jButtonClose.setPreferredSize(new java.awt.Dimension(100, 27));
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 12);
        getContentPane().add(jButtonClose, gridBagConstraints);

        jScrollPane2.setMaximumSize(new java.awt.Dimension(540, 275));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(540, 275));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(540, 275));

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 12);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        jButtonRemove.setText("Remove");
        jButtonRemove.setMaximumSize(new java.awt.Dimension(100, 27));
        jButtonRemove.setMinimumSize(new java.awt.Dimension(100, 27));
        jButtonRemove.setPreferredSize(new java.awt.Dimension(100, 27));
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 5, 0);
        getContentPane().add(jButtonRemove, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        
        setVisible(false);
        
    }//GEN-LAST:event_jButtonCloseActionPerformed
    
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        
        if ( isVisible() && jList1.getSelectedIndices() != null && jList1.getSelectedIndices().length == 1 )
        {
            dialog.updateOperatorList( jList1.getSelectedValue().toString() );
        }
        
    }//GEN-LAST:event_jList1ValueChanged
    
    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveActionPerformed
        
        jList1.getSelectedValuesList().stream().forEach(( item ) -> 
        {
            dialog.removeOperator( item.toString() );
        });
        
    }//GEN-LAST:event_jButtonRemoveActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
    
    public void updateList( Collection<String> list )
    {
        jList1.setListData( new Vector( list ) );
    }
    
    @Override
    public void setVisible( boolean b )
    {
        super.setVisible( b ); //To change body of generated methods, choose Tools | Templates.
        
        if ( b )
        {
            jList1.clearSelection();
        }
    }
}
