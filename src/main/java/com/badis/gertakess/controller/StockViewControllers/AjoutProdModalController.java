package com.badis.gertakess.controller.StockViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.controller.CaisseViewControllers.CaisseController;
import com.badis.gertakess.dao.ProduitDAO;
import com.badis.gertakess.model.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class AjoutProdModalController {

    @FXML
    public TextField nomProduitField;
    @FXML
    public TextField codeBarreField;
    @FXML
    public ComboBox<String> categorieComboBox;
    @FXML
    public TextField quantiteStockField;
    public TextField prixUnitField;


    @FXML
    public void initialize(){
        categorieComboBox.getItems().addAll(Utils.categories);
        categorieComboBox.setValue(Utils.categories.get(0));
    }

    @FXML
    public void cancelAddProd() {
        ModalLoader.closeModal();
    }

    @FXML
    public void ajouterProduit() {

        String nom = nomProduitField.getText().isEmpty() ? null : nomProduitField.getText();
        String categorie = categorieComboBox.getValue().isEmpty() ? null : categorieComboBox.getValue() ;
        String codeBarre = codeBarreField.getText().isEmpty() ?  null : codeBarreField.getText();
        String quantite = quantiteStockField.getText().isEmpty() ? null : quantiteStockField.getText();
        String prixUnit = prixUnitField.getText().isEmpty() ? null : prixUnitField.getText();

        boolean verifier = StockController.verifierProd(nom,codeBarre,quantite,prixUnit,categorie);

        if(verifier){
            int qte = Integer.parseInt(quantite);
            double prix = Double.parseDouble(prixUnit);
            Produit produit = new Produit(nom,codeBarre,categorie,qte,prix,false);

            ProduitDAO.insertProduit(produit);
            cancelAddProd();
            StockController.products.clear();
            StockController.products.addAll(ProduitDAO.getAllProduits());
        }

    }
}
