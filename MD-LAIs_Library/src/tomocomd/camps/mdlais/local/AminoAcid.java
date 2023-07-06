package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 */
public class AminoAcid implements ILocal
{
    private final LocalType local;

    public AminoAcid(LocalType local) 
    {
        this.local = local;
    }
    
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        switch(local)
        {
             case alanine:
            
             return resName.equalsIgnoreCase("ALA");
                 
             case arginine:
                 
             return resName.equalsIgnoreCase("ARG");
                 
             case asparagine:
                 
             return resName.equalsIgnoreCase("ASN");
                 
             case aspartate:
                 
             return resName.equalsIgnoreCase("ASP");
                 
             case cysteine:
                 
             return resName.equalsIgnoreCase("CYS"); 
                 
             case glutamate:
                 
             return resName.equalsIgnoreCase("GLU"); 
                 
             case glutamine:
                 
             return resName.equalsIgnoreCase("GLN"); 
                 
             case glycine:
                 
             return resName.equalsIgnoreCase("GLY");
                 
             case histidine:
                 
             return resName.equalsIgnoreCase("HIS");
                 
             case isoleucine:
                 
             return resName.equalsIgnoreCase("ILE");
                 
             case leucine:
                 
             return resName.equalsIgnoreCase("LEU");
                 
             case lysine:
                 
             return resName.equalsIgnoreCase("LYS");
                 
             case methionine:
             
             return resName.equalsIgnoreCase("MET");
                 
             case phenylalanine:
                 
             return resName.equalsIgnoreCase("PHE");
             
             case proline:
                 
             return resName.equalsIgnoreCase("PRO");       
                 
             case serine:    
                 
             return resName.equalsIgnoreCase("SER");
                 
             case threonine:
                 
             return resName.equalsIgnoreCase("THR");  
                 
             case tryptophan:
                 
             return resName.equalsIgnoreCase("TRP");   
                 
             case tyrosine:
                 
             return resName.equalsIgnoreCase("TYR");
                 
             case valine:
                 
             return resName.equalsIgnoreCase("VAL"); 
        }
        
        return false;
    }     
}
