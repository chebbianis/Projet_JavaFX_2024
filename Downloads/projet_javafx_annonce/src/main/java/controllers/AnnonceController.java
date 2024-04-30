package controllers;

import models.Annonce;
import services.AnnonceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AnnonceController {
    @FXML
    private Button btn_ajouter;
    @FXML
    private Button btn_modifier;
    @FXML
    private Button btn_supprimer;
    @FXML
    private Button btn_search;
    @FXML
    private TextField tf_titre_a;
    @FXML
    private TextField tf_description_a;
    @FXML
    private TextField tf_ville_a;
    @FXML
    private DatePicker tf_date_debut_a;
    @FXML
    private TextField tf_date_fin_a;
    @FXML
    private TextField tf_nb_jour_a;
    @FXML
    private TableView<Annonce> tvAnnonces;
    @FXML
    private TableColumn<Annonce, String> colIdAnnonce;
    @FXML
    private TableColumn<Annonce, String> colTitreAnnonce;
    @FXML
    private TableColumn<Annonce, String> colDescriptionAnnonce;
    @FXML
    private TableColumn<Annonce, String> colVilleAnnonce;
    @FXML
    private TableColumn<Annonce, LocalDate> colDateDebutA;
    @FXML
    private TableColumn<Annonce, LocalDate> colDateFinA;
    @FXML
    private TableColumn<Annonce, String> colNbJourA;
    @FXML
    private TextField tf_search;

    private final AnnonceService AnnonceService = new AnnonceService();

    public void initialize() throws SQLException {
        // Initialisation des colonnes et chargement des données
        colIdAnnonce.setCellValueFactory(new PropertyValueFactory<>("idAnnonce"));
        colTitreAnnonce.setCellValueFactory(new PropertyValueFactory<>("titreA"));
        colDescriptionAnnonce.setCellValueFactory(new PropertyValueFactory<>("descriptionA"));
        colVilleAnnonce.setCellValueFactory(new PropertyValueFactory<>("villeA"));
        colDateDebutA.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colNbJourA.setCellValueFactory(new PropertyValueFactory<>("nbJour"));
        colDateFinA.setCellValueFactory(new PropertyValueFactory<>("dateFin"));



        tf_date_debut_a.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(newValue, tf_nb_jour_a.getText());
        });
        tf_nb_jour_a.textProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(tf_date_debut_a.getValue(), newValue);
        });

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchAnnonce(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        //tf_search.setTextFormatter(textFormatter);

        loadAnnonceData(); // Chargement des données dans la TableView

        // Ajout de l'événement pour sélectionner une annonce dans le formulaire
        tvAnnonces.setOnMouseClicked(event -> selectAnnonce(event));

        btn_supprimer.setOnAction(event -> supprimerAnnonce());
        btn_modifier.setOnAction(event -> modifierAnnonce(event));
        btn_ajouter.setOnAction(event -> ajouterAnnonce(event));

    }

    private void updateDateFin(LocalDate dateDebut, String nbJour) {
        if (dateDebut != null && !nbJour.isEmpty()) {
            try {
                int nbJourInt = Integer.parseInt(nbJour);
                String dateFin = String.valueOf(dateDebut.plusDays(nbJourInt));
                tf_date_fin_a.setText(dateFin);
            } catch (NumberFormatException e) {
                // Handle invalid nbJour input
                tf_date_fin_a.setText(null);
            }
        } else {
            tf_date_fin_a.setText(null);
        }
    }


    private void selectAnnonce(MouseEvent event) {
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Annonce selectedAnnonce = tvAnnonces.getSelectionModel().getSelectedItem();
            if (selectedAnnonce != null) {
                // Affichage des détails de l'annonce sélectionnée dans le formulaire
                tf_titre_a.setText(selectedAnnonce.getTitreA());
                tf_description_a.setText(selectedAnnonce.getDescriptionA());
                tf_ville_a.setText(selectedAnnonce.getVilleA());
                tf_date_debut_a.setValue(selectedAnnonce.getDateDebut());
                tf_date_fin_a.setText(selectedAnnonce.getDateFin());
                tf_nb_jour_a.setText(String.valueOf(selectedAnnonce.getNbJour()));
            }
        }
    }

    private ObservableList<Annonce> getAnnonceList() throws SQLException {
        ObservableList<Annonce> annonces = FXCollections.observableArrayList();
        annonces.addAll(AnnonceService.afficher());
        return annonces;
    }

    private void loadAnnonceData() throws SQLException {
        ObservableList<Annonce> annonces = getAnnonceList();
        tvAnnonces.setItems(annonces);
    }

    @FXML
    void ajouterAnnonce(ActionEvent event) {
        String titreValue = tf_titre_a.getText();
        String descriptionValue = tf_description_a.getText();
        String villeValue = tf_ville_a.getText();
        LocalDate dateDebutValue = tf_date_debut_a.getValue();
        String dateFinValue = tf_date_fin_a.getText();
        String nbJourValue = tf_nb_jour_a.getText();

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

        Annonce annonce = new Annonce(titreValue, descriptionValue, villeValue, dateDebutValue, nbJourValue, dateFinValue);

        try {
            AnnonceService.ajouter(annonce);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Annonce ajoutée avec succès.");
            loadAnnonceData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'annonce : " + e.getMessage());
        }
    }
    @FXML
    void modifierAnnonce(ActionEvent event) {
        Annonce annonce = tvAnnonces.getSelectionModel().getSelectedItem();
        if (annonce != null) {
            String titre = tf_titre_a.getText();
            String description = tf_description_a.getText();
            String ville = tf_ville_a.getText();

            LocalDate dateDebut = tf_date_debut_a.getValue();
            String nbJour = tf_nb_jour_a.getText();
            String dateFin = tf_date_fin_a.getText();

            annonce.setTitreA(titre);
            annonce.setDescriptionA(description);
            annonce.setVilleA(ville);
            annonce.setDateDebut(dateDebut);
            annonce.setDateFin(LocalDate.parse(dateFin));
            annonce.setNbJour(nbJour);

            try {
                AnnonceService.modifier(annonce);
                loadAnnonceData();

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Annonce modifiée avec succès");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune annonce sélectionnée", "Veuillez sélectionner une annonce à modifier.");
        }
    }
    private void supprimerAnnonce() {
        Annonce annonce = tvAnnonces.getSelectionModel().getSelectedItem();
        if (annonce != null) {
            try {
                AnnonceService.supprimer(annonce);
                loadAnnonceData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'annonce a été supprimée avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression de l'annonce : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune annonce sélectionnée", "Veuillez sélectionner une annonce.");
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
    private void searchAnnonce(String searchString) throws SQLException {
        if (searchString.isEmpty()) {
            // If search text is empty, reload all annonces
            loadAnnonceData();
        } else {
            List<Annonce> filteredAnnonce = AnnonceService.afficher().stream()
                    .filter(annonce -> annonce.getTitreA().toLowerCase().startsWith(searchString.toLowerCase()))
                    .collect(Collectors.toList());

            ObservableList<Annonce> observableFilteredAnnonce = FXCollections.observableArrayList(filteredAnnonce);
            tvAnnonces.setItems(observableFilteredAnnonce);
        }
    }


}