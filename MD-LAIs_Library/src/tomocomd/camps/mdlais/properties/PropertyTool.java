package tomocomd.camps.mdlais.properties;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.protein.data.PDBAtom;
import tomocomd.camps.mdlais.protein.PDBProtein;

/**
 *
 * @author econtreras
 */
public class PropertyTool 
{
    public static String getAcronymAccordingToAminoAcidProperty(AminoAcidProperty p) 
    {
        switch (p) {
            case side_chain_mass:
                return "MM";
            case side_chain_volume:
                return "MV";
            case z1:
                return "Z1";
            case z2:
                return "Z2";
            case z3:
                return "Z3";
            case eci:
                return "ECI";
            case isa:
                return "ISA";
            case hws:
                return "HWS";
            case kds:
                return "KDS";
            case pie:
                return "PIE";
            case pah:
                return "PAH";
            case pbs:
                return "PBS";
            case ptt:
                return "PTT";
            case gcp1:
                return "GCP1";
            case gcp2:
                return "GCP2";
            case eps:
                return "EPS";
            case unit:
                return "U";
            case t2:
                return "T2";
            case t3:
                return "T3";
            case t4:
                return "T4";
            case vhse1:
                return "VHSE1";
            case vhse4:
                return "VHSE4";
            case vhse6:
                return "VHSE6";
            case vhse7:
                return "VHSE7";
            case vhse8:
                return "VHSE8";
            case mdl1:
                return "MDL1";
            case mdl2:
                return "MDL2";
            case mdl3:
                return "MDL3";
            case mdl4:
                return "MDL4";
            case mdl5:
                return "MDL5";
            case mdl6:
                return "MDL6";
            case mid1:
                return "MID1";
            case mid2:
                return "MID2";
            case mid3:
                return "MID3";
            case mid4:
                return "MID4";
            case mid5:
                return "MID5";
            case mid6:
                return "MID6";
            case mid7:
                return "MID7";
            case mid8:
                return "MID8";
            case mid9:
                return "MID9";
            default:
                break;
        }
        return "";
    }
    
    static public PropertyVector getAminoAcidPropertyVector(AminoAcidProperties aminoAcidProperties,AminoAcidProperty aminoAcidProperty, IAtomContainer container) throws Exception 
    {
        try         
        
        {
        PropertyVector propertyVector = getAminoAcidPropertyVector(aminoAcidProperties,aminoAcidProperty, (PDBProtein) container);

        return propertyVector;
        
        } catch (Exception e) 
        {
            throw e;
        }        
    }

    static public PropertyVector getAminoAcidPropertyVector(AminoAcidProperties aminoAcidProperties,AminoAcidProperty property, PDBProtein protein ) throws Exception 
    {
        int atomCount = protein.getAtomCount();

        double[] propertyArray = new double[atomCount];

        for (int i = 0; i < atomCount; i++) 
        {
            propertyArray[i] = aminoAcidProperties.getAminoAcidPropertyValue((PDBAtom) protein.getAtom(i), property);
        }

        return new PropertyVector(propertyArray);
    }
}
