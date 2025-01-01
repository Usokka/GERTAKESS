package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Fournisseur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAO {


    public static List<Fournisseur> getAllFournisseur(){
        return  getAllFournisseurByFilters(null,null,null,false);
    }

    public static List<Fournisseur> getAllFournisseurByFilters(String nom, String adresse,List<String> categories, boolean supprime) {
        String query = "SELECT * FROM fournisseur";
        List<Fournisseur> fournisseurs = new ArrayList<>();

        try (Connection con = DbConnector.connect()) {
            if (con == null) {
                throw new SQLException("Connection failed");
            }

            List<String> filtres = new ArrayList<>();
            List<Object> valeurs = new ArrayList<>();

            if (nom != null && !nom.trim().isEmpty()) {
                filtres.add("nomFourn LIKE ?");
                valeurs.add("%" + nom.trim() + "%");
            }
            if (adresse != null && !adresse.trim().isEmpty()) {
                filtres.add("adresseFourn LIKE ?");
                valeurs.add("%" + adresse.trim() + "%");
            }

            if (categories != null && !categories.isEmpty()) {
                List<String> categoryConditions = new ArrayList<>();
                for (String category : categories) {
                    categoryConditions.add("categoriesFourn LIKE ?");
                    valeurs.add("%" + category.trim() + "%");
                }
                filtres.add("(" + String.join(" OR ", categoryConditions) + ")");
            }

            query += !filtres.isEmpty() ? " WHERE " + String.join(" AND ", filtres) : " WHERE ";
            query += !filtres.isEmpty() ? "AND supprime = ?" : "supprime = ?";

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                int i = 0;
                for (; i < valeurs.size(); i++) {
                    stmt.setObject(i + 1, valeurs.get(i));
                }
                stmt.setBoolean(i + 1, supprime);
                System.out.println(query);
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Fournisseur fournisseur = new Fournisseur();
                    fournisseur.setIdFourn(rs.getInt("idFourn"));
                    fournisseur.setNomFourn(rs.getString("nomFourn"));
                    fournisseur.setEmailFourn(rs.getString("emailFourn"));
                    fournisseur.setAdresseFourn(rs.getString("adresseFourn"));
                    fournisseur.setNumTelFourn(rs.getString("numTelFourn"));
                    fournisseur.setCategoriesFourn(rs.getString("categoriesFourn"));
                    fournisseur.setSupprimeFourn(rs.getBoolean("supprime"));

                    fournisseurs.add(fournisseur);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fournisseurs;
    }


    public static boolean insertFournisseur(Fournisseur fourn){

        List<Fournisseur> fournisseurs = getAllFournisseurByFilters(null,null,null,true);
        Fournisseur ff = fournisseurs.stream().filter(f -> f.getNomFourn().equals(fourn.getNomFourn())).findFirst().orElse(null);
        if(ff != null){
            ff.setAdresseFourn(fourn.getAdresseFourn());
            ff.setEmailFourn(fourn.getEmailFourn());
            ff.setNumTelFourn(fourn.getNumTelFourn());
            ff.setCategoriesFourn(fourn.getCategoriesFourn());
            ff.setSupprimeFourn(false);
            return updateFournisseur(ff);
        }

        String query = "INSERT INTO fournisseur(nomFourn,emailFourn,numTelFourn,adresseFourn,categoriesFourn,supprime) VALUES (?,?,?,?,?,?)";

        try{
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,fourn.getNomFourn());
            statement.setString(2,fourn.getEmailFourn());
            statement.setString(3,fourn.getNumTelFourn());
            statement.setString(4, fourn.getAdresseFourn());
            statement.setString(5,fourn.getCategoriesFourn());
            statement.setBoolean(6,false);
            int rs= statement.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateFournisseur(Fournisseur fournisseur){

        String query = "UPDATE fournisseur SET" +
                " nomFourn = ?," +
                " adresseFourn = ?," +
                " emailFourn = ?," +
                " numTelFourn = ?," +
                " categoriesFourn = ?," +
                " supprime = ? "+
                " WHERE idFourn = ?";

        try
        {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,fournisseur.getNomFourn());
            stmt.setString(2,fournisseur.getAdresseFourn());
            stmt.setString(3,fournisseur.getEmailFourn());
            stmt.setString(4,fournisseur.getNumTelFourn());
            stmt.setString(5,fournisseur.getCategoriesFourn());
            stmt.setBoolean(6,fournisseur.isSupprimeFourn());
            stmt.setInt(7,fournisseur.getIdFourn());

            int rs = stmt.executeUpdate();

            return rs > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean supprimerFourn(int idFourn){
        String query = " UPDATE fournisseur SET supprime = ? where idFourn = ?";
        try {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setBoolean(1,true);
            stmt.setInt(2,idFourn);
            int rs = stmt.executeUpdate();
            return rs > 0;
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }

        return false;
    }


}
