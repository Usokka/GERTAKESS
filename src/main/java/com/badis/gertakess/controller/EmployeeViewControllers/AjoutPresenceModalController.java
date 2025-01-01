package com.badis.gertakess.controller.EmployeeViewControllers;

import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.Utils;
import com.badis.gertakess.dao.EmployeDAO;
import com.badis.gertakess.dao.FournisseurDAO;
import com.badis.gertakess.dao.PointageDAO;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Fournisseur;
import com.badis.gertakess.model.Pointage;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;


public class AjoutPresenceModalController {

    @FXML
    public ComboBox<Employee> employeComboBox;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Spinner<Integer> heureEntreeSpin;
    @FXML
    public Spinner<Integer> heureSortieSpin;
    @FXML
    public CheckBox absentCheckBox;
    @FXML
    public Spinner<Integer> minEntreeSpin;
    @FXML
    public Spinner<Integer> minSortieSpin;

    @FXML
    public void initialize(){
        datePicker.setValue(LocalDate.now());
        employeComboBox.setItems(FXCollections.observableArrayList(EmployeDAO.getAllEmployesByFilters(null,null,null,null,null,null)));
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
    }


    @FXML
    public void cancelAddPres() {
        ModalLoader.closeModal();
    }

    @FXML
    public void addPresence() {

        int heureEntree = heureEntreeSpin.getValue();
        int heureSortie = heureSortieSpin.getValue();
        int minEntree = minEntreeSpin.getValue();
        int minSortie = minSortieSpin.getValue();
        boolean absent = absentCheckBox.isSelected();
        Employee employee = employeComboBox.getValue() == null ? null : employeComboBox.getValue();
        LocalDate date = datePicker.getValue() == null ? null : datePicker.getValue();

        boolean verifier = EmployeesController.verifierPointage(employee,date,heureEntree,heureSortie,minEntree,minSortie,absent);

        if(verifier){
            Pointage pointage = new Pointage(date,LocalTime.of(heureEntree,minEntree),LocalTime.of(heureSortie,minSortie),employee);
            PointageDAO.insertPointage(pointage);
            cancelAddPres();
            EmployeesController.pointages.clear();
            EmployeesController.pointages.addAll(PointageDAO.getAllPointagesByFiltres(null,null,null));
        }
    }

    @FXML
    private void handleAbsent(){

        if(absentCheckBox.isSelected()){
            heureEntreeSpin.decrement(heureEntreeSpin.getValue());
            heureSortieSpin.decrement(heureSortieSpin.getValue());
            minEntreeSpin.decrement(minEntreeSpin.getValue());
            minSortieSpin.decrement(minSortieSpin.getValue());

            heureEntreeSpin.setDisable(true);
            heureSortieSpin.setDisable(true);
            minEntreeSpin.setDisable(true);
            minSortieSpin.setDisable(true);
        }
        else{
            heureEntreeSpin.setDisable(false);
            heureSortieSpin.setDisable(false);
            minEntreeSpin.setDisable(false);
            minSortieSpin.setDisable(false);
        }
    }



}
