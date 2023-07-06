package tomocomd.camps.mdlais.protein;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.protein.data.PDBMonomer;

/**
 *
 * @author econtreras
 */
public class PDBProtein extends Molecule {

    private List<PDBMonomer> monomers;// monomer list

    private List<SSBond> ssBonds;// ssbonds list

    private List<PDBStructure> secondaryStructures;// secondary structures
    
    public PDBProtein() 
    {
        monomers = new ArrayList<>();

        ssBonds = new ArrayList<>();

        secondaryStructures = new ArrayList<>();         
    }

    public List<PDBMonomer> getMonomers() {
        return monomers;
    }

    public void setMonomers(List<PDBMonomer> monomers) {
        this.monomers = monomers;
    }

    public List<SSBond> getSsBonds() {
        return ssBonds;
    }

    public List<PDBStructure> getSecondaryStructures() {
        return secondaryStructures;
    }

        
    public int getResDiffCount() {
        List<String> aminos = new ArrayList<>();

        for (PDBMonomer string : monomers) {
            if (!aminos.contains(string.getMonomerType())) {
                aminos.add(string.getMonomerType());
            }
        }

        return aminos.size();
    }

    public int getMonomerCount() {
        return monomers.size();
    }

    public void addAtom(PDBAtom oAtom, PDBMonomer oMonomer) {
        if (!contains(oAtom)) {
            PDBMonomer monomer = getMonomer(oMonomer.getMonomerName());

            if (monomer != null) {
                PDBAtom lastAtom = (PDBAtom) monomer.getLastAtom();

                if (!hasAltLocation(oAtom, lastAtom))// no tiene posicion alternativa
                {
                    monomer.addAtom(oAtom);

                } else if (oAtom.getOccupancy() > lastAtom.getOccupancy())//  si tiene mayor occupancy
                {
                    monomer.setAtom(monomer.getAtomCount() - 1, oAtom);//reemplaza el ultimo

                }
            } else {
                oMonomer.addAtom(oAtom);

                monomers.add(oMonomer);
            }
        }
    }

    private boolean contains(PDBAtom oAtom) {
        for (PDBMonomer iMonomer : monomers) {
            if (contains(iMonomer, oAtom)) {
                return true;
            }
        }

        return false;
    }

    private boolean contains(PDBMonomer oMonomer, PDBAtom oAtom) {
        return oMonomer.contains(oAtom);
    }

    private PDBMonomer getMonomer(String monomerName) {
        for (PDBMonomer iMonomer : monomers) {
            if (iMonomer.getMonomerName().equalsIgnoreCase(monomerName)) {
                return iMonomer;
            }
        }

        return null;
    }

    public PDBMonomer getMonomer(String monomerName, String chain) {
        for (PDBMonomer monomer : monomers) {
            if (monomer.getMonomerName().equalsIgnoreCase(monomerName) && monomer.getChainID().equalsIgnoreCase(chain)) {
                return monomer;
            }
        }

        return null;
    }

    public PDBMonomer getMonomer(int pos) {
        if (pos < 0 || pos >= monomers.size()) {
            return null;
        } else {
            return monomers.get(pos);
        }
    }

    public void setSsBonds(List<SSBond> ssBonds) {
        this.ssBonds = ssBonds;
    }

    public void setSecondaryStructures(List<PDBStructure> secondaryStructures) {
        this.secondaryStructures = secondaryStructures;
    }

    public void removeMonomer(int pos) {
        monomers.remove(pos);
    }

    public int getStrandCount() {
        List<String> strands = new ArrayList<>();

        for (PDBMonomer monomer : monomers) {
            if (!strands.contains(monomer.getChainID())) {
                strands.add(monomer.getChainID());
            }
        }

        return strands.size();
    }

    public List<String> getStrandNames() {
        List<String> strands = new ArrayList<>();

        for (PDBMonomer monomer : monomers) {
            if (!strands.contains(monomer.getChainID())) {
                strands.add(monomer.getChainID());
            }
        }

        return strands;
    }

    public boolean hasAltLocation(PDBAtom pdbAtom1, PDBAtom pdbAtom2) {
        return pdbAtom1.getName().equalsIgnoreCase(pdbAtom2.getName());
    }

    @Override
    public PDBProtein clone() throws CloneNotSupportedException {
        PDBProtein protein = new PDBProtein();

        protein.setSsBonds(new ArrayList(ssBonds));

        protein.setSecondaryStructures(new ArrayList(secondaryStructures));

        protein.setMonomers(new ArrayList(monomers));

        protein.getAllAtoms();

        return protein;
    }

    public void addStructure(PDBStructure structure) {
        secondaryStructures.add(structure);
    }

    public void addSSBond(SSBond ssBond) {
        ssBonds.add(ssBond);
    }

    public String getAminoSequence() {
        String sequence = "";

        for (PDBMonomer monomer : monomers) {
            sequence += monomer.getMonomerType();
        }

        return sequence;
    }

    public void getAllAtoms() {
        int atomsNumber = 0;

        for (PDBMonomer monomer : monomers) {
            atomsNumber += monomer.getAtomCount();
        }

        IAtom[] allAtoms = new IAtom[atomsNumber];

        int i = 0;

        for (PDBMonomer monomer : monomers) 
        {
            for (IAtom atom : monomer.atoms()) 
            {
                allAtoms[i++] = atom;
            }
        }
        //System.out.println("atom count" + atomsNumber + "\n");
        setAtoms(allAtoms);
    }
    
    public Hashtable<Integer,Integer> getMonomerAtomIndexes()
    {
        Hashtable<Integer,Integer> indexes = new Hashtable<>();
        
        int aminoAcidIndex = 0;
        
        int atomIndex = 0;
            
        for (PDBMonomer monomer : monomers) 
        {
            for (IAtom atom : monomer.atoms()) 
            {
                indexes.put(atomIndex++, aminoAcidIndex);                
            }
            
            aminoAcidIndex++;
        }
        
        return indexes;
    }
}
