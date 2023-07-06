package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
   VAL, ILE, PHE, TYR, TRP, THR.
 */
public class BetaSheetFavoring implements ILocal 
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("VAL")||resName.equalsIgnoreCase("ILE")||
                resName.equalsIgnoreCase("PHE")||resName.equalsIgnoreCase("TYR")||
                resName.equalsIgnoreCase("TRP")||resName.equalsIgnoreCase("THR"); 
    }    
}
