package com.badis.gertakess.controller.FournisseursViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.FournisseurDAO;
import com.badis.gertakess.model.Fournisseur;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.controlsfx.control.CheckComboBox;

import java.time.LocalDate;
import java.util.List;

public class FournisseursController {

    @FXML
    public TableColumn<Fournisseur,Integer> idColumn;
    @FXML
    public TableColumn<Fournisseur,String>  nomColumn;
    @FXML
    public TableColumn<Fournisseur,String>  numTelColumn;
    @FXML
    public TableColumn<Fournisseur,String>  adresseColumn;
    @FXML
    public TableColumn<Fournisseur,String>  emailColumn;
    @FXML
    public TableColumn<Fournisseur,String>  categoriesColumn;
    @FXML
    public TableColumn<Fournisseur, LocalDate> dateAoutColumn;

    @FXML
    public TableColumn<Fournisseur, Void> actionsColumn;
    @FXML
    public TextField filtrerParLieu;
    @FXML
    public CheckComboBox<String> filtrerParCategories;

    @FXML
    private Button ajouterFournBtn;

    @FXML
    private Button rafraichirFournBtn;

    @FXML
    public TextField filtrerParNom;

    @FXML
    private TableView<Fournisseur> fournisseursTable;
    public static final ObservableList<Fournisseur> fournisseurs = FXCollections.observableArrayList();
    public static Fournisseur fournisseurSelectionne;


    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idFourn"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomFourn"));
        numTelColumn.setCellValueFactory(new PropertyValueFactory<>("numTelFourn"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailFourn"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseFourn"));
        categoriesColumn.setCellValueFactory(new PropertyValueFactory<>("categoriesFourn"));
        ajouterFournBtn.setOnAction(event -> ModalLoader.openModal("/com/badis/gertakess/fournisseursView/ajouter-fournisseur-modal.fxml"));

        idColumn.prefWidthProperty().bind(fournisseursTable.widthProperty().multiply(0.05));
        nomColumn.prefWidthProperty().bind(fournisseursTable.widthProperty().multiply(0.10));
        numTelColumn.prefWidthProperty().bind(fournisseursTable.widthProperty().multiply(0.10));
        emailColumn.prefWidthProperty().bind(fournisseursTable.widthProperty().multiply(0.15));
        adresseColumn.prefWidthProperty().bind(fournisseursTable.widthProperty().multiply(0.15));
        categoriesColumn.prefWidthProperty().bind(fournisseursTable.widthProperty().multiply(0.30));
        actionsColumn.prefWidthProperty().bind(fournisseursTable.widthProperty().multiply(0.15));

        filtrerParCategories.getItems().addAll(Utils.categories);
        filtrerParCategories.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                loadFournisseurs();
            }
        });

        fournisseursTable.setItems(fournisseurs);
        setupActionColumn();
        loadFournisseurs();
    }

    @FXML
    public void loadFournisseurs(){
        String nom = filtrerParNom.getText() == "" ? null : filtrerParNom.getText() ;
        String adresse = filtrerParLieu.getText() == "" ? null :filtrerParLieu.getText();
        List<String> categories = filtrerParCategories.getCheckModel().getCheckedItems().isEmpty() ? null : filtrerParCategories.getCheckModel().getCheckedItems();
        fournisseurs.clear();
        fournisseurs.addAll(FournisseurDAO.getAllFournisseurByFilters(nom,adresse,categories,false));
    }

    private void setupActionColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = Utils.createEditerButton();
            private final Button deleteButton = Utils.createTrashButton();

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");

                editButton.setOnAction(event -> {
                    Fournisseur fournisseur = getTableView().getItems().get(getIndex());
                    fournisseurSelectionne = fournisseur;
                    ModalLoader.openModal("/com/badis/gertakess/fournisseursView/modifier-fournisseur-modal.fxml");
                });

                deleteButton.setOnAction(event -> {

                    boolean verif = Utils.confirmation(null,"Voulez vous supprimer ce fournisseur ?",null,null);
                    if (!verif)
                        return;

                    Fournisseur fournisseur = getTableView().getItems().get(getIndex());
                    FournisseurDAO.supprimerFourn(fournisseur.getIdFourn());
                    loadFournisseurs();
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
    private void initFiltres(){
        filtrerParNom.setText(null);
        filtrerParLieu.setText(null);
        filtrerParCategories.getCheckModel().clearChecks();
    }

    public static boolean verifierInformations(String nom,String email,String numTel,String adresse){

        if(nom == null || numTel == null || adresse == null || email == null){
            Utils.showAlert("Erreur","Erreur lors de la saisie des informations","Veuillez remplir tous les champs",null);
            return false;
        }

        if(!numTel.matches(Utils.numTelRegex)){
            Utils.showAlert("Erreur","Erreur lors de la saisie des informations","Veuillez saisir un numéro de téléphone valide",null);
            return false;
        }
        if(!email.matches(Utils.emailRegex)){
            Utils.showAlert("Erreur","Erreur lors de la saisie des informations","Veuillez saisir un email valide",null);
            return false;
        }

        return true;
    }

}
