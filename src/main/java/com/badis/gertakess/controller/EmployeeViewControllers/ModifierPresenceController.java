package com.badis.gertakess.controller.EmployeeViewControllers;

import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.EmployeDAO;
import com.badis.gertakess.dao.PointageDAO;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Pointage;
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

public class ModifierPresenceController {
    public ComboBox<Employee> employeComboBox;
    public DatePicker datePicker;
    public Spinner<Integer> heureEntreeSpin;
    public Spinner<Integer> minEntreeSpin;
    public Spinner<Integer> minSortieSpin;
    public Spinner<Integer> heureSortieSpin;
    public CheckBox absentCheckBox;

    @FXML public void initialize() {
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

        employeComboBox.setValue(EmployeesController.pointageSelectionne.getEmploye());
        datePicker.setValue(EmployeesController.pointageSelectionne.getDatePoint());
        minEntreeSpin.increment(EmployeesController.pointageSelectionne.getHeureEntreePoint().getMinute());
        minSortieSpin.increment(EmployeesController.pointageSelectionne.getHeureSortiePoint().getMinute());
        heureEntreeSpin.increment(EmployeesController.pointageSelectionne.getHeureEntreePoint().getHour());
        heureSortieSpin.increment(EmployeesController.pointageSelectionne.getHeureSortiePoint().getHour());
        absentCheckBox.setSelected(EmployeesController.pointageSelectionne.getHeureEntreePoint().equals(LocalTime.of(0,0)) && EmployeesController.pointageSelectionne.getHeureSortiePoint().equals(LocalTime.of(0,0)));
        handleAbsent();
    }

    public void cancelModifPres() {
        ModalLoader.closeModal();
    }

    public void modifPresence() {
        int heureEntree = heureEntreeSpin.getValue();
        int heureSortie = heureSortieSpin.getValue();
        int minEntree = minEntreeSpin.getValue();
        int minSortie = minSortieSpin.getValue();
        boolean absent = absentCheckBox.isSelected();
        Employee employee = employeComboBox.getValue() == null ? null : employeComboBox.getValue();
        LocalDate date = datePicker.getValue() == null ? null : datePicker.getValue();

        boolean verifier = EmployeesController.verifierPointage(employee,date,heureEntree,heureSortie,minEntree,minSortie,absent);

        if(verifier){
            int id = EmployeesController.pointageSelectionne.getIdPoint();
            Pointage pointage = new Pointage(id,date, LocalTime.of(heureEntree,minEntree),LocalTime.of(heureSortie,minSortie),employee);
            PointageDAO.modifierPointage(pointage);
            cancelModifPres();
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
