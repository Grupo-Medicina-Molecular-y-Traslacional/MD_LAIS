package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;

/**
 *
 * @author econtreras
    
 */
public class Total implements ILocal
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
       return  true;
    }    
}
