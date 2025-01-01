package com.badis.gertakess.controller.VentesViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.dao.PanierDAO;
import com.badis.gertakess.dao.VenteDAO;
import com.badis.gertakess.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Objects;


public class VentesController {

    //filtres
    public DatePicker filtrerParDate;
    public ComboBox<String> periodeComboBox;
    public TextField employeTextField;

    //table de ventes
    public TableView<Panier> ventesTable;
    public TableColumn<Panier,Integer> idVenteColumn;
    public TableColumn<Panier, String> dateColumn;
    public TableColumn<Panier, Integer> idPanierColumn;
    public TableColumn<Panier,Integer> idEmployeColumn;
    public TableColumn<Panier,String> nomEmployeColumn;
    public TableColumn<Panier,String> prenomEmployeColumn;

    //paniers des ventes
    public static final ObservableList<Panier> paniers =
            FXCollections.observableArrayList();
    public static Panier PanierSelectionne;
    public Label totalAnneeLabel;
    public Label totalMoisLabel;
    public Label totalSemaineLabel;
    public Label totalJourLabel;

    //détails panier
    public Label idPanierLabel;
    public Label datePanierLabel;
    public TableView<LigneDeVente> produitsTable;
    public TableColumn<LigneDeVente,String> nomColumn;
    public TableColumn<LigneDeVente,String> codeBarreColumn;
    public TableColumn<LigneDeVente,Integer> quantiteColumn;
    public TableColumn<LigneDeVente,Double> prixUnitaireColumn;
    public TableColumn<LigneDeVente, Double> totalColumn;
    public Label totalPanierLabel;


    @FXML
    private void initialize(){
        periodeComboBox.setItems(FXCollections.observableArrayList("Jour","Semaine","Mois","Année"));

        //paniers
        idVenteColumn.setCellValueFactory(celleData -> new SimpleIntegerProperty(celleData.getValue().getVente().getIdVente()).asObject());
        idPanierColumn.setCellValueFactory(new PropertyValueFactory<>("idPanier"));
        dateColumn.setCellValueFactory(celleData -> new SimpleStringProperty(celleData.getValue().getVente().getDateVente().toString()));
        idEmployeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVente().getEmploye().getIdEmpl()).asObject());
        nomEmployeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVente().getEmploye().getNomEmpl()));
        prenomEmployeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVente().getEmploye().getPrenomEmpl()));
        ventesTable.setItems(paniers);
        periodeComboBox.setValue("Jour");

        //Details panier
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduit().getNomProd()));
        codeBarreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduit().getCodeBarProd()));
        prixUnitaireColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrixUniteProd()).asObject());
        quantiteColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantiteProd()).asObject());
        totalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());

        ventesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadPanierDetails(newValue); // Load the details for the selected Panier
            }
        });

        ventesTable.getParent().layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            ventesTable.setPrefWidth(newBounds.getWidth() / 2);
        });

        produitsTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            nomColumn.setPrefWidth(tableWidth * 0.2);  // 30% de la largeur de la table
            codeBarreColumn.setPrefWidth(tableWidth * 0.25); // 20%
            quantiteColumn.setPrefWidth(tableWidth * 0.15); // 15%
            prixUnitaireColumn.setPrefWidth(tableWidth * 0.15); // 15%
            totalColumn.setPrefWidth(tableWidth * 0.15); // 20%
        });

        loadVentes();
    }

    @FXML
    private void loadVentes(){
        String employe = employeTextField.getText().isEmpty() ? null : employeTextField.getText();
        LocalDate date = filtrerParDate.getValue() == null ? null : filtrerParDate.getValue();
        String periode = periodeComboBox.getValue() == null ? null : periodeComboBox.getValue();

        paniers.clear();
        switch (Objects.requireNonNull(periode)){
            case "Jour":
                paniers.addAll(PanierDAO.getPaniers(employe,date,true,false,false,false));
                break;
            case "Semaine":
                paniers.addAll(PanierDAO.getPaniers(employe,date,false,true,false,false));
                break;
            case "Mois":
                paniers.addAll(PanierDAO.getPaniers(employe,date,false,false,true,false));
                break;
            case "Année" :
                paniers.addAll(PanierDAO.getPaniers(employe,date,false,false,false,true));
                break;
        }

        totalJourLabel.setText(Double.toString(VenteDAO.getTotalGains(date,true,false,false,false)));
        totalSemaineLabel.setText(Double.toString(VenteDAO.getTotalGains(date,false,true,false,false)));
        totalMoisLabel.setText(Double.toString(VenteDAO.getTotalGains(date,false,false,true,false)));
        totalAnneeLabel.setText(Double.toString(VenteDAO.getTotalGains(date,false,false,false,true)));
    }

    private void loadPanierDetails(Panier panier) {
        // Set the details of the selected panier
        idPanierLabel.setText(String.valueOf(panier.getIdPanier()));
        datePanierLabel.setText(panier.getVente().getDateVente().toString());

        // Load the list of products (LigneDeVente) in the panier
        ObservableList<LigneDeVente> lignesDeVente = FXCollections.observableArrayList(panier.getLignesDeVentes());
        produitsTable.setItems(lignesDeVente);

        // Calculate and display the total price of the panier
        double totalPanier = lignesDeVente.stream()
                .mapToDouble(LigneDeVente::getTotal)
                .sum();
        totalPanierLabel.setText(String.format("%.2f", totalPanier));
    }

    @FXML
    private void initVentes(){
        employeTextField.clear();
        periodeComboBox.setValue("Jour");
        dateColumn.setText(null);

        loadVentes();
    }

    public void imprimerVente(){
        Panier panier = ventesTable.getSelectionModel().getSelectedItem();

        if (panier == null) {
            return;
        }
        panier.imprimerPanier();
    }
}


