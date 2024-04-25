package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Location;
import services.LocationService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ModifierLocationController {

    LocationService sv = new LocationService();

    private Location locationToUpdate;

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
    private TextField txtIdL;



    public void setTxtDate_debut(LocalDate txtDate_debut) {
        this.txtDate_debut.setValue(txtDate_debut);
    }

    public void setTxtDate_fin(LocalDate txtDate_fin) {
        this.txtDate_fin.setValue(txtDate_fin);
    }

    public void setTxtUser_id(String txtUser_id) {
        this.txtUser_id.setText(txtUser_id);
    }

    public void setTxtVoiture_id(String txtVoiture_id) {
        this.txtVoiture_id.setText(txtVoiture_id);
    }

    public void setTxtTypePaiement(String txtTypePaiement) {
        this.txtTypePaiement.setText(txtTypePaiement);
    }

    public void setTxtOptions_supp(String txtOptions_supp) {
        this.txtOptions_supp.setText(txtOptions_supp);
    }

    public void setTxtIdL(String txtIdL) {
        this.txtIdL.setText(txtIdL);
    }

    public DatePicker getTxtDate_debut() {
        return txtDate_debut;
    }
    public DatePicker getTxtDate_fin() {
        return txtDate_fin;
    }

    @FXML
    void ModifierAction() {
        try {
            Location existingVoiture = new Location();


            existingVoiture.setTypePaiement(txtTypePaiement.getText());
            existingVoiture.setOptions_supp(txtOptions_supp.getText());
            existingVoiture.setDate_debut(txtDate_debut.getValue());
            existingVoiture.setDate_fin(txtDate_fin.getValue());
            existingVoiture.setUser_id((int) Long.parseLong(txtUser_id.getText()));
            existingVoiture.setVoiture_id((int) Long.parseLong(txtVoiture_id.getText()));
            existingVoiture.setId_location(Integer.parseInt(txtIdL.getText()));

            // Call the modifier method with the modified Transport object
            sv.modifier(existingVoiture);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setContentText("Location modifiée avec succès");
            alert.show();

            // Reload the view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherL.fxml"));

            try {
                Parent root = loader.load();

                AfficherLControllers afficherLControllers = loader.getController();
                afficherLControllers.setTxtDate_debut(txtDate_debut.getValue());
                afficherLControllers.setTxtDate_fin(txtDate_fin.getValue());
                afficherLControllers.setTxtUser_id(txtUser_id.getText());
                afficherLControllers.setTxtVoiture_id(txtVoiture_id.getText());
                afficherLControllers.setTxtTypePaiement(txtTypePaiement.getText());
                afficherLControllers.setTxtOptions_supp(txtOptions_supp.getText());
                afficherLControllers.setTxtIdL(txtIdL.getText());
                txtDate_fin.getScene().setRoot(root);

            } catch (IOException e) {
                // Handle IO exception
                e.printStackTrace();
            }
        }catch (SQLException e) {
            // Handle SQL exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    void SupprimerAction() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this Location?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {

                sv.supprimer(Integer.parseInt(txtIdL.getText()));

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setContentText("Location supprimé avec succès");
                alert.show();

                // Reload the view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterLocation.fxml"));

                try {
                    Parent root = loader.load();

                    AjouterLocationController a = loader.getController();
                    a.getTxtDate_debut().setValue(null);
                    a.getTxtDate_fin().setValue(null);
                    a.setTxtUser_id("");
                    a.setTxtVoiture_id("");
                    a.setTxtTypePaiement("");
                    a.setTxtOptions_supp("");

                    txtDate_fin.getScene().setRoot(root);

                } catch (IOException e) {
                    // Handle IO exception
                    e.printStackTrace();
                }
            }catch (SQLException e) {
                // Handle SQL exception
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }

    public void setTextFields(Location R){
        txtIdL.setText(String.valueOf(R.getId_location()));
        txtTypePaiement.setText(R.getTypePaiement());
        txtOptions_supp.setText(R.getOptions_supp());
        txtDate_debut.setValue(R.getDate_debut());
        txtDate_fin.setValue(R.getDate_fin());
        txtUser_id.setText(String.valueOf(R.getUser_id()));
        txtVoiture_id.setText(String.valueOf(R.getVoiture_id()));
    }
}

