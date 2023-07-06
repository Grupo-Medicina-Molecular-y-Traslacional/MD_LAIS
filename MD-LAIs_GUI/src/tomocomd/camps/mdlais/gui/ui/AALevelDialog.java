/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import javax.swing.ImageIcon;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Raul Jacas
 */
public class AALevelDialog extends javax.swing.JDialog 
{
    private ApplicationUI ui;
    
    public AALevelDialog(java.awt.Frame parent, boolean modal, ApplicationUI ui)
    {
        super(parent, modal);
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
        this.ui = ui;
    }
    
    public boolean isAALevel()
    {
        return jCheckBox1.isSelected();
    }
    public int getWindowSize(){
        
        return (int) jSpinner1.getValue();
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

        jSpinner1 = new javax.swing.JSpinner();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();

        setTitle("LAI Output");
        setMinimumSize(new java.awt.Dimension(210, 115));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));
        jSpinner1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(jSpinner1, gridBagConstraints);

        jButton2.setText("Ok");
        jButton2.setMaximumSize(new java.awt.Dimension(81, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(81, 23));
        jButton2.setName(""); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(81, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jButton2, gridBagConstraints);

        jCheckBox1.setText("Lag K:");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jCheckBox1, gridBagConstraints);

        jLabel2.setPreferredSize(new java.awt.Dimension(0, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        getContentPane().add(jLabel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        setVisible( false );        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        boolean state = false;
        if(jCheckBox1.isSelected()){
            state = true;
        }
        jSpinner1.setEnabled(state);        
        ui.setIsAA_level(state);
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables

    public void setSelected(boolean b) {
        jCheckBox1.setSelected(b);
        jSpinner1.setEnabled(b);
    }

    void setLagValue(int windowSize) 
    {
        jSpinner1.setValue(windowSize);
    }
}
