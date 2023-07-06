package tomocomd.camps.mdlais.weights;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class PI extends S {

    private double c;
    private double d;

    public PI(WeightType functionTruncationType, List<WeightValue> valuesFunction) {
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
        } else if (x >= b && x <= c) {
            return 1;
        } else if (x >= c && x <= semisum(c, d)) {
            return 1 - 2 * Math.pow((x - c) / (d - c), 2);
        } else if (x >= semisum(c, d) && x <= d) {
            return 2 * Math.pow((x - d) / (d - c), 2);
        } else {
            return 0;
        }
    }

    @Override
    public void updateParamsFromRadius(double radius) {
        setA(radius);

        double doff = roff * radius;

        this.b = (a + doff) * 0.45;

        this.c = (a + doff) * 0.55;

        this.d = doff;
    }

    @Override
    public String getFunctionSymbol() {
        return "PI";
    }
}
