package tomocomd.camps.mdlais.exceptions;

import org.openscience.cdk.interfaces.IMolecule;

/**
 * @author Rajarshi Guha
 * @author Cesar
 */
public class ExceptionInfo 
{
    private int serial;
    
    private String descriptorName;
    private IMolecule molecule;    
    
    private String myMessage;
    private Exception exception;
    
    public ExceptionInfo(int serial, IMolecule molecule, Exception exception, String name) 
    {
        this.serial = serial;
        this.molecule = molecule;
        this.exception = exception;
        this.myMessage = name;        
    }
    
    public String getMyMessage() 
    {
        return myMessage;
    }
    
    public String getDescriptorName() 
    {
        return descriptorName;
    }
    
    public void setDescriptorName(String descriptorName) 
    {
        this.descriptorName = descriptorName;
    }
    
    public int getSerial() 
    {
        return serial;
    }
    
    public void setSerial(int serial) 
    {
        this.serial = serial;
    }
    
    public IMolecule getMolecule() 
    {
        return molecule;
    }
    
    public void setMolecule(IMolecule molecule) 
    {
        this.molecule = molecule;
    }
    
    public Exception getException() 
    {
        return exception;
    }
    
    public void setException(Exception exception) 
    {
        this.exception = exception;
    }
}
