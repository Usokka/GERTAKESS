package com.badis.gertakess.controller;

import com.badis.gertakess.dao.PointageDAO;
import com.badis.gertakess.dao.VenteDAO;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Pointage;
import com.badis.gertakess.model.Vente;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalTime;

public class MainController {

    @FXML
    private Label currentUserLabel;

    @FXML
    private MenuButton userMenuButton;

    @FXML
    private StackPane mainContent;

    @FXML
    private Button inventoryButton, cashRegisterButton, employeesButton, salesButton, suppliersButton, ordersButton;
    @FXML
    private AnchorPane cashRegisterView,inventoryView,employeesView,salesView,ordersView,suppliersView;

    @FXML
    private Employee user;

    public static Vente vente;


    @FXML
    private void initialize() {

        user = ConnexionController.emp;
        loadViews(user);
        setupNavigation(user);

        currentUserLabel.setText(user.getNomEmpl() + " " + user.getPrenomEmpl());

        userMenuButton.getItems().forEach(item -> {
            item.setOnAction(event -> handleUserMenuAction(item.getText()));
        });

        mainContent.getChildren().clear();
        mainContent.getChildren().add(cashRegisterView);
        setActiveButton(cashRegisterButton);

        vente = new Vente();
        vente.setEmploye(user);
        VenteDAO.insertVente(vente);
        vente = VenteDAO.getLastVenteOfEmployee(user.getIdEmpl());
    }

    private void loadViews(Employee user) {
        try {
            ordersButton.setDisable(true);
            employeesButton.setDisable(true);
            suppliersButton.setDisable(true);
            salesButton.setDisable(true);

            if(user.getPermCptEmpl().equals("ADMIN")){
                FXMLLoader employeesLoader = new FXMLLoader(getClass().getResource("/com/badis/gertakess/employesView/employees.fxml"));
                employeesView = employeesLoader.load();
                employeesButton.setDisable(false);
            }

            if (user.getPermCptEmpl().equals("ADMIN") || user.getPermCptEmpl().equals("MODERATEUR")){
                FXMLLoader salesLoader = new FXMLLoader(getClass().getResource("/com/badis/gertakess/ventesView/sales.fxml"));
                salesView = salesLoader.load();
                FXMLLoader ordersLoader = new FXMLLoader(getClass().getResource("/com/badis/gertakess/commandesView/orders.fxml"));
                ordersView = ordersLoader.load();
                FXMLLoader suppliersLoader = new FXMLLoader(getClass().getResource("/com/badis/gertakess/fournisseursView/suppliers.fxml"));
                suppliersView = suppliersLoader.load();

                salesButton.setDisable(false);
                ordersButton.setDisable(false);
                suppliersButton.setDisable(false);
            }

            FXMLLoader cashRegisterLoader = new FXMLLoader(getClass().getResource("/com/badis/gertakess/caisseView/cash-register.fxml"));
            cashRegisterView = cashRegisterLoader.load();

            FXMLLoader inventoryLoader = new FXMLLoader(getClass().getResource("/com/badis/gertakess/stockView/inventory.fxml"));
            inventoryView = inventoryLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupNavigation(Employee emp){

        if(!user.getPermCptEmpl().equals("UTILISATEUR") && !user.getPermCptEmpl().equals("MODERATEUR")) {
            employeesButton.setOnAction(event -> showView(employeesView,employeesButton));
        }
        if(!user.getPermCptEmpl().equals("UTILISATEUR")){
            ordersButton.setOnAction(event -> showView(ordersView,ordersButton));
            suppliersButton.setOnAction(event -> showView(suppliersView,suppliersButton));
            salesButton.setOnAction(event -> showView(salesView,salesButton));
        }
        inventoryButton.setOnAction(event -> showView(inventoryView,inventoryButton));
        cashRegisterButton.setOnAction(event -> showView(cashRegisterView,cashRegisterButton));
    }

    private void showView(AnchorPane view, Button button){
        mainContent.getChildren().clear();
        mainContent.getChildren().add(view);
        setActiveButton(button);
    }

    private void setActiveButton(Button button) {
        cashRegisterButton.getStyleClass().remove("active");
        employeesButton.getStyleClass().remove("active");
        salesButton.getStyleClass().remove("active");
        inventoryButton.getStyleClass().remove("active");
        suppliersButton.getStyleClass().remove("active");
        ordersButton.getStyleClass().remove("active");

        button.getStyleClass().add("active");
    }

    public void showShade(boolean show) {
        mainContent.lookup("#shade").setVisible(show);
    }

    private void handleUserMenuAction(String action) {
        switch (action) {
            case "Se Déconnecter":
                Pointage p = PointageDAO.getLastPointageOf(ConnexionController.emp.getIdEmpl());
                p.setHeureSortiePoint(LocalTime.now());
                PointageDAO.modifierPointage(p);
                ConnexionController.mainStage.close();
                break;
        }
    }
}
