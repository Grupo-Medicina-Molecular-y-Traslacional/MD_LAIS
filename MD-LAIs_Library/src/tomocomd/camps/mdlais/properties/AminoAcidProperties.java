package tomocomd.camps.mdlais.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openscience.cdk.protein.data.PDBAtom;

/**
 *
 * @author econtreras
 */
public class AminoAcidProperties {

    private final Map<String, double[]> aaProperties;

    private final List<String> header;

    private final String configFile = "tomocomd/camps/mdlais/properties/aminoacid_weights.txt";

    public AminoAcidProperties() throws IOException 
    {
        aaProperties = new HashMap<>();

        header = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(configFile)));

        String currentLine = bufferedReader.readLine();//skip header

        String[] components = currentLine.split("\t");

        header.addAll(Arrays.asList(components).subList(2, components.length));

        currentLine = bufferedReader.readLine();

        while (currentLine != null) 
        {
            components = currentLine.split("\t");

            int len = components.length - 2;

            double[] currentAAProperties = new double[len];

            for (int i = 0; i < len; i++) 
            {
                currentAAProperties[i] = Double.parseDouble(components[i + 2]);
            }

            String aminoAcidCode = components[0];

            aaProperties.put(aminoAcidCode, currentAAProperties);
            
            currentLine = bufferedReader.readLine();
        }

        bufferedReader.close();
    }

    public double getAminoAcidPropertyValue(PDBAtom atom, AminoAcidProperty property) 
    {
        if (property != AminoAcidProperty.unit) 
        {
            int pos = header.indexOf(property.toString());

            return aaProperties.get(atom.getResName())[pos];
        } else 
        {
            return 1d;
        }
    }
}
