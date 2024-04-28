module com.example.projectpi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.desktop;
    requires json.simple;
    requires stripe.java;
    opens controllers;
    opens entities to javafx.base;
    exports controllers;
    exports test;
}
