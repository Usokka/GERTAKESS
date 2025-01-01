package com.badis.gertakess;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Objects;

public class ModalLoader {

    private static Stage modal = null;
    private static StackPane overlay;
    private static Scene mainScene;

    public static void openModal(String fxmlFile) {
        try {

            Stage mainStage = (Stage) mainScene.getWindow();

            overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Semi-transparent black

            mainScene.getRoot().setEffect(new GaussianBlur(10));

            ((StackPane) mainScene.getRoot()).getChildren().add(overlay);

            javafx.scene.Parent modalContent = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(ModalLoader.class.getResource(fxmlFile)));

            modal = new Stage();
            modal.setResizable(false);
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initStyle(StageStyle.UNDECORATED);
            modal.initOwner(mainStage);

            Scene modalScene = new Scene(modalContent);
            modalScene.setFill(Color.TRANSPARENT);
            modal.setScene(modalScene);

            modal.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public static void closeModal() {
        if (modal != null) {
            modal.close();
            modal = null;

            if (mainScene != null) {
                mainScene.getRoot().setEffect(null);
                ((StackPane) mainScene.getRoot()).getChildren().remove(overlay);
            }
            overlay = null;
        }
    }

    // Method to set the main scene (call this when your application starts)
    public static void setMainScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }
        mainScene = scene;

        // Ensure the root is a StackPane
        if (!(scene.getRoot() instanceof StackPane)) {
            StackPane newRoot = new StackPane(scene.getRoot());
            scene.setRoot(newRoot);
        }
    }
}