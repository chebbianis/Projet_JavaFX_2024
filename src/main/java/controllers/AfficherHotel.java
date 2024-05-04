package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import entities.Hotel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceHotel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import javafx.scene.input.MouseEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.itextpdf.text.pdf.PdfWriter;

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
    private ComboBox<String> hotelComboBox;
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
    private TextField rechercheField;
    private List<Hotel> hotels;

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


    public void backToHome(ActionEvent event) {
        try {
            // Chargement de la vue de réservation à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterHotel.fxml"));
            Parent root = loader.load();

            // Accédez au contrôleur de la vue de réservation si nécessaire pour passer des données
            AjouterHotel controller = loader.getController();
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
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherHotel(newValue); // Appeler rechercherHotel avec le nouveau texte de recherche
        });
        // Instanciation du service pour la gestion des hôtels
        ServiceHotel serviceHotel = new ServiceHotel();
        try {
            // Récupération de la liste des hôtels depuis la base de données
            //List<Hotel> hotels = serviceHotel.afficher();
            hotels = serviceHotel.afficher();

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
    private void rechercherHotel(String rechercheText) {
        ServiceHotel hotelService = new ServiceHotel();

        try {
            List<Hotel> hotel = hotelService.rechercher(rechercheText); // Remplacez cette ligne avec votre méthode de recherche personnalisée

            ObservableList<Hotel> observableList = FXCollections.observableList(hotel);
            tableHotel.setItems(observableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void sortByNameAsc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getNom_hotel));
        tableHotel.setItems(hotels);
    }

    public void sortByAddressAsc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getAdresse_hotel));
        tableHotel.setItems(hotels);
    }
    @FXML
    void naviguezVersFront(ActionEvent event) {
        navigateTo("/FrontListHotel.fxml", event);


    }

    @FXML
    void naviguezVersHotel(ActionEvent event) {
        navigateTo("/ajouterHotel.fxml", event);

    }

    @FXML
    void naviguezVersRev(ActionEvent event) {
        navigateTo("/AfficherReservation.fxml", event);

    }
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            ((Button) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void sortByPriceAsc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getPrix_nuit));
        tableHotel.setItems(hotels);
    }

    public void sortByStarCountAsc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getNbre_etoile));
        tableHotel.setItems(hotels);
    }
    public void sortByNameDesc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getNom_hotel).reversed());
        tableHotel.setItems(hotels);
    }

    public void sortByAddressDesc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getAdresse_hotel).reversed());
        tableHotel.setItems(hotels);
    }

    public void sortByPriceDesc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getPrix_nuit).reversed());
        tableHotel.setItems(hotels);
    }

    public void sortByStarCountDesc(ActionEvent actionEvent) {
        ObservableList<Hotel> hotels = tableHotel.getItems();
        hotels.sort(Comparator.comparing(Hotel::getNbre_etoile).reversed());
        tableHotel.setItems(hotels);
    }

   /* public void generateAndOpenPDF(ActionEvent event) {
            Document document = new Document();

            try {
                // Spécification du chemin et du nom du fichier PDF
                File file = new File("Téléchargements.pdf");
                PdfWriter.getInstance(document, new FileOutputStream(file));

                // Ouverture du document
                document.open();

                // Ajout du contenu au document en utilisant la liste hotels déclarée en tant que variable membre
                for (Hotel hotel : hotels) {
                    document.add(new Paragraph("Nom de l'hôtel: " + hotel.getNom_hotel()));
                    document.add(new Paragraph("Adresse: " + hotel.getAdresse_hotel()));
                    document.add(new Paragraph("Étoiles: " + hotel.getNbre_etoile()));
                    document.add(new Paragraph("")); // Ajout d'une ligne vide entre chaque hôtel
                }

                // Fermeture du document
                document.close();

                // Ouverture du fichier PDF avec le programme par défaut
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(file);
                    } else {
                        // Gestion alternative si l'ouverture n'est pas prise en charge
                    }
                } else {
                    // Gestion alternative si Desktop n'est pas pris en charge
                }
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }*/
   public void generateAndOpenPDF(ActionEvent event) {
       Document document = new Document();

       try {
           // Spécification du chemin et du nom du fichier PDF
           File file = new File("Téléchargements.pdf");
           PdfWriter.getInstance(document, new FileOutputStream(file));

           // Ouverture du document
           document.open();

           // Ajouter un titre
           Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.DARK_GRAY);
           Paragraph title = new Paragraph("La liste d'hôtel Tuinvista", titleFont);
           title.setAlignment(Element.ALIGN_CENTER);
           title.setSpacingAfter(20); // Espacement après le titre
           document.add(title);

           // Create a PdfPTable with 3 columns
           PdfPTable table = new PdfPTable(3);

           // Set table width percentage to 100%
           table.setWidthPercentage(100);

           // Set table border color and width
           table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
           table.getDefaultCell().setBorderWidth(1f);

           // Set table header properties
           Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
           PdfPCell headerCell1 = new PdfPCell(new Phrase("Nom de l'hôtel", headerFont));
           PdfPCell headerCell2 = new PdfPCell(new Phrase("Adresse", headerFont));
           PdfPCell headerCell3 = new PdfPCell(new Phrase("Étoiles", headerFont));

           // Set header cell background color
           headerCell1.setBackgroundColor(new BaseColor(51, 122, 183)); // Blue
           headerCell2.setBackgroundColor(new BaseColor(51, 122, 183)); // Blue
           headerCell3.setBackgroundColor(new BaseColor(51, 122, 183)); // Blue

           // Set header cell text color
           headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
           headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
           headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);

           // Add header cells to the table
           table.addCell(headerCell1);
           table.addCell(headerCell2);
           table.addCell(headerCell3);

           // Set table body properties
           Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10);
           PdfPCell bodyCell;

           // Add hotel details to the table
           for (Hotel hotel : hotels) {
               // Cell 1: Hotel Name
               bodyCell = new PdfPCell(new Phrase(hotel.getNom_hotel(), bodyFont));
               bodyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
               table.addCell(bodyCell);

               // Cell 2: Hotel Address
               bodyCell = new PdfPCell(new Phrase(hotel.getAdresse_hotel(), bodyFont));
               bodyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
               table.addCell(bodyCell);

               // Cell 3: Hotel Stars
               bodyCell = new PdfPCell(new Phrase(String.valueOf(hotel.getNbre_etoile()), bodyFont));
               bodyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
               table.addCell(bodyCell);
           }

           // Add the table to the document
           document.add(table);

           // Fermeture du document
           document.close();

           // Ouverture du fichier PDF avec le programme par défaut
           if (Desktop.isDesktopSupported()) {
               Desktop desktop = Desktop.getDesktop();
               if (desktop.isSupported(Desktop.Action.OPEN)) {
                   desktop.open(file);
               } else {
                   // Gestion alternative si l'ouverture n'est pas prise en charge
               }
           } else {
               // Gestion alternative si Desktop n'est pas pris en charge
           }
       } catch (DocumentException | IOException e) {
           e.printStackTrace();
       }
   }

}
