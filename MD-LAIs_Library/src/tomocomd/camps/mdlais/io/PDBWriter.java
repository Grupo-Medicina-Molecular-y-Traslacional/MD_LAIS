/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2000-2003  The Jmol Development Team
 * Copyright (C) 2003-2007  The CDK Project
 *
 * Contact: cdk-devel@lists.sf.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package tomocomd.camps.mdlais.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IPDBStructure;
import org.openscience.cdk.protein.data.PDBAtom;
import org.openscience.cdk.protein.data.PDBMonomer;
import tomocomd.camps.mdlais.pdbfilter.AtomFilterType;
import tomocomd.camps.mdlais.protein.PDBProtein;
import tomocomd.camps.mdlais.protein.PDBStructure;
import tomocomd.camps.mdlais.protein.SSBond;

/**
 * Saves molecules in a rudimentary PDB format.
 *
 * @author econtreras
 * @cdk.module io
 * @cdk.iooptions
 * @cdk.githash
 */
public class PDBWriter{

    public final String SERIAL_FORMAT = "%5d";
    public final String ATOM_NAME_FORMAT = "%-5s";
    public final String POSITION_FORMAT = "%8.3f";
    public final String RESIDUE_FORMAT = "%s";
    public static final String newLine="\r\n";
    public static String COORDINATES_FORMAT = "%8.3f";

    private static BufferedWriter writer;

    public PDBWriter() 
    {
       
    }

    public static void writePDBPolymer(File folder, PDBProtein pdbPolymer,String name, AtomFilterType atomFilterType) throws IOException
    {
        writer = new BufferedWriter(new FileWriter(new File(folder, name)));

        writer.write(writeHeader(pdbPolymer.getProperty(CDKConstants.TITLE).toString()));//header
        
        writer.write(writeRemark(atomFilterType.toString()));
        
        writer.write(writeSecondaryStructureInformation(pdbPolymer));// secondary structure

        writer.write(writeConectInformation(pdbPolymer));// conect info

        String currentStrand = ((PDBMonomer) pdbPolymer.getMonomer(0)).getChainID();

        PDBAtom currentAtom, previousAtom;

        int monomerCount = pdbPolymer.getMonomerCount();
        
        PDBMonomer monomer = null;
        
        for (int i = 0; i < monomerCount; i++) 
        {
             monomer = pdbPolymer.getMonomer(i);
            
            if(!monomer.getChainID().equalsIgnoreCase(currentStrand))
            {
                PDBMonomer previousMonomer = pdbPolymer.getMonomer(i-1);
                
                previousAtom = (PDBAtom) previousMonomer.getLastAtom();

                writer.write(writeTerRecord(previousAtom));

                writer.newLine();
                
                currentStrand = monomer.getChainID();
            }
            
            for (int j = 0; j < monomer.getAtomCount(); j++) 
            {
                currentAtom = (PDBAtom) monomer.getAtom(j);
                
                writer.write(atomRecord(currentAtom));
                
                writer.newLine();
                
                writer.flush();
            }             
        }

        previousAtom = (PDBAtom) monomer.getLastAtom();

        writer.write(writeTerRecord(previousAtom));

        writer.newLine();

        writer.write("END   ");

        writer.flush();

        writer.close();
    }
    
    public static void writePDBPolymer(File folder, PDBProtein pdbPolymer, String name) throws IOException 
    {
        writer = new BufferedWriter(new FileWriter(new File(folder, name)));

        writer.write(writeHeader(pdbPolymer.getProperty(CDKConstants.TITLE).toString()));//header

        writer.write(writeSecondaryStructureInformation(pdbPolymer));// secondary structure

        writer.write(writeConectInformation(pdbPolymer));// conect info

        String currentStrand = ((PDBMonomer) pdbPolymer.getMonomer(0)).getChainID();

        PDBAtom currentAtom, previousAtom;

        int monomerCount = pdbPolymer.getMonomerCount();
        
        PDBMonomer monomer = null;
        
        for (int i = 0; i < monomerCount; i++) 
        {
             monomer = pdbPolymer.getMonomer(i);
            
            if(!monomer.getChainID().equalsIgnoreCase(currentStrand))
            {
                PDBMonomer previousMonomer = pdbPolymer.getMonomer(i-1);
                
                previousAtom = (PDBAtom) previousMonomer.getLastAtom();

                writer.write(writeTerRecord(previousAtom));

                writer.newLine();
                
                currentStrand = monomer.getChainID();
            }
            
            for (int j = 0; j < monomer.getAtomCount(); j++) 
            {
                currentAtom = (PDBAtom) monomer.getAtom(j);
                
                writer.write(atomRecord(currentAtom));
                
                writer.newLine();
                
                writer.flush();
            }             
        }

        previousAtom = (PDBAtom) monomer.getLastAtom();

        writer.write(writeTerRecord(previousAtom));

        writer.newLine();

        writer.write("END   ");

        writer.flush();

        writer.close();
    }
    public static String writeBlock(String block, PDBProtein pdbPolymer) throws IOException 
    {
        if (block.equalsIgnoreCase("header")) 
        {
            return writeHeader(pdbPolymer.getProperty(CDKConstants.TITLE).toString());
        } else if (block.equalsIgnoreCase("ss_info")) 
        {
            return writeSecondaryStructureInformation(pdbPolymer);
        } else if (block.equalsIgnoreCase("conect_info")) 
        {
            return writeConectInformation(pdbPolymer);
        } else if (block.equalsIgnoreCase("coordinates_info")) 
        {
        StringBuilder sb = new StringBuilder();
            
        String currentStrand = ((PDBMonomer) pdbPolymer.getMonomer(0)).getChainID();

        PDBAtom currentAtom, previousAtom;

        int monomerCount = pdbPolymer.getMonomerCount();
        
        PDBMonomer monomer = null;

            for (int i = 0; i < monomerCount; i++) 
        {
             monomer = pdbPolymer.getMonomer(i);
            
            if(!monomer.getChainID().equalsIgnoreCase(currentStrand))
            {
                PDBMonomer previousMonomer = pdbPolymer.getMonomer(i-1);
                
                previousAtom = (PDBAtom) previousMonomer.getLastAtom();

                sb.append(writeTerRecord(previousAtom));

                sb.append(newLine);
                
                currentStrand = monomer.getChainID();
            }
            
            for (int j = 0; j < monomer.getAtomCount(); j++) 
            {
                currentAtom = (PDBAtom) monomer.getAtom(j);
                
                sb.append(atomRecord(currentAtom));
                
                sb.append(newLine);
            }
        }

        previousAtom = (PDBAtom) monomer.getLastAtom();

        sb.append(writeTerRecord(previousAtom));

        sb.append(newLine);

        sb.append("END   ");
        
          return sb.toString();
        }
        else 
            return null;
    }

    

    private static String writeTerRecord(PDBAtom currentAtom) 
    {
        int serial = currentAtom.getSerial() + 1;

        String ser = String.format("%5d", serial);

        String resSeq = String.format("%4s", currentAtom.getResSeq());

        return "TER   " + ser + "      " + currentAtom.getResName() + " " + currentAtom.getChainID() + resSeq;
    }

    private static String writeRemark(String rem) 
    {
        int len = 80;

        StringBuilder sb = new StringBuilder();

        sb.append("REMARK");

        for (int i = 6; i < len; i++) 
        {
            sb.append(' ');
        }

        int index = 12;

        for (int i = 0; i < rem.length(); i++) 
        {
            sb.setCharAt(index++, rem.charAt(i));
        }
        
        sb.append(newLine);
        
        return sb.toString();
    }
    
    private static String writeHeader(String id) 
    {
        int len = 80;

        StringBuilder sb = new StringBuilder();

        sb.append("HEADER");

        for (int i = 6; i < len; i++) 
        {
            sb.append(' ');
        }

        int index = 62;

        for (int i = 0; i < id.length(); i++) 
        {
            sb.setCharAt(index++, id.charAt(i));
        }
        
        sb.append(newLine);
        
        return sb.toString();
    }

    private static String writeSecondaryStructureInformation(PDBProtein polymer) throws IOException 
    {
        List<PDBStructure> secondaryStructures = polymer.getSecondaryStructures();

        StringBuilder sb = new StringBuilder();

        for (IPDBStructure iPDBStructure : secondaryStructures) 
        {
            String idPDBRecord = writeSecondaryStructure(iPDBStructure);

            if (idPDBRecord != null) 
            {
                sb.append(idPDBRecord);

                sb.append(newLine);
            }
        }
        return sb.toString();
    }

    private static String writeSecondaryStructure(IPDBStructure structure) 
    {
        String structureType = structure.getStructureType();

        if (structureType != null) 
        {
            if (structureType.equalsIgnoreCase(PDBStructure.HELIX)) 
            {
                return writeHelix((PDBStructure) structure);
            } else if (structureType.equalsIgnoreCase(PDBStructure.SHEET)) 
            {
                return writeSheet((PDBStructure) structure);
            } else {
                if (structureType.equalsIgnoreCase(PDBStructure.TURN)) 
                {
                    return writeTurn((PDBStructure) structure);
                }
            }
        }

        return null;
    }

    private static String writeHelix(PDBStructure helix) {
        int len = 80;

        StringBuilder sb = new StringBuilder();

        sb.append("HELIX");

        for (int i = 6; i < len; i++) {
            sb.append(' ');
        }

        int index = 15;

        String startResidueName = helix.getStartResidueName();

        for (int i = 0; i < startResidueName.length(); i++) {
            sb.setCharAt(index++, startResidueName.charAt(i));
        }

        sb.setCharAt(19, helix.getStartChainID());

        index = 21;

        String serialNumber = String.format("%4s", helix.getStartSequenceNumber());

        for (int i = 0; i < serialNumber.length(); i++) {
            sb.setCharAt(index++, serialNumber.charAt(i));
        }

        sb.setCharAt(25, helix.getStartInsertionCode());// start idcode

        index = 27;

        String endResidueName = helix.getEndResidueName();

        for (int i = 0; i < endResidueName.length(); i++) {
            sb.setCharAt(index++, endResidueName.charAt(i));
        }

        sb.setCharAt(31, helix.getEndChainID());

        index = 33;

        String endSequenceNumber = String.format("%4s", helix.getEndSequenceNumber());

        for (int i = 0; i < endSequenceNumber.length(); i++) {
            sb.setCharAt(index++, endSequenceNumber.charAt(i));
        }

        sb.setCharAt(37, helix.getStartInsertionCode());

        /**
         * structure.setStructureType(PDBStructure.HELIX);
         * structure.setStartResidueName(cRead.substring(15, 17).trim());
         * structure.setStartChainID(cRead.charAt(19));
         * structure.setStartSequenceNumber(Integer.parseInt(cRead.substring(21,
         * 25).trim())); structure.setStartInsertionCode(cRead.charAt(25));
         * structure.setEndResidueName(cRead.substring(27, 29).trim());
         * structure.setEndChainID(cRead.charAt(31));
         * structure.setEndSequenceNumber(Integer.parseInt(cRead.substring(33,
         * 37).trim())); structure.setEndInsertionCode(cRead.charAt(37));
         */
        return sb.toString();
    }

    /**
     * 1 - 6 Record name "ATOM " 7 - 11 Integer serial Atom serial number. 13 -
     * * 16 Atom name Atom name. 17 Character altLoc Alternate location
     * indicator. 18 - 20 Residue name resName Residue name. 22 Character
     * chainID Chain identifier. 23 - 26 Integer resSeq Residue sequence number.
     * 27 AChar * iCode Code for insertion of residues.
     *
     * 31 - 38 Real(8.3) x Orthogonal coordinates for X in Angstroms. 39 - 46
     * Real(8.3) y Orthogonal coordinates for Y in Angstroms. 47 - 54 Real(8.3)
     * z Orthogonal coordinates for Z in Angstroms. 55 - 60 Real(6.2) occupancy
     * Occupancy. 61 - 66 Real(6.2) tempFactor Temperature factor. 77 - 78
     * LString(2) element Element symbol, right-justified. 79 - 80 LString(2)
     * charge Charge on the atom.
     *
     * @param atom
     */
    public static String atomRecord(PDBAtom atom) throws IOException 
    {
        int len = 60;// to save memory

        StringBuilder sb = new StringBuilder();

        sb.append("ATOM  ");

        for (int i = 6; i < len; i++) 
        {
            sb.append(' ');
        }

        int index = 6;

        String serial = String.format("%5s", atom.getSerial());

        for (int i = 0; i < serial.length(); i++) {
            sb.setCharAt(index, serial.charAt(i));

            index++;
        }

        String atomName;

        if (atom.getName().length() > 3) {
            atomName = String.format("%-4s", atom.getName());
        } else {
            atomName = String.format(" %-3s", atom.getName());
        }

        index = 12;

        for (int i = 0; i < atomName.length(); i++) {
            sb.setCharAt(index, atomName.charAt(i));

            index++;
        }

        index = 17;

        String resName = String.format("%-3s", atom.getResName());

        for (int i = 0; i < resName.length(); i++) {
            sb.setCharAt(index, resName.charAt(i));

            index++;
        }

        sb.setCharAt(21, atom.getChainID().charAt(0));

        index = 22;//resseq

        String resSeq = String.format("%4s", atom.getResSeq());

        for (int i = 0; i < resSeq.length(); i++) {
            sb.setCharAt(index, resSeq.charAt(i));

            index++;
        }

        index = 26;// insertion code

        String iCode = atom.getICode();

        if (iCode != null) {
            for (int i = 0; i < iCode.length(); i++) {
                sb.setCharAt(index, atom.getICode().charAt(i));

                index++;
            }
        }

        // coordinates block
        index = 30;

        String x = String.format(Locale.ENGLISH, COORDINATES_FORMAT, atom.getPoint3d().x);

        String y = String.format(Locale.ENGLISH, COORDINATES_FORMAT, atom.getPoint3d().y);

        String z = String.format(Locale.ENGLISH, COORDINATES_FORMAT, atom.getPoint3d().z);

        for (int i = 0; i < x.length(); i++) {
            sb.setCharAt(index, x.charAt(i));

            index++;
        }

        index = 38;

        for (int i = 0; i < y.length(); i++) {
            sb.setCharAt(index, y.charAt(i));

            index++;
        }

        index = 46;

        for (int i = 0; i < z.length(); i++) 
        {
            sb.setCharAt(index, z.charAt(i));

            index++;
        }

        return sb.toString();
    }

    private static String writeSheet(PDBStructure sheet) 
    {
        int len = 80;

        StringBuilder sb = new StringBuilder();

        sb.append("SHEET");

        for (int i = 6; i < len; i++) {
            sb.append(' ');
        }

        int index = 17;

        String startResidueName = sheet.getStartResidueName();

        for (int i = 0; i < startResidueName.length(); i++) {
            sb.setCharAt(index++, startResidueName.charAt(i));
        }

        sb.setCharAt(21, sheet.getStartChainID());

        index = 22;

        String serialNumber = String.format("%4s", sheet.getStartSequenceNumber());

        for (int i = 0; i < serialNumber.length(); i++) {
            sb.setCharAt(index++, serialNumber.charAt(i));
        }
        if (sheet.getStartInsertionCode() != null) {
            sb.setCharAt(26, sheet.getStartInsertionCode());//start idcode
        }
        index = 28;

        String endResidueName = sheet.getEndResidueName();

        for (int i = 0; i < endResidueName.length(); i++) {
            sb.setCharAt(index++, endResidueName.charAt(i));
        }

        sb.setCharAt(32, sheet.getEndChainID());

        index = 33;

        String endSequenceNumber = String.format("%4s", sheet.getEndSequenceNumber());

        for (int i = 0; i < endSequenceNumber.length(); i++) {
            sb.setCharAt(index++, endSequenceNumber.charAt(i));
        }
        if (sheet.getEndInsertionCode() != null) {
            sb.setCharAt(37, sheet.getEndInsertionCode()); //end idcode
        }
        /*
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
         */
        return sb.toString();

    }
    /*
     1 -  6      Record name      "TURN "
     8 - 10      Integer          seq           Turn number; starts with 1 and
     increments by one.
     12 - 14      LString(3)       turnId        Turn identifier
     16 - 18      Residue name     initResName   Residue name of initial residue in
     turn.
     20           Character        initChainId   Chain identifier for the chain
     containing this turn.
     21 - 24      Integer          initSeqNum    Sequence number of initial residue
     in turn.
     25           AChar            initICode     Insertion code of initial residue 
     in turn.
     27 - 29      Residue name     endResName    Residue name of terminal residue 
     of turn.
     31           Character        endChainId    Chain identifier for the chain
     containing this turn.
     32 - 35      Integer          endSeqNum     Sequence number of terminal 
     residue of turn.
     */

    private static String writeTurn(PDBStructure turn) {
        int len = 80;

        StringBuilder sb = new StringBuilder();

        sb.append("TURN");

        for (int i = 6; i < len; i++) {
            sb.append(' ');
        }

        int index = 15;

        String startResidueName = turn.getStartResidueName();

        for (int i = 0; i < startResidueName.length(); i++) {
            sb.setCharAt(index++, startResidueName.charAt(i));
        }

        sb.setCharAt(19, turn.getStartChainID());

        index = 20;

        String serialNumber = String.format("%4s", turn.getStartSequenceNumber());

        for (int i = 0; i < serialNumber.length(); i++) {
            sb.setCharAt(index++, serialNumber.charAt(i));
        }
        if (turn.getStartInsertionCode() != null) {
            sb.setCharAt(24, turn.getStartInsertionCode());
        }

        index = 26;

        String endResidueName = turn.getEndResidueName();

        for (int i = 0; i < endResidueName.length(); i++) {
            sb.setCharAt(index++, endResidueName.charAt(i));
        }

        sb.setCharAt(30, turn.getEndChainID());

        index = 31;

        String endSequenceNumber = String.format("%4s", turn.getEndSequenceNumber());

        for (int i = 0; i < endSequenceNumber.length(); i++) {
            sb.setCharAt(index++, endSequenceNumber.charAt(i));
        }
        if (turn.getEndInsertionCode() != null) {
            sb.setCharAt(35, turn.getEndInsertionCode());
        }

        return sb.toString();
    }

    private static String writeConectInformation(PDBProtein pdbPolymer) throws IOException 
    {
        List<SSBond> ssBonds = pdbPolymer.getSsBonds();

        StringBuilder sb = new StringBuilder();

        for (SSBond ssBond : ssBonds) 
        {
            sb.append(ssBond.getRecord());

            sb.append(newLine);
        }
        
        return sb.toString();
    }
    
}
