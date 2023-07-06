package tomocomd.camps.mdlais.tools.invariants;

import java.util.ArrayList;
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;

public class PathKierHall {

    public static List<List<Integer>> order(IAtomContainer atomContainer, int order) {
        List<List<Integer>> fragments = new ArrayList<List<Integer>>();

        int n = atomContainer.getAtomCount();

        for (int i = 0; i < n - order; i++) {
            List<Integer> tmp = new ArrayList<Integer>();
            tmp.add(i);

            for (int j = 1; j <= order; j++) {
                tmp.add(i + j);
            }

            fragments.add(tmp);
        }

        for (int i = n - order; i < n; i++) {
            if (i >= 0) {
                List<Integer> tmp = new ArrayList<Integer>();
                tmp.add(i);

                for (int j = 1; j <= order; j++) {
                    if (i + j < n) {
                        tmp.add(i + j);
                    }
                }

                fragments.add(tmp);
            }

        }

        return fragments;
    }
}
