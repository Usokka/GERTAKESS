package com.badis.gertakess.controller.StockViewControllers;

import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.Utils;
import com.badis.gertakess.dao.PointageDAO;
import com.badis.gertakess.dao.ProduitDAO;
import com.badis.gertakess.model.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ModifierProduitController {
    public TextField nomProduitField;
    public TextField codeBarreField;
    public ComboBox<String> categorieComboBox;
    public TextField quantiteStockField;
    public TextField prixUnitField;


    @FXML
    public void initialize(){
        categorieComboBox.getItems().addAll(Utils.categories);
        nomProduitField.setText(StockController.produitSelectionne.getNomProd());
        codeBarreField.setText(StockController.produitSelectionne.getCodeBarProd() == null ? "" : StockController.produitSelectionne.getCodeBarProd());
        categorieComboBox.setValue(StockController.produitSelectionne.getCategorieProd());
        quantiteStockField.setText(Integer.toString(StockController.produitSelectionne.getQuantiteStock()));
        prixUnitField.setText(Double.toString(StockController.produitSelectionne.getPrixUniteProd()));
    }

    @FXML
    public void cancelModifierProduit() {
        ModalLoader.closeModal();
    }

    @FXML
    public void modifierProduit() {
        int p = StockController.produitSelectionne.getIdProd();
        String nom = nomProduitField.getText().isEmpty() ? null: nomProduitField.getText();
        String codeBar = codeBarreField.getText().isEmpty() ? null : codeBarreField.getText();
        String categorie = categorieComboBox.getValue().isEmpty() ? null: categorieComboBox.getValue();
        String prixUnit = prixUnitField.getText().isEmpty() ? null: prixUnitField.getText();
        String quantite = quantiteStockField.getText().isEmpty() ? null: quantiteStockField.getText();

        boolean verifier = StockController.verifierProd(nom,codeBar,quantite,prixUnit,categorie);

        if(verifier){
            int qte = Integer.parseInt(quantite);
            double prix = Double.parseDouble(prixUnit);
            Produit produit = new Produit(p,nom,codeBar,categorie,qte,prix,false);
            ProduitDAO.modifierProduit(produit);
            cancelModifierProduit();
            StockController.products.clear();
            StockController.products.addAll(ProduitDAO.getAllProduits());
        }

    }
}
