module com.example.pi {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires java.mail;
    requires java.desktop;
    requires webcam.capture;
    requires org.bytedeco.opencv;
    requires org.bytedeco.javacv;
    requires twilio;

    opens com.example.pi to javafx.fxml;
    exports com.example.pi;
    exports com.example.pi.DB;
    opens com.example.pi.DB to javafx.fxml;
    exports com.example.pi.Controllers;
    opens com.example.pi.Controllers to javafx.fxml;

    opens com.example.pi.Entities to javafx.base;
}