package tomocomd.camps.mdlais.pdbfilter;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class PDBDataFile 
{
    private final int modelCount;
    
    private final List<String> strands; 

    public PDBDataFile(int modelCount, List<String> strands) 
    {
        this.modelCount = modelCount;
        
        this.strands = strands;
    }

    public int getModelCount() 
    {
        return modelCount;
    }

    public List<String> getStrands() 
    {
        return strands;
    }
}
