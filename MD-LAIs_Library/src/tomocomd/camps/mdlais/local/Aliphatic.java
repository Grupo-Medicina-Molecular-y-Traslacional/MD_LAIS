package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
   GLY, ALA, PRO, VAL, LEU, ILE, MET.
 */
public class Aliphatic implements ILocal
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String residueName = ((PDBAtom)atom).getResName();
        
        return  residueName.equalsIgnoreCase("GLY")||residueName.equalsIgnoreCase("ALA")||
                residueName.equalsIgnoreCase("PRO")||residueName.equalsIgnoreCase("VAL")||
                residueName.equalsIgnoreCase("LEU")||residueName.equalsIgnoreCase("ILE")||
                residueName.equalsIgnoreCase("MET");
    }     
}
