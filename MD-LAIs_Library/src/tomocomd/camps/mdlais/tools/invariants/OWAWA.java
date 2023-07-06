/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.tools.invariants;

import java.util.HashMap;

/**
 *
 * @author Cesar
 */
public class OWAWA 
{
    public enum PARAMETER_NAMES 
    {
        BETA_OWAWA, LAMBDA_OWA, METHOD_OWA, ALFA_OWA, BETA_OWA, DELTA_WA, METHOD_WA, ALFA_WA, BETA_WA
    }
    
    public enum WEIGHT_METHODS 
    {
        S_OWA, WINDOW_OWA, EXPONENTIAL_SMOOTHING_1, EXPONENTIAL_SMOOTHING_2, AGGREGATED_OBJECTS_1, AGGREGATED_OBJECTS_2, NONE
    }
    
    static public double compute( double[] lovis_, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String )
    {
        if ( lovis_ == null || lovis_.length == 0 )
        {
            return Double.NaN;
        }
        
        int dim = lovis_.length;
        int[] indeces = new int[ lovis_.length ];
        double[] lovis = new double[ lovis_.length ];
        
        for ( int i = 0; i < lovis_.length; i++ )
        {
            indeces[ i ] = i;
            
            if ( ( lovis[ i ] = lovis_[ i ] ) < 0 )
            {
                lovis = null;
                indeces = null;
                
                dim = -1;
                break;
            }
        }
        
        if ( dim != -1 )
        {
            sort( lovis, indeces ); // it sorts the lovis vector in ascending order
        }
        
        double[] owa_weights = compute_weights( true , WEIGHT_METHODS.valueOf( (String) parameters.get( PARAMETER_NAMES.METHOD_OWA ) ), dim, parameters, lovis, outParams2String );
        double[]  wa_weights = compute_weights( false, WEIGHT_METHODS.valueOf( (String) parameters.get( PARAMETER_NAMES.METHOD_WA  ) ), dim, parameters, lovis, outParams2String );
        
        float beta_owawa = (Float) parameters.get( PARAMETER_NAMES.BETA_OWAWA );
        
        int lambda = (Integer) parameters.get( PARAMETER_NAMES.LAMBDA_OWA );
        int  delta = (Integer) parameters.get( PARAMETER_NAMES.DELTA_WA   );
        
        double gowa = 0;
        double  wgm = 0;
        
        if ( dim != -1 )
        {
            if ( lambda != 0 && delta != 0 )
            {
                for ( int i = lovis.length, j = 1; i >= 1; i--, j++ )
                {   
                    gowa += Math.pow( lovis[i - 1], lambda ) * owa_weights[ j - 1 ];
                     wgm += Math.pow( lovis[i - 1], delta  ) *  wa_weights[ indeces[ i - 1 ] ];       
                }
            }
            else
            {
                if ( lambda == 0 ) // weigthed ordered geometric mean
                {
                    gowa = 1;
                    for ( int i = lovis.length, j = 1; i >= 1; i--, j++ )
                    {   
                        gowa *= Math.pow( lovis[i - 1], owa_weights[ j - 1 ] );
                    }
                }
                else // generalized ordered weigthed averaging
                {
                    for ( int i = lovis.length, j = 1; i >= 1; i--, j++ )
                    {   
                        gowa += Math.pow( lovis[i - 1], lambda ) * owa_weights[ j - 1 ];
                    }
                }
                
                if ( delta == 0 ) // weigthed geometric mean
                {
                    wgm = 1;
                    for ( int i = lovis.length, j = 1; i >= 1; i--, j++ )
                    {
                        wgm *= Math.pow( lovis[i - 1], wa_weights[ indeces[ i - 1 ] ]  );       
                    }
                }
                else // weigthed generalized mean
                {
                    for ( int i = lovis.length, j = 1; i >= 1; i--, j++ )
                    {
                        wgm += Math.pow( lovis[i - 1], delta  ) *  wa_weights[ indeces[ i - 1 ] ];       
                    }
                }
            }
            gowa = ( lambda != 0 ? Math.pow( gowa, 1d/lambda ) : gowa ) * beta_owawa;
            wgm  = ( delta  != 0 ? Math.pow(  wgm, 1d/delta  ) :  wgm ) * ( 1 - beta_owawa );
        }
        
        lovis = null;
        indeces = null;                
        outParams2String[0] = beta_owawa + outParams2String[0];
        
        return dim != -1 ? ( gowa + wgm ) : Double.NaN;
    }
    
    static private double[] compute_weights( boolean isOWAVector, WEIGHT_METHODS method, int dim, HashMap<PARAMETER_NAMES, Object> parameters, double[] lovis, String[] outParams2String )
    {
        switch ( method ) 
        {
            case S_OWA:
                return S_OWA_Operator( isOWAVector, dim, parameters, outParams2String );
                
            case WINDOW_OWA:
                return WINDOW_OWA_Operator( isOWAVector, dim, parameters, outParams2String );
                
            case EXPONENTIAL_SMOOTHING_1:
                return EXPONENTIAL_SMOOTHING_1_OWA_Operator( isOWAVector, dim, parameters, outParams2String );
                
            case EXPONENTIAL_SMOOTHING_2:
                return EXPONENTIAL_SMOOTHING_2_OWA_Operator( isOWAVector, dim, parameters, outParams2String );
                
            case AGGREGATED_OBJECTS_1:
                return AGGREGATED_OBJECTS_1_OWA_Operator( isOWAVector, dim, parameters, lovis, outParams2String );
                
            case AGGREGATED_OBJECTS_2:
                return AGGREGATED_OBJECTS_2_OWA_Operator( isOWAVector, dim, parameters, lovis, outParams2String );
        }
        
        outParams2String[0] = outParams2String[0] + ";NONE";
        return dim > 0 ? new double[ dim ] : new double[ 0 ];
    }
    
    static private double[] S_OWA_Operator( boolean isOWAVector, int dim, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String ) 
    {        
        float alfa = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.ALFA_OWA : PARAMETER_NAMES.ALFA_WA );
        float beta = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.BETA_OWA : PARAMETER_NAMES.BETA_WA );
        
        double[] weights = dim > 0 ? new double[ dim ] : new double[ 0 ];
        if ( dim > 0 )
        {
            weights[0] = (1f / dim) * (1 - (alfa + beta)) + alfa;
            weights[dim - 1] = (1f / dim) * (1 - (alfa + beta)) + beta;
            
            for ( int i = 2; i <= dim - 1; i++ ) 
            {
                weights[i - 1] = (1f / dim) * (1 - (alfa + beta));
            }
        }
        
        outParams2String[0] = outParams2String[0] + ";" + ( isOWAVector ? parameters.get( PARAMETER_NAMES.LAMBDA_OWA ) : parameters.get( PARAMETER_NAMES.DELTA_WA ) ) + ";S-OWA;" + alfa + ";" + beta;
        return weights;
    }
    
    static private double[] WINDOW_OWA_Operator( boolean isOWAVector, int dim, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String ) 
    {
        float alfa = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.ALFA_OWA : PARAMETER_NAMES.ALFA_WA );
        float beta = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.BETA_OWA : PARAMETER_NAMES.BETA_WA );
        
        double[] weights = dim > 0 ? new double[ dim ] : new double[ 0 ];
        if ( dim > 0 )
        {
            int k = (k = (int) (alfa * dim)) == 0 ? k + 1 : k;
            int m = (int) (beta * dim);
            
            for ( int i = 1; i <= dim; i++ )
            {
                weights[i - 1] = (i < k ? 0 : i >= k && i <= m ? 1f / ((m - k) + 1) : 0);
            }
        }
        
        outParams2String[0] = outParams2String[0] + ";" + ( isOWAVector ? parameters.get( PARAMETER_NAMES.LAMBDA_OWA ) : parameters.get( PARAMETER_NAMES.DELTA_WA ) ) + ";W-OWA;" + alfa + ";" + beta;
        return weights;
    }
    
    static private double[] EXPONENTIAL_SMOOTHING_1_OWA_Operator( boolean isOWAVector, int dim, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String ) 
    {
        float alfa = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.ALFA_OWA : PARAMETER_NAMES.ALFA_WA );
        
        double[] weights = dim > 0 ? new double[ dim ] : new double[ 0 ];
        if ( dim > 0 )
        {
            weights[0] = alfa;        
            for ( int i = 2; i <= dim - 1; i++ )
            {
                weights[i - 1] = weights[i - 2] * (1 - alfa);
            }
            weights[dim - 1] = Math.pow( 1f - alfa, dim - 1 ); // = weights[dim - 2] * ((1f - alfa) / alfa);
        }
        
        outParams2String[0] = outParams2String[0] + ";" + ( isOWAVector ? parameters.get( PARAMETER_NAMES.LAMBDA_OWA ) : parameters.get( PARAMETER_NAMES.DELTA_WA ) ) + ";ES1-OWA;" + alfa;
        return weights;
    }
    
    static private double[] EXPONENTIAL_SMOOTHING_2_OWA_Operator( boolean isOWAVector, int dim, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String ) 
    {
        float alfa = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.ALFA_OWA : PARAMETER_NAMES.ALFA_WA );
        
        double[] weights = dim > 0 ? new double[ dim ] : new double[ 0 ];
        if ( dim > 0 )
        {
            weights[dim - 1] = 1f - alfa;        
            for ( int i = dim - 1; i >= 2; i-- )
            {
                weights[i - 1] = weights[i] * (1 - weights[dim - 1]); // = weights[i] * alfa;
            }
            weights[0] = Math.pow( alfa, dim - 1 ); // = weights[1] * (1 - weights[dim - 1]) / weights[dim - 1];
        }
        
        outParams2String[0] = outParams2String[0] + ";" + ( isOWAVector ? parameters.get( PARAMETER_NAMES.LAMBDA_OWA ) : parameters.get( PARAMETER_NAMES.DELTA_WA ) ) + ";ES2-OWA;" + alfa;
        return weights;
    }
    
    static private double[] AGGREGATED_OBJECTS_1_OWA_Operator( boolean isOWAVector, int dim, HashMap<PARAMETER_NAMES, Object> parameters, double[] lovis, String[] outParams2String ) 
    {
        float alfa = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.ALFA_OWA : PARAMETER_NAMES.ALFA_WA );
        
        double[] weights = dim > 0 ? new double[ dim ] : new double[ 0 ];
        if ( dim > 0 )
        {
            if ( !isOWAVector )
            {
                computeNaNVector( weights );
            }
            else
            {
                //the lovis vector is already in ascending order
                
                double den = 0d;
                for ( int i = 1; i <= dim ; i++ )
                {
                    den += Math.pow( lovis[i - 1], alfa );
                }
                
                if ( den != 0 )
                {
                    for ( int i = lovis.length, j = 1; i >= 1; i--, j++ )
                    {
                        weights[j - 1] = Math.pow( lovis[i - 1], alfa ) / den;
                    }
                }
            }
        }
        
        outParams2String[0] = outParams2String[0] + ";" + ( isOWAVector ? parameters.get( PARAMETER_NAMES.LAMBDA_OWA ) : parameters.get( PARAMETER_NAMES.DELTA_WA ) ) + ";AO1-OWA;" + alfa;
        return weights;
    }
    
    static private double[] AGGREGATED_OBJECTS_2_OWA_Operator( boolean isOWAVector, int dim, HashMap<PARAMETER_NAMES, Object> parameters, double[] lovis, String[] outParams2String ) 
    {
        float alfa = (Float) parameters.get( isOWAVector ? PARAMETER_NAMES.ALFA_OWA : PARAMETER_NAMES.ALFA_WA );
        
        double[] weights = dim > 0 ? new double[ dim ] : new double[ 0 ];
        if ( dim > 0 )
        {
            if ( !isOWAVector )
            {
                computeNaNVector( weights );
            }
            else
            {
                //the lovis vector is already in ascending order
                
                double den = 0d;
                for ( int i = 1; i <= dim ; i++ )
                {
                    den += Math.pow( Math.abs( 1d - lovis[i - 1] ), alfa );
                }
                
                if ( den != 0 )
                {
                    for ( int i = lovis.length, j = 1; i >= 1; i--, j++ )
                    {
                        weights[j - 1] = Math.pow( Math.abs( 1d - lovis[i - 1] ), alfa ) / den;
                    }
                }
            }
        }
        
        outParams2String[0] = outParams2String[0] + ";" + ( isOWAVector ? parameters.get( PARAMETER_NAMES.LAMBDA_OWA ) : parameters.get( PARAMETER_NAMES.DELTA_WA ) ) + ";AO2-OWA;" + alfa;
        return weights;
    }
    
    static private void computeNaNVector( double[] weights )
    {
        for ( int i = 0; i < weights.length ; i++ )
        {
            weights[ i ] = Double.NaN;
        }
    }
    
    static private void sort( double[] lovis, int[] indeces )
    {
        for ( int i = 0; i < lovis.length; i++ )
        {
            for ( int j = 0; j < lovis.length - 1 - i; j++ )
            {
                if ( lovis[ j ] > lovis[ j + 1 ] )
                {
                    double d_temp = lovis[ j ];
                    lovis[ j ] = lovis[ j + 1 ];
                    lovis[ j + 1 ] = d_temp;
                    
                    int i_temp = indeces[ j ];
                    indeces[ j ] = indeces[ j + 1 ];
                    indeces[ j + 1 ] = i_temp;
                }
            }
        }
    }
    
    //Windowed OWAWA
    
    static public double[] windowedcompute( double[] lovis_, HashMap<PARAMETER_NAMES, Object> parameters, String[] outParams2String, int windowSize )
    {
        int n = lovis_.length;

        double[] windowedLovis = new double[n];

        for (int i = 0; i < n; i++) {
            
            double[] window = Tools.generateWindowVector(lovis_, i,windowSize);
            
            windowedLovis[i] = compute(window, parameters, outParams2String);
        }

        return windowedLovis;
    }
}
