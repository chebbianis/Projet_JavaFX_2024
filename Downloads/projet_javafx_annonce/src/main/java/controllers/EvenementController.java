package controllers;

import models.Evenement;
import services.EvenementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class EvenementController {
    @FXML
    private Button btn_ajouter;
    @FXML
    private Button btn_modifier;
    @FXML
    private Button btn_supprimer;
    @FXML
    private Button btn_search;
    @FXML
    private TextField tf_titre_e;
    @FXML
    private TextField tf_description_e;
    @FXML
    private TextField tf_ville_e;
    @FXML
    private DatePicker tf_date_debut_e;
    @FXML
    private TextField tf_date_fin_e;
    @FXML
    private TextField tf_nb_jour_e;
    @FXML
    private TableView<Evenement> tvEvenements;
    @FXML
    private TableColumn<Evenement, String> colIdEvenement;
    @FXML
    private TableColumn<Evenement, String> colTitreEvenement;
    @FXML
    private TableColumn<Evenement, String> colDescriptionEvenement;
    @FXML
    private TableColumn<Evenement, String> colVilleEvenement;
    @FXML
    private TableColumn<Evenement, LocalDate> colDateDebutE;
    @FXML
    private TableColumn<Evenement, LocalDate> colDateFinE;
    @FXML
    private TableColumn<Evenement, String> colNbJourE;
    @FXML
    private TextField tf_search;

    private final EvenementService EvenementService = new EvenementService();

    public void initialize() throws SQLException {
        // Initialisation des colonnes et chargement des données
        colIdEvenement.setCellValueFactory(new PropertyValueFactory<>("idEvenement"));
        colTitreEvenement.setCellValueFactory(new PropertyValueFactory<>("titreE"));
        colDescriptionEvenement.setCellValueFactory(new PropertyValueFactory<>("descriptionE"));
        colVilleEvenement.setCellValueFactory(new PropertyValueFactory<>("villeE"));
        colDateDebutE.setCellValueFactory(new PropertyValueFactory<>("dateDebutE"));
        colNbJourE.setCellValueFactory(new PropertyValueFactory<>("nbJourE"));
        colDateFinE.setCellValueFactory(new PropertyValueFactory<>("dateFinE"));



        tf_date_debut_e.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(newValue, tf_nb_jour_e.getText());
        });
        tf_nb_jour_e.textProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(tf_date_debut_e.getValue(), newValue);
        });

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchEvenement(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        //tf_search.setTextFormatter(textFormatter);

        loadEvenementData(); // Chargement des données dans la TableView

        // Ajout de l'événement pour sélectionner une evneement dans le formulaire
        tvEvenements.setOnMouseClicked(event -> selectEvenement(event));

        btn_supprimer.setOnAction(event -> supprimerEvenement());
        btn_modifier.setOnAction(event -> modifierEvenement(event));
        btn_ajouter.setOnAction(event -> ajouterEvenement(event));

    }

    private void updateDateFin(LocalDate dateDebutE, String nbJourE) {
        if (dateDebutE != null && !nbJourE.isEmpty()) {
            try {
                int nbJourInt = Integer.parseInt(nbJourE);
                String dateFinE = String.valueOf(dateDebutE.plusDays(nbJourInt));
                tf_date_fin_e.setText(dateFinE);
            } catch (NumberFormatException e) {
                // Handle invalid nbJour input
                tf_date_fin_e.setText(null);
            }
        } else {
            tf_date_fin_e.setText(null);
        }
    }


    private void selectEvenement(MouseEvent event) {
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Evenement selectedEvenement = tvEvenements.getSelectionModel().getSelectedItem();
            if (selectedEvenement != null) {
                // Affichage des détails de l'evenement sélectionnée dans le formulaire
                tf_titre_e.setText(selectedEvenement.getTitreE());
                tf_description_e.setText(selectedEvenement.getDescriptionE());
                tf_ville_e.setText(selectedEvenement.getVilleE());
                tf_date_debut_e.setValue(selectedEvenement.getDateDebutE());
                tf_date_fin_e.setText(selectedEvenement.getDateFinE());
                tf_nb_jour_e.setText(String.valueOf(selectedEvenement.getNbJourE()));
            }
        }
    }

    private ObservableList<Evenement> getEvenementList() throws SQLException {
        ObservableList<Evenement> evenements = FXCollections.observableArrayList();
        evenements.addAll(EvenementService.afficher());
        return evenements;
    }

    private void loadEvenementData() throws SQLException {
        ObservableList<Evenement> evenements = getEvenementList();
        tvEvenements.setItems(evenements);
    }

    @FXML
    void ajouterEvenement(ActionEvent event) {
        String titreValue = tf_titre_e.getText();
        String descriptionValue = tf_description_e.getText();
        String villeValue = tf_ville_e.getText();
        LocalDate dateDebutValue = tf_date_debut_e.getValue();
        String dateFinValue = tf_date_fin_e.getText();
        String nbJourValue = tf_nb_jour_e.getText();

        // Vérification des valeurs saisies
        if (titreValue.isEmpty() || descriptionValue.isEmpty() || villeValue.isEmpty() || dateDebutValue == null || dateFinValue == null || nbJourValue.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        if (titreValue.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Le champ titre ne doit pas dépasser 100 caractères.");
            return;
        }

        if (!isValidDate(dateDebutValue) ) {
            showAlert(Alert.AlertType.WARNING, "Dates incorrectes", "Les dates doivent être au format jj/mm/aaaa et la date de début doit être antérieure à la date de fin.");
            return;
        }

        if (!isValidNbJour(nbJourValue)) {
            showAlert(Alert.AlertType.WARNING, "Nombre de jours incorrect", "Le champ nombre de jours doit contenir des chiffres uniquement.");
            return;
        }

        Evenement evenement = new Evenement(titreValue, descriptionValue, villeValue, dateDebutValue, nbJourValue, dateFinValue);

        try {
            EvenementService.ajouter(evenement);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Evenement ajoutée avec succès.");
            loadEvenementData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'evenement : " + e.getMessage());
        }
    }
    @FXML
    void modifierEvenement(ActionEvent event) {
        Evenement evenement = tvEvenements.getSelectionModel().getSelectedItem();
        if (evenement != null) {
            String titre = tf_titre_e.getText();
            String description = tf_description_e.getText();
            String ville = tf_ville_e.getText();

            LocalDate dateDebut = tf_date_debut_e.getValue();
            String nbJour = tf_nb_jour_e.getText();
            String dateFin = tf_date_fin_e.getText();

            evenement.setTitreE(titre);
            evenement.setDescriptionE(description);
            evenement.setVilleE(ville);
            evenement.setDateDebutE(dateDebut);
            evenement.setDateFinE(LocalDate.parse(dateFin));
            evenement.setNbJourE(nbJour);

            try {
                EvenementService.modifier(evenement);
                loadEvenementData();

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Evenement modifiée avec succès");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune evenement sélectionnée", "Veuillez sélectionner une evenement à modifier.");
        }
    }
    private void supprimerEvenement() {
        Evenement evenement = tvEvenements.getSelectionModel().getSelectedItem();
        if (evenement != null) {
            try {
                EvenementService.supprimer(evenement);
                loadEvenementData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'evenement a été supprimée avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression de l'evenement : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune evenement sélectionnée", "Veuillez sélectionner une evenement.");
        }
    }

    private boolean isValidDate(LocalDate date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM/DD");
            formatter.format(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidNbJour(String nbJour) {
        return nbJour.matches("\\d+");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void searchEvenement(String searchString) throws SQLException {
        if (searchString.isEmpty()) {
            // If search text is empty, reload all evenement
            loadEvenementData();
        } else {
            List<Evenement> filteredEvenement = EvenementService.afficher().stream()
                    .filter(evenement -> evenement.getTitreE().toLowerCase().startsWith(searchString.toLowerCase()))
                    .collect(Collectors.toList());

            ObservableList<Evenement> observableFilteredEvenement = FXCollections.observableArrayList(filteredEvenement);
            tvEvenements.setItems(observableFilteredEvenement);
        }
    }


}