package tomocomd.camps.mdlais.weights;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class S extends FuzzyWeight {

    public S(WeightType functionTruncationType, List<WeightValue> valuesFunction) {
        super(functionTruncationType, valuesFunction);
    }

    @Override
    public double computeMembershipValue(double x) {
        if (x <= a) {
            return 0;
        } else if (x >= a && x <= semisum(a, b)) {
            return 2 * Math.pow((x - a) / (b - a), 2);
        } else if (x >= semisum(a, b) && x <= b) {
            return 1 - 2 * Math.pow((x - b) / (b - a), 2);
        } else {
            return 1;
        }
    }

    @Override
    public void updateParamsFromRadius(double radius) {
        setA(radius);
        setB(radius);
    }

    @Override
    public String getFunctionSymbol() {
        return "S";
    }
}
