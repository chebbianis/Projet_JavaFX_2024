package com.example.pidevjava.Contollers;

import com.example.pidevjava.Entities.Voyage;
import com.example.pidevjava.service.serviceVoyage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;

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


        // Load data into TableView
        loadVoyageData();

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

            showAlert(Alert.AlertType.WARNING, "ajout imposible", "Veuillez remplir tous les champs.");

            return;
        }

        // Création du voyage
        Voyage voyage = new Voyage(programmeValue, dateDepartValue, dateArriveValue, prixValue, destinationValue);

        try {
            // Ajout du voyage à la base de données
            serviceVoyage serviceVoyage = new serviceVoyage();
            serviceVoyage.ajouter(voyage);

            // Affichage d'un message de succès
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setContentText("Voyage ajouté avec succès.");
            alert.showAndWait();

            // Rafraîchissement de la TableView
            loadVoyageData();
        } catch (SQLException e) {
            // Affichage d'un message d'erreur en cas d'échec
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de l'ajout du voyage : " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void modifierVoyage(ActionEvent event) {
        Voyage voyage = table_voyage.getSelectionModel().getSelectedItem();
        if (voyage != null) {
            // Récupérer les valeurs des champs texte
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

            serviceVoyage serviceVoyage = new serviceVoyage();
            try {
                serviceVoyage.modifier(voyage);
                loadVoyageData(); // Mettre à jour la TableView après modification

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succès");
                alert.setContentText("Voyage modifié avec succès");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun voyage sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un voyage à modifier.");
            alert.showAndWait();
        }
    }


    private void supprimerVoyage() {
        Voyage voyage = table_voyage.getSelectionModel().getSelectedItem();
        if (voyage != null) {
            try {
                serviceVoyage.supprimer(voyage);
                loadVoyageData(); // Mettre à jour la TableView après suppression
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le voyage a été supprimé avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression du voyage : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun voyage sélectionné", "Veuillez sélectionner un voyage.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
