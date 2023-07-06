package tomocomd.camps.mdlais.tools.invariants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.protein.data.PDBAtom;
import tomocomd.camps.mdlais.matrices.TopologicalMatrix;
import tomocomd.camps.mdlais.protein.PDBProtein;

/**
 *
 * @author jricardo
 */
public class Classics {

    public static final Hashtable<Integer, Double> scatterParameter;

    static {
        scatterParameter = new Hashtable<>();
        scatterParameter.put(1, 1d);
        scatterParameter.put(2, 0.5d);
        scatterParameter.put(3, 1 / 6d);
        scatterParameter.put(4, 1 / 24d);
        scatterParameter.put(5, 1 / 120d);
        scatterParameter.put(6, 1 / 720d);
        scatterParameter.put(7, 1 / 5040d);
    }

    public static final Hashtable<String, AminoAcidCharacter> aminoAcidCharacter;

    static {
        aminoAcidCharacter = new Hashtable<>();

        aminoAcidCharacter.put("ALA", AminoAcidCharacter.hydrophobic);
        aminoAcidCharacter.put("ARG", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("ASN", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("ASP", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("CYS", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("GLU", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("GLN", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("GLY", AminoAcidCharacter.hydrophobic);
        aminoAcidCharacter.put("HIS", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("ILE", AminoAcidCharacter.hydrophobic);
        aminoAcidCharacter.put("LEU", AminoAcidCharacter.hydrophobic);
        aminoAcidCharacter.put("LYS", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("MET", AminoAcidCharacter.amphipatic);
        aminoAcidCharacter.put("PHE", AminoAcidCharacter.amphipatic);
        aminoAcidCharacter.put("PRO", AminoAcidCharacter.hydrophobic);
        aminoAcidCharacter.put("SER", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("THR", AminoAcidCharacter.hydrophilic);
        aminoAcidCharacter.put("TRP", AminoAcidCharacter.amphipatic);
        aminoAcidCharacter.put("TYR", AminoAcidCharacter.amphipatic);
        aminoAcidCharacter.put("VAL", AminoAcidCharacter.hydrophobic);
    }

    /*
    Information-Theoretic
     */
    public static double[] MeanInformation(double[] a) {
        double[] result = null;
        boolean[] selections = new boolean[a.length];
        int n = a.length;
        ArrayList<ArrayList<String>> equivalenceClass = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!selections[i]) {
                ArrayList<String> subClass = new ArrayList<>();
                subClass.add(String.valueOf(a[i]));
                selections[i] = true;
                for (int j = i + 1; j < n; j++) {
                    if (a[i] == a[j] && !selections[j]) {
                        subClass.add(String.valueOf(a[j]));
                        selections[j] = true;
                    }
                }
                equivalenceClass.add(subClass);
            }
        }
        result = new double[equivalenceClass.size()];
        for (int i = 0; i < result.length; i++) {
            double size = equivalenceClass.get(i).size();
            double total = n;
            double pi = (double) (size / total);
            result[i] = -1 * pi * Tools.Log2(pi);
        }
        return result;
    }

    public static double TotalInformation(double[] a) {
        double[] result = null;
        boolean[] selections = new boolean[a.length];
        int n = a.length;
        ArrayList<ArrayList<String>> equivalenceClass = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!selections[i]) {
                ArrayList<String> subClass = new ArrayList<>();
                subClass.add(String.valueOf(a[i]));
                selections[i] = true;
                for (int j = i + 1; j < n; j++) {
                    if (a[i] == a[j] && !selections[j]) {
                        subClass.add(String.valueOf(a[j]));
                        selections[j] = true;
                    }
                }
                equivalenceClass.add(subClass);
            }
        }
        result = new double[equivalenceClass.size()];
        double sum = 0;
        for (int i = 0; i < result.length; i++) {
            double size = equivalenceClass.get(i).size();
            sum += size * Tools.Log2(size);
        }
        return n * Tools.Log2(n) - sum;
    }

    public static double StandardarizedInformation(double[] a) {
        double TI = TotalInformation(a);
        int n = a.length;
        return TI / (n * Tools.Log2(n));
    }

    public static double[] MeanInformationN(double[] a) {

        double total = a.length;

        Variable v = new Variable("", a);

        int[] aux = v.distribucionDeValores(a.length);

        double[] result = new double[a.length];

        for (int i = 0; i < aux.length; i++) {
            double size = aux[i];
            double pi = (double) (size / total);
            result[i] = pi != 0 ? -1 * pi * Tools.Log2(pi) : 0;
        }
        return result;
    }

    public static double TotalInformationN(double[] a) {

        double n = a.length;

        Variable v = new Variable("", a);

        int[] aux = v.distribucionDeValores(a.length);

        double sum = 0;

        for (int i = 0; i < aux.length; i++) {
            double e = aux[i];
            if (e != 0) {
                sum += e * Tools.Log2(e);
            }
        }
        return n * Tools.Log2(n) - sum;
    }

    public static double StandardarizedInformationN(double[] a) {
        double TI = TotalInformationN(a);
        int n = a.length;
        return TI / (n * Tools.Log2(n));
    }

    public static double[] MeanInformationSS(double[] a) 
    {
        double[] normalizedVector = Tools.NormalizeMinMax(a);

        double sum = Norms.MinkoskyNorm(normalizedVector, 1);

        sum = sum != 0 ? sum : 1;

        for (int i = 0; i < normalizedVector.length; i++) 
        {
            double pi = normalizedVector[i] / sum;

            normalizedVector[i] = pi != 0 ? (-pi * Tools.Log2(pi)) : 0;
        }

        return normalizedVector;
    }
    
    /*
    Classic algorithms
    */
    public static double[] Autocorrelation(double[] a, IAtomContainer container, int k) throws Exception 
    {
        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        int n = a.length;
        
        double [] lai = new double[n];
        
        for (int i = 0; i < n; i++) 
        {
            double li = a[i];
            
            for (int j = i+1; j < n; j++) 
            {
                double dij = distanceMatrix[i][j];

                if (dij == k) 
                {
                    double ri = li * a[j];
                    lai[i]+=ri;
                    lai[j]+=ri;
                }
            }
        }
        
        return lai;
    }
    
    public static double[] Gravitational(double[] a, IAtomContainer container, int k) throws Exception 
    {
        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        int n = a.length;
        
        double [] lai = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double li = a[i];
            
            for (int j = i+1; j < n; j++) 
            {
                double distance = distanceMatrix[i][j];

                if (distance == k) 
                {
                    double ri = (li * a[j])/k;
                    
                    lai[i]+=ri;
                    lai[j]+=ri;
                }
            }
        }

        return lai;
    }
    
    public static double[] TotalSumLagK(double[] a, IAtomContainer container, int k) throws Exception 
    {
        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        int n = a.length;
        
        double [] lai = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double li = a[i];
            
            for (int j = i+1; j < n; j++) 
            {
                double distance = distanceMatrix[i][j];

                if (distance == k) 
                {
                    double ri = (li + a[j]);
            
                    lai[i]+= ri;
                    lai[j]+= ri;
                }
            }
        }

        return lai;
    }
    
    public static Vector[] KierHall(double[] lovis, AtomContainer container, int start, int end) throws CDKException 
    {
        int len = end == start ? 1 : end - start;

        Vector[] vk = new Vector[len];

        if (len == 1) 
        {
            vk[0] = AllKierHall.calculate(container, lovis, start);
        } else 
        {
            for (int i = start; i < end; i++) 
            {
                vk[i] = AllKierHall.calculate(container, lovis, i+1);
            }
        }

        return vk;
    }
    
    
    public static double[] Geary(double[] a, IAtomContainer container, int k) throws Exception {

        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        int n = ((PDBProtein) container).getMonomerCount();

        double mean = Means.ArithmeticMean(a);

        double quotient = 0;

        double result[] = new double[n];

        for (int i = 0; i < n; i++) {
            double li = a[i];
            quotient += Math.pow(li - mean, 2);
            int deltaKi = 0;

            for (int j = 0; j < n; j++) {
                double dij = distanceMatrix[i][j];

                if (dij == k) {
                    result[i] += Math.pow(li - a[j], 2);

                    deltaKi++;
                }
            }

            result[i] /= deltaKi;
        }

        quotient *= n - 1;

        for (int i = 0; i < result.length; i++) {
            result[i] /= quotient;
        }

        return result;
    }

    
    public static double[] IvanciucBalaban(double[] lovis, AtomContainer molecule) 
    {
        int ringCount = 0;

        double[][] connMatrix = TopologicalMatrix.getConnectivityMatrixAsDouble(molecule);

        if (connMatrix == null) 
        {
            return new double[0];
        }

        int monomerCount = ((PDBProtein) molecule).getMonomerCount();

        return IvanciucBalabanType(lovis, lovis.length, monomerCount, monomerCount - 1, ringCount, connMatrix);
    }

    public static double[] IvanciucBalabanType(double[] lovis, int longitud, int atomCount, int bondCount, int ringCount, double[][] connMatrix) 
    {
        double iB = ((Math.pow(atomCount, 2) + bondCount) / (atomCount + ringCount + 1)) * 0.5;

        double[] IB = new double[longitud];
        
        for (int i = 0; i < longitud; i++) 
        {
            double Li = lovis[i];
            
            double sum = 0;
            
            for (int j = 0; j < longitud; j++) 
            {
                double Cij = connMatrix[i][j];
                
                double mult = Li * lovis[j];
                
                double it = 1.0 / (mult == 0 ? 1 : mult);
                
                sum+= (it * Cij);
            }
            IB[i] = iB * sum;
        }
        return IB;
    }

    public static double[] ElectroTopologicalState(double[] lovis, int length, AtomContainer molecule) 
    {
        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(molecule);

        double[] Si = new double[length];

        for (int i = 0; i < length; i++) 
        {
            double Li = lovis[i];

            double sum = 0;

            for (int j = 0; j < length; j++) 
            {
                double Dij = distanceMatrix[i][j] + 1;

                sum += (Li - lovis[j]) / (Dij * Dij);
            }

            Si[i] = Li + sum;
        }

        return Si;
    }

    //Potential of a charge distribution
    public static double[] PotentialChargeDistribution(double[] lovis, AtomContainer molecule) 
    {
        int n = molecule.getAtomCount();

        double[] pcdlovis = new double[n];

        int centerIndex = n / 2;

        for (int i = 0; i < n; i++) 
        {
            int distance2Center = Math.abs(centerIndex - i);

            double val = distance2Center != 0 ? distance2Center : 1;

            pcdlovis[i] = lovis[i] / val;
        }

        return pcdlovis;
    }

    public static double[] ConnectivityEccentricity(double[] lovis, AtomContainer molecule) 
    {
        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(molecule);

        if (distanceMatrix == null) {
            return new double[0];
        }

        int n = lovis.length;

        double[] ceilovis = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double[] rowDistances = distanceMatrix[i];

            double eccentricity = Statistics.XMax(rowDistances);

            ceilovis[i] = lovis[i] / eccentricity;
        }

        return ceilovis;
    }

    //Beteringhe–Filip–Tarko 
    public static double[] BeteringheFilipTarko(double[] lovis, AtomContainer molecule) 
    {
        int n = lovis.length;

        float scalingFactor = 1f / (float) n;

        double[] btflovis = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double value = lovis[i];

            if (value != 0) 
            {
                btflovis[i] = scalingFactor * Math.log(value * value);
            }
        }

        return btflovis;
    }

    public static double[] RadialDistributionFunction(double[] a, IAtomContainer container, int k) throws Exception {

        int n = container.getAtomCount();

        double[] rads = new double[]{(double) n / 8d, (double) n / 7d, (double) n / 6d, (double) n / 5d, (double) n / 4d,
                                    (double) n / 3d,(double) n / 2d};

        double Radius = rads[k - 1];

        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        double[] g = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double gRadius = 0;
            
            double ai = a[i];
            
            for (int j = i + 1; j < n; j++) 
            {
                double power = Radius - distanceMatrix[i][j];

                gRadius += ai * a[j] * Math.exp(-100 *power * power);
            }

            g[i] = gRadius / n;
        }

        return g;
    }

    public static double[] InteractionSpectrum(double[] a, IAtomContainer container, int k) throws Exception {

        int n = container.getAtomCount();

        double[] rads = new double[]{(double) n / 8d, (double) n / 7d, (double) n / 6d, (double) n / 5d, (double) n / 4d,
                                    (double) n / 3d,(double) n / 2d};
        double Radius = rads[k - 1];

        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        double[] lai = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double ias = 0;

            for (int j = 0; j < n; j++) 
            {
                double num = a[i] * a[j];

                double den = 1 + Math.sqrt(Math.pow(Radius - distanceMatrix[i][j], 2) / 0.1d);

                ias += num / den;
            }

            lai[i] = ias;
        }

        return lai;
    }

    public static double[] MoRSE(double[] a, IAtomContainer container, int k) throws Exception {

        int n = container.getAtomCount();

        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        double s = scatterParameter.get(k);

        double[] result = new double[a.length];

        for (int i = 1; i < n; i++) 
        {
            double mse = 0;
            
            for (int j = 0; j < i - 1; j++) 
            {
                double den = s * distanceMatrix[i][j];

                mse+=a[i] * a[j] * Math.sin(den) / den;
            }
            
            result[i] = mse;
        }

        return result;
    }

    public static double[] AmphiphilicMoments(double[] lovis, IAtomContainer molecule, int option) 
    {
        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(molecule);

        if (distanceMatrix == null) 
        {
            return new double[0];
        }

        AminoAcidCharacter ac = AminoAcidCharacter.values()[option - 1];

        int n = lovis.length;

        double[] amphillovis = new double[n];

        for (int i = 0; i < n; i++) 
        {
            double[] rowDistances = distanceMatrix[i];

            double distance2Farthest = 0;

            for (int j = 0; j < n; j++) 
            {
                PDBAtom atom = (PDBAtom) molecule.getAtom(j);

                String aaType = atom.getResName();

                if (ac == aminoAcidCharacter.get(aaType)) 
                {
                    distance2Farthest = Math.max(distance2Farthest,
                            rowDistances[j]);
                }
            }

            amphillovis[i] = lovis[i] * distance2Farthest;
        }

        return amphillovis;
    }

    
    public static double[] aaLevelMoRSE(double[] a, IAtomContainer container, int k) throws Exception 
    {
        int n = container.getAtomCount();

        double[][] distanceMatrix = TopologicalMatrix.getDistanceMatrixAsDouble(container);

        double s = scatterParameter.get(k);

        double[] result = new double[n];

        for (int i = 1; i < n; i++) 
        {
            for (int j = 0; j < i - 1; j++) 
            {
                double den = s * distanceMatrix[i][j];

                result[i]+= (a[i] * a[j] * Math.sin(den) / den);
            }
        }

        return result;
    }
}
