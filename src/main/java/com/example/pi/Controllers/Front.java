package com.example.pi.Controllers;


import com.example.pi.DB.DBUtils;
import com.example.pi.Entities.Maison;
import com.example.pi.Entities.User;
import com.example.pi.Services.ServiceMaison;
import com.example.pi.Services.UserSession;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javafx.scene.control.Alert;

public class Front implements Initializable {

    @FXML
    private VBox homeContainer;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    private List<Maison> maisons;
    private Maison selectedMaison;
    private int rating = 0;
    private final int maxRating = 5;
    @FXML
    private ImageView qrcodeMaison;


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

        System.out.println("Type sélectionné : " + selectedType);
        if ("Tous".equals(selectedType)) {
            maisonsFiltrees = maisons;
        } else {
            for (Maison maison : maisons) {
                if (maison.getType().equals(selectedType)) {
                    maisonsFiltrees.add(maison);
                }
            }
        }

        System.out.println("Maisons filtrées : " + maisonsFiltrees);
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
    private void generateAndDisplayQRCode(String qrData) {
        try {
            // Configuration pour générer le QR code
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Générer le QR code avec ZXing
            BitMatrix matrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 184, 199, hints);
            qrcodeMaison.setFitWidth(100);
            qrcodeMaison.setFitHeight(100);

            // Convertir la matrice en image JavaFX
            Image qrCodeImage = matrixToImage(matrix);

            // Afficher l'image du QR code dans l'ImageView
            qrcodeMaison.setImage(qrCodeImage);
            Alert a = new Alert(Alert.AlertType.WARNING);

            a.setTitle("Succes");
            a.setContentText("qr code generer");
            a.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour convertir une matrice BitMatrix en image BufferedImage
    private Image matrixToImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelColor = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                pixelWriter.setArgb(x, y, pixelColor);
            }
        }

        System.out.println("Matrice convertie en image avec succès");

        return writableImage;
    }


    private RatingDAO ratingDAO = new RatingDAO();

    public VBox createMaisonBox(Maison maison) {
        VBox maisonBox = new VBox();
        maisonBox.setSpacing(5);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);

        String imagePath = maison.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image("file:" + imagePath);
            imageView.setImage(image);
        } else {
            System.out.println("Image path is null or empty.");
        }

        Label nameLabel = new Label(maison.getNom());
        // Label prixLabel = new Label("Prix : " + maison.getPrix());
        Label typeLabel = new Label("Type : " + maison.getType());
        Label adresseLabel = new Label("Adresse : " + maison.getAdresse());
        Button addButton = new Button("Demander une visite ");
        addButton.getStyleClass().add("addbuttonevaluation");
        Button qrCodeButton = new Button("QR code");
        qrCodeButton.getStyleClass().add("addbuttonPanier");

      //  Button ratingButton = new Button("Rating");
         //ratingButton.getStyleClass().add("ratingButton");
       /* List<ImageView> ratingStars = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView();
            star.setFitWidth(30);
            star.setPreserveRatio(true);
            star.setImage(new Image("/com/example/pi/empty_star.png"));
            ratingStars.add(star);
        }

        for (int i = 0; i < ratingStars.size(); i++) {
            int rating = i + 1;
            int finalRating = rating;
            ratingStars.get(i).setOnMouseClicked(event -> {
                ratingDAO.mettreAJourNoteLivreur(maison.getRefB(), finalRating);

                for (int j = 0; j < ratingStars.size(); j++) {
                    if (j < finalRating) {
                        ratingStars.get(j).setImage(new Image("/com/example/pi/star.png"));
                    } else {
                        ratingStars.get(j).setImage(new Image("/com/example/pi/empty_star.png"));
                    }
                }
            });
        }*/

      //  HBox ratingStarsBox = new HBox(ratingStars.toArray(new ImageView[0]));
      //  ratingStarsBox.setAlignment(Pos.CENTER);
       // ratingStarsBox.setSpacing(5);

        maisonBox.getChildren().addAll(imageView, nameLabel, typeLabel, adresseLabel,addButton,qrCodeButton);

       /* IratingButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pi/Rating.fxml"));
                try {
                    Parent root = loader.load();
                    Rating ratingController = loader.getController();
                    ratingController.setMaison(maison);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });*/
        addButton.setOnAction(event -> {
            selectedMaison = maison;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pi/AjouterVisit.fxml"));
                Parent root = loader.load();

                AjouterVisit controller = loader.getController();

                String userEmail = UserSession.getInstance().getEmail();
                controller.getEmailVLabel().setText(userEmail);
                controller.getNomVLabel().setText(DBUtils.getFullNameFromEmail(userEmail));

                controller.getComboVLabel().setText(selectedMaison.getNom());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        qrCodeButton.setOnAction(event ->
        {
            String qrData = "nom: " + maison.getNom() + "\t adresse: " + maison.getAdresse() + "\n nombre_chambre: " + maison.getNombreChambre() + "\t prix: " + maison.getPrix() +"\n type: " + maison.getType()   ;

            generateAndDisplayQRCode(qrData);
        });

        maisonBox.setSpacing(20);
        maisonBox.setAlignment(Pos.CENTER);

        return maisonBox;
    }


}

/*
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
        Button addButton = new Button("Demander une visite ");
        addButton.getStyleClass().add("addbuttonevaluation");
        Button ratingButton = new Button("Rating");
        ratingButton.getStyleClass().add("ratingButton");
        HBox buttonBox = new HBox(addButton, ratingButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        maisonBox.getChildren().addAll(imageView, nameLabel, prixLabel, typeLabel, adresseLabel, addButton,ratingButton);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                selectedMaison = maison;

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterVisit.fxml"));
                    Parent root = loader.load();

                    AjouterVisit controller = loader.getController();

                    User user = new User(1, "Malek", "fffff", "Bdiri", "Malek", "malekbdiri05@gmail.com", "role", "s");
                    controller.getNomVLabel().setText(user.getUsername());
                    controller.getEmailVLabel().setText(user.getEmail());

                    controller.getComboVLabel().setText(selectedMaison.getNom());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ratingButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Rating.fxml"));
                try {
                    Parent root = loader.load();
                    Rating ratingController = loader.getController();
                    ratingController.setMaison(maison);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        maisonBox.setSpacing(20);
        maisonBox.setAlignment(Pos.CENTER);

        return maisonBox;
    }
}
 */
