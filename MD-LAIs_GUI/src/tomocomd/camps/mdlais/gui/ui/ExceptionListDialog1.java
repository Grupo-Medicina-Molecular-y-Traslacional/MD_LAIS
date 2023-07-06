package tomocomd.camps.mdlais.gui.ui;

import exceptions.ExceptionInfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import util.GUIUtil;

/**
 * @author rguha
 */
public class ExceptionListDialog1 extends JDialog 
{
    private String errorText;
    
    private void makeText(List<ExceptionInfo> list) 
    {
        errorText = "<html><body>";
        
        errorText += "<table>"
                + "<tr>"
                + "<td><b>Errors</b></td>" + "</tr>";
        
        for (ExceptionInfo ei : list) 
        {
          String fileInfo = ei.getFile()!=null?" at file: "+ei.getFile().getName():"";
          
          errorText += "<tr>"+ "<td>" + ei.getMyMessage()+ fileInfo +"</td></tr>";
        }
        
        errorText += "</table></body></html>";
    }
    public ExceptionListDialog1(JDialog parent,List<ExceptionInfo> list) throws HeadlessException 
    {
        super(parent);
        
        makeText(list);

        setTitle("MD-LAIs - Exceptions");

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
        
        setSize(500, 400);
        
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                
        setLocation(screen.width / 2 - 306, screen.height / 2 - 360);
    }
    
    public ExceptionListDialog1(JFrame parent,List<ExceptionInfo> list) throws HeadlessException 
    {
        super(parent);
        
        makeText(list);

        setTitle("MD-LAIs - Exceptions");

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
        
        setSize(500, 400);
        
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                
        setLocation(screen.width / 2 - 306, screen.height / 2 - 360);
    }
    
    public ExceptionListDialog1(List<ExceptionInfo> list) throws HeadlessException 
    {
        super();
        
        makeText(list);

        setTitle("GAPSS - Exceptions");

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
        
        setSize(500, 400);
        
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                
        setLocation(screen.width / 2 - 306, screen.height / 2 - 360);
    }

    private void onOK() {
        FileFilter[] filters = new FileFilter[]{GUIUtil.getCustomFileFilter(GUIUtil.HTML_EXTENSION, GUIUtil.HTML_DESCRIPTION)};

        JFileChooser fileChooser = new GUIUtil().loadFilesandFileChooserLastInputDirectory(this, GUIUtil.INPUT_FILES_DIALOG_TITLE, true, filters);

        if (fileChooser==null) 
        {
            return;
        }
        
        File aFile = fileChooser.getSelectedFile();

        try {
            
            if(!aFile.getName().endsWith(".html"))
            {
              aFile = new File(aFile.getParentFile(),aFile.getName()+".html");
            }
            
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
