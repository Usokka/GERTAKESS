package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Contrat;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Pointage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratDAO {

    public static boolean insererContrat(Contrat contrat) {
        String query = "INSERT INTO contrat(typeContr,dateDebContr,dateFinContr,posteContr,salaireContr,idEmpl) VALUES (?,?,?,?,?,?)";

        try {
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, contrat.getTypeContr());
            statement.setDate(2, Date.valueOf(contrat.getDateDebContr()));
            statement.setDate(3, contrat.getDateFinContr() != null ? Date.valueOf(contrat.getDateFinContr()) : null);
            statement.setString(4, contrat.getPosteContr());
            statement.setDouble(5, contrat.getSalaireJourContr());
            statement.setInt(6, contrat.getEmployeContr().getIdEmpl());

            statement.executeUpdate();
            return true;
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Contrat> getAllContratsByFiltres(String nom,String typeContr){
        String query = "SELECT c.*, e.nomEmpl, e.prenomEmpl FROM contrat c " +
                        "JOIN employe e ON e.idEmpl = c.idEmpl"   ;

        List<Contrat> contrats = new ArrayList<>();
        List<String> filtres = new ArrayList<>();
        List<String> valeurs = new ArrayList<>();

        if(nom != null){
            filtres.add("(e.nomEmpl LIKE ? OR e.prenomEmpl LIKE ?)");
            valeurs.add("%" + nom + "%");
            valeurs.add("%" + nom + "%");
        }

        if(typeContr != null){
            filtres.add("typeContr = ?");
            valeurs.add(typeContr);
        }

        if (!filtres.isEmpty()){
            query += " WHERE " + String.join(" AND ", filtres);
        }

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);

            for (int i = 0; i < valeurs.size(); i++) {
                stmt.setString(i + 1, valeurs.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Employee employe = new Employee();
                employe.setIdEmpl(rs.getInt("idEmpl"));
                employe.setNomEmpl(rs.getString("nomEmpl"));
                employe.setPrenomEmpl(rs.getString("prenomEmpl"));

                Contrat contrat = new Contrat();
                contrat.setIdContr(rs.getInt("idContr"));
                contrat.setPosteContr(rs.getString("posteContr"));
                contrat.setDateDebContr(rs.getDate("dateDebContr") == null ? null : rs.getDate("dateDebContr").toLocalDate());
                contrat.setDateFinContr(rs.getDate("dateFinContr") == null ? null : rs.getDate("dateFinContr").toLocalDate());
                contrat.setDateQuitEmploi(rs.getDate("dateQuitPoste") == null ? null : rs.getDate("dateQuitPoste").toLocalDate());
                contrat.setSalaireJourContr(rs.getDouble("salaireContr"));
                contrat.setEmployeContr(employe);

                contrats.add(contrat);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return contrats;
    }

    public static boolean finirContrat(int idContrat){
        String query = " UPDATE contrat SET dateQuitPoste = ? where idContr = ?";
        try {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, idContrat);
            int rs = stmt.executeUpdate();
            return rs > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }


}

