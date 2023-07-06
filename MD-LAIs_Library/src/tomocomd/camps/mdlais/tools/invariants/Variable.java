/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.tools.invariants;

import java.util.HashSet;

/**
 *
 * @author Alexis
 */
public class Variable implements Comparable<Variable> {

    public String nombre;
    private double[] value_instances;
    private double shannonEntropy;
    private double maximum;
    private double minimum;
    private double mean;
    private double desvstand;

    private int degeneration = -1;
    private double degenerativeEntropy = -1;

    private double singularvaluedescomposition = Double.NEGATIVE_INFINITY;

    /**
     *
     * @param nombre nombre del descriptor
     * @param varMol arreglo de valores de las intancias
     */
    public Variable(String nombre, double[] varMol) {
        this.nombre = nombre;
        this.value_instances = varMol;

    }

    public Variable() {
    }

    /**
     *
     * @param bins
     * @return un arreglo de enteros el cual contiene los valores de las
     * moleculas distribuidos por intervalos
     */
    public int[] distribucionDeValores(int bins) {
        int arreglo[] = new int[bins];
//        System.out.println(" cant moleculas por intervalo");
        double max = maximum();
        double min = minimum();

        double intervalo = max - min;
        double razon = intervalo / bins;

        for (int i = 0; i < value_instances.length; i++) {
            double minTemp = min;
            int contadorBin = 0;
            while (minTemp <= max && contadorBin != bins) {
                if (value_instances[i] >= minTemp && value_instances[i] < minTemp + razon) {
                    arreglo[contadorBin]++;
                    break;
                } else if (value_instances[i] == max) {
                    arreglo[arreglo.length - 1]++;
                    break;
                }
                minTemp = minTemp + razon;
                contadorBin++;
            }
        }

        return arreglo;

    }

    /**
     *
     * @param bin
     * @return retorna la probabilidad de aparicion de cada intervalo
     */
    public double[] distribucionDeProbabilidades(int bin) {
        //    System.out.println(" razon cant mol por intervalo");
        int arreglo[] = distribucionDeValores(bin);
        double arregloRazon[] = new double[arreglo.length];
        for (int i = 0; i < arreglo.length; i++) {
            arregloRazon[i] = (double) arreglo[i] / value_instances.length;
            //      System.out.print(" "+arreglo[i]);
        }

        return arregloRazon;
    }

    /**
     *
     * @param bin
     * @return la entropia de shannon
     */
    public double ShannonEntropy(int bin) {

        double[] arr = distribucionDeProbabilidades(bin);
        double SE = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                SE += 0;
            } else {
                SE += arr[i] * ((double) Math.log10(arr[i]) / (double) Math.log10(2));
            }
        }

        if (SE != 0) {
            SE = (double) redondear(-1 * SE);
        }
        return SE;
    }

    /**
     *
     * @param bins
     * @return el valor de la entropia de shannon estandarizada
     */
    public double sShannonEntropy(int bins) {
        return redondear((double) ShannonEntropy(bins) / (double) (Math.log10(bins) / (double) Math.log10(2)));

    }

    /**
     *
     * @param bin
     * @return retorna la negentropia para el descriptor
     */
    public double Negentropy(int bin) 
    {
        double entropia = ShannonEntropy(bin);
        
        return redondear(value_instances.length * entropia);
    }

    /**
     *
     * @param bin
     * @return retorna el valor del Indice de Redundancia para el descriptor
     */
    public double RedundancyIndex(int bin) {
        return redondear(1 - sShannonEntropy(bin));
    }

    /**
     *
     * @param bin
     * @return retorna y calcula el Information Energy Content
     */
    public double InformationalEnergyContent(int bin) {
        double d = 0;
        double arr[] = distribucionDeProbabilidades(bin);
        for (int i = 0; i < arr.length; i++) {

            d += arr[i] * arr[i];

        }
        return redondear(d);
    }

    /**
     *
     * @param bin
     * @return calcula el Gini Index
     *
     */
    public double GiniIndex(int bin) {
        double d = 0;
        double arr[] = distribucionDeProbabilidades(bin);
        for (int idx = 0; idx < arr.length - 1; idx++) {
//            for ( int i = idx + 1 ; i < arr.length ; i ++ )
//            {
            d += arr[idx] * arr[idx + 1];
//            }

        }
        return redondear(d);
    }

    public double[] normalizedValues() {

        double arr[] = new double[value_instances.length];

        if ((maximum() - minimum()) != 0) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (value_instances[i] - minimum()) / (maximum() - minimum());
            }
        }

        return arr;
    }

    public double[] standardizedValues() {

        double arr[] = new double[value_instances.length];
        double avg = average();
        double std = calcular_StandarDesviation();

        if ((maximum() - minimum()) != 0) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (value_instances[i] - avg) / (std);
            }
        }

        return arr;
    }

    /**
     *
     * @return el maximum de las instancias de la data
     */
    public double maximum() {
        if (maximum == 0) {
            return calcular_maximo();
        }

        return this.maximum;

    }

    public double average() {
        double avg = 0;

        for (double value : value_instances) {
            avg += value;
        }

        return avg / value_instances.length;
    }

    /**
     *
     * @return calcula el maximum de la data
     */
    private double calcular_maximo() {
        double max = Double.NEGATIVE_INFINITY;
        for (double value : value_instances) {
            if ((max == Double.NEGATIVE_INFINITY || max < value)) {
                max = value;
            }
        }
        maximum = max;
        return maximum;

    }

    /**
     *
     * @return retorna el minimum
     */
    public double minimum() {
        if (minimum == 0) {
            return calcular_minimo();
        }

        return this.minimum;
    }

    /**
     *
     * @return calcula el minimum
     */
    private double calcular_minimo() {
        double min = Double.POSITIVE_INFINITY;
        for (double value : value_instances) {
            if ((min == Double.POSITIVE_INFINITY || min > value)) {
                min = value;
            }
        }
        minimum = min;
        return minimum;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return los valores de las instancias
     */
    public double[] getValue_instances() {
        return value_instances;
    }

    /**
     * @param values the value_instances to set
     */
    public void setValue_instances(double[] values) {
        this.value_instances = new double[values.length];
        System.arraycopy(values, 0, this.value_instances, 0, values.length);
    }

    /**
     *
     * @param value_instances el arreglo de valores
     */
    public void setValue_instances(Double[] varMol) {
        this.value_instances = new double[varMol.length];
        for (int i = 0; i < varMol.length; i++) {

            this.value_instances[i] = varMol[i];
        }

    }

    /**
     *
     * @param num numero a redondear
     * @return valor redondeado 6 valores despues de la coma
     */
    private double redondear(double num) {
        return (double) Math.rint(num * 1000000) / 1000000;

    }

    public static double red(double num) {
        return (double) Math.rint(num * 1000000) / 1000000;

    }

    /**
     *
     * @return la mean de los valores del descriptor
     */
    public double mean() {
        if (this.mean == 0) {
            return calcular_media();
        }
        return this.mean;
    }

    /**
     *
     * @return calcula la mean del descriptor
     */
    private double calcular_media() {
        double d = 0;
        mean = 0;
        for (int i = 0; i < value_instances.length; i++) {
            d += value_instances[i];

        }
        mean = d / value_instances.length;
        return mean;
    }

    /**
     *
     * @return la desviacion estandar de los valores del descriptor
     */
    public double StandarDesviation() {
        if (desvstand == 0) {
            return calcular_StandarDesviation();
        }
        return this.desvstand;
    }

    /**
     *
     * @return la desviacion estandar del descriptor
     */
    private double calcular_StandarDesviation() {
        mean();
        double d = 0;
        desvstand = 0;
        for (int i = 0; i < value_instances.length; i++) {
            d += (double) Math.pow(value_instances[i] - mean, 2);

        }
        desvstand = (double) Math.sqrt(d / (double) (value_instances.length - 1));
        return redondear(desvstand);
    }
    //hacer un metodo que devuelva todas las entropias

    /**
     *
     * @return minimum redondeado
     */
    public double minRedo() {
        return redondear(minimum());
    }

    /**
     *
     * @return maximum redondeado
     */
    public double maxRedo() {
        return redondear(maximum());
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (((Variable) obj).nombre.equals(this.nombre)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    public static double NormalizedShannonEntropy(int bin, Variable valoresA, Variable valoresB) {
        double distribucionProbabilidades[] = Variable.normalizedProbabilitiesDistribution(bin, valoresA, valoresB);
        double SE = 0;
        for (int i = 0; i < distribucionProbabilidades.length; i++) {
            if (distribucionProbabilidades[i] == 0) {
                SE += 0;
            } else {
                SE += distribucionProbabilidades[i] * ((double) Math.log10(distribucionProbabilidades[i]) / (double) Math.log10(2));
            }
        }
        System.out.println("" + valoresA.getNombre() + "\t" + (-1 * SE));
        return (double) -1 * SE;

    }

    public static double[] normalizedProbabilitiesDistribution(int bin, Variable valoresA, Variable valoresB) {
        double tamanno = 0.0;

        double[] valoresNormal = Variable.normalizedValuesDistribution(bin, valoresA, valoresB);

        for (int i = 0; i < valoresNormal.length; i++) {
            tamanno += valoresNormal[i];
        }
        for (int i = 0; i < valoresNormal.length; i++) {
            double d = valoresNormal[i] / tamanno;
            valoresNormal[i] = d;
        }
        return valoresNormal;

    }

    public static double[] normalizedValuesDistribution(int bin, Variable valoresA, Variable valoresB) {
        double val[] = new double[bin];
        int[] valA = valoresA.distribucionDeValores(bin);
        int[] valB = valoresB.distribucionDeValores(bin);

        for (int i = 0; i < bin; i++) {
            val[i] = ((valA[i] + valB[i]) / 2.0);
        }
        return val;

    }

    public void calculatestatistic() {
        maximum();
        minimum();
        mean();
        StandarDesviation();
    }

    public int DegenerationValue() {
        if (degeneration == -1) {
            HashSet<Double> set = new HashSet<Double>();
            for (int i = 0; i < value_instances.length; i++) {
                double d = value_instances[i];
                set.add(d);
            }
            this.degeneration = value_instances.length - set.size();
        }
        return this.degeneration;
    }

    public double DegenerativeEntropy() {
        if (degenerativeEntropy == -1) {
            double entropy = ShannonEntropy(value_instances.length);
            if (entropy == 0) {
                degenerativeEntropy = 0;
            } else {
                degenerativeEntropy = ShannonEntropy(value_instances.length - DegenerationValue()) / entropy * (value_instances.length - DegenerationValue());
            }
        }
        return degenerativeEntropy;
    }

    public void setSingularValueDescomposition(double value) {
        singularvaluedescomposition = value;
    }

    public double getSingularValueDescomposition() {
        return singularvaluedescomposition;
    }

    public double getShannonEntropy() {
        return shannonEntropy;
    }

    public void setShannonEntropy(double shannonEntropy) {
        this.shannonEntropy = shannonEntropy;
    }

    @Override
    public int compareTo(Variable v) {
        return this.shannonEntropy < v.shannonEntropy ? -1 : this.shannonEntropy > v.shannonEntropy ? 1 : 0;
    }
}
