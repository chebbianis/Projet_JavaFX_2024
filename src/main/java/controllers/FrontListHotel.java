package controllers;

import entities.Hotel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceHotel;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class FrontListHotel implements Initializable {
    @FXML
    private TilePane hotelsContainer;
    @FXML
    private TextField rechercheField;

    @FXML
    private Button reserverButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherHotel(new ActionEvent()); // Appeler rechercherHotel avec un objet ActionEvent
        });
        try {
            List<Hotel> hotels = getHotels();
            afficherHotels(hotels);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private List<Hotel> getHotels() throws SQLException {
        ServiceHotel serviceHotel = new ServiceHotel();
        return serviceHotel.afficher();
    }



    private void afficherHotels(List<Hotel> hotels) {
        for (Hotel hotel : hotels) {
            VBox hotelBox = new VBox();
            hotelBox.setSpacing(5);

            Label nameLabel = new Label(hotel.getNom_hotel());
            Label prixLabel = new Label("Prix : " + hotel.getPrix_nuit());
            Label typeLabel = new Label("Type : " + hotel.getNbre_etoile() + " étoiles");
            Label adresseLabel = new Label("Adresse : " + hotel.getAdresse_hotel());

            Button reserverButton = new Button("Réserver");
            reserverButton.setOnAction(event -> {
                try {
                    // Chargement de la vue de réservation à partir du fichier FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutFrontRev.fxml"));
                    Parent root = loader.load();

                    // Accédez au contrôleur de la vue de réservation si nécessaire pour passer des données
                    AjoutFrontRev controller = loader.getController();
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
            });

            hotelBox.getChildren().addAll(nameLabel, prixLabel, typeLabel, adresseLabel, reserverButton);
            hotelBox.getStyleClass().add("hotel-details");
            hotelBox.setPadding(new Insets(10));

            hotelsContainer.getChildren().add(hotelBox);
        }
    }



    @FXML
    private void rechercherHotel(ActionEvent event) {
        ServiceHotel hotelService = new ServiceHotel();

        try {
            String rechercheText = rechercheField.getText();
            List<Hotel> hotels = hotelService.rechercher(rechercheText);

            if (hotels.isEmpty()) {
                // Afficher un message si aucun hôtel n'a été trouvé
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Aucun hôtel trouvé pour cette recherche.");
                alert.showAndWait();
            } else {
                // Afficher les hôtels dans le TilePane
                hotelsContainer.getChildren().clear();
                for (Hotel hotel : hotels) {
                    VBox hotelBox = new VBox();
                    Label nameLabel = new Label("Nom : " + hotel.getNom_hotel());
                    Label prixLabel = new Label("Prix : " + hotel.getPrix_nuit());
                    Label typeLabel = new Label("Type : " + hotel.getNbre_etoile() + " étoiles");
                    Label adresseLabel = new Label("Adresse : " + hotel.getAdresse_hotel());

                    Button reserverButton = new Button("Réserver");
                    reserverButton.setOnAction(reserverEvent -> {
                        // Chargement de la vue de réservation à partir du fichier FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutFrontRev.fxml"));
                        try {
                            Parent root = loader.load();

                            // Accédez au contrôleur de la vue de réservation si nécessaire pour passer des données
                            AjoutFrontRev controller = loader.getController();
                            // Par exemple, vous pouvez passer l'ID de l'hôtel sélectionné
                            // controller.setHotelId(selectedHotelId);

                            // Créez une nouvelle scène
                            Scene scene = new Scene(root);

                            // Obtenez la scène actuelle à partir de n'importe quel nœud dans la vue actuelle
                            Stage stage = (Stage) ((Node) reserverEvent.getSource()).getScene().getWindow();

                            // Affichez la nouvelle scène dans une nouvelle fenêtre
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            System.err.println("Erreur lors du chargement de la vue de réservation : " + e.getMessage());
                        }
                    });


                    hotelBox.getChildren().addAll(nameLabel, prixLabel, typeLabel, adresseLabel, reserverButton);
                    hotelsContainer.getChildren().add(hotelBox);
                }


            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}