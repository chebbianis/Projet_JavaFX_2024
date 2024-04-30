package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
//import java.awt.event.MouseEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Voiture;
import services.VoitureService;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


import javafx.scene.control.Alert;
/*import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;*/


public class AfficherVControllers {

    @FXML
    private TextField txtVoiture_Marque;

    @FXML
    private DatePicker txtAnnee;

    @FXML
    private TextField txtPrix_j;


    @FXML
    private TextField txtId;

    @FXML
    private TextField txtKilometrage;

    @FXML
    private TextField txtNbrPlaces;

    @FXML
    private TableView<Voiture> tableviewVoiture;

    @FXML
    private TableColumn<Voiture, String> colMarque;

    @FXML
    private TableColumn<Voiture, LocalDate> colAnnee;

    @FXML
    private TableColumn<Voiture, Integer> colPrix_j;

    @FXML
    private TableColumn<Voiture, Integer> colKilometrage;

    @FXML
    private TableColumn<Voiture, Integer> colNbrPlaces;

    private VoitureService voitureService;
    private ObservableList<Voiture> voitureList;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;



    private Stage stage;
    private Scene scene;
    private Parent root;





    public void setTxtVoiture_Marque(String txtVoiture_Marque) {
        this.txtVoiture_Marque.setText(txtVoiture_Marque);
    }

    public void setTxtAnnee(LocalDate txtAnnee) {
        this.txtAnnee.setValue(txtAnnee);
    }

    public void setTxtPrix_j(String txtPrix_j) {
        this.txtPrix_j.setText(txtPrix_j);
    }

    public void setTxtKilometrage(String txtKilometrage) {
        this.txtKilometrage.setText(txtKilometrage);
    }

    public void setTxtNbrPlaces(String txtNbrPlaces) {
        this.txtNbrPlaces.setText(txtNbrPlaces);
    }

    public void setTxtId(String txtId) {
        this.txtId.setText(txtId);
    }



    public void goToModification(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierV.fxml"));

        try {
            Parent root = loader.load();
            ModifierVoitureController modifierController = loader.getController();
            modifierController.setTxtAnnee(txtAnnee.getValue());
            modifierController.setTxtVoiture_Marque(txtVoiture_Marque.getText());
            modifierController.setTxtPrix_j(txtPrix_j.getText());
            modifierController.setTxtKilometrage(txtKilometrage.getText());
            modifierController.setTxtNbrPlaceset(txtNbrPlaces.getText());
            modifierController.setTxtId(txtId.getText());

            // Debug statement to check if root is loaded successfully
            System.out.println("FXML loaded successfully.");

            txtAnnee.getScene().setRoot(root);
        } catch (IOException e) {
            // Instead of just printing, handle the IOException
            e.printStackTrace();
        }
    }

    private void selectVoiture(MouseEvent event) {
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
            if (selectedVoiture != null) {
                // Affichage des détails du voyage sélectionné dans le formulaire
                txtVoiture_Marque.setText(selectedVoiture.getMarque());
                txtAnnee.setValue(selectedVoiture.getAnnee());
                txtPrix_j.setText(String.valueOf(selectedVoiture.getPrix_j()));
                txtKilometrage.setText(String.valueOf(selectedVoiture.getKilometrage()));
                txtNbrPlaces.setText(String.valueOf(selectedVoiture.getNbrPlaces()));
            }
        }
    }

    public void initialize() {
        voitureService = new VoitureService();
        setupTableView();
        loadVoitures();
    }

    private void setupTableView() {
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colPrix_j.setCellValueFactory(new PropertyValueFactory<>("prix_j"));
        colKilometrage.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));
        colNbrPlaces.setCellValueFactory(new PropertyValueFactory<>("nbrPlaces"));
    }

    private void loadVoitures() {
        try {
            List<Voiture> voitures = voitureService.recuperer();
            tableviewVoiture.getItems().addAll(voitures);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'erreur
        }
    }


    @FXML
    public void handleRowSelection(MouseEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            deleteButton.setDisable(false); // Enable delete button
            updateButton.setDisable(false); // Enable update button
        } else {
            deleteButton.setDisable(true); // Disable delete button if no row is selected
            updateButton.setDisable(true); // Disable update button if no row is selected
        }
    }

    @FXML
    public void handleDeleteButton(ActionEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            try {
                // Call your delete method with selectedVoiture.getId() as parameter
                voitureService.supprimer(selectedVoiture.getId_voiture());
                // Remove the selected row from the TableView
                tableviewVoiture.getItems().remove(selectedVoiture);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle error
            }
        }
    }

    public void handleUpdateButton(ActionEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            try {
                // Load the ModifierVoiture.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierV.fxml"));
                Parent root = loader.load();

                // Get the controller for ModifierV.fxml
                ModifierVoitureController modifierController = loader.getController();

                // Pass the selected voiture to the controller to pre-fill fields
                modifierController.setTextFields(selectedVoiture);

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Voiture");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle error loading FXML
            }

        }
    }

    @FXML
    private void searchVoitures() {
        String searchString = searchTextField.getText().toLowerCase();

        tableviewVoiture.getItems().clear(); // Clear previous items

        try {
            if (searchString.isEmpty()) {
                // If search text is empty, reload all voitures
                loadVoitures();
            } else {
                List<Voiture> filteredVoitures = voitureService.recuperer().stream()
                        .filter(voiture -> voiture.getMarque().toLowerCase().contains(searchString))
                        .collect(Collectors.toList());

                tableviewVoiture.getItems().addAll(filteredVoitures);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the SQLException
            // Optionally, display an error message to the user
        }
    }

    @FXML
    private void handleSortButtonClick() {
        // Call a method to sort the table
        sortTable();
    }

    private void sortTable() {
        // Perform sorting logic here
        // For example, to sort by the Marque column in ascending order
        colMarque.setSortType(TableColumn.SortType.ASCENDING);
        tableviewVoiture.getSortOrder().clear();
        tableviewVoiture.getSortOrder().add(colMarque);
        tableviewVoiture.sort();
    }

    @FXML
    private void showStatistics() {
        ObservableList<Voiture> voitures = tableviewVoiture.getItems();
        if (!voitures.isEmpty()) {
            // Calculate statistics
            DoubleSummaryStatistics stats = voitures.stream()
                    .mapToDouble(Voiture::getPrix_j)
                    .summaryStatistics();

            // Display statistics in a bar chart
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("Min", stats.getMin()));
            series.getData().add(new XYChart.Data<>("Max", stats.getMax()));
            series.getData().add(new XYChart.Data<>("Average", stats.getAverage()));
            barChart.getData().add(series);

            // Create and show an alert dialog containing the bar chart
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Statistics");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(barChart);
            alert.showAndWait();
        } else {
            // No data to calculate statistics
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Data");
            alert.setHeaderText(null);
            alert.setContentText("No voiture data available.");
            alert.showAndWait();
        }
    }



    /*@FXML
    private void generatePDF() {
        // Créer un sélecteur de fichier pour choisir l'emplacement de sauvegarde du PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                // Créer un nouveau document PDF
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                // Ajouter du texte au document
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Contenu de votre PDF ici...");
                contentStream.endText();
                contentStream.close();

                // Sauvegarder le document PDF
                document.save(file);
                document.close();

                // Afficher une notification de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Généré");
                alert.setHeaderText(null);
                alert.setContentText("Le PDF a été généré avec succès !");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                // Afficher une notification d'erreur en cas d'échec
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la génération du PDF.");
                alert.showAndWait();
            }
        }
    }*/


}
