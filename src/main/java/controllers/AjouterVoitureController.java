package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.VoitureService;
import models.Voiture;
import services.VoitureService;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AjouterVoitureController {

    VoitureService sv = new VoitureService();

    @FXML
    private TextField txtVoiture_Marque;

    @FXML
    private DatePicker txtAnnee;

    @FXML
    private TextField txtPrix_j;

    @FXML
    private TextField txtKilometrage;

    @FXML
    private TextField txtNbrPlaces;

    public DatePicker getTxtAnnee() {
        return txtAnnee;
    }


    @FXML
    void ajouterAction(javafx.event.ActionEvent event) {

        try {
            Voiture p = new Voiture(txtVoiture_Marque.getText(), txtAnnee.getValue(), Integer.parseInt(txtPrix_j.getText()), Integer.parseInt(txtKilometrage.getText()), Integer.parseInt(txtNbrPlaces.getText()));
            int generatedId = sv.add(p);
            //sv.add(new Voiture(txtVoiture_Marque.getText(), txtAnnee.getText(), Integer.parseInt(txtPrix_j.getText())));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setContentText("Voiture Ajouter avec succes");
            alert.show();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherV.fxml"));

            try {
                Parent root = loader.load();
                AfficherVControllers afficherTControllers = loader.getController();
                afficherTControllers.setTxtAnnee(txtAnnee.getValue());
                afficherTControllers.setTxtVoiture_Marque(txtVoiture_Marque.getText());
                afficherTControllers.setTxtPrix_j((txtPrix_j.getText()));
                afficherTControllers.setTxtKilometrage((txtKilometrage.getText()));
                afficherTControllers.setTxtNbrPlaces((txtNbrPlaces.getText()));
                afficherTControllers.setTxtId(String.valueOf(generatedId));


                txtPrix_j.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();

        }
    }

    public void setTxtVoiture_Marque(String txtVoiture_Marque) { this.txtVoiture_Marque.setText(txtVoiture_Marque);
    }

    public void setTxtAnnee(LocalDate txtAnnee) {
        this.txtAnnee.setValue(txtAnnee);
    }

    public void setTxtPrix_j(String txtPrix_j) { this.txtPrix_j.setText(txtPrix_j);
    }

    public void setTxtKilometrage(String txtKilometrage) { this.txtKilometrage.setText(txtKilometrage);
    }

    public void setTxtNbrPlaces(String txtNbrPlaces) { this.txtNbrPlaces.setText(txtNbrPlaces);
    }

}
