/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.tools.invariants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import tomocomd.camps.mdlais.math.NumberUtil;

/**
 *
 * @author cysva
 */
public class Means 
{
    public static String GeometricMeanBigNumber( double[] lovis ) 
    {
        int  n = lovis.length;
        if ( n == 0 )
        {
            return "0";
        }
        
        BigDecimal prod = BigDecimal.ONE;
        int nZeros = 0;
        
        for ( int i = 0; i < n; i++ ) 
        {
            if ( lovis[i] < 0 || Double.isNaN( lovis[i] ) )
            {
                return Double.toString( Double.NaN );
            }
            else if ( lovis[i] == 0 ) 
            {
                nZeros++;
            }
            else 
            {
                prod = prod.multiply( BigDecimal.valueOf( lovis[i] ) );
            }
        }
        
        if ( ( n - nZeros ) == 0 ) //all lovis are zeros
        {
            return "0";
        }
        
        if ( prod.signum() == 1 ) //positive
        {
            return NumberUtil.pow( prod, new BigDecimal( (double) 1 / (n - nZeros) ) ).round( new MathContext( 15, RoundingMode.HALF_EVEN ) ).toString();
        }
        else 
        {
            return "0";
        }
    }
    
    public static double ArithmeticMean( double[] lovis ) 
    {
        double sum = 0;
        
        for (int i = 0; i < lovis.length; i++) 
        {
            sum += lovis[i];
        }
        
        if ( (lovis.length > 0) && (sum != 0) ) 
        {
            return sum / lovis.length;
        }
        
        return 0;
    }
    
    public static double PotentialMean( double[] lovis, int pot ) 
    {
        int  n = lovis.length, nZeros = 0;        
        
        if ( n == 0 ) 
        {
            return 0;
        }
        
        double value = 0;
        
        for ( int i = 0; i < n; i++ )
        {
             if ( lovis[i] == 0 ) 
            {
                nZeros++;
            }
            else 
            {
                value = value + Math.pow( lovis[i], pot );
            }
        }
        
        if (n == nZeros) // all lovis are zeros
        {
            return 0;
        }
        
        switch ( pot ) 
        {
                case -1: // harmonic mean
                    value = value!=0?( n ) / value:Double.NaN;
                    break;
                case 2: // quadratic mean
                    value = Math.sqrt(value/n);
                    break;
                case 3: // potential mean
                    value = Math.cbrt( value/n);
                    break;
                default:
                    value = Double.NaN;
                    break;
         }
        
        return value;
    }
    
    // Windowed means
    
    public static String[] windowedGeometricMeanBigNumber(double[] lovis, int windowSize) 
    {
        int n = lovis.length;

        String[] windowedLovis = new String[n];

        for (int i = 0; i < n; i++) {
            
            double[] window = Tools.generateWindowVector(lovis, i,windowSize);
            
            windowedLovis[i] = GeometricMeanBigNumber(window);
        }

        return windowedLovis;
    }
    
    public static double[] windowedArithmeticMean(double[] lovis, int windowSize) 
    {
        int n = lovis.length;

        double[] windowedLovis = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double[] window = Tools.generateWindowVector(lovis, i,windowSize);
            
            windowedLovis[i] = ArithmeticMean(window);
        }

        return windowedLovis;
    }
    
    public static double[] windowedPotentialMean(double[] lovis, int windowSize, int pot) 
    {
        int n = lovis.length;

        double[] windowedLovis = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double[] window = Tools.generateWindowVector(lovis, i,windowSize);
            
            windowedLovis[i] = PotentialMean(window, pot);
        }

        return windowedLovis;
    }
    
}
