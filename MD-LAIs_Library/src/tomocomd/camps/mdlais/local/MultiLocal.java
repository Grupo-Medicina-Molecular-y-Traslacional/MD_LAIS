/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.local;

import org.openscience.cdk.interfaces.IAtom;

/**
 *
 * @author econtreras
 */
public class MultiLocal implements ILocal
{
    protected ILocal[] locals;

    protected boolean[] membership;
    
    public MultiLocal(LocalType[] locals) 
    {
        this.locals = new AminoAcid[locals.length];

        for (int i = 0; i < locals.length; i++) 
        {
            this.locals[i] = LocalFactory.getLocal(new LocalType[]{locals[i]});
        }
    }
    
    @Override
    public boolean belongsToFragment(IAtom atom) 
    {
        for (ILocal local : locals) 
        {
            if(!local.belongsToFragment(atom))
            {
                return false;
            }
        }
        return true;
    }    
}
