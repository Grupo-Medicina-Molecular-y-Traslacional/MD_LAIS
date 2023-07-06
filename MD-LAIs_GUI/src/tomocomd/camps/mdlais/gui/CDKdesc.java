package tomocomd.camps.mdlais.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.exceptions.ExceptionInfo;
import tomocomd.camps.mdlais.exceptions.TomocomdException;
import tomocomd.camps.mdlais.workers.ITaskFinishedListener;
import tomocomd.camps.mdlais.workers.ProteinCalculatorSwingWorker;
import tomocomd.camps.mdlais.gui.ui.ApplicationMenu;
import tomocomd.camps.mdlais.gui.ui.ApplicationUI;
import tomocomd.camps.mdlais.gui.ui.ExceptionListDialog;
import tomocomd.camps.mdlais.gui.ui.Splash;

/**
 * @author Rajarshi Guha
 */
public class CDKdesc extends JFrame implements DropTargetListener, ITaskFinishedListener {

    private static final long serialVersionUID = 1L;

    public static ExceptionListDialog eld = null;

    private long startTime;
    private long finishTime;

    private ApplicationUI ui;

    private final JProgressBar progressBar1, progressBar2;

    private JButton goButton;

    private ProteinCalculatorSwingWorker task;
    private File tempFile;
    private final JPanel statusPanel1;
    private final JPanel statusPanel2;
    private ApplicationMenu appMenu;
    private List<IDescriptor> selectedDescriptors = new ArrayList<>();

    static public Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

    static {
        if (!System.getProperty("os.name").toLowerCase().contains("window")) {
            DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
        }
    }

    public CDKdesc() {
        super("ToMoCoMD-CAMPS MD-LAIs");

        this.setResizable(false);

        //icon setup, tiene que ser 16x16 o 32x32 y en PNG
        setIconImage(new ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/appico.png")).getImage());

        Calendar cal = new GregorianCalendar();
        long time = cal.getTimeInMillis();

        // splash 
        Splash splash = new Splash();
        splash.setVisible(true);

        //set up temp file for output writing
        try {
            tempFile = File.createTempFile("tomocomd-camps", String.valueOf(time));
        } catch (IOException e) {
        }

        tempFile.deleteOnExit();

        try {
            if (CDKDescUtils.isMacOs()) {
                UIManager.setLookAndFeel("apple.laf.AquaLookAndFeel");
                System.setProperty("dock:name", "ToMoCoMD-CAMPS");
                System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
                System.setProperty("com.apple.macos.useScreenMenuBar ", "true");
            } else 
            {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception e) {
        }

        //TEST
        java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
        
        while (keys.hasMoreElements()) 
        {
            Object key = keys.nextElement();
            
            Object value = UIManager.get(key);

            if (value instanceof javax.swing.plaf.FontUIResource) 
            {
                UIManager.put(key, DEFAULT_FONT);
            }
        }

        UIManager.put("ProgressBar.foreground", new Color(2, 111, 190));

        UIManager.put("ProgressBar.background", java.awt.Color.LIGHT_GRAY);
        UIManager.put("Label.foreground", java.awt.Color.black);

        getContentPane().setLayout(new BorderLayout());

        //para poder controlar el evento de cerrar
        CDKdesc.super.setDefaultCloseOperation(CDKdesc.DO_NOTHING_ON_CLOSE);

        //capturando el evento de cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int dialogOption = JOptionPane.showConfirmDialog(CDKdesc.this,
                        "Do you want MD-LAIs to save your descriptors configuration?",
                        "Exit MD-LAIs",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (dialogOption == JOptionPane.NO_OPTION) {
                    shutdown();
                } else if (dialogOption == JOptionPane.YES_OPTION) {
                    appMenu.doSaveProject();
                }
            }
        });

        goButton = new JButton("Run");
        goButton.setName("go");
        goButton.setPreferredSize(new Dimension(95, 50));
        goButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/play.png")));
        
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (goButton.getText()) {
                    case "Run":
                        
                        if (ui.doApply() && JOptionPane.showConfirmDialog(CDKdesc.this,
                                " Do you want to compute in total " + ui.getTotalDesc() + " descriptors?", "Confirm compute", JOptionPane.YES_NO_OPTION) == 0) {
                            goApp(e);
                        }
                        
                        break;
                    case "Cancel":
                        
                        goApp(e);
                        break;
                }
            }
        });

        statusPanel1 = new JPanel(new BorderLayout());
        Border emptyBorder = new EmptyBorder(4, 2, 4, 2);
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        statusPanel1.setBorder(BorderFactory.createCompoundBorder(lowerEtched, emptyBorder));
        progressBar1 = new JProgressBar();
        statusPanel1.add(progressBar1, BorderLayout.CENTER);

        statusPanel2 = new JPanel(new BorderLayout());
        emptyBorder = new EmptyBorder(4, 2, 4, 2);
        lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        statusPanel2.setBorder(BorderFactory.createCompoundBorder(lowerEtched, emptyBorder));
        progressBar2 = new JProgressBar();
        statusPanel2.add(progressBar2, BorderLayout.CENTER);

        progressBar1.setVisible(true);
        progressBar1.setStringPainted(true);
        progressBar1.setBorderPainted(true);
        progressBar1.setString("Proteins");
        progressBar2.setVisible(true);
        progressBar2.setStringPainted(true);
        progressBar2.setBorderPainted(true);
        progressBar2.setString("Descriptors");

        ui = new ApplicationUI(this);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(2, 4, 2, 4));
        buttonPanel.add(goButton, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(new EmptyBorder(2, 4, 2, 4));
        progressPanel.add(statusPanel1, BorderLayout.NORTH);
        progressPanel.add(statusPanel2, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(2, 4, 2, 4));
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        bottomPanel.add(progressPanel, BorderLayout.CENTER);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(ui.getPanel(), BorderLayout.CENTER);

        appMenu = new ApplicationMenu(ui, this);
        setJMenuBar(appMenu.createMenu());

        synchronized (this) {
            try {
                wait(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CDKdesc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        splash.setVisible(false);
        System.gc();
    }

    public boolean isComputingDescriptors() {
        return goButton.getName().equals("cancel");
    }

    public ApplicationMenu getAppMenu() {
        return appMenu;
    }

    public File getTempFile() {
        return tempFile;
    }

    public ApplicationUI getUI() {
        return ui;
    }

    public JProgressBar getJProgressBar1() {
        return progressBar1;
    }

    public JProgressBar getJProgressBar2() {
        return progressBar2;
    }

    public boolean isTaskDone() {
        return task.isDone();
    }

    public boolean isTaskCancel() {
        return task.isCancelled();
    }

    public void doTaskCancel() {
        task.cancelProcess();
    }

    public void doTaskStop() {
        task.stop();
    }

    // aqui esta la cosa
    @SuppressWarnings("empty-statement")
    private void goApp(ActionEvent e) {
        try {
            if (ui.getPbdFileTextField().getText().equals("")
                    || ui.getOutFileTextField().getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please load the input file(s) and define the output file disk destination",
                        "ToMoCoMD-CAMPS Error", JOptionPane.ERROR_MESSAGE);
                System.gc();
                return;
            }

            progressBar1.setVisible(true);
            progressBar1.setStringPainted(true);
            progressBar1.setBorderPainted(true);
            progressBar2.setVisible(true);
            progressBar2.setStringPainted(true);
            progressBar2.setBorderPainted(true);

            if (((JButton) e.getSource()).getName().equals("cancel")) {
                notifyTaskFinished(true);
                goButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/play.png")));
        
                return;
            }

            if (((JButton) e.getSource()).getName().equals("go")) {
                goButton.setName("cancel");
                goButton.setText("Cancel");
                setBlockDuringCompute(false, false);
                goButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/cancel.png")));        
            }
            
            selectedDescriptors = new ArrayList<>();
            
            selectedDescriptors.addAll(ui.getDescriptorList());
           
            if (selectedDescriptors.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select at least one descriptor!",
                        "ToMoCoMD-CAMPS Error",
                        JOptionPane.ERROR_MESSAGE);

                goButton.setName("go");
                goButton.setText("Run");
                goButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/play.png")));
                progressBar1.setValue(0);
                progressBar1.setString("Protein");
                progressBar2.setValue(0);
                progressBar2.setString("Descriptor");
                setBlockDuringCompute(true, true);
                System.gc();
                return;
            }

            task = new ProteinCalculatorSwingWorker(progressBar1, progressBar2, ui.getStatusArea(), this,
                    selectedDescriptors, ui.getPbdFileTextField().getText(), ui.getOutFileTextField().getText(),
                    AppOptions.getInstance().getOutputMethod(), 
                    ui.getProcessorsToUse(), ui.getSelectedDescriptors(), ui.getTotalDesc(),
                    ui.getNewParameters(), ui.getCutoffConfiguration());

            if (task.getInputFormat().equals("invalid")) {
                goButton.setName("go");
                goButton.setText("Run");
                progressBar1.setValue(0);
                progressBar1.setString("Protein");
                progressBar2.setValue(0);
                progressBar2.setString("Descriptor");
                System.gc();
                return;
            }

            startTime = System.currentTimeMillis();

            task.go();
        } catch (TomocomdException ex) {
            Logger.getLogger(CDKdesc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setBlockDuringCompute(boolean status, boolean fromBM) {
        if (fromBM) {
            goButton.setEnabled(status);
        }

        ui.setBlockDuringCompute(status);
        appMenu.setBlockDuringCompute(status);
    }

    @Override
    public void notifyTaskFinished(boolean cancelled) {
        finishTime = System.currentTimeMillis();
        if (cancelled) {
            task.stop();
        }

        goButton.setName("go");
        goButton.setText("Run");

        goButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tomocomd/camps/mdlais/gui/data/play.png")));
        
        progressBar1.setString("Protein");
        progressBar1.setValue(0);
        progressBar2.setString("Descriptor");
        progressBar2.setValue(0);

        String message = cancelled ? "\r\nThe process was cancelled by the user.\r\n"
                : "\r\nThe process was finished.\r\n";

        ui.getStatusArea().append(message + getStats((finishTime - startTime) / 1000,
                task.getExceptionList(), task.getMolecules(), task.getDescriptors()));

        if (!cancelled) {
            JOptionPane.showMessageDialog(CDKdesc.this, "The process was successfully finished !", "Process Finished", JOptionPane.INFORMATION_MESSAGE);
        }

        if (task.getExceptionList().size() > 0) {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

            eld = new ExceptionListDialog(task.getExceptionList());
            eld.setLocation(screen.width / 2 - getSize().width / 2, screen.height / 2 - getSize().height / 2);
            eld.setVisible(true);
        }

        setBlockDuringCompute(true, false);
        System.gc();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar1.setString("Protein");
                progressBar1.setValue(0);
                progressBar2.setString("Descriptor");
                progressBar2.setValue(0);
            }
        });
    }

    /*
     * Return the ToMoCoMD-CAMPS statistics
     */
    private String getStats(long totalTime, List<ExceptionInfo> exceptions, ArrayList<String> moleculesName, ArrayList<String> descriptorsName) {
        String result = "\r\n===============Stats===============\r\n";
        result += "Number of Proteins: " + moleculesName.size() + "\r\n";
        result += "Number of Descriptors: " + (int) (ui.getTotalDesc()) + "\r\n";
        result += "Number of Errors: " + exceptions.size() + "\r\n";
        result += "Total Time: "
                + totalTime / (60 * 60) + "h :"
                + (totalTime % (60 * 60)) / (60) + "m :"
                + (totalTime % (60 * 60)) % 60 + "s";
        return result + "\r\n";
    }

    public void shutdown() {
        System.exit(0);
    }

    public static void main(String[] args) {
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final CDKdesc app = new CDKdesc();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.pack();
                app.setLocation(screen.width / 2 - 306, screen.height / 2 - 360);
                app.setVisible(true);
            }
        });

    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable trans = dtde.getTransferable();
        boolean gotData = false;
        try {
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String s = (String) trans.getTransferData(DataFlavor.stringFlavor);
                this.ui.getPbdFileTextField().setText(s.substring(7));
                gotData = true;
            }
        } catch (Exception e) {
        } finally {
            dtde.dropComplete(gotData);
        }
    }

    public void repaintApp() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }
}
