
package tomocomd.camps.mdlais.pdbfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author econtreras
 */
public class PDBDuplicateCheckerIdentifier extends PDBDuplicate
{
    @Override
    public List<ExperimentInformation> getDuplicated(List<File> files) throws Exception 
    {
        HashMap<String, List<File>> info = getDuplicatedFilesbyId(files);

        ArrayList<String> ids = new ArrayList(info.keySet());

        List<ExperimentInformation> experimentInformation = new ArrayList<>();

        for (String id : ids) 
        {
            List<File> duplicatedFiles = info.get(id);

            for (File file : duplicatedFiles) 
            {
                experimentInformation.add(getInformation(file, id));
            }
        }
        
       return experimentInformation;
    }
    private HashMap<String, List<File>> getDuplicatedFilesbyId(List<File> files) throws Exception 
    {
        HashMap<String, List<File>> idVsFiles = new HashMap<>();

        for (File file : files) 
        {
            String pdbId = getId(file);

            if (pdbId != null) 
            {
                List<File> filesNames = idVsFiles.get(pdbId);

                //existe el id
                if (filesNames != null) 
                {
                    //almacenar el fichero
                    filesNames.add(file);
                } else 
                {
                    filesNames = new ArrayList<>();

                    filesNames.add(file);

                    idVsFiles.put(pdbId, filesNames);
                }
            }
        }

        // delete non-duplicated files
        
        ArrayList<String> ids = new ArrayList(idVsFiles.keySet());

        for (String id : ids) 
        {
            if (idVsFiles.get(id).size() == 1) 
            {
                idVsFiles.remove(id);
            }
        }

        return idVsFiles;
    }
    
    public String getId(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine();

        boolean found = false;

        String pdbId = null;

        while (line != null && !found) {
            if (line.startsWith("HEADER")) {
                String cCol = line.substring(0, 6);

                if ("HEADER".equalsIgnoreCase(cCol)) {
                    pdbId = line.substring(62, 67).trim();

                    found = true;
                }
            }

            line = br.readLine();
        }

        br.close();

        return pdbId;
    }
}
