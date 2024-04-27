package com.example.pidevjava.Contollers;

import com.example.pidevjava.Entities.Voyageur;
import com.example.pidevjava.service.serviceVoyageur;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VoyageurController {

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_insert;

    @FXML
    private Button btn_update;

    @FXML
    private Button btn_stat;

    @FXML
    private Button bn_pdf;



    @FXML
    private TableColumn<Voyageur, Integer> col_age;

    @FXML
    private TableColumn<Voyageur, String> col_etat;

    @FXML
    private TableColumn<Voyageur, Integer> col_idv;

    @FXML
    private TableColumn<Voyageur, String> col_nom;

    @FXML
    private TableColumn<Voyageur, String> col_email;


    @FXML
    private TableColumn<Voyageur, Integer> col_pass;

    @FXML
    private TableColumn<Voyageur, String> col_prenom;

    @FXML
    private TableView<Voyageur> table_voyageur;

    @FXML
    private TextField tf_age;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_etat;

    @FXML
    private TextField tf_nom;

    @FXML
    private TextField tf_num;

    @FXML
    private TextField tf_prenom;

    @FXML
    private Label tr_age;

    @FXML
    private Label tr_email;

    @FXML
    private Label tr_etat;

    @FXML
    private Label tr_prenom;


    @FXML
    private TextField rechercheField;

    @FXML
    private Button btn_ret;


    @FXML
    void onRechercheFieldTextChanged(KeyEvent event) {
        String rechercheText = rechercheField.getText().trim();
        rechercherVoyageur(rechercheText);
    }

    private serviceVoyageur service = new serviceVoyageur();

    public void initialize() {
        // Initialisation des colonnes
        col_idv.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_pass.setCellValueFactory(new PropertyValueFactory<>("num_pass"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_etat.setCellValueFactory(new PropertyValueFactory<>("etat_civil"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));


        loadVoyageurData(); // Chargement des données dans la TableView

        // Ajout de l'événement pour sélectionner un voyageur dans le formulaire
        table_voyageur.setOnMouseClicked(event -> selectVoyageur(event));

        btn_delete.setOnAction(event -> supprimerVoyageur());
        btn_update.setOnAction(event -> modifierVoyageur());
        btn_insert.setOnAction(event -> ajouterVoyageur());

        btn_stat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/EtatCivil.fxml"));
                    Parent root = fxmlLoader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the stage from the button's scene
                    Stage stage = (Stage) btn_stat.getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_ret.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/hello-view.fxml"));
                    Parent root = fxmlLoader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the stage from the button's scene
                    Stage stage = (Stage) btn_ret.getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidAge(String age) {
        return age.matches("\\d{2}");
    }

    private boolean validateFields() {
        return !tf_num.getText().isEmpty() &&
                tf_num.getText().length() == 7 &&
                !tf_nom.getText().isEmpty() && tf_nom.getText().length() <= 50 &&
                !tf_prenom.getText().isEmpty() && tf_prenom.getText().length() <= 50 &&
                !tf_age.getText().isEmpty() && isValidAge(tf_age.getText()) &&
                !tf_etat.getText().isEmpty() && tf_etat.getText().length() <= 20 &&
                !tf_email.getText().isEmpty() && isValidEmail(tf_email.getText());
    }

    private void ajouterVoyageur() {

        if (!validateFields()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs correctement.");
            alert.showAndWait();
            return;
        }


        Voyageur newVoyageur = new Voyageur(
                Integer.parseInt(tf_num.getText()),
                tf_nom.getText(),
                tf_prenom.getText(),
                Integer.parseInt(tf_age.getText()),
                tf_etat.getText(),
                tf_email.getText()
        );

        try {
            service.ajouter(newVoyageur);
            loadVoyageurData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifierVoyageur() {
        Voyageur selectedVoyageur = table_voyageur.getSelectionModel().getSelectedItem();
        if (selectedVoyageur != null) {

            selectedVoyageur.setNum_pass(Integer.parseInt(tf_num.getText()));
            selectedVoyageur.setNom(tf_nom.getText());
            selectedVoyageur.setPrenom(tf_prenom.getText());
            selectedVoyageur.setAge(Integer.parseInt(tf_age.getText()));
            selectedVoyageur.setEtat_civil(tf_etat.getText());
            selectedVoyageur.setEmail(tf_email.getText());

            try {
                service.modifier(selectedVoyageur); // Appel de la méthode de service pour modifier le voyageur
                loadVoyageurData(); // Recharger les données après la modification
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer l'exception de manière appropriée (affichage ou journalisation)
            }
        } else {
            // Afficher un message à l'utilisateur indiquant de sélectionner un voyageur à modifier
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un voyageur à modifier.");
            alert.showAndWait();
        }
    }

    private void supprimerVoyageur() {
        Voyageur selectedVoyageur = table_voyageur.getSelectionModel().getSelectedItem();
        if (selectedVoyageur != null) {
            // Demander confirmation avant la suppression
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer ce voyageur?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    service.supprimer(selectedVoyageur); // Appel de la méthode de service pour supprimer le voyageur
                    loadVoyageurData(); // Recharger les données après la suppression
                } catch (SQLException e) {
                    e.printStackTrace(); // Gérer l'exception de manière appropriée (affichage ou journalisation)
                }
            }
        } else {
            // Afficher un message à l'utilisateur indiquant de sélectionner un voyageur à supprimer
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un voyageur à supprimer.");
            alert.showAndWait();
        }
    }

    private ObservableList<Voyageur> getVoyageurList() {
        ObservableList<Voyageur> voyageurs = FXCollections.observableArrayList();
        try {
            voyageurs.addAll(service.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyageurs;
    }

    private void loadVoyageurData() {
        ObservableList<Voyageur> voyageurs = getVoyageurList();
        table_voyageur.setItems(voyageurs);
    }

    private void selectVoyageur(MouseEvent event) {
        // Implémentez la logique pour sélectionner un voyageur dans la TableView
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Voyageur selectedVoyageur = table_voyageur.getSelectionModel().getSelectedItem();
            if (selectedVoyageur != null) {
                // Affichage des détails du voyageur sélectionné dans les labels
                tf_num.setText(String.valueOf(selectedVoyageur.getNum_pass()));
                tf_nom.setText(selectedVoyageur.getNom());
                tf_prenom.setText(selectedVoyageur.getPrenom());
                tf_age.setText(String.valueOf(selectedVoyageur.getAge()));
                tf_etat.setText(selectedVoyageur.getEtat_civil());
                tf_email.setText(selectedVoyageur.getEmail());
            }
        }
    }
    private void rechercherVoyageur(String rechercheText) {
        serviceVoyageur voyageurService = new serviceVoyageur();
        try {
            List<Voyageur> Voyagurs = voyageurService.rechercher(rechercheText);

            ObservableList<Voyageur> observableList = FXCollections.observableList(Voyagurs);
            table_voyageur.setItems(observableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }




}
