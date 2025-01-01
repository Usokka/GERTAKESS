package com.badis.gertakess.controller.FournisseursViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.FournisseurDAO;
import com.badis.gertakess.model.Fournisseur;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

public class AjoutFournisseurModalController {

    @FXML
    public CheckComboBox<String> categoriesFournCheckComboBox;
    @FXML
    public TextField adresseFournField;
    @FXML
    public TextField telephoneFournField;
    @FXML
    public TextField emailFournField;
    @FXML
    public TextField nomFournField;
    @FXML
    public Button cancelAddBtn;
    @FXML
    public Button addFournBtn;

    @FXML
    public void initialize(){
        categoriesFournCheckComboBox.getItems().addAll(Utils.categories);
    }

    public void cancelAddFourn() {
        ModalLoader.closeModal();
    }

    public void addFournisseur() {

        String nom = nomFournField.getText().isEmpty() ? null : nomFournField.getText();
        String email = emailFournField.getText().isEmpty() ? null : emailFournField.getText();
        String numTel = telephoneFournField.getText().isEmpty() ? null : telephoneFournField.getText();
        String adresse = adresseFournField.getText().isEmpty() ? null : adresseFournField.getText();
        ObservableList<String> categories = categoriesFournCheckComboBox.getCheckModel().getCheckedItems();

        boolean verifier = FournisseursController.verifierInformations(nom, email, numTel, adresse);

        if(verifier){
            String categoriesString = categories.toString();

            Fournisseur fournisseur = new Fournisseur(nom,email,numTel,adresse,false,null,categoriesString);
            FournisseurDAO.insertFournisseur(fournisseur);
            ModalLoader.closeModal();
            FournisseursController.fournisseurs.clear();
            FournisseursController.fournisseurs.addAll(FournisseurDAO.getAllFournisseur());
        }


    }
}
