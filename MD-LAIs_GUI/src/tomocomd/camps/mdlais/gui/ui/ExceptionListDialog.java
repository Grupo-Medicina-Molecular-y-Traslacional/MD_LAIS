package tomocomd.camps.mdlais.gui.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IMolecule;
import tomocomd.camps.mdlais.exceptions.ExceptionInfo;

/**
 * @author rguha
 */
public class ExceptionListDialog extends JDialog 
{
    private String errorText;
    
    private void makeText(List<ExceptionInfo> list) {
        errorText = "<html><body>";
        errorText += "<table>"
                + "<tr>"
                + "<td><b>Mol. Number</b></td><td><b>Title</b></td><td><b>Exception Message</b></td>"
                + "<td><b>Descriptor</b></td>"
                + "</tr>";
        for (ExceptionInfo ei : list) {
            int molnum = ei.getSerial();
            IMolecule molecule = ei.getMolecule();
            String title = null;
            try {
                title = (String) molecule.getProperty(CDKConstants.TITLE);
            } catch (Exception ej) {
                //TODO: recuperar el titulo.
            }
            if (title == null) {
                title = "";
            }
            String excepText = ei.getException().getMessage();
            if ( excepText == null )
            {
                excepText = ei.getMyMessage();
            }
            String descName = ei.getDescriptorName();
            if ( descName == null )
            {
                descName = ei.getMyMessage();
            }
            
            errorText += "<tr>" + "<td>" + ((int) molnum + 1) + "</td>"
                    + "<td>" + title + "</td>"
                    + "<td>" + excepText + "</td><td>" + descName + "</td></tr>";
        }
        errorText += "</table></body></html>";
    }
    
    
    public ExceptionListDialog(List<ExceptionInfo> list) throws HeadlessException {
        super();
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());
        
        makeText(list);

        setTitle("ToMoCoMD-CAMPS - Exceptions");

        JPanel panel = new JPanel(new BorderLayout());
        JEditorPane textArea = new JEditorPane("text/html", errorText);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setEditable(false);
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeButton);
        buttonPanel.add(saveButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        panel.registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        setContentPane(panel);
        setSize(600, 723);
    }
    

    private void onOK() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(this.getParent());
        File aFile = fileChooser.getSelectedFile();
        if (aFile == null) {
            return;
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(aFile));
            out.write(errorText);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
