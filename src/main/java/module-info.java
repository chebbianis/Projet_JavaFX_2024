module com.example.projectpi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens controllers;
    opens entities to javafx.base;
    exports controllers;
    exports test;
}
