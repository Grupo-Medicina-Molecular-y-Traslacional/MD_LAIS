/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tomocomd.camps.mdlais.pdbfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 *
 * @author Dracus
 */
public abstract class PDBDuplicate 
{
    public abstract List<ExperimentInformation> getDuplicated(List<File> files) throws Exception ;
    
    final protected ExperimentInformation getInformation(File file, String seqID) throws Exception 
    {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine();

        boolean found = false;

        String technique = "N/A";

        String resolution = "N/A";

        int remarkNumber = 0;

        while (line != null && !found) {
            if (line.startsWith("EXPDTA")) {
                try {
                    technique = line.substring(10, line.length()).trim();

                } catch (Exception e) {

                }
            }

            if (line.length() > 6 && line.substring(0, 6).equalsIgnoreCase("REMARK")) {
                if (line.charAt(9) == '2') {
                    remarkNumber++;
                }

                if (remarkNumber == 2) {
                    found = true;
                    try {
                        resolution = line.substring(23, 31).trim();
                    } catch (Exception e) {

                    }
                }
            }

            line = br.readLine();
        }

        br.close();

        return new ExperimentInformation(seqID, file.getName(), technique, resolution);
    }
}
