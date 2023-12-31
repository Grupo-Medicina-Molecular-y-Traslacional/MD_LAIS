
/*
 * StatusPanel.java
 *
 * Created on 7/Out/2011, 1:17:46
 */
package tomocomd.camps.mdlais.gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 *
 * @author yasilvalmeida
 */
public class StatusPanel extends javax.swing.JPanel {

    /**
     * Creates new form StatusPanel
     */
    private Action clearAction = new AbstractAction("clear") {
        public void actionPerformed(ActionEvent evt) {
            statusTextArea.setText("");
        }
    };

    public StatusPanel() 
    {
        initComponents();

        installContextMenu(statusTextArea);

        statusTextArea.getActionMap().put("clear", clearAction);
        
        statusTextArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK, false),clearAction );
    }

    private void installContextMenu(final JTextArea comp) {

        comp.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
                
                if (e.isPopupTrigger()) {
                    final JPopupMenu menu = new JPopupMenu();
                    JMenuItem item;
                    Action act = comp.getActionMap().get("copy-to-clipboard");
                    item = new JMenuItem(act);
                    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));                    
                    item.setText("Copy");        
                    item.setEnabled(comp.getSelectionStart() != comp.getSelectionEnd());
                    menu.add(item);
                    act = comp.getActionMap().get("clear");
                    item = new JMenuItem(act);
                    item.setText("Clear Logs");
                    item.setEnabled(!comp.getText().trim().isEmpty());
                    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
                    menu.add(item);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public JTextArea getTextArea() {
        return statusTextArea;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        statusTextArea = new javax.swing.JTextArea();

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setBorder(null);

        statusTextArea.setEditable(false);
        statusTextArea.setColumns(20);
        statusTextArea.setLineWrap(true);
        statusTextArea.setRows(5);
        statusTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(statusTextArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea statusTextArea;
    // End of variables declaration//GEN-END:variables
}
