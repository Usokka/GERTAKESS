package com.badis.gertakess.controller.EmployeeViewControllers;

import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.Utils;
import com.badis.gertakess.dao.ContratDAO;
import com.badis.gertakess.dao.EmployeDAO;
import com.badis.gertakess.model.Contrat;
import com.badis.gertakess.model.Employee;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class ModifierContratModalController {
    public ComboBox<Employee> employeComboBox;
    public DatePicker dateDebPicker;
    public DatePicker dateFinPick;
    public ComboBox<String> posteComboBox;
    public TextField salaireField;
    public ComboBox<String> typeComboBox;

    @FXML
    public void initialize() {
        employeComboBox.setItems(FXCollections.observableArrayList(EmployeDAO.getAllEmployesByFilters(null,null,null,null,"ACTIF",null)));
        employeComboBox.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee employe) {
                return employe != null ? employe.getIdEmpl() + " " + employe.getNomEmpl() + " " + employe.getPrenomEmpl() : "";
            }

            @Override
            public Employee fromString(String string) {
                return null;
            }
        });

        dateDebPicker.setValue(EmployeesController.contratSelectionne.getDateDebContr());
        posteComboBox.setItems(Utils.postes);
        posteComboBox.setValue(EmployeesController.contratSelectionne.getPosteContr());

        typeComboBox.setItems(Utils.contrats);
        typeComboBox.setValue(EmployeesController.contratSelectionne.getTypeContr());
        salaireField.setText(Double.toString(EmployeesController.contratSelectionne.getSalaireJourContr()));
        dateFinPick.setValue(EmployeesController.contratSelectionne.getDateFinContr());
    }

    public void modifContrat( ) {
        Employee employee = employeComboBox.getValue() == null ? null : employeComboBox.getValue();
        LocalDate dateDeb = dateDebPicker.getValue() == null ? null : dateDebPicker.getValue();
        LocalDate dateFin = dateFinPick.getValue() == null ? null : dateFinPick.getValue();
        String poste = posteComboBox.getValue().isEmpty() ? null : posteComboBox.getValue();
        String type = typeComboBox.getValue().isEmpty() ? null : typeComboBox.getValue();
        String salaire = salaireField.getText().isEmpty() ? null : salaireField.getText();

        boolean verifier = EmployeesController.verifierContrat(type,dateDeb,dateFin,salaire,poste);
        if(verifier){
            double salaireD = Double.parseDouble(salaire);
            Contrat contrat = new Contrat(type,dateDeb,dateFin,null,poste,salaireD,employee);
            ContratDAO.insererContrat(contrat);
            employee.setEtatCptEmpl(true);
            EmployeDAO.modifierEmploye(employee);
            ModalLoader.closeModal();
        }
    }

    public void cancelModifContr(){
        ModalLoader.closeModal();
    }
}
