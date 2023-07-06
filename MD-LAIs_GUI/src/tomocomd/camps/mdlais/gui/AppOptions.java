/**
 * @author Rajarshi Guha
 */
package tomocomd.camps.mdlais.gui;

import java.util.HashMap;
import java.util.Map;
import org.openscience.cdk.qsar.DescriptorEngine;
import tomocomd.camps.mdlais.workers.output.OutputFormats;

public class AppOptions 
{
    private static String outputMethod = OutputFormats.OUTPUT_SPC;
    private static DescriptorEngine engine = new DescriptorEngine(DescriptorEngine.MOLECULAR);
    private static Map<String, Boolean> selectedDescriptors = new HashMap<String, Boolean>();
    private static String settingsFile = "";
    private static String selectedFingerprintType = null;

    public static String getSelectedFingerprintType() {
        return selectedFingerprintType;
    }

    public static void setSelectedFingerprintType(String selectedFingerprintType) {
        AppOptions.selectedFingerprintType = selectedFingerprintType;
    }

    public String getSettingsFile() {
        return settingsFile;
    }

    public void setSettingsFile(String settingsFile) {
        AppOptions.settingsFile = settingsFile;
    }

    public Map<String, Boolean> getSelectedDescriptors() {
        return selectedDescriptors;
    }

    public static DescriptorEngine getEngine() {
        return engine;
    }

    public String getOutputMethod() {
        return outputMethod;
    }

    public void setOutputMethod(String outputMethod) {
        AppOptions.outputMethod = outputMethod;
    }

    private static AppOptions ourInstance = new AppOptions();

    public static AppOptions getInstance() {
        return ourInstance;
    }

    private AppOptions() {
    }
}
