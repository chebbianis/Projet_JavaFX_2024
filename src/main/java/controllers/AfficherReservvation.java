package controllers;

import entities.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import services.ServiceReservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class AfficherReservvation {

    @FXML
    private DatePicker dateArriveModif;

    @FXML
    private TableColumn<Reservation,String> dateArriveeCol;

    @FXML
    private TableColumn<Reservation, String > dateDepartCol;

    @FXML
    private DatePicker dateDepartModif;

    @FXML
    private Button deleteRev;

    @FXML
    private TextField idClientModif;

    @FXML
    private TableColumn<Reservation, Integer> idCol;

    @FXML
    private TableColumn<Reservation, Integer> nbreAdulteCol;

    @FXML
    private TextField nbreAdulteModif;

    @FXML
    private TableColumn<Reservation, Integer> nbreChambreCol;

    @FXML
    private TextField nbreChambreModif;

    @FXML
    private TableColumn<Reservation, Integer> nbreEnfantCol;

    @FXML
    private TextField nbreEnfantModif;

    @FXML
    private TableColumn<Reservation, String> nomHotelCol;

    @FXML
    private TextField nomHotelModif;

    @FXML
    private TableColumn<Reservation, Float> prixTotalCol;

    @FXML
    private TextField prixtotalModif;

    @FXML
    private TableView<Reservation> tableRev;

    @FXML
    private TableColumn<Reservation, String> typeChambreCol;

    @FXML
    private TextField typeChambreModif;

    @FXML
    private TableColumn<Reservation, String> typeRCol;

    @FXML
    private TextField typeRModif;

    @FXML
    private Button updateRev;
    @FXML
    void initialize() {

        ServiceReservation reservationService = new ServiceReservation();
        try {
            List<Reservation> reservations = reservationService.afficher();
            ObservableList<Reservation> observableList = FXCollections.observableList(reservations);
            tableRev.setItems(observableList);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id_client"));
            dateArriveeCol.setCellValueFactory(new PropertyValueFactory<>("date_arrivee"));
            dateDepartCol.setCellValueFactory(new PropertyValueFactory<>("date_depart"));
            typeRCol.setCellValueFactory(new PropertyValueFactory<>("type_r"));
            prixTotalCol.setCellValueFactory(new PropertyValueFactory<>("prix_total"));
            nbreChambreCol.setCellValueFactory(new PropertyValueFactory<>("nbre_chambre"));
            typeChambreCol.setCellValueFactory(new PropertyValueFactory<>("type_chambre"));
            nbreAdulteCol.setCellValueFactory(new PropertyValueFactory<>("nbre_adulte"));
            nbreEnfantCol.setCellValueFactory(new PropertyValueFactory<>("nbre_enfant"));
            nomHotelCol.setCellValueFactory(new PropertyValueFactory<>("idH"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void deleteRev(ActionEvent event) {
        Reservation reservation = tableRev.getSelectionModel().getSelectedItem();
        if (reservation != null) {
            ServiceReservation serviceReservation = new ServiceReservation();
            try {
                serviceReservation.supprimer(reservation);
                tableRev.getItems().remove(reservation);

                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("La réservation a été supprimée avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                // En cas d'erreur lors de la suppression, afficher un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur lors de la suppression");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la suppression de la réservation : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Aucune réservation sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune réservation sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une réservation à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    void getData(MouseEvent event) {
        Reservation reservation = tableRev.getSelectionModel().getSelectedItem();

        idClientModif.setText(String.valueOf(reservation.getId_client()));
        dateArriveModif.setValue(reservation.getDate_arrivee());
        dateDepartModif.setValue(reservation.getDate_depart());
        typeRModif.setText(reservation.getType_r());
        prixtotalModif.setText(String.valueOf(reservation.getPrix_total()));
        nbreChambreModif.setText(String.valueOf(reservation.getNbre_chambre()));
        typeChambreModif.setText(reservation.getType_chambre());
        nbreAdulteModif.setText(String.valueOf(reservation.getNbre_adulte()));
        nbreEnfantModif.setText(String.valueOf(reservation.getNbre_enfant()));
        // Assurez-vous que les ComboBox nomHotelModif et typeChambreModif sont remplis avec les bonnes valeurs.
        // Vous pouvez utiliser les méthodes setItems() pour cela.
        // nomHotelModif.setItems(vosDonneesDeNomHotel);
        // typeChambreModif.setItems(vosDonneesDeTypeChambre);
    }



    @FXML
    void updateRev(ActionEvent event) {
        Reservation reservation = tableRev.getSelectionModel().getSelectedItem();
        if (reservation != null) {
            // Récupérer les valeurs des champs texte
            String idHText = nomHotelModif.getText();
            String idClientText = idClientModif.getText();
            String prixTotalText = prixtotalModif.getText();
            String dateArriveeText = dateArriveModif.getValue().toString();
            String dateDepartText = dateDepartModif.getValue().toString();
            String typeR = typeRModif.getText();
            String nbreChambreText = nbreChambreModif.getText();
            String typeChambre = typeChambreModif.getText();
            String nbreAdulteText = nbreAdulteModif.getText();
            String nbreEnfantText = nbreEnfantModif.getText();

            int idH = 0;
            int idClient = 0;
            int prixTotal = 0;
            int nbreChambre = 0;
            int nbreAdulte = 0;
            int nbreEnfant = 0;

            if (!idHText.isEmpty() && !idHText.equals("[]")) {
                idH = Integer.parseInt(idHText.replaceAll("\\D+", ""));
            }
            if (!idClientText.isEmpty() && !idClientText.equals("[]")) {
                idClient = Integer.parseInt(idClientText);
            }
            if (!prixTotalText.isEmpty() && !prixTotalText.equals("[]")) {
                prixTotal = Integer.parseInt(prixTotalText);
            }
            if (!nbreChambreText.isEmpty() && !nbreChambreText.equals("[]")) {
                nbreChambre = Integer.parseInt(nbreChambreText);
            }
            if (!nbreAdulteText.isEmpty() && !nbreAdulteText.equals("[]")) {
                nbreAdulte = Integer.parseInt(nbreAdulteText);
            }
            if (!nbreEnfantText.isEmpty() && !nbreEnfantText.equals("[]")) {
                nbreEnfant = Integer.parseInt(nbreEnfantText);
            }

            reservation.setIdH(idH);
            reservation.setId_client(idClient);
            reservation.setPrix_total(prixTotal);
            reservation.setDate_arrivee(LocalDate.parse(dateArriveeText));
            reservation.setDate_depart(LocalDate.parse(dateDepartText));
            reservation.setType_r(typeR);
            reservation.setNbre_chambre(nbreChambre);
            reservation.setType_chambre(typeChambre);
            reservation.setNbre_adulte(nbreAdulte);
            reservation.setNbre_enfant(nbreEnfant);

            // Mettre à jour l'objet Reservation dans la base de données
            ServiceReservation serviceReservation = new ServiceReservation();
            try {
                serviceReservation.modifier(reservation);

                // Rafraîchir la table view
                initialize();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succès");
                alert.setContentText("Réservation modifiée");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune réservation sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une réservation à modifier.");
            alert.showAndWait();
        }

    }

}


