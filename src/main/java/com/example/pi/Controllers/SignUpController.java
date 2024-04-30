package com.example.pi.Controllers;

import com.example.pi.DB.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button button_Signup;

    @FXML
    private Button button_login;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField pf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_Signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_username.getText().trim().isEmpty() && !pf_password.getText().trim().isEmpty() ) {
                    DBUtils.signUpUser(event,tf_username.getText(),pf_password.getText());
                }else {
                    System.out.println("Please fill in all informations");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all informations to sign up !");
                    alert.show();
                }
            }
        });

//        button_login.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                DBUtils.changeScene(event, "/com/example/pi/hello-view.fxml", "Log In!", null);
//            }
//        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Chargez le fichier FXML de la page de connexion
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pi/hello-view.fxml"));
                    Parent root = fxmlLoader.load();

                    // Créez une nouvelle scène
                    Scene scene = new Scene(root);

                    // Obtenez la référence de la scène actuelle
                    Stage stage = (Stage) button_login.getScene().getWindow();

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
