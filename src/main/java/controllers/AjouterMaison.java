package controllers;
import entities.Maison;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import services.ServiceMaison;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AjouterMaison {
    @FXML
    private Button btnAjout;
    @FXML
    private ImageView imageView;

    @FXML
    private Button insertImage;
    @FXML
    private Text cheminImage;

    @FXML
    private TextField nom;
    @FXML
    private TextField adresse;
    @FXML
    private ListView<String> listView;
    private ObservableList<String> adressesGouvernorats;
    @FXML
    private TextField nombre_chambre;
    @FXML
    private TextField prix ;
    @FXML
    private TextField type ;


    @FXML
    void initialize() {
        adressesGouvernorats = FXCollections.observableArrayList(getAdressesGouvernorats());
        listView.setItems(adressesGouvernorats);
        listView.setVisible(false);
    }
    public List<String> getAdressesGouvernorats() {
        List<String> adressesGouvernorats = new ArrayList<>();
        adressesGouvernorats.add("Ariana");
        adressesGouvernorats.add("Béja");
        adressesGouvernorats.add("Ben Arous");
        adressesGouvernorats.add("Bizerte");
        adressesGouvernorats.add("Gabès");
        adressesGouvernorats.add("Gafsa");
        adressesGouvernorats.add("Jendouba");
        adressesGouvernorats.add("Kairouan");
        adressesGouvernorats.add("Kasserine");
        adressesGouvernorats.add("Kébili");
        adressesGouvernorats.add("Le Kef");
        adressesGouvernorats.add("Mahdia");
        adressesGouvernorats.add("Manouba");
        adressesGouvernorats.add("Médenine");
        adressesGouvernorats.add("Monastir");
        adressesGouvernorats.add("Nabeul");
        adressesGouvernorats.add("Sfax");
        adressesGouvernorats.add("Sidi Bouzid");
        adressesGouvernorats.add("Siliana");
        adressesGouvernorats.add("Sousse");
        adressesGouvernorats.add("Tataouine");
        adressesGouvernorats.add("Tozeur");
        adressesGouvernorats.add("Tunis");
        adressesGouvernorats.add("Zaghouan");
        return adressesGouvernorats;
    }

    @FXML
    void insertImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de maison");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers image (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(btnAjout.getScene().getWindow());
        if (file != null) {
            Image image1 = new Image(file.toURI().toString());
            imageView.setImage(image1);
            cheminImage.setText(file.getAbsolutePath());
            image = file.getAbsolutePath();
        }

    /*    Image image1 = new Image(file.toURI().toString());
        imageView.setImage(image1);
        String fileName = file.getName();
        cheminImage.setText(fileName);
        image = fileName;*/

    }
    @FXML
    void naviguerVisite(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AfficherVisit.fxml"));
        try {
            nombre_chambre.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void afficherMaison(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AfficherMaison.fxml"));
        try {
            nombre_chambre.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void ajouterMaison(ActionEvent event) {

        if (nom.getText().isEmpty() || adresse.getText().isEmpty() || nombre_chambre.getText().isEmpty()
                || prix.getText().isEmpty() || type.getText().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Champs non remplis");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Veuillez remplir tous les champs.");
            errorAlert.showAndWait();
            return;
        }
        String nomValue = nom.getText();
        if (!nomValue.matches("[a-zA-Z\\s]+")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Entrée invalide");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Le champ 'Nom' ne doit contenir que des lettres et des espaces.");
            errorAlert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }
        // Vérifier si le champ "nombre_chambre" contient un nombre valide
        String nombreChambreValue = nombre_chambre.getText();
        if (!nombreChambreValue.matches("\\d+") || Integer.parseInt(nombreChambreValue) <= 0 ) {
            // Afficher un message d'erreur à l'utilisateur
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Entrée invalide");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Le champ 'Nombre de Chambre' doit être un nombre entier positif .");
            errorAlert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }

        String prixValue = prix.getText();
        if (!prixValue.matches("\\d+") || Integer.parseInt(prixValue) <= 0 ) {
            // Afficher un message d'erreur à l'utilisateur
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Entrée invalide");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Le champ 'prix' doit être un nombre  positif .");
            errorAlert.showAndWait();
            return;
        }


        int nombreChambre = Integer.parseInt(nombreChambreValue);
        int Prix = Integer.parseInt(prixValue);
     

        Maison maison = new Maison(nom.getText(), adresse.getText(), nombreChambre, Prix, type.getText(), image);

        ServiceMaison serviceMaison = new ServiceMaison();
        try {
            serviceMaison.ajouter(maison);
            clear();
            Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Maison ajoutée");
            alert.showAndWait();

        } catch (SQLException e) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
          //  throw new RuntimeException(e);
        }
    }
    private String image;
  void clear (){
        nom.setText(null);
        adresse.setText(null);
        nombre_chambre.setText(null);
        prix.setText(null);
        type.setText(null);
        cheminImage.setText(null);
        imageView.setImage(null);
      //  btnAjout.setDisable(false);
  }
    @FXML
    void onAdresseInputChanged() {
        String input = adresse.getText().toLowerCase();
        List<String> filteredAddresses = adressesGouvernorats.stream()
                .filter(address -> address.toLowerCase().startsWith(input))
                .collect(Collectors.toList());
        if (filteredAddresses.isEmpty()) {
            listView.setVisible(false);
        } else {
            listView.setItems(FXCollections.observableArrayList(filteredAddresses));
            listView.setVisible(true);
        }
    }


    @FXML
    void onAddressSelected() {
        String selectedAddress = listView.getSelectionModel().getSelectedItem();
        if (selectedAddress != null) {
            adresse.setText(selectedAddress);
            listView.setVisible(false);
        }
    }



  /*  @FXML
    void initialize() {
            Platform.runLater(() -> {
                adresse.textProperty().addListener((observable, oldValue, newValue) -> {
                    String userInput = newValue.trim();
                    if (!userInput.isEmpty()) {
                        String firstLetter = userInput.substring(0, 1).toUpperCase();
                        List<String> suggestions = gouvernorats.stream()
                                .filter(g -> g.startsWith(firstLetter))
                                .collect(Collectors.toList());
                        if (!suggestions.isEmpty()) {
                            String suggestedGouvernorats = String.join(", ", suggestions);
                            adresse.setPromptText("Suggestions: " + suggestedGouvernorats);
                        } else {
                            adresse.setPromptText("Aucune suggestion pour cette lettre.");
                        }
                    } else {
                        adresse.setPromptText("adresse");
                    }
                });
            });
        }
*/
/*
       ServiceMaison serviceMaison=new ServiceMaison();
       Maison maison =new Maison();
       maison.setNom(nom.getText());
       maison.setAdresse(adresse.getText());
       maison.setNombreChambre(Integer.parseInt(nombre_chambre.getText()));
       maison.setPrix(Integer.parseInt(prix.getText()));
       maison.setType(type.getText());

        try {
            serviceMaison.ajouter(maison);
            Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Maison ajoutée");
            alert.showAndWait();

        } catch (SQLException e) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
          //  throw new RuntimeException(e);
        }
    }
 */
}
