package tomocomd.camps.mdlais.properties;

import org.openscience.cdk.math.Vector;

/**
 * @author crjacas
 */
public class PropertyVector extends Vector 
{    
    static public PropertyVector getUnitaryVector(int length) 
    {
        PropertyVector unitaryVector = new PropertyVector(length);
        
        for (int i = 0; i < length; i++) 
        {
            unitaryVector.vector[i] = 1;
        }
        
        return unitaryVector;
    }
    
    /** Creates a new instance of PropertyVector */
    public PropertyVector(double[] array) 
    {
        super(array);
    }
    
    public PropertyVector(int size) 
    {
        super(size);
    }
    
    /**
     * Return element al specific position
     */
    public double getElementAt(int index) 
    {
        return vector[index];
    }
    
    public void setElementAt(int index,double value)
    {
        vector[index] = value;
    }
    
    /**
     * return the vector as a Double Array
     */
    public double[] asDoubleArray() 
    {
        return vector;
    }
    
    /**
     *  Return a sum of all elements of the vector
     *  for Total meaning descriptors
     */
    public double sumUpVector() 
    {
        double sum = 0;
        for (int i = 0; i < vector.length; i++) 
        {
            sum += vector[i];
        }
        return sum;
    }
    
    public void destroy()
    {
        vector = null;
    }
}

