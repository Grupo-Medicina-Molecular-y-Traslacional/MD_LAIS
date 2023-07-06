package tomocomd.camps.mdlais.tools.invariants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.mcss.RMap;

/**
 * Utility methods for chi index calculations.
 * <p/> These methods are common to all the types of chi index calculations and
 * can be used to evaluate path, path-cluster, cluster and chain chi indices.
 *
 * @author Rajarshi Guha @cdk.module qsarmolecular @cdk.githash
 */
class ChiIndexUtils {

    /**
     * Gets the fragments from a target
     * <code>AtomContainer</code> matching a set of query fragments.
     * <p/> This method returns a list of lists. Each list contains the atoms of
     * the target
     * <code>AtomContainer</code> that arise in the mapping of bonds in the
     * target molecule to the bonds in the query fragment. The query fragments
     * should be constructed using the
     * <code>createAnyAtomAnyBondContainer</code> method of the
     * <code>QueryAtomContainerCreator</code> CDK class, since we are only
     * interested in connectivity and not actual atom or bond type information.
     *
     * @param atomContainer The target
     * <code>AtomContainer</code>
     * @param queries An array of query fragments
     * @return A list of lists, each list being the atoms that match the query
     * fragments
     */
    public static List<List<Integer>> getFragments(IAtomContainer atomContainer, QueryAtomContainer[] queries) {
        List<List<Integer>> uniqueSubgraphs = new ArrayList<List<Integer>>();
        for (QueryAtomContainer query : queries) {
            List subgraphMaps = null;
            try {
                // we get the list of bond mappings
                subgraphMaps = UniversalIsomorphismTester.getSubgraphMaps(atomContainer, query);
            } catch (CDKException e) {
                e.printStackTrace();
            }
            if (subgraphMaps == null) {
                continue;
            }
            if (subgraphMaps.size() == 0) {
                continue;
            }

            // get the atom paths in the unique set of bond maps
            uniqueSubgraphs.addAll(getUniqueBondSubgraphs(subgraphMaps, atomContainer));
        }

        // lets run a check on the length of each returned fragment and delete
        // any that don't match the length of out query fragments. Note that since
        // sometimes a fragment might be a ring, it will have number of atoms
        // equal to the number of bonds, where as a fragment with no rings
        // will have number of atoms equal to the number of bonds+1. So we need to check
        // fragment size against all unique query sizes - I get lazy and don't check
        // unique query sizes, but the size of each query
        List<List<Integer>> retValue = new ArrayList<List<Integer>>();
        for (List<Integer> fragment : uniqueSubgraphs) {
            for (QueryAtomContainer query : queries) {
                if (fragment.size() == query.getAtomCount()) {
                    retValue.add(fragment);
                    break;
                }
            }
        }
        return retValue;
    }

    /**
     * Evaluates the simple chi index for a set of fragments.
     *
     * @param atomContainer The target <code>AtomContainer</code>
     * @param fragLists      A list of fragments
     * @return The simple chi index
     */
    public static double evalSimpleIndex(IAtomContainer atomContainer, List<List<Integer>> fragLists) {
        double sum = 0;
        for (List<Integer> fragList : fragLists) {
            double prod = 1.0;
            for (Integer atomSerial : fragList) {
                IAtom atom = atomContainer.getAtom(atomSerial);
                int nconnected = atomContainer.getConnectedAtomsCount(atom);
                prod = prod * nconnected;
            }
            if (prod != 0) {
                sum += 1.0 / Math.sqrt(prod);
            }
        }
        return sum;
    }

    public static Vector vectorSimplexIndex(IAtomContainer atomContainer, List<List<Integer>> fragLists, double[] a) 
    {
        Vector vk = new Vector();
        
        double [] ak = new double[a.length];
        
        int index = 0;
                
        for (List<Integer> fragList : fragLists) 
        {
            double prod = 1;
           
            for (Integer atomSerial : fragList) 
            {
                prod = prod * a[atomSerial];
            }
            
            double result = prod<=0? 0 : 1/Math.sqrt(prod);
            
            ak[index++] = result;
        }
        
        vk.add(ak);
        
        return vk;
    }
    
    /**
     * Converts a set of bond mappings to a unique set of atom paths.
     * <p/>
     * This method accepts a <code>List</code> of bond mappings. It first
     * reduces the set to a unique set of bond maps and then for each bond map
     * converts it to a series of atoms making up the bonds.
     *
     * @param subgraphs A <code>List</code> of bon mappings
     * @param ac        The molecule we are examining
     * @return A unique <code>List</code> of atom paths
     */
    private static List<List<Integer>> getUniqueBondSubgraphs(List subgraphs, IAtomContainer ac) {
        List<List<Integer>> bondList = new ArrayList<List<Integer>>();
        for (Object subgraph : subgraphs) {
            List current = (List) subgraph;
            List<Integer> ids = new ArrayList<Integer>();
            for (Object aCurrent : current) {
                RMap rmap = (RMap) aCurrent;
                ids.add(rmap.getId1());
            }
            Collections.sort(ids);
            bondList.add(ids);
        }

        // get the unique set of bonds
        HashSet<List<Integer>> hs = new HashSet<List<Integer>>(bondList);
        bondList = new ArrayList<List<Integer>>(hs);

        List<List<Integer>> paths = new ArrayList<List<Integer>>();
        for (Object aBondList1 : bondList) {
            List aBondList = (List) aBondList1;
            List<Integer> tmp = new ArrayList<Integer>();
            for (Object anABondList : aBondList) {
                int bondNumber = (Integer) anABondList;
                for (IAtom atom : ac.getBond(bondNumber).atoms()) {
                    Integer atomInt = ac.getAtomNumber(atom);
                    if (!tmp.contains(atomInt)) {
                        tmp.add(atomInt);
                    }
                }
            }
            paths.add(tmp);
        }
        return paths;
    }
}
