package tomocomd.camps.mdlais.weights;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class AscendingGaussian extends FuzzyWeight {

    public AscendingGaussian(WeightType functionTruncationType, List<WeightValue> valuesFunction) {
        super(functionTruncationType, valuesFunction);
    }

    @Override
    public double computeMembershipValue(double x) {
        return Math.pow(Math.E, (-(x - b) * (x - b)) / (2 * a * a));
    }

    @Override
    public void updateParamsFromRadius(double radius) {
        double don = ron * radius;
        double doff = roff * radius;
        this.a = (doff - don) / 2;
        this.b = doff;
    }

    @Override
    public String getFunctionSymbol() 
    {
        return "AGAUSSIAN";
    }
}
