package proiect_oop;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.Arrays;

public class FereastraContract extends JFrame {
    private String[] label_names, checkBox_names, radioButton_names;
    private JPanel[] panels;
    private JLabel[] labels;
    private JComboBox[] comboBoxes;
    private JTextField[] textFields;
    private JTextArea textArea;
    private JCheckBox[] checkBoxes;
    private JRadioButton[] radioButtons;
    private ButtonGroup bg;
    private JButton b1,b2,b3;
    private int updateID;
    private Conexiune c;

    public FereastraContract(){
        c = Conexiune.getInstance();
        //Initializare fereastra
        setTitle("Contract de asigurare");
        setSize(770, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panels = new JPanel[6];
        for(int i=0; i<panels.length; i++){
            panels[i] = new JPanel();
        }
        panels[0].setLayout(new GridLayout(6,2));
        panels[1].setLayout(new GridLayout(8,2));
        panels[2].setLayout(new FlowLayout());
        panels[3].setLayout(new FlowLayout());
        panels[4].setLayout(new FlowLayout());
        panels[5].setLayout(new GridLayout(2,1));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 10, 20);
        panels[0].setBorder(emptyBorder);
        panels[1].setBorder(emptyBorder);

        //Initializare labels
        label_names = new String[]{"Marca", "An fabricatie", "Model", "Caracteristici", "Indice de risc", "Indice de poluare", "Nume client","Nr accidente in ultimii 5 ani", "Tipuri de riscuri", "Pret initial", "An achizitionare", "Imbunatatiri", "Tip plata"};
        labels = new JLabel[label_names.length];
        for(int i=0; i<label_names.length; i++){
            labels[i] = new JLabel(label_names[i]);
            labels[i].setBorder(emptyBorder);
        }

        //Initializare comboboxes
        comboBoxes = new JComboBox[3];
        for(int i=0; i<comboBoxes.length; i++){
            comboBoxes[i] = new JComboBox();
            comboBoxes[i].setBorder(emptyBorder);
        }
        loadMarca();
        comboBoxes[0].setSelectedItem(null);
        for (JComboBox cb : comboBoxes) {
            ItemChangeListener itemChangeListener = new ItemChangeListener();
            cb.addItemListener(itemChangeListener);
        }

        //Initializare textarea,textfields
        textArea = new JTextArea(2,10);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        textFields = new JTextField[7];
        for(int i=0; i<textFields.length; i++){
            textFields[i] = new JTextField();
        }
        textFields[0].setBorder(emptyBorder);
        textFields[1].setBorder(emptyBorder);
        textFields[0].setEditable(false);
        textFields[1].setEditable(false);

        //Initializare checkboxes
        checkBox_names = new String[]{"Accident", "Cutremur", "Incendiu", "Dezastru natural"};
        checkBoxes = new JCheckBox[checkBox_names.length];
        for(int i=0; i<checkBoxes.length; i++){
            checkBoxes[i] = new JCheckBox();
            checkBoxes[i].setText(checkBox_names[i]);
            checkBoxes[i].setBorder(emptyBorder);
        }

        //Initializare radiobuttons
        radioButton_names = new String[]{"Lunar","Semestrial","Anual"};
        radioButtons = new JRadioButton[radioButton_names.length];
        bg = new ButtonGroup();
        for(int i=0; i<radioButton_names.length; i++){
            radioButtons[i] = new JRadioButton();
            radioButtons[i].setText(radioButton_names[i]);
            bg.add(radioButtons[i]);
        }
        //Initializare buttons
        b1 = new JButton("Calculeaza rata finala");
        b2 = new JButton("Incarca contract");
        b3 = new JButton("Inapoi");
        updateID = 0;
        b1.addActionListener(e -> {
            if(comboBoxes[0].getSelectedIndex() == -1 ||
                    textFields[2].getText() == null || textFields[3].getText() == null || textFields[4].getText() == null || textFields[5].getText() == null ||
                    Arrays.stream(checkBoxes).allMatch(cb -> cb.isSelected()==false) ||
                    bg.getSelection() == null){
                JOptionPane.showMessageDialog(null, "Contractul este incomplet", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
            else{
                try{
                    Asigurare a = saveAsigurare();
                    setVisible(false);
                    if(updateID == 0){
                        a.saveInDB();
                    }
                    else{
                        a.updateInDB(updateID);
                    }
                    new FereastraPrintat(a);
                    dispose();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Date introduse eronat", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        b2.addActionListener(e -> {
            String ID_contract = JOptionPane.showInputDialog("Introdu numarul contractului");
            if(ID_contract != null){
                try{
                    loadContract(Integer.parseInt(ID_contract));
                    updateID = Integer.parseInt(ID_contract);
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Numarul introdus este eronat", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        b3.addActionListener(e -> {
            setVisible(false);
            new FereastraPrincipala();
            dispose();
        });

        //Adaugare componente in fereastra
        panels[0].add(labels[0]); panels[0].add(comboBoxes[0]);
        panels[0].add(labels[1]); panels[0].add(comboBoxes[1]);
        panels[0].add(labels[2]); panels[0].add(comboBoxes[2]);
        panels[0].add(labels[3]); panels[0].add(textArea);
        panels[0].add(labels[4]); panels[0].add(textFields[0]);
        panels[0].add(labels[5]); panels[0].add(textFields[1]);

        panels[1].add(labels[6]); panels[1].add(textFields[2]);
        panels[1].add(labels[7]); panels[1].add(textFields[3]);
        panels[1].add(labels[8]); panels[1].add(new JPanel());
        panels[1].add(checkBoxes[0]); panels[1].add(checkBoxes[1]);
        panels[1].add(checkBoxes[2]); panels[1].add(checkBoxes[3]);
        panels[1].add(labels[9]); panels[1].add(textFields[4]);
        panels[1].add(labels[10]); panels[1].add(textFields[5]);
        panels[1].add(labels[11]); panels[1].add(textFields[6]);

        panels[2].add(panels[0]);
        panels[2].add(panels[1]);

        panels[3].add(labels[12]); panels[3].add(radioButtons[0]); panels[3].add(radioButtons[1]); panels[3].add(radioButtons[2]);

        panels[4].add(b1);
        panels[4].add(b2);
        panels[4].add(b3);

        panels[5].add(panels[3]);
        panels[5].add(panels[4]);

        add(panels[2]);
        add(panels[5],BorderLayout.SOUTH);
    }

    public FereastraContract(int ID_contract){
        this();
        loadContract(ID_contract);
        updateID = ID_contract;
    }

    class ItemChangeListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                if(event.getSource() == comboBoxes[0]){
                    loadAnFabricatie();
                    loadInfo();
                }
                else if(event.getSource() == comboBoxes[1]){
                    loadModel();
                    loadInfo();
                }
                else if(event.getSource() == comboBoxes[2])
                    loadInfo();
            }
        }
    }

    public void loadMarca(){
        String sql = "SELECT DISTINCT marca FROM Masini";
        try {
            Statement stmt = c.getStatement();
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                comboBoxes[0].addItem(result.getString("marca"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAnFabricatie(){
        comboBoxes[1].removeAllItems();
        try {
            PreparedStatement ps = c.getConnection().prepareStatement("SELECT DISTINCT an_fabricatie FROM Masini WHERE marca=?");
            ps.setString(1,comboBoxes[0].getSelectedItem().toString());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                comboBoxes[1].addItem(result.getInt("an_fabricatie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadModel(){
        comboBoxes[2].removeAllItems();
        try{
            PreparedStatement ps = c.getConnection().prepareStatement("SELECT model FROM Masini WHERE marca=? AND an_fabricatie=?");
            ps.setString(1,comboBoxes[0].getSelectedItem().toString());
            ps.setString(2,comboBoxes[1].getSelectedItem().toString());
            ResultSet result = ps.executeQuery();
            while(result.next()){
                comboBoxes[2].addItem(result.getString("model"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void loadInfo(){
        try{
            PreparedStatement ps = c.getConnection().prepareStatement("SELECT an_fabricatie,caracteristici,indice_risc,tip_motor,clasificare_motor,indice_poluare FROM Masini WHERE marca=? AND an_fabricatie=? AND model=?");
            ps.setString(1,comboBoxes[0].getSelectedItem().toString());
            ps.setString(2,comboBoxes[1].getSelectedItem().toString());
            ps.setString(3,comboBoxes[2].getSelectedItem().toString());
            ResultSet result = ps.executeQuery();
            while(result.next()){
                String caracteristici = "",
                        tip_motor = result.getString("tip_motor"), clasificare_motor = result.getString("clasificare_motor");
                int an_fabricatie = result.getInt("an_fabricatie");

                for(String s : result.getString("caracteristici").split(";"))
                    caracteristici += s+"\n";
                textArea.setText(caracteristici);
                textFields[0].setText(""+result.getDouble("indice_risc"));
                double indice_poluare=result.getDouble("indice_poluare");
                if(result.wasNull()){
                    if(tip_motor.equals("diesel"))
                        indice_poluare+=0.05;
                    else if(tip_motor.equals("petrol"))
                        indice_poluare+=0.10;
                    if(!clasificare_motor.equals(""))
                        indice_poluare += Integer.parseInt(clasificare_motor.replace("euro","")) * 0.10;
                    indice_poluare += 0.003*(Year.now().getValue() - an_fabricatie);
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.DOWN);
                    indice_poluare = Double.parseDouble(df.format(indice_poluare));

                    ps = c.getConnection().prepareStatement("UPDATE Masini SET indice_poluare=? WHERE marca=? AND an_fabricatie=? AND model=?");
                    ps.setDouble(1,indice_poluare);
                    ps.setString(2,comboBoxes[0].getSelectedItem().toString());
                    ps.setString(3,comboBoxes[1].getSelectedItem().toString());
                    ps.setString(4,comboBoxes[2].getSelectedItem().toString());
                    ps.executeUpdate();
                }
                textFields[1].setText(indice_poluare+"");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        pack();
    }

    public void loadContract(int ID_contract){
        try{
            PreparedStatement ps = c.getConnection().prepareStatement("SELECT * FROM Asigurari WHERE ID_contract=?");
            ps.setInt(1,ID_contract);
            ResultSet result = ps.executeQuery();
            if(!result.next()){
                JOptionPane.showMessageDialog(null, "Nu exista contract cu acest numar", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
            else {
                int ID_masina = 0;
                do{
                    ID_masina = result.getInt("ID_masina");
                    int accidente_ultimii5ani = result.getInt("accidente_ultimii5ani"),
                            pret_initial = result.getInt("pret_initial"),
                            an_achizitionare = result.getInt("an_achizitionare");
                    String nume_client = result.getString("nume_client"),
                            tipuri_de_riscuri = result.getString("tipuri_de_riscuri"),
                            imbunatatiri = result.getString("imbunatatiri"),
                            tip_plata = result.getString("tip_plata");

                    textFields[3].setText(accidente_ultimii5ani + "");
                    textFields[4].setText(pret_initial + "");
                    textFields[5].setText(an_achizitionare + "");
                    textFields[2].setText(nume_client);
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (tipuri_de_riscuri.contains(checkBox_names[i]))
                            checkBoxes[i].setSelected(true);
                        else
                            checkBoxes[i].setSelected(false);
                    }
                    textFields[6].setText(imbunatatiri);
                    switch (tip_plata) {
                        case "Lunar" -> radioButtons[0].setSelected(true);
                        case "Semestrial" -> radioButtons[1].setSelected(true);
                        case "Anual" -> radioButtons[2].setSelected(true);
                    }
                } while (result.next());
                ps = c.getConnection().prepareStatement("SELECT marca,an_fabricatie,model FROM Masini WHERE ID_masina=?");
                ps.setInt(1, ID_masina);
                result = ps.executeQuery();
                while (result.next()) {
                    comboBoxes[0].setSelectedItem(result.getString("marca"));
                    comboBoxes[1].setSelectedItem(result.getInt("an_fabricatie"));
                    comboBoxes[2].setSelectedItem(result.getString("model"));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        pack();
    }

    public int getID_masina(){
        try {
            PreparedStatement ps = c.getConnection().prepareStatement("SELECT ID_masina FROM Masini WHERE marca=? AND an_fabricatie=? AND model=?");
            ps.setString(1,comboBoxes[0].getSelectedItem().toString());
            ps.setString(2,comboBoxes[1].getSelectedItem().toString());
            ps.setString(3,comboBoxes[2].getSelectedItem().toString());
            ResultSet result = ps.executeQuery();
            result.next();
            return result.getInt("ID_masina");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Asigurare saveAsigurare(){
        int ID_masina = getID_masina(),
                accidente_ultimii5ani = Integer.parseInt(textFields[3].getText()),
                pret_initial = Integer.parseInt(textFields[4].getText()),
                an_achizitionare = Integer.parseInt(textFields[5].getText());
        String nume_client = textFields[2].getText(),
                tipuri_de_riscuri="",
                imbunatatiri = textFields[6].getText(),
                tip_plata = "";
        for(int i=0; i<radioButtons.length; i++){
            if(radioButtons[i].isSelected())
                tip_plata = radioButton_names[i];
        }
        for(int i=0; i<checkBox_names.length; i++){
            if(checkBoxes[i].isSelected())
                tipuri_de_riscuri+=checkBox_names[i]+";";
        }
        return new Asigurare(0,ID_masina,accidente_ultimii5ani,pret_initial,an_achizitionare,nume_client,tipuri_de_riscuri,imbunatatiri,tip_plata,"");
    }
}
