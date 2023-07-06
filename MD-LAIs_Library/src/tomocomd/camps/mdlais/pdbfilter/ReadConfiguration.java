package tomocomd.camps.mdlais.pdbfilter;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class ReadConfiguration 
{
    private int modelIndex;
    
    private List<String> strands;
    
    private IPDBAtomFilter atomFilter;

    public ReadConfiguration(int modelIndex, List<String> strands, IPDBAtomFilter atomFilter) 
    {
        this.modelIndex = modelIndex;
        
        this.strands = strands;
        
        this.atomFilter = atomFilter;
    }

    public int getModelIndex() {
        return modelIndex;
    }

    public void setModelIndex(int modelIndex) {
        this.modelIndex = modelIndex;
    }

    public List<String> getStrands() {
        return strands;
    }

    public void setStrands(List<String> strands) {
        this.strands = strands;
    }

    public IPDBAtomFilter getAtomFilter() {
        return atomFilter;
    }

    public void setAtomFilter(IPDBAtomFilter atomFilter) {
        this.atomFilter = atomFilter;
    }
    
    public boolean isValid()
    {
        return !strands.isEmpty()&&atomFilter!=null;
    }
}
