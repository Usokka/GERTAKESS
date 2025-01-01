package com.badis.gertakess.controller.CommandesViewControllers;

import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.CommandeDAO;
import com.badis.gertakess.dao.FournisseurDAO;
import com.badis.gertakess.dao.ProduitCommandeDAO;
import com.badis.gertakess.dao.ProduitDAO;
import com.badis.gertakess.model.Commande;
import com.badis.gertakess.model.Fournisseur;
import com.badis.gertakess.model.Produit;
import com.badis.gertakess.model.ProduitCommande;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class ModifierCommandeController {

    @FXML
    private TableColumn<ProduitCommande, Double> prixUnitaireColumn;
    @FXML
    private TableColumn<ProduitCommande, Double> totalColumn;
    @FXML
    private ComboBox<Produit> produitComboBox;
    @FXML
    private TextField quantiteField;
    @FXML
    private TextField prixUnitaireField;
    @FXML
    private ComboBox<Fournisseur> fournisseurComboBox;
    @FXML
    private DatePicker datePrevuePicker;
    @FXML
    private TableView<ProduitCommande> produitsTable;
    @FXML
    private TableColumn<ProduitCommande, String> produitColumn;
    @FXML
    private TableColumn<ProduitCommande, Integer> quantiteColumn;
    private ObservableList<ProduitCommande> produitsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        produitColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduit().getNomProd()));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteProd"));
        prixUnitaireColumn.setCellValueFactory(new PropertyValueFactory<>("prixUniteProd"));
        totalColumn.setCellValueFactory(cellData -> {
            ProduitCommande pc = cellData.getValue();
            return new SimpleDoubleProperty(pc.getQuantiteProd() * pc.getPrixUniteProd()).asObject();
        });

        produitComboBox.setItems(FXCollections.observableArrayList(ProduitDAO.getAllProduits()));
        produitComboBox.setConverter(new StringConverter<Produit>() {
            @Override
            public String toString(Produit produit) {
                return produit != null ? produit.getNomProd() : "";
            }

            @Override
            public Produit fromString(String string) {
                return null;
            }
        });

        fournisseurComboBox.setItems(FXCollections.observableArrayList(FournisseurDAO.getAllFournisseur()));
        fournisseurComboBox.setConverter(new StringConverter<Fournisseur>() {
            @Override
            public String toString(Fournisseur fournisseur) {
                return fournisseur != null ? fournisseur.getIdFourn() + " " + fournisseur.getNomFourn() : "";
            }

            @Override
            public Fournisseur fromString(String string) {
                return null;
            }
        });

        datePrevuePicker.setValue(CommandesController.commandeSelectionne.getDatePrevCmd());
        fournisseurComboBox.setValue(CommandesController.commandeSelectionne.getFournisseur());
        produitsList.addAll(CommandesController.commandeSelectionne.getProduitsCommandes());

        for(ProduitCommande pc : CommandesController.commandeSelectionne.getProduitsCommandes()) {
            System.out.println(pc.getProduit().getIdProd() + " " + pc.getProduit().getNomProd());
        }

        produitsTable.setItems(produitsList);
    }
    @FXML
    private void cancelAddCmd(ActionEvent actionEvent) {
        ModalLoader.closeModal();
    }
    @FXML
    private void modifierCommande(ActionEvent actionEvent) {
        if (fournisseurComboBox.getValue() == null || datePrevuePicker.getValue() == null || produitsList.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs et ajouter au moins un produit.");
            return;
        }

        Commande commande = CommandesController.commandeSelectionne;
        commande.setProduitsCommandes(produitsList);
        for (ProduitCommande p : produitsList)
            System.out.println(p.getProduit().getNomProd() + " " + p.getQuantiteProd() + " " + p.getPrixUniteProd());

        commande.setFournisseur(fournisseurComboBox.getValue());
        System.out.println(commande.getFournisseur().getNomFourn() + " " + commande.getFournisseur().getIdFourn());
        commande.setDatePrevCmd(datePrevuePicker.getValue());

        ProduitCommandeDAO.deleteProduitCommande(commande.getIdCmd());
        CommandeDAO.modifierCmd(commande);

        produitsList.forEach(produitCommande ->{
            produitCommande.setCommande(commande);
            ProduitCommandeDAO.insertProduitCommande(produitCommande);
        });

        ModalLoader.closeModal();
        CommandesController.commandes.clear();
        CommandesController.commandes.addAll(CommandeDAO.getAllCommandesWithProductsByFilters(null,null,null,null));
    }
    @FXML
    private void addProductToList(ActionEvent actionEvent) {
        Produit produitSelectionne = produitComboBox.getValue();
        String quantiteText = quantiteField.getText();
        String prixUnitaireText = prixUnitaireField.getText();

        if (produitSelectionne == null || quantiteText.isEmpty() || prixUnitaireText.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un produit et remplir tous les champs.");
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteText);
            double prixUnitaire = Double.parseDouble(prixUnitaireText);

            if (quantite <= 0 || prixUnitaire <= 0) {
                showAlert("Erreur", "La quantité et le prix unitaire doivent être supérieurs à zéro.");
                return;
            }

            // Vérifier si le produit existe déjà dans la liste
            ProduitCommande existingProduitCommande = findProduitCommandeByProduit(produitSelectionne);

            if (existingProduitCommande != null) {
                // Mettre à jour la quantité et le prix unitaire
                existingProduitCommande.setQuantiteProd(existingProduitCommande.getQuantiteProd() + quantite);
                existingProduitCommande.setPrixUniteProd(prixUnitaire);
            } else {
                // Ajouter un nouveau ProduitCommande
                ProduitCommande produitCommande = new ProduitCommande();
                produitCommande.setProduit(produitSelectionne);
                produitCommande.setQuantiteProd(quantite);
                produitCommande.setPrixUniteProd(prixUnitaire);
                produitsList.add(produitCommande);
            }

            // Réinitialiser les champs après l'ajout
            produitComboBox.setValue(null);
            quantiteField.clear();
            prixUnitaireField.clear();

            // Mettre à jour l'affichage de la table
            produitsTable.refresh();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour la quantité et le prix unitaire.");
        }
    }

    private ProduitCommande findProduitCommandeByProduit(Produit produit) {
        for (ProduitCommande pc : produitsList) {
            if (pc.getProduit().getIdProd() == produit.getIdProd()) {
                return pc;
            }
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
