module com.example.pi {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.pi to javafx.fxml;
    exports com.example.pi;
    exports com.example.pi.DB;
    opens com.example.pi.DB to javafx.fxml;
    exports com.example.pi.Controllers;
    opens com.example.pi.Controllers to javafx.fxml;
}