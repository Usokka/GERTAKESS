package com.badis.gertakess.controller.EmployeeViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.ContratDAO;
import com.badis.gertakess.dao.EmployeDAO;
import com.badis.gertakess.model.Contrat;
import com.badis.gertakess.model.Employee;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class AjoutEmpModalController {

    public PasswordField confirmMdpField;
    public ComboBox<String> posteComboBox;
    public TextField salaireField;
    public DatePicker dateDebPicker;
    public DatePicker dateFinPicker;
    public ComboBox<String> typeContrComboBox;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private ComboBox<String> sexeComboBox;
    @FXML
    private DatePicker dateNaissPicker;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField numTelField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> situationComboBox;
    @FXML
    private PasswordField mdpField;
    @FXML
    private ComboBox<String> permissionComboBox;

    @FXML
    public void initialize() {
        typeContrComboBox.getItems().addAll(Utils.contrats);
        posteComboBox.getItems().addAll(Utils.postes);
        sexeComboBox.getItems().addAll(FXCollections.observableArrayList("HOMME", "FEMME"));
        situationComboBox.getItems().addAll(FXCollections.observableArrayList("MARIE", "CELIBATAIRE", "DIVORCE"));
        permissionComboBox.getItems().addAll(FXCollections.observableArrayList("ADMIN", "MODERATEUR", "UTILISATEUR"));
        sexeComboBox.setValue(sexeComboBox.getItems().get(0));
        situationComboBox.setValue(situationComboBox.getItems().get(0));
        permissionComboBox.setValue(permissionComboBox.getItems().get(0));
        posteComboBox.setValue(posteComboBox.getItems().get(0));
        typeContrComboBox.setValue(typeContrComboBox.getItems().get(0));
    }

    @FXML
    private void cancelAddEmpl(){

        ModalLoader.closeModal();
    }

    @FXML
    private void addEmployee() {
        // Retrieve data and validate inputs


        String nom = nomField.getText().isEmpty() ? null : nomField.getText();
        String prenom = prenomField.getText().isEmpty() ? null : prenomField.getText();
        String sexe = sexeComboBox.getValue().isEmpty() ? null : sexeComboBox.getValue();
        LocalDate dateNaiss = dateNaissPicker.getValue() == null ? null : dateNaissPicker.getValue();
        String adresse = adresseField.getText().isEmpty() ? null : adresseField.getText();
        String numTel = numTelField.getText().isEmpty() ? null : numTelField.getText();
        String email = emailField.getText().isEmpty() ? null : emailField.getText();
        String situation = situationComboBox.getValue().isEmpty() ? null : situationComboBox.getValue();
        String motDePasse = mdpField.getText().isEmpty() ? null : mdpField.getText();
        String confMotDePasse = confirmMdpField.getText().isEmpty() ? null : confirmMdpField.getText();
        String permission = permissionComboBox.getValue().isEmpty() ? null : permissionComboBox.getValue();


        LocalDate dateDeb = dateDebPicker.getValue() == null ? null : dateDebPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue() == null ? null : dateFinPicker.getValue();
        String salaire = salaireField.getText().isEmpty() ? null : salaireField.getText();
        String poste = posteComboBox.getValue().isEmpty() ? null : posteComboBox.getValue();
        String type = typeContrComboBox.getValue().isEmpty() ? null : typeContrComboBox.getValue();

        boolean verifier1 = EmployeesController.verifierInformations(nom,prenom,sexe,situation,adresse,dateNaiss,email,numTel,permission,motDePasse,confMotDePasse);
        boolean verifier2 = EmployeesController.verifierContrat(type,dateDeb,dateFin,salaire,poste);

        if(verifier1 && verifier2){
            Employee employee = new Employee(nom,prenom,sexe,dateNaiss,adresse,numTel,email,situation,motDePasse,permission,LocalDate.now(),true,null,null);
            boolean insert1 = EmployeDAO.insertEmploye(employee);
            int id = EmployeDAO.getLasIdAdded();
            employee.setIdEmpl(id);
            double salaireD = Double.parseDouble(salaire);
            Contrat contr = new Contrat(type,dateDeb,dateFin,null,poste,salaireD,employee);
            boolean insert2 = ContratDAO.insererContrat(contr);
            cancelAddEmpl();
        }
    }
}

