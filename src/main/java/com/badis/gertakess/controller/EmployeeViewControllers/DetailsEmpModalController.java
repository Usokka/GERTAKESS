package com.badis.gertakess.controller.EmployeeViewControllers;

import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Contrat;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class DetailsEmpModalController {

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label genderLabel;
    @FXML private Label dobLabel;
    @FXML private Label addressLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label statusLabel;
    @FXML private Label passwordLabel;
    @FXML private Label permissionLabel;
    @FXML private Label creationDateLabel;
    @FXML private Label accountStatusLabel;

    @FXML private TableView<Contrat> contractsTable;
    @FXML private TableColumn<Contrat, Integer> contractIdColumn;
    @FXML private TableColumn<Contrat, String> contractTypeColumn;
    @FXML private TableColumn<Contrat, String> startDateColumn;
    @FXML private TableColumn<Contrat, String> endDateColumn;
    @FXML private TableColumn<Contrat, String> quitDateColumn;
    @FXML private TableColumn<Contrat, String> positionColumn;
    @FXML private TableColumn<Contrat, Double> salaryColumn;

    private Employee employee;

    public void setEmployee(Employee employee) {
        this.employee = employee;
        populateEmployeeDetails();
        populateContractsTable();
    }

    @FXML
    private void initialize() {
        setEmployee(EmployeesController.employeSelectionne);
        setupContractsTable();
    }

    private void setupContractsTable() {
        contractIdColumn.setCellValueFactory(new PropertyValueFactory<>("idContr"));
        contractTypeColumn.setCellValueFactory(new PropertyValueFactory<>("typeContr"));
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebContr() != null ? cellData.getValue().getDateDebContr().toString() : null));
        endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinContr() != null ? cellData.getValue().getDateFinContr().toString() : null));
        quitDateColumn.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getDateQuitEmploi() != null ? cellData.getValue().getDateQuitEmploi().toString() : null)
        );
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("posteContr"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salaireJourContr"));
    }

    private void populateEmployeeDetails() {
        idLabel.setText(String.valueOf(employee.getIdEmpl()));
        nameLabel.setText(employee.getNomEmpl());
        surnameLabel.setText(employee.getPrenomEmpl());
        genderLabel.setText(employee.getSexeEmpl());
        dobLabel.setText(employee.getDateNaissEmpl().toString());
        addressLabel.setText(employee.getAdresseEmpl());
        phoneLabel.setText(employee.getNumTelEmpl());
        emailLabel.setText(employee.getEmailEmpl());
        statusLabel.setText(employee.getSituationEmpl());
        passwordLabel.setText("********"); // For security reasons, we don't display the actual password
        permissionLabel.setText(employee.getPermCptEmpl());
        creationDateLabel.setText(employee.getDateCptEmpl().toString());
        accountStatusLabel.setText(employee.isEtatCptEmpl() ? "ACTIF" : "INACTIF");
    }

    private void populateContractsTable() {
        contractsTable.getItems().setAll(employee.getContratsEmpl());
    }

    @FXML
    private void handleClose() {
        ModalLoader.closeModal();
    }
}