package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.Annonce;
import services.AnnonceService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierAnnonceController implements Initializable {

    private Annonce annonce;
    private final AnnonceService annonceService = new AnnonceService();

    @FXML
    private TextField titre_aTF;

    @FXML
    private TextField description_aTF;

    @FXML
    private TextField ville_aTF;

    @FXML
    private TextField date_debutTF;

    @FXML
    private TextField date_finTF;

    @FXML
    private TextField nb_jourTF;

    @FXML
    private Button btn_modifier;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // No need to initialize fields here, as we'll set them using a separate method
    }

    // Setter method to set the Annonce object
    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
        titre_aTF.setText(annonce.getTitre_a());
        description_aTF.setText(annonce.getDescription_a());
        ville_aTF.setText(annonce.getVille_a());
        date_debutTF.setText(annonce.getDate_debut());
        date_finTF.setText(annonce.getDate_fin());
        nb_jourTF.setText(annonce.getNb_jour());
    }

    @FXML
    private void modifierAction(ActionEvent event) throws SQLException {
        // Update the fields of the existing announcement object
        if (annonce != null) {
            annonce.setTitre_a(titre_aTF.getText());
        annonce.setDescription_a(description_aTF.getText());
        annonce.setVille_a(ville_aTF.getText());
        annonce.setDate_debut(date_debutTF.getText());
        annonce.setDate_fin(date_finTF.getText());
        annonce.setNb_jour(nb_jourTF.getText());

        // Call the service method to update the announcement in the database
        annonceService.updateAnnonce(annonce);


        // Show a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modification Successful");
        alert.setHeaderText(null);
        alert.setContentText("The announcement has been successfully modified.");
        alert.showAndWait();
    }else {
        System.out.println("Annonce is null. Please set the annonce before modifying.");
        // Handle the case where annonce is null
    }
    }
}
