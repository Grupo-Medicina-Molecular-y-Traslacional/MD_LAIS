package tomocomd.camps.mdlais.pdbfilter;

import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 */
public class CBAtomFilter implements IPDBAtomFilter {

    @Override
    public boolean isAtom(PDBAtom atom) 
    {
        String residueName = atom.getResName().substring(0, 3);

        if (!residueName.equalsIgnoreCase("GLY")) 
        {
            return atom.getName().equalsIgnoreCase("CB");
        } else
        {
            return atom.getName().equalsIgnoreCase("CA");           
        }
    }
}
