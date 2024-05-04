package controllers;

import entities.Hotel;
import entities.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceHotel;
import services.ServiceReservation;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

public class AjoutFrontRev {

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

            float prix_nuit = 100; // Remplacez cette valeur par le prix unitaire réel

            LocalDate dateArrivee = reservation.getDate_arrivee();
            LocalDate dateDepart = reservation.getDate_depart();

            int nbreNuits = (int) ChronoUnit.DAYS.between(dateArrivee, dateDepart); // Calcul du nombre de nuits

           // float prix_total = calculerPrixTotal(prix_nuit, nbreChambre, nbreNuits);
          //  reservation.setPrix_total((int) prix_total);
            // ... remaining code ...


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
    @FXML
    private Button btnPayer;


    public void backToHome(ActionEvent event) {
        try {
            // Chargement de la vue de réservation à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontListHotel.fxml"));
            Parent root = loader.load();

            // Accédez au contrôleur de la vue de réservation si nécessaire pour passer des données
            FrontListHotel controller = loader.getController();
            // Par exemple, vous pouvez passer l'ID de l'hôtel sélectionné
            // controller.setHotelId(selectedHotelId);

            // Créez une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenez la scène actuelle à partir de n'importe quel nœud dans la vue actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Affichez la nouvelle scène dans une nouvelle fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue de réservation : " + e.getMessage());
        }
    }
    @FXML
    public void payer(ActionEvent event) {
        // Vérifiez si tous les champs sont remplis
        if (!allFieldsFilled()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Chargement de la vue de paiement à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Payment.fxml"));
            Parent root = loader.load();

            // Accédez au contrôleur de la vue de paiement si nécessaire pour passer des données
            Payment controller = loader.getController();

            // Créez une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenez la scène actuelle à partir de n'importe quel nœud dans la vue actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Affichez la nouvelle scène dans une nouvelle fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue de paiement : " + e.getMessage());
        }
    }

    private boolean allFieldsFilled() {
        return !idClientRev.getText().isEmpty() &&
                !prixTotalRev.getText().isEmpty() &&
                dateArriveeRev.getValue() != null &&
                dateDepartRev.getValue() != null;
    }

}

