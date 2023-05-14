package proiect_oop;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class FereastraVerificare extends JFrame {
    private JPanel p1,p2,p3;
    private JLabel l1,l2,l3,l4;
    private JTextField[] textFields;
    private JList list;
    private JButton b1,b2;
    private Conexiune c;

    public FereastraVerificare() {
        c = Conexiune.getInstance();
        //Initializare fereastra
        setTitle("Verificare plati");
        setSize(680, 250);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        p1 = new JPanel(); p2 = new JPanel(); p3 = new JPanel();
        p1.setBorder(emptyBorder);
        p1.setLayout(new GridLayout(4,2,10,10));
        p2.setLayout(new GridLayout(1,2));
        p3.setLayout(new GridLayout(2,1,0,15));

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

        //Initializare list
        ListaAsigurari li = new ListaAsigurari();
        String neplatitori="", IDs="";
        for (Asigurare a : li.getList()) {
            String tip_plata = a.getTip_plata(),
                    ultima_plata = a.getUltima_plata();
            if (ultima_plata == null) {
                neplatitori += a.getNume_client()+";";
                IDs += a.getID_contract()+";";
            }
            else {
                long timeDifference = ChronoUnit.MONTHS.between(LocalDate.parse(ultima_plata), LocalDate.now());
                if (tip_plata.equals("Lunar") && timeDifference >= 1 || tip_plata.equals("Semestrial") && timeDifference >= 6 || tip_plata.equals("Anual") && timeDifference >= 12){
                    neplatitori += a.getNume_client()+";";
                    IDs += a.getID_contract()+";";
                }
            }
        }
        String[] splitIDs = IDs.split(";");
        int[] integerIDs = Arrays.stream(splitIDs).mapToInt(Integer::parseInt).toArray();

        list = new JList(neplatitori.split(";"));
        list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
                JList source = (JList)e.getSource();
                int selectedIndex = source.getSelectedIndex();
                Asigurare a = li.getAsigurare(integerIDs[selectedIndex]);
                String tip_plata = a.getTip_plata(),
                        ultima_plata = a.getUltima_plata();
                textFields[0].setText(a.getID_contract()+"");
                textFields[1].setText(a.getRata()+"");
                textFields[2].setText(tip_plata);
                if (ultima_plata == null) {
                    textFields[3].setText("n/a");
                }
                else {
                    textFields[3].setText(ChronoUnit.DAYS.between(LocalDate.parse(ultima_plata), LocalDate.now())+"");
                }
            }
        });

        //Initializare buttons
        b1 = new JButton("Anuleaza"); b2 = new JButton("Inapoi");
        b1.addActionListener(e -> {
            if(list.getSelectedValue() == null){
                JOptionPane.showMessageDialog(null, "Nu ati selectat niciun contract", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
            else{
                try {
                    PreparedStatement ps = c.getConnection().prepareStatement("DELETE FROM Asigurari WHERE ID_contract=?");
                    ps.setInt(1,Integer.parseInt(textFields[0].getText()));
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
                new FereastraVerificare();
                dispose();
            }
        });
        b2.addActionListener(e -> {
            setVisible(false);
            new FereastraPrincipala();
            dispose();
        });

        //Adaugare componente in fereastra
        p1.add(l1); p1.add(textFields[0]);
        p1.add(l2); p1.add(textFields[1]);
        p1.add(l3); p1.add(textFields[2]);
        p1.add(l4); p1.add(textFields[3]);

        p2.add(list); p2.add(p1);

        p3.add(b1); p3.add(b2);

        add(p2);
        add(p3);
    }
}
