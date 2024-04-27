package controllers;

import entities.Maison;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import services.ServiceMaison;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherMaison {
    @FXML
    private TableColumn<?, ?> adresseCol;

    @FXML
    private TextField adresseModif;

    @FXML
    private TableColumn<?, ?> nomCol;

    @FXML
    private TextField nomModif;

    @FXML
    private TableColumn<?, ?> nombreChambreCol;

    @FXML
    private TextField nombreChambreModif;

    @FXML
    private TableColumn<?, ?> prixCol;

    @FXML
    private TextField prixModif;

    @FXML
    private TableColumn<?, ?> refCol;

    @FXML
    private TableView<Maison> tableView;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private ComboBox<String> typeModif;
    @FXML
    private Button btnModifier;
    @FXML
    private TextField rechercheField;
    @FXML
    private Button btnSupprimer;
    @FXML
    private TableColumn<?, ?> imageCol;
    @FXML
    private ImageView imageModif;
    private String image;
    @FXML
    private Button retour;
    @FXML
    private Button insert;
    @FXML
    private Text cheminImage;
    @FXML
    private TextField montantTNDField; // Champ de texte pour saisir le montant en TND

    @FXML
    private TextField montantEURField; // Champ de texte pour afficher le montant converti en EUR
    @FXML
    private Button convertirTNDenEUR;

    private double tauxChangeTND_EUR = 0.3;

    @FXML
    void insertImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de maison");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers image (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(btnModifier.getScene().getWindow());
        if (file != null) {
            Image image1 = new Image(file.toURI().toString());
            imageModif.setImage(image1);
            cheminImage.setText(file.getAbsolutePath());
            image = file.getAbsolutePath();
         /*   Image image1 = new Image(file.toURI().toString());
            imageModif.setImage(image1);
            String fileName = file.getName();
           // cheminImage.setText(fileName);
            image = fileName;*/
        }
    }
    @FXML
    void retourPage (ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AjouterMaison.fxml"));
        try {
            nomModif.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void getData(MouseEvent event) {
        Maison maison =tableView.getSelectionModel().getSelectedItem();

        nomModif.setText(maison.getNom());
        adresseModif.setText(maison.getAdresse());
        nombreChambreModif.setText(String.valueOf(maison.getNombreChambre()));
        prixModif.setText(String.valueOf(maison.getPrix()));
        typeModif.setValue(maison.getType());
        image = maison.getImage();
        double prixTND = maison.getPrix();
        double prixEUR = convertirTNDenEUR(prixTND, tauxChangeTND_EUR);
        montantEURField.setText(String.valueOf(prixEUR));
        if (maison.getImage() != null && !maison.getImage().isEmpty()) {
            File file = new File(maison.getImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageModif.setImage(image);
            }
        }
    }


    @FXML
    void modifierMaison(ActionEvent event) {
        Maison maison = tableView.getSelectionModel().getSelectedItem();
        if (maison != null) {
            // Retrieve values from text fields
            String nom = nomModif.getText();
            String adresse = adresseModif.getText();
            int nombreChambre = Integer.parseInt(nombreChambreModif.getText());
            int prix = Integer.parseInt(prixModif.getText());
            String type = typeModif.getValue();
            //  String image = this.image;

            maison.setNom(nom);
            maison.setAdresse(adresse);
            maison.setNombreChambre(nombreChambre);
            maison.setPrix(prix);
            maison.setType(type);
            if (image != null && !image.isEmpty()) {
                maison.setImage(image);
            }

            ServiceMaison serviceMaison = new ServiceMaison();
            try {
                serviceMaison.modifier(maison);
                clear();
                initialize();

                Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setContentText("Maison modifiée");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Maison Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Maison to modify.");
            alert.showAndWait();
        }
    }

    @FXML
    void supprimerMaison(ActionEvent event) {
        Maison maison = tableView.getSelectionModel().getSelectedItem();
        if (maison != null) {
            ServiceMaison serviceMaison = new ServiceMaison();
            try {
                serviceMaison.supprimer(maison);
                tableView.getItems().remove(maison);
                clear();
                serviceMaison.afficher();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("La maison a été supprimée avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur lors de la suppression");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la suppression de la maison : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune maison sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une maison.");
            alert.showAndWait();
        }
    }
    void clear (){
        nomModif.setText(null);
        adresseModif.setText(null);
        nombreChambreModif.setText(null);
        prixModif.setText(null);
        typeModif.setValue(null);
        imageModif.setImage(null);
        // btnModif.setDisable(false);
    }
    private void rechercherMaison(String rechercheText) {
        ServiceMaison serviceMaison = new ServiceMaison();
        try {
            List<Maison> maisons = serviceMaison.rechercher(rechercheText); // Remplacez cette ligne avec votre méthode de recherche personnalisée

            ObservableList<Maison> observableList = FXCollections.observableList(maisons);
            tableView.setItems(observableList);
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
    private double convertirTNDenEUR(double montantTND, double tauxChange) {
        return montantTND * tauxChange;
    }
    @FXML
    void convertirTNDenEUR(ActionEvent event) {
        String montantTNDText = montantTNDField.getText();
        if (!montantTNDText.isEmpty()) {
            double montantTND = Double.parseDouble(montantTNDText);
            double montantEUR = convertirTNDenEUR(montantTND, tauxChangeTND_EUR);
            montantEURField.setText(String.valueOf(montantEUR));
        } else {
            // Gérer le cas où aucun montant n'est saisi
            // Afficher un message d'erreur, par exemple
            System.out.println("Veuillez saisir un montant en TND.");
        }}

    @FXML
    void initialize() {
        rechercherMaison("");
        ServiceMaison serviceMaison = new ServiceMaison();
        try {
            List<Maison> maison = serviceMaison.afficher();
            ObservableList<Maison> observableList = FXCollections.observableList(maison);
            tableView.setItems(observableList);

            refCol.setCellValueFactory(new PropertyValueFactory<>("refB"));
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            nombreChambreCol.setCellValueFactory(new PropertyValueFactory<>("nombreChambre"));
            prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Charger les types de maisons dans le ComboBox
            ObservableList<String> types = FXCollections.observableArrayList("Villa", "Appartement", "Studio");
            typeModif.setItems(types);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}



/*
assert adresseCol != null : "fx:id=\"adresseCol\" was not injected: check your FXML file 'AfficherMaison.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'AfficherMaison.fxml'.";
        assert nomCol != null : "fx:id=\"nomCol\" was not injected: check your FXML file 'AfficherMaison.fxml'.";
        assert nombre_chambreCol != null : "fx:id=\"nombre_chambreCol\" was not injected: check your FXML file 'AfficherMaison.fxml'.";
        assert prixCol != null : "fx:id=\"prixCol\" was not injected: check your FXML file 'AfficherMaison.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'AfficherMaison.fxml'.";
        assert typeCol != null : "fx:id=\"typeCol\" was not injected: check your FXML file 'AfficherMaison.fxml'.";
*/
