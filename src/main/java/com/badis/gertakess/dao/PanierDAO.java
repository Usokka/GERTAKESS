package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.controller.EmployeeViewControllers.EmployeesController;
import com.badis.gertakess.model.*;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanierDAO {


    public static void insertPanier(Panier panier){
        String query = "INSERT INTO panier(idVente) VALUES (?)";

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,panier.getVente().getIdVente());
            statement.executeUpdate();
            System.out.println("ajout succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Panier getLastPanierOfVente(int idVente){
        String query = "SELECT * FROM Panier" +
                " WHERE idPan = (" +
                " SELECT MAX(idPan) FROM Panier WHERE idVente = ? )";
        Panier panier = null;
        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,idVente);

            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                panier = new Panier();
                Vente vente = new Vente();
                vente.setIdVente(idVente);
                panier.setIdPanier(rs.getInt("idPan"));
                panier.setVente(vente);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return panier;
    }

    public static List<Panier> getPaniers(String nomEmpl, LocalDate date, boolean jour, boolean sem, boolean mois, boolean annee) {
        String query = """
        SELECT 
            v.idVente,
            v.dateVente,
            p.idPan,
            e.idEmpl,
            e.nomEmpl,
            e.prenomEmpl,
            ldv.idProd,
            pr.nomProd,
            pr.codeBarreProd,
            ldv.qteProdVente,
            ldv.prixUniteVente
        FROM 
            Panier p
        INNER JOIN 
            Vente v ON p.idVente = v.idVente
        INNER JOIN 
            Employe e ON v.idEmpl = e.idEmpl
        INNER JOIN 
            lignedevente ldv ON p.idPan = ldv.idPan
        INNER JOIN
            produit pr ON ldv.idProd = pr.idProd
    """;

        List<String> filtres = new ArrayList<>();
        List<Object> valeurs = new ArrayList<>();
        List<Panier> paniers = new ArrayList<>();
        Map<Integer, Panier> paniersMap = new HashMap<>();

        // Ajout des filtres dynamiques
        if (date == null) {
            if (jour) {
                filtres.add("DATE(v.dateVente) = CURDATE()");
            } else if (sem) {
                filtres.add("YEARWEEK(v.dateVente, 0) = YEARWEEK(CURDATE(), 0)"); // Semaine actuelle ISO
            } else if (mois) {
                filtres.add("MONTH(v.dateVente) = MONTH(CURDATE()) AND YEAR(v.dateVente) = YEAR(CURDATE())");
            } else if (annee) {
                filtres.add("YEAR(v.dateVente) = YEAR(CURDATE())");
            }
        } else {
            if (jour) {
                filtres.add("DATE(v.dateVente) = ?");
                valeurs.add(java.sql.Date.valueOf(date));
            } else if (sem) {
                filtres.add("YEARWEEK(v.dateVente, 0) = YEARWEEK(?, 0)"); // Comparaison avec la semaine donnée
                valeurs.add(java.sql.Date.valueOf(date));
            } else if (mois) {
                filtres.add("MONTH(v.dateVente) = ? AND YEAR(v.dateVente) = ?");
                valeurs.add(date.getMonthValue());
                valeurs.add(date.getYear());
            } else if (annee) {
                filtres.add("YEAR(v.dateVente) = ?");
                valeurs.add(date.getYear());
            }
        }


        // Filtre sur le nom de l'employé
        if (nomEmpl != null && !nomEmpl.trim().isEmpty()) {
            filtres.add("CONCAT(e.nomEmpl, ' ', e.prenomEmpl) LIKE ?");
            valeurs.add("%" + nomEmpl + "%");
        }

        if (!filtres.isEmpty()) {
            query += " WHERE " + String.join(" AND ", filtres);
        }

        try (Connection conn = DbConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Injection des paramètres dans le PreparedStatement
            for (int i = 0; i < valeurs.size(); i++) {
                Object valeur = valeurs.get(i);
                if (valeur instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) valeur);
                } else if (valeur instanceof String) {
                    stmt.setString(i + 1, (String) valeur);
                } else if (valeur instanceof java.sql.Date) {
                    stmt.setDate(i + 1, (java.sql.Date) valeur);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idPan = rs.getInt("idPan");

                    // Vérifier si le panier est déjà en mémoire
                    Panier panier = paniersMap.get(idPan);
                    if (panier == null) {
                        Employee employee = new Employee();
                        employee.setIdEmpl(rs.getInt("idEmpl"));
                        employee.setNomEmpl(rs.getString("nomEmpl"));
                        employee.setPrenomEmpl(rs.getString("prenomEmpl"));

                        Vente vente = new Vente();
                        vente.setIdVente(rs.getInt("idVente"));
                        vente.setDateVente(rs.getDate("dateVente").toLocalDate());
                        vente.setEmploye(employee);

                        panier = new Panier();
                        panier.setIdPanier(idPan);
                        panier.setVente(vente);
                        panier.setLignesDeVentes(new ArrayList<>());

                        paniers.add(panier);
                        paniersMap.put(idPan, panier);
                    }

                    // Ajout des lignes de vente
                    Produit produit = new Produit();
                    produit.setIdProd(rs.getInt("idProd"));
                    produit.setNomProd(rs.getString("nomProd"));
                    produit.setCodeBarProd(rs.getString("codeBarreProd"));

                    LigneDeVente ldv = new LigneDeVente();
                    ldv.setProduit(produit);
                    ldv.setQuantiteProd(rs.getInt("qteProdVente"));
                    ldv.setPrixUniteProd(rs.getDouble("prixUniteVente"));

                    panier.getLignesDeVentes().add(ldv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paniers;
    }


    public static List<Panier> getTransactionsRecentes(int idVente){
        String query = """
        SELECT 
            v.dateVente,
            p.idPan,
            pr.nomProd,
            pr.codeBarreProd,
            ldv.qteProdVente,
            ldv.prixUniteVente
        FROM 
            Panier p
        INNER JOIN 
            Vente v ON p.idVente = v.idVente
        INNER JOIN 
            lignedevente ldv ON p.idPan = ldv.idPan
        INNER JOIN
            produit pr ON ldv.idProd = pr.idProd
        WHERE p.idVente = ?
        ORDER BY p.idPan DESC
        LIMIT 20
    """;


        List<Panier> paniers = new ArrayList<>();
        Map<Integer, Panier> paniersMap = new HashMap<>();


        try (Connection conn = DbConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setInt(1, idVente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idPan = rs.getInt("idPan");

                    // Vérifier si le panier est déjà en mémoire
                    Panier panier = paniersMap.get(idPan);
                    if (panier == null) {
                        Vente vente = new Vente();
                        vente.setDateVente(rs.getDate("dateVente").toLocalDate());

                        panier = new Panier();
                        panier.setIdPanier(idPan);
                        panier.setVente(vente);
                        panier.setLignesDeVentes(new ArrayList<>());

                        paniers.add(panier);
                        paniersMap.put(idPan, panier);
                    }

                    // Ajout des lignes de vente
                    Produit produit = new Produit();
                    produit.setNomProd(rs.getString("nomProd"));
                    produit.setCodeBarProd(rs.getString("codeBarreProd"));

                    LigneDeVente ldv = new LigneDeVente();
                    ldv.setProduit(produit);
                    ldv.setQuantiteProd(rs.getInt("qteProdVente"));
                    ldv.setPrixUniteProd(rs.getDouble("prixUniteVente"));

                    panier.getLignesDeVentes().add(ldv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paniers;
    }

}


