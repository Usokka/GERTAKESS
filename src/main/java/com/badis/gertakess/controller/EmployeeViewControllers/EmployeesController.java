package com.badis.gertakess.controller.EmployeeViewControllers;

import com.badis.gertakess.Utils;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.dao.ContratDAO;
import com.badis.gertakess.dao.EmployeDAO;
import com.badis.gertakess.dao.PointageDAO;
import com.badis.gertakess.dao.ProduitDAO;
import com.badis.gertakess.model.Contrat;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Pointage;
import com.badis.gertakess.model.Produit;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class EmployeesController {

    //EMPLOYEE TAB
    @FXML
    private TableView<Employee> employesTable;
    @FXML
    private TableColumn<Employee,String> emNivPermissionColumn;
    @FXML
    private TableColumn<Employee,Integer> emIdColumn;
    @FXML
    private TableColumn<Employee,String> emNomColumn;
    @FXML
    private TableColumn<Employee,String> emPrenomColumn;
    @FXML
    private TableColumn<Employee,String> emAdresseColumn;
    @FXML
    private TableColumn<Employee,String> emNumTelColumn;
    @FXML
    private TableColumn<Employee,String> emEmailColumn;
    @FXML
    public TableColumn<Employee,String>  emEtatEmpl;
    @FXML
    private TableColumn<Employee,String> emPosteColumn;
    @FXML
    private TableColumn<Employee,String> emTypeContratColumn;
    @FXML
    private TableColumn<Employee,Void> emActionsColumn;
    @FXML
    private Button ajouterEmployeBtn;
    @FXML
    private TextField nomTextField;
    @FXML
    private ComboBox<String> sexeComboBox;
    @FXML
    private TextField adresseTextField;
    @FXML
    private ComboBox<String> posteComboBox;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private ComboBox<String> contratComboBox;


    //PRESENCE TAB
    @FXML
    private TableView<Pointage> presenceTable;
    @FXML
    private TableColumn<Pointage,Integer> prIdColumn;
    @FXML
    private TableColumn<Pointage,String> prIdEmpColumn;
    @FXML
    private TableColumn<Pointage,String> prNomColumn;
    @FXML
    private TableColumn<Pointage,String> prPrenomNomColumn;
    @FXML
    private TableColumn<Pointage,String> prStatutColumn;
    @FXML
    private TableColumn<Pointage,String> prDateColumn;
    @FXML
    private TableColumn<Pointage,String> prHEntreColumn;
    @FXML
    private TableColumn<Pointage,String> prHSortieColumn;
    @FXML
    private TableColumn<Pointage,Void> prActionsColumn;
    @FXML
    private Button ajouterPresenceBtn;

    @FXML
    private TextField prRechEmplField;
    @FXML
    private DatePicker prDatePicker;
    @FXML
    private ComboBox<String> prStatutComboBox;


    //CONTRATS TAB
    @FXML
    private TextField ctrRechEmplField;
    @FXML
    private ComboBox<String> ctrComboBox;
    @FXML
    private TableView<Contrat> contratTable;
    @FXML
    private TableColumn<Contrat,Integer> ctrIdColumn;
    @FXML
    private TableColumn<Contrat,Integer> ctrIdEmpColumn;
    @FXML
    private TableColumn<Contrat,String> ctrNomColumn;
    @FXML
    private TableColumn<Contrat,String> ctrPrenomColumn;
    @FXML
    private TableColumn<Contrat,String> ctrPosteColumn;
    @FXML
    private TableColumn<Contrat,Double> ctrSalaireColumn;
    @FXML
    private TableColumn<Contrat,String> ctrDateDebColumn;
    @FXML
    private TableColumn<Contrat,String> ctrDateFinColumn;
    @FXML
    private TableColumn<Contrat,String> ctrDateQuitolumn;
    @FXML
    private TableColumn<Contrat,Void> ctrActionsColumn;
    public Button ajouterContratBtn;


    public static final ObservableList<Employee> employes = FXCollections.observableArrayList();
    public static final ObservableList<Pointage> pointages = FXCollections.observableArrayList();
    public static final ObservableList<Contrat> contrats = FXCollections.observableArrayList();
    public static Employee employeSelectionne;
    public static Pointage pointageSelectionne;
    public static Contrat contratSelectionne;


    @FXML
    private void initialize(){

        // EMPLOYÉS
        emIdColumn.setCellValueFactory(new PropertyValueFactory<>("idEmpl"));
        emNomColumn.setCellValueFactory(new PropertyValueFactory<>("nomEmpl"));
        emPrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenomEmpl"));
        emAdresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseEmpl"));
        emNumTelColumn.setCellValueFactory(new PropertyValueFactory<>("numTelEmpl"));
        emEmailColumn.setCellValueFactory(new PropertyValueFactory<>("emailEmpl"));
        emPosteColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContratsEmpl().get(cellData.getValue().getContratsEmpl().size() - 1).getPosteContr()));
        emTypeContratColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContratsEmpl().get(cellData.getValue().getContratsEmpl().size() - 1).getTypeContr()));
        emNivPermissionColumn.setCellValueFactory(new PropertyValueFactory<>("permCptEmpl"));
        emEtatEmpl.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isEtatCptEmpl() ? "ACTIF" : "INACTIF"));

        sexeComboBox.getItems().addAll(FXCollections.observableArrayList("HOMME","FEMME","TRANSFORMER"));
        posteComboBox.getItems().addAll(Utils.postes);
        contratComboBox.getItems().addAll(Utils.contrats);
        statutComboBox.getItems().addAll(FXCollections.observableArrayList("ACTIF","INACTIF"));
        ajouterEmployeBtn.setOnAction(event -> ModalLoader.openModal("/com/badis/gertakess/employesView/ajouter-employe-modal.fxml"));


        // TABLE EMPLOYE WIDTH
        emIdColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.05));
        emNomColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.10));
        emPrenomColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.10));
        emAdresseColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.15));
        emNumTelColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.10));
        emEmailColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.10));
        emPosteColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.10));
        emTypeContratColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.05));
        emNivPermissionColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.10));
        emActionsColumn.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.10));
        emEtatEmpl.prefWidthProperty().bind(employesTable.widthProperty().multiply(0.05));

        // PRÉSENCES
        prIdColumn.setCellValueFactory(new PropertyValueFactory<>("idPoint"));
        prIdEmpColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getEmploye().getIdEmpl())));
        prNomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmploye().getNomEmpl()));
        prPrenomNomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmploye().getPrenomEmpl()));
        prStatutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureEntreePoint() == LocalTime.of(0, 0) && cellData.getValue().getHeureSortiePoint() == LocalTime.of(0, 0) ? "Absent" : "Présent"));
        prDateColumn.setCellValueFactory(new PropertyValueFactory<>("datePoint"));
        prHEntreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureEntreePoint() == LocalTime.of(0, 0) && cellData.getValue().getHeureSortiePoint() == LocalTime.of(0, 0) ? null : cellData.getValue().getHeureEntreePoint().toString() ));
        prHSortieColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureEntreePoint() == LocalTime.of(0, 0) && cellData.getValue().getHeureSortiePoint() == LocalTime.of(0, 0) ? null : cellData.getValue().getHeureSortiePoint().toString()) );
        ajouterPresenceBtn.setOnAction(event -> ModalLoader.openModal("/com/badis/gertakess/employesView/ajouter-présence-modal.fxml"));

        prStatutComboBox.setItems(FXCollections.observableArrayList("Présent","Absent"));

        //CONTRATS
        ctrIdColumn.setCellValueFactory(new PropertyValueFactory<>("idContr"));
        ctrIdEmpColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty( cellData.getValue().getEmployeContr().getIdEmpl()).asObject());
        ctrNomColumn.setCellValueFactory(cellData -> new SimpleStringProperty( cellData.getValue().getEmployeContr().getNomEmpl()));
        ctrPrenomColumn.setCellValueFactory(cellData -> new SimpleStringProperty( cellData.getValue().getEmployeContr().getPrenomEmpl()));
        ctrDateDebColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebContr"));
        ctrDateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFinContr"));
        ctrDateQuitolumn.setCellValueFactory(new PropertyValueFactory<>("dateQuitEmploi"));
        ctrSalaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaireJourContr"));
        ctrPosteColumn.setCellValueFactory(new PropertyValueFactory<>("posteContr"));
        ctrComboBox.setItems(Utils.contrats);
        ajouterContratBtn.setOnAction(event -> ModalLoader.openModal("/com/badis/gertakess/employesView/ajouter-contrat-modal.fxml"));

        setupActionColumn();
        contratTable.setItems(contrats);
        employesTable.setItems(employes);
        presenceTable.setItems(pointages);
        loadEmployes();
        loadPresences();
        loadContrats();
    }


    @FXML
    private void loadEmployes(){

        String nom = Objects.equals(nomTextField.getText(), "") ? null : nomTextField.getText() ;
        String adresse = Objects.equals(adresseTextField.getText(), "") ? null :adresseTextField.getText();
        String sexe = sexeComboBox.getValue() == null ? null : sexeComboBox.getValue();
        String poste = posteComboBox.getValue() == null ? null : posteComboBox.getValue();
        String contrat = contratComboBox.getValue() == null ? null : contratComboBox.getValue();
        String statut = statutComboBox.getValue() == null ? null : statutComboBox.getValue();

        employes.clear();
        employes.addAll(EmployeDAO.getAllEmployesByFilters(nom,adresse,sexe,poste,statut,contrat));
    }

    @FXML
    private void loadPresences(){
        String nom = Objects.equals(prRechEmplField.getText(), "") ? null : prRechEmplField.getText();
        LocalDate date = prDatePicker.getValue() == null ? null : prDatePicker.getValue();
        String statut = Objects.equals(prStatutComboBox.getValue(), "") ? null : prStatutComboBox.getValue();

        pointages.clear();
        pointages.addAll(PointageDAO.getAllPointagesByFiltres(nom,date,statut));
    }

    @FXML
    private void loadContrats(){

        String nom = Objects.equals(ctrRechEmplField.getText(), "") ? null : ctrRechEmplField.getText();
        String typeContr = Objects.equals(ctrComboBox.getValue(), "") ? null : ctrComboBox.getValue();

        contrats.clear();
        contrats.addAll(ContratDAO.getAllContratsByFiltres(nom,typeContr));
    }

    @FXML
    private void initFiltresEmployes(){
        nomTextField.setText(null);
        adresseTextField.setText(null);

        sexeComboBox.setValue(null);
        statutComboBox.setValue(null);
        posteComboBox.setValue(null);
        contratComboBox.setValue(null);

        Utils.resetComboBox(sexeComboBox,"Filtrer par Sexe");
        Utils.resetComboBox(statutComboBox,"Filtrer par Statut");
        Utils.resetComboBox(posteComboBox,"Filtrer par Poste");
        Utils.resetComboBox(contratComboBox,"Filtrer par Contrat");
        loadEmployes();
    }

    @FXML
    private void initFiltresPresences(){
        prRechEmplField.setText(null);
        prDatePicker.setValue(null);
        Utils.resetComboBox(prStatutComboBox,"Filtrer par Statut");
        loadPresences();
    }

    @FXML
    private void initFiltresContrats(){
        ctrRechEmplField.setText(null);
        Utils.resetComboBox(ctrComboBox,"Filtrer par Type");
        loadContrats();
    }

    private void setupActionColumn() {
        emActionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = Utils.createEditerButton();
            private final Button deleteButton = Utils.createTrashButton();
            private final Button detailsButton = Utils.createDetailsButton();

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
                detailsButton.getStyleClass().add("details-button");

                editButton.setOnAction(event -> {
                    Employee employe = getTableView().getItems().get(getIndex());
                    employeSelectionne = employe;
                    ModalLoader.openModal("/com/badis/gertakess/employesView/modifier-employe-modal.fxml");
                });

                deleteButton.setOnAction(event -> {

                    boolean verif = Utils.confirmation(null,"Voulez vous supprimer cet(te) Employé(e) ?",null,null);
                    if (!verif)
                        return;
                    Employee employe = getTableView().getItems().get(getIndex());
                    EmployeDAO.supprimerEmploye(employe.getIdEmpl());
                    loadEmployes();
                });

                detailsButton.setOnAction(event -> {
                    Employee employe = getTableView().getItems().get(getIndex());
                    employeSelectionne = employe;
                    ModalLoader.openModal("/com/badis/gertakess/employesView/details-employe-modal.fxml");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(5, editButton, deleteButton, detailsButton);
                    setGraphic(buttonsBox);
                }
            }
        });

        prActionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = Utils.createEditerButton();
            private final Button deleteButton = Utils.createTrashButton();
            private final Button detailsButton = Utils.createDetailsButton();

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
                detailsButton.getStyleClass().add("details-button");

                editButton.setOnAction(event -> {
                    Pointage pointage = getTableView().getItems().get(getIndex());
                    pointageSelectionne = pointage;
                    ModalLoader.openModal("/com/badis/gertakess/employesView/modifier-presence-modal.fxml");
                });

                deleteButton.setOnAction(event -> {
                    boolean verif = Utils.confirmation(null,"Voulez vous supprimer ce pointage ?",null,null);
                    if (!verif)
                        return;

                    Pointage pointage = getTableView().getItems().get(getIndex());
                    PointageDAO.supprimerPointage(pointage.getIdPoint());
                    loadPresences();
                    loadEmployes();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(5, editButton, deleteButton);
                    setGraphic(buttonsBox);
                }
            }
        });

        ctrActionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = Utils.createEditerButton();
            private final Button finishButton = Utils.createValiderButton();

            {
                editButton.getStyleClass().add("edit-button");
                finishButton.getStyleClass().add("details-button");


                editButton.setOnAction(event -> {
                    Contrat contrat = getTableView().getItems().get(getIndex());
                    contratSelectionne = contrat;
                    ModalLoader.openModal("/com/badis/gertakess/employesView/modifier-contrat-modal.fxml");
                });

                finishButton.setOnAction(event -> {

                    boolean verif = Utils.confirmation(null,"Voulez vous finir ce Contrat ?",null,null);
                    if (!verif)
                        return;

                    Contrat contrat = getTableView().getItems().get(getIndex());
                    ContratDAO.finirContrat(contrat.getIdContr());
                    EmployeDAO.supprimerEmploye(contrat.getEmployeContr().getIdEmpl());
                    loadContrats();
                    loadEmployes();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Contrat contrat = getTableView().getItems().get(getIndex());
                    HBox buttonsBox = null;
                    if(contrat.getDateQuitEmploi() == null)
                        buttonsBox = new HBox(5, editButton, finishButton);

                    setGraphic(buttonsBox);
                }
            }
        });
    }


    public static boolean verifierInformations(String nom,String prenom,String sexe,String situation,
                                               String adresse,LocalDate dateNaiss,String email,String numTel,
                                               String permission,String mdp,String confirmMdp){
        if(nom == null || prenom == null || sexe == null || situation == null || adresse == null || dateNaiss == null || email == null || numTel == null || mdp == null || confirmMdp == null || permission == null) {
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez remplir tous les champs",null);
            return false;
        }

        if(!nom.matches(Utils.nomRegex)) {
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir un nom valide",null);
            return false;
        }

        if(!prenom.matches(Utils.nomRegex)) {
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir un prénom valide",null);
            return false;
        }

        if(!email.matches(Utils.emailRegex)){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir un email valide",null);
            return false;
        }

        if(!numTel.matches(Utils.numTelRegex)){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir un numéro de téléphone valide",null);
            return false;
        }

        if (mdp.length() < 5){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir un mot de passe de plus de 5 caractères",null);
            return false;
        }

        if(!mdp.equals(confirmMdp)){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir un mot de passe identique",null);
            return false;
        }

        return true;
    }

    public static boolean verifierContrat(String type, LocalDate dateDeb,LocalDate dateFin,String salaire,String poste){
        if(type == null || dateDeb==null|| salaire==null || poste == null){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez remplir tous les champs",null);
            return false;
        }

        if (type.equals("CDD") && dateFin == null){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir la date de fin du contrat",null);
            return false;
        }

        if (type.equals("CDD") && dateFin.isBefore(dateDeb)){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","La date de fin doit être supérieure à celle du début du contrat",null);
            return false;
        }

        if (type.equals("CDI") && dateFin != null){
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez enlever la date de fin du contrat",null);
            return false;
        }



        try{
            double salaireD = Double.parseDouble(salaire);
        } catch (NumberFormatException e) {
            Utils.showAlert("Erreur informations","Erreur lors de la saisie d'informations","Veuillez saisir un salaire valide",null);
            return false;
        }
        return true;
    }

    public static boolean verifierPointage(Employee employe,LocalDate date,int heureEntree,int heureSortie,int minEntree,int minSortie,boolean absent) {
        if (employe == null || date == null) {
            Utils.showAlert("Erreur", "Erreur Ajout Pointage", "Veuillez remplir tous les champs", null);
            return false;
        }

        if (!absent) {
            if (heureEntree > heureSortie) {
                Utils.showAlert("Erreur", "Erreur Ajout Pointage", "Heure d'entrée doit être inférieure à l'heure de sortie", null);
                return false;
            } else if (heureSortie == heureEntree && minEntree == minSortie) {
                Utils.showAlert("Erreur", "Erreur Ajout Pointage", "Heure d'entrée et heure de sortie ne peuvent pas être pareilles", null);
                return false;
            }
        }
        return true;
    }

}
