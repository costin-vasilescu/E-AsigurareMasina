package proiect_oop;

import javax.print.DocFlavor;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.io.*;
import java.sql.*;
import java.util.concurrent.FutureTask;

public class Asigurare {
    private int ID_contract,ID_masina,accidente_ultimii5ani,pret_initial,an_achizitionare;
    private String nume_client,tipuri_de_riscuri,imbunatatiri,tip_plata,ultima_plata;
    private Conexiune c;
    private FutureTask<Double> ft;

    public Asigurare(int ID_contract, int ID_masina, int accidente_ultimii5ani, int pret_initial, int an_achizitionare, String nume_client, String tipuri_de_riscuri, String imbunatatiri, String tip_plata, String ultima_plata) {
        if(ID_contract != 0)
            this.ID_contract = ID_contract;
        this.ID_masina = ID_masina;
        this.accidente_ultimii5ani = accidente_ultimii5ani;
        this.pret_initial = pret_initial;
        this.an_achizitionare = an_achizitionare;
        this.nume_client = nume_client;
        this.tipuri_de_riscuri = tipuri_de_riscuri;
        this.imbunatatiri = imbunatatiri;
        this.tip_plata = tip_plata;
        this.ultima_plata = ultima_plata;

        c = Conexiune.getInstance();
        CalculatorRata cr  = new CalculatorRata(ID_masina,accidente_ultimii5ani,pret_initial,an_achizitionare,tipuri_de_riscuri,imbunatatiri,tip_plata);
        ft = new FutureTask<>(cr);
    }

    public void saveInDB(){
        try {
            PreparedStatement ps = c.getConnection().prepareStatement("INSERT INTO Asigurari([ID_masina],[nume_client],[accidente_ultimii5ani],[tipuri_de_riscuri],[pret_initial],[an_achizitionare],[imbunatatiri],[tip_plata]) VALUES (?,?,?,?,?,?,?,?)");
            ps.setInt(1,ID_masina);
            ps.setString(2,nume_client);
            ps.setInt(3,accidente_ultimii5ani);
            ps.setString(4,tipuri_de_riscuri);
            ps.setInt(5,pret_initial);
            ps.setInt(6,an_achizitionare);
            ps.setString(7,imbunatatiri);
            ps.setString(8,tip_plata);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateInDB(int ID_contract){
        try {
            PreparedStatement ps = c.getConnection().prepareStatement("UPDATE Asigurari SET ID_masina=?,nume_client=?,accidente_ultimii5ani=?,tipuri_de_riscuri=?,pret_initial=?,an_achizitionare=?,imbunatatiri=?,tip_plata=? WHERE ID_contract=?");
            ps.setInt(1,ID_masina);
            ps.setString(2,nume_client);
            ps.setInt(3,accidente_ultimii5ani);
            ps.setString(4,tipuri_de_riscuri);
            ps.setInt(5,pret_initial);
            ps.setInt(6,an_achizitionare);
            ps.setString(7,imbunatatiri);
            ps.setString(8,tip_plata);
            ps.setInt(9,ID_contract);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printAsigurare(){
        String marca="",model="",caracteristici="";
        int an_fabricatie=0;
        double indice_risc=0,indice_poluare=0;
        try{
            PreparedStatement ps = c.getConnection().prepareStatement("SELECT marca,an_fabricatie,model,caracteristici,indice_risc,indice_poluare FROM Masini WHERE ID_masina=?");
            ps.setInt(1, ID_masina);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                marca = result.getString("marca");
                an_fabricatie = result.getInt("an_fabricatie");
                model = result.getString("model");
                caracteristici = result.getString("caracteristici");
                indice_risc = result.getDouble("indice_risc");
                indice_poluare = result.getDouble("indice_poluare");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder("<p style=\"text-align:center;font-size:40px\">Contract de asigurare</p>");
        sb.append("<p>Marca: "); sb.append(marca); sb.append("</p>");
        sb.append("<p>An fabricatie: "); sb.append(an_fabricatie); sb.append("</p>");
        sb.append("<p>Model: "); sb.append(model); sb.append("</p>");
        sb.append("<p>Caracteristici: "); sb.append(caracteristici); sb.append("</p>");
        sb.append("<p>Indice risc: "); sb.append(indice_risc); sb.append("</p>");
        sb.append("<p>Indice poluare: "); sb.append(indice_poluare); sb.append("</p>");
        sb.append("<p>Nume client: "); sb.append(nume_client); sb.append("</p>");
        sb.append("<p>Nr accidente in ultimii 5 ani: "); sb.append(marca); sb.append("</p>");
        sb.append("<p>Tipuri de riscuri: "); sb.append(tipuri_de_riscuri); sb.append("</p>");
        sb.append("<p>Pret initial: "); sb.append(pret_initial); sb.append("</p>");
        sb.append("<p>An achizitionare: "); sb.append(an_achizitionare); sb.append("</p>");
        sb.append("<p>Imbunatatiri: "); sb.append(imbunatatiri); sb.append("</p>");
        sb.append("<p>Tip plata: "); sb.append(tip_plata); sb.append("</p>");
        sb.append("<p>Rata: "); sb.append(getRata()); sb.append("</p>");
        sb.append("<p>Data: "); sb.append("</p>");
        sb.append("<p>Semnatura angajat: "); sb.append("</p>");
        sb.append("<p>Semnatura client: "); sb.append("</p>");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("contract.html"));
            bw.write(sb.toString());
            bw.close();

            PrinterText pt = new PrinterText(new HashPrintRequestAttributeSet(), DocFlavor.INPUT_STREAM.AUTOSENSE);
            pt.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public double getRata(){
        Thread t = new Thread(ft);
        t.start();
        try{
            return ft.get();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public int getID_contract() {
        return ID_contract;
    }

    public String getNume_client() {
        return nume_client;
    }

    public String getTip_plata() {
        return tip_plata;
    }

    public String getUltima_plata() {
        return ultima_plata;
    }
}
