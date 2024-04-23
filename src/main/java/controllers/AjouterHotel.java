package controllers;

import entities.Hotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceHotel;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
/*import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;*/

public class AjouterHotel {
 //   private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private String imageFilePath;

    @FXML
    private TextField txtNomHotel;

    @FXML
    private Spinner<Integer> spinnerNbreEtoile;

    @FXML
    private TextField txtAdresseHotel;
    @FXML
    private ListView<String> adresseListView;
    private ObservableList<String> adressesGouvernorats;

    @FXML
    private Spinner<Double> spinnerPrixNuit;
    @FXML
    private Button insert;
    @FXML
    private Button button_Submit;
    @FXML
    private ImageView imageHotel;

    @FXML
    void ajouterHotel(ActionEvent event) {

        String nomHotel = txtNomHotel.getText();
        String adresseHotel = txtAdresseHotel.getText();
        Integer nbrEtoilesValue = spinnerNbreEtoile.getValue();

        if (nbrEtoilesValue == null) {
            showInputError("Veuillez sélectionner un nombre d'étoiles.");
            return;
        }
        String nbrEtoilesString = nbrEtoilesValue.toString();
        String prixNuitValue = spinnerPrixNuit.getValue().toString();

        if (nomHotel.isEmpty() || adresseHotel.isEmpty()) {
            showInputError("Veuillez remplir tous les champs.");
            return;
        }

        if (imageFilePath == null || imageFilePath.isEmpty()) {
            showInputError("Veuillez sélectionner une image.");
            return;
        }

        try {
            int nbreEtoiles = Integer.parseInt(String.valueOf(nbrEtoilesValue));
            float prixNuit = Float.parseFloat(prixNuitValue);

            Hotel hotel = new Hotel(nomHotel, nbreEtoiles, adresseHotel, prixNuit, imageFilePath);
            ServiceHotel serviceHotel = new ServiceHotel();
            serviceHotel.ajouter(hotel);

            showAlert("Hôtel ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Veuillez entrer des valeurs valides pour le nombre d'étoiles et le prix par nuit.");
        } catch (SQLException e) {
            showAlert("Erreur lors de l'ajout de l'hôtel : " + e.getMessage());
        }
    }


    @FXML
    void initialize() {
        txtNomHotel.setTextFormatter(createTextFormatter("[a-zA-Z\\s]+"));
        txtAdresseHotel.setTextFormatter(createTextFormatter("[a-zA-Z\\s]+")); // Autorise également les chiffres dans l'adresse
        spinnerNbreEtoile.getEditor().setTextFormatter(createTextFormatter("[0-9]+"));
        spinnerPrixNuit.getEditor().setTextFormatter(createTextFormatter("[0-9]+(\\.[0-9]+)?"));
        // Récupérer les adresses des hôtels pour les afficher dans la liste
        List<String> hotelAddresses = getAdressesGouvernorats();
        ObservableList<String> observableHotelAddresses = FXCollections.observableArrayList(hotelAddresses);

        // Masquer la liste d'adresses au début
        adresseListView.setVisible(false);

        // Mettre à jour la liste d'adresses en fonction de la saisie de l'utilisateur
        txtAdresseHotel.textProperty().addListener((observable, oldValue, newValue) -> {
            String filter = newValue;
            List<String> filteredList = hotelAddresses.stream()
                    .filter(address -> address.toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
            adresseListView.setItems(FXCollections.observableArrayList(filteredList));
            adresseListView.setVisible(true);
            // Autorise les nombres décimaux
           /* // Vérifier si l'adresse saisie est valide pour un hôtel
            if (!hotelAddresses.contains(filter)) {
                showAlert("Adresse invalide pour un hôtel");
            }*/
        });
        spinnerNbreEtoile.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
        spinnerPrixNuit.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.1));

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
    void insertHotel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image d'hôtel");
        File file = fileChooser.showOpenDialog(imageHotel.getScene().getWindow());

        if (file != null) {
            // Affiche le chemin d'accès sélectionné
            System.out.println("Chemin d'accès à l'image : " + file.getAbsolutePath());

            // Charge l'image et affiche-la dans l'interface utilisateur
            Image image = new Image(file.toURI().toString());
            imageHotel.setImage(image);
            imageFilePath = file.getAbsolutePath();
        }
    }

    private void showInputError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Entrée invalide");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
    @FXML
    void naviguezVersAffichage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherHotel.fxml"));
            txtAdresseHotel.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML
    void onAdresseInputChanged() {
        String input = txtAdresseHotel.getText();
        List<String> filteredAddresses = adressesGouvernorats.stream()
                .filter(address -> address.startsWith(input))
                .collect(Collectors.toList());
        if (filteredAddresses.isEmpty()) {
            adresseListView.setVisible(false);
        } else {
            adresseListView.setItems(FXCollections.observableArrayList(filteredAddresses));
            adresseListView.setVisible(true);
        }
    }

    @FXML
    void onAddressSelected() {
        String selectedAddress = adresseListView.getSelectionModel().getSelectedItem();
        if (selectedAddress != null) {
            txtAdresseHotel.setText(selectedAddress);
            adresseListView.setVisible(false);
        }
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





    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
