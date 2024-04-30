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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AjouterAnnonceController implements Initializable {

    private final AnnonceService us = new AnnonceService();

    @FXML
    private TextField titre_aTF;

    @FXML
    private TextField description_aTF;

    @FXML
    private TextField ville_aTF;

    @FXML
    private TextField date_debutTF;

    @FXML
    private TextField nb_jourTF;

    @FXML
    private TextField date_finTF;
    @FXML
    private Button btn_ajouter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_ajouter.setOnAction(event -> {
            try {
                if (validateInputs()) {
                    ajouterAnnonceController(new ActionEvent());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer l'exception (afficher un message d'erreur, par exemple)
            }
        });
    }

    private boolean validateInputs() {
        String titre = titre_aTF.getText();
        String description = description_aTF.getText();
        String ville = ville_aTF.getText();
        String startDateString = date_debutTF.getText();
        String endDateString = date_finTF.getText();
        String nbJour = nb_jourTF.getText();

        // Validate Titre, Description, and Ville
        if (!validateStringInput(titre, "Titre")) return false;
        if (!validateStringInput(description, "Description")) return false;
        if (!validateStringInput(ville, "Ville")) return false;

        // Validate Date Debut
        if (!validateDateInput(startDateString, "Date Debut")) return false;

        // Validate Nombre de Jours
        if (!validateIntegerInput(nbJour, "Nombre de Jours")) return false;

        // Validate Date Fin
        if (!validateDateInput(endDateString, "Date Fin")) return false;

        return true;
    }

    private boolean validateStringInput(String input, String fieldName) {
        if (input.isEmpty() || input.length() < 2 || input.length() > 30) {
            showAlert("Erreur", fieldName + " doit contenir entre 2 et 30 caractères.");
            return false;
        }
        return true;
    }

    private boolean validateDateInput(String input, String fieldName) {
        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", input)) {
            showAlert("Erreur", fieldName + " doit être au format yyyy-MM-dd.");
            return false;
        }
        return true;
    }

    private boolean validateIntegerInput(String input, String fieldName) {
        if (!input.matches("\\d+")) {
            showAlert("Erreur", fieldName + " doit être un nombre entier positif.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    void ajouterAnnonceController(ActionEvent event) throws SQLException {
        if(titre_aTF.getText().isEmpty()) {
            System.out.println("Name is empty");
        } else if (!nb_jourTF.getText().matches("\\d+")) {
            System.out.println("Invalid number of days");
        } else {
            String startDateString = date_debutTF.getText();
            int numberOfDays = Integer.parseInt(nb_jourTF.getText());

            LocalDate startDate = LocalDate.parse(startDateString, DateTimeFormatter.ISO_DATE);
            LocalDate endDate = startDate.plusDays(numberOfDays);

            String endDateString = endDate.format(DateTimeFormatter.ISO_DATE);
            date_finTF.setText(endDateString);

            us.addAnnonce(new Annonce(titre_aTF.getText(), description_aTF.getText(), ville_aTF.getText(), startDateString, nb_jourTF.getText(), endDateString));
        }
    }


}
