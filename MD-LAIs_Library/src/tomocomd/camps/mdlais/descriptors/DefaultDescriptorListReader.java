package tomocomd.camps.mdlais.descriptors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author econtreras
 */
public class DefaultDescriptorListReader {

    private final HashMap<String, List<String>> descriptorLists;

    private final String configFile = "tomocomd/camps/mdlais/descriptors/lists/list.txt";

    private final String configFolder = "tomocomd/camps/mdlais/descriptors/lists/";
    
    public DefaultDescriptorListReader() throws IOException 
    {
        descriptorLists = new HashMap<>();
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(configFile)));
            
            List<String> lines = new LinkedList<>();
            
            String currentLine = bufferedReader.readLine();
            
            while (currentLine!=null) 
            {                
                lines.add(currentLine);
                
                currentLine = bufferedReader.readLine();
            }
            
            bufferedReader.close();
            
            getDescriptorLists(lines);
            
        } catch (Exception ex) {
            Logger.getLogger(DefaultDescriptorListReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, List<String>> getDescriptorLists() 
    {
        return descriptorLists;
    }
    
    public HashMap<String, List<String>> getDescriptorLists(List<String> urls) throws IOException
    {
        for (String url : urls) 
        {
            List<String> headings = new LinkedList<>();
            
            String conf = configFolder+url+".csv";
            
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(conf)));
            
            String currentLine = bufferedReader.readLine();
            
            while (currentLine!=null) 
            {                
                headings.add(currentLine);
                
                currentLine = bufferedReader.readLine();
            }
            
            bufferedReader.close();
            
            descriptorLists.put(url, headings);
        }
        
        return descriptorLists;
    }
}
