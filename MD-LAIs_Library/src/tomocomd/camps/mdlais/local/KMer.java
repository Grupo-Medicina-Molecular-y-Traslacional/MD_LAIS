package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;

/**
 *
 * @author econtreras
 */
public class KMer extends MultiLocal {

    private IMolecule molecule;

    public KMer(LocalType[] locals) {
        super(locals);
    }

    public void setMolecule(IMolecule molecule) {
        this.molecule = molecule;

        setMembershipVector();
    }

    @Override
    public boolean belongsToFragment(IAtom atom) {
        return membership[molecule.getAtomNumber(atom)];
    }

    public boolean belongsToFragment(IAtom[] atoms) {
        for (int i = 0; i < locals.length; i++) {
            if (!locals[i].belongsToFragment(atoms[i])) {
                return false;
            }
        }
        return true;
    }

    public void setMembershipVector() 
    {
        int n = molecule.getAtomCount();
        this.membership = new boolean[n];

        if (n == locals.length) 
        {
            IAtom[] atoms = new IAtom[locals.length];

            for (int j = 0; j < locals.length; j++) 
            {
                atoms[j] = molecule.getAtom(j);
            }

            if (belongsToFragment(atoms)) 
            {
                for (int j = 0; j < atoms.length; j++) 
                {
                    membership[j] = true;
                }
            }
        }

        if (n > locals.length) 
        {
            for (int i = 0; i < n; i++) {
                if ((i + locals.length) < n) {
                    IAtom[] atoms = new IAtom[locals.length];

                    for (int j = 0; j < locals.length; j++) {
                        atoms[j] = molecule.getAtom(i + j);
                    }
                    if (belongsToFragment(atoms)) {
                        for (int j = 0; j < atoms.length; j++) {
                            membership[i + j] = true;
                        }
                    }
                }
            }
        }
    }
}
