package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
    PHE, TYR, TRP.
 */
public class Aromatic implements ILocal
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("PHE")||resName.equalsIgnoreCase("TYR")||
                resName.equalsIgnoreCase("TRP");
    }    
}
