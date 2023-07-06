/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tomocomd.camps.mdlais.weights;

/**
 *
 * @author Cesar
 */
public class WeightValue 
{
    protected boolean equalStric;
    protected float minimumValue;
    protected String sMinimunValue;
    
    public WeightValue( boolean equalStric, float minimumValue, String sMinimunValue )
    {
        this.equalStric = equalStric;
        this.minimumValue = minimumValue;
        this.sMinimunValue = sMinimunValue;
    }
    
    public float getMinimumValue()
    {
        return minimumValue;
    }
    
    public boolean isAccomplished( float value )
    {
        return equalStric ? value == minimumValue : value <= minimumValue;
    }
    
    @Override
    public String toString() 
    {
        return sMinimunValue != null ? sMinimunValue : Float.toString( minimumValue );
    }
}
