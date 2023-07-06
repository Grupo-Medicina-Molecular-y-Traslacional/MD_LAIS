package tomocomd.camps.mdlais.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.RecursiveAction;
import javax.swing.JProgressBar;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import tomocomd.camps.mdlais.descriptors.MolecularDescriptor;
import tomocomd.camps.mdlais.descriptors.result.AlgebraicDescriptorResult;
import tomocomd.camps.mdlais.exceptions.ExceptionInfo;
import tomocomd.camps.mdlais.weights.WeightConfiguration;
import tomocomd.camps.mdlais.tools.invariants.Choquet;
import tomocomd.camps.mdlais.tools.invariants.Classics;
import tomocomd.camps.mdlais.tools.invariants.DimensionType;
import tomocomd.camps.mdlais.tools.invariants.InvariantType;
import tomocomd.camps.mdlais.tools.invariants.Means;
import tomocomd.camps.mdlais.tools.invariants.Norms;
import tomocomd.camps.mdlais.tools.invariants.OWAWA;
import tomocomd.camps.mdlais.tools.invariants.Statistics;

/**
 *
 * @author cesar
 * @author econtreras
 */
public class ProteinCalculatorTask extends RecursiveAction {

    static public boolean canceled;

    final int MAX_K = 7;

    private int proc;
    private int proteinCount;
    private int start, end;
    private boolean batchMode;

    private IMolecule protein;
    private List<IDescriptor> descriptors;
    private Object[] uiNewParameters;
    private WeightConfiguration cutConfig;

    private StringBuffer history;

    private boolean isAA_level, isAALevelList;
    private int windowSize;

    private List<String> dataItems;
    private List<List<String>> aaDataItems;
    private List<String> aaHeaderItems;
    private List<String> headerItems;
    private List<ExceptionInfo> exceptionList;

    private String descName;
    private List<String> descriptorsName;

    private ArrayList<ProteinCalculatorTask> tasks;

    private int[] currentDescriptor;
    private JProgressBar progressBar2 = null;

    private boolean listMode;

    ProteinCalculatorTask() {

    }

    private ProteinCalculatorTask(List<IDescriptor> descriptors, boolean batchMode, int proc,
            Object[] uiNewParameters,
            WeightConfiguration cutConfig) {
        this.proc = proc;
        this.batchMode = batchMode;
        this.descriptors = descriptors;
        this.dataItems = new ArrayList<>();
        this.aaDataItems = new ArrayList<>();
        this.headerItems = new ArrayList<>();
        this.aaHeaderItems = new ArrayList<>();
        this.exceptionList = new ArrayList<>();
        this.uiNewParameters = uiNewParameters;
        this.cutConfig = cutConfig;
        this.isAA_level = uiNewParameters != null ? (boolean) uiNewParameters[3] : false;
        if (isAA_level) {
            windowSize = (int) uiNewParameters[4];
        }
        this.listMode = (boolean) uiNewParameters[5];
    }

    public void setAaHeaderItems(List<String> aaHeaderItems) {
        this.aaHeaderItems = aaHeaderItems;
    }

    public void setHeaderItems(List<String> headerItems) {
        this.headerItems = headerItems;
    }

    public void setListMode(boolean listMode) {
        this.listMode = listMode;
    }

    public void setIsAALevelList(boolean isAALevelList) {
        this.isAALevelList = isAALevelList;
    }

    public void setTasks(ArrayList<ProteinCalculatorTask> tasks) {
        this.tasks = tasks;
    }
    
    public void setInmutableAtrr(boolean batchMode, Object[] uiNewParameters, WeightConfiguration cutConfig,
            List<ExceptionInfo> exceptionList, List<IDescriptor> descriptors, List<String> descriptorsName, int[] currentDescriptor, JProgressBar progressBar2) {
        this.batchMode = batchMode;
        this.uiNewParameters = uiNewParameters;
        this.cutConfig = cutConfig;

        this.exceptionList = exceptionList;
        this.descriptors = descriptors;
        this.descriptorsName = descriptorsName;

        this.progressBar2 = progressBar2;
        this.currentDescriptor = currentDescriptor;
        this.isAA_level = uiNewParameters != null ? (boolean) uiNewParameters[3] : false;
        if (isAA_level) {
            windowSize = (int) uiNewParameters[4];
        }
        this.listMode = (boolean) uiNewParameters[5];
    }

    public void setVolatileAtrr(int proteinCount, IMolecule protein, int start, int end, List<String> dataItems, List<List<String>> aaDataItems) {
        this.proteinCount = proteinCount;
        this.start = start;
        this.end = end;

        this.protein = protein;

        this.dataItems = dataItems;
        this.aaDataItems = aaDataItems;

        this.history = new StringBuffer("");

        if (tasks == null) {
            tasks = new ArrayList<>();

            for (int i = 1; i <= end - start + 1; i++) {
                ProteinCalculatorTask task = new ProteinCalculatorTask(descriptors, batchMode, i - 1,
                        uiNewParameters, cutConfig);

                if (isAA_level || isAALevelList) {
                    int dim = protein.getAtomCount();
                    List<List<String>> taskAADataItems = new ArrayList<>(dim);
                    
                    for (int k = 0; k < dim; k++) {
                        List<String> aa = new ArrayList<>(dim);
                        taskAADataItems.add(aa);
                    }

                    task.aaDataItems = taskAADataItems;
                }

                tasks.add(task);
            }
        }
    }

    @Override
    protected void compute() {
        if (canceled) {
            return;
        }

        if (end - start == 0) {
            if (start < descriptors.size()) {
                MolecularDescriptor algDesc = (MolecularDescriptor) descriptors.get(start);
                algDesc.initialize(proc);

                if (!calculateDescriptors(descriptors.get(start))) {
                    if (!batchMode) {
                        history.append("\r\nProtein was not Calculated");
                    }
                }

                if (canceled) {
                    return;
                }

                algDesc.destroy();

            } else {
                start = end = -1;
            }

            return;
        }

        int i = start;
        for (ProteinCalculatorTask task : tasks) {
            task.proteinCount = proteinCount;
            task.protein = protein;
            task.start = i;
            task.end = i++;
            task.dataItems.clear();

            if (isAA_level || isAALevelList) {
                    int dim = protein.getAtomCount();
                    List<List<String>> taskAADataItems = new ArrayList<>(dim);
                    
                    for (int k = 0; k < dim; k++) {
                        List<String> aa = new ArrayList<>(dim);
                        taskAADataItems.add(aa);
                    }

                    task.aaDataItems = taskAADataItems;
                }

            task.headerItems.clear();
            task.aaHeaderItems.clear();
            task.exceptionList.clear();
            task.history = new StringBuffer("");
            task.reinitialize();

            if (task.start < descriptors.size()) {
                task.progressBar2 = progressBar2;
                task.currentDescriptor = currentDescriptor;
            }
        }

        invokeAll(tasks);

        for (ProteinCalculatorTask task : tasks) {
            if (task.start != -1) {
                descriptorsName.add(task.descName);
                history.append(task.getHistory());
                dataItems.addAll(task.dataItems);
                headerItems.addAll(task.headerItems);

                if (isAA_level || isAALevelList) {
                    for (int j = 0; j < task.aaHeaderItems.size(); j++) {
                        String aaHeader = task.aaHeaderItems.get(j);
                        if (!aaHeaderItems.contains(aaHeader)) {
                            aaHeaderItems.add(aaHeader);
                        }
                    }
                    
                    for (int j = 0; j < task.aaDataItems.size(); j++) {
                        List<String> task_aadataItems = task.aaDataItems.get(j);
                        aaDataItems.get(j).addAll(task_aadataItems);
                    }
                }

                exceptionList.addAll(task.exceptionList);
            }

            task.progressBar2 = null;
            task.currentDescriptor = null;
        }
        
        if (isAA_level || isAALevelList) 
        {
            tasks = null;
        }
    }

    public boolean isAA_Level(IDescriptor desc) {
        String classicInv = ((MolecularDescriptor) desc).getClassicInv();

        if (classicInv.equals("sic") || classicInv.equals("tic") || classicInv.equals("sicn") || classicInv.equals("ticn")) {
            return false;
        }
        String noClassicInv = ((MolecularDescriptor) desc).getNoClassicInv();
        return noClassicInv.isEmpty() || noClassicInv.contains("[");
    }

    public int getWindowSize(String inv) {
        return Integer.parseInt(inv.substring(inv.indexOf("[") + 1, inv.indexOf("]")));
    }

    public StringBuffer getHistory() {
        return history;
    }

    private boolean calculateDescriptors(Object object) {
        if (canceled) {
            return false;
        }

        IDescriptor descriptor = null;

        try {
            descriptor = (IDescriptor) object;
        } catch (Exception ex) {
            ExceptionInfo newExceptionInfo = new ExceptionInfo(proteinCount, protein, ex, "Object cast to IDescriptor");
            exceptionList.add(newExceptionInfo);
            if (!batchMode) {
                history.append("\r\nError Casting Descriptor");
            }
            return false;
        }

        IMolecularDescriptor molecularDescriptor = (IMolecularDescriptor) descriptor;

        try {
            molecularDescriptor.setParameters(uiNewParameters);
        } catch (CDKException ex) {
            ExceptionInfo newExceptionInfo = new ExceptionInfo(proteinCount, protein, ex, "Adding new parameters");
            newExceptionInfo.setDescriptorName(descriptor.getDescriptorNames()[0]);
            exceptionList.add(newExceptionInfo);
            if (!batchMode) {
                history.append("\r\nError Adding New Parameters");
            }
            return false;
        }

        DescriptorValue value = null;

        try {
            value = molecularDescriptor.calculate(protein);
        } catch (Exception ex) {
            ex.printStackTrace();
            ExceptionInfo newExceptionInfo = new ExceptionInfo(proteinCount, protein, ex, "Unexpected Error calculating descriptor");
            newExceptionInfo.setDescriptorName(descriptor.getDescriptorNames()[0]);

            exceptionList.add(newExceptionInfo);

            if (!batchMode) {
                history.append("\r\nError calculating descriptor:").append(descriptor.getDescriptorNames()[0]).append("at protein: ").append(proteinCount);
            }

            return false;
        }

        if (value.getException() != null) {
            ExceptionInfo newExceptionInfo = new ExceptionInfo(proteinCount, protein, value.getException(), value.getNames()[0]);
            newExceptionInfo.setDescriptorName(descriptor.getDescriptorNames()[0]);
            exceptionList.add(newExceptionInfo);
            if (!batchMode) {
                history.append("\r\nError on Descriptor Value: ").append(value.getException().getMessage());
            }
            return false;
        }

        boolean calculated = true;
        if (molecularDescriptor instanceof MolecularDescriptor) {
            String name = descriptor.getDescriptorNames()[0];

            double[] lovisArray = null;
            descName = name;

            IDescriptorResult result = value.getValue();
            AlgebraicDescriptorResult newResult = (AlgebraicDescriptorResult) result;
            MolecularDescriptor algebraicDescriptor = (MolecularDescriptor) descriptor;

            String[] descriptorNames = algebraicDescriptor.getDescriptorNames();

            /**
             * calculates each selected invariant
             */
            List<String> uiClassicInvList = new LinkedList<>();
            String classicInv = algebraicDescriptor.getClassicInv().toLowerCase();
            uiClassicInvList.add(classicInv);

            List<String> uiNoClassicInvList = new LinkedList<>();
            uiNoClassicInvList.add(algebraicDescriptor.getNoClassicInv().toLowerCase());

            HashMap<InvariantType, Object[]> invariantParameters = new HashMap<>();

            Object[] params = new Object[]{DimensionType.TOPOLOGICAL, null};

            if (classicInv.startsWith("ac") || classicInv.startsWith("ts") || classicInv.startsWith("gv")
                    || classicInv.startsWith("kh") || classicInv.startsWith("es") || classicInv.startsWith("ib")
                    || classicInv.startsWith("gc") || classicInv.startsWith("pcd") || classicInv.startsWith("cei")
                    || classicInv.startsWith("rdf") || classicInv.startsWith("is") || classicInv.startsWith("mse")
                    || classicInv.startsWith("apm")) {
                if (classicInv.contains("[")) {
                    int k = Integer.parseInt(classicInv.substring(classicInv.indexOf("[") + 1, classicInv.indexOf("]")));

                    params = new Object[]{DimensionType.TOPOLOGICAL, null, k};

                    classicInv = classicInv.substring(0, classicInv.indexOf("["));
                    uiClassicInvList.clear();
                    uiClassicInvList.add(classicInv);
                }

                if (classicInv.startsWith("ac")) {
                    invariantParameters.put(InvariantType.AUTOCORRELATION, params);
                } else if (classicInv.startsWith("ts")) {
                    invariantParameters.put(InvariantType.TOTAL_SUM, params);
                } else if (classicInv.startsWith("gv")) {
                    invariantParameters.put(InvariantType.GRAVITATIONAL, params);
                } else if (classicInv.startsWith("kh")) {
                    invariantParameters.put(InvariantType.KIER_HALL, params);
                } else if (classicInv.startsWith("es")) {
                    invariantParameters.put(InvariantType.ELECTROTOPOLOGICAL_STATE, params);
                } else if (classicInv.startsWith("ib")) {
                    invariantParameters.put(InvariantType.IVANCIUC_BALABAN, params);
                } else if (classicInv.startsWith("gc")) {
                    invariantParameters.put(InvariantType.GEARY, params);
                } else if (classicInv.startsWith("pcd")) {
                    invariantParameters.put(InvariantType.POTENTIAL_CHARGE_DISTRIBUTION, params);
                } else if (classicInv.startsWith("cei")) {
                    invariantParameters.put(InvariantType.CONNECTIVE_ECCENTRICITY_INDEX, params);
                } else if (classicInv.startsWith("rdf")) {
                    invariantParameters.put(InvariantType.RADIAL_DISTRIBUTION_FUNCTION, params);
                } else if (classicInv.startsWith("is")) {
                    invariantParameters.put(InvariantType.INTERACTION_SPECTRUM, params);
                } else if (classicInv.startsWith("mse")) {
                    invariantParameters.put(InvariantType.MORSE, params);
                } else if (classicInv.startsWith("apm")) {
                    invariantParameters.put(InvariantType.AMPHIPHILIC_MOMENTS, params);
                }
            }

            for (int inv = 0; inv < uiClassicInvList.size(); inv++) {
                if (canceled) {
                    return false;
                }

                String s = uiClassicInvList.get(inv);
                String invariantUppercaseSymbol = s.toUpperCase();

                if (s.startsWith("ac")) {
                    params = invariantParameters.get(InvariantType.AUTOCORRELATION);
                } else if (s.startsWith("gv")) {
                    params = invariantParameters.get(InvariantType.GRAVITATIONAL);
                } else if (s.startsWith("ts")) {
                    params = invariantParameters.get(InvariantType.TOTAL_SUM);
                } else if (s.startsWith("es")) {
                    params = invariantParameters.get(InvariantType.ELECTROTOPOLOGICAL_STATE);
                } else if (s.startsWith("ib")) {
                    params = invariantParameters.get(InvariantType.IVANCIUC_BALABAN);
                } else if (s.startsWith("kh")) {
                    params = invariantParameters.get(InvariantType.KIER_HALL);
                } else if (s.startsWith("rdf")) {
                    params = invariantParameters.get(InvariantType.RADIAL_DISTRIBUTION_FUNCTION);
                } else if (s.startsWith("gc")) {
                    params = invariantParameters.get(InvariantType.GEARY);
                } else if (s.startsWith("pcd")) {
                    params = invariantParameters.get(InvariantType.POTENTIAL_CHARGE_DISTRIBUTION);
                } else if (s.startsWith("cei")) {
                    params = invariantParameters.get(InvariantType.CONNECTIVE_ECCENTRICITY_INDEX);
                } else if (s.startsWith("mse")) {
                    params = invariantParameters.get(InvariantType.MORSE);
                } else if (s.startsWith("is")) {
                    params = invariantParameters.get(InvariantType.INTERACTION_SPECTRUM);
                } else if (s.startsWith("apm")) {
                    params = invariantParameters.get(InvariantType.AMPHIPHILIC_MOMENTS);
                }

                if (progressBar2 != null) {
                    progressBar2.setValue(currentDescriptor[0]++);
                }

                /*  
                 * setup descriptor names
                 */
                for (int i = 0; i < value.getNames().length; i++) {
                    String descriptorName = descriptorNames[i];

                    if (canceled) {
                        return false;
                    }

                    lovisArray = newResult.get(i);

                    double[] ds = null;

                    /*
                     * Classic Invariants
                     */
                    if (s.equals("lai")) {
                        //<editor-fold defaultstate="collapsed" desc="(LAI Vector)Identity, (i.e. No Classic Invariants)">
                        int j = 0;
                        String ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }
                            
                            j = i;

                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);

                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol.equals("LAI") ? ss.toUpperCase().equals("") ? "" : ss.toUpperCase() + "_"
                                            : invariantUppercaseSymbol + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }

                                applyInvariants("", ss, lovisArray, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, descriptorNames[i]));
                            dataItems.add("");
                            headerItems.add(ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("ac")) {
                        //<editor-fold defaultstate="collapsed" desc="AutoCorrelation Invariant">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            //iterating from 1 to max K
                            j = i;

                            int t = 1;
                            int u = MAX_K;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.Autocorrelation(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);

                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                                }
                            }

                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("gv")) {
                        //<editor-fold defaultstate="collapsed" desc="Gravitational Invariant">
                        int j = 0;
                        String tmp = "", ss = "";

                        try {

                            //TO DO: cambiar la variable j por i que es el lovis en cuestion
                            j = i;
                            int t = 1;
                            int u = MAX_K;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.Gravitational(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);
//                                        
                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                                }
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("ts")) {
                        //<editor-fold defaultstate="collapsed" desc="TotalSumLagK">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            j = i;

                            int t = 1;
                            int u = MAX_K;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.TotalSumLagK(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);

                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                                }
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.equals("mic")) {
                        //<editor-fold defaultstate="collapsed" desc="MeanInformation">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            ds = Classics.MeanInformation(lovisArray);

                            j = i;
                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);

                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }
                                tmp = s;

                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.equals("sic")) {
                        //<editor-fold defaultstate="collapsed" desc="StandardarizedInformation">
                        double d = Classics.StandardarizedInformation(lovisArray);
                        dataItems.add(String.valueOf(d));

                        headerItems.add(s.toUpperCase() + "_" + descriptorName);
                        //</editor-fold>
                    } else if (s.equals("tic")) {
                        //<editor-fold defaultstate="collapsed" desc="TotalInformation">
                        double d = Classics.TotalInformation(lovisArray);
                        dataItems.add(String.valueOf(d));

                        headerItems.add(invariantUppercaseSymbol + "_" + descriptorName);
                        //</editor-fold>
                    } else if (s.equals("micn")) {
                        //<editor-fold defaultstate="collapsed" desc="MeanInformation">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            ds = Classics.MeanInformationN(lovisArray);
                            j = i;
                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);

                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }
                                tmp = s;
                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.equals("sicn")) {
                        //<editor-fold defaultstate="collapsed" desc="StandardarizedInformation">
                        double d = Classics.StandardarizedInformationN(lovisArray);
                        dataItems.add(String.valueOf(d));

                        headerItems.add(s.toUpperCase() + "_" + descriptorName);
                        //</editor-fold>
                    } else if (s.equals("ticn")) {
                        //<editor-fold defaultstate="collapsed" desc="TotalInformation">
                        double d = Classics.TotalInformationN(lovisArray);
                        dataItems.add(String.valueOf(d));

                        headerItems.add(invariantUppercaseSymbol + "_" + descriptorName);
                        //</editor-fold>
                    } else if (s.equals("h")) {
                        //<editor-fold defaultstate="collapsed" desc="Entropy">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            ds = Classics.MeanInformationSS(lovisArray);

                            j = i;
                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);

                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }

                                tmp = s;
                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("es")) {
                        //<editor-fold defaultstate="collapsed" desc="(ES) ElectroTopologicalState">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            tmp = invariantUppercaseSymbol;
                            j = i;

                            ds = Classics.ElectroTopologicalState(lovisArray, lovisArray.length, (AtomContainer) protein);
                                                        
                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);

                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }

                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorNames[i]));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("ib")) {
                        //<editor-fold defaultstate="collapsed" desc="(IB) Ivanciuc-Balaban">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            tmp = invariantUppercaseSymbol;
                            j = i;

                            ds = Classics.IvanciucBalaban(lovisArray, (AtomContainer) protein);

                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);
                                       
                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }

                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorNames[i]));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("rdf")) {
                        //<editor-fold defaultstate="collapsed" desc="(RDF) RadialDistributionFunction">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            //iterating from 1 to max K
                            j = i;

                            int t = 1;
                            int u = MAX_K;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.RadialDistributionFunction(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);

                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                                }
                            }

                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                    } //</editor-fold>                   
                    else if (s.startsWith("kh")) {
                        //<editor-fold defaultstate="collapsed" desc=" (KH)KierHall invariant">
                        String ss = null;
                        String tmp = "";
                        int j = i;
                        try {
                            if (canceled) {
                                return false;
                            }
                            tmp = invariantUppercaseSymbol;

                            int u = 0;

                            int v = 7;

                            if (params.length > 2) {
                                u = (int) params[2];
                                v = u;
                            }

                            Vector[] vk = Classics.KierHall(lovisArray, (AtomContainer) protein, u, v);

                            if (vk == null) {
                                exceptionList.add(new ExceptionInfo(proteinCount, protein, new CDKException("Problem in KierHall invariant"),
                                        invariantUppercaseSymbol + "_" + algebraicDescriptor.getDescriptorNames()[i]));
                                return false;
                            }

                            if (progressBar2 != null) {
                                progressBar2.setMaximum(progressBar2.getMaximum() + vk.length);
                            }

                            for (int t = 0; t < vk.length; t++) {
                                if (canceled) {
                                    return false;
                                }

                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                }

                                tmp = u == 0 ? invariantUppercaseSymbol + "[" + ((int) t + 1) + "]" : invariantUppercaseSymbol + "[" + ((int) u) + "]";

                                ds = (double[]) vk[t].get(0);

                                //<editor-fold defaultstate="collapsed" desc="No classic inv">
                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);

                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = tmp + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, ds, algebraicDescriptor, j, invariantParameters);
                                }
                                //</editor-fold>
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + algebraicDescriptor.getDescriptorNames()[i]));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + invariantUppercaseSymbol + "_" + algebraicDescriptor.getDescriptorNames()[j]);
                            calculated = false;
                        }
//</editor-fold>

                    } else if (s.startsWith("gc")) {
                        //<editor-fold defaultstate="collapsed" desc="Geary Coefficient">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            //iterating from 1 to max K
                            j = i;

                            int t = 1;
                            int u = MAX_K;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.Geary(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);
//                                        
                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                                }
                            }

                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>

                    } else if (s.startsWith("pcd")) {
                        //<editor-fold defaultstate="collapsed" desc="(PCD) PotentialChargeDistribution">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            tmp = invariantUppercaseSymbol;
                            j = i;

                            ds = Classics.PotentialChargeDistribution(lovisArray, (AtomContainer) protein);

                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);
//                                        
                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }

                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorNames[i]));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("cei")) {
                        //<editor-fold defaultstate="collapsed" desc="(CEI) ConnectivityEccentricityIndex">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            tmp = invariantUppercaseSymbol;
                            j = i;

                            ds = Classics.ConnectivityEccentricity(lovisArray, (AtomContainer) protein);

                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);
//                                        
                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }

                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorNames[i]));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("is")) {
                        //<editor-fold defaultstate="collapsed" desc="Interaction Spectrum">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            //iterating from 1 to max K
                            j = i;

                            int t = 1;
                            int u = MAX_K;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.InteractionSpectrum(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);
//                                        
                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                                }
                            }

                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("mse")) {
                        //<editor-fold defaultstate="collapsed" desc="MoRSE Descriptors">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            //iterating from 1 to max K
                            j = i;

                            int t = 1;
                            int u = MAX_K;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.MoRSE(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);
//                                        
                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    double[] wds = !listMode && isAA_level || listMode && isAA_Level(algebraicDescriptor) ? Classics.aaLevelMoRSE(lovisArray, protein, t) : null;

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, wds, algebraicDescriptor, j, invariantParameters);
                                }
                            }

                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.equals("bft")) {
                        //<editor-fold defaultstate="collapsed" desc="(BFT) Beteringhe–Filip–Tarko ">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            if (canceled) {
                                return false;
                            }

                            tmp = invariantUppercaseSymbol;
                            j = i;

                            ds = Classics.BeteringheFilipTarko(lovisArray, (AtomContainer) protein);

                            for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                if (canceled) {
                                    return false;
                                }

                                ss = uiNoClassicInvList.get(inc);

                                if (progressBar2 != null) {
                                    progressBar2.setValue(currentDescriptor[0]++);
                                    String str = invariantUppercaseSymbol + "_" + ss.toUpperCase() + "_";
                                    progressBar2.setString(str
                                            + descriptorName
                                            + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                }

                                applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                            }
                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorNames[i]));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    } else if (s.startsWith("apm")) {
                        //<editor-fold defaultstate="collapsed" desc="Amphiphilic Descriptors">
                        int j = 0;
                        String tmp = "", ss = "";
                        try {
                            //iterating from 1 to max K
                            j = i;
                            int t = 1;
                            int u = 3;

                            if (params.length > 2) {
                                t = (int) params[2];
                                u = t;
                            }

                            for (; t <= u; t++) {
                                if (canceled) {
                                    return false;
                                }

                                tmp = invariantUppercaseSymbol + "[" + t + "]";

                                ds = Classics.AmphiphilicMoments(lovisArray, protein, t);

                                for (int inc = 0; inc < uiNoClassicInvList.size(); inc++) {
                                    if (canceled) {
                                        return false;
                                    }

                                    ss = uiNoClassicInvList.get(inc);

                                    if (progressBar2 != null) {
                                        progressBar2.setValue(currentDescriptor[0]++);
                                        String str = invariantUppercaseSymbol + "[" + t + "]" + "_" + ss.toUpperCase() + "_";
                                        progressBar2.setString(str
                                                + descriptorName
                                                + " " + (int) ((progressBar2.getValue() * 100) / progressBar2.getMaximum()) + "%");
                                    }

                                    applyInvariants(tmp.toUpperCase() + "_", ss, ds, null, algebraicDescriptor, j, invariantParameters);
                                }
                            }

                        } catch (Exception ex) {
                            exceptionList.add(new ExceptionInfo(proteinCount, protein, ex, invariantUppercaseSymbol + "_" + descriptorName));
                            dataItems.add("");
                            headerItems.add(tmp.toUpperCase() + "_" + ss.toUpperCase() + "_" + descriptorNames[j]);
                            calculated = false;
                        }
                        //</editor-fold>
                    }
                }

            }
        }
        return calculated;
    }

    private void applyInvariants(String stdHeader, String s, double[] lovisArray, double[] wlovisArray, MolecularDescriptor descriptor, int pos, HashMap<InvariantType, Object[]> invariantParameters) throws Exception {
        double[] ds = null;

        boolean diffLength = wlovisArray != null;

        boolean flag = !listMode && isAA_level || listMode && isAA_Level(descriptor);

        if (!s.equalsIgnoreCase("gowawa") && !s.equalsIgnoreCase("choquet")) {
            if (lovisArray != null) {
                if (s.equals("n1") || s.startsWith("n1[")) {
                    if (s.equals("n1")) {
                        dataItems.add(String.valueOf(Norms.MinkoskyNorm(lovisArray, 1)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }

                        ds = diffLength ? Norms.windowedMinkoskiNorm(wlovisArray, 1, wSize)
                                : Norms.windowedMinkoskiNorm(lovisArray, 1, wSize);
                    }
                } else if (s.equals("n2") || s.startsWith("n2[")) {
                    if (s.equals("n2")) {
                        dataItems.add(String.valueOf(Norms.MinkoskyNorm(lovisArray, 2)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Norms.windowedMinkoskiNorm(wlovisArray, 2, wSize)
                                : Norms.windowedMinkoskiNorm(lovisArray, 2, wSize);

                    }
                } else if (s.equals("n3") || s.startsWith("n3[")) {

                    if (s.equals("n3")) {
                        dataItems.add(String.valueOf(Norms.MinkoskyNorm(lovisArray, 3)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Norms.windowedMinkoskiNorm(wlovisArray, 3, wSize)
                                : Norms.windowedMinkoskiNorm(lovisArray, 3, wSize);

                    }
                } else if (s.equals("gm") || s.startsWith("gm[")) {
                    try {
                        if (s.equals("gm")) {
                            dataItems.add(Means.GeometricMeanBigNumber(lovisArray));
                        }
                        if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                            int wSize = -1;

                            if (!listMode) {
                                wSize = windowSize;
                            } else {
                                wSize = getWindowSize(s);
                            }

                            String[] sds = diffLength ? Means.windowedGeometricMeanBigNumber(wlovisArray, wSize)
                                    : Means.windowedGeometricMeanBigNumber(lovisArray, wSize);

                            for (int j = 0; j < sds.length; j++) {
                                aaDataItems.get(j).add(sds[j]);
                            }
                        }
                    } catch (Exception ex) {
                        ExceptionInfo newExceptionInfo = new ExceptionInfo(proteinCount, protein, ex, "Error calculating geometric mean");
                        newExceptionInfo.setDescriptorName(descriptor.getDescriptorNames()[0]);

                        exceptionList.add(newExceptionInfo);
                        if (!batchMode) {
                            history.append("\r\nError calculating geometric mean:").append(descriptor.getDescriptorNames()[0]).append("at molecule: ").append(proteinCount);
                        }

                        throw ex;
                    }
                } else if (s.equals("am") || s.startsWith("am[")) {
                    if (s.equals("am")) {
                        dataItems.add(String.valueOf(Means.ArithmeticMean(lovisArray)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Means.windowedArithmeticMean(wlovisArray, wSize)
                                : Means.windowedArithmeticMean(lovisArray, wSize);
                    }
                } else if (s.equals("hm") || s.startsWith("hm[")) {
                    if (s.equals("hm")) {
                        dataItems.add(String.valueOf(Means.PotentialMean(lovisArray, -1)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Means.windowedPotentialMean(wlovisArray, wSize, -1)
                                : Means.windowedPotentialMean(lovisArray, wSize, -1);
                    }
                } else if (s.equals("p2") || s.startsWith("p2[")) {
                    if (s.equals("p2")) {
                        dataItems.add(String.valueOf(Means.PotentialMean(lovisArray, 2)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Means.windowedPotentialMean(wlovisArray, wSize, 2)
                                : Means.windowedPotentialMean(lovisArray, wSize, 2);
                    }
                } else if (s.equals("p3") || s.startsWith("p3[")) {
                    if (s.equals("p3")) {
                        dataItems.add(String.valueOf(Means.PotentialMean(lovisArray, 3)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Means.windowedPotentialMean(wlovisArray, wSize, 3)
                                : Means.windowedPotentialMean(lovisArray, wSize, 3);
                    }
                } else if (s.equals("v") || s.startsWith("v[")) {
                    if (s.equals("v")) {
                        dataItems.add(String.valueOf(Statistics.Variance(lovisArray)));
                    }
                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("s") || s.startsWith("s[")) {
                    if (s.equals("s")) {
                        dataItems.add(String.valueOf(Statistics.Skewness(lovisArray)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("k") || s.startsWith("k[")) {
                    if (s.equals("k")) {
                        dataItems.add(String.valueOf(Statistics.Kurtosis(lovisArray)));
                    }
                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("sd") || s.startsWith("sd[")) {
                    if (s.equals("sd")) {
                        dataItems.add(String.valueOf(Statistics.StandardDeviation(lovisArray)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("vc") || s.startsWith("vc[")) {
                    if (s.equals("vc")) {
                        dataItems.add(String.valueOf(Statistics.VariationCoefficient(lovisArray)));
                    }
                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("ra") || s.startsWith("ra[")) {
                    if (s.equals("ra")) {
                        dataItems.add(String.valueOf(Statistics.Range(lovisArray)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("q1") || s.startsWith("q1[")) {
                    if (s.equals("q1")) {
                        dataItems.add(String.valueOf(Statistics.Percentil(lovisArray, 25)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("q2") || s.startsWith("q2[")) {
                    if (s.equals("q2")) {
                        dataItems.add(String.valueOf(Statistics.Percentil(lovisArray, 50)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }

                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("q3") || s.startsWith("q3[")) {
                    if (s.equals("q3")) {
                        dataItems.add(String.valueOf(Statistics.Percentil(lovisArray, 75)));
                    }
                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }

                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("i50") || s.startsWith("i50[")) {
                    if (s.equals("i50")) {
                        dataItems.add(String.valueOf(Statistics.I50(lovisArray)));
                    }

                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("mx") || s.startsWith("mx[")) {
                    if (s.equals("mx")) {
                        dataItems.add(String.valueOf(Statistics.XMax(lovisArray)));
                    }
                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                } else if (s.equals("mn") || s.startsWith("mn[")) {
                    if (s.equals("mn")) {
                        dataItems.add(String.valueOf(Statistics.XMin(lovisArray)));
                    }
                    if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                        int wSize = -1;

                        if (!listMode) {
                            wSize = windowSize;
                        } else {
                            wSize = getWindowSize(s);
                        }
                        ds = diffLength ? Statistics.windowedStatistics(wlovisArray, wSize, s)
                                : Statistics.windowedStatistics(lovisArray, wSize, s);
                    }
                }
            } else {
                dataItems.add(String.valueOf(Double.NaN));
            }

            String head = descriptor.getDescriptorNames()[pos];

            if (!isAA_Level(descriptor)) {
                headerItems.add(stdHeader + s.toUpperCase() + "_" + head);
            }

            if (flag && !stdHeader.equals("MIC") && !stdHeader.equals("TIC")) {
                if (!listMode) {
                    aaHeaderItems.add(stdHeader + s.toUpperCase() + "[" + windowSize + "]" + "_" + head);
                } else if (isAA_Level(descriptor) && !s.equals("")) {
                    aaHeaderItems.add(stdHeader + s.toUpperCase() + "_" + head);
                }

                if (!s.equals("gm") && !s.startsWith("gm[") && !s.equals("")) {
                    for (int j = 0; j < ds.length; j++) {
                        aaDataItems.get(j).add(String.valueOf(ds[j]));
                    }
                }
            }

        } else if (s.equalsIgnoreCase("gowawa")) {
            //OWAWA
            double[] wds = diffLength ? wlovisArray : lovisArray;

            ((HashMap<String, HashMap<OWAWA.PARAMETER_NAMES, Object>>) invariantParameters.get(InvariantType.GOWAWA)[0]).values()
                    .stream().forEach((param) -> {
                        if (progressBar2 != null) {
                            progressBar2.setValue(currentDescriptor[0]++);
                        }

                        String[] params2String = new String[]{""};

                        dataItems.add(String.valueOf(OWAWA.compute(lovisArray, param, params2String)));

                        String prefix = stdHeader + ("GOWAWA[" + params2String[0] + "]");
                        String suffix = "_" + descriptor.getDescriptorNames()[pos];

                        headerItems.add(prefix + suffix);

                        if (isAA_level && !stdHeader.startsWith("MIC") && !stdHeader.startsWith("TIC")) {
                            aaHeaderItems.add(prefix + "[" + windowSize + "]" + suffix);

                            double[] aux = OWAWA.windowedcompute(wds, param, params2String, windowSize);

                            for (int j = 0; j < aux.length; j++) {
                                aaDataItems.get(j).add(String.valueOf(aux[j]));
                            }
                        }
                    });
        } else if (s.equalsIgnoreCase("choquet")) {
            //CHOQUET
            double[] wds = diffLength ? wlovisArray : lovisArray;

            ((HashMap<String, HashMap<Choquet.PARAMETER_NAMES, Object>>) invariantParameters.get(InvariantType.CHOQUET)[0]).values()
                    .stream().forEach((param) -> {
                        if (progressBar2 != null) {
                            progressBar2.setValue(currentDescriptor[0]++);
                        }

                        String[] params2String = new String[]{""};
                        dataItems.add(String.valueOf(Choquet.compute(lovisArray, param, params2String)));
                        String prefix = stdHeader + ("CHOQUET[" + params2String[0] + "]");
                        String suffix = "_" + descriptor.getDescriptorNames()[pos];

                        headerItems.add(prefix + suffix);

                        if (isAA_level && !stdHeader.startsWith("MIC") && !stdHeader.startsWith("TIC")) {
                            aaHeaderItems.add(prefix + "[" + windowSize + "]" + suffix);

                            double[] aux = Choquet.windowedcompute(wds, param, params2String, windowSize);

                            for (int j = 0; j < aux.length; j++) {
                                aaDataItems.get(j).add(String.valueOf(aux[j]));
                            }
                        }
                    });
        }
    }
}
