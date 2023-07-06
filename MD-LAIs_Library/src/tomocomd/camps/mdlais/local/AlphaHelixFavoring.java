package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
   ALA, CYS, LEU, MET, GLU, GLN, HIS, LYS.
 */
public class AlphaHelixFavoring implements ILocal 
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("ALA")||resName.equalsIgnoreCase("CYS")||
                resName.equalsIgnoreCase("LEU")||resName.equalsIgnoreCase("MET")||
                resName.equalsIgnoreCase("GLU")||resName.equalsIgnoreCase("GLN")||
                resName.equalsIgnoreCase("HIS")||resName.equalsIgnoreCase("LYS");
    }    
}
