/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 * ASN, CYS, GLY, SER, THR, TYR, GLN.
 */
public class PolarUncharged implements ILocal 
{
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        String resName = ((PDBAtom)atom).getResName();
        
        return  resName.equalsIgnoreCase("ASN")||resName.equalsIgnoreCase("CYS")||
                resName.equalsIgnoreCase("GLY")||resName.equalsIgnoreCase("SER")||
                resName.equalsIgnoreCase("THR")||resName.equalsIgnoreCase("TYR")||
                resName.equalsIgnoreCase("GLN");
    }    
}
