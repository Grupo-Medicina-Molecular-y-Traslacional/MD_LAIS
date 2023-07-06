package tomocomd.camps.mdlais.weights;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class Z extends S {

    public Z(WeightType functionTruncationType, List<WeightValue> valuesFunction) {
        super(functionTruncationType, valuesFunction);
    }

    @Override
    public double computeMembershipValue(double x) {
        if (x <= a) {
            return 1;
        } else if (x >= a && x <= semisum(a, b)) {
            return 1 - 2 * Math.pow((x - a) / (b - a), 2);

        } else if (x >= semisum(a, b) && x <= b) {
            return 2 * Math.pow((x - b) / (b - a), 2);
        } else {
            return 0;
        }
    }

    @Override
    public String getFunctionSymbol() 
    {
        return "Z";
    }
}
