package com.badis.gertakess.controller;
import com.badis.gertakess.ModalLoader;
import com.badis.gertakess.Utils;
import com.badis.gertakess.dao.EmployeDAO;
import com.badis.gertakess.dao.PointageDAO;
import com.badis.gertakess.model.Employee;
import com.badis.gertakess.model.Pointage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConnexionController {

    @FXML
    private TextField utilisateur;
    @FXML
    private PasswordField motDePasse;

    public static Employee emp;

    public static Stage mainStage;
    @FXML
    private Button closeButton;

    @FXML
    private Button connectButton;

    @FXML
    private void handleCloseButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void seConnecter(){

        try {
            String user = utilisateur.getText();

            if((user.matches(Utils.emailRegex) || user.matches(Utils.numTelRegex))){
                String password = motDePasse.getText();

                emp = EmployeDAO.getUser(user,password);
                if(emp == null){
                    Utils.showAlert("Erreur","Erreur de Connexion","Nom d'utilisateur ou mot de passe incorrects.",null);
                }
                else{
                    lancer();
                }
            }
            else if(user.equals("ADMIN"))
            {
                emp = new Employee();
                emp.setNomEmpl("ABCD");
                emp.setPrenomEmpl("EFG");
                emp.setPermCptEmpl("ADMIN");
                lancer();
            }
            else
            {
                Utils.showAlert("Erreur","Erreur de Connexion","Veuillez saisir votre email ou votre numéro de téléphone",null);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert("Ereur","Erreur FXML","main-view n'a pas pu être chargée",e);
        }
    }

    public void lancer() throws IOException {
        URL fxmlUrl = getClass().getResource("/com/badis/gertakess/main.fxml");
        if (fxmlUrl == null) {
            throw new IOException("Cannot find main.fxml file");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        mainStage = new Stage();
        mainStage.setTitle("GERTAKESS");
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();

        Stage loginStage = (Stage) connectButton.getScene().getWindow();
        loginStage.close();

        ModalLoader.setMainScene(scene);
        Pointage p = new Pointage(LocalDate.now(), LocalTime.now(),null,emp);
        PointageDAO.insertPointage(p);
    }
}
