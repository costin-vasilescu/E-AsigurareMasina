package proiect_oop;

import javax.swing.*;
import java.awt.*;

public class FereastraPrincipala extends JFrame {
    private JButton[] buttons;

    public FereastraPrincipala(){
        setTitle("E-Asigurari Masini");
        setSize(600, 80);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] button_names = {"Inregistrare contract", "Modificare contract", "Plata asigurare", "Verificare plati"};
        buttons = new JButton[4];
        for(int i=0; i<buttons.length; i++){
            buttons[i] = new JButton(button_names[i]);
            add(buttons[i]);
        }
        buttons[0].addActionListener(e -> {
            setVisible(false);
            new FereastraContract();
            dispose();
        });
        buttons[1].addActionListener(e -> {
            String ID_contract = JOptionPane.showInputDialog("Introdu numarul contractului");
            if(ID_contract != null){
                try{
                    new FereastraContract(Integer.parseInt(ID_contract));
                    dispose();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Numarul introdus este eronat", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttons[2].addActionListener(e -> {
            String ID_contract = JOptionPane.showInputDialog("Introdu numarul contractului");
            if(ID_contract != null){
                try{
                    new FereastraPlata(Integer.parseInt(ID_contract));
                    dispose();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Numarul introdus este eronat", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttons[3].addActionListener(e -> {
            setVisible(false);
            new FereastraVerificare();
            dispose();
        });
        pack();
    }
}