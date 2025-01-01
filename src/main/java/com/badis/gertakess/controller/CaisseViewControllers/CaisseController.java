package com.badis.gertakess.controller.CaisseViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.controller.MainController;
import com.badis.gertakess.dao.LigneDeVenteDAO;
import com.badis.gertakess.dao.PanierDAO;
import com.badis.gertakess.dao.ProduitDAO;
import com.badis.gertakess.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import java.util.List;
import java.util.stream.Collectors;

public class CaisseController {

    @FXML
    private Button payerButton;
    @FXML
    private VBox panier;
    @FXML
    private VBox produits;
    @FXML
    private VBox transactions;
    @FXML
    private HBox main;
    @FXML
    private Label totalLab;
    @FXML
    private TextField seachCodeBarre;

    @FXML
    private GridPane produitsGrid;
    @FXML
    private GridPane quantityButtonsGrid;
    @FXML
    private TextField seachProduit;
    @FXML
    private TableView<Produit> panierTableView;
    @FXML
    private TableColumn<Produit, String> produitCaisse;
    @FXML
    private TableColumn<Produit, String>  codeBarreCaisse;
    @FXML
    private TableColumn<Produit, Double> prixProduitCaisse;
    @FXML
    private TableColumn<Produit, Integer> quantiteProduitCaisse;
    @FXML
    private TableColumn<Produit, Double> totalProduitCaisse;
    @FXML
    private TableColumn<Produit, Void> caisseActions;

    public static ObservableList<Produit> allProductsSansCB = FXCollections.observableArrayList();
    public static ObservableList<Produit> allProducts = FXCollections.observableArrayList();
    public static ObservableList<Produit> cartItems = FXCollections.observableArrayList();


    //TRANSACTIONS
    @FXML
    private TableView<Panier> transactionsTable;
    @FXML
    private TableColumn<Panier,Integer> idPan;
    @FXML
    private TableColumn<Panier, Void> produitsPan;
    @FXML
    private TableColumn<Panier, Double> totalPan;
    private ObservableList<Panier> transactionList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        panierTableView.setItems(cartItems);
        loadProducts();
        setupSearchFunctionality();
        setupCartTable();
        setupQuantityButtons();

        transactionsTable.setItems(transactionList);
        idPan.setCellValueFactory(new PropertyValueFactory<>("idPanier"));
        totalPan.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getLignesDeVentes().stream().mapToDouble(LigneDeVente::getTotal).sum()).asObject());
        produitsPan.setCellFactory(column -> new TableCell<>() {
            private final Button detailsButton = Utils.createDetailsButton();

            {
                detailsButton.setOnAction(event -> {
                    Panier pan = getTableView().getItems().get(getIndex());
                    String text = "Produits :\n";
                    for (LigneDeVente ldv : pan.getLignesDeVentes()) {
                        text += ldv.getProduit().getNomProd() + " - "+ ldv.getQuantiteProd() + "x"+ ldv.getPrixUniteProd() +" = " + ldv.getTotal() + "\n";
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Produits");
                    alert.setHeaderText("Produits");
                    alert.setContentText(text);
                    alert.showAndWait();
                });


            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Panier panier = getTableView().getItems().get(getIndex());
                    if (panier != null) { // Ajustez cette condition
                        HBox buttonsBox = new HBox(5, detailsButton);
                        setGraphic(buttonsBox);
                    } else {
                        setGraphic(null); // Attention à ne pas bloquer l'affichage
                    }
                }
            }
        });


        HBox.setHgrow(transactions, Priority.ALWAYS);
        HBox.setHgrow(produits, Priority.ALWAYS);
        HBox.setHgrow(panier, Priority.ALWAYS);

        transactions.prefWidthProperty().bind(main.widthProperty().multiply(0.25));
        produits.prefWidthProperty().bind(main.widthProperty().multiply(0.35));
        panier.prefWidthProperty().bind(main.widthProperty().multiply(0.40));
    }

    @FXML
    private void loadProducts() {
        allProductsSansCB.clear();
        allProductsSansCB.addAll(ProduitDAO.getAllProduitsSansCodeBarre(false));
        allProducts.clear();
        allProducts.addAll(ProduitDAO.getAllProduits());
        displayProducts(allProductsSansCB);
    }

    private void displayProducts(List<Produit> products) {
        produitsGrid.getChildren().clear();
        int col = 0;
        int row = 0;

        int width = (int) produitsGrid.getWidth();
        int productSize  = 300;

        for (Produit product : products) {
            if (product.getCodeBarProd() == null) {
                VBox productBox = createProductBox(product);
                productBox.setPrefWidth(productSize);
                productBox.setPrefHeight(productSize/2);
                produitsGrid.add(productBox, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private VBox createProductBox(Produit product) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("product-box");

        Label nameLabel = new Label(product.getNomProd());
        nameLabel.getStyleClass().add("product-name");
        Label priceLabel = new Label(String.format("%.2f DZD", product.getPrixUniteProd()));
        priceLabel.getStyleClass().add("product-price");

        box.getChildren().addAll(nameLabel, priceLabel);

        box.setOnMouseClicked(event -> addToCart(product));

        return box;
    }

    private void addToCart(Produit product) {

        System.out.println(product.getQuantiteStock());
        if (product.getQuantiteStock() <= 0)
            return;

        Produit existingItem = cartItems.stream()
                .filter(item -> item.getIdProd() == product.getIdProd())
                .findFirst()
                .orElse(null);

        if (existingItem != null)
        {
            if( product.getQuantiteStock() == existingItem.getQuantiteStock())
                return;
        }

        if (existingItem != null) {
            existingItem.setQuantiteStock(existingItem.getQuantiteStock() + 1);
        } else {
            Produit cartItem = new Produit(
                    product.getIdProd(),
                    product.getNomProd(),
                    product.getCodeBarProd(),
                    product.getCategorieProd(),
                    1,
                    product.getPrixUniteProd(),
                    false
            );
            cartItems.add(cartItem);
        }

        int total = 0;
        for(Produit produit : cartItems){
            total += produit.getTotal();
        }

        totalLab.setText(String.valueOf(total) + ".00 DZD");
        panierTableView.refresh();
    }

    private void setupSearchFunctionality() {
        seachProduit.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Produit> filteredProducts = allProductsSansCB.stream()
                    .filter(product -> product.getNomProd().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            displayProducts(filteredProducts);
        });
    }

    private void setupCartTable() {
        produitCaisse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomProd()));
        prixProduitCaisse.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrixUniteProd()).asObject());
        quantiteProduitCaisse.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantiteStock()).asObject());
        totalProduitCaisse.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());
        codeBarreCaisse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodeBarProd()));


        panierTableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            produitCaisse.setPrefWidth(tableWidth * 0.25);
            codeBarreCaisse.setPrefWidth(tableWidth * 0.25);
            prixProduitCaisse.setPrefWidth(tableWidth * 0.1);
            quantiteProduitCaisse.setPrefWidth(tableWidth * 0.1);
            totalProduitCaisse.setPrefWidth(tableWidth * 0.15);
            caisseActions.setPrefWidth(tableWidth * 0.13);
            totalLab.setMinWidth(tableWidth);
        });


        caisseActions.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = Utils.createTrashButton();

            {
                deleteButton.setOnAction(event -> {
                    Produit product = getTableView().getItems().get(getIndex());
                    cartItems.remove(product);
                    int total = 0;
                    for(Produit produit : cartItems){
                        total += produit.getTotal();
                    }

                    totalLab.setText(String.valueOf(total) + ".00 DZD");
                    panierTableView.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        panierTableView.setItems(cartItems);
    }

    private void setupQuantityButtons() {
        quantityButtonsGrid.getStyleClass().add("quantity-buttons-grid");

        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.33);
            quantityButtonsGrid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < 3; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(33.33);
            quantityButtonsGrid.getRowConstraints().add(row);
        }

        for (int i = 1; i <= 9; i++) {
            Button button = new Button(String.valueOf(i));
            button.getStyleClass().addAll("quantity-button", "bg-primary", "text-primary-foreground", "hover:bg-primary/90");
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(Double.MAX_VALUE);
            button.setOnAction(event -> multiplyQuantity(Integer.parseInt(button.getText())));
            quantityButtonsGrid.add(button, (i - 1) % 3, (i - 1) / 3);
        }
    }

    private void multiplyQuantity(int multiplier) {
        Produit selectedItem = panierTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem.getIdProd() + " " + selectedItem.getNomProd());
        Produit p = allProducts.stream().filter(produit -> produit.getIdProd() == selectedItem.getIdProd()).findFirst().orElse(null);

        assert p != null;
        if (selectedItem.getQuantiteStock()*multiplier > p.getQuantiteStock())
            return;

        if (selectedItem != null) {
            selectedItem.setQuantiteStock(selectedItem.getQuantiteStock() * multiplier);
            int total = 0;
            for(Produit produit : cartItems){
                total += produit.getTotal();
            }

            totalLab.setText(String.valueOf(total) + ".00 DZD");
            panierTableView.refresh();
        }

    }

    @FXML
    private void validerPaiement() {
        List<Produit> produits = panierTableView.getItems();

        if (produits != null && !produits.isEmpty()) {
            Panier panier = new Panier();
            panier.setVente(MainController.vente);
            PanierDAO.insertPanier(panier);
            panier = PanierDAO.getLastPanierOfVente(MainController.vente.getIdVente());

            for (Produit prod : produits) {
                LigneDeVente ldv = new LigneDeVente();
                ldv.setPanier(panier);
                ldv.setProduit(prod);
                ldv.setPrixUniteProd(prod.getPrixUniteProd());
                ldv.setQuantiteProd(prod.getQuantiteStock());
                LigneDeVenteDAO.insertLigneDeVente(ldv);
                ProduitDAO.decrementStock(prod.getIdProd(),prod.getQuantiteStock());
            }

            int total = 0;
            totalLab.setText(String.valueOf(total) + ".00 DZD");
            cartItems.clear();
        }

        transactionList.clear();
        transactionList.addAll(PanierDAO.getTransactionsRecentes(MainController.vente.getIdVente()));
        loadProducts();
    }

    @FXML
    private void annulerPaiement() {

        int total = 0;
        totalLab.setText(String.valueOf(total) + ".00 DZD");
        cartItems.clear();
    }

    @FXML
    private void ajouterProd(){
        String codeBarre = seachCodeBarre.getText();

        if(codeBarre == null || codeBarre.isEmpty()){
            return;
        }

        if(!codeBarre.matches(Utils.codeBarreRegex)){
            seachCodeBarre.clear();
            return;
        }

        Produit p = ProduitDAO.getProduitByCodeBar(codeBarre);
        if(p == null){
            seachCodeBarre.clear();
            return;
        }

        addToCart(p);
    }

    @FXML
    private void loadTransactions(){
        transactionList.clear();
        transactionList.addAll(PanierDAO.getTransactionsRecentes(MainController.vente.getIdVente()));
    }

    @FXML
    private void imprimerVente(){
        Panier panier = transactionsTable.getSelectionModel().getSelectedItem();

        if (panier == null) {
            return;
        }
        panier.imprimerPanier();
    }
}

