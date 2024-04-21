package controllers;

import entities.Maison;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceMaison;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Front implements Initializable {

    @FXML
    private VBox homeContainer;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    private List<Maison> maisons;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> types = FXCollections.observableArrayList("Tous", "Villa", "Appartement", "Studio");
        typeFilterComboBox.setItems(types);
        typeFilterComboBox.setValue("Tous");
        try {
            maisons = getMaisons();
            afficherMaisons(maisons);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void filtrerParType(ActionEvent event) {
        String selectedType = typeFilterComboBox.getValue();
        List<Maison> maisonsFiltrees = new ArrayList<>();

        System.out.println("Type sélectionné : " + selectedType); // Afficher le type sélectionné

        if ("Tous".equals(selectedType)) {
            // Afficher toutes les maisons si "Tous" est sélectionné
            maisonsFiltrees = maisons;
        } else {
            // Filtrer les maisons par type sélectionné
            for (Maison maison : maisons) {
                if (maison.getType().equals(selectedType)) {
                    maisonsFiltrees.add(maison);
                }
            }
        }

        System.out.println("Maisons filtrées : " + maisonsFiltrees); // Afficher les maisons filtrées

        // Afficher les maisons filtrées
        afficherMaisons(maisonsFiltrees);
    }

    private List<Maison> getMaisons() throws SQLException {
        ServiceMaison serviceMaison = new ServiceMaison();
        return serviceMaison.afficher();
    }

    private void afficherMaisons(List<Maison> maisons) {
        homeContainer.getChildren().clear();

        HBox rowBox = new HBox();
        rowBox.setSpacing(10);

        for (int i = 0; i < maisons.size(); i++) {
            Maison maison = maisons.get(i);
            VBox maisonBox = createMaisonBox(maison);

            rowBox.getChildren().add(maisonBox);
            if ((i + 1) % 4 == 0 || i == maisons.size() - 1) {
                homeContainer.getChildren().add(rowBox);
                rowBox = new HBox();
                rowBox.setSpacing(10);
            }
        }
    }

    public VBox createMaisonBox(Maison maison) {
        VBox maisonBox = new VBox();
        maisonBox.setSpacing(5);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        String imagePath = maison.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image("file:" + imagePath);
            imageView.setImage(image);
        } else {
            System.out.println("Image path is null or empty.");
        }


        Label nameLabel = new Label(maison.getNom());
        Label prixLabel = new Label("Prix : " + maison.getPrix());
        Label typeLabel = new Label("Type : " + maison.getType());
        Label adresseLabel = new Label("Adresse : " + maison.getAdresse());


        maisonBox.getChildren().addAll(imageView, nameLabel, prixLabel, typeLabel, adresseLabel);

        return maisonBox;
    }

}