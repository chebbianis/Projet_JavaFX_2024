package controllers;

import entities.Maison;
import entities.Visit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import services.ServiceMaison;
import services.ServiceVisit;
import utils.MyDB;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

public class AfficherVisit {


    @FXML
    private TableColumn<?, ?> dateC;

    @FXML
    private DatePicker dateModif;

    @FXML
    private TableColumn<?, ?> emailC;

    @FXML
    private TextField emailModif;

    @FXML
    private TableColumn<?, ?> idC;

    @FXML
    private TableColumn<?, ?> nomC;

    @FXML
    private TextField nomModif;

    @FXML
    private TextField numModif;

    @FXML
    private TableColumn<?, ?> numeroC;

    @FXML
    private TableColumn<?, ?> refC;

    @FXML
    private TextField refModif;

    @FXML
    private TableView<Visit> tableViewV;
    @FXML
    private TextField rechercheField;

    @FXML
    private ListView<Visit> listView;


    @FXML
    void getData(MouseEvent event) {
        Visit visit = tableViewV.getSelectionModel().getSelectedItem();

        numModif.setText(String.valueOf(visit.getNumero()));
        nomModif.setText(visit.getNom());
        emailModif.setText(visit.getEmail());
        refModif.setText(String.valueOf(visit.getRefB()));
        dateModif.setValue(visit.getDateVisit());

    }
@FXML
void modifierVisit(ActionEvent event) {
    Visit visit = tableViewV.getSelectionModel().getSelectedItem();
    if (visit != null) {
        // Récupérer les valeurs des champs texte
        String ref_bText = refModif.getText().toString();
        String numeroText = numModif.getText();
        LocalDate date_visit = dateModif.getValue();
        String email = emailModif.getText();
        String nom = nomModif.getText();

        int ref_b = 0;
        int numero = 0;

        if (!ref_bText.isEmpty() && !ref_bText.equals("[]")) {
            ref_b = Integer.parseInt(ref_bText.replaceAll("\\D+", ""));
        }
        if (!numeroText.isEmpty() && !numeroText.equals("[]")) {
            numero = Integer.parseInt(numeroText);
        }


        visit.setNumero(numero);
        visit.setEmail(email);
        visit.setNom(nom);
        visit.setRefB(ref_b);
        visit.setDateVisit(date_visit);

        // Mettre à jour l'objet Visite dans la base de données
        ServiceVisit serviceVisite = new ServiceVisit();
        try {
            serviceVisite.modifier(visit);
            // Rafraîchir la table view
            initialize();
            clear();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setContentText("Visite modifiée");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aucune visite sélectionnée");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez sélectionner une visite à modifier.");
        alert.showAndWait();
    }
}
    @FXML
    void initialize() {
        ServiceVisit serviceVisit =new ServiceVisit();

        try {
            List<Visit> visit= serviceVisit.afficher();
            ObservableList<Visit> observableList= FXCollections.observableList(visit);
            tableViewV.setItems(observableList);

            idC.setCellValueFactory(new PropertyValueFactory<>("id"));
            numeroC.setCellValueFactory(new PropertyValueFactory<>("numero"));
            nomC.setCellValueFactory(new PropertyValueFactory<>("nom"));
            emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
            refC.setCellValueFactory(new PropertyValueFactory<>("refB"));
            dateC.setCellValueFactory(new PropertyValueFactory<>("dateVisit"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void rechercherMaison(String rechercheText) {
        ServiceVisit serviceVisit = new ServiceVisit();
        try {
            List<Visit> visits = serviceVisit.rechercher(rechercheText); // Remplacez cette ligne avec votre méthode de recherche personnalisée

            ObservableList<Visit> observableList = FXCollections.observableList(visits);
            tableViewV.setItems(observableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void onRechercheFieldTextChanged(KeyEvent event) {
        String rechercheText = rechercheField.getText().trim();
        rechercherMaison(rechercheText);
    }
    @FXML
    void supprimerVisit(ActionEvent event) {
        Visit visit = tableViewV.getSelectionModel().getSelectedItem();
        if (visit != null) {
            ServiceVisit serviceVisit = new ServiceVisit();
            try {
                serviceVisit.supprimer(visit);
                tableViewV.getItems().remove(visit);
                clear();
                // Mettre à jour l'affichage après la suppression
                serviceVisit.afficher();
                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("La demande de visite a été supprimée avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                // En cas d'erreur lors de la suppression, afficher un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur lors de la suppression");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la suppression de la maison : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Aucune maison sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune maison sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une visite.");
            alert.showAndWait();
        }
    }
    void clear (){
        nomModif.setText(null);
        numModif.setText(null);
        emailModif.setText(null);
        refModif.setText(null);
        dateModif.setValue(null);
    }
}

/*    public void afficherListe() {
        try {
            // Créez une liste observable pour stocker les réservations
            ObservableList<Visit> listData = FXCollections.observableArrayList();

            // Récupérez les données de la base de données
            String sql = "SELECT * FROM visit ";
            Connection connection = MyDB.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();


            // Parcourez le résultat de la requête et ajoutez les réservations à la liste observable
            while (resultSet.next()) {
                Visit reservationInfo = new Visit(
                        resultSet.getInt("id"),
                        resultSet.getInt("numero"),
                        resultSet.getString("nom"),
                        resultSet.getString("email"),
                        resultSet.getInt("refB"),
                        resultSet.getDate("date_visit").toLocalDate()
                );
                listData.add(reservationInfo);
            }  // Définissez les éléments de votre ListView avec la liste observable
            listView.setItems(listData);

            // Fermez la connexion à la base de données
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Affichez l'erreur dans la console
        }
    }

    @FXML
    void initialize() {
        afficherListe();

    }

*/

/*
  assert dateC != null : "fx:id=\"dateC\" was not injected: check your FXML file 'AfficherVisit.fxml'.";
        assert emailC != null : "fx:id=\"emailC\" was not injected: check your FXML file 'AfficherVisit.fxml'.";
        assert nomC != null : "fx:id=\"nomC\" was not injected: check your FXML file 'AfficherVisit.fxml'.";
        assert numeroC != null : "fx:id=\"numeroC\" was not injected: check your FXML file 'AfficherVisit.fxml'.";
        assert refC != null : "fx:id=\"refC\" was not injected: check your FXML file 'AfficherVisit.fxml'.";
        assert tableViewV != null : "fx:id=\"tableViewV\" was not injected: check your FXML file 'AfficherVisit.fxml'.";

 */