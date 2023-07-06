/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author cesar
 */
public class NumberUtil
{    
    public static double[] sum(double[] v1, double[]v2) 
    {
        double [] sumvector = new double[v1.length];
        
        for (int j = 0; j < v1.length; j++) 
        {
            sumvector[j]=v1[j]+v2[j];
            
        }
            return sumvector;
    }
    public static double sumVectorElements(double[] v1) 
    {
        double sum = 0;
        
        for (int j = 0; j < v1.length; j++) 
        {
            sum+=v1[j];
            
        }
            return sum;
    }
    static public Matrix mulElementByElement( int atomCount, Matrix m1, Matrix m2, Matrix m3 )
    {
        for ( int i = 0; i < atomCount; i++ )
        {
            for ( int j = i; j < atomCount; j++ )
            {
                m3.matrix[i][j] = m1.matrix[i][j] * m2.matrix[i][j];
                m3.matrix[j][i] = m1.matrix[j][i] * m2.matrix[j][i];
            }
        }
        
        return m3;
    }
    
    static public BigDecimal pow( BigDecimal bd1, BigDecimal bd2 )
    {
        BigDecimal aux = new BigDecimal( bd1.toString() );
        
        int count = 0;
        while ( aux.compareTo( new BigDecimal( Double.MAX_VALUE ) ) > 0 )
        {
            aux = root( 2, aux );
            count++;
        }
        
        // get integer part of exp
        BigDecimal a = new BigDecimal( bd2.toBigInteger() );
        
        // get fractional part of exp
        BigDecimal b = bd2.subtract( a );
        
        
        BigDecimal aToExp = aux.pow( a.toBigIntegerExact().intValue() );
        BigDecimal bToExp = new BigDecimal( Math.pow( aux.doubleValue() , b.doubleValue() ) );
        
        for ( int i = 1; i <= count; i++ )
        {
            aToExp = aToExp.multiply( aToExp );
            bToExp = bToExp.multiply( bToExp );
        }
        
        // putting all together
        return aToExp.multiply( bToExp );
    }
    
    private final static int PRECISION = 50;
    
    private final static MathContext mathcontext = new MathContext( PRECISION, RoundingMode.HALF_EVEN );
    
    private static final BigDecimal ONE_DOT = BigDecimal.ONE;
    private static final BigDecimal TWO_DOT = BigDecimal.valueOf(2.);
    private static final BigDecimal ROOT_10_TWO = new BigDecimal("1.07177346253629316421300632502334202290638460497755678");
    private static final BigDecimal ONE_HALF = BigDecimal.valueOf(.5);
    private static final BigDecimal ONE_SIXTH = ONE_DOT.divide(BigDecimal.valueOf(6),mathcontext);
    
    static public BigDecimal root( int n, BigDecimal z )
    {
        if ( n==0 ) 
        {
            throw new IllegalArgumentException("Zeroth root does not exist!");
        }
        
        if ( n < 0 )
        {
            return ONE_DOT.divide(root(-n,z), mathcontext);
        }
        
        byte sign = 1;
        if ( z.signum() < 0 ) 
        {
            if ( n % 2 == 0 ) 
            {
                throw new IllegalArgumentException("" + n + "-th root of a negative number");
            }
            sign = -1;
            z = z.negate();
        }
        
        int scale = PRECISION;
        
        BigDecimal w, n1, n2, h;
        BigDecimal accuracy;
        
        if ( z.compareTo(ONE_DOT) > 0 ) 
        {
            accuracy = z.multiply(new BigDecimal("1e-"+scale/2));
        }
        else 
        {
            accuracy = new BigDecimal("1e-"+scale);
        }
        
        w = pow( ROOT_10_TWO, 10 * z.toBigInteger().bitLength() / n ).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        
        n1 = BigDecimal.valueOf(n-1).divide(BigDecimal.valueOf(n), PRECISION, RoundingMode.HALF_EVEN); // (n-1)/n
        n2 = BigDecimal.valueOf(n);
        while ( pow(w,n).subtract(z).abs().compareTo(accuracy) > 0 ) 
        {
            w = w.multiply(n1).add(z.divide(pow(w,n-1).multiply(n2), scale, RoundingMode.HALF_UP));
            w = w.setScale(scale+n, BigDecimal.ROUND_HALF_EVEN);
        }
        
        if (sign < 0)
        {
            w = w.negate();
        }
        
        h = z.divide(w.pow(n), PRECISION, RoundingMode.HALF_EVEN).subtract(ONE_DOT);
        
        if ( h.compareTo(ONE_DOT) < 0 )  // accuracy < 1 always???
        {
            // Taylor expansion of (1+h)^{1/n} to compute w (1+h)^{1/n} = z^{1/n}:
            n2 = ONE_DOT.divide(n2, PRECISION, RoundingMode.HALF_EVEN);
            
            // factor f = 1 + h/n - 1/2n * (n-1)/n * h^2 + 1/6n * (n-1)/n * h^3: 
            BigDecimal f = ONE_DOT.add(h.multiply(n2));
            f = f.subtract(ONE_HALF.multiply(n2).multiply(n1).multiply(pow(h,2)));
            f = f.add(ONE_SIXTH.multiply(n2).multiply(n1).multiply(TWO_DOT.subtract(n1)).multiply(pow(h,3)));
            w = w.multiply(f);
        }
        
        return w.setScale(scale, BigDecimal.ROUND_HALF_EVEN); //(PRECISION, BigDecimal.ROUND_HALF_EVEN);
    }
    
    static private BigDecimal pow( BigDecimal x, int n )
    {
        //if (x.equals(ZERO_DOT) && n == 0) return ONE_DOT;
        return x.pow(n, mathcontext);
    }
}
