package com.badis.gertakess;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Optional;

public class Utils {


    public static String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static String numTelRegex = "^0[5-7]\\d{8}$";
    public static String nomRegex = "^[a-zA-Z]+([\\s-][a-zA-Z]+)*$";
    public static String codeBarreRegex = "^613\\d{9}\\d$";


    public static Button createTrashButton(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH); // Changed to a login icon
        icon.setGlyphSize(16); // Increased size for better visibility

        Button button = new Button("", icon);
        button.getStyleClass().add("custom-button"); // Add a style class instead of inline styles

        return button;
    }

    public static Button createDetailsButton() {
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EYE); // Changed to a login icon
        icon.setGlyphSize(16); // Increased size for better visibility

        Button button = new Button("", icon);
        button.getStyleClass().add("custom-button"); // Add a style class instead of inline styles

        return button;
    }


    public static Button createValiderButton(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CHECK); // Changed to a login icon
        icon.setGlyphSize(16); // Increased size for better visibility

        Button button = new Button("", icon);
        button.getStyleClass().add("custom-button"); // Add a style class instead of inline styles

        return button;
    }

    public static Button createEditerButton(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EDIT); // Changed to a login icon
        icon.setGlyphSize(16); // Increased size for better visibility

        Button button = new Button("", icon);
        button.getStyleClass().add("custom-button"); // Add a style class instead of inline styles

        return button;
    }

    public static void resetComboBox(ComboBox<String> comboBox, String promptText) {
        comboBox.getSelectionModel().clearSelection();
        comboBox.setValue(null);
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(promptText);
                } else {
                    setText(item);
                }
            }
        });
    }

    public static void showAlert(String titre,String titreErreur,String messErreur,Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(titreErreur);
        alert.setContentText(messErreur + (e != null ? e.getMessage() : ""));
        alert.showAndWait();
    }

    public static boolean confirmation(String titre,String titreErreur,String messErreur,Exception e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(titreErreur);
        alert.setContentText(messErreur + (e != null ? e.getMessage() : ""));

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static final ObservableList<String> postes=
            FXCollections.observableArrayList("Caissier(e)","Gérant","Nettoyeur","Responsable de stock");
    public static final ObservableList<String> contrats =
            FXCollections.observableArrayList("CDI","CDD");
    public static final ObservableList<String> categories =
            FXCollections.observableArrayList(
                    "Fruits","Légumes","Viandes & Volaille","Lait","Fromages","Yaourts","Glaces","Riz & Pâtes","Légumes secs","Farines et Céréales","Biscuits","Conserves","Eeau minérales et gazeuses","Sodas","Jus",
                    "Boissons énergétiques","Boulangerie et Pâtisserie","Huiles","Vinaigres","Sauces","Epices et Herbes","Chips","Noix et Fruits secs");
}
