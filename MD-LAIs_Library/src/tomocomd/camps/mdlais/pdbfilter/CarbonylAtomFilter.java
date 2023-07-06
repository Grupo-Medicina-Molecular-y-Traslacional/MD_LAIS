package tomocomd.camps.mdlais.pdbfilter;

import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 */
public class CarbonylAtomFilter implements IPDBAtomFilter
{
    @Override
    public boolean isAtom(PDBAtom atom) 
    {
        return atom.getName().equalsIgnoreCase("C");
    }   
}
