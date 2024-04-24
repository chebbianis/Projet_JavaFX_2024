package com.example.pidevjava.Contollers;

import com.example.pidevjava.Entities.Voyage;
import com.example.pidevjava.service.serviceVoyage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VoyageController {
    @FXML
    private TableColumn<?, ?> col_prix;

    @FXML
    private TableColumn<?, ?> col_programme;

    @FXML
    private TableColumn<?, ?> col_datearrive;

    @FXML
    private TableColumn<?, ?> col_datedepart;

    @FXML
    private TableColumn<?, ?> col_destination;

    @FXML
    private TableView<Voyage> table_voyage;

    @FXML
    private TableColumn<Voyage, Integer> col_id;

    @FXML
    private DatePicker tf_dateA;

    @FXML
    private DatePicker tf_dated;

    @FXML
    private TextField tf_des;

    @FXML
    private TextField tf_prix;

    @FXML
    private TextField tf_prog;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_update;

    @FXML
    private Button btn_insert;

    @FXML

    private final serviceVoyage serviceVoyage = new serviceVoyage();

    public void initialize() {
        // Initialisation des colonnes et chargement des données
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_programme.setCellValueFactory(new PropertyValueFactory<>("programme"));
        col_datedepart.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
        col_datearrive.setCellValueFactory(new PropertyValueFactory<>("dateArrive"));
        col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        col_destination.setCellValueFactory(new PropertyValueFactory<>("destination"));

        loadVoyageData(); // Chargement des données dans la TableView

        // Ajout de l'événement pour sélectionner un voyage dans le formulaire
        table_voyage.setOnMouseClicked(event -> selectVoyage(event));

        btn_delete.setOnAction(event -> supprimerVoyage());
        btn_update.setOnAction(event -> modifierVoyage(event));
        btn_insert.setOnAction(event -> ajouterVoyage(event));
    }

    private void selectVoyage(MouseEvent event) {
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Voyage selectedVoyage = table_voyage.getSelectionModel().getSelectedItem();
            if (selectedVoyage != null) {
                // Affichage des détails du voyage sélectionné dans le formulaire
                tf_prog.setText(selectedVoyage.getProgramme());
                tf_dated.setValue(selectedVoyage.getDateDepart());
                tf_dateA.setValue(selectedVoyage.getDateArrive());
                tf_prix.setText(selectedVoyage.getPrix());
                tf_des.setText(selectedVoyage.getDestination());
            }
        }
    }

    private ObservableList<Voyage> getVoyageList() {
        ObservableList<Voyage> voyages = FXCollections.observableArrayList();
        try {
            voyages.addAll(serviceVoyage.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }

    private void loadVoyageData() {
        ObservableList<Voyage> voyages = getVoyageList();
        table_voyage.setItems(voyages);
    }

    @FXML
    void ajouterVoyage(ActionEvent event) {
        String programmeValue = tf_prog.getText();
        LocalDate dateDepartValue = tf_dated.getValue();
        LocalDate dateArriveValue = tf_dateA.getValue();
        String prixValue = tf_prix.getText();
        String destinationValue = tf_des.getText();

        // Vérification des valeurs saisies
        if (programmeValue.isEmpty() || dateDepartValue == null || dateArriveValue == null || prixValue.isEmpty() || destinationValue.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        if (programmeValue.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Le champ programme ne doit pas dépasser 100 caractères.");
            return;
        }


        if (!isValidDate(dateDepartValue) || !isValidDate(dateArriveValue) || dateDepartValue.isAfter(dateArriveValue)) {
            showAlert(Alert.AlertType.WARNING, "Dates incorrectes", "Les dates doivent être au format jj/mm/aaaa et la date de départ doit être antérieure à la date d'arrivée.");
            return;
        }


        if (!isValidPrice(prixValue)) {
            showAlert(Alert.AlertType.WARNING, "Prix incorrect", "Le champ prix ne doit contenir que des chiffres.");
            return;
        }

        Voyage voyage = new Voyage(programmeValue, dateDepartValue, dateArriveValue, prixValue, destinationValue);

        try {

            serviceVoyage.ajouter(voyage);

            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Voyage ajouté avec succès.");

            loadVoyageData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du voyage : " + e.getMessage());
        }
    }

    @FXML
    void modifierVoyage(ActionEvent event) {
        Voyage voyage = table_voyage.getSelectionModel().getSelectedItem();
        if (voyage != null) {
            String programme = tf_prog.getText();
            LocalDate dateDepart = tf_dated.getValue();
            LocalDate dateArrive = tf_dateA.getValue();
            String prix = tf_prix.getText();
            String destination = tf_des.getText();

            voyage.setProgramme(programme);
            voyage.setDateDepart(dateDepart);
            voyage.setDateArrive(dateArrive);
            voyage.setPrix(prix);
            voyage.setDestination(destination);

            try {
                serviceVoyage.modifier(voyage);
                loadVoyageData();

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Voyage modifié avec succès");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun voyage sélectionné", "Veuillez sélectionner un voyage à modifier.");
        }
    }

    private void supprimerVoyage() {
        Voyage voyage = table_voyage.getSelectionModel().getSelectedItem();
        if (voyage != null) {
            try {
                serviceVoyage.supprimer(voyage);
                loadVoyageData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le voyage a été supprimé avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression du voyage : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun voyage sélectionné", "Veuillez sélectionner un voyage.");
        }
    }

    private boolean isValidDate(LocalDate date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formatter.format(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidPrice(String price) {
        return price.matches("\\d+");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
