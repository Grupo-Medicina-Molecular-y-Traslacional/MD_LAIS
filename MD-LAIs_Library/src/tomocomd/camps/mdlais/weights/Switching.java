package tomocomd.camps.mdlais.weights;

import java.util.List;

/**
 *
 * @author econtreras
 */
public class Switching extends FuzzyWeight 
{
    public Switching(WeightType functionTruncationType, List<WeightValue> valuesFunction) 
    {
        super(functionTruncationType, valuesFunction);
    }

    @Override
    public double computeMembershipValue(double x) 
    {
        if (x <= a) 
        {
            return 1;
        } else if (x > a && x < b) 
        {
            double squaredron = a * a;
            double squaredroff = b * b;
            double squaredX = x * x;

            return ((squaredroff - squaredX) * (squaredroff + 2 * squaredX - 3 * squaredron))
                    / Math.pow((squaredroff - squaredron),3);
        } else 
        {
            return 0;
        }
    }

    @Override
    public void updateParamsFromRadius(double radius) 
    {
        setA(radius);
        setB(radius);
    }    

    @Override
    public String getFunctionSymbol() {
        return "SWITCHING";
    }
}
