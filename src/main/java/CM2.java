import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;

public class CM2 {
    private double Fi_a;
    private double Fi_b;
    private double Fi_c;
    private double B_b;
    private double B_c;
    private double B_d;
    private double B_e;
    private double pi;
    private double T;
    private double a;
    private double l;
    private double tau;
    private double h;
    private double B_a;

    CM2(double Fi_a, double Fi_b, double Fi_c, double B_b, double B_c, double B_d, double B_e,
        double T, double a, double l, double tau, double h, double B_a){
        this.Fi_a = Fi_a;
        this.Fi_b = Fi_b;
        this.Fi_c = Fi_c;
        this.B_b = B_b;
        this.B_c = B_c;
        this.B_d = B_d;
        this.B_e = B_e;
        this.pi = Math.PI;
        this.T = T;
        this.a = a;
        this.l = l;
        this.tau = tau;
        this.h = h;
        this.B_a = B_a;
    }

    private double FuncFi(double x, double l){
            return Fi_a * 1 + Fi_b * Math.cos(pi * x / l) + Fi_c * Math.cos(2 * pi * x / l);
    }

    private double FuncB(double x, double l){
            return B_a * 1 + B_b * Math.cos(pi * x / l) + B_c * Math.sin(pi * x / l) +
    B_d * Math.cos(2 * pi * x / l) + B_e * Math.sin(2 * pi * x / l);
    }

    private double[] RightConst(double[] y,int size,double tau){
        double l = 1, h = 0.01234;
        double[] x = new double[y.length];
        double c = size / 2;
        double simpson = 0;
        simpson += (1 / 3) * ( FuncB(0, l) * y[0] + FuncB(size-1, l) * y[size-1] );
        for (int i = 2; i < size-1; i+=2)
            simpson += (2 / 3) * (FuncB(i * h, size) * y[i]);

        for (int i = 2; i < size-1; i+=2)
            simpson += (4 / 3) * (FuncB(i * h, size) * y[i]);

        for (int i = 0; i < x.length - 1; i++ )
            x[i] = y[i] * ((1 / tau) + FuncB(i * h, size) - simpson);

//        return null;
        return x;

    }

    public double[][] main() {
        int size = (int)(l / h);
        double[] x = new double[size];
        double[] y = new double[size];
        double[] b = new double[size];
        double[] y0 = new double[size];
        double[][] A = new double[size][size];
        double[][] B = new double[size][size];
        double[] yb = new double[size];

        for(int i =0; i <y.length; i++)
        y[i] = FuncFi(i * h, l);

        for(int i = 0; i <size; i++){
        y0[i] = FuncFi(i * h, l);
        b[i] = FuncB(i * h, l);
        }
        double coef1 = (-1) * (a * a) / (h * h);
        double coef2 = ((2 * a * a) / (h * h)) + (1 / tau);

        for(int i = 1; i < size-1; i++){
            A[i][i - 1] = coef1;
            A[i][i] = coef2;
            A[i][i + 1] = coef1;
        }

        A[0][0] = coef2;
        A[0][1] = coef1;
        A[size-1][size-2] = coef1;
        A[size-1][size-1] = coef2;

        for(int i = 1; i < size-1; i++) {
            B[i][i - 1] = coef1;
            B[i][i] = coef2;
            B[i][i + 1] = coef1;
        }
        B[0][0] = 1;
        B[0][1] = -1;
        B[1][0] = 0;
        B[size - 2][size-1] = 0;
        B[size-1][size - 2] = -1;
        B[size-1][size-1] = 1;

        for(int i =0; i <(size-1); i++)
        yb[i + 1] = y[i];

        yb[0] = 0;
        yb[size-1] = 0;
        Algebra algebra = new Algebra();
        DoubleMatrix2D matrix = new DenseDoubleMatrix2D(B);
        double[][] tmp_y = new double[size][1];;
        for(int i =1; i <(T+1); i++) {
            double[] tmp = RightConst(yb, size, tau);
            for (int j = 0; j < size; j++) {
                tmp_y[j][0] = tmp[j];
            }
            DoubleMatrix2D vector = new DenseDoubleMatrix2D(tmp_y);
            DoubleMatrix2D sol = algebra.solve(matrix, vector);
            tmp_y = sol.toArray();
            for (int j = 0; j < size; j++) {
                yb[j] = tmp_y[j][0];
            }
        }

        for(int i =0; i <(size-1); i++)
            x[i] = i * h;
        double[][] result = new double[2][];
        result[0] = x;
        result[1] = yb;
        return result;
    }

}
