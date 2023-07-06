package tomocomd.camps.mdlais.pdbfilter;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author econtreras
 */
public class PDBReaderUtils {

    public static File[] readFiles(Component parent) {
        JFileChooser jfc = new JFileChooser();

        jfc.setVisible(true);

        jfc.setMultiSelectionEnabled(true);

        int status = jfc.showOpenDialog(parent);

        if (status == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFiles();

        } else {
            return null;
        }
    }

    public static File[] selectFile(Component parent) {
        JFileChooser jfc = new JFileChooser();

        jfc.setVisible(true);

        jfc.setMultiSelectionEnabled(false);

        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int status = jfc.showSaveDialog(parent);

        if (status == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFiles();

        } else {
            return null;
        }
    }

    public static String getStringAccordingToFilterType(AtomFilterType atomFilterType) 
    {
        switch (atomFilterType) 
        {
            case CA:
                return "CA";
            case CB:
                return "CB";
            case AVERAGE:
                return "AVG";
            case AB:
                return "AB";
            case AVR:
                return "AVR";
        }
        return null;
    }
}
