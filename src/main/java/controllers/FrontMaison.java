package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import entities.Maison;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import services.ServiceMaison;

public class FrontMaison implements Initializable {
    @FXML
    private VBox maisonsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Maison> maisons = getMaisons();
            afficherMaisons(maisons);
        } catch (SQLException e) {

        }
    }

    private List<Maison> getMaisons() throws SQLException {
        ServiceMaison serviceMaison = new ServiceMaison();
        return serviceMaison.afficher();
    }

    private void afficherMaisons(List<Maison> maisons) {
        HBox rowBox = new HBox();
        rowBox.setSpacing(10);

        for (int i = 0; i < maisons.size(); i++) {
            Maison maison = maisons.get(i);
            VBox maisonBox = createMaisonBox(maison);

            rowBox.getChildren().add(maisonBox);
            if ((i + 1) % 4 == 0 || i == maisons.size() - 1) {
                maisonsContainer.getChildren().add(rowBox);
                rowBox = new HBox();
                rowBox.setSpacing(10);
            }
        }
    }

    private VBox createMaisonBox(Maison maison) {
        VBox maisonBox = new VBox();
        maisonBox.setSpacing(5);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
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
