package tomocomd.camps.mdlais.pdbfilter;

import javax.vecmath.Point3d;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.protein.data.PDBMonomer;

/**
 *
 * @author econtreras
 */
public class AverageRAtomsFilter extends AverageAtomsFilter {

    @Override
    public boolean isAtom(PDBAtom atom) 
    {
        return true;
    }
    
    @Override
      public PDBAtom getMediaAtoms(PDBMonomer monomer) 
    {
        double x = 0,y = 0,z = 0;
        
        int atomCount = monomer.getAtomCount();
        
        for (IAtom atom : monomer.atoms()) 
        {
            PDBAtom currentAtom = (PDBAtom)atom;
            
           String atm = currentAtom.getName();
            
            double w = atm.equals("CA")||atm.equals("N")||atm.equals("O")||atm.equals("C")?0:1;
            
            double weight = Math.pow(Math.E,w);
            
            x+=currentAtom.getPoint3d().x/weight;
            
            y+=currentAtom.getPoint3d().y/weight;
            
            z+=currentAtom.getPoint3d().z/weight;                 
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
