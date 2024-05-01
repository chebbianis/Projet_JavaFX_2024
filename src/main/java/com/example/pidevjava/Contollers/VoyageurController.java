package com.example.pidevjava.Contollers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.example.pidevjava.Entities.Voyageur;
import java.io.IOException;
import java.awt.Color;
import java.util.*;

import com.example.pidevjava.Entities.Voyageur;
import com.example.pidevjava.service.serviceVoyageur;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class VoyageurController {

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_insert;

    @FXML
    private Button btn_update;

    @FXML
    private Button btn_stat;

    @FXML
    private Button bn_pdf;



    @FXML
    private TableColumn<Voyageur, Integer> col_age;

    @FXML
    private TableColumn<Voyageur, String> col_etat;

    @FXML
    private TableColumn<Voyageur, Integer> col_idv;

    @FXML
    private TableColumn<Voyageur, String> col_nom;

    @FXML
    private TableColumn<Voyageur, String> col_email;


    @FXML
    private TableColumn<Voyageur, Integer> col_pass;

    @FXML
    private TableColumn<Voyageur, String> col_prenom;

    @FXML
    private TableView<Voyageur> table_voyageur;

    @FXML
    private TextField tf_age;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_etat;

    @FXML
    private TextField tf_nom;

    @FXML
    private TextField tf_num;

    @FXML
    private TextField tf_prenom;

    @FXML
    private Label tr_age;

    @FXML
    private Label tr_email;

    @FXML
    private Label tr_etat;

    @FXML
    private Label tr_prenom;


    @FXML
    private TextField rechercheField;

    @FXML
    private Button btn_ret;


    @FXML
    void onRechercheFieldTextChanged(KeyEvent event) {
        String rechercheText = rechercheField.getText().trim();
        rechercherVoyageur(rechercheText);
    }
   /* @FXML
    void handleGeneratePDF(ActionEvent event) {
        // Récupérer la liste des voyageurs (supposons qu'elle soit déjà remplie)
        List<Voyageur> voyageurs = null;

        // Définir le chemin de sortie du fichier PDF
        String outputPath = "liste_voyageurs.pdf";

        // Générer le PDF en utilisant PDFGenerator
        try {
            VoyageurController.generatePDF(voyageurs, outputPath);
            System.out.println("PDF généré avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }*/

    private serviceVoyageur service = new serviceVoyageur();
    /*ObservableList<String> items = FXCollections.observableArrayList(
            "Single", "Married", "Divorced", "Widowed");
    ListView<String> listViewEtat = new ListView<>(items);*/

    public void initialize() {
        // Initialisation des colonnes
        col_idv.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_pass.setCellValueFactory(new PropertyValueFactory<>("num_pass"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_etat.setCellValueFactory(new PropertyValueFactory<>("etat_civil"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        List<String> etat = getAdressesGouvernorats();




        loadVoyageurData(); // Chargement des données dans la TableView

        // Ajout de l'événement pour sélectionner un voyageur dans le formulaire
        table_voyageur.setOnMouseClicked(event -> selectVoyageur(event));

        btn_delete.setOnAction(event -> supprimerVoyageur());
        btn_update.setOnAction(event -> modifierVoyageur());
        btn_insert.setOnAction(event -> ajouterVoyageur());
        bn_pdf.setOnAction(event -> {
            List<Voyageur> voyageurs = getVoyageurList();
            String fileName = "C:\\Users\\iheba\\Downloads\\Voyageur.pdf";
            generatePDF(voyageurs, fileName);
        });

        btn_stat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/EtatCivil.fxml"));
                    Parent root = fxmlLoader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the stage from the button's scene
                    Stage stage = (Stage) btn_stat.getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_ret.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/hello-view.fxml"));
                    Parent root = fxmlLoader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the stage from the button's scene
                    Stage stage = (Stage) btn_ret.getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidAge(String age) {
        return age.matches("\\d{2}");
    }

    private boolean validateFields() {
        return !tf_num.getText().isEmpty() &&
                tf_num.getText().length() == 7 &&
                !tf_nom.getText().isEmpty() && tf_nom.getText().length() <= 50 &&
                !tf_prenom.getText().isEmpty() && tf_prenom.getText().length() <= 50 &&
                !tf_age.getText().isEmpty() && isValidAge(tf_age.getText()) &&
                !tf_etat.getText().isEmpty() && tf_etat.getText().length() <= 20 &&
                !tf_email.getText().isEmpty() && isValidEmail(tf_email.getText());
    }

    private void ajouterVoyageur() {

        if (!validateFields()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs correctement.");
            alert.showAndWait();
            return;
        }


        Voyageur newVoyageur = new Voyageur(
                Integer.parseInt(tf_num.getText()),
                tf_nom.getText(),
                tf_prenom.getText(),
                Integer.parseInt(tf_age.getText()),
                tf_etat.getText(),

        tf_email.getText()
        );

        try {
            service.ajouter(newVoyageur);
            loadVoyageurData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifierVoyageur() {
        Voyageur selectedVoyageur = table_voyageur.getSelectionModel().getSelectedItem();
        if (selectedVoyageur != null) {

            selectedVoyageur.setNum_pass(Integer.parseInt(tf_num.getText()));
            selectedVoyageur.setNom(tf_nom.getText());
            selectedVoyageur.setPrenom(tf_prenom.getText());
            selectedVoyageur.setAge(Integer.parseInt(tf_age.getText()));
            selectedVoyageur.setEtat_civil(tf_etat.getText());
            selectedVoyageur.setEmail(tf_email.getText());

            try {
                service.modifier(selectedVoyageur); // Appel de la méthode de service pour modifier le voyageur
                loadVoyageurData(); // Recharger les données après la modification
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer l'exception de manière appropriée (affichage ou journalisation)
            }
        } else {
            // Afficher un message à l'utilisateur indiquant de sélectionner un voyageur à modifier
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un voyageur à modifier.");
            alert.showAndWait();
        }
    }

    private void supprimerVoyageur() {
        Voyageur selectedVoyageur = table_voyageur.getSelectionModel().getSelectedItem();
        if (selectedVoyageur != null) {
            // Demander confirmation avant la suppression
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer ce voyageur?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    service.supprimer(selectedVoyageur); // Appel de la méthode de service pour supprimer le voyageur
                    loadVoyageurData(); // Recharger les données après la suppression
                } catch (SQLException e) {
                    e.printStackTrace(); // Gérer l'exception de manière appropriée (affichage ou journalisation)
                }
            }
        } else {
            // Afficher un message à l'utilisateur indiquant de sélectionner un voyageur à supprimer
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un voyageur à supprimer.");
            alert.showAndWait();
        }
    }

    private ObservableList<Voyageur> getVoyageurList() {
        ObservableList<Voyageur> voyageurs = FXCollections.observableArrayList();
        try {
            voyageurs.addAll(service.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyageurs;
    }

    private void loadVoyageurData() {
        ObservableList<Voyageur> voyageurs = getVoyageurList();
        table_voyageur.setItems(voyageurs);
    }

    private void selectVoyageur(MouseEvent event) {
        // Implémentez la logique pour sélectionner un voyageur dans la TableView
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Voyageur selectedVoyageur = table_voyageur.getSelectionModel().getSelectedItem();
            if (selectedVoyageur != null) {
                // Affichage des détails du voyageur sélectionné dans les labels
                tf_num.setText(String.valueOf(selectedVoyageur.getNum_pass()));
                tf_nom.setText(selectedVoyageur.getNom());
                tf_prenom.setText(selectedVoyageur.getPrenom());
                tf_age.setText(String.valueOf(selectedVoyageur.getAge()));
                tf_etat.setText(selectedVoyageur.getEtat_civil());
                tf_email.setText(selectedVoyageur.getEmail());
            }
        }
    }

    private void rechercherVoyageur(String rechercheText) {
        serviceVoyageur voyageurService = new serviceVoyageur();
        try {
            List<Voyageur> Voyagurs = voyageurService.rechercher(rechercheText);

            ObservableList<Voyageur> observableList = FXCollections.observableList(Voyagurs);
            table_voyageur.setItems(observableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public static void generatePDF(List<Voyageur> voyageurs, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.RED);
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("LISTES DES VOYAGEURS ") / 1000f * 16;
                float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000f * 16;
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText("LISTES DES VOYAGEURS");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin - 30 - titleHeight;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;

                // Define column widths and headers
                String[] headers = {"ID", "Nom", "Prenom", "Numéro Passeport", "Age", "Email", "Etat Civil"};
                float[] columnWidths = {50, 150, 100, 100, 50, 100, 100}; // Adjust these values as needed

                // Draw table headers
                drawTableRow(contentStream, yPosition, margin, tableWidth, headers, columnWidths);
                yPosition -= rowHeight;

                // Draw table data
                for (Voyageur voyageur : voyageurs) {
                    String[] rowData = {
                            Integer.toString(voyageur.getId()),
                            voyageur.getNom(),
                            voyageur.getPrenom(),
                            Integer.toString(voyageur.getNum_pass()),
                            Integer.toString(voyageur.getAge()),
                            voyageur.getEmail(),
                            voyageur.getEtat_civil()
                    };
                    drawTableRow(contentStream, yPosition, margin, tableWidth, rowData, columnWidths);
                    yPosition -= rowHeight;
                }
            }

            // Save the PDF document
            document.save(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void drawTableRow(PDPageContentStream contentStream, float yPosition, float margin, float tableWidth, String[] rowData, float[] columnWidths) throws IOException {
        float xPosition = margin;
        for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(xPosition, yPosition);
            contentStream.showText(rowData[i]);
            contentStream.endText();
            xPosition += columnWidths[i];
        }
    }
   /* void onAddressSelected() {
        String selectedAddress = listViewEtat.getSelectionModel().getSelectedItem();
        if (selectedAddress != null) {
            tf_etat.setText(selectedAddress);
            listViewEtat.setVisible(false);
        }*/


    public List<String> getAdressesGouvernorats() {
        List<String> adressesGouvernorats = new ArrayList<>();
        adressesGouvernorats.add("Single");
        adressesGouvernorats.add("Married");
        adressesGouvernorats.add("Divorced");
        adressesGouvernorats.add("Widowed");
        return adressesGouvernorats;
    }
}




