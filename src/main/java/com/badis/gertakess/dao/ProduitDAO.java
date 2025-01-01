package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProduitDAO {

    public static List<Produit> getAllProduits(){

        return getAllProduitByFilter(null,null,false);
    }

    public static List<Produit> getAllProduitsSansCodeBarre(boolean supprime){
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit WHERE codeBarreProd IS NULL AND supprime = ?";

        try
        {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setBoolean(1,supprime);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                Produit produit = new Produit();
                produit.setIdProd(rs.getInt("idProd"));
                produit.setNomProd(rs.getString("nomProd"));
                produit.setCodeBarProd(rs.getString("codeBarreProd"));
                produit.setCategorieProd(rs.getString("categorieProd"));
                produit.setQuantiteStock(rs.getInt("qteStockProd"));
                produit.setPrixUniteProd(rs.getInt("prixUniteProd"));
                produits.add(produit);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return produits;
    }

    public static List<Produit> getAllProduitByFilter(List<String> categories, String nomProd,boolean supprime) {

        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit";
        List<String> filtres = new ArrayList<>();
        List<String> valeurs = new ArrayList<>();

        if (nomProd != null) {
            filtres.add("nomProd LIKE ?");
            valeurs.add("%" + nomProd + "%");
        }

        if (categories != null && !categories.isEmpty()) {
            List<String> orClauses = new ArrayList<>();
            for (String categorie : categories) {
                orClauses.add("categorieProd = ?");
                valeurs.add(categorie);
            }
            filtres.add("(" + String.join(" OR ", orClauses) + ")");
        }

        query += !filtres.isEmpty() ? " WHERE " + String.join(" AND ", filtres) : " WHERE ";
        query += !filtres.isEmpty() ? "AND supprime = ?" : "supprime = ?";

        try (Connection con = DbConnector.connect();
             PreparedStatement stmt = con.prepareStatement(query)) {
            int i = 0;
            for (; i < valeurs.size(); i++) {
                stmt.setString(i + 1, valeurs.get(i));
            }
            stmt.setBoolean(i + 1, supprime);


            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produit produit = new Produit();
                    produit.setIdProd(rs.getInt("idProd"));
                    produit.setNomProd(rs.getString("nomProd"));
                    produit.setCodeBarProd(rs.getString("codeBarreProd"));
                    produit.setCategorieProd(rs.getString("categorieProd"));
                    produit.setQuantiteStock(rs.getInt("qteStockProd"));
                    produit.setPrixUniteProd(rs.getDouble("prixUniteProd"));
                    produit.setSupprime(rs.getBoolean("supprime"));
                    produits.add(produit);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return produits;
    }

    public static boolean modifierProduit(Produit produit){

        String query = "UPDATE produit SET" +
                        " nomProd = ?," +
                        " qteStockProd = ?," +
                        " codeBarreProd = ?," +
                        " categorieProd = ?," +
                        " prixUniteProd = ?," +
                        " supprime = ? " +
                        " WHERE idProd = ?";

        try
        {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,produit.getNomProd());
            stmt.setInt(2,produit.getQuantiteStock());
            stmt.setString(3,produit.getCodeBarProd());
            stmt.setString(4,produit.getCategorieProd());
            stmt.setDouble(5,produit.getPrixUniteProd());
            stmt.setBoolean(6,produit.isSupprime());
            stmt.setInt(7,produit.getIdProd());

            int rs = stmt.executeUpdate();
            return rs > 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean insertProduit(Produit prod){

        String query = "INSERT INTO PRODUIT(nomProd,codeBarreProd,categorieProd,qteStockProd,prixUniteProd,supprime) VALUES (?,?,?,?,?,?)";
        List<Produit> produits = getAllProduitByFilter(null,null,true);
        Produit produit = produits.stream().filter( p-> p.getNomProd().equals(prod.getNomProd())).findFirst().orElse(null);
        if(produit != null){
            produit.setCategorieProd(prod.getCategorieProd());
            produit.setQuantiteStock(prod.getQuantiteStock());
            produit.setPrixUniteProd(prod.getPrixUniteProd());
            produit.setCodeBarProd(prod.getCodeBarProd());
            produit.setSupprime(false);
            return modifierProduit(produit);
        }

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,prod.getNomProd());
            statement.setString(2,prod.getCodeBarProd());
            statement.setString(3,prod.getCategorieProd());
            statement.setInt(4,prod.getQuantiteStock());
            statement.setDouble(5,prod.getPrixUniteProd());
            statement.setBoolean(6,false);

            int rs = statement.executeUpdate();

            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean supprimerProd(int idProd) {
        String query = " UPDATE produit SET supprime = ? where idProd = ?";
        try {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setBoolean(1, true);
            stmt.setInt(2, idProd);
            int rs = stmt.executeUpdate();
            return rs > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static Produit getProduitByCodeBar(String codeBar){
        String query = "SELECT * FROM produit WHERE codeBarreProd = ?";
        Produit produit = null;

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,codeBar);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
            {
                produit = new Produit();
                produit.setIdProd(rs.getInt("idProd"));
                produit.setNomProd(rs.getString("nomProd"));
                produit.setCodeBarProd(rs.getString("codeBarreProd"));
                produit.setCategorieProd(rs.getString("categorieProd"));
                produit.setQuantiteStock(rs.getInt("qteStockProd"));
                produit.setPrixUniteProd(rs.getInt("prixUniteProd"));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return produit;
    }

    public static boolean incrementStock(int idProduit, int quantity) {
        String query = "UPDATE produit SET qteStockProd = qteStockProd + ? WHERE idProd = ?";

        try (Connection con = DbConnector.connect()) {
            assert con != null;
            try (PreparedStatement statement = con.prepareStatement(query)) {

                 statement.setInt(1, quantity);
                 statement.setInt(2, idProduit);
                 int rowsUpdated = statement.executeUpdate();

                 return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean decrementStock(int idProd,int quantite){
        String query = "UPDATE produit " +
                "SET qteStockProd = qteStockProd - ? " +
                "WHERE idProd = ?";

        try {
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, quantite);
            stmt.setInt(2, idProd);
            int rs = stmt.executeUpdate();
            return rs > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
