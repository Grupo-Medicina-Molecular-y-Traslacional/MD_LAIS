package tomocomd.camps.mdlais.workers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptor;
import tomocomd.camps.mdlais.exceptions.ExceptionInfo;
import tomocomd.camps.mdlais.exceptions.TomocomdException;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.pdbfilter.PDBFilter;
import tomocomd.camps.mdlais.workers.output.ARFFTextOutput;
import tomocomd.camps.mdlais.workers.output.ITextOutput;
import tomocomd.camps.mdlais.workers.output.OutputFormats;
import tomocomd.camps.mdlais.workers.output.PlainTextOutput;

/**
 * @author Rajarshi Guha
 * @author Cesar
 */
public class ProteinCalculatorWorker4BM implements ISwingWorker {

    private final int procs;
    private int current;
    private int proteinCount;
    private final int cThreads;
    private final int lengthOfTask;
    private final int[] currentDescriptor;

    private boolean done;
    private boolean canceled;
    private final boolean isAALevel,isAALevelList;
    private boolean isPrintHeader;
    private ITextOutput textOutput;
    private ITextOutput aatextOutput;

    private BufferedWriter outWriter;
    private BufferedWriter aaoutWriter;

    private String inputFormat;
    private final String outputFormat;
    private final File ouputFile;
    private final File[] filenames;
    private final ArrayList<String> moleculesName;
    private final ArrayList<String> descriptorsName;

    private final WeightConfiguration cutConfig;
    private final List<IDescriptor> descriptors;
    private final List<ExceptionInfo> exceptionList;

    private final Object[] uiNewParameters;

    public ProteinCalculatorWorker4BM(
            List<IDescriptor> descriptors, File[] nameInputFiles, File ouputFile, String outputFormat, int procs,
            Object[] uiNewParameters,
            WeightConfiguration cutConfig) throws TomocomdException {
        current = 0;
        proteinCount = 0;
        cThreads = 0;
        lengthOfTask = 1;
        currentDescriptor = new int[]{0};

        done = false;
        canceled = false;

        isPrintHeader = false;
        outWriter = null;
        aaoutWriter = null;

        this.descriptors = descriptors;
        this.ouputFile = ouputFile;
        this.outputFormat = outputFormat;
        this.cutConfig = cutConfig;
        this.procs = procs;
        this.uiNewParameters = uiNewParameters;
        this.isAALevel = uiNewParameters!=null?(boolean) uiNewParameters[3]:false;
        this.isAALevelList = isAALevel(descriptors);
        exceptionList = new ArrayList<>();
        descriptorsName = new ArrayList<>();
        moleculesName = new ArrayList<>();

        inputFormat = "invalid";

        for (File molFile : (filenames = nameInputFiles)) {
            if (molFile.exists()) 
            {
                String ext = getFileExtension(molFile.getName());
                
                if (ext.equalsIgnoreCase("fastax"))
                {
                    inputFormat = "fastax";
                }
                
                if (ext.equalsIgnoreCase("fasta"))
                {
                    inputFormat = "fasta";
                }
            }
        }

        // sort for unix systems
        List<File> fileOrdered = new ArrayList<>();

        fileOrdered.addAll(Arrays.asList(filenames));

        Collections.sort(fileOrdered);

        for (int i = 0; i < fileOrdered.size(); i++) {
            filenames[i] = fileOrdered.get(i);
        }

        if (inputFormat.equals("invalid")) {
            done = true;

            canceled = true;

            throw new TomocomdException("Input file format was not recognized. Accepted file formats for MD-LAIs program is fasta"
            );
        }
    }

    private String getFileExtension(String filename) {
        String ext = "";
        int p = filename.length() - 1;
        while (p >= 0) {
            char c = filename.charAt(p);
            if (c != '.') {
                ext = c + ext;
            } else {
                return ext;
            }
            p--;
        }
        return ext;
    }

    @Override
    public void go() {
        current = 0;
        done = false;
        canceled = false;
        try {
            new ActualTask().doTask();
        } catch (Exception ex) {
            Logger.getLogger(ProteinCalculatorWorker4BM.class.getName()).log(Level.SEVERE, null, ex);

            exceptionList.add(new ExceptionInfo(current, null, ex, ex.getMessage()));
        }
    }

    @Override
    public List<ExceptionInfo> getExceptionList() {
        return exceptionList;
    }

    public ArrayList<String> getMolecules() {
        return moleculesName;
    }

    public ArrayList<String> getDescriptors() {
        return descriptorsName;
    }

    public int getCurrentDescriptor() {
        return currentDescriptor[0];
    }

    @Override
    public String getInputFormat() {
        return inputFormat;
    }

    @Override
    public int getLengthOfTask() {
        return lengthOfTask;
    }

    @Override
    public int getCurrent() {
        return proteinCount;
    }

    @Override
    synchronized public void stop() {
        canceled = true;
        currentDescriptor[0] = 0;

        ProteinCalculatorTask.canceled = true;
        if (pool != null) {
            pool.shutdown();
            pool.shutdownNow();
        }

        try {
            outWriter.flush();
            outWriter.close();
            outWriter = null;

           if (isAALevel||isAALevelList) {
                aaoutWriter.flush();
                aaoutWriter.close();
                aaoutWriter = null;
            }

        } catch (Exception e) {

        }
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    public void cancelProcess() {
        canceled = true;
    }

    public int getCountSubSets() {
        return cThreads;
    }
    
    public boolean isAA_Level(IDescriptor desc) 
    {
        String classicInv = ((MolecularDescriptor)desc).getClassicInv();
        
        if(classicInv.equals("sic")||classicInv.equals("tic")||classicInv.equals("sicn")||classicInv.equals("ticn")){
            return false;
        }
        String noClassicInv = ((MolecularDescriptor)desc).getNoClassicInv();
        return noClassicInv.isEmpty()||noClassicInv.contains("[");
    }
    
    public boolean isAALevel(List<IDescriptor> descriptors)
    {
        for (IDescriptor descriptor: descriptors)
        {
            if(isAA_Level(descriptor))
            {
               return true;
            }
        }
        return false;
    }

    private ForkJoinPool pool;

    class ActualTask {

        private final List<String> dataItems;
        private final List<String> headerItems;
        private final List<List<String>> aaDataitems;
        private final List<String> aaHeaderItems;

        ActualTask() throws CDKException, Exception {
            dataItems = new ArrayList<>();
            headerItems = new ArrayList<>();
            aaDataitems = new ArrayList<>();
            aaHeaderItems = new ArrayList<>();
        }

        void doTask() {
            switch (outputFormat) {
                case OutputFormats.OUTPUT_TAB:
                case OutputFormats.OUTPUT_CSV:
                case OutputFormats.OUTPUT_ARFF:
                case OutputFormats.OUTPUT_SPC: {
                    boolean status = evalToTextFile();
                    if (status) {
                        done = true;
                    } else {
                        canceled = true;
                    }
                    break;
                }
            }
        }

        private boolean evalToTextFile() {
            try {
                outWriter = new BufferedWriter(new FileWriter(ouputFile));

                if (isAALevel||isAALevelList) {

                    String suffix = "_AA.";

                    switch (outputFormat) {
                        case "CSV":
                            suffix += "csv";
                            break;
                        case "TAB":
                            suffix += "txt";
                            break;
                        case "ARFF":
                            suffix += "arff";
                            break;
                    }

                    String out = ouputFile.getName().substring(0, ouputFile.getName().lastIndexOf(".")) + suffix;

                    aaoutWriter = new BufferedWriter(new FileWriter(new File(ouputFile.getParentFile(), out)));
                }
            } catch (IOException exception) {
                exceptionList.add(new ExceptionInfo(proteinCount, null, exception, "Output file error " + outputFormat + " is either currently in use by another program or an invalid file path was chosen"));
                return false;
            }

            pool = new ForkJoinPool();
            ProteinCalculatorTask task = new ProteinCalculatorTask();
            task.setHeaderItems(headerItems);
            task.setAaHeaderItems(aaHeaderItems);
            task.setInmutableAtrr(true, uiNewParameters, cutConfig, exceptionList, descriptors, descriptorsName, currentDescriptor, null);
            task.setIsAALevelList(isAALevelList);
            try {
                // read properties
                MolecularDescriptor.readProperties(procs);
            } catch (IOException ex) {
                exceptionList.add(new ExceptionInfo(0, null, ex, "Error reading properties"));
            }

            for (File molFile : filenames) {
                //<editor-fold defaultstate="collapsed" desc="INPUT & OUTPUT Setup">
                if (!molFile.exists()) {
                    continue;
                }

                IMoleculeSet molSet = null;
                
                String title = null;

                IMolecule molecule = null;
                
                if (inputFormat.equalsIgnoreCase("fasta")||(inputFormat.equalsIgnoreCase("fastax")))
                {
                    try {
                        molSet = PDBFilter.fastaFile2PDBList(molFile);
                    } catch (Exception ex) {
                        Logger.getLogger(ProteinCalculatorSwingWorker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                int moleculeCount = molSet.getAtomContainerCount();
                                
                for (int k = 0; k < moleculeCount; k++) 
                {
                    molecule = (IMolecule) molSet.getAtomContainer(k);
                    
                    int atomCount = molecule.getAtomCount();
                
                for (int i = 0; i < atomCount - 1; i++) 
                {
                    molecule.addBond(new Bond(new IAtom[]{molecule.getAtom(i), molecule.getAtom(i + 1)},
                        IBond.Order.DOUBLE));
                }

                switch (outputFormat) {
                    case OutputFormats.OUTPUT_TAB:
                        textOutput = new PlainTextOutput(outWriter);
                        textOutput.setItemSeparator("\t");

                        if (isAALevel||isAALevelList) {
                            aatextOutput = new PlainTextOutput(aaoutWriter);
                            aatextOutput.setItemSeparator("\t");
                        }
                        break;
                    case OutputFormats.OUTPUT_CSV:
                        textOutput = new PlainTextOutput(outWriter);
                        textOutput.setItemSeparator(",");
                        if (isAALevel||isAALevelList) {
                            aatextOutput = new PlainTextOutput(aaoutWriter);
                            aatextOutput.setItemSeparator(",");
                        }
                        break;
                    case OutputFormats.OUTPUT_SPC:
                        textOutput = new PlainTextOutput(outWriter);
                        textOutput.setItemSeparator(" ");
                       if (isAALevel||isAALevelList) {
                            aatextOutput = new PlainTextOutput(aaoutWriter);
                            aatextOutput.setItemSeparator(" ");
                        }
                        break;
                    case OutputFormats.OUTPUT_ARFF:
                        textOutput = new ARFFTextOutput(outWriter);
                        if (isAALevel||isAALevelList) {
                            aatextOutput = new ARFFTextOutput(aaoutWriter);
                        }
                        break;
                }

                title = molFile.getName() + "_" + (int) (proteinCount + 1);
                molecule.setProperty(CDKConstants.TITLE, title);

                //</editor-fold>
                headerItems.add("proteins");

                if (isAALevel||isAALevelList) {
                    aaHeaderItems.add("aminoacids");
                }
                moleculesName.add(title);
                dataItems.add(title);

                int dim = molecule == null ? 0 : molecule.getAtomCount();

                if (isAALevel||isAALevelList) {
                    for (int i = 0; i < dim; i++) {
                        PDBAtom atom = (PDBAtom) molecule.getAtom(i);

                        List<String> aa = new ArrayList<>(dim);
                        aa.add(title + "_" + atom.getResName() + "_" + atom.getResSeq());
                        aaDataitems.add(aa);
                    }
                }

                ProteinCalculatorTask.canceled = false;

                MolecularDescriptor.createGlobalVariables(procs, dim);

                for (int i = 0; i < descriptors.size();) 
                {
                    int end = ((i + procs) - 1) < descriptors.size() ? procs : descriptors.size() - i;
                    
                    try {
                        task.setVolatileAtrr(proteinCount, molecule, i, (end + i) - 1, dataItems, aaDataitems);
                        task.reinitialize();
                        pool.invoke(task);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }

                    i += end;

                    if (canceled) {
                        return false;
                    }
                }

                //set mol count 
                proteinCount++;

                // reset descriptors count for next mol
                currentDescriptor[0] = 0;

                try {
                    assert textOutput != null;
                    if (!isPrintHeader) {
                        textOutput.writeHeader(headerItems);

                        if (isAALevel||isAALevelList) {
                            aatextOutput.writeHeader(aaHeaderItems);
                        }
                        isPrintHeader = true;
                    }

                    textOutput.writeLine(dataItems);
                    outWriter.flush();

                   if (isAALevel||isAALevelList) {
                        aatextOutput.writeBlock(aaDataitems);
                        aaoutWriter.flush();
                    }
                } catch (IOException e) {
                    exceptionList.add(new ExceptionInfo(proteinCount, molecule, e, "Error Writing Header Line"));
                }

                dataItems.clear();
                headerItems.clear();

                if (isAALevel||isAALevelList) {
                    aaDataitems.clear();
                }
                }                
            }
            // destroy properties
            MolecularDescriptor.destroyProperties();

            pool.shutdownNow();
            pool = null;

            try {
                outWriter.close();
                outWriter = null;

                if (isAALevel||isAALevelList) {
                    aaoutWriter.close();
                    aaoutWriter = null;
                }
            } catch (IOException e) 
            {
                exceptionList.add(new ExceptionInfo(proteinCount, null, e, "Error closing the output writer"));
            }

            return !canceled;
        }
    }//end actual task class 
}
