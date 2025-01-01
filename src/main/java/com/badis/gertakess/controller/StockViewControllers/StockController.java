package com.badis.gertakess.controller.StockViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.controller.CaisseViewControllers.CaisseController;
import com.badis.gertakess.dao.ProduitDAO;
import com.badis.gertakess.model.Produit;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;

import java.util.List;
import java.util.stream.Collectors;

public class StockController {

    @FXML
    private TableView<Produit> productTable;
    @FXML
    public TableColumn<Produit, Double> prixColumn;
    @FXML
    public TableColumn<Produit,Void> actionColumn;

    @FXML
    private TableColumn<Produit, Integer> idColumn;

    @FXML
    private TableColumn<Produit, String> codeBarColumn;

    @FXML
    private TableColumn<Produit, String> nameColumn;

    @FXML
    private TableColumn<Produit, Integer> quantityColumn;

    @FXML
    private TableColumn<Produit, String> categoryColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button ajouterProdBtn;

    @FXML
    private CheckComboBox<String> filterByCategories;

    public static final ObservableList<Produit> products = FXCollections.observableArrayList();
    public static Produit produitSelectionne;

    @FXML
    public void initialize() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProd"));
        codeBarColumn.setCellValueFactory(new PropertyValueFactory<>("codeBarProd"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomProd"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categorieProd"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixUniteProd"));

        idColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.05));
        codeBarColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.15));
        nameColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.30));
        quantityColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.10));
        categoryColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.10));
        prixColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.10));
        actionColumn.prefWidthProperty().bind(productTable.widthProperty().multiply(0.10));



        ajouterProdBtn.setOnAction(event -> ModalLoader.openModal("/com/badis/gertakess/stockView/ajouter-produit-modal.fxml"));
        filterByCategories.getItems().addAll(Utils.categories);
        productTable.setItems(products);

        filterByCategories.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                loadProduits();
            }
        });


        setupActionColumn();
        loadProduits();
        rupture();
    }


    @FXML
    private void loadProduits(){
        List<String> categories = filterByCategories.getCheckModel().getCheckedItems();
        String nomProd = searchField.getText() == "" ? null : searchField.getText();

        products.clear();
        products.addAll(ProduitDAO.getAllProduitByFilter(categories,nomProd,false));
    }

    private void rupture(){
        List<Produit> produitsRupture = products.stream().filter(produit -> produit.getQuantiteStock() <= 50).collect(Collectors.toList());

        for (Produit produit : produitsRupture) {
            String message = produit.getNomProd() + " ARRIVE BIENTÔT EN RUPTURE DE STOCK";
            System.out.println(message);

            // Show notification
            Notifications.create()
                    .title("Alerte Stock")
                    .text(message)
                    .position(Pos.TOP_RIGHT) // You can change the position
                    .hideAfter(Duration.seconds(5)) // Duration for the notification
                    .showWarning(); // Warning style
        }

    }

    // ACTIOOOOOOOONS

    private void setupActionColumn() {
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = Utils.createEditerButton();
            private final Button deleteButton = Utils.createTrashButton();

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");

                editButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    produitSelectionne = produit;
                    ModalLoader.openModal("/com/badis/gertakess/stockView/modifier-produit-modal.fxml");
                });

                deleteButton.setOnAction(event -> {

                    boolean verif = Utils.confirmation(null,"Voulez vous supprimer ce produit ?",null,null);
                    if (!verif)
                        return;

                    Produit produit = getTableView().getItems().get(getIndex());
                    ProduitDAO.supprimerProd(produit.getIdProd());
                    loadProduits();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(5, editButton, deleteButton);
                    setGraphic(buttonsBox);
                }
            }
        });
    }

    @FXML
    private void initFiltresProduits(){
        filterByCategories.getCheckModel().clearChecks();
        searchField.setText(null);
    }

    public static boolean verifierProd(String nom,String codeBarre,String quantite,String prix,String categorie){
        if(nom == null || quantite == null || prix == null ||categorie == null){
            Utils.showAlert("Erreur","Erreur de saisie d'informations","Veuillez remplir tous les champs",null);
            return false;
        }

        if(codeBarre != null && !codeBarre.isEmpty()){
            if(!codeBarre.matches(Utils.codeBarreRegex)){
                Utils.showAlert("Erreur","Erreur de saisie d'informations","Veuillez saisir un codebarre valide",null);
                return false;
            }
        }
        double prixD;
        try{
            prixD = Double.parseDouble(prix);
        }catch (NumberFormatException e){
            Utils.showAlert("Erreur","Erreur de saisie d'informations","Veuillez saisir un prix valide",null);
            return false;
        }

        if(prixD < 0){
            Utils.showAlert("Erreur","Erreur de saisie d'informations","Veuillez saisir un prix valide",null);
            return false;
        }

        int quantiteD;
        try{
            quantiteD = Integer.parseInt(quantite);
        }catch (NumberFormatException e){
            Utils.showAlert("Erreur","Erreur de saisie d'informations","Veuillez saisir une quantité valide",null);
            return false;
        }

        if(quantiteD < 0){
            Utils.showAlert("Erreur","Erreur de saisie d'informations","Veuillez saisir une quantité valide",null);
            return false;
        }

        return true;
    }
}
