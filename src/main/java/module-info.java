module com.badis.gertakess {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.controlsfx.controls; // Required for ControlsFX
    requires com.jfoenix;

    opens com.badis.gertakess to javafx.fxml;
    opens com.badis.gertakess.controller to javafx.fxml;
    exports com.badis.gertakess;
    opens com.badis.gertakess.model to javafx.base;
    exports com.badis.gertakess.controller.CaisseViewControllers to javafx.fxml;
    opens com.badis.gertakess.controller.CaisseViewControllers to javafx.fxml;
    exports com.badis.gertakess.controller.EmployeeViewControllers to javafx.fxml;
    opens com.badis.gertakess.controller.EmployeeViewControllers to javafx.fxml;
    exports com.badis.gertakess.controller.CommandesViewControllers to javafx.fxml;
    opens com.badis.gertakess.controller.CommandesViewControllers to javafx.fxml;
    exports com.badis.gertakess.controller.FournisseursViewControllers to javafx.fxml;
    opens com.badis.gertakess.controller.FournisseursViewControllers to javafx.fxml;
    exports com.badis.gertakess.controller.VentesViewControllers to javafx.fxml;
    opens com.badis.gertakess.controller.VentesViewControllers to javafx.fxml;
    exports com.badis.gertakess.controller.StockViewControllers to javafx.fxml;
    opens com.badis.gertakess.controller.StockViewControllers to javafx.fxml;
    exports com.badis.gertakess.controller;
    exports com.badis.gertakess.model;

    requires java.base; // Core Java classes
    requires java.desktop; // For file handling (optional, but safe to include)
    requires org.apache.pdfbox; // iText core library

}