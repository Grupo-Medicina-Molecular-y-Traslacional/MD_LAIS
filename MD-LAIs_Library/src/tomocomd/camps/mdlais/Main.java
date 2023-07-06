package tomocomd.camps.mdlais;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nu.xom.ParsingException;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptorFactory;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptorReader;
import tomocomd.camps.mdlais.exceptions.TomocomdException;
import tomocomd.camps.mdlais.workers.ProteinCalculatorWorker4BM;
import tomocomd.camps.mdlais.workers.output.OutputFormats;

/**
 *
 * @author Cesar
 * @author Ernesto
 */
public class Main {

    static public void main(String[] arg0) throws IOException, ParsingException, TomocomdException {

        System.out.println("==MD-LAIs CLI Interface==");
        
        File root = new File(System.getProperty("user.dir"));

        File datasets = new File(root, "datasets");
        
        System.out.println("==Protein datasets==");

        //LOAD datasets FOLDERS ONLY
        File[] proteinDatasetFolders = datasets.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                
                File directory = new File(datasets,name);
                
                if(directory.isDirectory())
                {
                    System.out.println(directory.getName());
                }
                return directory.isDirectory();
            }
        });
        
        System.out.println("==Protein datasets==");

        System.out.println();

        
        System.out.println("==Projects==");

        File projectFolder = new File(root, "projects");

        File[] projects = projectFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".xml") || name.endsWith(".csv")) {
                    System.out.println(name);
                }

                return name.endsWith(".xml") || name.endsWith(".csv");
            }
        });

        System.out.println("==Projects==");

        //  Check for projects
        boolean checkProjects = checkProjectFiles(projects);

        if (!checkProjects) {
            return;
        }
        // Sort project files for unix environments
        List<File> projectsList = new ArrayList<>();

        if (projects != null && projects.length > 0) {
            projectsList = Arrays.asList(projects);

            Collections.sort(projectsList);
        }

        System.out.println();
        long ti = System.currentTimeMillis();
        System.out.println();
        System.out.println("Starting time: " + ti);

        int availableProcessors = (arg0 != null && arg0.length == 1) ? Integer.parseInt(arg0[0])
                : Runtime.getRuntime().availableProcessors();

        System.out.println("Available processors: " + availableProcessors);

        // Execute aminoacid only datasets
        execute(availableProcessors, proteinDatasetFolders, projectsList);

        long tf = System.currentTimeMillis();

        File timeRecord = new File(root, "time.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(timeRecord, false));
        bw.write("processors: " + availableProcessors + "\r" + "\n");
        bw.write("Total time(s): " + ((tf - ti) / 1000));
        bw.close();

        System.out.println("Final time: " + tf);
        System.out.println("Total time (ms): " + (tf - ti));
        System.out.println("Total time (s): " + ((tf - ti) / 1000));
        System.out.println("Total time (min): " + (((tf - ti) / 1000)) / 60);
    }

    public static void execute(int availableProcessors, File[] proteinDatasetsFolders, List<File> projectsList) throws IOException, ParsingException, TomocomdException {
        for (File currentDirectory : proteinDatasetsFolders) {
            File[] proteins = currentDirectory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".fasta")) {
                        System.out.println(name);
                    }

                    return name.endsWith(".fasta");
                }
            });

            if (proteins.length > 0) {
                int currentProject = 1;

                int totalProjects = projectsList.size();

                File parent = currentDirectory.getParentFile();
                
                File outputFolder = new File(parent.getParent(), "OUTPUT");

                outputFolder.mkdir();

                for (File project : projectsList) 
                {
                    File output = new File(outputFolder.getPath() + File.separatorChar, currentDirectory.getName() + "_" + project.getName().substring(0, project.getName().lastIndexOf(".")) + ".csv");

                    MolecularDescriptorReader adr = new MolecularDescriptorReader();

                    ProteinCalculatorWorker4BM mc4bm = null;

                    List<IDescriptor> descriptorsList = null;
                    Object[] parameters = null;
                    
                    if (project.getName().endsWith(".csv")) 
                    {
                        descriptorsList = MolecularDescriptorReader.getAlgebraicDescriptorListFromList(project);
                        
                        parameters =  new Object[6];                    
                        parameters[3] = false;
                        parameters[5] = true;
                        
                        mc4bm = new ProteinCalculatorWorker4BM(descriptorsList, proteins, output,
                                OutputFormats.OUTPUT_CSV, availableProcessors,
                                parameters, adr.getCutConfig());
                    } else if (project.getName().endsWith(".xml")) 
                    {
                        adr.readProjectFile(project);

                        descriptorsList = new ArrayList<>();

                        descriptorsList.addAll(MolecularDescriptorFactory.getAlgebraicDescriptorsFromReader(adr));

                        parameters = adr.getNewParameters();

                        parameters[2] = currentDirectory.getPath() + File.separatorChar;
                        parameters[5] = false;     
                        
                        mc4bm = new ProteinCalculatorWorker4BM(descriptorsList, proteins, output,
                                OutputFormats.OUTPUT_CSV, availableProcessors,
                                parameters, adr.getCutConfig());
                    }

                    mc4bm.go();

                    System.out.println("Processed : " + currentProject + " of " + totalProjects + " projects" + "\n");

                    currentProject++;
                }
            }

            System.out.println("Processed : " + currentDirectory.getName() + " dataset" + "\n");
        }
    }

    private static boolean checkProjectFiles(File[] projects) {
        String wrongFormatFiles = "";

        System.out.println("Total: " + projects.length + " " + "projects");

        for (File project : projects) 
        {
            if (project.getName().endsWith(".xml")) 
            {

                MolecularDescriptorReader adr = new MolecularDescriptorReader();

                try 
                {
                    adr.readProjectFile(project);

                } catch (Exception e) 
                {
                    wrongFormatFiles += "\r\n" + project.getName();
                }
            } else if (project.getName().endsWith(".csv")) 
            {
                List<IDescriptor> descriptors = MolecularDescriptorReader.getAlgebraicDescriptorListFromList(project);

                if (descriptors.isEmpty()) 
                {
                    wrongFormatFiles += project.getName();
                }
            }
        }

        if (wrongFormatFiles.length() > 0) 
        {
            System.out.println("Please, fix the format of the following projects to continue with calculations: " + wrongFormatFiles);

            return false;
        }

        return true;
    }
}
