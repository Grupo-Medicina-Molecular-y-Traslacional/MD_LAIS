package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 * GLY, PRO
 */
public class Unfolding implements ILocal
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("GLY")||resName.equalsIgnoreCase("PRO");
    }    
}
