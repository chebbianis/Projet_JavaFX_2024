package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.LocationService;
import models.Location;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AjouterLocationController {

    LocationService sv = new LocationService();


    @FXML
    private DatePicker txtDate_debut;

    @FXML
    private DatePicker txtDate_fin;

    @FXML
    private TextField txtUser_id;

    @FXML
    private TextField txtVoiture_id;

    @FXML
    private TextField txtTypePaiement;

    @FXML
    private TextField txtOptions_supp;

    public DatePicker getTxtDate_debut() {
        return txtDate_debut;
    }
    public DatePicker getTxtDate_fin() {
        return txtDate_fin;
    }


    @FXML
    void ajouterAction(javafx.event.ActionEvent event) {

        try {
            Location p = new Location(txtDate_debut.getValue(),txtDate_fin.getValue(), Integer.parseInt(txtUser_id.getText()), Integer.parseInt(txtVoiture_id.getText()), txtTypePaiement.getText(), txtOptions_supp.getText());
            int generatedId = sv.add(p);
            //sv.add(new Voiture(txtVoiture_Marque.getText(), txtAnnee.getText(), Integer.parseInt(txtPrix_j.getText())));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setContentText("Location Ajouter avec succes");
            alert.show();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherL.fxml"));

            try {
                Parent root = loader.load();
                AfficherLControllers afficherLControllers = loader.getController();
                afficherLControllers.setTxtDate_debut(txtDate_debut.getValue());
                afficherLControllers.setTxtDate_fin(txtDate_fin.getValue());
                afficherLControllers.setTxtUser_id(txtUser_id.getText());
                afficherLControllers.setTxtVoiture_id((txtVoiture_id.getText()));
                afficherLControllers.setTxtTypePaiement((txtTypePaiement.getText()));
                afficherLControllers.setTxtOptions_supp((txtOptions_supp.getText()));
                afficherLControllers.setTxtIdL(String.valueOf(generatedId));


                txtOptions_supp.getScene().setRoot(root);
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


    public void setTxtDate_debut(LocalDate txtDate_debut) {
        this.txtDate_debut.setValue(txtDate_debut);
    }

    public void setTxtDate_fin(LocalDate txtDate_fin) {
        this.txtDate_fin.setValue(txtDate_fin);
    }

    public void setTxtUser_id(String txtUser_id) { this.txtUser_id.setText(txtUser_id);
    }

    public void setTxtVoiture_id(String txtVoiture_id) { this.txtVoiture_id.setText(txtVoiture_id);
    }

    public void setTxtTypePaiement(String txtTypePaiement) { this.txtTypePaiement.setText(txtTypePaiement);
    }
    public void setTxtOptions_supp(String txtOptions_supp) { this.txtOptions_supp.setText(txtOptions_supp);
    }

}