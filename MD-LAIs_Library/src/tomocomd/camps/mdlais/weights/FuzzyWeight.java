/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.weights;

import java.util.List;
import org.openscience.cdk.interfaces.IMolecule;

/**
 *
 * @author econtreras
 */
public abstract class FuzzyWeight extends Weight {

    protected double a, b, ron, roff;

    protected WeightType functionTruncationType;

    protected double[] membership;

    protected ReferenceType referenceType;
    
    protected List<WeightValue> values;

    public FuzzyWeight(WeightType functionTruncationType, List<WeightValue> valuesFunction) 
    {
        super();

        this.functionTruncationType = functionTruncationType;

        if (!functionTruncationType.equals(WeightType.SHIFTING1)) 
        {
            this.ron = valuesFunction.get(0).getMinimumValue();

            this.roff = ((WeightValueInterval) valuesFunction.get(0)).getMaximunValue();
        } else 
        {
            this.ron = 0;

            this.roff = valuesFunction.get(0).getMinimumValue();
        }

        this.values = valuesFunction;
    }

    @Override
    public void setMolecule(IMolecule molecule) 
    {
        super.setMolecule(molecule);

        int n = molecule.getAtomCount();

        membership = new double[n];

        double reference = 0;

        double maxDistance = 0;

        switch (referenceType) {
            case N_TERMINAL:
                reference = 0;
                maxDistance = n - 1;
                break;
            case MIDDLE:
                reference = n / 2;
                maxDistance = reference;
                break;
            case C_TERMINAL:
                reference = n - 1;
                maxDistance = reference;
                break;
        }
        
        updateParamsFromRadius(maxDistance);
        
        for (int i = 0; i < n; i++) 
        {
            double distance = Math.abs(reference - i);

            membership[i] = computeMembershipValue(distance);
        }
    }

    public void setA(double Radius) {
        this.a = ron * Radius;
    }

    public void setB(double Radius) {
        this.b = roff * Radius;
    }

    @Override
    public double[] cutoff() {
        
        return membership;
    }

    public abstract double computeMembershipValue(double x);

    public abstract void updateParamsFromRadius(double radius);

    protected double semisum(double x, double y) {
        return (x + y) / 2;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(ReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    @Override
    public String toString() 
    {
        String heading = "";

        if (values != null && !values.isEmpty()) {
            String pref = "";

            switch (referenceType) {
                case N_TERMINAL:
                    pref = "_LGSTN(";
                    break;
                case MIDDLE:
                    pref = "_LGSTM(";
                    break;
                case C_TERMINAL:
                    pref = "_LGSTC(";
                    break;
            }
            heading = pref + getFunctionSymbol() + ")[" + valuesToString() + "]" + heading;
        }

        return heading;
    }
    
    final protected String valuesToString() 
    {
        String resp = "";

        for (WeightValue value : values) 
        {
            if (!resp.isEmpty()) 
            {
                resp = resp + ";";
            }

            resp += value.toString();
        }

        return resp;
    }

    public abstract String getFunctionSymbol();

}
