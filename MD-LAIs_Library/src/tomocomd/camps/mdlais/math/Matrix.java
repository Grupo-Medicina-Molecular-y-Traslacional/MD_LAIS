/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.camps.mdlais.math;

import org.openscience.cdk.math.Vector;
import tomocomd.camps.mdlais.tools.invariants.Norms;

/**
 *
 * @author Cesar
 * @author econtreras
 */
public class Matrix extends org.openscience.cdk.math.Matrix {

    public Matrix(int rows, int columns) {
        super(rows, columns);
    }

    public Matrix(double[][] array) {
        super(array);
    }

    public Vector getVectorFromRow(int index) {
        double[] row = new double[columns];
        for (int i = 0; i < columns; i++) {
            row[i] = matrix[index][i];
        }
        return new Vector(row);
    }

    public double[] getVectorFromRow(int index, int cols, double[] row) {
        System.arraycopy(matrix[index], 0, row, 0, cols);
        return row;
    }

    public Matrix mul(Matrix b) {
        if ((b == null) || (columns != b.rows)) {
            return null;
        }

        Matrix result = new Matrix(rows, b.columns);
        double sum;
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < b.columns; k++) {
                sum = 0;
                for (int j = 0; j < columns; j++) {
                    sum += matrix[i][j] * b.matrix[j][k];
                }

                result.matrix[i][k] = sum;
            }
        }

        return result;
    }

    public Matrix mul(int dim, Matrix b, Matrix c) {
        if ((b == null) || (columns != b.rows)) {
            return null;
        }

        double sum;
        for (int i = 0; i < dim; i++) {
            for (int k = 0; k < dim; k++) {
                sum = 0;
                for (int j = 0; j < dim; j++) {
                    sum += matrix[i][j] * b.matrix[j][k];
                }

                c.matrix[i][k] = sum;
            }
        }

        return c;
    }

    @Override
    public Matrix duplicate() {
        Matrix result = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result.matrix[i][j] = matrix[i][j];
            }
        }
        return result;
    }

    public double getElementToPower(int row, int col, double power) {
        if (power == 0) {
            return 1;
        } else {
            double value = matrix[row][col];

            return value != 0 ? Math.pow(value, power) : 0;
        }
    }

    public double getElement(int row, int col) {
        return matrix[row][col];
    }
    
    public Matrix toSS()
    {
    double[][] m = new double[rows][columns];
    
        for (int i = 0; i < rows; i++) 
        {
         double sum = Norms.MinkoskyNorm(matrix[i], 1);
         
            for (int j = 0; j < rows; j++) 
            {
              if(sum!=0){  
              m[i][j] = matrix[i][j]/sum;
              }
            }
        }
        
        return new Matrix(m);
    }
}
