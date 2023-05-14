package proiect_oop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ListaAsigurari {
    private ArrayList<Asigurare> list;

    public ListaAsigurari() {
        list = new ArrayList<Asigurare>();
        Conexiune c = Conexiune.getInstance();
        String sql = "SELECT * FROM Asigurari";
        try {
            Statement stmt = c.getStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                int ID_contract = result.getInt("ID_contract"),
                        ID_masina = result.getInt("ID_masina"),
                        accidente = result.getInt("accidente_ultimii5ani"),
                        pret_initial = result.getInt("pret_initial"),
                        an_achizitionare = result.getInt("an_achizitionare");
                String nume_client = result.getString("nume_client"),
                        tipuri_de_riscuri = result.getString("tipuri_de_riscuri"),
                        imbunatatiri = result.getString("imbunatatiri"),
                        tip_plata = result.getString("tip_plata"),
                        ultima_plata = result.getString("ultima_plata");
                Asigurare a = new Asigurare(ID_contract,ID_masina,accidente,pret_initial,an_achizitionare,nume_client,tipuri_de_riscuri,imbunatatiri,tip_plata,ultima_plata);
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Asigurare> getList() {
        return list;
    }

    public Asigurare getAsigurare(int ID_contract){
        for(Asigurare a : list){
            if(ID_contract == a.getID_contract())
                return a;
        }
        return null;
    }
}
