package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
   GLY, SER, ASP, ASN, PRO
 */
public class BetaTurnFavoring implements ILocal
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("GLY")||resName.equalsIgnoreCase("SER")||
                resName.equalsIgnoreCase("ASP")||resName.equalsIgnoreCase("ASN")||
                resName.equalsIgnoreCase("PRO");
    }    
}
