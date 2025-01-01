package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Commande;
import com.badis.gertakess.model.Produit;
import com.badis.gertakess.model.ProduitCommande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitCommandeDAO {

    public static List<ProduitCommande> getAllProduitCommande(){
        String query = "SELECT * FROM produitcommande";
        List<ProduitCommande> produitCommandes = new ArrayList<>();

        try{
            Connection con = DbConnector.connect();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()){
                Commande commande = new Commande();
                commande.setIdCmd(rs.getInt("idCmd"));
                Produit produit = new Produit();
                produit.setIdProd(rs.getInt("idProd"));

                ProduitCommande produitCommande = new ProduitCommande();
                produitCommande.setCommande(commande);
                produitCommande.setProduit(produit);
                produitCommande.setPrixUniteProd(rs.getDouble("prixUniteCmd"));
                produitCommande.setQuantiteProd(rs.getInt("qteProdCmd"));
                produitCommandes.add(produitCommande);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produitCommandes;
    }

    public static void insertProduitCommande(ProduitCommande produitCommande) {
        String query = "INSERT INTO produitCommande(idCmd,idProd,qteProdCmd,prixUniteCmd) VALUES (?,?,?,?)";

        try {
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, (produitCommande.getCommande().getIdCmd()));
            statement.setInt(2, produitCommande.getProduit().getIdProd());
            statement.setInt(3, produitCommande.getQuantiteProd());
            statement.setDouble(4, produitCommande.getPrixUniteProd());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteProduitCommande(int idCmd){

        String query = "DELETE FROM produitCommande where idCmd = ?";

        try{
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, idCmd);
            int rs =statement.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
