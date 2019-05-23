import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class form extends JFrame{
    private JButton button1;
    private JPanel outputPanel;
    private JPanel mainPanel;
    private Plot2DPanel plot;
    private JTextField Fi_cin;
    private JTextField Tin;
    private JTextField Fi_bin;
    private JTextField Fi_ain;
    private JTextField tauin;
    private JTextField hin;
    private JTextField Lin;
    private JTextField Ain;
    private JTextField B_ain;
    private JTextField B_bin;
    private JTextField B_cin;
    private JTextField B_din;
    private JTextField B_ein;
    double Fi_a;
    double Fi_b;
    double Fi_c;
    double B_a;
    double B_b;
    double B_c;
    double B_d;
    double B_e;
    double T;
    double a;
    double l;
    double tau;
    double h;
    int a1, a2, a3;


    private void getFields() {
        this.Fi_a = Double.valueOf(Fi_ain.getText());
        this.Fi_b = Double.valueOf(Fi_bin.getText());
        this.Fi_c = Double.valueOf(Fi_cin.getText());
        this.B_a = Double.valueOf(B_ain.getText());
        this.B_b = Double.valueOf(B_bin.getText());
        this.B_c = Double.valueOf(B_cin.getText());
        this.B_d = Double.valueOf(B_din.getText());
        this.B_e = Double.valueOf(B_ein.getText());
        this.T = Double.valueOf(Tin.getText());
        this.a = Double.valueOf(Ain.getText());
        this.l = Double.valueOf(Lin.getText());
        this.tau = Double.valueOf(tauin.getText());
        this.h = Double.valueOf(hin.getText());
    }

    public form() {
        outputPanel.setVisible(true);
        setContentPane(mainPanel);
        setVisible(true);
        outputPanel.setSize(500,500);

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getFields();
                CM2 cm2 = new CM2(Fi_a, Fi_b, Fi_c, B_b, B_c, B_d, B_e,
                        T, a, l, tau, h, B_a);
                double res[][] = cm2.main();
                plot.removeAllPlots();
                a1 = plot.addLinePlot("my plot", Color.RED, res[0], res[1]);
                a2 = plot.addLinePlot("my plot", Color.BLACK, res[0], res[2]);
                a3 = plot.addLinePlot("my plot", Color.BLUE, res[0], res[3]);

                outputPanel = plot;
                outputPanel.invalidate();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {

        form G = new form();
        G.setSize(1400,1000);
        G.setVisible(true);
        G.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while (true){
            Thread.sleep(500);
        }
    }

    private void createUIComponents() {
        plot = new Plot2DPanel();
        outputPanel = plot;
        outputPanel.setSize(500,500);
        outputPanel.setVisible(true);
        outputPanel.repaint();
    }
}
