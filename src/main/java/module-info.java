module com.example.pidevjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;

    requires org.apache.pdfbox;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens com.example.pidevjava to javafx.fxml;
    opens com.example.pidevjava.Contollers to javafx.fxml;
    opens com.example.pidevjava.Entities to javafx.base;


    exports com.example.pidevjava;
}
