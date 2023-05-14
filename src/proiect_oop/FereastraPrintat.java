package proiect_oop;

import javax.swing.*;
import java.awt.*;

public class FereastraPrintat extends JFrame {
    private JPanel p1,p2;
    private JLabel l;
    private JTextField tf;
    private JButton b1,b2;

    public FereastraPrintat(Asigurare a){
        setTitle("Contract de asigurare");
        setSize(250, 120);
        setLayout(new GridLayout(2,1));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        p1 = new JPanel(); p2 = new JPanel();
        p1.setLayout(new FlowLayout());
        p2.setLayout(new FlowLayout());

        l = new JLabel("Rata finala");

        tf = new JTextField();
        tf.setText(a.getRata()+"");
        tf.setEditable(false);

        b1 = new JButton("Printeaza contract");
        b1.addActionListener(e -> {
            setVisible(false);
            a.printAsigurare();
            new FereastraContract();
            dispose();
        });
        b2 = new JButton("Inchide");
        b2.addActionListener(e -> {
            setVisible(false);
            new FereastraContract();
            dispose();
        });

        p1.add(l); p1.add(tf);
        p2.add(b1); p2.add(b2);
        add(p1);
        add(p2);
    }
}
