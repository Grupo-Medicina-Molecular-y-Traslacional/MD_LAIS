package tomocomd.camps.mdlais.gui;

import org.openscience.cdk.qsar.IDescriptor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptor;

/**
 * @author Rajarshi Guha
 */
public class CDKDescUtils {

    /**
     * Checks whether the input file is in SMI format.
     * <p/>
     * The approach I take is to read the first line of the file. This should
     * splittable to give two parts. The first part should be a valid SMILES
     * string. If so this method returns true, otherwise false
     *
     * @param filename The file to consider
     * @return true if the file is in SMI format, otherwise false
     */
    public static boolean isMacOs() {
        String lcOSName = System.getProperty("os.name").toLowerCase();
        return lcOSName.startsWith("mac os x");
    }

    public static Comparator getDescriptorComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                if (o1 instanceof MolecularDescriptor && o2 instanceof MolecularDescriptor) {
                    MolecularDescriptor ad1 = (MolecularDescriptor) o1;
                    MolecularDescriptor ad2 = (MolecularDescriptor) o2;
                    
                            if (ad1.getX().ordinal()< ad2.getX().ordinal()) {
                                return -1;
                            } else if (ad1.getX().ordinal() > ad2.getX().ordinal()) {
                                return 1;
                            } else {
                                return 0;
                            }
                } else {
                    IDescriptor desc1 = (IDescriptor) o1;
                    IDescriptor desc2 = (IDescriptor) o2;

                    String[] comp1 = desc1.getSpecification().getSpecificationReference().split("#");
                    String[] comp2 = desc2.getSpecification().getSpecificationReference().split("#");

                    return comp1[1].compareTo(comp2[1]);
                }
            }
        };
    }

    /**
     * Sorts the vector according to the current invariance, corresponds to step
     * 3
     *
     * @param v the invariance pair vector
     * @cdk.todo can this be done in one loop?
     */
    static public void sortDescriptorArrayList(ArrayList v) {
        Object[] array = v.toArray();

        legacyMergeSort(array, CDKDescUtils.getDescriptorComparator());

        ListIterator i = v.listIterator();
        for (int j = 0; j < array.length; j++) {
            i.next();
            i.set(array[j]);
        }
    }

    static private void legacyMergeSort(Object[] a, Comparator c) {
        Object[] aux = a.clone();
        mergeSort(aux, a, 0, a.length, 0, c);
    }

    static private final int INSERTIONSORT_THRESHOLD = 7;

    static private void mergeSort(Object[] src,
            Object[] dest,
            int low, int high, int off,
            Comparator c) {
        int length = high - low;

        // Insertion sort on smallest arrays
        if (length < INSERTIONSORT_THRESHOLD) {
            for (int i = low; i < high; i++) {
                for (int j = i; j > low && c.compare(dest[j - 1], dest[j]) > 0; j--) {
                    swap(dest, j, j - 1);
                }
            }
            return;
        }

        // Recursively sort halves of dest into src
        int destLow = low;
        int destHigh = high;
        low += off;
        high += off;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid, -off, c);
        mergeSort(dest, src, mid, high, -off, c);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (c.compare(src[mid - 1], src[mid]) <= 0) {
            System.arraycopy(src, low, dest, destLow, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0) {
                dest[i] = src[p++];
            } else {
                dest[i] = src[q++];
            }
        }
    }

    static private void swap(Object[] x, int a, int b) {
        Object t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
}
