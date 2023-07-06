/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.tools.invariants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yasilvameida
 */
public class Tools {

    public static double[] Standarize(double[] lovis) 
    {
        int n = lovis.length;
        double media = Means.ArithmeticMean(lovis);
        double sd = Statistics.StandardDeviation(lovis);
        if (sd == 0) {
            sd = 1;
        }
        for (int i = 0; i < n; i++) {
            lovis[i] = (lovis[i] - media) / sd;
        }
        return lovis;
    }
    
    public static double[] NormalizeMinMax(double[] lovis) 
    {
        int n = lovis.length;
        
        double min = Statistics.XMin(lovis);
        
        double max = Statistics.XMax(lovis);
        
        double range = max-min;
        
        range = range!=0?range:1;
        
        double [] normalized = new double[n];
        
        for (int i = 0; i < n; i++) 
        {
            normalized[i] = (lovis[i] - min) / range;
        }
        
        return normalized;
    }
    
    public static double[] Sort(double[] lovis) {
        double[] lovisCopy = lovis.clone();
        Arrays.sort(lovisCopy);
        return lovisCopy;
    }

    public static double truncar(double num, int cifra) {
        if (Double.isNaN(num)) {
            num = 0;
        }
        BigDecimal decim = new BigDecimal(10);
        BigDecimal power = decim.pow(cifra);
        BigDecimal numb = new BigDecimal(num);
        BigInteger intg = numb.multiply(power).toBigInteger();
        BigDecimal r = new BigDecimal(intg);
        r = r.divide(power);
        return r.doubleValue();
    }

    public static double Log2(double x) {
        return Math.log10(x) / Math.log10(2);
    }
    
    public static double[] generateWindowVector(double[] lovis, int index, int windowSize) {
        int n = lovis.length;

        List<Double> aux = new LinkedList<>();

        for (int j = 1; j <= windowSize; j++) {
            if ((index - j) >= 0) {
                aux.add(lovis[index - j]);
            }
        }

        aux.add(lovis[index]);

        for (int j = 1; j <= windowSize; j++) {
            if ((index + j) < n) {
                aux.add(lovis[index + j]);
            }
        }

        int size = aux.size();

        double[] window = new double[size];

        for (int j = 0; j < size; j++) {
            window[j] = aux.get(j);
        }

        return window;
    }
}
