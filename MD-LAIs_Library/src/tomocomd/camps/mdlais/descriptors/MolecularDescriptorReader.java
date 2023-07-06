package tomocomd.camps.mdlais.descriptors;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.local.LocalType;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.weights.WeightType;
import tomocomd.camps.mdlais.weights.WeightValue;
import tomocomd.camps.mdlais.weights.WeightValueInterval;
import tomocomd.camps.mdlais.weights.ReferenceType;
import tomocomd.camps.mdlais.properties.AminoAcidProperty;
import tomocomd.camps.mdlais.tools.invariants.Choquet;
import tomocomd.camps.mdlais.tools.invariants.DimensionType;
import tomocomd.camps.mdlais.tools.invariants.InvariantType;
import tomocomd.camps.mdlais.tools.invariants.OWAWA;

/**
 *
 * @author Cesar
 */
public class MolecularDescriptorReader implements Serializable {

    private boolean ka;

    private boolean total;

    private boolean aminoLevelON;

    private int windowSize;
    
    private WeightConfiguration cutConfig;
    
    private List<String> classicInvariants;
    
    private List<String> noClassicInvariants;
    
    private List<LocalType[]> locals;
    
    private List<AminoAcidProperty> aminoAcidProperties;
    
    private final HashMap<InvariantType, Object[]> invParameters;

    public MolecularDescriptorReader() {
        locals = new ArrayList<>();
        noClassicInvariants = new ArrayList<>();
        invParameters = new HashMap<>();
        classicInvariants = new ArrayList<>();
    }

    public boolean isTotal() {
        return total;
    }

    public boolean isKA() {
        return ka;
    }

    public WeightConfiguration getCutConfig() {
        return cutConfig;
    }

    public List<LocalType[]> getLocals() {
        return locals;
    }

    public List<AminoAcidProperty> getAminoaAcidProperties() {
        return aminoAcidProperties;
    }

    public List<String> getInvariants() {
        ArrayList<String> invs = new ArrayList<>();
        invs.addAll(classicInvariants);
        invs.addAll(noClassicInvariants);

        return invs;
    }

    public List<String> getNoClassicInvariants() {
        return noClassicInvariants;
    }

    public List<String> getClassicInvariants() {
        return classicInvariants;
    }

   public HashMap<InvariantType, Object[]> getInvParameters() {
        return invParameters;
    }

    public void readProjectFile(File project) throws IOException, ParsingException {
        locals = new ArrayList<>();
        aminoAcidProperties = new ArrayList<>();
        noClassicInvariants = new ArrayList<>();
        classicInvariants = new ArrayList<>();

        Builder parser = new Builder();
        Document doc = parser.build(project);
        Element root = doc.getRootElement();

        setCutoffs(root);
        setLocalGroups(root);
        setProperties(root);
        setInvariants(root);
    }

    public Object[] getNewParameters() {
        Object[] result = new Object[6];
        result[0] = ka;
        result[1] = false;//is show report
        result[2] = null; // here must be the output directory
        result[3] = aminoLevelON;
        result[4] = windowSize;
        return result;
    }

    private void setCutoffs(Element root) 
    {
        Element e = (Element) root.getFirstChildElement("cut_off");

        int n = e.getChildCount();

        for (int i = 0; i < n; i++) 
        {
            if (e.getChild(i) instanceof Element) {
                Element ec = (Element) e.getChild(i);

                String name = ec.getLocalName();

                if (cutConfig == null && (name.startsWith("lag"))) 
                {
                    cutConfig = new WeightConfiguration();
                }

                switch (name) {
                    case "all":
                        ka = true;
                        break;

                    case "lagst":
                        String func = ec.getAttributeValue("function");

                        List<WeightValue> attValue = getCutoffValueList(false, ec.getAttributeValue("st"));

                        switch (func) {
                            case "SHIFTING TYPE 1":
                                cutConfig.setTruncationValues(WeightType.SHIFTING1, attValue);
                                break;
                            case "SWITCHING":
                                cutConfig.setTruncationValues(WeightType.SWITCHING, attValue);
                                break;
                            case "S-SHAPED":
                                cutConfig.setTruncationValues(WeightType.S_SHAPED, attValue);
                                break;
                            case "Z-SHAPED":
                                cutConfig.setTruncationValues(WeightType.Z_SHAPED, attValue);
                                break;
                            case "PI-SHAPED":
                                cutConfig.setTruncationValues(WeightType.PI_SHAPED, attValue);
                                break;
                            case "ASCENDING GAUSSIAN":
                                cutConfig.setTruncationValues(WeightType.ASCENDING_GAUSSIAN, attValue);
                                break;
                        }

                        String ref = ec.getAttributeValue("ref");
                        if (ref.equals("N-TER")) {
                            cutConfig.setTruncationReferenceType(ReferenceType.N_TERMINAL);
                        }

                        if (ref.equals("MIDDLE")) {
                            cutConfig.setTruncationReferenceType(ReferenceType.MIDDLE);
                        }

                        if (ref.equals("C-TER")) {
                            cutConfig.setTruncationReferenceType(ReferenceType.C_TERMINAL);
                        }

                        break;
                }
            }
        }

        if (cutConfig != null) {
            cutConfig.setKa(true);
        }
    }

    private List<WeightValue> getCutoffValueList(boolean equalStrict, String lagValue) {
        List<WeightValue> list = new ArrayList<>();

        getCutoffValueList(equalStrict, lagValue, list);

        return list;
    }

    private void getCutoffValueList(boolean equalStrict, String lagValue, List<WeightValue> list) {
        if (lagValue.isEmpty()) {
            //TODO poner que devuelva 1 o 0 para saber si se puedo poner el k value o no.
        } else {
            if (lagValue.contains(";")) // If have more than one value
            {
                for (String tag : lagValue.split(";")) {
                    getCutoffValueList(equalStrict, tag, list);
                }
            } else {
                if (lagValue.contains("-")) // If have an interval
                {
                    String[] tags = lagValue.split("-");

                    list.add(new WeightValueInterval(Float.parseFloat(tags[0]), tags[0], Float.parseFloat(tags[1]), tags[1]));

                }
            }
        }
    }

    private void setLocalGroups(Element root) {
        Element e = (Element) root.getFirstChildElement("groups");

        int n = e.getChildCount();

        for (int i = 0; i < n; i++) {
            if (e.getChild(i) instanceof Element) {
                String name = ((Element) e.getChild(i)).getLocalName();

                if (name.equals("total")) {
                    total = true;
                    locals.add(new LocalType[]{LocalType.valueOf(name)});
                } else if (name.equals("aaLevel")) {

                    Element el = (Element) e.getChild(i);
                    String status = el.getAttributeValue("status");

                    if (status.equalsIgnoreCase("ON")) {
                        windowSize = Integer.parseInt(el.getAttribute("windowSize").getValue());

                        aminoLevelON = true;
                    } else {
                        aminoLevelON = false;
                    }
                } else {
                    
                    String[] locaux = name.split("-");
                    LocalType[] loc = new LocalType[locaux.length];

                    for (int j = 0; j < loc.length; j++) 
                    {
                       loc[j] = LocalType.valueOf(locaux[j]);
                    }
                    
                    locals.add(loc);
                }
            }
        }
    }

    private void setProperties(Element root) 
    {
        Element e = (Element) root.getFirstChildElement("aminoacid_properties");

        int n = e.getChildCount();

        for (int i = 0; i < n; i++) 
        {
            if (e.getChild(i) instanceof Element) 
            {
                aminoAcidProperties.add(AminoAcidProperty.valueOf(((Element) e.getChild(i)).getLocalName()));
            }
        }
    }

    private void setInvariants(Element root) {
        HashMap<String, HashMap<OWAWA.PARAMETER_NAMES, Object>> list_owawas = new HashMap<>();
        HashMap<String, HashMap<Choquet.PARAMETER_NAMES, Object>> list_choquets = new HashMap<>();

        Element e = (Element) root.getFirstChildElement("invariants");

        int n = e.getChildCount();
        for (int i = 0; i < n; i++) {
            if (e.getChild(i) instanceof Element) {
                Element ec = (Element) e.getChild(i);
                setInvariants(ec.getLocalName(), ec.getAttributeCount() == 1 ? ec.getAttributeValue("value").split("//") : null, list_owawas, list_choquets);
            }
        }

        if (!list_owawas.isEmpty()) {
            noClassicInvariants.add("gowawa");
            invParameters.put(InvariantType.GOWAWA, new Object[]{list_owawas});
        }

        if (!list_choquets.isEmpty()) {
            noClassicInvariants.add("choquet");
            invParameters.put(InvariantType.CHOQUET, new Object[]{list_choquets});
        }
    }

    private void setInvariants(String elem, String[] attr, HashMap<String, HashMap<OWAWA.PARAMETER_NAMES, Object>> list_owawas,
            HashMap<String, HashMap<Choquet.PARAMETER_NAMES, Object>> list_choquets) {
        try {
            switch (elem) {
                case "lai":
                case "ac":
                case "gv":
                case "ts":
                case "mic":
                case "tic":
                case "sic":
                case "micn":
                case "ticn":
                case "sicn":
                case "h":
                case "es":
                case "ib":
                case "kh":
                case "gc":
                case "pcd":
                case "mse":
                case "rdf":
                case "cei":
                case "is":
                case "apm":
                case "bft": {
                    classicInvariants.add(elem);
                    setClassicalInvParameters(elem);
                }
                break;
                case "gowawa": {
                    attr = readerOwawaHeader(attr);

                    HashMap<OWAWA.PARAMETER_NAMES, Object> parameters = new HashMap<>();
                    parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWAWA, Float.parseFloat(attr[0]));
                    parameters.put(OWAWA.PARAMETER_NAMES.LAMBDA_OWA, Integer.parseInt(attr[1]));
                    parameters.put(OWAWA.PARAMETER_NAMES.METHOD_OWA, attr[2] = getOWAWAInvariantName(attr[2]));
                    parameters.put(OWAWA.PARAMETER_NAMES.ALFA_OWA, Float.parseFloat(attr[3]));
                    parameters.put(OWAWA.PARAMETER_NAMES.BETA_OWA, Float.parseFloat(attr[4]));
                    parameters.put(OWAWA.PARAMETER_NAMES.DELTA_WA, Integer.parseInt(attr[5]));
                    parameters.put(OWAWA.PARAMETER_NAMES.METHOD_WA, attr[6] = getOWAWAInvariantName(attr[6]));
                    parameters.put(OWAWA.PARAMETER_NAMES.ALFA_WA, Float.parseFloat(attr[7]));
                    parameters.put(OWAWA.PARAMETER_NAMES.BETA_WA, Float.parseFloat(attr[8]));

                    list_owawas.put(attr[0] + "//" + attr[1] + "//" + attr[2] + "//" + attr[3] + "//" + attr[4] + "//" + attr[5] + "//"
                            + attr[6] + "//" + attr[7] + "//" + attr[8], parameters);
                }
                break;
                case "choquet": {
                    HashMap<Choquet.PARAMETER_NAMES, Object> parameters = new HashMap<>();
                    parameters.put(Choquet.PARAMETER_NAMES.ORDER, attr[0].equals("A") ? "ASCENDING" : "DESCENDING");
                    parameters.put(Choquet.PARAMETER_NAMES.L_VALUE, Float.parseFloat(attr[1]));
                    parameters.put(Choquet.PARAMETER_NAMES.SINGLETON_METHOD, attr[2] = getChoquetInvariantName(attr[2]));
                    parameters.put(Choquet.PARAMETER_NAMES.ALFA_SINGLETON_METHOD, Float.parseFloat(attr[3]));

                    list_choquets.put(attr[0] + "//" + attr[1] + "//" + attr[2] + "//" + attr[3], parameters);
                }
                break;
                default: {
                    noClassicInvariants.add(elem);
                }
                break;
            }
        } catch (NumberFormatException ex) {

        }
    }

    private String[] readerOwawaHeader(String[] values) {
        if (values.length < 9) {
            String[] attr = new String[9];

            attr[0] = values[0]; // OWAWA Beta

            for (int i = 1, j = 1; i < 9 && j < values.length;) {
                if (values[j].equalsIgnoreCase("none")) {
                    attr[i++] = "0";
                    attr[i++] = values[j++];
                    attr[i++] = "0.0";
                    attr[i++] = "0.0";
                } else {
                    attr[i++] = values[j++]; // lambda GOWA or delta WGM
                    attr[i++] = values[j++]; // method name
                    attr[i++] = values[j++]; // alfa value

                    if (j < values.length && isFloat(values[j])) {
                        attr[i++] = values[j++]; // beta value                     
                    } else {
                        attr[i++] = "0.0"; // beta value
                    }
                }
            }

            return attr;
        }

        return values;
    }

    private String getOWAWAInvariantName(String name) {
        switch (name) {
            case "S-OWA":
                return "S_OWA";
            case "W-OWA":
                return "WINDOW_OWA";
            case "AO1-OWA":
                return "AGGREGATED_OBJECTS_1";
            case "AO2-OWA":
                return "AGGREGATED_OBJECTS_2";
            case "ES1-OWA":
                return "EXPONENTIAL_SMOOTHING_1";
            case "ES2-OWA":
                return "EXPONENTIAL_SMOOTHING_2";
        }

        return name;
    }

    private String getChoquetInvariantName(String name) {
        switch (name) {
            case "AO1":
                return "AGGREGATED_OBJECTS_1";
            case "AO2":
                return "AGGREGATED_OBJECTS_2";
        }

        return name;
    }

    private void setClassicalInvParameters(String inv) 
    {
        Object[] params = new Object[]{DimensionType.TOPOLOGICAL, null};
        
        switch (inv) {

            case "ac":
                invParameters.put(InvariantType.AUTOCORRELATION, params);
                break;
            case "gv":
                invParameters.put(InvariantType.GRAVITATIONAL, params);
                break;
            case "ts":
                invParameters.put(InvariantType.TOTAL_SUM, params);
                break;
            case "kh":
                invParameters.put(InvariantType.KIER_HALL, params);
                break;
            case "es":
                invParameters.put(InvariantType.ELECTROTOPOLOGICAL_STATE, params);
                break;
            case "ib":
                invParameters.put(InvariantType.IVANCIUC_BALABAN, params);
                break;
            case "gc":
                invParameters.put(InvariantType.GEARY, params);
                break;
            case "pcd":
                invParameters.put(InvariantType.POTENTIAL_CHARGE_DISTRIBUTION, params);
                break;
            case "cei":
                invParameters.put(InvariantType.CONNECTIVE_ECCENTRICITY_INDEX, params);
                break;
            case "rdf":
                invParameters.put(InvariantType.RADIAL_DISTRIBUTION_FUNCTION, params);
                break;
            case "is":
                invParameters.put(InvariantType.INTERACTION_SPECTRUM, params);
                break;
            case "mse":
                invParameters.put(InvariantType.MORSE, params);
                break;
            case "apm":
                invParameters.put(InvariantType.AMPHIPHILIC_MOMENTS, params);
                break;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return str.contains(".") || str.contains(",");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static List<IDescriptor> getAlgebraicDescriptorListFromList(File file) {
        List<String> lines = new LinkedList<>();

        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(MolecularDescriptorReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        MolecularDescriptorHeading dh = new MolecularDescriptorHeading();

        return dh.getAlgebraicDescriptorList(lines);
    }

    public static List<IDescriptor> getAlgebraicDescriptorList(File file) {
        MolecularDescriptorReader adr = new MolecularDescriptorReader();

        List<IDescriptor> descriptorList = new LinkedList<>();

        try {
            adr.readProjectFile(file);

            descriptorList.addAll(MolecularDescriptorFactory.getAlgebraicDescriptorsFromReader(adr));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return descriptorList;
    }
}
