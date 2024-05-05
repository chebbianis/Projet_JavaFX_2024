package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Voiture;
import services.LocationService;
import services.VoitureService;
import models.Location;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


public class AjouterLocationController {

    LocationService sv = new LocationService();
    VoitureService voitureService = new VoitureService();


    @FXML
    private DatePicker txtDate_debut;

    @FXML
    private DatePicker txtDate_fin;

    @FXML
    private TextField txtUser_id;

    @FXML
    private ComboBox<String> cmbVoiture;

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
            LocalDate dateDebut = txtDate_debut.getValue();
            LocalDate dateFin = txtDate_fin.getValue();
            String selectedVoitureId = cmbVoiture.getValue();
            if (dateDebut != null && dateFin != null && selectedVoitureId != null && !selectedVoitureId.isEmpty()) {
                // Retrieve the selected voiture ID from the ComboBox
                String selectedVoiture = cmbVoiture.getSelectionModel().getSelectedItem();
                if (selectedVoiture != null) {
                    int voitureId = Integer.parseInt(selectedVoiture.split("\\[")[1].split("]")[0]);
                    // Use voitureId as needed
                }

                Location p = new Location(dateDebut, dateFin, Integer.parseInt(txtUser_id.getText()), Integer.parseInt(selectedVoitureId), txtTypePaiement.getText(), txtOptions_supp.getText());
                int generatedId = sv.add(p);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Location ajoutée avec succès");
                alert.show();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherL.fxml"));
                try {
                    Parent root = loader.load();
                    AfficherLControllers afficherLControllers = loader.getController();
                    afficherLControllers.setTxtDate_debut(dateDebut);
                    afficherLControllers.setTxtDate_fin(dateFin);
                    afficherLControllers.setTxtUser_id(txtUser_id.getText());
                    afficherLControllers.setTxtVoiture_id(selectedVoitureId);
                    afficherLControllers.setTxtTypePaiement(txtTypePaiement.getText());
                    afficherLControllers.setTxtOptions_supp(txtOptions_supp.getText());
                    afficherLControllers.setTxtIdL(String.valueOf(generatedId));

                    txtOptions_supp.getScene().setRoot(root);
                } catch (IOException e) {
                    // Handle the IOException
                    e.printStackTrace(); // Print the stack trace for debugging
                    // You can also show an error message to the user
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Failed to load FXML file: " + e.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please fill all required fields.");
                alert.show();
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

    public void setTxtVoiture_id(String txtVoiture_id) {
        // Set the selected voiture ID in the ComboBox
        cmbVoiture.setValue(txtVoiture_id);
    }

    public void setTxtTypePaiement(String txtTypePaiement) { this.txtTypePaiement.setText(txtTypePaiement);
    }
    public void setTxtOptions_supp(String txtOptions_supp) { this.txtOptions_supp.setText(txtOptions_supp);
    }

    @FXML
    void initialize() {
        try {
            List<Voiture> voitures = voitureService.recuperer();
            // Populate ComboBox with marque and set the ID as the value
            ObservableList<String> voitureMarques = FXCollections.observableArrayList(voitures.stream()
                    .map(Voiture::getMarque)
                    .collect(Collectors.toList()));
            cmbVoiture.setItems(voitureMarques);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add a listener to cmbVoiture ComboBox
        cmbVoiture.setOnAction(event -> calculateTotalPrice());

        // Add listeners to date pickers
        txtDate_debut.setOnAction(event -> calculateTotalPrice());
        txtDate_fin.setOnAction(event -> calculateTotalPrice());
    }

    private void calculateTotalPrice() {
        String selectedMarque = cmbVoiture.getValue();
        if (selectedMarque != null && !selectedMarque.isEmpty()) {
            try {
                // Retrieve the voiture ID based on the selected marque
                int voitureId = voitureService.getVoitureIdByMarque(selectedMarque);
                int prixJ = voitureService.getPrixJById(voitureId);
                LocalDate dateDebut = txtDate_debut.getValue();
                LocalDate dateFin = txtDate_fin.getValue();
                if (dateDebut != null && dateFin != null) {
                    long numberOfDays = ChronoUnit.DAYS.between(dateDebut, dateFin);
                    int totalPrice = prixJ * (int) numberOfDays;
                    txtTotalPrice.setText(String.valueOf(totalPrice));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}