package com.example.pidevjava;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label welcomeText;

    @FXML
    private Button btn_voyage;

    @FXML
    private Button btn_voyageur;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_voyage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/Voyage.fxml"));
                    Parent root = fxmlLoader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the stage from the button's scene
                    Stage stage = (Stage) btn_voyage.getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_voyageur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/Voyageur.fxml"));
                    Parent root = fxmlLoader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the stage from the button's scene
                    Stage stage = (Stage) btn_voyage.getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        welcomeText.setText("Welcome to JavaFX Application!");

        // Load the voyage.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("voyage.fxml"));
        try {
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the action event
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

