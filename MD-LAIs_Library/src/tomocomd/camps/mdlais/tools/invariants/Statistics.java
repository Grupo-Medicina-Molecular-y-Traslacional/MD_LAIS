/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.tools.invariants;

/**
 *
 * @author jricardo
 */
public class Statistics 
{
    public static double ThirdCentralMoment( double[] lovis ) 
    {
        double sum = 0;        
        double arithMean = Means.ArithmeticMean( lovis );        
        double stdDev = StandardDeviation( lovis );
        
        if (stdDev == 0) 
        {
            return 0;
        }
        
        for (int i = 0; i < lovis.length; i++) 
        {
            sum = sum + Math.pow( ( lovis[i] - arithMean ) / stdDev, 3 );
        }
        
        return sum;
    }
    
    public static double Skewness( double[] lovis ) 
    {
        if ( lovis.length < 3 ) 
        {
            return 0;
        }
        
        double tcm_std = ThirdCentralMoment( lovis );
        double result = ( ( lovis.length * tcm_std) / ( ( lovis.length - 1 ) * ( lovis.length - 2 ) ) );
        
        return result;
    }
    
    public static double Kurtosis( double[] lovis ) 
    {
        int n = lovis.length;        
        if (n < 4) 
        {
            return 0;
        }
        
        double M2 = 0, M4 = 0;
        double arithmeticMean = Means.ArithmeticMean( lovis );
        
        for (int i = 0; i < lovis.length; i++) 
        {
            M2 = M2 + Math.pow( lovis[i] - arithmeticMean, 2 );
            M4 = M4 + Math.pow( lovis[i] - arithmeticMean, 4 );
        }
        
        double stdDev4 = Math.pow(StandardDeviation( lovis ), 4);
        
        if (stdDev4 == 0) 
        {
            return 0;
        }
        
        return (n * (n + 1) * M4 - 3 * M2 * M2 * (n - 1))
                / ((n - 1) * (n - 2) * (n - 3) * stdDev4);
    }
    
    public static double XMax( double[] lovis ) 
    {
        if ( lovis.length <= 0 ) 
        {
            return 0;
        }
        
        Double max = lovis[0];
        for ( int i = 1; i < lovis.length; i++ ) 
        {
            max = Math.max( max, lovis[i] );
        }
        
        return max;
    }
    
    public static double XMin( double[] lovis )
    {
        if ( lovis.length <= 0 ) 
        {
            return 0;
        }
        
        Double min = lovis[0];
        for ( int i = 1; i < lovis.length; i++ ) 
        {
            min = Math.min( min, lovis[i] );
        }
        
        return min;
    }
    
    public static double Range( double[] lovis ) 
    {
        return XMax( lovis ) - XMin( lovis );
    }
    
    public static double Variance( double[] lovis ) 
    {
        int longitud = lovis.length;
        double sum = 0;
        int nZeros = 0;
        
        if (longitud <= 1) 
        {
            return 0;
        }
        
        double arithMean = Means.ArithmeticMean( lovis );
        
        for (int i = 0; i < longitud; i++) 
        {
            if ( lovis[i] == 0 ) 
            {
                nZeros++;
            }
            else 
            {
                sum = sum + Math.pow( lovis[i] - arithMean, 2 );
            }
        }
        
        if ((longitud - nZeros) == 0) //all lovis are zeros
        {
            return 0;
        }
        
        return sum / (longitud - 1);
    }
    
    public static double StandardDeviation( double[] lovis ) 
    {
        return Math.sqrt( Variance( lovis ) );
    }
    
    public static double VariationCoefficient( double[] lovis ) 
    {
        int longitud = lovis.length;
        if (longitud <= 0) 
        {
            return 0;
        }
        
        double ArithmeticMean = Means.ArithmeticMean( lovis );        
        if (ArithmeticMean == 0) 
        {
            return 0;
        }
        
        return StandardDeviation( lovis ) / ArithmeticMean;
    }
    
    public static double Percentil( double[] lovis, int per ) 
    {
        int longitud = lovis.length;
        if (longitud <= 0) 
        {
            return 0;
        }
        
        double[] a1 = Tools.Sort( lovis );
        int num = (per * longitud) / 100;
        return a1[num];
    }
    
    public static double I50( double[] lovis ) 
    {
        return Percentil( lovis, 75 ) - Percentil( lovis, 25 );
    }
    
    // Windowed Statistics
    
    public static double[] windowedStatistics(double[] lovis, int windowSize, String inv) 
    {
        int n = lovis.length;

        double[] windowedLovis = new double[n];
        
        inv = inv.substring(0, inv.indexOf("["));

        for (int i = 0; i < n; i++) 
        {
            double[] window = Tools.generateWindowVector(lovis, i,windowSize);
            
            switch (inv) 
            {
                case "s":
                
                windowedLovis[i] = Skewness(window);
                
                break;
                
                case "k":
                
                windowedLovis[i] = Kurtosis(window);
                
                break;
                
                case "v":
                
                windowedLovis[i] = Variance(window);
                
                break;
                
                case "sd":
                
                windowedLovis[i] = StandardDeviation(window);
                
                break;
                
                case "vc":
                
                windowedLovis[i] = VariationCoefficient(window);
                
                break;
                
                case "ra":
                
                windowedLovis[i] = Range(window);
                
                break;
                
                case "q1":
                
                windowedLovis[i] = Percentil(window,25);
                
                break;
                
                case "q2":
                
                windowedLovis[i] = Percentil(window,50);
                
                break;
                
                case "q3":
                
                windowedLovis[i] = Percentil(window,75);
                
                break;
                
                case "i50":
                
                windowedLovis[i] = I50(window);
                
                break;
                
                case "mn":
                
                windowedLovis[i] = XMin(window);
                
                break;
                
                case "mx":
                
                windowedLovis[i] = XMax(window);
                
                break;
            }       
            
        }

        return windowedLovis;
    }
}
