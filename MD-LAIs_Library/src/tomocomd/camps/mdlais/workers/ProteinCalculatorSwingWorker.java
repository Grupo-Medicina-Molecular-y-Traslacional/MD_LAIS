package tomocomd.camps.mdlais.workers;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.qsar.IDescriptor;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptor;
import tomocomd.camps.mdlais.pdbfilter.PDBFilter;
import tomocomd.camps.mdlais.exceptions.ExceptionInfo;
import tomocomd.camps.mdlais.exceptions.TomocomdException;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.workers.output.ARFFTextOutput;
import tomocomd.camps.mdlais.workers.output.ITextOutput;
import tomocomd.camps.mdlais.workers.output.OutputFormats;
import tomocomd.camps.mdlais.workers.output.PlainTextOutput;

/**
 * @author Rajarshi Guha
 * @author Cesar
 * @author econtreras
 */
public final class ProteinCalculatorSwingWorker implements ISwingWorker {

    private int processors;
    private int current;
    private int proteinCount;
    private int cThreads;
    private int lengthOfTask;
    private int totalDescriptors;
    private int[] currentDescriptor;

    private boolean done;
    private boolean canceled;
    private boolean isAALevel,isAALevelList;
    private int windowSize;

    private boolean isPrintHeader;
    private ITextOutput textOutput;
    private ITextOutput aatextOutput;
    private BufferedWriter outWriter;
    private BufferedWriter aaoutWriter;

    private String ouputFile;
    private String inputFormat;
    private String outputFormat;
    private String[] filenames;
    private ArrayList<String> moleculesName;
    private ArrayList<String> descriptorsName;

    private WeightConfiguration cutConfig;
    private List<IDescriptor> descriptors;
    private List<ExceptionInfo> exceptionList;

    private Object[] uiNewParameters;
    
    private JTextArea textArea;
    private JProgressBar progressBar1;
    private JProgressBar progressBar2;
    private ITaskFinishedListener taskFinish;
    
    public ProteinCalculatorSwingWorker(JProgressBar progressBar1, JProgressBar progressBar2, JTextArea textArea, ITaskFinishedListener taskFinish,
            List<IDescriptor> descriptors, String nameInputFiles, String ouputFile, String outputFormat, int procs,
            int classDescriptors, int totalDescriptors,Object[] uiNewParameters, WeightConfiguration cutConfig) throws TomocomdException {
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

        this.progressBar1 = progressBar1;
        this.progressBar1.setIndeterminate(false);
        this.progressBar1.setPreferredSize(progressBar1.getSize());

        this.progressBar2 = progressBar2;
        this.progressBar2.setIndeterminate(false);
        this.progressBar2.setPreferredSize(progressBar2.getSize());

        this.textArea = textArea;
        this.taskFinish = taskFinish;

        this.descriptors = descriptors;
        this.ouputFile = ouputFile;
        this.outputFormat = outputFormat;
        this.cutConfig = cutConfig;
        this.processors = procs;
        this.totalDescriptors = totalDescriptors;
        this.uiNewParameters = uiNewParameters;
        this.isAALevel = (boolean) uiNewParameters[3];
        this.isAALevelList = isAALevel(descriptors);
        this.windowSize = (int) uiNewParameters[4];
        exceptionList = new ArrayList<>();
        descriptorsName = new ArrayList<>();
        moleculesName = new ArrayList<>();

        // see what type of file we have
        inputFormat = "invalid";

        filenames = nameInputFiles.split(";");

        for (int i = 1; i < filenames.length; i++) {
            if (filenames[i].isEmpty()) {
                continue;
            } else {
                if (getFileExtension(filenames[i]).equalsIgnoreCase("fasta")) {
                    inputFormat = "fasta";
                }
                
                if (getFileExtension(filenames[i]).equalsIgnoreCase("fastax")) {
                    inputFormat = "fastax";
                }
            }
        }

        if (inputFormat.equals("invalid")) 
        {
            done = true;
            canceled = true;

            throw new TomocomdException("Input file format was not recognized. The accepted file format for MD-LAIs is fasta"
                    + "\n");
        }

        // Sort file names lexicography for unix systems
        List<String> sortedFileNames = new ArrayList<>();

        for (int i = 1; i < filenames.length; i++) {
            sortedFileNames.add(filenames[i]);
        }

        Collections.sort(sortedFileNames);

        int index = 0;

        for (int i = 1; i < filenames.length; i++) {
            filenames[i] = sortedFileNames.get(index++);
        }

        textArea.append("\r\nDescriptor(s) Size (Class): " + classDescriptors);
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
        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                current = 0;
                done = false;
                canceled = false;
                try {
                    new ActualTask().doTask();
                    if (!canceled && taskFinish != null) {
                        taskFinish.notifyTaskFinished(false);
                        currentDescriptor[0] = 0;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ProteinCalculatorSwingWorker.class.getName()).log(Level.SEVERE, null, ex);
                    exceptionList.add(new ExceptionInfo(current, null, ex, ex.getMessage()));
                }
            }
        });
        workerThread.start();
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
        private final List<List<String>> aaDataItems;
        private final List<String> aaHeaderItems;

        ActualTask() throws CDKException, Exception {
            dataItems = new ArrayList<>();
            headerItems = new ArrayList<>();
            aaDataItems = new ArrayList<>();
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
            progressBar1.setOpaque(true);
            progressBar1.setStringPainted(true);
            progressBar1.setString("Preparing Output File...");

            try {
                outWriter = new BufferedWriter(new FileWriter(ouputFile));

                if (isAALevel||isAALevelList) 
                {
                    String out = ouputFile.substring(0, ouputFile.lastIndexOf(".")) + "_AA" + ouputFile.substring(ouputFile.lastIndexOf("."), ouputFile.length());

                    aaoutWriter = new BufferedWriter(new FileWriter(out));
                }
            } catch (IOException exception) 
            {
                exceptionList.add(new ExceptionInfo(proteinCount, null, exception, "Output file error " + outputFormat + " is either currently in use by another program or an invalid file path was chosen"));
                return false;
            }

            pool = new ForkJoinPool();
            ProteinCalculatorTask task = new ProteinCalculatorTask();
            task.setHeaderItems(headerItems);
            task.setAaHeaderItems(aaHeaderItems);
            task.setInmutableAtrr(false, uiNewParameters, cutConfig, exceptionList, descriptors, descriptorsName, currentDescriptor, progressBar2);
            task.setIsAALevelList(isAALevelList);
            
            int maxProgress1 = 0;

            for (int f = 1; f < filenames.length; f++) {
                if (filenames[f].isEmpty()) {
                    continue;
                }

                maxProgress1++;
            }

//            progressBar1.setMaximum(maxProgress1);

            try {
                // read properties
                MolecularDescriptor.readProperties(processors);
            } catch (IOException ex) {
                exceptionList.add(new ExceptionInfo(0, null, ex, "Error reading properties"));
            }

            for (int f = 1; f < filenames.length; f++) {
                String title = null;
                IMoleculeSet molSet = null;
                
                IMolecule molecule = null;

                if (filenames[f].isEmpty()) {
                    continue;
                }

                //<editor-fold defaultstate="collapsed" desc="INPUT & OUTPUT Setup">
                File file = new File(filenames[0] + File.separatorChar + filenames[f]);
                
                if (inputFormat.equalsIgnoreCase("fasta")||(inputFormat.equalsIgnoreCase("fastax")))
                {
                    try {
                        molSet = PDBFilter.fastaFile2PDBList(file);
                    } catch (Exception ex) 
                    {
                        exceptionList.add(new ExceptionInfo(f, molecule, ex, title));
                        
                        Logger.getLogger(ProteinCalculatorSwingWorker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                int moleculeCount = molSet.getAtomContainerCount();
                
                progressBar1.setMaximum(moleculeCount);
                        
                for (int k = 0; k < moleculeCount; k++) 
                {
                    molecule = (IMolecule) molSet.getAtomContainer(k);
                    
                    int atomCount = molecule.getAtomCount();
                
                for (int l = 0; l < atomCount - 1; l++) 
                {
                    molecule.addBond(new Bond(new IAtom[]{molecule.getAtom(l), molecule.getAtom(l + 1)},
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
                    //</editor-fold>
                    headerItems.add("proteins");

                    if (isAALevel||isAALevelList) {
                        aaHeaderItems.add("aminoacids");
                    }
                    title = filenames[f] + "_" + (int) (proteinCount + 1); // obtengo el pdbID
                    molecule.setProperty(CDKConstants.TITLE, title);
                    
                textArea.append("\r\nProtein Name: " + title);

                progressBar1.setOpaque(false);
                progressBar1.setStringPainted(true);
                progressBar1.setString(title + " " + ((long) (((double) progressBar1.getValue()) / progressBar1.getMaximum() * 100) + "%"));

                try {
                    outWriter.flush();

                    if (isAALevel||isAALevelList) {
                        aaoutWriter.flush();
                    }
                } catch (IOException ex) {
                    exceptionList.add(new ExceptionInfo(proteinCount, molecule, ex, "Error Flush Buffer"));
                    textArea.append("\r\nError Flush Buffer");
                }

                progressBar2.setMaximum(totalDescriptors);
                moleculesName.add(title);
                dataItems.add(title);

                int dim = molecule.getAtomCount();

                if (isAALevel||isAALevelList) 
                {
                    for (int i = 0; i < dim; i++) 
                    {
                        PDBAtom atom = (PDBAtom) molecule.getAtom(i);
                        List<String> aa = new ArrayList<>(dim);
                        aa.add(title + "_" + atom.getResName() + "_" + atom.getResSeq());
                        aaDataItems.add(aa);
                    }
                }
                
                ProteinCalculatorTask.canceled = false;
                MolecularDescriptor.createGlobalVariables(processors, dim);                
                task.setTasks(null);

                for (int i = 0; i < descriptors.size();) 
                {
                       int end =  ((i + processors) - 1) < descriptors.size() ? processors : descriptors.size() - i;
                    try {
                        task.setVolatileAtrr(proteinCount, molecule, i, (end + i) - 1, dataItems, aaDataItems);
                        task.reinitialize();
                        pool.invoke(task);
                    } catch (Throwable ex) {
                        if (ex instanceof OutOfMemoryError) 
                        {
                            exceptionList.add(new ExceptionInfo(proteinCount, molecule, new Exception("OutOfMemoryError"), ""));
                        }

                        ex.printStackTrace();
                    }

                    i += end;

                    if (canceled) {
                        return false;
                    }

                    textArea.append(task.getHistory().toString());
                }
                
                 textArea.append("\r\nProtein was Calculated");

                //set protein count 
                proteinCount++;

                // reset descriptors count for next mol
                currentDescriptor[0] = 0;
                progressBar2.setString("");
                progressBar2.setValue(currentDescriptor[0]);

                progressBar1.setValue(k+1);
                progressBar1.setString(title + " " + ((long) (((double) progressBar1.getValue()) / progressBar1.getMaximum() * 100) + "%"));

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
                        aatextOutput.writeBlock(aaDataItems);
                        aaoutWriter.flush();
                    }
                } catch (IOException e) {
                    exceptionList.add(new ExceptionInfo(proteinCount, molecule, e, "Error Writing Header Line"));
                    textArea.append("\r\nError Writing Header Line");
                }

                dataItems.clear();
                headerItems.clear();
                if (isAALevel||isAALevelList) {
                    aaHeaderItems.clear();
                    aaDataItems.clear();
                }
                molecule = null;
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

            } catch (IOException e) {
                exceptionList.add(new ExceptionInfo(proteinCount, null, e, "Error closing the output writer"));
                textArea.append("\r\nError Closing the Output Writer");
            }
            if (canceled) {
                return false;
            } else {
                return true;
            }  
        }
    }
}
