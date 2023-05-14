package proiect_oop;

import javax.swing.*;
import java.sql.*;

public class Conexiune {
    private static Conexiune instance;
    private Connection con;
    private Statement stmt;

    public Conexiune() {
        String databaseURL = "jdbc:ucanaccess://AsigurareMasinaDB.accdb";
        try {
            con = DriverManager.getConnection(databaseURL);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Conectarea la baza de date a esuat", "Eroare fatala", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public static Conexiune getInstance() {
        if (instance == null)
            instance = new Conexiune();
        return instance;
    }

    public Connection getConnection() {
        return con;
    }

    public Statement getStatement() {
        return stmt;
    }
}