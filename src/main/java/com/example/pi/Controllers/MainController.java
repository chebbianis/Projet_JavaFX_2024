package com.example.pi.Controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import com.example.pi.DB.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_email.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty() ) {
                    DBUtils.loginUser(event,tf_email.getText(),tf_password.getText());
                } else {
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to log in!");
                    alert.show();
                }
            }
        });


//        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                DBUtils.changeScene(event, "/com/example/pi/sign-up.fxml", "Sign Up !!", null);
//            }
//        });

        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Chargez le fichier FXML de la page d'inscription
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pi/sign-up.fxml"));
                    Parent root = fxmlLoader.load();

                    // Créez une nouvelle scène
                    Scene scene = new Scene(root);

                    // Obtenez la référence de la scène actuelle
                    Stage stage = (Stage) button_sign_up.getScene().getWindow();

                    // Définissez la nouvelle scène
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}