package proiect_oop;

import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.concurrent.Callable;

public class CalculatorRata implements Callable<Double> {
    private int ID_masina,accidente_ultimii5ani,pret_initial,an_achizitionare;
    private String tipuri_de_riscuri,imbunatatiri,tip_plata;
    Conexiune c;

    public CalculatorRata(int ID_masina, int accidente_ultimii5ani, int pret_initial, int an_achizitionare, String tipuri_de_riscuri, String imbunatatiri, String tip_plata) {
        this.ID_masina = ID_masina;
        this.accidente_ultimii5ani = accidente_ultimii5ani;
        this.pret_initial = pret_initial;
        this.an_achizitionare = an_achizitionare;
        this.tipuri_de_riscuri = tipuri_de_riscuri;
        this.imbunatatiri = imbunatatiri;
        this.tip_plata = tip_plata;
        c = Conexiune.getInstance();
    }

    public Double call(){
        double rata = 1, indice_risc, indice_poluare=0;
        String caracteristici;

        try {
            PreparedStatement ps = c.getConnection().prepareStatement("SELECT caracteristici,indice_risc,indice_poluare FROM Masini WHERE ID_masina=?");
            ps.setInt(1,ID_masina);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                caracteristici = result.getString("caracteristici");
                indice_risc = result.getDouble("indice_risc");
                indice_poluare = result.getDouble("indice_poluare");
                rata += 3*caracteristici.split(";").length;
                rata += rata*indice_risc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        rata += rata*indice_poluare;
        rata += 40.0/100.0 * rata * accidente_ultimii5ani;
        if(tipuri_de_riscuri.equals("Accident"))
            rata += 20;
        if(tipuri_de_riscuri.equals("Incendiu"))
            rata += 30;
        if(tipuri_de_riscuri.equals("Cutremur"))
            rata += 25;
        if(tipuri_de_riscuri.equals("Dezastru natural"))
            rata += 25;
        rata += 0.2/100.0*pret_initial;
        rata += 2*(Year.now().getValue() - an_achizitionare);
        rata += 3*imbunatatiri.split(";").length;
        if(tip_plata.equals("Semestrial")){
            rata *= 6;
            rata -= rata*5.0/100.0;
        }
        else if(tip_plata.equals("Anual")){
            rata *= 12;
            rata -= rata*10.0/100.0;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(rata));
    }
}
