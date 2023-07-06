/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.descriptors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.exceptions.TomocomdException;
import tomocomd.camps.mdlais.local.LocalType;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.weights.WeightType;
import tomocomd.camps.mdlais.weights.WeightValue;
import tomocomd.camps.mdlais.weights.WeightValueInterval;
import tomocomd.camps.mdlais.weights.ReferenceType;
import tomocomd.camps.mdlais.properties.AminoAcidProperty;
import tomocomd.camps.mdlais.tools.invariants.InvariantType;

/**
 *
 * @author cesar
 */
public class MolecularDescriptorHeading {

    static LinkedHashMap<String, InvariantType> operatorType;

    static LinkedHashMap<String, LocalType> localTypes;

    static LinkedHashMap<String, AminoAcidProperty> propertyType;

    static LinkedHashMap<String, WeightType> weightTypes;

    static {
        operatorType = new LinkedHashMap<>();
        operatorType.put("N1", InvariantType.MINKOWSKI_P1);
        operatorType.put("N2", InvariantType.MINKOWSKI_P2);
        operatorType.put("N3", InvariantType.MINKOWSKI_P3);

        operatorType.put("GM", InvariantType.GEOMETRIC_MEAN);
        operatorType.put("AM", InvariantType.ARITHMETIC_MEAN);
        operatorType.put("P2", InvariantType.POTENTIAL_MEAN_P2);
        operatorType.put("P3", InvariantType.POTENTIAL_MEAN_P3);
        operatorType.put("HM", InvariantType.HARMONIC_MEAN);

        operatorType.put("V", InvariantType.VARIANCE);
        operatorType.put("S", InvariantType.SKEWNESS);
        operatorType.put("K", InvariantType.KURTOSIS);
        operatorType.put("SD", InvariantType.STANDARD_DEVIATION);
        operatorType.put("VC", InvariantType.VARIATION_COEFFICIENT);
        operatorType.put("RA", InvariantType.RANGE);
        operatorType.put("Q1", InvariantType.PERCENTIL_25);
        operatorType.put("Q2", InvariantType.PERCENTIL_50);
        operatorType.put("Q3", InvariantType.PERCENTIL_75);
        operatorType.put("I50", InvariantType.I50);
        operatorType.put("MX", InvariantType.MAX);
        operatorType.put("MN", InvariantType.MIN);

        operatorType.put("AC", InvariantType.AUTOCORRELATION);
        operatorType.put("GV", InvariantType.GRAVITATIONAL);
        operatorType.put("MIC", InvariantType.MEAN_INFORMATION);
        operatorType.put("MICN", InvariantType.MEAN_INFORMATION);
        operatorType.put("SIC", InvariantType.STANDARIZED_INFORMATION);
        operatorType.put("SICN", InvariantType.STANDARIZED_INFORMATION);
        operatorType.put("TIC", InvariantType.TOTAL_INFORMATION);
        operatorType.put("TICN", InvariantType.TOTAL_INFORMATION);
        operatorType.put("TS", InvariantType.TOTAL_SUM);
        operatorType.put("IB", InvariantType.IVANCIUC_BALABAN);
        operatorType.put("ES", InvariantType.ELECTROTOPOLOGICAL_STATE);
        operatorType.put("KH", InvariantType.KIER_HALL);
        operatorType.put("GC", InvariantType.GEARY);
        operatorType.put("PCD", InvariantType.POTENTIAL_CHARGE_DISTRIBUTION);
        operatorType.put("CEI", InvariantType.CONNECTIVE_ECCENTRICITY_INDEX);
        operatorType.put("RDF", InvariantType.RADIAL_DISTRIBUTION_FUNCTION);
        operatorType.put("MSE", InvariantType.MORSE);
        operatorType.put("IS", InvariantType.INTERACTION_SPECTRUM);
        operatorType.put("APM", InvariantType.AMPHIPHILIC_MOMENTS);
        operatorType.put("H", InvariantType.ENTROPY);
        operatorType.put("BFT", InvariantType.BFT);

        localTypes = new LinkedHashMap<>();

        localTypes.put("T", LocalType.total);
        localTypes.put("RAP", LocalType.apolar);
        localTypes.put("RPC", LocalType.polar_positively_charged);
        localTypes.put("RNC", LocalType.polar_negatively_charged);
        localTypes.put("RPU", LocalType.polar_uncharged);
        localTypes.put("ARO", LocalType.aromatic);
        localTypes.put("ALG", LocalType.aliphatic);
        localTypes.put("FAH", LocalType.alpha_helix_favoring);
        localTypes.put("FBS", LocalType.beta_sheet_favoring);
        localTypes.put("AFT", LocalType.beta_turn_favoring);
        localTypes.put("UFG", LocalType.unfolding);
        localTypes.put("ALA", LocalType.alanine);
        localTypes.put("ARG", LocalType.arginine);
        localTypes.put("ASN", LocalType.asparagine);
        localTypes.put("ASP", LocalType.aspartate);
        localTypes.put("CYS", LocalType.cysteine);
        localTypes.put("GLU", LocalType.glutamate);
        localTypes.put("GLN", LocalType.glutamine);
        localTypes.put("GLY", LocalType.glycine);
        localTypes.put("HIS", LocalType.histidine);
        localTypes.put("ILE", LocalType.isoleucine);
        localTypes.put("LEU", LocalType.leucine);
        localTypes.put("LYS", LocalType.lysine);
        localTypes.put("MET", LocalType.methionine);
        localTypes.put("PHE", LocalType.phenylalanine);
        localTypes.put("PRO", LocalType.proline);
        localTypes.put("SER", LocalType.serine);
        localTypes.put("THR", LocalType.threonine);
        localTypes.put("TRP", LocalType.tryptophan);
        localTypes.put("TYR", LocalType.tyrosine);
        localTypes.put("VAL", LocalType.valine);

        propertyType = new LinkedHashMap<>();
        propertyType.put("MM", AminoAcidProperty.side_chain_mass);
        propertyType.put("MV", AminoAcidProperty.side_chain_volume);
        propertyType.put("Z1", AminoAcidProperty.z1);
        propertyType.put("Z2", AminoAcidProperty.z2);
        propertyType.put("Z3", AminoAcidProperty.z3);
        propertyType.put("ECI", AminoAcidProperty.eci);
        propertyType.put("ISA", AminoAcidProperty.isa);
        propertyType.put("HWS", AminoAcidProperty.hws);
        propertyType.put("KDS", AminoAcidProperty.kds);
        propertyType.put("PIE", AminoAcidProperty.pie);
        propertyType.put("EPS", AminoAcidProperty.eps);
        propertyType.put("PAH", AminoAcidProperty.pah);
        propertyType.put("PBS", AminoAcidProperty.pbs);
        propertyType.put("PTT", AminoAcidProperty.ptt);
        propertyType.put("GCP1", AminoAcidProperty.gcp1);
        propertyType.put("GCP2", AminoAcidProperty.gcp2);
        propertyType.put("U", AminoAcidProperty.unit);
        propertyType.put("VHSE1", AminoAcidProperty.vhse1);
        propertyType.put("VHSE4", AminoAcidProperty.vhse4);
        propertyType.put("VHSE6", AminoAcidProperty.vhse6);
        propertyType.put("VHSE7", AminoAcidProperty.vhse7);
        propertyType.put("VHSE8", AminoAcidProperty.vhse8);
        propertyType.put("T2", AminoAcidProperty.t2);
        propertyType.put("T3", AminoAcidProperty.t3);
        propertyType.put("T4", AminoAcidProperty.t4);
        propertyType.put("MDL1", AminoAcidProperty.mdl1);
        propertyType.put("MDL2", AminoAcidProperty.mdl2);
        propertyType.put("MDL3", AminoAcidProperty.mdl3);
        propertyType.put("MDL4", AminoAcidProperty.mdl4);
        propertyType.put("MDL5", AminoAcidProperty.mdl5);
        propertyType.put("MDL6", AminoAcidProperty.mdl6);
        propertyType.put("MID1", AminoAcidProperty.mid1);
        propertyType.put("MID2", AminoAcidProperty.mid2);
        propertyType.put("MID3", AminoAcidProperty.mid3);
        propertyType.put("MID4", AminoAcidProperty.mid4);
        propertyType.put("MID5", AminoAcidProperty.mid5);
        propertyType.put("MID6", AminoAcidProperty.mid6);
        propertyType.put("MID7", AminoAcidProperty.mid7);
        propertyType.put("MID8", AminoAcidProperty.mid8);
        propertyType.put("MID9", AminoAcidProperty.mid9);

        weightTypes = new LinkedHashMap<>();

        weightTypes.put("Z", WeightType.Z_SHAPED);
        weightTypes.put("SHIFTING1", WeightType.SHIFTING1);
        weightTypes.put("PI", WeightType.PI_SHAPED);
        weightTypes.put("SWITCHING", WeightType.SWITCHING);
        weightTypes.put("AGAUSSIAN", WeightType.ASCENDING_GAUSSIAN);
        weightTypes.put("S", WeightType.S_SHAPED);
    }

    private List<TomocomdException> exceptionList;

    public MolecularDescriptorHeading() {
        exceptionList = new LinkedList<>();
    }

    public List<TomocomdException> getExceptionList() {
        return exceptionList;
    }

    static WeightConfiguration getCutoffConfiguration(String tag) {
        WeightConfiguration cutConfig;

        if (!tag.startsWith("UW") && !tag.startsWith("LGP") && !tag.startsWith("LGSTN") && !tag.startsWith("LGSTM") && !tag.startsWith("LGSTC")) {
            return null;
        } else {
            cutConfig = new WeightConfiguration();

            if (tag.startsWith("UW")) {
                cutConfig.setKa(true);
            } else {

                int start = tag.indexOf("[") + 1;

                int end = tag.indexOf("]");

                String interval = tag.substring(start, end);

                if (tag.startsWith("LGSTN") || tag.startsWith("LGSTM") || tag.startsWith("LGSTC")) {
                    List<WeightValue> truncationValues = getCutoffValueList(false, interval);

                    String fmf = tag.substring(tag.indexOf("(") + 1, tag.indexOf(")"));

                    WeightType cutType = weightTypes.get(fmf);

                    cutConfig.setTruncationValues(cutType, truncationValues);

                    if (tag.startsWith("LGSTN")) {
                        cutConfig.setTruncationReferenceType(ReferenceType.N_TERMINAL);
                    } else if (tag.startsWith("LGSTM")) {
                        cutConfig.setTruncationReferenceType(ReferenceType.MIDDLE);
                    } else if (tag.startsWith("LGSTC")) {
                        cutConfig.setTruncationReferenceType(ReferenceType.C_TERMINAL);
                    }
                }
            }
        }

        return cutConfig;
    }

    static WeightConfiguration getCutoffConfiguration(String[] tagArray) {
        WeightConfiguration cutConfig = new WeightConfiguration();

        String lgst = tagArray[1];

        int start = lgst.indexOf("[") + 1;
        int end = lgst.indexOf("]");
        String interval = lgst.substring(start, end);

        List<WeightValue> truncationValues = getCutoffValueList(false, interval);

        String fmf = lgst.substring(lgst.indexOf("(") + 1, lgst.indexOf(")"));

        WeightType cutType = weightTypes.get(fmf);

        cutConfig.setTruncationValues(cutType, truncationValues);

        if (lgst.startsWith("LGSTN")) {
            cutConfig.setTruncationReferenceType(ReferenceType.N_TERMINAL);
        } else if (lgst.startsWith("LGSTM")) {
            cutConfig.setTruncationReferenceType(ReferenceType.MIDDLE);
        } else if (lgst.startsWith("LGSTC")) {
            cutConfig.setTruncationReferenceType(ReferenceType.C_TERMINAL);
        }

        return cutConfig;
    }

    static public MolecularDescriptor decode(String descriptorHeading) {
        try {
            String tags[] = descriptorHeading.split("_");

            int len = tags.length;

            if (len < 3 || len > 5) {
                return null;
            }

            String classicInv = "";
            String noClassicInv = "";

            int cutIndex = -1;
            int locIndex = -1;

            switch (len) {
                //N1_T_KA_Z3_MDLAIs
                case 3:
                    classicInv = "lai";
                    locIndex = 0;
                    cutIndex = 1;
                    break;                    
                case 4:
                    classicInv = "lai";
                    noClassicInv = tags[0];
                    locIndex = 1;
                    cutIndex = 2;                    
                    break;
                case 5:
                    classicInv = tags[0];
                    noClassicInv = tags[1];                        
                    locIndex = 2;
                    cutIndex = 3;
                    break;
                default:
                    return null;
            }
            
            if (noClassicInv.equals("SIC") || noClassicInv.equals("SICN")
               || noClassicInv.equals("TIC") || noClassicInv.equals("TICN")) 
            {
                        classicInv = noClassicInv;
            }

            //setting values
            WeightConfiguration cutConfig = getCutoffConfiguration(tags[cutIndex]);

            String propertyCombination = tags[len-1];

            String[] localTag = tags[locIndex].split("-");

            LocalType[] localType = new LocalType[localTag.length];

            for (int i = 0; i < localTag.length; i++) {
                LocalType loc = localTypes.get(localTag[i]);

                if (loc != null) {
                    localType[i] = loc;
                } else {
                    localType = null;

                    break;
                }
            }

            if(len>3)
            {
            if (!classicInv.equalsIgnoreCase("lai") && !classicInv.contains("[") && operatorType.get(classicInv) == null) {
                return null;
            }
            if (classicInv.isEmpty() || noClassicInv.isEmpty()
                    || localType == null || propertyCombination == null) {
                return null;
            }
            }
            if (classicInv.contains("[") && operatorType.get(classicInv.substring(0, classicInv.indexOf("["))) == null) {
                return null;
            }

            if (descriptorHeading.contains("_LGSTN") || descriptorHeading.startsWith("_LGSTM") || descriptorHeading.startsWith("_LGSTC")) {
                if (cutConfig == null) {
                    return null;
                }
            }

            MolecularDescriptor algebraicDescriptor = MolecularDescriptorFactory.newDescriptor(classicInv, noClassicInv, propertyCombination, localType, cutConfig);

            algebraicDescriptor.setParameters(new Object[]{cutConfig == null, false, "", false,null,true});

            return algebraicDescriptor;

        } catch (Exception e) {
            return null;
        }
    }

    public List<IDescriptor> getAlgebraicDescriptorList(List<String> heading) {
        List<IDescriptor> descriptorList = new LinkedList<>();

        for (String string : heading) {
            string = string.trim();
            if (!string.isEmpty()) {

                IDescriptor descriptor = decode(string.trim());

                if (descriptor != null) {
                    descriptorList.add(descriptor);
                } else {
                    exceptionList.add(new TomocomdException("Descriptor " + string + " was not built cause: heading not recognized"));
                }
            }
        }

        return descriptorList;
    }

    public List<IDescriptor> getAlgebraicDescriptorListFromList(File file) {
        List<String> lines = new LinkedList<>();

        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(MolecularDescriptorReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return getAlgebraicDescriptorList(lines);
    }

    static public List<WeightValue> getCutoffValueList(String lagValue) {
        return getCutoffValueList(false, lagValue);
    }

    static public List<WeightValue> getCutoffValueList(boolean equalStric, String lagValue) 
    {
        List<WeightValue> list = new ArrayList<>();

        getCutoffValueList(equalStric, lagValue, list);

        return list;
    }

    static private void getCutoffValueList(boolean equalStric, String lagValue, List<WeightValue> list) 
    {
        if (!lagValue.isEmpty()) 
        {
            if (lagValue.contains("-")) // If have an interval
            {
                String[] tags = lagValue.split("-");

                list.add(new WeightValueInterval(Float.parseFloat(tags[0]), tags[0], Float.parseFloat(tags[1]), tags[1]));
            } else {
                list.add(new WeightValue(equalStric, Float.parseFloat(lagValue), lagValue));
            }
        }
    }

    static public int getIntervalLength(int min, int max) 
    {
        int intervalLength = 0;

        for (int i = min; i <= max; i++) {
            intervalLength++;
        }
        return intervalLength;
    }
}
