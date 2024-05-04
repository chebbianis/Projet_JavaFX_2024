package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class back {

    @FXML
    private Button Bt_Hotel;

    @FXML
    private Button Bt_front;

    @FXML
    void naviguezVersFront(ActionEvent event) {
        navigateTo("/FrontListHotel.fxml", event);


    }

    @FXML
    void naviguezVersHotel(ActionEvent event) {
        navigateTo("/ajouterHotel.fxml", event);

    }

    @FXML
    void naviguezVersRev(ActionEvent event) {
        navigateTo("/AfficherReservation.fxml", event);

    }
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            ((Button) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


}
