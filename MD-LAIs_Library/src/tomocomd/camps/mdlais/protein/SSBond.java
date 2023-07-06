package tomocomd.camps.mdlais.protein;

/**
 *
 * --------------------------------------------------------------------------------
1 - 6 Record name "SSBond"
8 - 10 Integer serNum Serial number.
12 - 14 LString(3) "CYS" Residue name.
16 Character chainID1 Chain identifier.
18 - 21 Integer seqNum1 Residue sequence number.
22 AChar icode1 Insertion code.
26 - 28 LString(3) "CYS" Residue name.
30 Character chainID2 Chain identifier.
32 - 35 Integer seqNum2 Residue sequence number.
* 
 * @author econtreras
 */
public class SSBond {
    
    private final String record;
    private final int cys1Number;
    private final int cys2Number;
    private final String cys1Name;
    private final String chain1Name;
    private final String cys2Name;
    private final String chain2Name;
   
    public SSBond(String record) 
    {
        this.record = record.substring(0, 36);
        
        this.cys1Name = record.substring(11, 14).trim();
        
        this.chain1Name = record.substring(15,16);
        this.cys1Number = Integer.parseInt(record.substring(17, 21).trim());
        
        this.cys2Name = record.substring(25, 28).trim();        
        this.chain2Name = record.substring(29, 30).trim();
        this.cys2Number = Integer.parseInt(record.substring(31, 35).trim());
    }

    public String getRecord() {
        return record;
    }

    public int getCys1Number() {
        return cys1Number;
    }

    public int getCys2Number() {
        return cys2Number;
    }

    public String getCys1Name() {
        return cys1Name;
    }

    public String getChain1Name() {
        return chain1Name;
    }

    public String getCys2Name() {
        return cys2Name;
    }

    public String getChain2Name() {
        return chain2Name;
    }
}
