package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Voiture;
import services.VoitureService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ModifierVoitureController {

    VoitureService sv = new VoitureService();

    private Voiture transportToUpdate;

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

    @FXML
    private TextField txtId;



    public void setTxtVoiture_Marque(String txtVoiture_Marque) { this.txtVoiture_Marque.setText(txtVoiture_Marque);
    }

    public void setTxtAnnee(LocalDate txtAnnee) {
        this.txtAnnee.setValue(txtAnnee);
    }

    public void setTxtPrix_j(String txtPrix_j) {
        this.txtPrix_j.setText(txtPrix_j);
    }

    public void setTxtKilometrage(String txtKilometrage) {
        this.txtKilometrage.setText(txtKilometrage);
    }

    public void setTxtNbrPlaceset(String txtNbrPlaces) {
        this.txtNbrPlaces.setText(txtNbrPlaces);
    }

    public void setTxtId(String txtId) {
        this.txtId.setText(txtId);
    }

    public DatePicker getTxtAnnee() {
        return txtAnnee;
    }

    @FXML
    void ModifierAction() {
        try {
            Voiture existingVoiture = new Voiture();


            existingVoiture.setMarque(txtVoiture_Marque.getText());
            existingVoiture.setAnnee(txtAnnee.getValue());
            existingVoiture.setPrix_j((int) Long.parseLong(txtPrix_j.getText()));
            existingVoiture.setKilometrage(Integer.parseInt(txtKilometrage.getText()));
            existingVoiture.setNbrPlaces(Integer.parseInt(txtNbrPlaces.getText()));
            existingVoiture.setId_voiture(Integer.parseInt(txtId.getText()));

            // Call the modifier method with the modified Transport object
            sv.modifier(existingVoiture);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setContentText("Voiture modifiée avec succès");
            alert.show();

            // Reload the view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherV.fxml"));

            try {
                Parent root = loader.load();

                AfficherVControllers afficherVControllers = loader.getController();
                afficherVControllers.setTxtAnnee(txtAnnee.getValue());
                afficherVControllers.setTxtVoiture_Marque(txtVoiture_Marque.getText());
                afficherVControllers.setTxtPrix_j(txtPrix_j.getText());
                afficherVControllers.setTxtKilometrage(txtKilometrage.getText());
                afficherVControllers.setTxtNbrPlaces(txtNbrPlaces.getText());
                afficherVControllers.setTxtId(txtId.getText());
                txtAnnee.getScene().setRoot(root);

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
        confirmationAlert.setContentText("Are you sure you want to delete this Voiture?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {

                sv.supprimer(Integer.parseInt(txtId.getText()));

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setContentText("Voiture supprimé avec succès");
                alert.show();

                // Reload the view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterVoiture.fxml"));

                try {
                    Parent root = loader.load();

                    AjouterVoitureController a = loader.getController();
                    a.getTxtAnnee().setValue(null);
                    a.setTxtVoiture_Marque("");
                    a.setTxtPrix_j("");
                    a.setTxtKilometrage("");
                    a.setTxtNbrPlaces("");

                    txtAnnee.getScene().setRoot(root);

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

    public void setTextFields(Voiture R){
        txtId.setText(String.valueOf(R.getId_voiture()));
        txtVoiture_Marque.setText(R.getMarque());
        txtPrix_j.setText(String.valueOf(R.getPrix_j()));
        txtAnnee.setValue(R.getAnnee());
        txtKilometrage.setText(String.valueOf(R.getKilometrage()));
        txtNbrPlaces.setText(String.valueOf(R.getNbrPlaces()));
    }
}



