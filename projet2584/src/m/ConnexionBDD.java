/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author apollo7
 * Classe utilisée pour établir une connexion avec la base de données
 * Hérite de la classe Serializable
 */
public class ConnexionBDD implements Serializable {

    private String host, port, dbname, username, password;
    private Connection con = null;

    /**
     * Constructeur de la classe connexionBDD
     * @param h
     * @param po
     * @param dbn nom de la base de données
     * @param u nom d'utilisateur
     * @param p mot de passe
     */
    public ConnexionBDD(String h, String po, String dbn, String u, String p) {
        this.host = h;
        this.port = po;
        this.dbname = dbn;
        this.username = u;
        this.password = p;
    }

    /**
     * Ouvre la connexion avec la base de données
     */
    private void openConnexion() {
        String connectUrl = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbname;
        if (con != null) {
            this.closeConnexion();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(connectUrl, username, password);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Cannot load db driver: com.mysql.jdbc.Driver");
            cnfe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur inattendue");
            e.printStackTrace();
        }
    }

    /**
     * Ferme la connexion avec la base de données
     */
    private void closeConnexion() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Database connection terminated.");
            } catch (Exception e) { /* ignore close errors */ }
        }
    }

    /**
     * Permet d'interroger la base de données
     * @param query la requête a exécuter
     * @return la réponse de la bdd à la requête
     * @see ResultSet#getMetaData() 
     * @see ResultSetMetaData#getColumnCount()
     * @see Statement#executeQuery(java.lang.String)
     */
    public ArrayList<String> getTuples(String query) {
        ArrayList<String> res = null;
        try {
            this.openConnexion();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData(); // permet de récupérer les noms des colonnes des tuples en sortie de la requête
            String tuple;
            res = new ArrayList<>();
            while (rs.next()) {
                tuple = "";
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    tuple += rs.getString(i);
                    if (i<metadata.getColumnCount()) {
                        tuple +=";";
                    }
                }
                res.add(tuple);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Probleme avec la requete");
        } finally {
            this.closeConnexion();
        }
        return res;
    }
    
    /**
     * Insère un ou plusieurs tuples dans la bdd
     * @param updateQuery requête
     * @see Statement#executeUpdate(java.lang.String) 
     */
    public void insertTuples(String updateQuery) {
        try {
            this.openConnexion();
            Statement stmt = con.createStatement();
            int n = stmt.executeUpdate(updateQuery);
            System.out.println(n+" tuples inseres");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Probleme avec la requete d'insertion");
            System.out.println("Tuple deja existant");
        } finally {
            this.closeConnexion();
        }
    }
}
