package tomocomd.camps.mdlais.descriptors.result;

import org.openscience.cdk.qsar.result.IDescriptorResult;

/**
 *
 * @author cysva
 */
public class AlgebraicDescriptorResultType implements IDescriptorResult
{
    int size;
    
    public AlgebraicDescriptorResultType(int size)
    {
        this.size = size;
    }
    
    @Override
    public int length() 
    {
        return size;
    }
    
    public String toString()
    {
        return "AlgebraicDescriptorResultType";
    }
}
