package proiect_oop;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FereastraPlata extends JFrame {
    private JPanel[] panels;
    private JLabel l1,l2,l3,l4;
    private JTextField[] textFields;
    private JButton b1,b2,b3;
    private Conexiune c;
    
    public FereastraPlata(int ID_contract){
        c = Conexiune.getInstance();
        //Initializare fereastra
        setTitle("Plata asigurare");
        setSize(300, 250);
        setLayout(new GridLayout(5,1,10,10));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Border emptyBorder = BorderFactory.createEmptyBorder(10,0,10,0);
        panels = new JPanel[5];
        for(int i=0; i<panels.length; i++){
            panels[i] = new JPanel(new FlowLayout());
        }

        //Initializare labels
        l1 = new JLabel("Numar contract: ");
        l2 = new JLabel("Rata: ");
        l3 = new JLabel("Tip plata: ");
        l4 = new JLabel("Zile de la ultima plata: ");

        //Initializare textfields
        textFields = new JTextField[4];
        for(int i=0; i<textFields.length; i++){
            textFields[i] = new JTextField();
            textFields[i].setBorder(emptyBorder);
            textFields[i].setEditable(false);
        }
        textFields[0].setText(ID_contract+"");

        //Initializare buttons
        JButton b1 = new JButton("Plateste"); b2 = new JButton("Anuleaza"); b3 = new JButton("Inapoi");
        b1.addActionListener(e -> {
            try {
                PreparedStatement ps = c.getConnection().prepareStatement("UPDATE Asigurari SET ultima_plata=? WHERE ID_contract=?");
                ps.setString(1,LocalDate.now().toString());
                ps.setInt(2,ID_contract);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setVisible(false);
            new FereastraPrincipala();
            dispose();
        });
        b2.addActionListener(e -> {
            try {
                PreparedStatement ps = c.getConnection().prepareStatement("DELETE FROM Asigurari WHERE ID_contract=?");
                ps.setInt(1,ID_contract);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            setVisible(false);
            new FereastraPrincipala();
            dispose();
        });
        b3.addActionListener(e -> {
            setVisible(false);
            new FereastraPrincipala();
            dispose();
        });

        //Actualizare componente
        ListaAsigurari li = new ListaAsigurari();
        Asigurare a = li.getAsigurare(ID_contract);
        if(a!=null){
            textFields[1].setText(a.getRata()+"");
            String tip_plata = a.getTip_plata(),
                    ultima_plata = a.getUltima_plata();
            textFields[2].setText(" "+tip_plata);
            if(ultima_plata == null){
                textFields[3].setText("n/a");
            }
            else{
                textFields[3].setText(ChronoUnit.DAYS.between(LocalDate.parse(ultima_plata), LocalDate.now())+"");

                long timeDifference = ChronoUnit.MONTHS.between(LocalDate.parse(ultima_plata), LocalDate.now());
                if(tip_plata.equals("Lunar") && timeDifference < 1 || tip_plata.equals("Semestrial") && timeDifference < 6 || tip_plata.equals("Anual") && timeDifference < 12)
                    b1.setEnabled(false);
            }
        }

        //Adaugare componente in fereastra
        panels[0].add(l1); panels[0].add(textFields[0]);
        panels[1].add(l2); panels[1].add(textFields[1]);
        panels[2].add(l3); panels[2].add(textFields[2]);
        panels[3].add(l4); panels[3].add(textFields[3]);
        panels[4].add(b1); panels[4].add(b2); panels[4].add(b3);

        add(panels[0]);
        add(panels[1]);
        add(panels[2]);
        add(panels[3]);
        add(panels[4]);
    }

}
