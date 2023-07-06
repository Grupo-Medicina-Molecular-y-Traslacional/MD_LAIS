/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.tools.invariants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Cesar
 */
public class Choquet {

    public enum PARAMETER_NAMES {
        ALFA_SINGLETON_METHOD, SINGLETON_METHOD, L_VALUE, ORDER
    }
    
    public enum SINGLETON_METHODS {
        AGGREGATED_OBJECTS_1, AGGREGATED_OBJECTS_2
    }
    
    public enum SORTING_METHOD {
        ASCENDING, DESCENDING
    }
    
    static public double compute(double[] lovis, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String) {
        if (lovis == null || lovis.length == 0) {
            return Double.NaN;
        }
        
        int dim = lovis.length;
        
        Double[] lovis_ = new Double[lovis.length];
        for (int i = 0; i < lovis.length; i++) {
            if ((lovis_[i] = lovis[i]) < 0) {
                dim = -1;
                break;
            }
        }
        
        return compute(lovis_, dim, SINGLETON_METHODS.valueOf((String) parameters.get(PARAMETER_NAMES.SINGLETON_METHOD)), parameters, outParams2String);
    }
    
    static private double compute(Double[] lovis, int dim, SINGLETON_METHODS method, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String) {
        double value = 0;
        float L_value = (Float) parameters.get(PARAMETER_NAMES.L_VALUE);
        SORTING_METHOD sort = SORTING_METHOD.valueOf((String) parameters.get(PARAMETER_NAMES.ORDER));
        
        if (dim != -1) {
            switch (sort) {
                case DESCENDING: {
                    Arrays.sort(lovis); // the LOVIs array is sorted in ascending order to perform the calculation of the Choquet integral following the DESCENDING order specified
                }
                break;
                case ASCENDING: {
                    Arrays.sort(lovis, Collections.reverseOrder()); // the LOVIs array is sorted in descending order to perform the calculation of the Choquet integral following the ASCENDING order specified
                }
                break;
            }
        }
        
        double[] singleton = computeSingletonMeasures(method, dim, parameters, lovis, outParams2String); // singleton measures --> from minimum to maximun value
        
        if (dim != -1) {
            double[] summationLU = new double[dim]; // summation from minumum to maximum value
            double[] summationUL = new double[dim]; // summation from maximum to minumum value
            
            switch (sort) {
                case DESCENDING: {
                    for (int i = dim, j = 1; i >= 1; i--, j++) {
                        summationLU[j - 1] = (j - 1 == 0) ? singleton[j - 1] : summationLU[j - 2] + singleton[j - 1];                        
                        summationUL[i - 1] = (j - 1 == 0) ? singleton[i - 1] : summationUL[i] + singleton[i - 1];
                    }
                }
                break;
                case ASCENDING: {
                    for (int i = 1, j = dim; i <= dim; i++, j--) {
                        summationLU[j - 1] = (j == dim) ? singleton[j - 1] : singleton[j - 1] + summationLU[j];
                        summationUL[i - 1] = (j == dim) ? singleton[i - 1] : singleton[i - 1] + summationUL[i - 2];
                    }                    
                }
                break;
            }
            
            double A = 1d;
            for (int i = dim; i >= 1; i--) {
                double A_MINUS_1;
                if (sort.equals(SORTING_METHOD.DESCENDING)) {
                    A_MINUS_1 = computeFuzzyValue(dim, i - 1, singleton, summationLU, summationUL, L_value, true);
                } else {
                    A_MINUS_1 = computeFuzzyValue(dim, i - 1, singleton, summationUL, summationLU, L_value, false);
                }
                
                value += (lovis[i - 1] * (A - A_MINUS_1));
                
                A = A_MINUS_1;
            }
        }
        
        lovis = null;
        outParams2String[0] = (sort.equals(SORTING_METHOD.DESCENDING) ? "D;" : "A;") + L_value + outParams2String[0];
        return dim != -1 ? value : Double.NaN;
    }
    
    static private double computeFuzzyValue(int dim, int dimA, double[] singleton, double[] summationLU, double[] summationUL, double L, boolean desc) {
        if (dimA == 0) {
            return 0d;
        } else if (dim == dimA) {
            return 1d;
        } else if (L == -1) {
            return desc ? singleton[dimA - 1] : singleton[0];
        } else if (L > -1 && L <= 0) {
            double num = (1 + L) * summationLU[dimA - 1] * (1 + L * singleton[dimA - 1]);
            double den = 1 + L * summationLU[dimA - 1];
            
            return (num / den) - (L * singleton[dimA - 1]);
        } else if (L > 0) {
            double num = L * (dimA - 1) * summationLU[dimA - 1] * (1 - summationLU[dimA - 1]);
            double den = ((dim - dimA) * summationUL[dimA]) + (L * (dimA - 1) * summationLU[dimA - 1]);
            
            return (num / den) + summationLU[dimA - 1];
        }
        
        return Double.NaN;
    }
    
    static private double[] computeSingletonMeasures(SINGLETON_METHODS method, int dim, HashMap<PARAMETER_NAMES, Object> parameters, Double[] lovis, String[] outParams2String) {
        switch (method) {
            case AGGREGATED_OBJECTS_1:
                return AGGREGATED_OBJECTS_1_METHOD(dim, parameters, lovis, outParams2String);
            
            case AGGREGATED_OBJECTS_2:
                return AGGREGATED_OBJECTS_2_METHOD(dim, parameters, lovis, outParams2String);
        }
        
        outParams2String[0] = outParams2String[0] + ";NONE";
        return dim > 0 ? new double[dim] : new double[0];        
    }
    
    static private double[] AGGREGATED_OBJECTS_1_METHOD(int dim, HashMap<PARAMETER_NAMES, Object> parameters, Double[] lovis, String[] outParams2String) {
        float alfa = (Float) parameters.get(PARAMETER_NAMES.ALFA_SINGLETON_METHOD);
        
        double[] singleton = dim > 0 ? new double[dim] : new double[0];        
        if (dim > 0) {
            //the lovis vector is already in ascending order
            
            double den = 0d;
            for (int i = 1; i <= dim; i++) {
                den += Math.pow(lovis[i - 1], alfa);
            }
            
            if (den != 0) {
                for (int i = lovis.length; i >= 1; i--) {
                    singleton[i - 1] = Math.pow(lovis[i - 1], alfa) / den;
                }
            }
        }
        
        outParams2String[0] = outParams2String[0] + ";AO1;" + alfa;
        return singleton;
    }
    
    static private double[] AGGREGATED_OBJECTS_2_METHOD(int dim, HashMap<PARAMETER_NAMES, Object> parameters, Double[] lovis, String[] outParams2String) {
        float alfa = (Float) parameters.get(PARAMETER_NAMES.ALFA_SINGLETON_METHOD);
        
        double[] singleton = dim > 0 ? new double[dim] : new double[0];
        if (dim > 0) {
            //the lovis vector is already in ascending order
            
            double den = 0d;
            for (int i = 1; i <= dim; i++) {
                den += Math.pow(Math.abs(1d - lovis[i - 1]), alfa);
            }
            
            if (den != 0) {
                for (int i = lovis.length; i >= 1; i--) {
                    singleton[i - 1] = Math.pow(Math.abs(1d - lovis[i - 1]), alfa) / den;
                }
            }
        }
        
        outParams2String[0] = outParams2String[0] + ";AO2;" + alfa;
        return singleton;
    }

    //Windowed Choquet
    static public double[] windowedcompute(double[] lovis, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String, int windowSize) 
    {
        int n = lovis.length;
        
        double[] windowedLovis = new double[n];
        
        for (int i = 0; i < n; i++) {
            
            double[] window = Tools.generateWindowVector(lovis, i, windowSize);
            
            windowedLovis[i] = compute(window, parameters, outParams2String);
        }
        
        return windowedLovis;
    }
}
