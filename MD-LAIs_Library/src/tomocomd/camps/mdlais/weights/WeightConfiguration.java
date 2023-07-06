package tomocomd.camps.mdlais.weights;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cesar
 */
public class WeightConfiguration {

    private final List<WeightType> cutoffTypes;

    private List<WeightValue> truncationValues;
    
    private ReferenceType truncationReferenceType;
    
    private boolean ka;

    public WeightConfiguration() 
    {
        this.cutoffTypes = new ArrayList<>();
        ka = false;
    }

    public List<WeightType> getCutoffTypes() {
        return cutoffTypes;
    }

    public List<WeightValue> getTruncationValues() {
        return truncationValues;
    }

    public void setTruncationValues(WeightType type, List<WeightValue> truncationValues) {
        this.truncationValues = truncationValues;
        this.cutoffTypes.add(type);
    }

    public ReferenceType getTruncationReferenceType() {
        return truncationReferenceType;
    }

    public void setTruncationReferenceType(ReferenceType truncationReferenceType) {
        this.truncationReferenceType = truncationReferenceType;
    }

    public boolean isKa() {
        return ka;
    }

    public void setKa(boolean ka) {
        this.ka = ka;
    }
}
