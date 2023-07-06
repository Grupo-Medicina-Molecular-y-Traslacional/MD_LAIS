package tomocomd.camps.mdlais.pdbfilter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openscience.cdk.interfaces.IChemFile;
import tomocomd.camps.mdlais.protein.PDBProtein;

/**
 *
 * @author econtreras
 */
public class PDBDuplicateCheckerSequence extends PDBDuplicate
{
    @Override
    public List<ExperimentInformation> getDuplicated(List<File> files) throws Exception 
    {
        HashMap<String, List<File>> info = getDuplicatedFilesbySequence(files);

        ArrayList<String> sequences = new ArrayList(info.keySet());

        List<ExperimentInformation> exp = new ArrayList<>();

        int sequenceID = 1;

        for (String sequence : sequences) 
        {
            List<File> duplicatedFiles = info.get(sequence);

            for (File file : duplicatedFiles) 
            {
                exp.add(getInformation(file, String.valueOf(sequenceID)));
            }

            sequenceID++;
        }

        return exp;
    }
    
    public static HashMap<String, List<File>> getDuplicatedFilesbySequence(List<File> files) 
    {
        HashMap<String, List<File>> sequenceVsFiles = new HashMap<>();

        for (File file : files) 
        {
            IChemFile chemFile = PDBFilter.readChemFile(file);

            PDBProtein protein = PDBFilter.getModel(chemFile, 0);

            if (protein != null) 
            {
                String aminoSequence = protein.getAminoSequence();

                List<File> filesNames = sequenceVsFiles.get(aminoSequence);
                //existe la secuencia
                if (filesNames != null) 
                {
                    //almacenar el fichero
                    filesNames.add(file);
                } else 
                {
                    filesNames = new ArrayList<>();

                    filesNames.add(file);

                    sequenceVsFiles.put(aminoSequence, filesNames);
                }
            }
        }

        // delete non-duplicated files
        ArrayList<String> sequences = new ArrayList(sequenceVsFiles.keySet());

        for (String sequence : sequences) 
        {
            if (sequenceVsFiles.get(sequence).size() == 1) 
            {
                sequenceVsFiles.remove(sequence);
            }
        }

        return sequenceVsFiles;
    }
}
