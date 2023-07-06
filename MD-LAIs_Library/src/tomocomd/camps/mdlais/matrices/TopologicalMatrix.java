package tomocomd.camps.mdlais.matrices;

import org.openscience.cdk.graph.PathTools;
import org.openscience.cdk.graph.matrix.IGraphMatrix;
import org.openscience.cdk.interfaces.IAtomContainer;
import tomocomd.camps.mdlais.protein.PDBProtein;

/**
 *
 * @author econtreras
 */
public class TopologicalMatrix implements IGraphMatrix {

    /**
     * Returns the topological matrix for the given protein.
     *
     * @param container The AtomContainer for which the matrix is calculated
     * @return A topological matrix representating this AtomContainer
     */
    public static int[][] getDistanceMatrix(IAtomContainer container) 
    {
        int[][] conMat = getConnectivityMatrix(container);
        
        int[][] TopolDistance = PathTools.computeFloydAPSP(conMat);

        return TopolDistance;
    }

    public static int[][] getConnectivityMatrix(IAtomContainer container) 
    {
        int aminoAcidCount = ((PDBProtein) container).getMonomerCount();

        int[][] conMat = new int[aminoAcidCount][aminoAcidCount];

        for (int i = 0; i < aminoAcidCount - 1; i++) 
        {
            conMat[i][i + 1] = 1;

            conMat[i + 1][i] = 1;
        }
        
        return conMat;
    }
    
    public static double[][] getConnectivityMatrixAsDouble(IAtomContainer container) 
    {
        int aminoAcidCount = ((PDBProtein) container).getMonomerCount();

        double[][] conMat = new double[aminoAcidCount][aminoAcidCount];

        for (int i = 0; i < aminoAcidCount - 1; i++) 
        {
            conMat[i][i + 1] = 1;

            conMat[i + 1][i] = 1;
        }
        
        return conMat;
    }
    
   public static double[][] getDistanceMatrixAsDouble(IAtomContainer container) 
    {
        int [][] TopolDistance = getDistanceMatrix(container);
        
        int n = TopolDistance.length;
        
        double[][] TopolDistanceAsDouble = new double[n][n];
        
        for (int i = 0; i < n; i++) 
        {
            for (int j = i+1; j < n; j++) 
            {
                int distance = TopolDistance[i][j];
                
                TopolDistanceAsDouble[i][j]=distance;
                
                TopolDistanceAsDouble[j][i]=distance;
            }
        }
        
        return TopolDistanceAsDouble;        
    }
}
