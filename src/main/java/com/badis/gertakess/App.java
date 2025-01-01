package com.badis.gertakess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.StageStyle;


public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("se-connecter.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Configurer la fenêtre
        stage.setTitle("Connexion");
        stage.initStyle(StageStyle.UNDECORATED); // Supprime les bordures et la barre de titre
        stage.setResizable(false); // Empêche le redimensionnement

        // Définir la scène
        stage.setScene(scene);

        // Ajuster la taille de la fenêtre au contenu
        stage.sizeToScene();

        // Centrer la fenêtre sur l'écran
        stage.centerOnScreen();

        // Afficher la fenêtre
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}