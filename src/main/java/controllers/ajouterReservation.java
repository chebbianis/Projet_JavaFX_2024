package controllers;

import entities.Hotel;
import entities.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import services.ServiceReservation;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.function.UnaryOperator;

public class ajouterReservation {

    @FXML
    private Button btnreserver;

    @FXML
    private Button button_Submit;

    @FXML
    private DatePicker dateArriveeRev;

    @FXML
    private DatePicker dateDepartRev;

    @FXML
    private TextField idClientRev;

    @FXML
    private TextField nbreAdulteRev;

    @FXML
    private TextField nbreChambreRev;

    @FXML
    private TextField nbreEnfantRev;

    @FXML
    private ComboBox<String> nomHotelRev;

    @FXML
    private TextField prixTotalRev;

    @FXML
    private TextField typeRRev;

    @FXML
    private TextField typeChambreRev;
    private void populateComboBox() {
        ObservableList<String> nomHotelList = FXCollections.observableArrayList();

        try {
            String url = "jdbc:mysql://localhost:3306/tunvista";
            String username = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT id_h, nom_hotel FROM hotel";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String nomHotel = resultSet.getString("nom_hotel");
                nomHotelList.add(nomHotel);
            }
            nomHotelRev.setItems(nomHotelList);
            resultSet.close();

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        idClientRev.setTextFormatter(createTextFormatter("[0-9]+"));
        prixTotalRev.setTextFormatter(createTextFormatter("[0-9]+"));
        nbreChambreRev.setTextFormatter(createTextFormatter("[0-9]+"));
        nbreAdulteRev.setTextFormatter(createTextFormatter("[0-9]+"));
        nbreEnfantRev.setTextFormatter(createTextFormatter("[0-9]+"));
        typeChambreRev.setTextFormatter(createTextFormatter("[a-zA-Z\\s]+")); // Autorise les caractères alphanumériques et les espaces
        typeRRev.setTextFormatter(createTextFormatter("[a-zA-Z\\s]+")); // Autorise les caractères alphabétiques et les espaces
        populateComboBox();
    }
    private TextFormatter<String> createTextFormatter(String pattern) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(pattern) || newText.isEmpty()) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(filter);
    }
    @FXML
    public void reserver(ActionEvent event) {
        LocalDate dateArrivee = dateArriveeRev.getValue();
        LocalDate dateDepart = dateDepartRev.getValue();
        // Vérifier si les dates sont sélectionnées
        if (dateArrivee == null || dateDepart == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez sélectionner les dates d'arrivée et de départ.");
            return;
        }

        // Vérifier si les dates sont postérieures à aujourd'hui
        LocalDate today = LocalDate.now();
        if (dateArrivee.isBefore(today) || dateDepart.isBefore(today)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Les dates doivent être postérieures à aujourd'hui.");
            return;
        }

        // Vérifier si la date de départ est après la date d'arrivée
        if (dateDepart.isBefore(dateArrivee)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "La date de départ doit être après la date d'arrivée.");
            return;
        }

        try {
            if (idClientRev.getText().isEmpty() || prixTotalRev.getText().isEmpty() || dateArrivee == null || dateDepart == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez remplir tous les champs.");
            } else {
                int idClient = Integer.parseInt(idClientRev.getText());
                int prixTotal = Integer.parseInt(prixTotalRev.getText());
                int nbreChambre = Integer.parseInt(nbreChambreRev.getText());
                int nbreAdulte = Integer.parseInt(nbreAdulteRev.getText());
                int nbreEnfant = Integer.parseInt(nbreEnfantRev.getText());
                String selectedNomHotel = nomHotelRev.getSelectionModel().getSelectedItem();
                int idH = getNomHotel(selectedNomHotel);

                if (idH == -1) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", null, "Le nom de l'hôtel sélectionné est invalide.");
                    return;
                }

                Reservation reservation = new Reservation();
                reservation.setId_client(idClient);
                reservation.setPrix_total(prixTotal);
                reservation.setDate_arrivee(dateArrivee);
                reservation.setDate_depart(dateDepart);
                reservation.setNbre_chambre(nbreChambre);
                reservation.setType_chambre(typeChambreRev.getText());
                reservation.setNbre_adulte(nbreAdulte);
                reservation.setNbre_enfant(nbreEnfant);
                reservation.setIdH(idH);
                reservation.setType_r(typeRRev.getText());

                ServiceReservation serviceReservation = new ServiceReservation();
                serviceReservation.ajouter(reservation);

                showAlert(Alert.AlertType.INFORMATION, "Succès", null, "La réservation a été ajoutée avec succès.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez entrer des valeurs numériques valides.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    @FXML
    void naviguezVersAffichage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReservation.fxml"));
            idClientRev.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }




    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private int getNomHotel(String nom) throws SQLException {
        int id_h = -1;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id_h FROM hotel WHERE nom_hotel = ?")) {
            preparedStatement.setString(1, nom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id_h = resultSet.getInt("id_h");
                }
            }
        }
        return id_h;
    }
}

