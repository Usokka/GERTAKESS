package com.badis.gertakess.controller.FournisseursViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.FournisseurDAO;
import com.badis.gertakess.model.Fournisseur;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

public class ModifierFournisseurController {
    public TextField telephoneFournField;
    public TextField emailFournField;
    public TextField nomFournField;
    public TextField adresseFournField;
    public CheckComboBox<String> categoriesFournCheckComboBox;
    private Fournisseur f;

    @FXML
    public void initialize(){
        f = FournisseursController.fournisseurSelectionne;
        nomFournField.setText(f.getNomFourn());
        telephoneFournField.setText(f.getNumTelFourn());
        emailFournField.setText(f.getEmailFourn());
        adresseFournField.setText(f.getAdresseFourn());

        categoriesFournCheckComboBox.getItems().addAll(Utils.categories);

        String categories = f.getCategoriesFourn();
        categories = categories.replace("[", "").replace("]", "");
        String[] array = categories.split(",\\s*");

        for (String category : array) {
                categoriesFournCheckComboBox.getCheckModel().check(category);
            }
    }

    public void cancelModifFourn() {
        ModalLoader.closeModal();
    }

    public void modifFournisseur() {

        String nom = nomFournField.getText().isEmpty() ? null : nomFournField.getText();
        String email = emailFournField.getText().isEmpty() ? null : emailFournField.getText();
        String numTel = telephoneFournField.getText().isEmpty() ? null : telephoneFournField.getText();
        String adresse = adresseFournField.getText().isEmpty() ? null : adresseFournField.getText();
        ObservableList<String> categories = categoriesFournCheckComboBox.getCheckModel().getCheckedItems();


        boolean verifier = FournisseursController.verifierInformations(nom, email, numTel, adresse);

        if(verifier){
            String categoriesString = categories.toString();
            f.setNomFourn(nom);
            f.setEmailFourn(email);
            f.setNumTelFourn(numTel);
            f.setAdresseFourn(adresse);
            f.setCategoriesFourn(categoriesString);
            FournisseurDAO.updateFournisseur(f);
            cancelModifFourn();
            FournisseursController.fournisseurs.clear();
            FournisseursController.fournisseurs.addAll(FournisseurDAO.getAllFournisseur());
        }
    }
}
