/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package tomocomd.camps.mdlais.gui.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import nu.xom.Element;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.tools.invariants.InvariantType;

/**
 *
 * @author cesar
 * @author econtreras
 */
public interface IFormPanel
{
    int getTotalDesc();
    
    int getSelectedDescriptors();
    
    HashMap<InvariantType, Object[]> getInvParameters();
    
    WeightConfiguration getCutoffConfiguration();
    
    Hashtable<String, String> getCutOffsList();
    
    ArrayList<String> getGroupsList();
    
    ArrayList<String> getAminoAcidPropertiesList();
    
    ArrayList<String> getInvariantsList();
    
    ArrayList<String> getNoClassicsInvariantList();
    
    ArrayList<String> getClassicsInvariantList();
       
    boolean doApply();
    
    void initialize();
    
    void setPanelAttribute( String attribute );
    
    void setBlockDuringCompute( boolean status );
    
    void readConfiguration( Element root );
    
    void saveConfiguration( Element root );
    
    void setAALevel();    
    
    List<IDescriptor> getDescriptorList();
}
