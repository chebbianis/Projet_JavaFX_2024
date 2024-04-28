module com.example.pi {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires google.api.client;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;
    requires org.apache.commons.codec;
    requires jbcrypt;

    opens com.example.pi to javafx.fxml;
    exports com.example.pi;
    exports com.example.pi.DB;
    opens com.example.pi.DB to javafx.fxml;
    exports com.example.pi.Controllers;
    opens com.example.pi.Controllers to javafx.fxml;

    opens com.example.pi.Entities to javafx.base;
}