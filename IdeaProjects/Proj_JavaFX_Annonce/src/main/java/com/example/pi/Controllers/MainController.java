package com.example.pi.Controllers;

import com.example.pi.Entities.Annonce;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button btn_ajouter;
    @FXML
    private Button btn_modifier;
    @FXML
    private Button btn_supprimer;
    @FXML
    private TextField tf_titre_a;
    @FXML
    private TextField tf_description_a;
    @FXML
    private TextField tf_ville_a;
    @FXML
    private DatePicker tf_date_debut_a;
    @FXML
    private DatePicker tf_date_fin_a;
    @FXML
    private TextField tf_nb_jour_a;
    @FXML
    private TableView<Annonce> tvAnnonces;
    @FXML
    private TableColumn<Annonce, Integer> colIdAnnonce;
    @FXML
    private TableColumn<Annonce, String> colTitreAnnonce;
    @FXML
    private TableColumn<Annonce, String> colDescriptionAnnonce;
    @FXML
    private TableColumn<Annonce, String> colVilleAnnonce;
    @FXML
    private TableColumn<Annonce, Date> colDateDebutA;
    @FXML
    private TableColumn<Annonce, Date> colDateFinA;
    @FXML
    private TableColumn<Annonce, Integer> colNbJourA;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_ajouter.setOnAction(event -> {
            ajouterAnnonce();
        });

        btn_modifier.setOnAction(event -> {
            modifierAnnonce();
        });

        btn_supprimer.setOnAction(event -> {
            supprimerAnnonce();
        });

        tvAnnonces.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                afficherDetailsAnnonce(newSelection);
            } else {
                clearForm();
            }
        });
        afficherAnnonce();
    }


    private boolean validateInputs() {
        String titre = tf_titre_a.getText();
        String description = tf_description_a.getText();
        String ville = tf_ville_a.getText();
        String nbJour = tf_nb_jour_a.getText();

        if (!validateStringInput(titre, "Titre")) return false;
        if (!validateStringInput(description, "Description")) return false;
        if (!validateStringInput(ville, "Ville")) return false;
        if (!validateIntegerInput(nbJour, "Nombre de Jours")) return false;

        return true;
    }

    private boolean validateStringInput(String input, String fieldName) {
        if (input.isEmpty() || input.length() < 2 || input.length() > 30) {
            showAlert("Erreur", fieldName + " doit contenir entre 2 et 30 caractères.");
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

    private void clearForm() {
        tf_titre_a.clear();
        tf_description_a.clear();
        tf_ville_a.clear();
        tf_date_debut_a.setValue(null);
        tf_date_fin_a.setValue(null);
        tf_nb_jour_a.clear();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tunvista",
                "root",
                ""
        );
    }

    public ObservableList<Annonce> getAnnonceList() {
        ObservableList<Annonce> annonceList = FXCollections.observableArrayList();
        String query = "SELECT * FROM annonce";
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Annonce annonce = new Annonce(
                        resultSet.getString("id_annonce"),
                        resultSet.getString("titre_a"),
                        resultSet.getString("description_a"),
                        resultSet.getString("ville_a"),
                        resultSet.getDate("date_debut"),
                        resultSet.getDate("date_fin"),
                        resultSet.getString("nb_jour")
                );
                annonceList.add(annonce);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return annonceList;
    }

    private void afficherAnnonce() {
        ObservableList<Annonce> list = getAnnonceList();

        colIdAnnonce.setCellValueFactory(new PropertyValueFactory<>("idAnnonce"));
        colTitreAnnonce.setCellValueFactory(new PropertyValueFactory<>("titreA"));
        colDescriptionAnnonce.setCellValueFactory(new PropertyValueFactory<>("descriptionA"));
        colVilleAnnonce.setCellValueFactory(new PropertyValueFactory<>("villeA"));
        colDateDebutA.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFinA.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colNbJourA.setCellValueFactory(new PropertyValueFactory<>("nbJour"));

        tvAnnonces.setItems(list);
    }

    private void afficherDetailsAnnonce(Annonce annonce) {
        tf_titre_a.setText(annonce.getTitreA());
        tf_description_a.setText(annonce.getDescriptionA());
        tf_ville_a.setText(annonce.getVilleA());
        tf_date_debut_a.setValue(annonce.getDateDebut().toLocalDate());
        tf_date_fin_a.setValue(annonce.getDateFin().toLocalDate());
        tf_nb_jour_a.setText(String.valueOf(annonce.getNbJour()));
    }

    private void ajouterAnnonce() {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("INSERT INTO annonce (titre_a, description_a, ville_a, date_debut, date_fin, nb_jour) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, tf_titre_a.getText());
            statement.setString(2, tf_description_a.getText());
            statement.setString(3, tf_ville_a.getText());
            statement.setDate(4, Date.valueOf(tf_date_debut_a.getValue()));
            statement.setDate(5, Date.valueOf(tf_date_fin_a.getValue()));
            statement.setInt(6, Integer.parseInt(tf_nb_jour_a.getText()));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Annonce ajoutée avec succès !");
                afficherAnnonce();
                clearForm();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'annonce", e);
        }
    }

    private void modifierAnnonce() {
        Annonce selectedAnnonce = tvAnnonces.getSelectionModel().getSelectedItem();
        if (selectedAnnonce != null) {
            String query = "UPDATE annonce SET titre_a = ?, description_a = ?, ville_a = ?, date_debut = ?, date_fin = ?, nb_jour = ? WHERE id_annonce = ?";
            try (Connection conn = getConnection();
                 PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, tf_titre_a.getText());
                statement.setString(2, tf_description_a.getText());
                statement.setString(3, tf_ville_a.getText());
                statement.setDate(4, Date.valueOf(tf_date_debut_a.getValue()));
                statement.setDate(5, Date.valueOf(tf_date_fin_a.getValue()));
                statement.setString(6, tf_nb_jour_a.getText());
                statement.setString(7, selectedAnnonce.getIdAnnonce());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Annonce mise à jour avec succès !");
                    afficherAnnonce();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la mise à jour de l'annonce", e);
            }
        } else {
            System.out.println("Veuillez sélectionner une annonce à mettre à jour.");
        }
    }

    private void supprimerAnnonce() {
        Annonce selectedAnnonce = tvAnnonces.getSelectionModel().getSelectedItem();
        if (selectedAnnonce != null) {
            String query = "DELETE FROM annonce WHERE id_annonce = ?";
            try (Connection conn = getConnection();
                 PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, selectedAnnonce.getIdAnnonce());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0
) {
                    System.out.println("Annonce supprimé avec succès !");
                    afficherAnnonce(); // Actualiser la table des utilisateurs après la suppression
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la suppression de l'annonce", e);
            }
        } else {
            System.out.println("Veuillez sélectionner une annonce à supprimer.");
        }
    }


    // This method should be replaced with your actual implementation to fetch announcements from the database
    /*private ObservableList<Announcement> getAnnouncementList() {
        // Dummy implementation for demonstration purposes
        ObservableList<Announcement> list = FXCollections.observableArrayList();
        list.add(new Announcement(1, "Title 1", "Description 1", "City 1", Date.valueOf("2024-01-01"), Date.valueOf("2024-01-10"), 10));
        list.add(new Announcement(2, "Title 2", "Description 2", "City 2", Date.valueOf("2024-02-01"), Date.valueOf("2024-02-10"), 10));
        list.add(new Announcement(3, "Title 3", "Description 3", "City 3", Date.valueOf("2024-03-01"), Date.valueOf("2024-03-10"), 10));
        return list;
    }*/
}
