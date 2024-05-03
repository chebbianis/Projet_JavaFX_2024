package com.example.pi.Controllers;
import com.example.pi.DB.DBUtils;
import com.example.pi.Services.SendSmsService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SmsController implements Initializable {

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_send;

    @FXML
    private TextArea tf_message;

    @FXML
    private TextField tf_number;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"/com/example/pi/logged-in.fxml","User Page",null);
            }
        });


        btn_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Vérification du champ tf_message
                String message = tf_message.getText().trim();
                String number = tf_number.getText().trim();
                if (message.isEmpty() || number.isEmpty()) {
                    showAlert("Veuillez remplir tous les champs.");
                } else if (!number.matches("\\d{8}")) {
                    showAlert("Le numéro doit contenir exactement 8 chiffres.");
                }
                else {
                    SendSmsService.sendSMS(number,message);
                    DBUtils.changeScene(event,"/com/example/pi/logged-in.fxml","User Page",null);
                }
            }
        });
    }

    // Méthode utilitaire pour afficher une boîte de dialogue d'alerte
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
