package tomocomd.camps.mdlais.descriptors.result;

import java.util.*;

/**
 *
 * @author cysva
 */
public class AlgebraicDescriptorResult extends AlgebraicDescriptorResultType 
{
    List<double[]> array;
    
    public AlgebraicDescriptorResult() 
    {
        super(0);
        array = new ArrayList<>();
    }
    
    public AlgebraicDescriptorResult( int size ) 
    {
        super(size);
        array = new ArrayList<>( size );
    }
    
    public void add( double[] value )
    {
        array.add(value);
    }
    
    public void set( int pos, double[] value )
    {
        array.set( pos, value );
    }
    
    public double[] get(int index) 
    {
        if (index >= this.array.size()) 
        {
            return new double[0];
        }
        
        return this.array.get(index);
    }
    
    @Override
    public int length() 
    {
        return Math.max(super.length(), this.array.size());
    }
    
    public void destroy()
    {
        for ( int i = 0; i < array.size(); i++ )
        {
            array.set( i, null );
        }
        
        array.clear();
    }
}
