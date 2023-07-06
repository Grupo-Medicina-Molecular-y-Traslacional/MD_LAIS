package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
   PRO, ILE, ALA, VAL, LEU, PHE, TRP, MET. 
 */
public class Apolar implements ILocal
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
       String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("PRO")||resName.equalsIgnoreCase("ILE")||
                resName.equalsIgnoreCase("ALA")||resName.equalsIgnoreCase("VAL")||
                resName.equalsIgnoreCase("LEU")||resName.equalsIgnoreCase("PHE")||
                resName.equalsIgnoreCase("TRP")||resName.equalsIgnoreCase("MET");
    }    
}
