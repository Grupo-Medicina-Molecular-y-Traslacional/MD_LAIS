package tomocomd.camps.mdlais.tools.invariants;

import java.util.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;
import java.util.List;
import org.openscience.cdk.exception.CDKException;

public class AllKierHall {

    public static Vector calculate(IAtomContainer localAtomContainer, double[] lais, int option) throws CDKException 
    {
        List<List<Integer>> subgraph = PathKierHall.order(localAtomContainer, option);
        
        return ChiIndexUtils.vectorSimplexIndex(localAtomContainer, subgraph, lais);
    }
}
