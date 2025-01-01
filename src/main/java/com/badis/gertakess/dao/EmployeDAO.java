package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Contrat;
import com.badis.gertakess.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeDAO {


    public static boolean insertEmploye(Employee employe){
        String query = "INSERT INTO employe(nomEmpl,prenomEmpl,sexeEmpl,dateNaissEmpl," +
                        "adresseEmpl,situationEmpl,emailEmpl,numTelEmpl,mdpCptEmpl," +
                        "permissionCptEmpl,dateCptEmpl,etatEmpl) " +
                         "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,employe.getNomEmpl());
            statement.setString(2,employe.getPrenomEmpl());
            statement.setString(3,employe.getSexeEmpl());
            statement.setDate(4, Date.valueOf(employe.getDateNaissEmpl()));
            statement.setString(5, employe.getAdresseEmpl());
            statement.setString(6,employe.getSituationEmpl());
            statement.setString(7,employe.getEmailEmpl());
            statement.setString(8,employe.getNumTelEmpl());
            statement.setString(9,employe.getMdpCptEmpl());
            statement.setString(10,employe.getPermCptEmpl());
            statement.setDate(11, Date.valueOf(employe.getDateCptEmpl()));
            statement.setBoolean(12,true);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean modifierEmploye(Employee employe){
        String query = "UPDATE employe SET nomEmpl = ?, prenomEmpl = ?, sexeEmpl = ?," +
                " dateNaissEmpl = ?, adresseEmpl = ?, SituationEmpl = ?, emailEmpl = ?," +
                " numTelEmpl = ?, mdpCptEmpl = ?, permissionCptEmpl = ?, etatEmpl = ? WHERE idEmpl = ?";

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,employe.getNomEmpl());
            statement.setString(2,employe.getPrenomEmpl());
            statement.setString(3,employe.getSexeEmpl());
            statement.setDate(4, Date.valueOf(employe.getDateNaissEmpl()));
            statement.setString(5, employe.getAdresseEmpl());
            statement.setString(6,employe.getSituationEmpl());
            statement.setString(7,employe.getEmailEmpl());
            statement.setString(8,employe.getNumTelEmpl());
            statement.setString(9,employe.getMdpCptEmpl());
            statement.setString(10,employe.getPermCptEmpl());
            statement.setBoolean(11,employe.isEtatCptEmpl());
            statement.setInt(12,employe.getIdEmpl());

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<Employee> getAllEmployesByFilters(String nom, String adresse, String sexe, String poste, String statut, String typeContrat) {
        String query = "SELECT e.*, c.* FROM employe e " +
                "LEFT JOIN contrat c ON e.idEmpl = c.idEmpl";

        List<Employee> employes = new ArrayList<>();
        Map<Integer, Employee> employeMap = new HashMap<>();

        try (Connection con = DbConnector.connect()) {
            List<String> filtres = new ArrayList<>();
            List<String> valeurs = new ArrayList<>();

            if (nom != null) {
                filtres.add("CONCAT(e.nomEmpl,' ',e.prenomEmpl) LIKE ?");
                valeurs.add("%" + nom + "%");
            }
            if (adresse != null) {
                filtres.add("e.adresseEmpl LIKE ?");
                valeurs.add("%" + adresse + "%");
            }
            if (sexe != null) {
                filtres.add("e.sexeEmpl = ?");
                valeurs.add(sexe);
            }
            if (poste != null) {
                filtres.add("c.posteContr = ?");
                valeurs.add(poste);
            }
            if (typeContrat != null) {
                filtres.add("c.typeContr = ?");
                valeurs.add(typeContrat);
            }
            if (statut != null) {
                filtres.add("e.etatEmpl = ?");
            }

            query += !filtres.isEmpty() ? " WHERE " + String.join(" AND ", filtres) : "";

            assert con != null;
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                int i = 0;
                for (; i < valeurs.size(); i++) {
                    stmt.setString(i + 1, valeurs.get(i));
                }
                if (statut != null) {
                    stmt.setBoolean(i + 1, statut.equals("ACTIF"));
                }
                System.out.println(stmt.toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int idEmpl = rs.getInt("idEmpl");

                        // Vérifier si l'employé existe déjà dans la map.
                        Employee employe = employeMap.get(idEmpl);
                        if (employe == null) {
                            employe = new Employee();
                            employe.setIdEmpl(idEmpl);
                            employe.setDateNaissEmpl(rs.getDate("dateNaissEmpl").toLocalDate());
                            employe.setDateCptEmpl(rs.getDate("dateCptEmpl").toLocalDate());
                            employe.setNomEmpl(rs.getString("nomEmpl"));
                            employe.setPrenomEmpl(rs.getString("prenomEmpl"));
                            employe.setEmailEmpl(rs.getString("emailEmpl"));
                            employe.setNumTelEmpl(rs.getString("numTelEmpl"));
                            employe.setAdresseEmpl(rs.getString("adresseEmpl"));
                            employe.setPermCptEmpl(rs.getString("permissionCptEmpl"));
                            employe.setEtatCptEmpl(rs.getBoolean("etatEmpl"));
                            employe.setMdpCptEmpl(rs.getString("mdpCptEmpl"));
                            employe.setSexeEmpl(rs.getString("sexeEmpl"));
                            employe.setSituationEmpl(rs.getString("situationEmpl"));
                            employe.setContratsEmpl(new ArrayList<>()); // Initialiser la liste des contrats

                            employeMap.put(idEmpl, employe);
                        }

                        if (rs.getInt("idContr") != 0) { // Vérifier si un contrat existe
                            Contrat contrat = new Contrat();
                            contrat.setIdContr(rs.getInt("idContr"));
                            contrat.setTypeContr(rs.getString("typeContr"));
                            contrat.setDateDebContr(rs.getDate("dateDebContr") != null ?
                                    rs.getDate("dateDebContr") .toLocalDate() : null);
                            contrat.setDateFinContr(rs.getDate("dateFinContr") != null ?
                                    rs.getDate("dateFinContr").toLocalDate() : null);
                            contrat.setDateQuitEmploi(rs.getDate("dateQuitPoste")!= null ?
                                    rs.getDate("dateQuitPoste").toLocalDate() : null);
                            contrat.setPosteContr(rs.getString("posteContr"));
                            contrat.setSalaireJourContr(rs.getDouble("salaireContr"));
                            employe.getContratsEmpl().add(contrat);
                        }
                    }
                }
            }
            employes.addAll(employeMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employes;
    }


    public static int getLasIdAdded() {
        String query = "SELECT max(idEmpl) from employe";
        int result = 0;
        try {
            Connection con = DbConnector.connect();
            assert con != null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                result = rs.getInt("max(idEmpl)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Employee getUser(String user,String password){

        String query = "SELECT idEmpl,nomEmpl,prenomEmpl,permissionCptEmpl" +
                        " FROM employe WHERE ( emailEmpl = ? OR numTelEmpl = ? ) AND mdpCptEmpl = ? AND etatEmpl = ?";

        Employee employe = null;
        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,user);
            stmt.setString(2,user);
            stmt.setString(3,password);
            stmt.setBoolean(4,true);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                employe= new Employee();
                employe.setIdEmpl(rs.getInt("idEmpl"));
                employe.setNomEmpl(rs.getString("nomEmpl"));
                employe.setPrenomEmpl(rs.getString("prenomEmpl"));
                employe.setPermCptEmpl(rs.getString("permissionCptEmpl"));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return employe;
    }

    public static boolean supprimerEmploye(int idEmpl){
        String query = " UPDATE employe SET etatEmpl = ? where idEmpl = ?";
        try {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setInt(2, idEmpl);
            int rs = stmt.executeUpdate();
            return rs > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
