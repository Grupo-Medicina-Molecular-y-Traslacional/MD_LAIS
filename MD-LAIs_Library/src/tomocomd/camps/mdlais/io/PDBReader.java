package tomocomd.camps.mdlais.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3d;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.graph.rebond.RebondTool;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IStrand;
import org.openscience.cdk.io.DefaultChemObjectReader;
import org.openscience.cdk.io.formats.IResourceFormat;
import org.openscience.cdk.io.formats.PDBFormat;
import org.openscience.cdk.io.setting.BooleanIOSetting;
import org.openscience.cdk.io.setting.IOSetting;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.protein.data.PDBMonomer;
import tomocomd.camps.mdlais.protein.PDBStructure;
import tomocomd.camps.mdlais.protein.SSBond;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import tomocomd.camps.mdlais.exceptions.TomocomdException;
import tomocomd.camps.mdlais.pdbfilter.PDBDataFile;
import tomocomd.camps.mdlais.pdbfilter.ReadConfiguration;
import tomocomd.camps.mdlais.protein.PDBProtein;

/**
 * Reads the contents of a PDBFile.
 *
 * <p>
 * A description can be found at <a
 * href="http://www.rcsb.org/pdb/static.do?p=file_formats/pdb/index.html">
 * http://www.rcsb.org/pdb/static.do?p=file_formats/pdb/index.html</a>.
 *
 * @cdk.module pdb
 * @cdk.githash
 * @cdk.iooptions
 *
 * @author Edgar Luttmann
 * @author Bradley Smith <bradley@baysmith.com>
 * @author Martin Eklund <martin.eklund@farmbio.uu.se>
 * @author Ola Spjuth <ola.spjuth@farmbio.uu.se>
 * @author Gilleain Torrance <gilleain.torrance@gmail.com>
 * @cdk.created 2001-08-06
 * @cdk.keyword file format, PDB
 * @cdk.bug 1714141
 * @cdk.bug 1794439
 */
@TestClass("org.openscience.cdk.io.PDBReaderTest")
public class PDBReader extends DefaultChemObjectReader {

    private static ILoggingTool logger
            = LoggingToolFactory.createLoggingTool(PDBReader.class);
    private BufferedReader _oInput; // The internal used BufferedReader
    private BooleanIOSetting useRebondTool;
    private BooleanIOSetting readConnect;
    private BooleanIOSetting useHetDictionary;

    private Map<Integer, IAtom> atomNumberMap;

    /*
     * This is a temporary store for bonds from CONNECT records.
     * As CONNECT is deliberately fully redundant (a->b and b->a)
     * we need to use this to weed out the duplicates.
     */
    private ArrayList bondsFromConnectRecords;

    private static AtomTypeFactory pdbFactory;

    /**
     * A mapping between HETATM 3-letter codes + atomNames to CDK atom type
     * names; for example "RFB.N13" maps to "N.planar3".
     */
    private Map<String, String> hetDictionary;

    private AtomTypeFactory cdkAtomTypeFactory;

    private static final String hetDictionaryPath
            = "org/openscience/cdk/config/data/type_map.txt";

    private ReadConfiguration readConfig;
    
    private List<TomocomdException> exceptionList = new ArrayList<>();
    /**
     *
     * Constructs a new PDBReader that can read Molecules from a given
     * InputStream.
     *
     * @param oIn The InputStream to read from
     *
     */
    public PDBReader(InputStream oIn) {
        this(new InputStreamReader(oIn));
    }

    /**
     *
     * Constructs a new PDBReader that can read Molecules from a given Reader.
     *
     * @param oIn The Reader to read from
     *
     */
    public PDBReader(Reader oIn) {
        _oInput = new BufferedReader(oIn);
        initIOSettings();
        pdbFactory = null;
        hetDictionary = null;
        cdkAtomTypeFactory = null;
    }

    public PDBReader() {
        this(new StringReader(""));
    }

    @TestMethod("testGetFormat")
    public IResourceFormat getFormat() {
        return PDBFormat.getInstance();
    }

    @TestMethod("testSetReader_Reader")
    public void setReader(Reader input) throws CDKException {
        if (input instanceof BufferedReader) {
            this._oInput = (BufferedReader) input;
        } else {
            this._oInput = new BufferedReader(input);
        }
    }

    @TestMethod("testSetReader_InputStream")
    public void setReader(InputStream input) throws CDKException {
        setReader(new InputStreamReader(input));
    }

    @TestMethod("testAccepts")
    public boolean accepts(Class<? extends IChemObject> classObject) {
        Class[] interfaces = classObject.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (IChemFile.class.equals(interfaces[i])) {
                return true;
            }
        }
        if (IChemFile.class.equals(classObject)) {
            return true;
        }
        Class superClass = classObject.getSuperclass();
        if (superClass != null) {
            return this.accepts(superClass);
        }
        return false;
    }

    public void setReadConfig(ReadConfiguration readConfig) {
        this.readConfig = readConfig;
    }

   /**
     *
     * Takes an object which subclasses IChemObject, e.g. Molecule, and will
     * read this (from file, database, internet etc). If the specific
     * implementation does not support a specific IChemObject it will throw an
     * Exception.
     *
     * @param oObj The object that subclasses IChemObject
     * @return The IChemObject read
     * @exception CDKException
     *
     */
    public <T extends IChemObject> T read(T oObj) throws CDKException {
        if (oObj instanceof IChemFile) {
            if (pdbFactory == null) {
                try {
                    pdbFactory = AtomTypeFactory.getInstance("org/openscience/cdk/config/data/pdb_atomtypes.xml",
                            ((IChemFile) oObj).getBuilder());                    
                } catch (Exception exception) {
                    logger.debug(exception);
                    throw new CDKException("Could not setup list of PDB atom types! " + exception.getMessage(), exception);
                }
            }
            if(readConfig==null)
            return (T) readChemFile((IChemFile) oObj);
            else
                return (T) readChemFileBM((IChemFile) oObj);
        } else {
            throw new CDKException("Only supported is reading of ChemFile objects.");
        }
    }

    /**
     * Read a <code>ChemFile</code> from a file in PDB format. The molecules in
     * the file are stored as <code>BioPolymer</code>s in the
     * <code>ChemFile</code>. The residues are the monomers of the
     * <code>BioPolymer</code>, and their names are the concatenation of the
     * residue, chain id, and the sequence number. Separate chains (denoted by
     * TER records) are stored as separate <code>BioPolymer</code> molecules.
     *
     * <p>
     * Connectivity information is not currently read.
     *
     * @return The ChemFile that was read from the PDB file.
     */
    private IChemFile readChemFile(IChemFile oFile) {
        // initialize all containers
        IChemSequence oSeq = oFile.getBuilder().newInstance(IChemSequence.class);
        IChemModel oModel = oFile.getBuilder().newInstance(IChemModel.class);
        IMoleculeSet oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);

        // some variables needed
        String cCol;
        PDBAtom oAtom;
        PDBProtein oBP = new PDBProtein();
        IMolecule molecularStructure = oFile.getBuilder().newInstance(IMolecule.class);
        StringBuffer cResidue;
        String oObj;
        PDBMonomer oMonomer;
        String cRead = "";
        String chain = "A";	// To ensure stringent name giving of monomers
        IStrand oStrand;
        int lineLength = 0;

        boolean isProteinStructure = true;

        atomNumberMap = new Hashtable<Integer, IAtom>();
        if (readConnect.isSet()) {
            bondsFromConnectRecords = new ArrayList();
        }

        // do the reading of the Input		
        try {
            do {
                cRead = _oInput.readLine();
                logger.debug("Read line: ", cRead);
                if (cRead != null) {
                    lineLength = cRead.length();

                    if (lineLength < 80) {
                        logger.warn("Line is not of the expected length 80!");
                    }

                    // make sure the record name is 6 characters long
                    if (lineLength < 6) {
                        cRead = cRead + "      ";
                    }
                    // check the first column to decide what to do
                    cCol = cRead.substring(0, 6);
                    if ("HEADER".equalsIgnoreCase(cCol)) {
                        String pdbId = cRead.substring(62, 67).trim();

                        oFile.setID(pdbId);

                    } // skip all remarks
//                    else if ("REMARK".equalsIgnoreCase(cCol)) {
//                        while (cCol.equalsIgnoreCase("REMARK")) {
//                            cRead = _oInput.readLine();
//
//                            cCol = cRead.substring(0, 6);
//                        }
//                    } 
                    else if ("ATOM  ".equalsIgnoreCase(cCol)) {
                        // read an atom record
                        oAtom = readAtom(cRead, lineLength);

                        if (oAtom != null) {
                            // construct a string describing the residue
                            cResidue = new StringBuffer(8);
                            oObj = oAtom.getResName();
                            if (oObj != null) {
                                cResidue = cResidue.append(oObj.trim());
                            }
                            oObj = oAtom.getChainID();
                            if (oObj != null) {
                                // cResidue = cResidue.append(((String)oObj).trim());
                                chain = ((String) oObj).trim();
                                cResidue = cResidue.append(chain);
                            }
                            oObj = oAtom.getResSeq();
                            if (oObj != null) {
                                cResidue = cResidue.append(oObj.trim());
                            }
                            
                            //The combination of residue numbering and insertion code
                            //defines the unique residue. error modelFound in file 1azz.pdb
                            oObj = oAtom.getICode();
                            if (oObj != null) {
                                cResidue = cResidue.append(oObj.trim());
                            }

                            // search for an existing strand or create a new one.
                            String strandName = oAtom.getChainID();
                            if (strandName == null || strandName.length() == 0) {
                                strandName = String.valueOf(chain);
                            }
//                            oStrand = oBP.getStrand(strandName);
//                            if (oStrand == null) {
//                                oStrand = new PDBStrand();
//                                oStrand.setStrandName(strandName);
//                                oStrand.setID(String.valueOf(chain));
//                            }

                            // search for an existing monomer or create a new one.
                            oMonomer = oBP.getMonomer(cResidue.toString(), String.valueOf(chain));
                            if (oMonomer == null) {
                                PDBMonomer monomer = new PDBMonomer();
                                monomer.setMonomerName(cResidue.toString());
                                monomer.setMonomerType(oAtom.getResName());
                                monomer.setChainID(oAtom.getChainID());
                                monomer.setICode(oAtom.getICode());
                                monomer.setResSeq(oAtom.getResSeq());
                                oMonomer = monomer;
                            }

                            // add the atom
                            oBP.addAtom(oAtom, oMonomer);
                        }
//                        } else {
//                            molecularStructure.addAtom(oAtom);
//                        }

                        if (readConnect.isSet() && atomNumberMap.put(oAtom.getSerial(), oAtom) != null) {
                            logger.warn("Duplicate serial ID found for atom: ", oAtom);
                        }
                        logger.debug("Added ATOM: ", oAtom);

                        /**
                         * As HETATMs cannot be considered to either belong to a
                         * certain monomer or strand, they are dealt with
                         * seperately.
                         */
//                    } else if ("HETATM".equalsIgnoreCase(cCol)) {
//                        // read an atom record
//                        oAtom = readAtom(cRead, lineLength);
//                        oAtom.setHetAtom(true);
//                        if (isProteinStructure) {
//                            oBP.addAtom(oAtom);
//                        } else {
//                            molecularStructure.addAtom(oAtom);
//                        }
//                        if (atomNumberMap.put(oAtom.getSerial(), oAtom) != null) {
//                            logger.warn("Duplicate serial ID found for atom: ", oAtom);
//                        }
//                        logger.debug("Added HETATM: ", oAtom);
//                    } else if ("TER   ".equalsIgnoreCase(cCol)) {
                        // start new strand						
                        //chain++;
//                        oStrand = new PDBStrand();
//                        oStrand.setStrandName(chain);
                        logger.debug("Added new STRAND");
                    } else if ("END   ".equalsIgnoreCase(cCol)) {
                        atomNumberMap.clear();
                        if (isProteinStructure) {
                            // create bonds and finish the molecule
                            //oSet.addMolecule(oBP);
                            if (useRebondTool.isSet()) {
                                try {
                                    if (!createBondsWithRebondTool(oBP)) {
                                        // Get rid of all potentially created bonds.
                                        logger.info("Bonds could not be created using the RebondTool when PDB file was read.");
                                        oBP.removeAllBonds();
                                    }
                                } catch (Exception exception) {
                                    logger.info("Bonds could not be created when PDB file was read.");
                                    logger.debug(exception);
                                }
                            }
                        } else {
                            createBondsWithRebondTool(molecularStructure);
                            oSet.addMolecule(molecularStructure);
                        }

                    } else if (cCol.equals("MODEL ")) {
                        // OK, start a new model and save the current one first *if* it contains atoms
                        if (isProteinStructure) {
                            if (oBP.getAtomCount() > 0) {
                                // save the model
                                oSet.addAtomContainer(oBP);
                                oModel.setMoleculeSet(oSet);
                                oSeq.addChemModel(oModel);
                                // setup a new one
                                oBP = new PDBProtein();
                                oModel = oFile.getBuilder().newInstance(IChemModel.class);
                                oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);
                            }
                        } else {
                            if (molecularStructure.getAtomCount() > 0) {
//								 save the model
                                oSet.addAtomContainer(molecularStructure);
                                oModel.setMoleculeSet(oSet);
                                oSeq.addChemModel(oModel);
                                // setup a new one
                                molecularStructure = oFile.getBuilder().newInstance(IMolecule.class);
                                oModel = oFile.getBuilder().newInstance(IChemModel.class);
                                oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);
                            }
                        }
                    } else if ("COMPND".equalsIgnoreCase(cCol)) {
                        String title = cRead.substring(10).trim();
                        oFile.setProperty(CDKConstants.TITLE, title);
                    } /* ***********************************************************
                     * Read connectivity information from CONECT records.
                     * Only covalent bonds are dealt with. Perhaps salt bridges
                     * should be dealt with in the same way..?
                     
                     
                     */ else if ("SSBOND".equalsIgnoreCase(cCol)) {

                        SSBond ssBond = new SSBond(cRead);
                        oBP.addSSBond(ssBond);
                        //oBP.addBond(ssBond);
                    } else if (readConnect.isSet() && "CONECT".equalsIgnoreCase(cCol)) {
                        cRead.trim();
                        if (cRead.length() < 16) {
                            logger.debug("Skipping unexpected empty CONECT line! : ", cRead);
                        } else {
                            int lineIndex = 6;
                            int atomFromNumber = -1;
                            int atomToNumber = -1;
                            IMolecule molecule = (isProteinStructure) ? oBP : molecularStructure;
                            while (lineIndex + 5 <= cRead.length()) {
                                String part = cRead.substring(lineIndex, lineIndex + 5).trim();
                                if (atomFromNumber == -1) {
                                    try {
                                        atomFromNumber = Integer.parseInt(part);
                                    } catch (NumberFormatException nfe) {
                                    }
                                } else {
                                    try {
                                        atomToNumber = Integer.parseInt(part);
                                    } catch (NumberFormatException nfe) {
                                        atomToNumber = -1;
                                    }
                                    if (atomFromNumber != -1 && atomToNumber != -1) {
                                        addBond(molecule, atomFromNumber, atomToNumber);
                                        logger.warn("Bonded " + atomFromNumber + " with " + atomToNumber);
                                    }
                                }
                                lineIndex += 5;
                            }
                        }
                    } /* ***********************************************************/ else if ("HELIX ".equalsIgnoreCase(cCol)) {
//						HELIX    1 H1A CYS A   11  LYS A   18  1 RESIDUE 18 HAS POSITIVE PHI    1D66  72
//						          1         2         3         4         5         6         7
//						01234567890123456789012345678901234567890123456789012345678901234567890123456789
                        PDBStructure structure = new PDBStructure();
                        structure.setStructureType(PDBStructure.HELIX);
                        structure.setStartResidueName(cRead.substring(15, 19).trim());
                        structure.setStartChainID(cRead.charAt(19));
                        structure.setStartSequenceNumber(Integer.parseInt(cRead.substring(21, 25).trim()));
                        structure.setStartInsertionCode(cRead.charAt(25));
                        structure.setEndResidueName(cRead.substring(27, 30).trim());
                        structure.setEndChainID(cRead.charAt(31));
                        structure.setEndSequenceNumber(Integer.parseInt(cRead.substring(33, 37).trim()));
                        structure.setEndInsertionCode(cRead.charAt(37));
                        oBP.addStructure(structure);
                    } else if ("SHEET ".equalsIgnoreCase(cCol)) {
                        PDBStructure structure = new PDBStructure();
                        structure.setStructureType(PDBStructure.SHEET);
                        structure.setStartResidueName(cRead.substring(17, 21));
                        structure.setStartChainID(cRead.charAt(21));
                        structure.setStartSequenceNumber(Integer.parseInt(cRead.substring(22, 26).trim()));
                        structure.setStartInsertionCode(cRead.charAt(26));
                        structure.setEndResidueName(cRead.substring(28, 31));
                        structure.setEndChainID(cRead.charAt(32));
                        structure.setEndSequenceNumber(Integer.parseInt(cRead.substring(33, 37).trim()));
                        structure.setEndInsertionCode(cRead.charAt(37));
                        oBP.addStructure(structure);
                    } else if ("TURN  ".equalsIgnoreCase(cCol)) {
                        PDBStructure structure = new PDBStructure();
                        structure.setStructureType(PDBStructure.TURN);
                        structure.setStartResidueName(cRead.substring(15, 18));
                        structure.setStartChainID(cRead.charAt(19));
                        structure.setStartSequenceNumber(Integer.parseInt(cRead.substring(20, 24).trim()));
                        structure.setStartInsertionCode(cRead.charAt(24));
                        structure.setEndResidueName(cRead.substring(26, 29));
                        structure.setEndChainID(cRead.charAt(30));
                        structure.setEndSequenceNumber(Integer.parseInt(cRead.substring(31, 35).trim()));
                        structure.setEndInsertionCode(cRead.charAt(35));
                        oBP.addStructure(structure);
                    } // ignore all other commands
                }
            } while (_oInput.ready() && (cRead != null));
        } catch (Exception e) {
            logger.error("Found a problem at line:");
            logger.error(cRead);
            logger.error("01234567890123456789012345678901234567890123456789012345678901234567890123456789");
            logger.error("          1         2         3         4         5         6         7         ");
            logger.error("  error: " + e.getMessage());
            logger.debug(e);
            e.printStackTrace();            
        }

        // try to close the Input
        try {
            _oInput.close();
        } catch (Exception e) {
            logger.debug(e);
        }

        // Set all the dependencies
            oSet.addAtomContainer(oBP);            
            oModel.setMoleculeSet(oSet);
            oSeq.addChemModel(oModel);
            oFile.addChemSequence(oSeq);
        
            return oFile;
    }
    
    private IChemFile readChemFileBM(IChemFile oFile) {
        // initialize all containers
        IChemSequence oSeq = oFile.getBuilder().newInstance(IChemSequence.class);
        IChemModel oModel = oFile.getBuilder().newInstance(IChemModel.class);
        IMoleculeSet oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);

        // some variables needed
        String cCol;
        PDBAtom oAtom;
        PDBProtein oBP = new PDBProtein();
        IMolecule molecularStructure = oFile.getBuilder().newInstance(IMolecule.class);
        StringBuffer cResidue;
        String oObj;
        PDBMonomer oMonomer;
        String cRead = "";
        String chain = "A";	// To ensure stringent name giving of monomers
        IStrand oStrand;
        int lineLength = 0;
        int currentModel = 0;
        boolean singleModel = true;
        boolean modelFound = false;
        boolean modelSave = false;
        boolean isProteinStructure = true;
        int resSeq = 1;
        atomNumberMap = new Hashtable<Integer, IAtom>();
        if (readConnect.isSet()) {
            bondsFromConnectRecords = new ArrayList();
        }

        // do the reading of the Input		
        try {
            do {
                cRead = _oInput.readLine();
                logger.debug("Read line: ", cRead);
                if (cRead != null) {
                    lineLength = cRead.length();

                    if (lineLength < 80) {
                        logger.warn("Line is not of the expected length 80!");
                    }

                    // make sure the record name is 6 characters long
                    if (lineLength < 6) {
                        cRead = cRead + "      ";
                    }
                    // check the first column to decide what to do
                    cCol = cRead.substring(0, 6);
                    if ("HEADER".equalsIgnoreCase(cCol)) {
                        String pdbId = cRead.substring(62, 67).trim();

                        oFile.setID(pdbId);

                    } // skip all remarks
//                    else if ("REMARK".equalsIgnoreCase(cCol)) {
//                        while (cCol.equalsIgnoreCase("REMARK")) {
//                            cRead = _oInput.readLine();
//
//                            cCol = cRead.substring(0, 6);
//                        }
//                        
//                    } 
                    else if ("MODEL ".equalsIgnoreCase(cCol)) 
                    {
                        modelFound = currentModel ==readConfig.getModelIndex();

                        singleModel = false;
                        
                        currentModel++;
                    }else if ("ATOM  ".equalsIgnoreCase(cCol)&&((singleModel||modelFound))) {
                        // read an atom record
                        oAtom = readAtom(cRead, lineLength);

                        if (oAtom != null) {
                            // construct a string describing the residue
                            cResidue = new StringBuffer(8);
                            oObj = oAtom.getResName();
                            if (oObj != null) {
                                cResidue = cResidue.append(oObj.trim());
                            }
                            oObj = oAtom.getChainID();
                            if (oObj != null) {
                                // cResidue = cResidue.append(((String)oObj).trim());
                                chain = ((String) oObj).trim();
                                cResidue = cResidue.append(chain);
                            }
                            oObj = oAtom.getResSeq();
                            if (oObj != null) {
                                cResidue = cResidue.append(oObj.trim());
                            }
                            
                            //The combination of residue numbering and insertion code
                            //defines the unique residue. error modelFound in file 1azz.pdb
                            oObj = oAtom.getICode();
                            if (oObj != null) {
                                cResidue = cResidue.append(oObj.trim());
                            }

                            // search for an existing strand or create a new one.
                            String strandName = oAtom.getChainID();
                            if (strandName == null || strandName.length() == 0) {
                                strandName = String.valueOf(chain);
                            }
//                            oStrand = oBP.getStrand(strandName);
//                            if (oStrand == null) {
//                                oStrand = new PDBStrand();
//                                oStrand.setStrandName(strandName);
//                                oStrand.setID(String.valueOf(chain));
//                            }

                            // search for an existing monomer or create a new one.
                            oMonomer = oBP.getMonomer(cResidue.toString(), String.valueOf(chain));
                            if (oMonomer == null) {
                                PDBMonomer monomer = new PDBMonomer();
                                monomer.setMonomerName(cResidue.toString());
                                monomer.setMonomerType(oAtom.getResName());
                                monomer.setChainID(oAtom.getChainID());
                                monomer.setICode(oAtom.getICode());
                                monomer.setResSeq(String.valueOf(resSeq++));
                                oMonomer = monomer;
                            }

                            // add the atom
                            oAtom.setResSeq(oMonomer.getResSeq());
                            oBP.addAtom(oAtom, oMonomer);
                        }
//                        } else {
//                            molecularStructure.addAtom(oAtom);
//                        }

                        if (readConnect.isSet() && atomNumberMap.put(oAtom.getSerial(), oAtom) != null) {
                            logger.warn("Duplicate serial ID found for atom: ", oAtom);
                        }
                        logger.debug("Added ATOM: ", oAtom);

                        /**
                         * As HETATMs cannot be considered to either belong to a
                         * certain monomer or strand, they are dealt with
                         * seperately.
                         */
//                    } else if ("HETATM".equalsIgnoreCase(cCol)) {
//                        // read an atom record
//                        oAtom = readAtom(cRead, lineLength);
//                        oAtom.setHetAtom(true);
//                        if (isProteinStructure) {
//                            oBP.addAtom(oAtom);
//                        } else {
//                            molecularStructure.addAtom(oAtom);
//                        }
//                        if (atomNumberMap.put(oAtom.getSerial(), oAtom) != null) {
//                            logger.warn("Duplicate serial ID found for atom: ", oAtom);
//                        }
//                        logger.debug("Added HETATM: ", oAtom);
//                    } else if ("TER   ".equalsIgnoreCase(cCol)) {
                        // start new strand						
                        //chain++;
//                        oStrand = new PDBStrand();
//                        oStrand.setStrandName(chain);
                        logger.debug("Added new STRAND");
                    } else if ("END   ".equalsIgnoreCase(cCol)) {
                        atomNumberMap.clear();
                        if (isProteinStructure) {
                            // create bonds and finish the molecule
                            //oSet.addMolecule(oBP);
                            if (useRebondTool.isSet()) {
                                try {
                                    if (!createBondsWithRebondTool(oBP)) {
                                        // Get rid of all potentially created bonds.
                                        logger.info("Bonds could not be created using the RebondTool when PDB file was read.");
                                        oBP.removeAllBonds();
                                    }
                                } catch (Exception exception) {
                                    logger.info("Bonds could not be created when PDB file was read.");
                                    logger.debug(exception);
                                }
                            }
                        } else {
                            createBondsWithRebondTool(molecularStructure);
                            oSet.addMolecule(molecularStructure);
                        }

                    } else if (cCol.equals("ENDMDL")) {
                        // OK, start a new model and save the current one first *if* it contains atoms
                        if (isProteinStructure) {
                            if (oBP.getAtomCount() > 0) {
                                // save the model
                                oSet.addAtomContainer(oBP);
                                oModel.setMoleculeSet(oSet);
                                oSeq.addChemModel(oModel);
                                // setup a new one
                                oBP = new PDBProtein();
                                oModel = oFile.getBuilder().newInstance(IChemModel.class);
                                oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);
                            }
                            
                            modelSave = modelFound;
//                        } else {
//                            if (molecularStructure.getAtomCount() > 0) {
////								 save the model
//                                oSet.addAtomContainer(molecularStructure);
//                                oModel.setMoleculeSet(oSet);
//                                oSeq.addChemModel(oModel);
//                                // setup a new one
//                                molecularStructure = oFile.getBuilder().newInstance(IMolecule.class);
//                                oModel = oFile.getBuilder().newInstance(IChemModel.class);
//                                oSet = oFile.getBuilder().newInstance(IMoleculeSet.class);
//                            }
                        }
//                    } else if ("COMPND".equalsIgnoreCase(cCol)) {
//                        String title = cRead.substring(10).trim();
//                        oFile.setProperty(CDKConstants.TITLE, title);
                    } /* ***********************************************************
                     * Read connectivity information from CONECT records.
                     * Only covalent bonds are dealt with. Perhaps salt bridges
                     * should be dealt with in the same way..?
                     
                     
                     */ else if ("SSBOND".equalsIgnoreCase(cCol)) {

                        SSBond ssBond = new SSBond(cRead);
                        oBP.addSSBond(ssBond);
                        //oBP.addBond(ssBond);
                    } else if (readConnect.isSet() && "CONECT".equalsIgnoreCase(cCol)) {
                        cRead.trim();
                        if (cRead.length() < 16) {
                            logger.debug("Skipping unexpected empty CONECT line! : ", cRead);
                        } else {
                            int lineIndex = 6;
                            int atomFromNumber = -1;
                            int atomToNumber = -1;
                            IMolecule molecule = (isProteinStructure) ? oBP : molecularStructure;
                            while (lineIndex + 5 <= cRead.length()) {
                                String part = cRead.substring(lineIndex, lineIndex + 5).trim();
                                if (atomFromNumber == -1) {
                                    try {
                                        atomFromNumber = Integer.parseInt(part);
                                    } catch (NumberFormatException nfe) {
                                    }
                                } else {
                                    try {
                                        atomToNumber = Integer.parseInt(part);
                                    } catch (NumberFormatException nfe) {
                                        atomToNumber = -1;
                                    }
                                    if (atomFromNumber != -1 && atomToNumber != -1) {
                                        addBond(molecule, atomFromNumber, atomToNumber);
                                        logger.warn("Bonded " + atomFromNumber + " with " + atomToNumber);
                                    }
                                }
                                lineIndex += 5;
                            }
                        }
                    } /* ***********************************************************/ else if ("HELIX ".equalsIgnoreCase(cCol)) {
//						HELIX    1 H1A CYS A   11  LYS A   18  1 RESIDUE 18 HAS POSITIVE PHI    1D66  72
//						          1         2         3         4         5         6         7
//						01234567890123456789012345678901234567890123456789012345678901234567890123456789
                        PDBStructure structure = new PDBStructure();
                        structure.setStructureType(PDBStructure.HELIX);
                        structure.setStartResidueName(cRead.substring(15, 19).trim());
                        structure.setStartChainID(cRead.charAt(19));
                        structure.setStartSequenceNumber(Integer.parseInt(cRead.substring(21, 25).trim()));
                        structure.setStartInsertionCode(cRead.charAt(25));
                        structure.setEndResidueName(cRead.substring(27, 30).trim());
                        structure.setEndChainID(cRead.charAt(31));
                        structure.setEndSequenceNumber(Integer.parseInt(cRead.substring(33, 37).trim()));
                        structure.setEndInsertionCode(cRead.charAt(37));
                        oBP.addStructure(structure);
                    } else if ("SHEET ".equalsIgnoreCase(cCol)) {
                        PDBStructure structure = new PDBStructure();
                        structure.setStructureType(PDBStructure.SHEET);
                        structure.setStartResidueName(cRead.substring(17, 21));
                        structure.setStartChainID(cRead.charAt(21));
                        structure.setStartSequenceNumber(Integer.parseInt(cRead.substring(22, 26).trim()));
                        structure.setStartInsertionCode(cRead.charAt(26));
                        structure.setEndResidueName(cRead.substring(28, 31));
                        structure.setEndChainID(cRead.charAt(32));
                        structure.setEndSequenceNumber(Integer.parseInt(cRead.substring(33, 37).trim()));
                        structure.setEndInsertionCode(cRead.charAt(37));
                        oBP.addStructure(structure);
                    } else if ("TURN  ".equalsIgnoreCase(cCol)) {
                        PDBStructure structure = new PDBStructure();
                        structure.setStructureType(PDBStructure.TURN);
                        structure.setStartResidueName(cRead.substring(15, 18));
                        structure.setStartChainID(cRead.charAt(19));
                        structure.setStartSequenceNumber(Integer.parseInt(cRead.substring(20, 24).trim()));
                        structure.setStartInsertionCode(cRead.charAt(24));
                        structure.setEndResidueName(cRead.substring(26, 29));
                        structure.setEndChainID(cRead.charAt(30));
                        structure.setEndSequenceNumber(Integer.parseInt(cRead.substring(31, 35).trim()));
                        structure.setEndInsertionCode(cRead.charAt(35));
                        oBP.addStructure(structure);
                    } // ignore all other commands                    
                }
            } while (_oInput.ready() && (cRead != null)&&!modelSave);
        } catch (Exception e) {
            logger.error("Found a problem at line:");
            logger.error(cRead);
            logger.error("01234567890123456789012345678901234567890123456789012345678901234567890123456789");
            logger.error("          1         2         3         4         5         6         7         ");
            logger.error("  error: " + e.getMessage());
            logger.debug(e);
            e.printStackTrace();
        }

        // try to close the Input
        try {
            _oInput.close();
        } catch (Exception e) {
            logger.debug(e);
        }

        // Set all the dependencies
            oSet.addAtomContainer(oBP);            
            oModel.setMoleculeSet(oSet);
            oSeq.addChemModel(oModel);
            oFile.addChemSequence(oSeq);
        
            return oFile;
    }

    private void addBond(IMolecule molecule, int bondAtomNo, int bondedAtomNo) {
        IAtom firstAtom = atomNumberMap.get(bondAtomNo);
        IAtom secondAtom = atomNumberMap.get(bondedAtomNo);
        if (firstAtom == null) {
            logger.error("Could not find bond start atom in map with serial id: ", bondAtomNo);
        }
        if (secondAtom == null) {
            logger.error("Could not find bond target atom in map with serial id: ", bondAtomNo);
        }
        IBond bond = firstAtom.getBuilder().newInstance(IBond.class,
                firstAtom, secondAtom, IBond.Order.SINGLE);
        for (int i = 0; i < bondsFromConnectRecords.size(); i++) {
            IBond existingBond
                    = (IBond) bondsFromConnectRecords.get(i);
            IAtom a = existingBond.getAtom(0);
            IAtom b = existingBond.getAtom(1);
            if ((a == firstAtom && b == secondAtom)
                    || (b == firstAtom && a == secondAtom)) {
                // already stored
                return;
            }
        }
        bondsFromConnectRecords.add(bond);
        molecule.addBond(bond);
    }

    private boolean createBondsWithRebondTool(IMolecule molecule) {
        RebondTool tool = new RebondTool(2.0, 0.5, 0.5);
        try {
//			 configure atoms
            AtomTypeFactory factory = AtomTypeFactory.getInstance("org/openscience/cdk/config/data/jmol_atomtypes.txt",
                    molecule.getBuilder());
            for (IAtom atom : molecule.atoms()) {
                try {
                    IAtomType[] types = factory.getAtomTypes(atom.getSymbol());
                    if (types.length > 0) {
                        // just pick the first one
                        AtomTypeManipulator.configure(atom, types[0]);
                    } else {
                        logger.warn("Could not configure atom with symbol: " + atom.getSymbol());
                    }
                } catch (Exception e) {
                    logger.warn("Could not configure atom (but don't care): " + e.getMessage());
                    logger.debug(e);
                }
            }
            tool.rebond(molecule);
        } catch (Exception e) {
            logger.error("Could not rebond the polymer: " + e.getMessage());
            logger.debug(e);
        }
        return true;
    }

    /**
     * Creates an <code>Atom</code> and sets properties to their values from the
     * ATOM or HETATM record. If the line is shorter than 80 characters, the
     * information past 59 characters is treated as optional. If the line is
     * shorter than 59 characters, a <code>RuntimeException</code> is thrown.
     *
     * @param cLine the PDB ATOM or HEATATM record.
     * @return the <code>Atom</code> created from the record.
     * @throws RuntimeException if the line is too short (less than 59
     * characters).
     */
    private PDBAtom readAtom(String cLine, int lineLength) {
        // a line can look like (two in old format, then two in new format):
        //
        //           1         2         3         4         5         6         7
        // 01234567890123456789012345678901234567890123456789012345678901234567890123456789
        // ATOM      1  O5*   C A   1      20.662  36.632  23.475  1.00 10.00      114D  45
        // ATOM   1186 1H   ALA     1      10.105   5.945  -6.630  1.00  0.00      1ALE1288
        // ATOM     31  CA  SER A   3      -0.891  17.523  51.925  1.00 28.64           C
        // HETATM 3486 MG    MG A 302      24.885  14.008  59.194  1.00 29.42          MG+2
        //
        // note: the first two examples have the PDBID in col 72-75
        // note: the last two examples have the element symbol in col 76-77
        // note: the last (Mg hetatm) has a charge in col 78-79

        
        if (lineLength < 59) {
            throw new RuntimeException("PDBReader error during readAtom(): line too short");
        }
        String elementSymbol;
        if (cLine.length() > 78) {
            elementSymbol = cLine.substring(76, 78).trim();
            if (elementSymbol.length() == 0) {
                elementSymbol = cLine.substring(12, 14).trim();
            }
        } else {
            elementSymbol = cLine.substring(12, 14).trim();
        }

        if (elementSymbol.length() == 2) {
            // ensure that the second char is lower case
            if (Character.isDigit(elementSymbol.charAt(0))) {
                elementSymbol = elementSymbol.substring(1);
            } else {
                elementSymbol = elementSymbol.charAt(0)
                        + elementSymbol.substring(1).toLowerCase();
            }
        }

        String rawAtomName = cLine.substring(12, 16).trim();

        String resName = cLine.substring(17, 20).trim();

        boolean isHetatm;
        
        IAtomType type = null;
        try 
        {
            type = pdbFactory.getAtomType(resName + "." + rawAtomName);

            elementSymbol = type.getSymbol();

            if (elementSymbol.equalsIgnoreCase("H")) 
            {
                return null;
            }

            isHetatm = false;
            
            
        } catch (NoSuchAtomTypeException e) 
        {
            logger.error("Did not recognize PDB atom type: " + resName + "." + rawAtomName);
            
            return null;
        }
        PDBAtom oAtom = new PDBAtom(elementSymbol,
                new Point3d(Double.parseDouble(cLine.substring(30, 38)),
                        Double.parseDouble(cLine.substring(38, 46)),
                        Double.parseDouble(cLine.substring(46, 54))
                )
        );
        
//        if (useHetDictionary.isSet() && isHetatm) {
//            String cdkType = typeHetatm(resName, rawAtomName);
//            oAtom.setAtomTypeName(cdkType);
//            if (cdkType != null) {
//                try {
//                    cdkAtomTypeFactory.configure(oAtom);
//                } catch (CDKException cdke) {
//                    logger.warn("Could not configure", resName, " ", rawAtomName);
//                }
//            }
//        }

        //oAtom.setRecord(cLine);
        oAtom.setSerial(Integer.parseInt(cLine.substring(6, 11).trim()));
        oAtom.setName(rawAtomName.trim());
        oAtom.setAltLoc(cLine.substring(16, 17).trim());
        oAtom.setResName(resName);
        oAtom.setChainID(cLine.substring(21, 22).trim());
        oAtom.setResSeq(cLine.substring(22, 26).trim());
        oAtom.setICode(cLine.substring(26, 27).trim());
        
       //covalent radius is missing
//        cdkAtomTypeFactory = AtomTypeFactory.getInstance("org/openscience/cdk/config/data/chemicalElements.xml",new IChemFile )
//       if (useHetDictionary.isSet() && isHetatm) {
//            oAtom.setID(oAtom.getResName() + "." + rawAtomName);
//        } else {
//            oAtom.setAtomTypeName(oAtom.getResName() + "." + rawAtomName);
//        }
        if (lineLength >= 59) {
            String frag = cLine.substring(54, 60).trim();
            if (frag.length() > 0) {
                oAtom.setOccupancy(Double.parseDouble(frag));
            }
        }
//        if (lineLength >= 65) {
//            String frag = cLine.substring(60, 66).trim();
//            if (frag.length() > 0) {
//                oAtom.setTempFactor(Double.parseDouble(frag));
//            }
//        }
//        if (lineLength >= 75) {
//            oAtom.setSegID(cLine.substring(72, 76).trim());
//        }
//		if (lineLength >= 78) {
//            oAtom.setSymbol((new String(cLine.substring(76, 78))).trim());
//		}
//        if (lineLength >= 79) {
//            String frag;
//            if (lineLength >= 80) {
//                frag = cLine.substring(78, 80).trim();
//            } else {
//                frag = cLine.substring(78);
//            }
//            if (frag.length() > 0) {
//                // see Format_v33_A4.pdf, p. 178
//                if (frag.endsWith("-") || frag.endsWith("+")) {
//                    oAtom.setCharge(Double.parseDouble(new StringBuilder(frag).reverse().toString()));
//                } else {
//                    oAtom.setCharge(Double.parseDouble(frag));
//                }
//            }
//        }

        /* ***********************************************************************************
         * It sets a flag in the property content of an atom,
         * which is used when bonds are created to check if the atom is an OXT-record => needs
         * special treatment.
         */
        String oxt = cLine.substring(13, 16).trim();

        if (oxt.equals("OXT")) {
            oAtom.setOxt(true);
        } else {
            oAtom.setOxt(false);
        }
  
        if(readConfig!=null)
        {
            // check for strands and atom filter
            
            if(!readConfig.getStrands().contains(oAtom.getChainID())||!readConfig.getAtomFilter().isAtom(oAtom))
            {
                return null;
            }
        }
        
        return oAtom;
    }

    private String typeHetatm(String resName, String atomName) {
        if (hetDictionary == null) {
            readHetDictionary();
            cdkAtomTypeFactory
                    = AtomTypeFactory.getInstance(
                            "org/openscience/cdk/dict/data/cdk-atom-types.owl",
                            DefaultChemObjectBuilder.getInstance());
        }
        String key = resName + "." + atomName;
        if (hetDictionary.containsKey(key)) {
            return hetDictionary.get(key);
        }
        return null;
    }

    private void readHetDictionary() {
        try {
            InputStream ins
                    = getClass().getClassLoader().getResourceAsStream(hetDictionaryPath);
            BufferedReader bufferedReader
                    = new BufferedReader(new InputStreamReader(ins));
            hetDictionary = new HashMap<String, String>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int colonIndex = line.indexOf(":");
                if (colonIndex == -1) {
                    continue;
                }
                String typeKey = line.substring(0, colonIndex);
                String typeValue = line.substring(colonIndex + 1);
                if (typeValue.equals("null")) {
                    hetDictionary.put(typeKey, null);
                } else {
                    hetDictionary.put(typeKey, typeValue);
                }
            }
            bufferedReader.close();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }

    @TestMethod("testClose")
    public void close() throws IOException {
        _oInput.close();
    }

    private void initIOSettings() {
        useRebondTool = new BooleanIOSetting("UseRebondTool", IOSetting.LOW,
                "Should the PDBReader deduce bonding patterns?",
                "false");
        readConnect = new BooleanIOSetting("ReadConnectSection", IOSetting.LOW,
                "Should the CONECT be read?",
                "false");
        useHetDictionary = new BooleanIOSetting("UseHetDictionary", IOSetting.LOW,
                "Should the PDBReader use the HETATM dictionary for atom types?",
                "false");
    }

    public void customizeJob() {
        fireIOSettingQuestion(useRebondTool);
        fireIOSettingQuestion(readConnect);
        fireIOSettingQuestion(useHetDictionary);
    }

    public IOSetting[] getIOSettings() {
        IOSetting[] settings = new IOSetting[3];
        settings[0] = useRebondTool;
        settings[1] = readConnect;
        settings[2] = useHetDictionary;
        return settings;
    }
    
       public PDBDataFile scanFile(File file) 
       {
        pdbFactory = AtomTypeFactory.getInstance("org/openscience/cdk/config/data/pdb_atomtypes.xml",
                            (new ChemFile()).getBuilder());
        String cCol;
        PDBAtom oAtom;
        String cRead = "";
        String chain = "A";	// To ensure stringent name giving of monomers
        int lineLength = 0;

        int modelCount = 0;
        int atomCount = 0;        
        List<String> strands = new ArrayList<String>();
        
        try {
            _oInput = new BufferedReader(new FileReader(file));
            do {
                cRead = _oInput.readLine();
                logger.debug("Read line: ", cRead);
                if (cRead != null) {
                    lineLength = cRead.length();

                    if (lineLength < 80) {
                        logger.warn("Line is not of the expected length 80!");
                    }

                    // make sure the record name is 6 characters long
                    if (lineLength < 6) {
                        cRead = cRead + "      ";
                    }
                    // check the first column to decide what to do
                    cCol = cRead.substring(0, 6);
                     if ("ATOM  ".equalsIgnoreCase(cCol)) {
                        // read an atom record
                        oAtom = readAtom(cRead, lineLength);

                        if (oAtom != null) {
                            atomCount++;
    
                            // search for an existing strand or create a new one.
                            String strandName = oAtom.getChainID();
//                            if (strandName == null || strandName.length() == 0) {
//                                strandName = String.valueOf(chain);
//                            }
                            
                            if(strandName!=null&&!strandName.isEmpty()&&!strands.contains(strandName))
                            {
                               strands.add(strandName);
                            }

                        }
                    } else if ("ENDMDL".equalsIgnoreCase(cCol)) 
                    {
                        if (atomCount>0) 
                        {   atomCount =  0;
                            modelCount++;
                        }
                    }  
                }
            } while (_oInput.ready() && (cRead != null));
        } catch (Exception e) 
        {
            logger.error("Found a problem at line:");
            logger.error(cRead);
            logger.error("01234567890123456789012345678901234567890123456789012345678901234567890123456789");
            logger.error("          1         2         3         4         5         6         7         ");
            logger.error("  error: " + e.getMessage());
            logger.debug(e);
            e.printStackTrace();
            exceptionList.add(new TomocomdException("error "+e.getMessage()));
            return null;
        }

        // try to close the Input
        try {
            _oInput.close();
        } catch (Exception e) 
        {
            logger.debug(e);
        }

        if(modelCount==0&&atomCount>0)
        {
            modelCount = 1;
        }
        
            if(modelCount==0||strands.isEmpty())
        {
            return null;
        }   
        else
        {
            return new PDBDataFile(modelCount, strands);
        }
    }
}
