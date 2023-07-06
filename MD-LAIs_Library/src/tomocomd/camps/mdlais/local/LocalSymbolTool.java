package tomocomd.camps.mdlais.local;

import java.util.HashMap;

/**
 *
 * @author econtreras
 */
/*
 Alanine	ALA	A
 Arginine	ARG	R
 Asparagine	ASN	N
 Aspartate	ASP	D
 Cysteine	CYS	C
 Glutamate	GLU	E
 Glutamine	GLN	Q
 Glycine	GLY	G
 Histidine	HIS	H
 Isoleucine	ILE	I
 Leucine	LEU	L
 Lysine	LYS	K
 Methionine	MET	M
 Phenylalanine	PHE	F
 Proline	PRO	P
 Serine	SER	S
 Threonine	THR	T
 Tryptophan	TRP	W
 Tyrosine	TYR	Y
 Valine         VAL	V

 */
public class LocalSymbolTool {

    static HashMap<LocalType, String[]> symbolTable = new HashMap<>();

    public static String getThreeLetterCode(LocalType local) {
        if (symbolTable.isEmpty()) {
            fillSymbolTable();
        }

        return symbolTable.get(local)[0];
    }

    public static String getOneLetterCode(LocalType local) {
        if (symbolTable.isEmpty()) {
            fillSymbolTable();
        }

        return symbolTable.get(local)[1];
    }

    public static void fillSymbolTable() {
        // <editor-fold defaultstate="collapsed" desc="Aminoacid list">
//                Alanine         ALA	A
//Arginine	ARG	R
//Asparagine	ASN	N
//Aspartate	ASP	D
//Cysteine	CYS	C
//Glutamate	GLU	E
//Glutamine	GLN	Q
//Glycine         GLY	G
//Histidine	HIS	H
//Isoleucine	ILE	I
//Leucine         LEU	L
//Lysine          LYS	K
//Methionine	MET	M
//Phenylalanine	PHE	F
//Proline         PRO	P
//Serine          SER	S
//Threonine	THR	T
//Tryptophan	TRP	W
//Tyrosine	TYR	Y
//Valine          VAL	V

        // </editor-fold>
        symbolTable.put(LocalType.alanine, new String[]{"ALA", "A"});
        symbolTable.put(LocalType.arginine, new String[]{"ARG", "R"});
        symbolTable.put(LocalType.asparagine, new String[]{"ASN", "N"});
        symbolTable.put(LocalType.aspartate, new String[]{"ASP", "D"});
        symbolTable.put(LocalType.cysteine, new String[]{"CYS", "C"});
        symbolTable.put(LocalType.glutamate, new String[]{"GLU", "E"});
        symbolTable.put(LocalType.glutamine, new String[]{"GLN", "Q"});
        symbolTable.put(LocalType.glycine, new String[]{"GLY", "G"});
        symbolTable.put(LocalType.histidine, new String[]{"HIS", "H"});
        symbolTable.put(LocalType.isoleucine, new String[]{"ILE", "I"});
        symbolTable.put(LocalType.leucine, new String[]{"LEU", "L"});
        symbolTable.put(LocalType.lysine, new String[]{"LYS", "K"});
        symbolTable.put(LocalType.methionine, new String[]{"MET", "M"});
        symbolTable.put(LocalType.phenylalanine, new String[]{"PHE", "F"});
        symbolTable.put(LocalType.proline, new String[]{"PRO", "P"});
        symbolTable.put(LocalType.serine, new String[]{"SER", "S"});
        symbolTable.put(LocalType.threonine, new String[]{"THR", "T"});
        symbolTable.put(LocalType.tryptophan, new String[]{"TRP", "W"});
        symbolTable.put(LocalType.tyrosine, new String[]{"TYR", "Y"});
        symbolTable.put(LocalType.valine, new String[]{"VAL", "V"});
    }

    public static String getAcronymAccordingToLocal(LocalType localType) {
        if (localType != null) {
            // rgroups and secondary structures prefs groups (10)
            if (localType.equals(LocalType.apolar)) {
                return "RAP";
            } else if (localType.equals(LocalType.polar_positively_charged)) {
                return "RPC";
            } else if (localType.equals(LocalType.polar_negatively_charged)) {
                return "RNC";
            } else if (localType.equals(LocalType.polar_uncharged)) {
                return "RPU";
            } else if (localType.equals(LocalType.aromatic)) {
                return "ARO";//cambio porque arginine es ARG tambien
            } else if (localType.equals(LocalType.aliphatic)) {
                return "ALG";
            } else if (localType.equals(LocalType.unfolding)) {
                return "UFG";
            } else if (localType.equals(LocalType.alpha_helix_favoring)) {
                return "FAH";
            } else if (localType.equals(LocalType.beta_sheet_favoring)) {
                return "FBS";
            } else if (localType.equals(LocalType.beta_turn_favoring)) {
                return "AFT";

            } 
            // amino acid locals (20)
            //alanine,arginine,asparagine,aspartate,cysteine,glutamate,glutamine,glycine,histidine,isoleucine,
            //leucine,lysine,methionine,phenylalanine,proline,serine,threonine,tryptophan,tyrosine,valine
            else if (localType.equals(LocalType.alanine) || localType.equals(LocalType.arginine) || localType.equals(LocalType.arginine)
                    || localType.equals(LocalType.asparagine) || localType.equals(LocalType.aspartate) || localType.equals(LocalType.cysteine)
                    || localType.equals(LocalType.glutamate) || localType.equals(LocalType.glutamine) || localType.equals(LocalType.glycine)
                    || localType.equals(LocalType.histidine) || localType.equals(LocalType.isoleucine) || localType.equals(LocalType.leucine)
                    || localType.equals(LocalType.lysine) || localType.equals(LocalType.methionine) || localType.equals(LocalType.phenylalanine)
                    || localType.equals(LocalType.proline) || localType.equals(LocalType.serine) || localType.equals(LocalType.threonine)
                    || localType.equals(LocalType.tryptophan) || localType.equals(LocalType.tyrosine) || localType.equals(LocalType.valine)) {
                    
                return LocalSymbolTool.getThreeLetterCode(localType);
            }
        }

        return "T";
    }
    
    public static String getAcronymAccordingToLocal(LocalType[] localType) 
    {
        if (localType != null) 
        {
            String result = "";

            for (int i = 0; i < localType.length - 1; i++) 
            {
                result += getAcronymAccordingToLocal(localType[i]) + "-";
            }

            result += getAcronymAccordingToLocal(localType[localType.length - 1]);

            return result;
        }

        return "T";
    }

    public static String getLocalAccordingToAcronym(String acronym) {
        if (acronym != null) {
            // rgroups and secondary structures prefs groups (10)
            switch (acronym) {
                case "RAP":
                    return LocalType.apolar.toString();
                case "RPC":
                    return LocalType.polar_positively_charged.toString();
                case "RNC":
                    return LocalType.polar_negatively_charged.toString();
                case "RPU":
                    return LocalType.polar_uncharged.toString();
                case "ARO":
                    return LocalType.aromatic.toString();//cambio porque arginine es ARG tambien
                case "ALG":
                    return LocalType.aliphatic.toString();
                case "UFG":
                    return LocalType.unfolding.toString();
                case "FAH":
                    return LocalType.alpha_helix_favoring.toString();
                case "FBS":
                    return LocalType.beta_sheet_favoring.toString();
                case "AFT":
                    return LocalType.beta_turn_favoring.toString();

                case "ALA":
                    return LocalType.alanine.toString();
                case "ARG":
                    return LocalType.arginine.toString();
                case "ASN":
                    return LocalType.asparagine.toString();
                case "ASP":
                    return LocalType.aspartate.toString();
                case "CYS":
                    return LocalType.cysteine.toString();
                case "GLU":
                    return LocalType.glutamate.toString();
                case "GLN":
                    return LocalType.glutamine.toString();
                case "GLY":
                    return LocalType.glycine.toString();
                case "HIS":
                    return LocalType.histidine.toString();
                case "ILE":
                    return LocalType.isoleucine.toString();
                case "LEU":
                    return LocalType.leucine.toString();
                case "LYS":
                    return LocalType.lysine.toString();
                case "MET":
                    return LocalType.methionine.toString();
                case "PHE":
                    return LocalType.phenylalanine.toString();
                case "PRO":
                    return LocalType.proline.toString();
                case "SER":
                    return LocalType.serine.toString();
                case "THR":
                    return LocalType.threonine.toString();
                case "TRP":
                    return LocalType.tryptophan.toString();
                case "TYR":
                    return LocalType.tyrosine.toString();
                case "VAL":
                    return LocalType.valine.toString();
            }

        }
        return "total";
    }
    
    public static LocalType getLocalTypeAccordingToAcronym(String acronym) {
        if (acronym != null) {
            // rgroups and secondary structures prefs groups (10)
            switch (acronym) {
                 case "total":
                    return LocalType.total;
                case "RAP":
                    return LocalType.apolar;
                case "RPC":
                    return LocalType.polar_positively_charged;
                case "RNC":
                    return LocalType.polar_negatively_charged;
                case "RPU":
                    return LocalType.polar_uncharged;
                case "ARO":
                    return LocalType.aromatic;
                case "ALG":
                    return LocalType.aliphatic;
                case "UFG":
                    return LocalType.unfolding;
                case "FAH":
                    return LocalType.alpha_helix_favoring;
                case "FBS":
                    return LocalType.beta_sheet_favoring;
                case "AFT":
                    return LocalType.beta_turn_favoring;

                case "ALA":
                    return LocalType.alanine;
                case "ARG":
                    return LocalType.arginine;
                case "ASN":
                    return LocalType.asparagine;
                case "ASP":
                    return LocalType.aspartate;
                case "CYS":
                    return LocalType.cysteine;
                case "GLU":
                    return LocalType.glutamate;
                case "GLN":
                    return LocalType.glutamine;
                case "GLY":
                    return LocalType.glycine;
                case "HIS":
                    return LocalType.histidine;
                case "ILE":
                    return LocalType.isoleucine;
                case "LEU":
                    return LocalType.leucine;
                case "LYS":
                    return LocalType.lysine;
                case "MET":
                    return LocalType.methionine;
                case "PHE":
                    return LocalType.phenylalanine;
                case "PRO":
                    return LocalType.proline;
                case "SER":
                    return LocalType.serine;
                case "THR":
                    return LocalType.threonine;
                case "TRP":
                    return LocalType.tryptophan;
                case "TYR":
                    return LocalType.tyrosine;
                case "VAL":
                    return LocalType.valine;
            }

        }
        return null;
    }
}
