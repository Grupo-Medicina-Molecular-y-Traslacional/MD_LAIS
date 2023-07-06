package tomocomd.camps.mdlais.pdbfilter;

import javax.vecmath.Point3d;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.protein.data.PDBMonomer;

/**
 *
 * @author econtreras
 */
public class AverageAtomsFilter implements IPDBAtomFilter {

    @Override
    public boolean isAtom(PDBAtom atom) 
    {
        return true;
    }
    
      public PDBAtom getMediaAtoms(PDBMonomer monomer) 
    {
        double x = 0,y = 0,z = 0;
        
        int atomCount = monomer.getAtomCount();
        
        for (IAtom atom : monomer.atoms()) 
        {
            PDBAtom currentAtom = (PDBAtom)atom;
            
            x+=currentAtom.getPoint3d().x;
            
            y+=currentAtom.getPoint3d().y;
            
            z+=currentAtom.getPoint3d().z;            
        }
        
        PDBAtom meanAtom =  new PDBAtom("C", new Point3d(x/atomCount, y/atomCount, z/atomCount));
        
        meanAtom.setName("C");
        
        meanAtom.setChainID(monomer.getChainID());
        
        meanAtom.setResName(monomer.getMonomerType());
        
        meanAtom.setResSeq(monomer.getResSeq());
        
        meanAtom.setSerial(Integer.parseInt(monomer.getResSeq()));
        
        return meanAtom;
    }    
}
