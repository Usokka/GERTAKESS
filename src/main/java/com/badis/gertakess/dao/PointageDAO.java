package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Employee;

import com.badis.gertakess.model.Pointage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PointageDAO {

    public static void insertPointage(Pointage pointage){
        String query = "INSERT INTO pointage(datePoint,heureEntreePoint,heureSortiePoint,idEmpl) VALUES (?,?,?,?)";

        try{
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setDate(1,Date.valueOf(pointage.getDatePoint()));
            statement.setTime(2, Time.valueOf(pointage.getHeureEntreePoint()));
            statement.setTime(3, Time.valueOf(pointage.getHeureSortiePoint() == null ? LocalTime.of(0,0) : pointage.getHeureSortiePoint()));
            statement.setInt(4,pointage.getEmploye().getIdEmpl());

            statement.executeUpdate();
            System.out.println("ajout succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modifierPointage(Pointage pointage){
        String query = "UPDATE pointage SET datePoint = ? ,heureEntreePoint = ? , heureSortiePoint = ? ,idEmpl = ? WHERE idPoint = ? ";

        try{
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setDate(1,Date.valueOf(pointage.getDatePoint()));
            statement.setTime(2, Time.valueOf(pointage.getHeureEntreePoint()));
            statement.setTime(3, Time.valueOf(pointage.getHeureSortiePoint()));
            statement.setInt(4,pointage.getEmploye().getIdEmpl());
            statement.setInt(5,pointage.getIdPoint());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean supprimerPointage(int idPoint){
        String query = "DELETE FROM pointage WHERE idPoint = ?";
        try {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, idPoint);
            int rs = stmt.executeUpdate();
            return rs > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static List<Pointage> getAllPointagesByFiltres(String nom, LocalDate date, String statut){
        String query = "SELECT p.idPoint, e.idEmpl,e.nomEmpl,e.prenomEmpl," +
                        "p.datePoint,p.heureEntreePoint,p.heureSortiePoint" +
                        " FROM employe e JOIN pointage p ON e.idEmpl = p.idEmpl";

        List<Pointage> pointages = new ArrayList<>();
        List<String> filtres = new ArrayList<>();
        List<String> valeurs = new ArrayList<>();

        if(nom != null){
            filtres.add("(e.nomEmpl LIKE ? OR e.prenomEmpl LIKE ?)");
            valeurs.add("%" + nom + "%");
            valeurs.add("%" + nom + "%");
        }

        if(date != null){
            filtres.add("DATE(p.datePoint) = ?");
            valeurs.add(date.toString());
        }

        if (statut != null){
            if (statut.equals("Absent")){
                filtres.add("(p.heureEntreePoint = ? AND p.heureSortiePoint = ?)");
            }
            else if (statut.equals("Présent")){
                filtres.add("(p.heureEntreePoint != ? AND p.heureSortiePoint != ?)");
            }
            valeurs.add("00:00:00");
            valeurs.add("00:00:00");
        }

        if (!filtres.isEmpty()){
            query += " WHERE " + String.join(" AND ", filtres);
        }

        try{
            Connection con = DbConnector.connect();
            PreparedStatement stmt = con.prepareStatement(query);

            for (int i = 0; i < valeurs.size(); i++) {
                stmt.setString(i + 1, valeurs.get(i));
            }
            System.out.println(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Pointage pointage = new Pointage();
                Employee employe = new Employee();
                employe.setIdEmpl(rs.getInt("idEmpl"));
                employe.setNomEmpl(rs.getString("nomEmpl"));
                employe.setPrenomEmpl(rs.getString("prenomEmpl"));

                pointage.setIdPoint(rs.getInt("idPoint"));
                pointage.setEmploye(employe);
                pointage.setHeureEntreePoint(rs.getTime("heureEntreePoint").toLocalTime());
                pointage.setHeureSortiePoint(rs.getTime("heureSortiePoint").toLocalTime());
                pointage.setDatePoint(rs.getDate("datePoint").toLocalDate());

                pointages.add(pointage);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return pointages;
    }

    public static Pointage getLastPointageOf(int idEmpl){
        String query = "SELECT  idPoint,datePoint, heureEntreePoint, heureSortiePoint " +
                        "FROM pointage " +
                        "WHERE idPoint = ( " +
                        "    SELECT MAX(idPoint) " +
                        "    FROM pointage " +
                        "    WHERE idEmpl = ? " +
                        ") " +
                        "AND idEmpl = ?";

        Pointage pointage = null;
        try {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, idEmpl);
            statement.setInt(2, idEmpl);
            Employee employe = new Employee();
            employe.setIdEmpl(idEmpl);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                pointage = new Pointage(rs.getInt("idPoint"),rs.getDate("datePoint").toLocalDate(),rs.getTime("heureEntreePoint").toLocalTime(),rs.getTime("heureSortiePoint").toLocalTime(),employe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pointage;
    }

}
