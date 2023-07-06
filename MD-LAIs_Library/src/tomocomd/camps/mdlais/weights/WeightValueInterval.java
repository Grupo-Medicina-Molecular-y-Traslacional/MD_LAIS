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
public class WeightValueInterval extends WeightValue
{
    private final float maximumValue;
    private final String sMaximumValue;
    
    public WeightValueInterval( float minimunValue, String sMinimumValue, float maximumValue, String sMaximumValue )
    {
        super( false, minimunValue, sMinimumValue );
        
        this.maximumValue = maximumValue;
        this.sMaximumValue = sMaximumValue;
    }
    
    public float getMaximunValue()
    {
        return maximumValue;
    }
    
    @Override
    public boolean isAccomplished( float value )
    {
        return value >= minimumValue && value <= maximumValue;
    }
    
    @Override
    public String toString() 
    {
        String min = super.toString();
        String max = sMaximumValue != null ? sMaximumValue : Float.toString(maximumValue );
        
        return min + "-" + max;
    }
}
