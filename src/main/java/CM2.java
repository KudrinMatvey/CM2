import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;

public class CM2 {
    static double Fi_a = 3.5;
    static double Fi_b = 1;
    static double Fi_c = 1;
    static double B_a = 0;
    static double B_b = 0.25;
    static double B_c = -0.25;
    static double B_d = -0.5;
    static double B_e = -0.5;
    static double pi = 3.14;

    static double FuncFi(double x, double l){
            return Fi_a * 1 + Fi_b * Math.cos(pi * x / l) + Fi_c * Math.cos(2 * pi * x / l);
    }

    static double FuncB(double x, double l){
            return B_a * 1 + B_b * Math.cos(pi * x / l) + B_c * Math.sin(pi * x / l) +
    B_d * Math.cos(2 * pi * x / l) + B_e * Math.sin(2 * pi * x / l);
    }

    static double[] RightConst(double[] y,int size,double tau){
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

    public static void main(String[] args) {

//        double[] y = new double[200];
//
//        for (int i = -100; i < 100; i++) {
//            x[i + 100] = i;
//            y[i+ 100] = i + 12* i * i - 0.5*(i * i * i);
//        }


        double T = 1 ; double a  = 2; double l = 42 ; double tau = 1 ; double h = 1 ; double Fi_a = 3;
        double Fi_b = 1 ; double Fi_c = 1; double B_a = 0 ; double B_b = 0.25 ; double B_d = 0.25 ; double B_c = 0.5 ; double B_e = 0.5 ;
//        T = Double.valueOf(args[0]) ; l = Double.valueOf(args[1]) ; a = Double.valueOf(args[2]) ;
//        h = Double.valueOf(args[3]) ; tau = Double.valueOf(args[4]);
//        Fi_a = Double.valueOf(args[5]) ; Fi_b = Double.valueOf(args[6]) ; Fi_c = Double.valueOf(args[7]);
//        B_a = Double.valueOf(args[8]) ; B_b = Double.valueOf(args[9]) ; B_c = Double.valueOf(args[10]) ;
//        B_d = Double.valueOf(args[11]) ; B_e = Double.valueOf(args[12]);


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

//        print(b)
//        print(y0)
//        print(yb)

//        plt.plot(x, yb, color='black')
//        plt.grid()
//        plt.minorticks_on()
//        plt.grid(which='minor', color = 'gray', linestyle = ':')
//        plt.plot(x, y0, color='blue')
//        plt.plot(x, b, color='red')
//        plt.show()
        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        // add a line plot to the PlotPanel
        plot.addLinePlot("my plot", Color.CYAN, x, yb);
//        plot.addPlot()

        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(1000, 1000);

    }

}
