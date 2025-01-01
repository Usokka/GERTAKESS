package com.badis.gertakess.dao;


import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.Commande;
import com.badis.gertakess.model.Fournisseur;
import com.badis.gertakess.model.Produit;
import com.badis.gertakess.model.ProduitCommande;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandeDAO {

    public static boolean modifierCmd(Commande commande){
        String query = "UPDATE Commande SET datePrevueCmd = ? , idFourn = ? WHERE idCmd = ?";

        try {
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setDate(1, Date.valueOf(commande.getDatePrevCmd()));
            statement.setInt(2, commande.getFournisseur().getIdFourn());
            statement.setInt(3,commande.getIdCmd());
            int rs =statement.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void insertCommande(Commande commande) {
        String query = "INSERT INTO commande(datePrevueCmd,idFourn,dateCmd,statutCmd) VALUES (?,?,?,?)";

        try {
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setDate(1, Date.valueOf(commande.getDatePrevCmd()));
            statement.setInt(2, commande.getFournisseur().getIdFourn());
            statement.setDate(3, Date.valueOf(LocalDate.now()));
            statement.setBoolean(4, false);

            statement.executeUpdate();
            System.out.println("ajout succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validerCmd(int idCmd) {

        String query = "UPDATE Commande SET statutCmd = ? , dateLivrCmd = ? WHERE idCmd = ?";
        try{
            Connection con = DbConnector.connect();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setBoolean(1, true);
            statement.setDate(2,Date.valueOf(LocalDate.now()));
            statement.setInt(3,idCmd);

            int rs = statement.executeUpdate();

            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
    public static int getLastIdCommande() {
        String query = "SELECT MAX(idCmd) FROM commande";
        int derId = 0;
        try {
            Connection con = DbConnector.connect();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                derId = rs.getInt("MAX(idCmd)");
            }

            System.out.println("ajout succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return derId;
    }

    public static List<Commande> getAllCommandesWithProductsByFilters(
            String nomFournisseur, LocalDate dateCommande, LocalDate datePrevue, String statut) {

        String query = "SELECT c.idCmd,f.idFourn, f.nomFourn, c.dateCmd, c.datePrevueCmd,c.dateLivrCmd, c.statutCmd, " +
                "p.idProd,p.codeBarreProd, p.nomProd, pc.qteProdCmd, pc.prixUniteCmd " +
                "FROM commande c " +
                "JOIN fournisseur f ON c.idFourn = f.idFourn " +
                "JOIN produitcommande pc ON pc.idCmd = c.idCmd " +
                "JOIN produit p ON pc.idProd = p.idProd";

        List<Commande> commandes = new ArrayList<>();
        Map<Integer, Commande> commandesMap = new HashMap<>();
        List<String> filtres = new ArrayList<>();
        List<String> valeurs = new ArrayList<>();

        if (nomFournisseur != null) {
            filtres.add("f.nomFourn LIKE ?");
            valeurs.add("%" + nomFournisseur + "%");
        }
        if (dateCommande != null) {
            filtres.add("DATE(c.dateCmd) = ?");
            valeurs.add(dateCommande.toString());
        }
        if (datePrevue != null) {
            filtres.add("DATE(c.datePrevueCmd) = ?");
            valeurs.add(datePrevue.toString());
        }
        if (statut != null) {
            filtres.add("c.statutCmd = ?");
            valeurs.add(statut);
        }
        if (!filtres.isEmpty()) {
            query += " WHERE " + String.join(" AND ", filtres);
        }

        try (Connection con = DbConnector.connect();
             PreparedStatement statement = con.prepareStatement(query)) {

            for (int i = 0; i < valeurs.size(); i++) {
                statement.setString(i + 1, valeurs.get(i));
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {

                    int idCmd = rs.getInt("idCmd");

                    Commande commande = commandesMap.get(idCmd);
                    if (commande == null) {
                        Fournisseur fournisseur = new Fournisseur();
                        fournisseur.setNomFourn(rs.getString("nomFourn"));
                        fournisseur.setIdFourn(rs.getInt("idFourn"));

                        commande = new Commande();
                        commande.setIdCmd(idCmd);
                        commande.setFournisseur(fournisseur);
                        commande.setDateCmd(rs.getDate("dateCmd") != null ?
                                rs.getDate("dateCmd").toLocalDate() : null);
                        commande.setDatePrevCmd(rs.getDate("datePrevueCmd") != null ?
                                rs.getDate("datePrevueCmd").toLocalDate() : null);
                        commande.setDateLivrCmd(rs.getDate("dateLivrCmd") != null ?
                                rs.getDate("dateLivrCmd").toLocalDate() : null);
                        commande.setStatutCmd(rs.getBoolean("statutCmd"));

                        commande.setProduitsCommandes(new ArrayList<>());
                        commandes.add(commande);
                        commandesMap.put(idCmd, commande);
                    }

                    Produit produit = new Produit();
                    produit.setIdProd(rs.getInt("idProd"));
                    produit.setNomProd(rs.getString("nomProd"));
                    produit.setCodeBarProd(rs.getString("codeBarreProd"));

                    ProduitCommande produitCommande = new ProduitCommande();
                    produitCommande.setProduit(produit);
                    produitCommande.setQuantiteProd(rs.getInt("qteProdCmd"));
                    produitCommande.setPrixUniteProd(rs.getDouble("prixUniteCmd"));

                    commande.getProduitsCommandes().add(produitCommande);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    public static Commande getCmdById(int idCmd) {
        String query = "SELECT c.idCmd, p.idProd, pc.qteProdCmd, pc.prixUniteCmd " +
                        "FROM commande c " +
                        "JOIN produitcommande pc ON pc.idCmd = c.idCmd " +
                        "JOIN produit p ON pc.idProd = p.idProd " +
                        "WHERE c.idCmd = ?";

        Commande commande = null;

        try (Connection con = DbConnector.connect();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, idCmd);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {

                    commande = new Commande();
                    commande.setIdCmd(rs.getInt("idCmd"));
                    commande.setProduitsCommandes(new ArrayList<>());

                    do {
                        Produit produit = new Produit();
                        produit.setIdProd(rs.getInt("idProd"));
                        ProduitCommande produitCommande = new ProduitCommande();
                        produitCommande.setProduit(produit);
                        produitCommande.setQuantiteProd(rs.getInt("qteProdCmd"));
                        produitCommande.setPrixUniteProd(rs.getDouble("prixUniteCmd"));
                        commande.getProduitsCommandes().add(produitCommande);
                    } while (rs.next());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commande;
    }

    public static boolean supprimerCmd(int idCmd) {

        String query = "DELETE from commande WHERE idCmd = ?";
        try {
            Connection con = DbConnector.connect();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, idCmd);
            int rs = stmt.executeUpdate();

            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
