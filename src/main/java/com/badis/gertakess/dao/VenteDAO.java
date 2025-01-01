package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Vente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO {

    public static void insertVente(Vente vente){
        String query = "INSERT INTO Vente(idEmpl,dateVente) " +
                "VALUES (?,?)";

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,vente.getEmploye().getIdEmpl());
            statement.setDate(2,Date.valueOf(LocalDate.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Vente getLastVenteOfEmployee(int idEmploye){
        String query = "SELECT * FROM Vente" +
                        " WHERE idVente = (" +
                        " SELECT MAX(idVente) FROM Vente WHERE idEmpl = ? )";
        Vente vente = null;
        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,idEmploye);

            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                vente = new Vente();
                Employee employee = new Employee();
                employee.setIdEmpl(idEmploye);
                vente.setIdVente(rs.getInt("idVente"));
                vente.setDateVente(rs.getDate("dateVente").toLocalDate());
                vente.setEmploye(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vente;
    }

    public static double getTotalGainsDuJour() {
        String query = """
            SELECT
                SUM(LigneDeVente.qteProdVente * LigneDeVente.prixUniteVente) AS totalGainsJour
            FROM
                Panier
            INNER JOIN
                Vente ON Panier.idVente = Vente.idVente
            INNER JOIN
                LigneDeVente ON Panier.idPan = LigneDeVente.idPan
            WHERE
                DATE(Vente.dateVente) = CURDATE();
        """;
        double total = 0;
        try (Connection conn = DbConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("totalGainsJour");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public static double getTotalGains(LocalDate date, boolean jour, boolean sem, boolean mois, boolean annee) {
        String query = """
        SELECT 
            SUM(LigneDeVente.qteProdVente * LigneDeVente.prixUniteVente) AS totalGains
        FROM 
            Panier
        INNER JOIN 
            Vente v ON Panier.idVente = v.idVente
        INNER JOIN 
            LigneDeVente ON Panier.idPan = LigneDeVente.idPan
    """;

        List<String> filtres = new ArrayList<>();
        List<Object> valeurs = new ArrayList<>();
        double total = 0;

        if (date == null) {
            if (jour) {
                filtres.add("DATE(v.dateVente) = CURDATE()");
            } else if (sem) {
                filtres.add("YEARWEEK(v.dateVente, 0) = YEARWEEK(CURDATE(), 0)");
            } else if (mois) {
                filtres.add("MONTH(v.dateVente) = MONTH(CURDATE()) AND YEAR(v.dateVente) = YEAR(CURDATE())");
            } else if (annee) {
                filtres.add("YEAR(v.dateVente) = YEAR(CURDATE())");
            }
        } else {
            if (jour) {
                filtres.add("DATE(v.dateVente) = ?");
                valeurs.add(date);
            } else if (sem) {
                filtres.add("YEARWEEK(v.dateVente, 0) = ?");
                valeurs.add(getYearWeek(date)); // Calculé en Java
            } else if (mois) {
                filtres.add("MONTH(v.dateVente) = ? AND YEAR(v.dateVente) = ?");
                valeurs.add(date.getMonthValue());
                valeurs.add(date.getYear());
            } else if (annee) {
                filtres.add("YEAR(v.dateVente) = ?");
                valeurs.add(date.getYear());
            }
        }

        if (!filtres.isEmpty()) {
            query += " WHERE " + String.join(" AND ", filtres);
        }

        try (Connection conn = DbConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < valeurs.size(); i++) {
                if (valeurs.get(i) instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) valeurs.get(i));
                } else if (valeurs.get(i) instanceof String) {
                    stmt.setString(i + 1, (String) valeurs.get(i));
                } else if (valeurs.get(i) instanceof LocalDate) {
                    stmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) valeurs.get(i)));
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("totalGains");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    private static int getYearWeek(LocalDate date) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("YYYYww");
        return Integer.parseInt(date.format(formatter));
    }


}
