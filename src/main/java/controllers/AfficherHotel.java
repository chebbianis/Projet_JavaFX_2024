package controllers;

import entities.Hotel;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceHotel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.input.MouseEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AfficherHotel {

    @FXML
    private TextField txtNomHotelModif;

    @FXML
    private TextField txtAdresseHotelModif;

    @FXML
    private TextField txtImage;

    @FXML
    private Spinner<Integer> spinnerNbreEtoileModif;

    @FXML
    private Spinner<Double> spinnerPrixnuitModif;

    @FXML
    private TableView<Hotel> tableHotel;

    @FXML
    private TableColumn<Hotel, String> idCol;

    @FXML
    private TableColumn<Hotel, String> nom_hotelCol;

    @FXML
    private TableColumn<Hotel, String> nbre_etoileCol;

    @FXML
    private TableColumn<Hotel, String> adresse_hotelCol;

    @FXML
    private TableColumn<Hotel, String> prix_nuitCol;

    @FXML
    private TableColumn<Hotel, String> imageCol;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;
    private String image;
    @FXML
    private ImageView imageHotel;

   @FXML
   void update(ActionEvent event) {
       Hotel hotel = tableHotel.getSelectionModel().getSelectedItem();
       if (hotel != null) {
           // Récupérer les valeurs des champs texte
           String nom = txtNomHotelModif.getText();
           String adresse = txtAdresseHotelModif.getText();

           // Obtenir les valeurs des Spinners
           Integer nbreEtoileInteger = spinnerNbreEtoileModif.getValueFactory().getValue();
           int nbreEtoile = nbreEtoileInteger != null ? nbreEtoileInteger.intValue() : 0;

           Double prixNuitFloat = spinnerPrixnuitModif.getValueFactory().getValue();
           float prixNuit = prixNuitFloat != null ? prixNuitFloat.floatValue() : 0.0f;

           // String image = this.image;

           hotel.setNom_hotel(nom);
           hotel.setAdresse_hotel(adresse);
           hotel.setNbre_etoile(nbreEtoile);
           hotel.setPrix_nuit(prixNuit);
           if (image != null && !image.isEmpty()) {
               hotel.setImage(image);
           }

           ServiceHotel serviceHotel = new ServiceHotel();
           try {
               serviceHotel.modifier(hotel);

               initialize(); // Mettre à jour la table après la modification

               Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
               alert.setTitle("Succès");
               alert.setContentText("Hôtel modifié");
               alert.showAndWait();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       } else {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Aucun hôtel sélectionné");
           alert.setHeaderText(null);
           alert.setContentText("Veuillez sélectionner un hôtel à modifier.");
           alert.showAndWait();
       }
   }

    @FXML
    void getData(MouseEvent event) {
        Hotel hotel = tableHotel.getSelectionModel().getSelectedItem();

        if (hotel != null) {
            txtNomHotelModif.setText(hotel.getNom_hotel());
            txtAdresseHotelModif.setText(hotel.getAdresse_hotel());
            spinnerNbreEtoileModif.getValueFactory().setValue(hotel.getNbre_etoile());
            spinnerPrixnuitModif.getValueFactory().setValue((double) hotel.getPrix_nuit());
            image = hotel.getImage();
            System.out.println("Chemin d'accès à l'image : " + hotel.getImage());

            if (hotel.getImage() != null && !hotel.getImage().isEmpty()) {
                File file = new File(hotel.getImage());
                if (file.exists()) {
                    // Charger l'image dans l'ImageView
                    Image image1 = new Image(file.toURI().toString());
                    imageHotel.setImage(image1);
                } else {
                    // Si le fichier n'existe pas, effacer l'image
                    imageHotel.setImage(null);
                }
            } else {
                // Si aucun chemin d'image n'est spécifié, effacer l'image
                imageHotel.setImage(null);
            }
        }
    }




    @FXML
    void Delete(ActionEvent event) {
        Hotel hotel = tableHotel.getSelectionModel().getSelectedItem();
        if (hotel != null) {
            ServiceHotel serviceHotel = new ServiceHotel();
            try {
                serviceHotel.supprimer(hotel);
                tableHotel.getItems().remove(hotel);
                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("L'hôtel a été supprimé avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                // En cas d'erreur lors de la suppression, afficher un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur lors de la suppression");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la suppression de l'hôtel : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Aucun hôtel sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun hôtel sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un hôtel.");
            alert.showAndWait();
        }
    }


    @FXML
  void insert(ActionEvent event) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choisir une image d'hôtel");

      // Filtre pour afficher uniquement les fichiers d'image
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg");
      fileChooser.getExtensionFilters().add(extFilter);

      // Afficher la boîte de dialogue de sélection de fichier
      File file = fileChooser.showOpenDialog(imageHotel.getScene().getWindow());
      if (file != null) {
          // Charger l'image sélectionnée dans l'ImageView
          Image image1 = new Image(file.toURI().toString());
          imageHotel.setImage(image1);

          // Enregistrer le chemin de l'image sélectionnée
          image = file.getAbsolutePath();
      }
  }

    @FXML
    void initialize() {
        // Instanciation du service pour la gestion des hôtels
        ServiceHotel serviceHotel = new ServiceHotel();
        try {
            // Récupération de la liste des hôtels depuis la base de données
            List<Hotel> hotels = serviceHotel.afficher();

            // Création d'une liste observable à partir de la liste des hôtels
            ObservableList<Hotel> observableList = FXCollections.observableList(hotels);

            // Configuration de la TableView avec la liste observable
            tableHotel.setItems(observableList);

            // Liaison des propriétés des hôtels aux colonnes de la TableView
            idCol.setCellValueFactory(new PropertyValueFactory<>("idH"));
            nom_hotelCol.setCellValueFactory(new PropertyValueFactory<>("nom_hotel"));
            nbre_etoileCol.setCellValueFactory(new PropertyValueFactory<>("nbre_etoile"));
            adresse_hotelCol.setCellValueFactory(new PropertyValueFactory<>("adresse_hotel"));
            prix_nuitCol.setCellValueFactory(new PropertyValueFactory<>("prix_nuit"));
            imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

            spinnerNbreEtoileModif.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5)); // Par exemple, de 1 à 5 étoiles
            spinnerPrixnuitModif.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.1)); // Par exemple, prix positif avec décimales

        } catch (SQLException e) {
            // En cas d'erreur, affichage d'une alerte
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}
