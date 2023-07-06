package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;

/**
 *
 * @author econtreras
 */
public interface ILocal 
{
   boolean belongsToFragment(IAtom atom);   
}
