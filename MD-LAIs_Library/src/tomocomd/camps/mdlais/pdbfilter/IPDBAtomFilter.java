package tomocomd.camps.mdlais.pdbfilter;

import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 */
public interface IPDBAtomFilter 
{
    boolean isAtom(PDBAtom atom);
}
