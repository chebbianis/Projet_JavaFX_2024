package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.LocationService;
import services.VoitureService;
import models.Location;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


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

    @FXML
    private TextField txtTotalPrice;


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

    @FXML
    void initialize() {
        // Add a listener to txtVoiture_id textField
        txtVoiture_id.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the new value is not empty
            if (!newValue.isEmpty()) {
                try {
                    // Retrieve the prix_j value from the database
                    VoitureService voitureService = new VoitureService();
                    int prixJ = voitureService.getPrixJById(Integer.parseInt(newValue));

                    // Calculate the number of days
                    LocalDate dateDebut = txtDate_debut.getValue();
                    LocalDate dateFin = txtDate_fin.getValue();
                    if (dateDebut != null && dateFin != null) {
                        long numberOfDays = ChronoUnit.DAYS.between(dateDebut, dateFin);

                        // Calculate the total price
                        int totalPrice = prixJ * (int) numberOfDays;

                        // Set the total price value in the textField
                        txtTotalPrice.setText(String.valueOf(totalPrice));
                    }
                } catch (SQLException e) {
                    // Handle SQL exception
                    e.printStackTrace();
                }
            } else {
                // Clear the total price textField if the voiture ID is empty
                txtTotalPrice.clear();
            }
        });
    }

}