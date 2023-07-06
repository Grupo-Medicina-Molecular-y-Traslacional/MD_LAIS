package tomocomd.camps.mdlais.gui.ui;

import javax.swing.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

/**
 * @cdk.author Rajarshi Guha
 * @cdk.svnrev $Revision: 9162 $
 */
public class PluginUI extends JDialog {

    public JList pluginList;
    private ImageIcon jarIcon;
    private JButton disableButton;

    public PluginUI(String[] pluginItems) {

        super();

        // get the icon for the entries in the list
        URL imageUrl = this.getClass().getClassLoader().getResource("net/guha/apps/cdkdesc/data/jaricon.png");
        jarIcon = new ImageIcon(imageUrl);
        Image jarImage = jarIcon.getImage();
        jarIcon = new ImageIcon(jarImage.getScaledInstance(16, 16, Image.SCALE_AREA_AVERAGING));

        // create the holding panels
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        // make all the buttons
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.addChoosableFileFilter(new JarFileFilter());
                int status = fileChooser.showOpenDialog(pluginList);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    DefaultListModel model = (DefaultListModel) pluginList.getModel();
                    for (File selectedFile : selectedFiles) {
                        model.addElement(new PluginListEntry(selectedFile.getAbsolutePath(), true));
                    }
                }
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                DefaultListModel model = (DefaultListModel) pluginList.getModel();
                Object[] values = pluginList.getSelectedValues();
                for (Object value : values) {
                    model.remove(model.indexOf(value));
                }
            }
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Object[] selectedValues = pluginList.getSelectedValues();
                // insert the descriptors into the tree at this point
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });


        disableButton = new JButton("Disable");
        disableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[] selectedValues = pluginList.getSelectedValues();
                for (Object object : selectedValues) {
                    PluginListEntry ple = (PluginListEntry) object;
                    if (ple.isEnabled()) ple.setEnabled(false);
                    else
                        ple.setEnabled(true);
                }
                pluginList.repaint();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(disableButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // now make the list pane and scroll pane        
        DefaultListModel listModel = new DefaultListModel();
        if (pluginItems != null) {
            for (String s : pluginItems) listModel.addElement(new PluginListEntry(s, true));
        }
        pluginList = new JList(listModel);
        JarListRenderer renderer = new JarListRenderer();
        pluginList.setCellRenderer(renderer);

        pluginList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pluginList.setLayoutOrientation(JList.VERTICAL);

        JScrollPane scrollPane = new JScrollPane(pluginList, VERTICAL_SCROLLBAR_AS_NEEDED,
                HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // make some dummy spacer panels
        JPanel dummy1 = new JPanel();
        JPanel dummy2 = new JPanel();
        JPanel dummy3 = new JPanel();
        dummy1.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        dummy1.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        dummy3.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        // set everything up
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(dummy1, BorderLayout.WEST);
        mainPanel.add(dummy2, BorderLayout.NORTH);
        mainPanel.add(dummy3, BorderLayout.SOUTH);

        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setContentPane(mainPanel);

        setContentPane(mainPanel);
        setModal(true);
        setTitle("CDK Descriptor Plugins");
        setSize(450, 411);
    }


    public static void main(String[] args) {
        PluginUI pui = new PluginUI(new String[]{"dgasdfasdfasfasfasfsdf",
                "dfdefdfdsdfasdasdfsdfasfasdfasasfs",
                "sdfasdfasdfasdfasdfasfasfasdfasfasfasdfsdfasdfsdf",
                "dfgfgdfgdfgdfgsdfgsdgsdgdf"});
        pui.setVisible(true);
    }

    class JarFileFilter extends javax.swing.filechooser.FileFilter {
        public boolean accept(File file) {
            return file.getName().endsWith(".jar") || file.isDirectory();
        }

        public String getDescription() {
            return "*.jar";
        }
    }

    class JarListRenderer extends JLabel implements ListCellRenderer {
        public JarListRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList jList, Object o, int i, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(jList.getSelectionBackground());
                setForeground(jList.getSelectionForeground());
            } else {
                setBackground(jList.getBackground());
                setForeground(jList.getForeground());
            }

            PluginListEntry ple = (PluginListEntry) o;
            setIcon(jarIcon);
            setText(ple.toString());

            if (isSelected) {
                setBackground(new Color(153, 204, 255));
                if (ple.isEnabled()) disableButton.setText("Disable");
                else
                    disableButton.setText("Enable");
            } else {
                if (!ple.isEnabled())
                    setBackground(Color.gray);
                else
                    setBackground(Color.white);
            }
            return this;
        }
    }

    class PluginListEntry {
        String text;
        boolean enabled;

        public String getText() {
            return text;
        }

        public PluginListEntry(String text, boolean isEnabled) {
            this.text = text;
            this.enabled = isEnabled;
        }

        public String toString() {
            return text;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean flag) {
            enabled = flag;
        }
    }

}
