package tomocomd.camps.mdlais.descriptors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.math.Vector;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import tomocomd.camps.mdlais.descriptors.result.AlgebraicDescriptorResult;
import tomocomd.camps.mdlais.local.LocalSymbolTool;
import tomocomd.camps.mdlais.local.LocalType;
import tomocomd.camps.mdlais.matrices.LocalOperationMatrix;
import tomocomd.camps.mdlais.weights.Weight;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.weights.WeightFactory;
import tomocomd.camps.mdlais.properties.AminoAcidProperties;
import tomocomd.camps.mdlais.properties.AminoAcidProperty;
import tomocomd.camps.mdlais.properties.PropertyTool;
import tomocomd.camps.mdlais.properties.PropertyVector;

/**
 *
 * @author econtreras
 */
public class MolecularDescriptor implements IMolecularDescriptor, Comparable, Cloneable {

    protected int processor;

    protected boolean cutOffAll;

    protected Weight weight;

    protected WeightConfiguration cutoffConfig;

    protected boolean isShowReport;
    protected boolean aminoLevelON;

    protected AminoAcidProperties aminoProperty;

    protected AminoAcidProperty x;

    protected PropertyVector propertyVectorX;

    protected double[] lovisArray;

    protected LocalType[] localType;

    protected DescriptorValue descriptorValue;

    protected BufferedWriter bufferWriter;

    protected String breakLine = "\r\n";

    protected String classicInv, noClassicInv;

    // global variables
    static private String[][] paramNames;

    static private Object[][] paramTypes;

    static private AminoAcidProperties[] aminoProperties;

    protected AlgebraicDescriptorResult arrayResult;

    protected String name = "";

    protected String specRefEnd;

    protected String fullpath;

    protected final String specRef = "http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#";

    protected Object[] params;

    public MolecularDescriptor(AminoAcidProperty property, LocalType[] localType) 
    {
        this.x = property;

        this.localType = localType;

        name = LocalSymbolTool.getAcronymAccordingToLocal(localType) + "_"+PropertyTool.getAcronymAccordingToAminoAcidProperty(x)+"_";
    }

    public AminoAcidProperty getX() {
        return x;
    }

    public String getBreakLine() {
        return breakLine;
    }

    public DescriptorValue getDescriptorValue() {
        return descriptorValue;
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(specRef + specRefEnd, this.getClass().getName(), "$Id$", "The Chemistry Development Kit");
    }

    @Override
    public IDescriptorResult getDescriptorResultType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getParameterNames() {
        paramNames[processor][0] = "is cutoff all";
        paramNames[processor][1] = "is show report";
        paramNames[processor][2] = "report path";
        paramNames[processor][3] = "Amino Level On";
        return paramNames[processor];
    }

    @Override
    public Object getParameterType(String name) {
        paramTypes[processor][0] = cutOffAll;
        paramTypes[processor][1] = isShowReport;
        paramTypes[processor][2] = fullpath;
        paramTypes[processor][3] = aminoLevelON;
        return paramTypes[processor];
    }

    /**
     * Sets the parameters attribute of the object.
     *
     * This descriptor takes one parameter, which should be Integer to indicate
     * the maximum limit for matrix order
     *
     * @param params The new parameters value
     * @throws CDKException if more than one parameter or a non-Integer
     * parameter is specified
     * @see #setParameters
     */
    @Override
    public void setParameters(Object[] params) throws CDKException {
        if (this.params == null) {
            if (params.length != 6) {
                throw new CDKException("Algebraic Form expects 6 parameters");
            }

            isShowReport = (Boolean) params[1];
            fullpath = (String) params[2];
            aminoLevelON = (Boolean) params[3];
            this.params = params;
        }
    }

    /**
     * Gets the parameters attribute of the object.
     *
     * @return The parameters value
     * @see #getParameters
     */
    @Override
    public Object[] getParameters() {
        paramTypes[processor][0] = cutOffAll;
        paramTypes[processor][1] = isShowReport;
        paramTypes[processor][2] = fullpath;
        paramTypes[processor][3] = aminoLevelON;
        return paramTypes[processor];
    }

    public Object[] getParams() {
        return params;
    }

    /**
     * Gets the name(s) for each descriptor value(s)
     *
     * @return An String array with all the names
     */
    @Override
    public String[] getDescriptorNames() {
        
        String cutOff = "";
        if (cutOffAll) {
            cutOff = "_UW";
        } else if (weight != null) {
            cutOff = weight.toString();
        }
       
        String[] tags = name.split("_");

        String[] namesArray = new String[1];

        for (int i = 0; i < namesArray.length; i++) 
        {
            try 
            {
                namesArray[i] = tags[0]
                            + cutOff+"_"+tags[1] ;
            } catch (Exception ex) 
            {

            }
        }

        return namesArray;
    }

    public void setCutoff(WeightConfiguration config) 
    {
        if (this.cutoffConfig == null) 
        {
            this.cutoffConfig = config;

            this.weight = WeightFactory.getCutoff(config);
            
            cutOffAll = weight==null;
        }
    }

    public AlgebraicDescriptorResult getArrayResult() {
        return arrayResult;
    }

    static public void createGlobalVariables(int processors, int dim) {
        if (paramNames != null && paramNames.length != processors) {
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = null;
                paramTypes[i] = null;
            }
            paramNames = null;
            paramTypes = null;
        }

        if (paramNames == null) {
            paramNames = new String[processors][];
            paramTypes = new Object[processors][];

            for (int i = 0; i < processors; i++) {
                paramNames[i] = new String[9];
                paramTypes[i] = new Object[9];
            }
        }
    }

    static public void readProperties(int processors) throws IOException 
    {
        aminoProperties = new AminoAcidProperties[processors];

        for (int i = 0; i < processors; i++) 
        {
            aminoProperties[i] = new AminoAcidProperties();
        }
    }

    static public void destroyProperties() 
    {
        if (aminoProperties != null) 
        {
            aminoProperties = null;
        }
    }

    public String getClassicInv() {
        return classicInv;
    }

    public String getNoClassicInv() {
        return noClassicInv;
    }

    public void setClassicInv(String classicInv) {
        this.classicInv = classicInv;
    }

    public void setNoClassicInv(String noClassicInv) {
        this.noClassicInv = noClassicInv;
    }

    @Override
    public DescriptorValue calculate(IAtomContainer container) {
        if (isShowReport) {
            //<editor-fold defaultstate="collapsed" desc="Show report">
            try {
                String title = (String) container.getProperty(CDKConstants.TITLE);

                String name = getDescriptorNames()[0];

                String path = fullpath + title + "_" + name;

                bufferWriter = new BufferedWriter(new FileWriter(path));
            } catch (IOException ex) {
                Logger.getLogger(MolecularDescriptor.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
        }

        //<editor-fold defaultstate="collapsed" desc="Property vector calculation">
        try {          
            calculatePropertyVectors(container);
        } catch (Exception e) {
            e.printStackTrace();

            return getDummyDescriptorValue(new CDKException("problem computing property vector:" + e));
        }
        //</editor-fold>

        arrayResult = new AlgebraicDescriptorResult();

        calculateDescriptor(container);

        if (isShowReport) {
            //<editor-fold defaultstate="collapsed" desc="Show report">
            try {
                bufferWriter.close();
                bufferWriter = null;
            } catch (IOException ex) {
                Logger.getLogger(MolecularDescriptor.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
        }

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                arrayResult, getDescriptorNames());
    }
    
    protected void calculateDescriptor(IAtomContainer container) 
    {
       if(weight!=null)
       {
        weight.setMolecule((IMolecule) container);
       }
       
       lovisArray = propertyVectorX.vector;
       
       LocalOperationMatrix loc = new LocalOperationMatrix(localType, (IMolecule) container);
       
       for (int i = 0; i < lovisArray.length; i++) 
       {
            double laiWeight =  (weight!=null?weight.cutoff()[i]:1)*loc.ratio(new IAtom[]{container.getAtom(i)});
            
            lovisArray[i]*=laiWeight;
       }
        
       if (isShowReport) {
            // <editor-fold defaultstate="collapsed" desc="ShowReport LAIS">
            try {
                bufferWriter.write("LAIS Vector " + breakLine);
                printVector(new Vector(lovisArray), container, bufferWriter);
                bufferWriter.flush();
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
            // </editor-fold>
        }
        
        arrayResult.add(lovisArray);

        if (isShowReport) {
            // <editor-fold defaultstate="collapsed" desc="Close buffer">
            try {
                bufferWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(MolecularDescriptor.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.toString());
            }
            // </editor-fold>
            // </editor-fold>
        }
    }

    final public void initialize(int processor) 
    {
        this.processor = processor;

        this.aminoProperty = aminoProperties[processor];
    }

    public void destroy() 
    {
        lovisArray = null;

        arrayResult = null;

        if (propertyVectorX != null) 
        {
            propertyVectorX.destroy();

            propertyVectorX = null;
        }       
    }

    protected void calculatePropertyVectors(IAtomContainer container) throws Exception 
    {
        propertyVectorX = PropertyTool.getAminoAcidPropertyVector(aminoProperty, x, container);

                if (isShowReport) {
            //<editor-fold defaultstate="collapsed" desc="Show report">
            try {
               
                bufferWriter.write("Property Vector X" + breakLine);
                printVector(propertyVectorX, container, bufferWriter);
            } catch (Exception ex) {

            }
            //</editor-fold>
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Print vector">
    protected void printVector(Vector v, IAtomContainer container, BufferedWriter bufferedWriter) throws IOException {
        int length = container.getAtomCount() - 1;

        bufferedWriter.write("[ ");

        for (int i = 0; i < container.getAtomCount() - 1; i++) {
            PDBAtom atom = (PDBAtom) container.getAtom(i);

            bufferedWriter.write(atom.getResName() + "." + (int) (1 + container.getAtomNumber(container.getAtom(i))) + "; ");
        }

        PDBAtom atom = (PDBAtom) container.getAtom(length);

        bufferedWriter.write(atom.getResName() + "." + (int) (1 + container.getAtomNumber(container.getAtom(length))) + " ]" + breakLine);

        // print the vector
        bufferedWriter.write("[ ");

        for (int i = 0; i < container.getAtomCount() - 1; i++) {
            bufferedWriter.write(v.vector[i] + "; ");
        }

        bufferedWriter.write(v.vector[length] + " ]" + breakLine + breakLine);
    }
    //</editor-fold>

    final protected DescriptorValue getDummyDescriptorValue(Exception e) 
    {
        int ndesc = getDescriptorNames().length;

        DoubleArrayResult results = new DoubleArrayResult(ndesc);

        for (int i = 0; i < ndesc; i++) {
            results.add(Double.NaN);
        }

        return new DescriptorValue(getSpecification(), getParameterNames(),
                getParameters(), results, getDescriptorNames(), e);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
