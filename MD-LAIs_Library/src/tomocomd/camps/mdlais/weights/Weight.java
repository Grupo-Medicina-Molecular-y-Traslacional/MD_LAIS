/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tomocomd.camps.mdlais.weights;

import java.io.Serializable;
import org.openscience.cdk.interfaces.IMolecule;

/**
 *
 * @author Cesar
 */
abstract public class Weight implements Serializable
{
    protected IMolecule molecule;
        
    public void setMolecule( IMolecule molecule )
    {
        this.molecule = molecule;
    }
   
    abstract public double[] cutoff( );
}
