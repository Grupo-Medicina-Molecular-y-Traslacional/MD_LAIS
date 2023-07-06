/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.tools.invariants;

/**
 *
 * @author cysva
 */
public class Norms 
{
    public static double MinkoskyNorm( double[] lovis, int k ) 
    {
        double sum = 0;
        for ( int i = 0; i < lovis.length; i++ ) 
        {
            sum = sum + Math.pow( lovis[i], k );
        }
        
        if ( k == 1 ) 
        {
            return sum;
        } 
        else if ( k == 2 ) 
        {
            return Math.sqrt( sum );
        }
        
        return Math.cbrt( sum );
    }
    
    public static double PenroseNorm( double[] lovis )
    {
        int longitud = lovis.length;
        if (longitud <= 0) 
        {
            return 0;
        }
        
        double result = 0;
        for (int i = 0; i < longitud; i++) 
        {
            result += lovis[i];
        }
        
        result = Math.pow(result, 2);
        result = result / Math.pow(longitud, 2);
        result = Math.pow(result, (double) 1 / 2);
        
        return result;
    }
    // Windowed Norms
    public static double[] windowedMinkoskiNorm(double[] lovis, int k, int windowSize) 
    {
        int n = lovis.length;

        double[] windowedLovis = new double[n];

        for (int i = 0; i < n; i++) {
            
            double[] window = Tools.generateWindowVector(lovis, i,windowSize);
            
            windowedLovis[i] = MinkoskyNorm(window, k);
        }

        return windowedLovis;
    }
}
