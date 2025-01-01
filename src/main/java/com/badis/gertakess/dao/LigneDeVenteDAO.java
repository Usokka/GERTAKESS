package com.badis.gertakess.dao;

import com.badis.gertakess.DbConnector;
import com.badis.gertakess.model.LigneDeVente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LigneDeVenteDAO {

    public static void insertLigneDeVente(LigneDeVente ldv){
        String query = "INSERT INTO lignedevente(idPan,idProd,qteProdVente,prixUniteVente) VALUES (?,?,?,?)";

        try{
            Connection con = DbConnector.connect();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,ldv.getPanier().getIdPanier());
            statement.setInt(2,ldv.getProduit().getIdProd());
            statement.setInt(3,ldv.getQuantiteProd());
            statement.setDouble(4, ldv.getPrixUniteProd());

            statement.executeUpdate();
            System.out.println("ajout succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
