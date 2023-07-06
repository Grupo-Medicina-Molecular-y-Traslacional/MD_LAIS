package tomocomd.camps.mdlais.matrices;

import org.openscience.cdk.interfaces.IMolecule;
import tomocomd.camps.mdlais.local.ILocal;
import tomocomd.camps.mdlais.local.LocalFactory;
import org.openscience.cdk.interfaces.IAtom;
import tomocomd.camps.mdlais.local.LocalType;
import tomocomd.camps.mdlais.local.KMer;

/**
 *
 * @author econtreras
 */
public class LocalOperationMatrix {

    private IMolecule molecule;

    private final ILocal local;

    public LocalOperationMatrix(LocalType[] localType, IMolecule molecule) 
    {
        this.local = LocalFactory.getLocal(localType);

        this.molecule = molecule;

       if (local instanceof KMer) 
       {
          ((KMer) local).setMolecule(molecule);
       }
    }

    public double ratio(IAtom[] atoms) 
    {
        return local.belongsToFragment(atoms[0])?1:0;
    }

    public IMolecule getMolecule() {
        return molecule;
    }

    public void setMolecule(IMolecule molecule) {
        this.molecule = molecule;
    }
}
