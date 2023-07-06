/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author manager
 */
public class MemoryDialog extends javax.swing.JDialog {

    /**
     * Creates new form MemoryDialog
     */
    MemoryMXBean memory;
    Timer timer;
    
    public MemoryDialog(java.awt.Frame parent, boolean modal) 
    {
        super(parent, modal);
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
        initComponents();
        
        memory = ManagementFactory.getMemoryMXBean();
        
        timer = new Timer(1000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
                jLabelUsedValue.setText("" + memory.getHeapMemoryUsage().getUsed() / (1024 * 1024) + " MB");
                jLabelMaxValue.setText("" + memory.getHeapMemoryUsage().getMax() / (1024 * 1024) + " MB");
            }
        });
        
        timer.start();
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

        jLabelUsed = new javax.swing.JLabel();
        jLabelMax = new javax.swing.JLabel();
        jLabelUsedValue = new javax.swing.JLabel();
        jLabelMaxValue = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Memory Manager");
        setLocationByPlatform(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabelUsed.setText("Used Memory: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 2);
        getContentPane().add(jLabelUsed, gridBagConstraints);

        jLabelMax.setText("Max. Memory: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        getContentPane().add(jLabelMax, gridBagConstraints);

        jLabelUsedValue.setText("<html><i>loading...</i>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 8);
        getContentPane().add(jLabelUsedValue, gridBagConstraints);

        jLabelMaxValue.setText("<html><i>loading...</i>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 7);
        getContentPane().add(jLabelMaxValue, gridBagConstraints);

        jButton1.setText("Run Garbage Collector");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 11, 10, 25);
        getContentPane().add(jButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        System.gc();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        
    }//GEN-LAST:event_formMouseMoved

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
        timer.stop();
        
    }//GEN-LAST:event_formWindowClosed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabelMax;
    private javax.swing.JLabel jLabelMaxValue;
    private javax.swing.JLabel jLabelUsed;
    private javax.swing.JLabel jLabelUsedValue;
    // End of variables declaration//GEN-END:variables
}