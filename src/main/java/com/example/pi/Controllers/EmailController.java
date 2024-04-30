package com.example.pi.Controllers;

import com.example.pi.DB.DBUtils;
import com.example.pi.Services.GmailService;
import com.example.pi.Services.UserSession;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailController implements Initializable {
    @FXML
    private Button btn_back;

    @FXML
    private Button btn_send;

    @FXML
    private TextArea tf_message;

    @FXML
    private TextField tf_subject;

    @FXML
    private TextField tf_to;

    @FXML
    private Button btn_attach;

    // Variable pour stocker le chemin du fichier sélectionné
    private String attachedFilePath = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Récupère l'e-mail passé en paramètre lors du changement de scène
        String email = (String) DBUtils.getSceneData();

        // Affiche l'e-mail dans le champ tf_to
        tf_to.setText(email);
//        System.out.println("adresse mail dans emailController est : "+email);


        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"/com/example/pi/logged-in.fxml","User Page",null);
            }
        });

        btn_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (validateFields()) {
                    String to = tf_to.getText();
                    String subject = tf_subject.getText();
                    String message = tf_message.getText();

                    try {
                        // Passer le chemin du fichier attaché à SendEmail
                        GmailService.SendEmail(to, subject, message, attachedFilePath);
                        DBUtils.changeScene(event, "/com/example/pi/logged-in.fxml", "User Page", null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    public void setData(Object data) {
        if (data instanceof String) {
            String email = (String) data;
            tf_to.setText(email);
        }
    }

    private boolean validateFields() {
        if (tf_subject.getText().isEmpty() || tf_to.getText().isEmpty() || tf_message.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return false;
        }
        return true;
    }

    @FXML
    private void attachFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Attach");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Stocker le chemin du fichier sélectionné dans la variable de classe
            attachedFilePath = selectedFile.getAbsolutePath();
            // Utilisez le chemin du fichier sélectionné comme nécessaire, par exemple :
            // Vous pouvez l'afficher dans un champ de texte ou le stocker pour l'envoyer par e-mail plus tard.
            System.out.println("Chemin du fichier sélectionné : " + attachedFilePath);
        }
    }
}
