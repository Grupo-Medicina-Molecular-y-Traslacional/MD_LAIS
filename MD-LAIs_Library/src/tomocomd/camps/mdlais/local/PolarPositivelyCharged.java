package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 * LYS, HIS, ARG.
 */
public class PolarPositivelyCharged implements ILocal
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("LYS")||resName.equalsIgnoreCase("HIS")||
                resName.equalsIgnoreCase("ARG");
    }    
}
