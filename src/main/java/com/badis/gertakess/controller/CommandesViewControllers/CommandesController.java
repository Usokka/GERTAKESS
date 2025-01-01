package com.badis.gertakess.controller.CommandesViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.CommandeDAO;
import com.badis.gertakess.dao.ProduitDAO;
import com.badis.gertakess.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.time.LocalDate;

public class CommandesController {


    @FXML
    private TableView<Commande> commandesTable;
    @FXML
    public TableColumn<Commande,Void> actionsColumn;
    @FXML
    private Button ajouterCommandeBtn;
    @FXML
    private TableColumn<Commande, Integer> idCmdColumn;
    @FXML
    private TableColumn<Commande, String> fournisseurColumn;
    @FXML
    private TableColumn<Commande, String> datePrevColumn;
    @FXML
    private TableColumn<Commande, String> dateColumn;
    @FXML
    private TableColumn<Commande, String> statutColumn;
    @FXML
    private TextField fournisseurSearch;
    @FXML
    private DatePicker datePicker;
    @FXML
    private DatePicker datePrevPicker;
    @FXML
    private ComboBox<String> statutComboBox;

    public static final ObservableList<Commande> commandes =
            FXCollections.observableArrayList();
    public static Commande commandeSelectionne;

    //Produits commandés
    @FXML
    private Label idCommandeLabel;
    @FXML
    private Label dateCommandeLabel;
    @FXML
    private Label datePrevLabel;
    @FXML
    private Label dateLivrLabel;
    @FXML
    private TableView<ProduitCommande> produitsTable;
    @FXML
    private TableColumn<ProduitCommande,String> nomColumn;
    @FXML
    private TableColumn<ProduitCommande,String> codeBarreColumn;
    @FXML
    private TableColumn<ProduitCommande,Integer> quantiteColumn;
    @FXML
    private TableColumn<ProduitCommande,Double> prixUnitaireColumn;
    @FXML
    private TableColumn<ProduitCommande, Double> totalColumn;
    @FXML
    private Label totalCmdLabel;

    @FXML
    private void initialize() {

        idCmdColumn.setCellValueFactory(new PropertyValueFactory<>("idCmd"));
        fournisseurColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFournisseur().getNomFourn()));
        datePrevColumn.setCellValueFactory(new PropertyValueFactory<>("datePrevCmd"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCmd"));
        statutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatutCmd() ? "LIVREE" : "NON LIVREE"));

        statutComboBox.setItems(FXCollections.observableArrayList("LIVREE","NON_LIVREE"));

        ajouterCommandeBtn.setOnAction(e -> ModalLoader.openModal("/com/badis/gertakess/commandesView/ajouter-cmd-modal.fxml"));
        commandesTable.setItems(commandes);

        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduit().getNomProd()));
        codeBarreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduit().getCodeBarProd()));
        quantiteColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantiteProd()).asObject());
        prixUnitaireColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrixUniteProd()).asObject());
        totalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());

        commandesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadCommandeDetails(newValue); // Load the details for the selected Panier
            }
        });

        commandesTable.getParent().layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            commandesTable.setPrefWidth(newBounds.getWidth() / 2);
        });

        commandesTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            idCmdColumn.setPrefWidth(tableWidth * 0.05);  // 30% de la largeur de la table
            fournisseurColumn.setPrefWidth(tableWidth * 0.15); // 20%
            datePrevLabel.setPrefWidth(tableWidth * 0.15); // 15%
            dateColumn.setPrefWidth(tableWidth * 0.15); // 15%
            statutColumn.setPrefWidth(tableWidth * 0.2); // 15%
            actionsColumn.setPrefWidth(tableWidth * 0.3); // 20%
        });

        produitsTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            nomColumn.setPrefWidth(tableWidth * 0.3);  // 30% de la largeur de la table
            codeBarreColumn.setPrefWidth(tableWidth * 0.2); // 20%
            quantiteColumn.setPrefWidth(tableWidth * 0.15); // 15%
            prixUnitaireColumn.setPrefWidth(tableWidth * 0.15); // 15%
            totalColumn.setPrefWidth(tableWidth * 0.2); // 20%
        });

        setupActionColumn();
        loadCommandes();
    }

    @FXML
    private void loadCommandes(){
        String nom = fournisseurSearch.getText() == null ? null : fournisseurSearch.getText();
        LocalDate date = datePicker.getValue() == null ? null : datePicker.getValue();
        LocalDate datePrev = datePrevPicker.getValue() == null ? null : datePrevPicker.getValue();
        String statut = statutComboBox.getValue() == null ? null : statutComboBox.getValue();

        commandes.clear();
        commandes.addAll(CommandeDAO.getAllCommandesWithProductsByFilters(nom,date,datePrev,statut));
    }

    private void setupActionColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button modifierButton = Utils.createEditerButton();
            private final Button supprimerButton = Utils.createTrashButton();
            private final Button validerButton = Utils.createValiderButton();

            {
                modifierButton.setOnAction(event -> {
                    Commande commande = getTableView().getItems().get(getIndex());
                    commandeSelectionne = commande;
                    ModalLoader.openModal("/com/badis/gertakess/commandesView/modifier-commande-modal.fxml");
                });

                supprimerButton.setOnAction(event -> {
                    boolean verif = Utils.confirmation(null,"Voulez vous supprimer cette commande ?",null,null);
                    if (!verif)
                        return;

                    Commande commande = getTableView().getItems().get(getIndex());
                    CommandeDAO.supprimerCmd(commande.getIdCmd());
                    loadCommandes();
                });

                validerButton.setOnAction(event -> {
                    boolean verif = Utils.confirmation(null,"Voulez vous valider cette commande ?",null,null);
                    if (!verif)
                        return;

                    Commande commande = getTableView().getItems().get(getIndex());
                    for (ProduitCommande pc : commande.getProduitsCommandes()) {
                        ProduitDAO.incrementStock(pc.getProduit().getIdProd(), pc.getQuantiteProd());
                    }
                    CommandeDAO.validerCmd(commande.getIdCmd());
                    loadCommandes();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Commande commande = getTableView().getItems().get(getIndex());
                    if (!commande.getStatutCmd()) { // Ajustez cette condition
                        HBox buttonsBox = new HBox(5, modifierButton, supprimerButton, validerButton);
                        setGraphic(buttonsBox);
                    } else {
                        setGraphic(null); // Attention à ne pas bloquer l'affichage
                    }
                }
            }
        });
    }


    @FXML
    private void initFiltresCommandes(){
        fournisseurSearch.setText(null);
        datePicker.setValue(null);
        datePrevPicker.setValue(null);

        statutComboBox.getSelectionModel().clearSelection();
        statutComboBox.setValue(null);
        Utils.resetComboBox(statutComboBox,"Filtrer par Statut");
    }


    private void loadCommandeDetails(Commande commande) {
        // Set the details of the selected panier
        idCommandeLabel.setText(String.valueOf(commande.getIdCmd()));
        dateCommandeLabel.setText(String.valueOf(commande.getDateCmd()));
        datePrevLabel.setText(String.valueOf(commande.getDatePrevCmd()));
        dateLivrLabel.setText(String.valueOf(commande.getDateLivrCmd()));

        // Load the list of products (LigneDeVente) in the commande
        ObservableList<ProduitCommande> produitsCommandes = FXCollections.observableArrayList(commande.getProduitsCommandes());
        produitsTable.setItems(produitsCommandes);

        // Calculate and display the total price of the panier
        double totalPanier = produitsCommandes.stream()
                .mapToDouble(ProduitCommande::getTotal)
                .sum();
        totalCmdLabel.setText(String.format("%.2f", totalPanier));
    }

    @FXML
    private void imprimerCommande(){
        Commande commande = commandesTable.getSelectionModel().getSelectedItem();

        if (commande == null) {
            return;
        }
        commandeSelectionne = commande;
        commande.imprimerCommande();
    }

}