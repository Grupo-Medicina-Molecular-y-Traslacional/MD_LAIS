package tomocomd.camps.mdlais.pdbfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.vecmath.Point3d;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.protein.data.PDBMonomer;
import tomocomd.camps.mdlais.io.PDBReader;
import tomocomd.camps.mdlais.io.PDBWriter;
import tomocomd.camps.mdlais.protein.PDBProtein;

/**
 *
 * @author econtreras
 */
public class PDBFilter {

    private List<File> files;// archivos pdb

    private HashMap<String, PDBDataFile> filesInformation;// información de cada fichero

    private HashMap<File, ReadConfiguration> readConfigurations;// configuraciones de lectura de cada fichero

    private List<String> invalidFormatFiles;// ficheros no válidos

    public PDBFilter() {
        this.files = new ArrayList<>();

        this.filesInformation = new HashMap<>();

        this.readConfigurations = new HashMap<>();

        this.invalidFormatFiles = new ArrayList<>();
    }

    public ReadConfiguration getFileReadConfiguration(File file) {
        return readConfigurations.get(file);
    }

    public void addFileReadConfiguration(File file, ReadConfiguration config) {
        this.readConfigurations.put(file, config);
    }

    public List<File> getFiles() {
        return files;
    }

    public List<String> getInvalidFormatFiles() {
        return invalidFormatFiles;
    }

    //get file information
    public PDBDataFile getFileInformation(int dataIndex) {
        //devuelve la info de un archivo
        return filesInformation.get(files.get(dataIndex).getName());
    }

    public File getFile(String name) {
        for (File file : files) {
            if (file.getName().equalsIgnoreCase(name)) {
                return file;
            }
        }

        return null;
    }

    //get file information
    public PDBDataFile getFileInformation(String fileName) {
        return filesInformation.get(fileName);
    }

    //load files information
    public void loadFilesInformation() {
        // procesar todos los archivos 

        PDBReader pdbReader = new PDBReader();

        List<File> validFiles = new ArrayList<>();

        int progress = 0;

        CAAtomFilter atomFilter = new CAAtomFilter();

        for (File file : files) {
            PDBDataFile dataFile = pdbReader.scanFile(file);

            if (dataFile != null) {
                filesInformation.put(file.getName(), dataFile);

                validFiles.add(file);

                readConfigurations.put(file, new ReadConfiguration(0, dataFile.getStrands().subList(0, 1), atomFilter));
            } else {
                invalidFormatFiles.add(file.getName());
            }

            progress++;

            System.out.println("processed " + progress + " of" + files.size());
        }

        files = validFiles;
    }

    // read the file information
    public void loadFileInformation(File file) {
        // procesar un archivo

        PDBReader pdbReader = new PDBReader();

        PDBDataFile dataFile = pdbReader.scanFile(file);

        if (dataFile != null) {
            filesInformation.put(file.getName(), dataFile);

            readConfigurations.put(file, new ReadConfiguration(0, dataFile.getStrands().subList(0, 1), new CAAtomFilter()));

            files.add(file);

        } else {
            invalidFormatFiles.add(file.getName());
        }
    }

// devuelve los datos de un fichero si existen
    public PDBDataFile getDataFile(String fileName) {
        return filesInformation.get(fileName);
    }

    public static IChemFile readFile(File inputFile) {
        return readChemFile(inputFile);
    }

    public void generateRepresentations(List<String> descriptors, File[] pdbFiles, File output) throws FileNotFoundException, CDKException, IOException, Exception {
        List<AtomFilterType> representations = new ArrayList<>();

        List<String> acronyms = new ArrayList<>(5);

        for (String descriptor : descriptors) {
            String rep = descriptor.split("_")[0];

            if (!acronyms.contains(rep)) {
                acronyms.add(rep);
            }
        }

        for (String rep : acronyms) {
            switch (rep) {
                case "CA":
                    representations.add(AtomFilterType.CA);
                    break;

                case "CB":
                    representations.add(AtomFilterType.CB);
                    break;

                case "AVG":
                    representations.add(AtomFilterType.AVERAGE);
                    break;

                case "AB":
                    representations.add(AtomFilterType.AB);
                    break;

                case "AVR":
                    representations.add(AtomFilterType.AVR);
                    break;
            }
        }

        // por cada archivo escanearlo para obtener su informacion
        for (File file : pdbFiles) {
            PDBDataFile dataFile = new PDBReader().scanFile(file);

            if (dataFile != null) {

                for (AtomFilterType atomFilter : representations) {

                    File repFolder = new File(output, AtomFilterTool.getRepresentationSymbol(atomFilter));

                    if (!repFolder.exists()) {
                        repFolder.mkdir();
                    }

                    PDBProtein pdbProtein = getFilteredPDB(file, 0, dataFile.getStrands(), atomFilter);

                    PDBWriter.writePDBPolymer(repFolder, pdbProtein, file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdbx");
                }

            } else {
                throw new Exception("PDB file " + file.getName() + " not recognized");
            }
        }
    }

    // MAIN METHOD
    public PDBProtein getFilteredPDB(File file, int model, List<String> strands, AtomFilterType atomFilterType) throws FileNotFoundException, CDKException {
        // Read the pdb file
        IChemFile chemFile = readChemFile(file);

        // Get the model
        PDBProtein protein = getModel(chemFile, model);

        // Get the specified strands
        PDBProtein selectedModel = filterByStrands(protein, strands, AtomFilterFactory.getAtomFilter(atomFilterType));

        selectedModel.setSecondaryStructures(protein.getSecondaryStructures());

        selectedModel.setSsBonds(protein.getSsBonds());

        selectedModel.setProperty(CDKConstants.TITLE, chemFile.getID());

        return selectedModel;
    }

    public PDBProtein getFilteredPDBBM(File file, int model, List<String> strands, AtomFilterType atomFilterType) throws FileNotFoundException, CDKException {
        // Obtener el modelo    
        IChemFile chemFile = readChemFile(file, model, strands, atomFilterType);

        PDBProtein protein = getModel(chemFile, 0);

        if (atomFilterType == AtomFilterType.AVERAGE) {
            protein = getAveragePolymer(protein, new AverageAtomsFilter());
        }

        if (atomFilterType == AtomFilterType.AVR) {
            protein = getAveragePolymer(protein, new AverageRAtomsFilter());
        }

        protein.setProperty(CDKConstants.TITLE, chemFile.getID());

        return protein;
    }

    // leer fichero con una configuracion 
    public PDBProtein getFilteredPDBBM(File file, ReadConfiguration config) throws FileNotFoundException, CDKException {
        IChemFile chemFile = readChemFile(file, config);

        PDBProtein protein = getModel(chemFile, 0);

        if (config.getAtomFilter() instanceof AverageAtomsFilter) {
            protein = getAveragePolymer(protein, (AverageAtomsFilter) config.getAtomFilter());
        }

        protein.setProperty(CDKConstants.TITLE, chemFile.getID());

        protein.getAllAtoms();

        return protein;
    }

    public static IChemFile readChemFile(File file) {
        IChemFile chemFile;

        try {
            chemFile = new PDBReader(new FileInputStream(file)).read(new ChemFile());

        } catch (FileNotFoundException | CDKException ex) {
            chemFile = null;
        }

        return chemFile;
    }

    public static IChemFile readChemFile(File file, int model, List<String> strands, AtomFilterType atomFilterType) {
        IChemFile chemFile;

        try {
            PDBReader pdbReader = new PDBReader();

            pdbReader.setReadConfig(new ReadConfiguration(model, strands, AtomFilterFactory.getAtomFilter(atomFilterType)));

            pdbReader.setReader(new FileInputStream(file));

            chemFile = pdbReader.read(new ChemFile());

        } catch (FileNotFoundException | CDKException ex) {
            chemFile = null;
        }

        return chemFile;
    }

    public static IChemFile readChemFile(File file, ReadConfiguration config) {
        IChemFile chemFile;

        try {
            PDBReader pdbReader = new PDBReader();

            pdbReader.setReadConfig(config);

            pdbReader.setReader(new FileInputStream(file));

            chemFile = pdbReader.read(new ChemFile());

        } catch (FileNotFoundException | CDKException ex) {
            chemFile = null;
        }

        return chemFile;
    }

    // devuelve el modelo especificado
    public static PDBProtein getModel(IChemFile chemFile, int modelIndex) {
        if (modelIndex < 0 || modelIndex >= getModelCount(chemFile)) {
            return null;
        } else {
            PDBProtein protein = ((PDBProtein) chemFile.getChemSequence(0).getChemModel(modelIndex).getMoleculeSet().getAtomContainer(0));

            if (modelIndex > 0) {
                protein.setSecondaryStructures(((PDBProtein) chemFile.getChemSequence(0).getChemModel(0).getMoleculeSet().getAtomContainer(0)).getSecondaryStructures());

                protein.setSsBonds(((PDBProtein) chemFile.getChemSequence(0).getChemModel(0).getMoleculeSet().getAtomContainer(0)).getSsBonds());
            }

            protein.getAllAtoms();

            return protein;
        }
    }

    // filtrar por cadenas
    public PDBProtein filterByStrands(PDBProtein protein, List<String> strands, IPDBAtomFilter atomFilter) {
        PDBProtein pdbFiltered = new PDBProtein();

        for (int i = 0; i < protein.getMonomerCount(); i++) {
            PDBMonomer currentMonomer = protein.getMonomer(i);

            if (strands.contains(currentMonomer.getChainID())) // chequeo a nivel de strand
            {
                PDBMonomer monomer = new PDBMonomer();

                monomer.setChainID(currentMonomer.getChainID());

                monomer.setICode(currentMonomer.getICode());

                monomer.setMonomerName(currentMonomer.getMonomerName());

                monomer.setMonomerType(currentMonomer.getMonomerType());

                monomer.setResSeq(currentMonomer.getResSeq());

                for (int j = 0; j < currentMonomer.getAtomCount(); j++) {
                    PDBAtom atom = (PDBAtom) currentMonomer.getAtom(j);

                    if (atomFilter.isAtom(atom)) {
                        pdbFiltered.addAtom(atom, monomer);
                    }
                }

                if (atomFilter instanceof AverageAtomsFilter) {
                    PDBAtom meanAtom = ((AverageAtomsFilter) atomFilter).getMediaAtoms(monomer);// calcula el atomo promedio

                    monomer.removeAllElements();// elimina todos los atomos

                    monomer.addAtom(meanAtom);// adiciona el atomo promedio
                }
            }
        }

        return pdbFiltered;
    }

    //average polymer
    public PDBProtein getAveragePolymer(PDBProtein pdbPolymer, AverageAtomsFilter atomFilter) {

        for (int i = 0; i < pdbPolymer.getMonomerCount(); i++) {
            PDBMonomer currentMonomer = pdbPolymer.getMonomer(i);

            PDBAtom meanAtom = atomFilter.getMediaAtoms(currentMonomer);// calcula el atomo promedio

            currentMonomer.removeAllElements();// elimina todos los atomos

            currentMonomer.addAtom(meanAtom);// adiciona el atomo promedio
        }

        return pdbPolymer;
    }

    public static int getModelCount(IChemFile chemFile) {
        return chemFile.getChemSequence(0).getChemModelCount();
    }

    public static boolean hasAltLocation(PDBAtom pdbAtom1, PDBAtom pdbAtom2) {
        return pdbAtom1.getName().equalsIgnoreCase(pdbAtom2.getName());
    }

    public static String getAminoAcidRepresentation(File pdbFile) throws FileNotFoundException, IOException, Exception {
        BufferedReader br = new BufferedReader(new FileReader(pdbFile));

        String line;

        try {
            br.readLine();

            line = br.readLine();

        } catch (IOException ex) {
            throw new IOException("An I/O problem reading file " + pdbFile.getName() + "format was not recognized");
        }

        if (line == null || !line.startsWith("REMARK")) {
            throw new Exception("Input format was not recognized");
        } else {
            String representation = line.substring(12, line.length()).trim();

            boolean isValid = true;

            try {
                AtomFilterType atomType = AtomFilterType.valueOf(representation);

            } catch (Exception ex) {
                isValid = false;
            }

            if (!isValid) {
                throw new Exception("The aminoacid representation was not recognized");
            } else {
                return representation;
            }
        }
    }
/*
    tras la línea de cabecera y los comentarios, una o más líneas pueden seguir para describir 
    la secuencia: cada línea de una secuencia debería tener algo menos de 80 caracteres. 
    Las secuencia pueden corresponder a secuencias de proteínas (estructura primaria de las proteínas) 
    o de ácidos nucleicos, y pueden contener huecos (o gaps) o caracteres de alineamiento. 
    Normalmente se espera que las secuencias se representen en los códigos estándar IUB/IUPAC para 
    aminoácidos y ácidos nucléicos, con las siguientes excepciones: se aceptan letras minúsculas 
    y se mapean a mayúsculas; un único guion o raya puede usarse para representar un hueco; y 
    en secuencias de aminoácidos, 'U' y '*' son caracteres aceptables (ver más abajo). 
    No se admiten dígitos numéricos, pero se utilizan en algunas bases de datos para indicar 
    la posición en la secuencia. 
    */
    public static IChemFile readFastaFile(File fastaFile) throws Exception {

        ChemFile oFile = new ChemFile();

        IChemSequence oSeq = oFile.getBuilder().newInstance(IChemSequence.class);
        IChemModel oModel = oFile.getBuilder().newInstance(IChemModel.class);
        IMoleculeSet oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);
        int seq = 1;
FastaReader<ProteinSequence, AminoAcidCompound> fastaReader
                    = new FastaReader<ProteinSequence, AminoAcidCompound>(
                            fastaFile,
                            new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                            new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

        try {
            

            LinkedHashMap<String, ProteinSequence> b = fastaReader.process();

            
                for (String key : b.keySet()) {
                    try {
                        ProteinSequence proteinSequence = b.get(key);

                        PDBProtein oBP = new PDBProtein();

                        List<AminoAcidCompound> amino = proteinSequence.getAsList();

                        int i = 0;

                        for (AminoAcidCompound aminoAcidCompound : amino) {

                            PDBAtom atom = new PDBAtom("C");
                            atom.setAltLoc(aminoAcidCompound.getShortName());
                            atom.setResSeq("" + i);
                            atom.setName(atom.getResSeq() + "C");
                            atom.setResName(aminoAcidCompound.getLongName().toUpperCase());

                            PDBMonomer monomer = new PDBMonomer();

                            monomer.setMonomerType(atom.getResName());
                            monomer.setMonomerName(i + atom.getResName());

                            monomer.setResSeq(atom.getResSeq());

                            oBP.addAtom(atom, monomer);

                            i++;
                        }

                        oBP.setProperty(CDKConstants.TITLE, proteinSequence.getOriginalHeader() + "" + seq);
                        seq++;
                        oSet.addAtomContainer(oBP);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                
            

           
        } catch (Exception e) {
            //logger.warn("Exception: ", e);
            e.printStackTrace();
        }
        
         fastaReader.close();
        oModel.setMoleculeSet(oSet);
        oSeq.addChemModel(oModel);
        oFile.addChemSequence(oSeq);
        return oFile;
    }
    
    public static IMoleculeSet fastaFile2PDBList(File fastaFile) throws Exception {

        ChemFile oFile = new ChemFile();

//        IChemSequence oSeq = oFile.getBuilder().newInstance(IChemSequence.class);
//        IChemModel oModel = oFile.getBuilder().newInstance(IChemModel.class);
//        

IMoleculeSet oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);
        
        
        int seq = 1;
FastaReader<ProteinSequence, AminoAcidCompound> fastaReader
                    = new FastaReader<ProteinSequence, AminoAcidCompound>(
                            fastaFile,
                            new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                            new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

        try {
            

            LinkedHashMap<String, ProteinSequence> b = fastaReader.process();

            
                for (String key : b.keySet()) {
                    try {
                        ProteinSequence proteinSequence = b.get(key);

                        PDBProtein oBP = fasta2PDB(proteinSequence);

                        seq++;
                        oSet.addAtomContainer(oBP);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                
            

           
        } catch (Exception e) {
            //logger.warn("Exception: ", e);
            e.printStackTrace();
        }
        
         fastaReader.close();
//        oModel.setMoleculeSet(oSet);
//        oSeq.addChemModel(oModel);
//        oFile.addChemSequence(oSeq);
        return oSet;
    }    
    
    static public PDBProtein fasta2PDB(ProteinSequence ps) {
        List<AminoAcidCompound> ac = ps.getAsList();

        PDBProtein prot = new PDBProtein();

        prot.setProperty(CDKConstants.TITLE, ps.getOriginalHeader());

        int i = 1;

        int n = ac.size();

        IAtom[] atomList = new IAtom[n];

        List<PDBMonomer> monomers = new ArrayList<>(n);

        for (AminoAcidCompound aminoAcidCompound : ac) {

            PDBAtom atom = new PDBAtom("C", new Point3d(new double[3]));
            atom.setSerial(i);
            atom.setResSeq("" + i);
            atom.setName("CA");
            atom.setResName(aminoAcidCompound.getLongName().toUpperCase());
            atom.setChainID("A");

            PDBMonomer monomer = new PDBMonomer();
            monomer.setMonomerType(atom.getResName());
            monomer.setMonomerName(i + atom.getResName());
            monomer.setResSeq(atom.getResSeq());
            monomer.setChainID(atom.getChainID());

            monomer.addAtom(atom);

            atomList[i - 1] = atom;

            monomers.add(monomer);

            i++;
        }

        prot.setAtoms(atomList);
        prot.setMonomers(monomers);

        return prot;
    }
}
