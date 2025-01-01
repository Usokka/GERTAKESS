package com.badis.gertakess.controller.EmployeeViewControllers;

import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.EmployeDAO;
import com.badis.gertakess.model.Employee;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ModifierEmployeController {
    public TextField nomField;
    public TextField prenomField;
    public ComboBox<String> sexeComboBox;
    public ComboBox<String> situationComboBox;
    public TextField adresseField;
    public DatePicker dateNaissPicker;
    public TextField emailField;
    public TextField numTelField;
    public PasswordField mdpField;
    public PasswordField confirmMdpField;
    public ComboBox<String> permissionComboBox;

    @FXML
    public void initialize() {
        sexeComboBox.getItems().addAll(FXCollections.observableArrayList("HOMME", "FEMME"));
        situationComboBox.getItems().addAll(FXCollections.observableArrayList("MARIE", "CELIBATAIRE", "DIVORCE"));
        Employee emp = EmployeesController.employeSelectionne;

        nomField.setText(emp.getNomEmpl());
        prenomField.setText(emp.getPrenomEmpl());
        sexeComboBox.setValue(emp.getSexeEmpl());
        situationComboBox.setValue(emp.getSituationEmpl());
        adresseField.setText(emp.getAdresseEmpl());
        dateNaissPicker.setValue(emp.getDateNaissEmpl());
        emailField.setText(emp.getEmailEmpl());
        numTelField.setText(emp.getNumTelEmpl());
        mdpField.setText(emp.getMdpCptEmpl());
        confirmMdpField.setText(emp.getMdpCptEmpl());
    }

    @FXML
    public void modifEmploye() {
        Employee emp = EmployeesController.employeSelectionne;
        emp.setNomEmpl(nomField.getText().isEmpty() ? null : nomField.getText());
        emp.setPrenomEmpl(prenomField.getText().isEmpty() ? null : prenomField.getText());
        emp.setSexeEmpl(sexeComboBox.getValue().isEmpty() ? null : sexeComboBox.getValue());
        emp.setSituationEmpl(situationComboBox.getValue().isEmpty() ? null : situationComboBox.getValue());
        emp.setAdresseEmpl(adresseField.getText().isEmpty() ? null : adresseField.getText());
        emp.setDateNaissEmpl(dateNaissPicker.getValue() == null ? null : dateNaissPicker.getValue());
        emp.setEmailEmpl(emailField.getText().isEmpty() ? null : emailField.getText());
        emp.setNumTelEmpl(numTelField.getText().isEmpty() ? null : numTelField.getText());
        emp.setMdpCptEmpl(mdpField.getText().isEmpty() ? null : mdpField.getText());
        emp.setPermCptEmpl(permissionComboBox.getValue().isEmpty() ? null : permissionComboBox.getValue());
        boolean verif = EmployeesController.verifierInformations(emp.getNomEmpl(),
                emp.getPrenomEmpl(),emp.getSexeEmpl(),emp.getSituationEmpl(),
                emp.getAdresseEmpl(),emp.getDateNaissEmpl(),emp.getEmailEmpl(),
                emp.getNumTelEmpl(),emp.getMdpCptEmpl(),
                permissionComboBox.getValue().isEmpty() ? null : permissionComboBox.getValue(),
                confirmMdpField.getText().isEmpty() ? null : confirmMdpField.getText());

        if(verif)
            EmployeDAO.modifierEmploye(emp);

    }

    @FXML
    public void cancelModifEmpl() {
        ModalLoader.closeModal();
    }
}
