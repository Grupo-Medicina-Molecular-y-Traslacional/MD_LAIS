package tomocomd.camps.mdlais.weights;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class Shifting1 extends FuzzyWeight {

    public Shifting1(WeightType functionTruncationType, List<WeightValue> valuesFunction) 
    {
        super(functionTruncationType, valuesFunction);
    }

   @Override
    public double computeMembershipValue(double x) 
    {
        return x <= b ? Math.pow((1 - ((x / b) * (x / b))), 2) : 0;
    }

    @Override
    public void updateParamsFromRadius(double radius) 
    {
        setB(radius);
    }   

    @Override
    public String getFunctionSymbol() {
        return "SHIFTING1";
    }
}
